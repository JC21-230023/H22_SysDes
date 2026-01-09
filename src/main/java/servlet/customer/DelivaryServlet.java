package servlet.customer;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sql_temp.ConnectSQL;

@WebServlet("/delivaryform")
public class DelivaryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // =============================
        // ① フォーム入力取得
        // =============================
        String postCode = request.getParameter("zipcode");
        String address  = request.getParameter("address");
        String name     = request.getParameter("name");
        String tel      = request.getParameter("tel");

        int furnitureCount = Integer.parseInt(request.getParameter("furnitureCount"));

        // 家具番号取得
        List<String> furnitureCodes = new ArrayList<>();
        for (int i = 1; i <= furnitureCount; i++) {
            String code = request.getParameter("furniture" + i);
            if (code != null && !code.trim().isEmpty()) {
                furnitureCodes.add(code.trim());
            }
        }

        // =============================
        // 入力数チェック（未入力対策）
        // =============================
        if (furnitureCodes.size() != furnitureCount) {
            request.setAttribute("errorMessage", "家具番号が未入力の項目があります。");
            request.setAttribute("zipcode", postCode);
            request.setAttribute("address", address);
            request.setAttribute("name", name);
            request.setAttribute("tel", tel);
            request.setAttribute("furnitureCount", furnitureCount);
            request.setAttribute("furnitureCodes", furnitureCodes);

            request.getRequestDispatcher("/delivaryform.jsp")
                   .forward(request, response);
            return;
        }

        // =============================
        // ② 家具番号 → 家具名（存在チェック）
        // =============================
        List<Map<String,String>> furnitureList = new ArrayList<>();
        List<String> errorFurniture = new ArrayList<>();

        try {
            for (String code : furnitureCodes) {
                PreparedStatement ps =
                    ConnectSQL.getSt("SELECT FURN_NAME FROM FURN WHERE FURN_CODE = ?");
                ps.setString(1, code);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    Map<String,String> map = new HashMap<>();
                    map.put("code", code);
                    map.put("name", rs.getString("FURN_NAME"));
                    furnitureList.add(map);
                } else {
                    errorFurniture.add(code);
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }

        if (!errorFurniture.isEmpty()) {
            request.setAttribute("errorMessage",
                "存在しない家具番号があります： " + String.join(", ", errorFurniture));
            request.setAttribute("zipcode", postCode);
            request.setAttribute("address", address);
            request.setAttribute("name", name);
            request.setAttribute("tel", tel);
            request.setAttribute("furnitureCount", furnitureCount);
            request.setAttribute("furnitureCodes", furnitureCodes);

            request.getRequestDispatcher("/delivaryform.jsp")
                   .forward(request, response);
            return;
        }

        // =============================
        // ③ 郵便番号 → 配達業者検索（DATE_CONTRA件数最大）
        // =============================
        String contraCode = null;
        String contraName = null;

        String sqlContra =
            "SELECT C.CONTRA_CODE, C.CONTRA_NAME, COUNT(D.AVAIL_DEL_DATETIME) AS DATE_COUNT " +
            "FROM POSTCODE P " +
            "JOIN CONTRA C ON P.DELAREA_CODE = C.DELAREA_CODE " +
            "LEFT JOIN DATE_CONTRA D ON C.CONTRA_CODE = D.CONTRA_CODE " +
            "WHERE P.POST_CODE LIKE ? " +
            "GROUP BY C.CONTRA_CODE, C.CONTRA_NAME " +
            "ORDER BY DATE_COUNT DESC";

        try {
            PreparedStatement ps = ConnectSQL.getSt(sqlContra);
            ps.setString(1, postCode.substring(0, 3) + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                contraCode = rs.getString("CONTRA_CODE");
                contraName = rs.getString("CONTRA_NAME");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }

        if (contraCode == null) {
            request.setAttribute("errorMessage", "この郵便番号では配達可能な業者が見つかりません。");
            request.setAttribute("zipcode", postCode);
            request.setAttribute("address", address);
            request.setAttribute("name", name);
            request.setAttribute("tel", tel);
            request.setAttribute("furnitureCount", furnitureCount);
            request.setAttribute("furnitureCodes", furnitureCodes);
            request.setAttribute("furnitureList", furnitureList);

            request.getRequestDispatcher("/delivaryform.jsp")
                   .forward(request, response);
            return;
        }

        // =============================
        // ④ 配達可能日取得（選択された業者）
        // =============================
        List<String> availableDateList = new ArrayList<>();
        String sqlDate =
        	    "SELECT DISTINCT TO_CHAR(AVAIL_DEL_DATETIME,'YYYY-MM-DD') AS AVAIL_DATE " +
        	    "FROM DATE_CONTRA " +
        	    "WHERE CONTRA_CODE = ? " +
        	    "  AND MAX_DELIVERY_COUNT >= ? " +
        	    "  AND AVAIL_DEL_DATETIME >= TRUNC(SYSDATE) + 2 " +
        	    "ORDER BY AVAIL_DATE";

        try {
            PreparedStatement ps = ConnectSQL.getSt(sqlDate);
            ps.setString(1, contraCode);
            ps.setInt(2, furnitureCount);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                availableDateList.add(rs.getString("AVAIL_DATE"));
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }

        // =============================
        // ⑤ JSPへ渡す
        // =============================
        request.setAttribute("postCode", postCode);
        request.setAttribute("address", address);
        request.setAttribute("name", name);
        request.setAttribute("phoneNum", tel);
        request.setAttribute("furnitureCount", furnitureCount);
        request.setAttribute("furnitureList", furnitureList);
        request.setAttribute("contraCode", contraCode);
        request.setAttribute("contraName", contraName);
        request.setAttribute("availableDateList", availableDateList);

        RequestDispatcher rd =
            request.getRequestDispatcher("/delivary.jsp");
        rd.forward(request, response);
    }
}

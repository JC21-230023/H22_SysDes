package servlet.deliver;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

@WebServlet("/search")
public class ReserveDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // ==============================
        // ① 予約番号取得
        // ==============================
        String reservCode = request.getParameter("reservationCode");

        // ==============================
        // ② 予約＋受付＋業者情報取得
        // ==============================
        String orderNum = null;
        String postCode = null;
        String address  = null;
        String name     = null;
        String phoneNum = null;
        String contraName = null;
        LocalDateTime deliveryDatetime = null; // 配達予定日時

        String sql =
            "SELECT O.ORDER_NUM, O.POST_CODE, O.ADDRESS, O.NAME, O.PHONE_NUM, C.CONTRA_NAME, R.DELIVERY_DATETIME " +
            "FROM RESERV R " +
            "JOIN ORDERS O ON R.ORDER_NUM = O.ORDER_NUM " +
            "JOIN CONTRA C ON R.CONTRA_CODE = C.CONTRA_CODE " +
            "WHERE R.RESERV_CODE = ?";

        try {
            PreparedStatement ps = ConnectSQL.getSt(sql);
            ps.setString(1, reservCode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                orderNum        = rs.getString("ORDER_NUM");
                postCode        = rs.getString("POST_CODE");
                address         = rs.getString("ADDRESS");
                name            = rs.getString("NAME");
                phoneNum        = rs.getString("PHONE_NUM");
                contraName      = rs.getString("CONTRA_NAME");
                deliveryDatetime = rs.getTimestamp("DELIVERY_DATETIME").toLocalDateTime();
            } else {
                // 予約番号が存在しない
                request.setAttribute("errorMessage", "入力された予約番号は存在しません。");
                request.getRequestDispatcher("/reservesearch.jsp").forward(request, response);
                return;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }

        // ==============================
        // 配達日チェック
        // 過去日および2日以内のキャンセル不可
        // ==============================
        LocalDateTime now = LocalDateTime.now();
        long daysBetween = ChronoUnit.DAYS.between(now.toLocalDate(), deliveryDatetime.toLocalDate());

        if (deliveryDatetime.isBefore(now)) {
            request.setAttribute("errorMessage", "過去の予約番号です。");
            request.getRequestDispatcher("/reservesearch.jsp").forward(request, response);
            return;
        }

        if (daysBetween < 2) {
            request.setAttribute("errorMessage", "配達日が2日以内のためキャンセルできません。");
            request.getRequestDispatcher("/reservesearch.jsp").forward(request, response);
            return;
        }

        // ==============================
        // ③ 家具情報取得
        // ==============================
        List<Map<String,String>> furnList = new ArrayList<>();

        String sqlFurn =
            "SELECT F.FURN_CODE, F.FURN_NAME " +
            "FROM ORDER_FURN OFU " +
            "JOIN FURN F ON OFU.FURN_CODE = F.FURN_CODE " +
            "WHERE OFU.ORDER_NUM = ?";

        try {
            PreparedStatement ps = ConnectSQL.getSt(sqlFurn);
            ps.setString(1, orderNum);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String,String> map = new HashMap<>();
                map.put("code", rs.getString("FURN_CODE"));
                map.put("name", rs.getString("FURN_NAME"));
                furnList.add(map);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }

        // ==============================
        // ④ JSPへ引き渡し
        // ==============================
        request.setAttribute("reservCode", reservCode);
        request.setAttribute("postCode", postCode);
        request.setAttribute("address", address);
        request.setAttribute("name", name);
        request.setAttribute("phoneNum", phoneNum);
        request.setAttribute("contraName", contraName);
        request.setAttribute("deliveryDatetime", deliveryDatetime);
        request.setAttribute("furnQuantity", furnList.size());
        request.setAttribute("furnList", furnList);

        // ==============================
        // ⑤ 確認画面へ遷移
        // ==============================
        RequestDispatcher rd = request.getRequestDispatcher("/reserveselect.jsp");
        rd.forward(request, response);
    }
}

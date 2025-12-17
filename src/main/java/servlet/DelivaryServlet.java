package servlet;

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

        // -----------------------------
        // ① フォーム入力取得
        // -----------------------------
        String postCode = request.getParameter("zipcode");
        String address  = request.getParameter("address");
        String name     = request.getParameter("name");
        String tel      = request.getParameter("tel");

        int furnitureCount = Integer.parseInt(request.getParameter("furnitureCount"));

        // 家具番号リスト
        List<String> furnitureCodes = new ArrayList<>();
        for (int i = 1; i <= furnitureCount; i++) {
            String code = request.getParameter("furniture" + i);
            if (code != null && !code.isEmpty()) {
                furnitureCodes.add(code);
            }
        }

     // -----------------------------
     // ② 家具番号 → 家具名（存在チェック付き）
     // -----------------------------
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
                 map.put("code", code);                     // 登録用
                 map.put("name", rs.getString("FURN_NAME"));// 表示用
                 furnitureList.add(map);
             } else {
                 // 家具テーブルに存在しない番号
                 errorFurniture.add(code);
             }
         }
     } catch (Exception e) {
         e.printStackTrace();
     }

  // -----------------------------
  // 家具番号エラーがあればフォームに戻す
  // -----------------------------
  if (!errorFurniture.isEmpty()
      || furnitureList.size() != furnitureCount) {

      request.setAttribute(
          "errorMessage",
          "存在しない家具番号があります： " + String.join(", ", errorFurniture)
      );

      // 入力値を戻す
      request.setAttribute("zipcode", postCode);
      request.setAttribute("address", address);
      request.setAttribute("name", name);
      request.setAttribute("tel", tel);
      request.setAttribute("furnitureCount", furnitureCount);
      request.setAttribute("furnitureCodes", furnitureCodes);

      RequestDispatcher rd =
          request.getRequestDispatcher("/delivaryform.jsp");
      rd.forward(request, response);
      return;
  }


//-----------------------------
//③ 郵便番号上3桁 → 業者検索
//-----------------------------
String contraCode = null;
String contraName = null;

String sqlContra =
   "SELECT C.CONTRA_CODE, C.CONTRA_NAME " +
   "FROM POSTCODE P " +
   "JOIN CONTRA C ON P.DELAREA_CODE = C.DELAREA_CODE " +
   "WHERE P.POST_CODE LIKE ?";

try {
   PreparedStatement ps = ConnectSQL.getSt(sqlContra);
   ps.setString(1, postCode.substring(0,3) + "%");
   ResultSet rs = ps.executeQuery();
   if (rs.next()) {
       contraCode = rs.getString("CONTRA_CODE");
       contraName = rs.getString("CONTRA_NAME");
   }
} catch (Exception e) {
   e.printStackTrace();
}

/* ===== ここを追加 ===== */
if (contraCode == null) {

   request.setAttribute(
       "errorMessage",
       "この郵便番号では配達可能な業者が見つかりません。"
   );

   // 入力値を戻す
   request.setAttribute("zipcode", postCode);
   request.setAttribute("address", address);
   request.setAttribute("name", name);
   request.setAttribute("tel", tel);
   request.setAttribute("furnitureCount", furnitureCount);

   // 家具番号も戻す（重要）
   request.setAttribute("furnitureCodes", furnitureCodes);

   RequestDispatcher rd =
       request.getRequestDispatcher("/delivaryform.jsp");
   rd.forward(request, response);
   return;
}


        // -----------------------------
        // ④ 配達可能日取得
        // -----------------------------
        List<String> availableDateList = new ArrayList<>();
        if (contraCode != null) {
            String sqlDate =
                "SELECT DISTINCT TO_CHAR(AVAIL_DEL_DATETIME,'YYYY-MM-DD') AS AVAIL_DATE " +
                "FROM DATE_CONTRA " +
                "WHERE CONTRA_CODE = ? AND MAX_DELIVERY_COUNT >= ? " +
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
                e.printStackTrace();
            }
        }

        // -----------------------------
        // ⑤ JSPへ渡す
        // -----------------------------
        request.setAttribute("postCode", postCode);
        request.setAttribute("address", address);
        request.setAttribute("name", name);
        request.setAttribute("phoneNum", tel);

        request.setAttribute("furnitureCount", furnitureCount);
        request.setAttribute("furnitureList", furnitureList);

        request.setAttribute("contraCode", contraCode);   // hiddenで送る
        request.setAttribute("contraName", contraName);
        request.setAttribute("availableDateList", availableDateList);

        // -----------------------------
        // ⑥ JSPへ画面遷移
        // -----------------------------
        RequestDispatcher rd = request.getRequestDispatcher("/delivary.jsp");
        rd.forward(request, response);
    }
}

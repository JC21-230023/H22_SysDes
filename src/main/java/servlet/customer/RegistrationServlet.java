package servlet.customer;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sql_temp.ConnectSQL;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        /* =========================
         * ① 共通入力値取得
         * ========================= */
        String postCode = request.getParameter("postCode");
        String address  = request.getParameter("address");
        String name     = request.getParameter("name");
        String phoneNum = request.getParameter("phoneNum");

        String hopeDate = request.getParameter("hopeDate");
        String hopeTime = request.getParameter("hopeTime");
        String contraCode = request.getParameter("contraCode");

        int furnitureCount =
            Integer.parseInt(request.getParameter("furnitureCount"));

        /* =========================
         * ② 家具情報取得（番号＋名前）
         * ========================= */
        int furnitureSize =
            Integer.parseInt(request.getParameter("furnitureSize"));

        List<Map<String,String>> furnitureList = new ArrayList<>();

        for (int i = 0; i < furnitureSize; i++) {
            String code = request.getParameter("furnitureCode" + i);
            String fname = request.getParameter("furnitureName" + i);

            if (code != null && fname != null) {
                Map<String,String> map = new HashMap<>();
                map.put("code", code);
                map.put("name", fname);
                furnitureList.add(map);
            }
        }

        String orderNum  = null;
        String reservCode = null;

        try {
            /* =========================
             * ③ ORDER 登録
             * ========================= */
            orderNum = generateOrderNum();

            String orderSql =
                "INSERT INTO ORDERS " +
                "(ORDER_NUM, RECEPTION_DATE, POST_CODE, ADDRESS, NAME, PHONE_NUM, FURN_QUANTITY) " +
                "VALUES (?, SYSDATE, ?, ?, ?, ?, ?)";

            PreparedStatement orderSt = ConnectSQL.getSt(orderSql);
            orderSt.setString(1, orderNum);
            orderSt.setString(2, postCode);
            orderSt.setString(3, address);
            orderSt.setString(4, name);
            orderSt.setString(5, phoneNum);
            orderSt.setInt(6, furnitureCount);
            orderSt.executeUpdate();

            /* =========================
             * ④ ORDER_FURN 登録
             * ========================= */
            String furnSql =
                "INSERT INTO ORDER_FURN (ORDER_NUM, FURN_CODE) VALUES (?, ?)";

            for (Map<String,String> f : furnitureList) {
                PreparedStatement furnSt = ConnectSQL.getSt(furnSql);
                furnSt.setString(1, orderNum);
                furnSt.setString(2, f.get("code"));
                furnSt.executeUpdate();
            }

            /* =========================
             * ⑤ RESERV 登録
             * ========================= */
            reservCode = generateReservCode();

            String deliveryDatetime =
                hopeDate + (hopeTime.equals("AM") ? " 09" : " 13");

            String reservSql =
                "INSERT INTO RESERV " +
                "(RESERV_CODE, CONTRA_CODE, DELIVERY_DATETIME, ORDER_NUM, DEL_STATUS, APPROVAL) " +
                "VALUES (?, ?, TO_DATE(?, 'YYYY-MM-DD HH24'), ?, '00', ?)";

            PreparedStatement reservSt = ConnectSQL.getSt(reservSql);
            reservSt.setString(1, reservCode);
            reservSt.setString(2, contraCode);
            reservSt.setString(3, deliveryDatetime);
            reservSt.setString(4, orderNum);
            reservSt.setString(5, "N");   // 初期承認フラグ
            reservSt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();

            /* =========================
             * エラー時：確認画面へ戻す
             * ========================= */
            request.setAttribute("errorMessage",
                "登録処理でエラーが発生しました");

            request.setAttribute("postCode", postCode);
            request.setAttribute("address", address);
            request.setAttribute("name", name);
            request.setAttribute("phoneNum", phoneNum);
            request.setAttribute("furnitureCount", furnitureCount);
            request.setAttribute("furnitureList", furnitureList);

            request.getRequestDispatcher("/delivary.jsp")
                   .forward(request, response);
            return;
        }

        /* =========================
         * ⑥ 登録完了画面へ
         * ========================= */
        request.setAttribute("reservCode", reservCode);
        request.setAttribute("postCode", postCode);
        request.setAttribute("address", address);
        request.setAttribute("name", name);
        request.setAttribute("phoneNum", phoneNum);
        request.setAttribute("furnitureCount", furnitureCount);
        request.setAttribute("furnitureList", furnitureList);
        request.setAttribute("hopeDate", hopeDate);
        request.setAttribute("hopeTime", hopeTime);

        request.getRequestDispatcher("/registration.jsp")
               .forward(request, response);
    }

    /* ================================
     * 受付番号生成（内部用）
     * ================================ */
    private String generateOrderNum()
            throws SQLException, ClassNotFoundException {

        String date =
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));

        String sql =
            "SELECT LPAD(NVL(MAX(SUBSTR(ORDER_NUM, 7)), 0) + 1, 4, '0') " +
            "FROM ORDERS WHERE SUBSTR(ORDER_NUM, 1, 6) = ?";

        PreparedStatement st = ConnectSQL.getSt(sql);
        st.setString(1, date);
        ResultSet rs = st.executeQuery();
        rs.next();

        return date + rs.getString(1);
    }

    /* ================================
     * 予約番号生成
     * ================================ */
    private String generateReservCode()
            throws SQLException, ClassNotFoundException {

        String date =
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));

        String sql =
            "SELECT LPAD(NVL(MAX(SUBSTR(RESERV_CODE, 7)), 0) + 1, 4, '0') " +
            "FROM RESERV WHERE SUBSTR(RESERV_CODE, 1, 6) = ?";

        PreparedStatement st = ConnectSQL.getSt(sql);
        st.setString(1, date);
        ResultSet rs = st.executeQuery();
        rs.next();

        return date + rs.getString(1);
    }
}

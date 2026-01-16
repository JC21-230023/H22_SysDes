package servlet.deliver;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sql_temp.ConnectSQL;

@WebServlet("/updateExecute")
public class UpdateResultServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // ==============================
        // ① パラメータ取得（JSPと一致）
        // ==============================
        String reservCode = request.getParameter("reservationCode");
        String hopeDate   = request.getParameter("hopeDate");   // yyyy-MM-dd
        String hopeTime   = request.getParameter("hopeTime");   // AM / PM

        // ------------------------------
        // 必須チェック
        // ------------------------------
        if (reservCode == null || hopeDate == null || hopeTime == null
            || hopeDate.isEmpty() || hopeTime.isEmpty()) {

            request.setAttribute("errorMessage", "日付または時間帯が選択されていません。");
            request.getRequestDispatcher("/updateselect.jsp")
                   .forward(request, response);
            return;
        }

        // ==============================
        // ② 配達日時生成
        // ==============================
        LocalDate date = LocalDate.parse(hopeDate);
        LocalDateTime deliveryDatetime;

        if ("AM".equals(hopeTime)) {
            deliveryDatetime = date.atTime(9, 0);
        } else if ("PM".equals(hopeTime)) {
            deliveryDatetime = date.atTime(13, 0);
        } else {
            throw new ServletException("不正な時間帯: " + hopeTime);
        }

        int updateCount = 0;

        // ==============================
        // ③ 更新SQL
        // ==============================
        String sql =
            "UPDATE RESERV " +
            "SET DELIVERY_DATETIME = ? " +
            "WHERE RESERV_CODE = ?";

        try {
            PreparedStatement ps = ConnectSQL.getSt(sql);
            ps.setTimestamp(1, java.sql.Timestamp.valueOf(deliveryDatetime));
            ps.setString(2, reservCode);

            updateCount = ps.executeUpdate();

        } catch (Exception e) {
            throw new ServletException(e);
        }

     // ==============================
     // ④ 結果判定
     // ==============================
     if (updateCount == 1) {
         request.setAttribute("reservCode", reservCode);
         request.setAttribute("hopeDate", hopeDate);
         request.setAttribute("hopeTime", hopeTime);
         request.setAttribute("message", "配達予約を更新しました。");
     }


        // ==============================
        // ⑤ 完了画面へ
        // ==============================
        RequestDispatcher rd =
            request.getRequestDispatcher("/updateresult.jsp");
        rd.forward(request, response);
    }
}

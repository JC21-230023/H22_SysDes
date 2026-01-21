package servlet.deliver;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sql_temp.ConnectSQL;

@WebServlet("/update")
public class UpdateSearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // ==============================
        // ① 予約番号取得
        // ==============================
        String reservCode = request.getParameter("reservationCode");

        String postCode = null;
        String contraCode = null;
        LocalDateTime deliveryDatetime = null;

        // ==============================
        // ② 予約情報取得（元の配達日含む）
        // ==============================
        String sql =
            "SELECT O.POST_CODE, R.CONTRA_CODE, R.DELIVERY_DATETIME " +
            "FROM RESERV R " +
            "JOIN ORDERS O ON R.ORDER_NUM = O.ORDER_NUM " +
            "WHERE R.RESERV_CODE = ?";

        try {
            PreparedStatement ps = ConnectSQL.getSt(sql);
            ps.setString(1, reservCode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                postCode = rs.getString("POST_CODE");
                contraCode = rs.getString("CONTRA_CODE");
                deliveryDatetime =
                    rs.getTimestamp("DELIVERY_DATETIME").toLocalDateTime();
            } else {
                request.setAttribute("errorMessage", "予約情報が取得できません。");
                request.getRequestDispatcher("/reservesearch.jsp")
                       .forward(request, response);
                return;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }

        // ==============================
        // ③ 元の配達日の翌日
        // ==============================
        LocalDate baseDate = deliveryDatetime.toLocalDate();
        LocalDate searchStartDate = baseDate.plusDays(1);

        // ==============================
        // ④ 配達可能日検索（DBは広め）
        // ==============================
        List<String> availableDateList = new ArrayList<>();

        String sqlDate =
            "SELECT DISTINCT TO_CHAR(D.AVAIL_DEL_DATETIME,'YYYY-MM-DD') AS AVAIL_DATE " +
            "FROM POSTCODE P " +
            "JOIN CONTRA C ON P.DELAREA_CODE = C.DELAREA_CODE " +
            "JOIN DATE_CONTRA D ON C.CONTRA_CODE = D.CONTRA_CODE " +
            "WHERE P.POST_CODE LIKE ? " +
            "AND C.CONTRA_CODE = ? " +
            "AND D.AVAIL_DEL_DATETIME >= ? " +
            "ORDER BY AVAIL_DATE";

        try {
            PreparedStatement ps = ConnectSQL.getSt(sqlDate);
            ps.setString(1, postCode.substring(0, 3) + "%");
            ps.setString(2, contraCode);
            ps.setDate(3, java.sql.Date.valueOf(searchStartDate));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                availableDateList.add(rs.getString("AVAIL_DATE"));
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }

        // ==============================
        // ⑤ JSPへ渡す
        // ==============================
        request.setAttribute("reservCode", reservCode);
        request.setAttribute("postCode", postCode);

        // ★ これが重要
        request.setAttribute(
            "baseDeliveryDate",
            baseDate.toString()
        );

        request.setAttribute("availableDateList", availableDateList);

        // ==============================
        // ⑥ カレンダー画面へ
        // ==============================
        RequestDispatcher rd =
            request.getRequestDispatcher("/updateselect.jsp");
        rd.forward(request, response);
    }
}

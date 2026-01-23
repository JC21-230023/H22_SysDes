package servlet.registcontra;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ApprovalDeliveryStatus")
public class ApprovalDeliverystatusServlet extends HttpServlet {

    private static final String DRIVER =
            "oracle.jdbc.driver.OracleDriver";
    private static final String URL =
            "jdbc:oracle:thin:@192.168.54.222:1521/r07sysdev";
    private static final String USER = "r07sysdev";
    private static final String PASS = "R07SysDev";

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        List<Map<String, Object>> reservList = new ArrayList<>();

        String[] statuses = request.getParameterValues("status");
        String singleDate = request.getParameter("singleDate");
        String fromDate   = request.getParameter("fromDate");
        String toDate     = request.getParameter("toDate");

        String approval = request.getParameter("approval");
        if (approval == null || approval.isEmpty()) {
            approval = "N";
        }

        try {
            Class.forName(DRIVER);
            try (Connection con =
                     DriverManager.getConnection(URL, USER, PASS)) {

                StringBuilder sql = new StringBuilder(
                    "SELECT r.RESERV_CODE, r.ORDER_NUM, o.FURN_QUANTITY, " +
                    "r.DELIVERY_DATETIME, r.DEL_COMP_DATE, " +
                    "r.DEL_STATUS, r.REMARK " +
                    "FROM RESERV r " +
                    "JOIN ORDERS o ON r.ORDER_NUM = o.ORDER_NUM " +
                    "WHERE r.APPROVAL = ? "
                );

                if (statuses != null && statuses.length > 0) {
                    sql.append("AND r.DEL_STATUS IN (");
                    for (int i = 0; i < statuses.length; i++) {
                        sql.append("?");
                        if (i < statuses.length - 1) sql.append(",");
                    }
                    sql.append(") ");
                }

                if (singleDate != null && !singleDate.isEmpty()) {
                    sql.append("AND TRUNC(r.DELIVERY_DATETIME) = TO_DATE(?, 'YYYY-MM-DD') ");
                } else {
                    if (fromDate != null && !fromDate.isEmpty()) {
                        sql.append("AND TRUNC(r.DELIVERY_DATETIME) >= TO_DATE(?, 'YYYY-MM-DD') ");
                    }
                    if (toDate != null && !toDate.isEmpty()) {
                        sql.append("AND TRUNC(r.DELIVERY_DATETIME) <= TO_DATE(?, 'YYYY-MM-DD') ");
                    }
                }

                sql.append("ORDER BY r.DELIVERY_DATETIME");

                PreparedStatement ps = con.prepareStatement(sql.toString());
                int idx = 1;

                ps.setString(idx++, approval);

                if (statuses != null) {
                    for (String s : statuses) {
                        ps.setString(idx++, s);
                    }
                }

                if (singleDate != null && !singleDate.isEmpty()) {
                    ps.setString(idx++, singleDate);
                } else {
                    if (fromDate != null && !fromDate.isEmpty()) {
                        ps.setString(idx++, fromDate);
                    }
                    if (toDate != null && !toDate.isEmpty()) {
                        ps.setString(idx++, toDate);
                    }
                }

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("RESERV_CODE", rs.getString("RESERV_CODE"));
                    row.put("ORDER_NUM", rs.getString("ORDER_NUM"));
                    row.put("FURN_QUANTITY", rs.getInt("FURN_QUANTITY"));
                    row.put("DELIVERY_DATETIME", rs.getTimestamp("DELIVERY_DATETIME"));
                    row.put("DEL_COMP_DATE", rs.getDate("DEL_COMP_DATE"));
                    row.put("DEL_STATUS", rs.getString("DEL_STATUS"));
                    row.put("REMARK", rs.getString("REMARK"));
                    reservList.add(row);
                }
            }

            request.setAttribute("reservList", reservList);
            request.getRequestDispatcher("approvaldeliverystatus.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String[] codes = request.getParameterValues("approveCodes");

        if (codes == null || codes.length == 0) {
            request.setAttribute("errorMsg", "承認対象が選択されていません");
            doGet(request, response);
            return;
        }

        try {
            Class.forName(DRIVER);
            try (Connection con =
                     DriverManager.getConnection(URL, USER, PASS);
                 PreparedStatement ps = con.prepareStatement(
                     "UPDATE RESERV SET APPROVAL='Y', DEL_STATUS='04', " +
                     "DEL_COMP_DATE=SYSDATE WHERE RESERV_CODE=?")) {

                for (String c : codes) {
                    ps.setString(1, c);
                    ps.executeUpdate();
                }
            }

            response.sendRedirect("ApprovalDeliveryStatus");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
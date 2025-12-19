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

    private static final String DRIVER_NAME =
            "oracle.jdbc.driver.OracleDriver";
    private static final String URL =
            "jdbc:oracle:thin:@192.168.54.222:1521/r07sysdev";
    private static final String USER = "r07sysdev";
    private static final String PASS = "R07SysDev";

    // =========================
    // 一覧表示（GET）
    // =========================
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        List<Map<String, Object>> reservList = new ArrayList<>();

        try {
            Class.forName(DRIVER_NAME);
            try (Connection con =
                     DriverManager.getConnection(URL, USER, PASS);
                 PreparedStatement ps = con.prepareStatement(
                     "SELECT RESERV_CODE, ORDER_NUM, DELIVERY_DATETIME, " +
                     "       DEL_COMP_DATE, DEL_STATUS, REMARK " +
                     "FROM RESERV " +
                     "WHERE APPROVAL = 'N' " +
                     "ORDER BY DELIVERY_DATETIME");
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("RESERV_CODE", rs.getString("RESERV_CODE"));
                    row.put("ORDER_NUM", rs.getString("ORDER_NUM"));
                    row.put("DELIVERY_DATETIME",
                            rs.getTimestamp("DELIVERY_DATETIME"));
                    row.put("DEL_COMP_DATE",
                            rs.getDate("DEL_COMP_DATE"));
                    row.put("DEL_STATUS",
                            rs.getString("DEL_STATUS"));
                    row.put("REMARK",
                            rs.getString("REMARK"));

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

    // =========================
    // 承認処理（POST）
    // =========================
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String[] approveCodes =
                request.getParameterValues("approveCodes");

        if (approveCodes == null || approveCodes.length == 0) {
            request.setAttribute("errorMsg", "承認対象が選択されていません");
            doGet(request, response);
            return;
        }

        try {
            Class.forName(DRIVER_NAME);
            try (Connection con =
                     DriverManager.getConnection(URL, USER, PASS);
                 PreparedStatement ps = con.prepareStatement(
                     "UPDATE RESERV " +
                     "SET APPROVAL='Y', DEL_STATUS='04', " +
                     "    DEL_COMP_DATE=SYSDATE " +
                     "WHERE RESERV_CODE=? AND APPROVAL='N'")) {

                for (String code : approveCodes) {
                    ps.setString(1, code);
                    ps.executeUpdate();
                }
            }

            response.sendRedirect("ApprovalDeliveryStatus");

        } catch (Exception e) {
            request.setAttribute("errorMsg",
                    "承認処理中にエラーが発生しました");
            doGet(request, response);
        }
    }
}
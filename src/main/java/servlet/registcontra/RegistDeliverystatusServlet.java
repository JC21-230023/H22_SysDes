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

@WebServlet("/RegistDeliverystatusServlet")
public class RegistDeliverystatusServlet extends HttpServlet {

    private static final String DRIVER =
        "oracle.jdbc.driver.OracleDriver";
    private static final String URL =
        "jdbc:oracle:thin:@192.168.54.222:1521/r07sysdev";
    private static final String USER = "r07sysdev";
    private static final String PASS = "R07SysDev";

    // =========================
    // 画面表示
    // =========================
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String reservCode = request.getParameter("reservCode");

        if (reservCode == null || reservCode.isEmpty()) {
            request.setAttribute("errorMsg", "予約番号を入力してください");
            request.getRequestDispatcher("deliverystatusinput.jsp")
                   .forward(request, response);
            return;
        }

        try {
            Class.forName(DRIVER);

            try (Connection con =
                     DriverManager.getConnection(URL, USER, PASS)) {

                // 予約情報
                PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM RESERV WHERE RESERV_CODE=?"
                );
                ps.setString(1, reservCode);
                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    request.setAttribute("errorMsg", "予約が存在しません");
                    request.getRequestDispatcher("deliverystatusinput.jsp")
                           .forward(request, response);
                    return;
                }

                Map<String, Object> reserv = new HashMap<>();
                reserv.put("RESERV_CODE", rs.getString("RESERV_CODE"));
                reserv.put("DELIVERY_DATETIME", rs.getTimestamp("DELIVERY_DATETIME"));
                reserv.put("ORDER_NUM", rs.getString("ORDER_NUM"));

                request.setAttribute("reserv", reserv);
                
                String orderNum = rs.getString("ORDER_NUM");

                // 家具一覧（設計どおり）
                PreparedStatement ps2 = con.prepareStatement(
                    "SELECT f.FURN_CODE, f.FURN_NAME " +
                    "FROM ORDER_FURN ofu " +
                    "JOIN FURN f ON ofu.FURN_CODE = f.FURN_CODE " +
                    "WHERE ofu.ORDER_NUM = ?"
                );
                ps2.setString(1, orderNum);

                ResultSet frs = ps2.executeQuery();
                List<Map<String,String>> furniList = new ArrayList<>();

                while (frs.next()) {
                    Map<String,String> m = new HashMap<>();
                    m.put("FURN_CODE", frs.getString("FURN_CODE"));
                    m.put("FURN_NAME", frs.getString("FURN_NAME"));
                    furniList.add(m);
                }

                request.setAttribute("furniList", furniList);
            }

            request.getRequestDispatcher("deliverystatus.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // =========================
    // 登録処理
    // =========================
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String reservCode = request.getParameter("reservCode");
        String delDate    = request.getParameter("delDate");
        String status     = request.getParameter("delStatus");
        String remark     = request.getParameter("remark");

        if ("03".equals(status) &&
            (remark == null || remark.isEmpty())) {

            request.setAttribute("errorMsg", "異常時は備考を入力してください");
            doGet(request, response);
            return;
        }

        try {
            Class.forName(DRIVER);

            try (Connection con =
                     DriverManager.getConnection(URL, USER, PASS);
                 PreparedStatement ps = con.prepareStatement(
                    "UPDATE RESERV " +
                    "SET DEL_STATUS=?, " +
                    "    DEL_COMP_DATE=TO_DATE(?, 'YYYY-MM-DD'), " +
                    "    REMARK=? " +
                    "WHERE RESERV_CODE=?"
                 )) {

                ps.setString(1, status);
                ps.setString(2, delDate);
                ps.setString(3, remark);
                ps.setString(4, reservCode);
                ps.executeUpdate();
            }
            System.out.println("contextPath = " + request.getContextPath());
            response.sendRedirect(
            	    request.getContextPath() + "/deliverystatuscomplete.jsp"
            	);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}

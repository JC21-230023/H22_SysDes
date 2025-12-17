package servlet.registcontra;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RegistContra")
public class RegistContraServlet extends HttpServlet {

    // DB接続情報
    private static final String DRIVER_NAME =
            "oracle.jdbc.driver.OracleDriver";
    private static final String URL =
            "jdbc:oracle:thin:@192.168.54.222:1521/r07sysdev";
    private static final String USER = "r07sysdev";
    private static final String PASS = "R07SysDev";
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            delareadao dao = new delareadao();
            request.setAttribute("delareaList", dao.findAll());
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestDispatcher rd =
            request.getRequestDispatcher("registcontra.jsp");
        rd.forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String traderName = request.getParameter("contraName");
        String tel = request.getParameter("tel");
        String areaCode = request.getParameter("DELAREA_CODE");
        String password = request.getParameter("password");

        // ===== 入力チェック =====
        String errorMsg = null;

        if (traderName == null || traderName.isEmpty()) {
            errorMsg = "業者名を入力してください";
        } else if (!tel.matches("\\d{10,11}")) {
            errorMsg = "連絡先はハイフンなし半角数字で入力してください";
        } else if (!password.matches("\\d{4}")) {
            errorMsg = "パスワードは半角数字4桁で入力してください";
        }

        if (errorMsg != null) {
            request.setAttribute("errorMsg", errorMsg);
            RequestDispatcher rd =
                    request.getRequestDispatcher("trader_register.jsp");
            rd.forward(request, response);
            return;
        }

        // ===== DB登録 =====
        Connection con = null;
        PreparedStatement ps = null;

        try {
            Class.forName(DRIVER_NAME);
            con = DriverManager.getConnection(URL, USER, PASS);

            String sql =
                "INSERT INTO TRADER " +
                "(TRADER_ID, TRADER_NAME, TEL, AREA_CODE, PASSWORD) " +
                "VALUES (TRADER_SEQ.NEXTVAL, ?, ?, ?, ?)";

            ps = con.prepareStatement(sql);
            ps.setString(1, traderName);
            ps.setString(2, tel);
            ps.setString(3, areaCode);
            ps.setString(4, password);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", "登録処理中にエラーが発生しました");
            RequestDispatcher rd =
                    request.getRequestDispatcher("trader_register.jsp");
            rd.forward(request, response);
            return;

        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // ===== 登録完了 =====
        response.sendRedirect("registcontracomp.jsp");
    }
}
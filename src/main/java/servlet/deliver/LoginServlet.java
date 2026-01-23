package servlet.deliver;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sql_temp.ConnectSQL;

@WebServlet("/deliver/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String contraCode = request.getParameter("contraCode");
        String password = request.getParameter("password");

        // SQL: 業者コードとパスワードが一致するレコードがあるか
        String sql = "SELECT CONTRA_NAME FROM CONTRA WHERE CONTRA_CODE = ? AND PASSWORD = ?";

        try {
            PreparedStatement ps = ConnectSQL.getSt(sql);
            ps.setString(1, contraCode);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // 認証成功：セッションに業者情報を保存
                HttpSession session = request.getSession();
                session.setAttribute("loginUser", rs.getString("CONTRA_NAME"));
                session.setAttribute("contraCode", contraCode);
                
                // 業者用トップページへ遷移
                response.sendRedirect("index3.jsp"); 
            } else {
                // 認証失敗
                request.setAttribute("error", "業者コードまたはパスワードが正しくありません");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
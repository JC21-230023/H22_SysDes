package servlet.registcontra;

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

@WebServlet("/EditFormServlet")
public class EditFormServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // ★ セッションから業者コード取得
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("contraCode") == null) {
            response.sendRedirect("../deliver/login.jsp");
            return;
        }
        String contraCode = (String) session.getAttribute("contraCode");

        try {
            PreparedStatement ps =
                ConnectSQL.getSt(
                    "SELECT CONTRA_CODE, CONTRA_NAME, CONTRA_PHONUM, " +
                    "       DELAREA_CODE, PASSWORD " +
                    "FROM CONTRA " +
                    "WHERE CONTRA_CODE = ?"
                );

            ps.setString(1, contraCode);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                request.setAttribute(
                    "errorMessage",
                    "業者情報の取得に失敗しました。"
                );
                request.getRequestDispatcher("index3.jsp")
                       .forward(request, response);
                return;
            }

            request.setAttribute("contraCode", rs.getString("CONTRA_CODE"));
            request.setAttribute("contraName", rs.getString("CONTRA_NAME"));
            request.setAttribute("contraPhonum", rs.getString("CONTRA_PHONUM"));
            request.setAttribute("delareaCode", rs.getString("DELAREA_CODE"));
            request.setAttribute("password", rs.getString("PASSWORD"));

            rs.close();
            ps.close();

            request.getRequestDispatcher("editform.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}

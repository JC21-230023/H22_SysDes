package servlet.registcontra;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sql_temp.ConnectSQL;

@WebServlet("/EditFormServlet")
public class EditFormServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 業者コード取得
        String contraCode = request.getParameter("contraCode");

        try {
            /* ==============================
             * ① 業者情報取得
             * ============================== */
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
                    "入力された業者コードは存在しません。"
                );
                request.getRequestDispatcher("testform.jsp")
                       .forward(request, response);
                return;
            }

            /* ==============================
             * ② request に直接セット
             * ============================== */
            request.setAttribute("contraCode", rs.getString("CONTRA_CODE"));
            request.setAttribute("contraName", rs.getString("CONTRA_NAME"));
            request.setAttribute("contraPhonum", rs.getString("CONTRA_PHONUM"));
            request.setAttribute("delareaCode", rs.getString("DELAREA_CODE"));
            request.setAttribute("password", rs.getString("PASSWORD"));

            rs.close();
            ps.close();

            /* ==============================
             * ③ editform.jsp へ
             * ============================== */
            RequestDispatcher rd =
                request.getRequestDispatcher("editform.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}

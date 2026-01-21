package servlet.registcontra;

import java.io.IOException;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sql_temp.ConnectSQL;

@WebServlet("/editresult")
public class EditServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // ===== ① フォーム受取 =====
        String contraCode   = request.getParameter("contraCode");   // 変更不可
        String contraName   = request.getParameter("contraName");
        String contraPhonum = request.getParameter("contraPhonum");
        String delareaCode  = request.getParameter("delareaCode");
        String password    = request.getParameter("password");

        try {
            PreparedStatement ps;

            /* ==============================
             * ② パスワード有無でSQL分岐
             * ============================== */
            if (password != null && !password.isEmpty()) {

                // パスワード更新あり
                ps = ConnectSQL.getSt(
                    "UPDATE CONTRA SET " +
                    "CONTRA_NAME = ?, " +
                    "CONTRA_PHONUM = ?, " +
                    "DELAREA_CODE = ?, " +
                    "PASSWORD = ? " +
                    "WHERE CONTRA_CODE = ?"
                );

                ps.setString(1, contraName);
                ps.setString(2, contraPhonum);
                ps.setString(3, delareaCode);
                ps.setString(4, password);
                ps.setString(5, contraCode);

            } else {

                // パスワード更新なし
                ps = ConnectSQL.getSt(
                    "UPDATE CONTRA SET " +
                    "CONTRA_NAME = ?, " +
                    "CONTRA_PHONUM = ?, " +
                    "DELAREA_CODE = ? " +
                    "WHERE CONTRA_CODE = ?"
                );

                ps.setString(1, contraName);
                ps.setString(2, contraPhonum);
                ps.setString(3, delareaCode);
                ps.setString(4, contraCode);
            }

            /* ==============================
             * ③ 更新実行
             * ============================== */
            int updateCount = ps.executeUpdate();
            ps.close();

            if (updateCount == 0) {
                request.setAttribute(
                    "errorMessage",
                    "更新に失敗しました。業者コードを確認してください。"
                );
                request.getRequestDispatcher("editform.jsp")
                       .forward(request, response);
                return;
            }

            /* ==============================
             * ④ 完了画面 or 一覧へ
             * ============================== */
            response.sendRedirect("editcomplete.jsp");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}

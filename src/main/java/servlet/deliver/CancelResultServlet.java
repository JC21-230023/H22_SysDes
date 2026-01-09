package servlet.deliver;

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

@WebServlet("/cancelresult")
public class CancelResultServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String reservCode = request.getParameter("reservationCode");
        String orderNum = null;

        try {
            /* ==============================
             * ① RESERV → ORDER_NUM 取得
             * ============================== */
            PreparedStatement ps0 =
                ConnectSQL.getSt(
                    "SELECT ORDER_NUM FROM RESERV WHERE RESERV_CODE = ?"
                );
            ps0.setString(1, reservCode);
            ResultSet rs = ps0.executeQuery();

            if (!rs.next()) {
                request.setAttribute(
                    "errorMessage",
                    "入力された予約番号は存在しません。"
                );
                request.getRequestDispatcher("selectform.jsp")
                       .forward(request, response);
                return;
            }

            orderNum = rs.getString("ORDER_NUM");
            rs.close();
            ps0.close();

            /* ==============================
             * ② ORDER_FURN 削除（子）
             * ============================== */
            PreparedStatement ps1 =
                ConnectSQL.getSt(
                    "DELETE FROM ORDER_FURN WHERE ORDER_NUM = ?"
                );
            ps1.setString(1, orderNum);
            ps1.executeUpdate();
            ps1.close();

            /* ==============================
             * ③ RESERV 削除（子）
             * ============================== */
            PreparedStatement ps2 =
                ConnectSQL.getSt(
                    "DELETE FROM RESERV WHERE RESERV_CODE = ?"
                );
            ps2.setString(1, reservCode);
            ps2.executeUpdate();
            ps2.close();

            /* ==============================
             * ④ ORDERS 削除（親）
             * ============================== */
            PreparedStatement ps3 =
                ConnectSQL.getSt(
                    "DELETE FROM ORDERS WHERE ORDER_NUM = ?"
                );
            ps3.setString(1, orderNum);
            ps3.executeUpdate();
            ps3.close();

            /* ==============================
             * 完了画面へ
             * ============================== */
            request.setAttribute("reservCode", reservCode);
            request.setAttribute("orderNum", orderNum);

            RequestDispatcher rd =
                request.getRequestDispatcher("cancelresult.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}

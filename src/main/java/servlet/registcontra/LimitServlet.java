package servlet.registcontra;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sql_temp.ConnectSQL;

@WebServlet("/limit")
public class LimitServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        /* ===== ① セッションから業者コード取得 ===== */
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("contraCode") == null) {
            // セッション切れ対策
            res.sendRedirect("login.jsp");
            return;
        }

        String contraCode = (String) session.getAttribute("contraCode");

        /* ===== ② 年月取得 ===== */
        int year  = Integer.parseInt(req.getParameter("year"));
        int month = Integer.parseInt(req.getParameter("month"));

        String ym = year + "-" + String.format("%02d", month);

        try {
            /* ===== ③ 業者名取得 ===== */
            String contraName = null;
            PreparedStatement stName =
                ConnectSQL.getSt(
                    "SELECT CONTRA_NAME FROM CONTRA WHERE CONTRA_CODE = ?"
                );
            stName.setString(1, contraCode);

            ResultSet rsName = stName.executeQuery();
            if (rsName.next()) {
                contraName = rsName.getString("CONTRA_NAME");
            }
            rsName.close();
            stName.close();

            /* ===== ④ 既存データ削除 ===== */
            PreparedStatement del =
                ConnectSQL.getSt(
                    "DELETE FROM DATE_CONTRA " +
                    "WHERE CONTRA_CODE = ? " +
                    "AND TO_CHAR(AVAIL_DEL_DATETIME, 'YYYY-MM') = ?"
                );
            del.setString(1, contraCode);
            del.setString(2, ym);
            del.executeUpdate();
            del.close();

            /* ===== ⑤ INSERT 準備 ===== */
            PreparedStatement ins =
                ConnectSQL.getSt(
                    "INSERT INTO DATE_CONTRA " +
                    "(AVAIL_DEL_DATETIME, CONTRA_CODE, MAX_DELIVERY_COUNT) " +
                    "VALUES (?, ?, ?)"
                );

            /* ===== ⑥ 日付ごとの上限登録 ===== */
            for (Map.Entry<String, String[]> e : req.getParameterMap().entrySet()) {
                String name = e.getKey();

                if (!name.startsWith("am_") && !name.startsWith("pm_")) {
                    continue;
                }

                String val = e.getValue()[0];
                if (val == null || val.isEmpty()) {
                    continue;
                }

                String date = name.substring(3); // yyyy-MM-dd
                String time = name.startsWith("am_")
                                ? "09:00:00"
                                : "13:00:00";

                ins.setString(1, date + " " + time);
                ins.setString(2, contraCode);
                ins.setInt(3, Integer.parseInt(val));
                ins.executeUpdate();
            }
            ins.close();

            /* ===== ⑦ 完了画面へ ===== */
            String targetYm = year + "年" + month + "月";

            req.setAttribute("contraCode", contraCode);
            req.setAttribute("contraName", contraName);
            req.setAttribute("targetYm", targetYm);

            req.getRequestDispatcher("/limitresult.jsp")
               .forward(req, res);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}

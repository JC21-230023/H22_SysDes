

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DBoperation")
public class DB_operation extends HttpServlet {

    final String driverName = "oracle.jdbc.driver.OracleDriver";
    final String url = "jdbc:oracle:thin:@192.168.54.222:1521/r07sysdev";
    final String id = "r07sysdev";
    final String pass = "R07SysDev";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        Connection con = null;
        Statement stmt = null;
        List<String[]> table = new ArrayList<>();
        String sql = request.getParameter("sql");
        String message = "";

        try {
            Class.forName(driverName);
            con = DriverManager.getConnection(url, id, pass);
            stmt = con.createStatement();

            if (sql != null && !sql.trim().isEmpty()) {

                String upper = sql.trim().toUpperCase();

                // ---------------------------
                // SELECT 文は普通に実行
                // ---------------------------
                if (upper.startsWith("SELECT")) {

                    ResultSet rs = stmt.executeQuery(sql);
                    int colCount = rs.getMetaData().getColumnCount();

                    while (rs.next()) {
                        String[] row = new String[colCount];
                        for (int i = 1; i <= colCount; i++) {
                            row[i - 1] = rs.getString(i);
                        }
                        table.add(row);
                    }
                    rs.close();

                    message = "SELECT 実行完了（" + table.size() + " 行）";

                } else {
                    // ----------------------------
                    // CREATE / INSERT / UPDATE など
                    // まず実行
                    // ----------------------------
                    stmt.executeUpdate(sql);
                    message = "SQL 実行完了";

                    // ----------------------------
                    // 自動 SELECT：テーブル名を抽出
                    // ----------------------------
                    String tableName = extractTableName(upper);

                    if (tableName != null) {
                        String autoSelect = "SELECT * FROM " + tableName;
                        try {
                            ResultSet rs2 = stmt.executeQuery(autoSelect);
                            int colCount2 = rs2.getMetaData().getColumnCount();

                            while (rs2.next()) {
                                String[] row = new String[colCount2];
                                for (int i = 1; i <= colCount2; i++) {
                                    row[i - 1] = rs2.getString(i);
                                }
                                table.add(row);
                            }
                            rs2.close();

                            message += " → 自動 SELECT 実行: " + autoSelect;

                        } catch (SQLException e) {
                            message += "（自動SELECT失敗: " + e.getMessage() + "）";
                        }
                    } else {
                        message += "（テーブル名が判定できません）";
                    }
                }
            }

        } catch (Exception e) {
            message = "エラー: " + e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // 結果を JSP に渡す
        request.setAttribute("RESULT_TABLE", table);
        request.setAttribute("MESSAGE", message);
        request.setAttribute("SQL", sql);
        request.getRequestDispatcher("/SQLexcater.jsp").forward(request, response);
    }

    // -------------------------
    // SQL からテーブル名抽出
    // -------------------------
    private String extractTableName(String upperSql) {

        upperSql = upperSql.replaceAll("\\s+", " ").trim();

        String[] tokens = upperSql.split(" ");

        if (tokens.length < 3) return null;

        if (tokens[0].equals("CREATE") && tokens[1].equals("TABLE")) {
            return tokens[2];
        }

        if (tokens[0].equals("INSERT") && tokens[1].equals("INTO")) {
            return tokens[2];
        }

        if (tokens[0].equals("UPDATE")) {
            return tokens[1];
        }

        if (tokens[0].equals("DELETE") && tokens[1].equals("FROM")) {
            return tokens[2];
        }

        return null;
    }
}
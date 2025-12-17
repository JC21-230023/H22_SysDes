package servlet.registcontra;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class delareadao {

    private static final String DRIVER ="oracle.jdbc.driver.OracleDriver";
    private static final String URL ="jdbc:oracle:thin:@192.168.54.222:1521/r07sysdev";
    private static final String USER = "r07sysdev";
    private static final String PASS = "R07SysDev";

    public List<delarea> findAll() throws Exception {

        List<delarea> list = new ArrayList<>();

        Class.forName(DRIVER);
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps =
                 con.prepareStatement(
                     "SELECT DELAREA_CODE, DELAREA_NAME FROM DELAREA ORDER BY DELAREA_CODE");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new delarea(
                    rs.getString("DELAREA_CODE"),
                    rs.getString("DELAREA_NAME")
                ));
            }
        }
        return list;
    }
}
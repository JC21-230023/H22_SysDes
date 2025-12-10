package sql_temp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public interface ConnectSQL {
    
	final  String driverName = 
			"oracle.jdbc.driver.OracleDriver";
	final  String url = 
			"jdbc:oracle:thin:@192.168.54.222:1521/r07sysdev";
	final  String id = 
			"r07sysdev";
	final  String pass = 
			"R07SysDev";
	
	public static ResultSet connectDB(String sql) 
			throws ClassNotFoundException, SQLException{
		//Select用
			Class.forName(driverName);
			Connection connection=
					DriverManager.getConnection(url,id,pass);
			PreparedStatement st = 
					connection.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
		return rs;
	}
	/*public static ResultSet editDB(String sql) {
		追加予定
	}*/
}

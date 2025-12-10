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
	final  String id = "r07sysdev";
	final  String pass = "R07SysDev";
	
	//SQLにアクセス兼insert,deleat用
	public static PreparedStatement getSt(String sql) 
			throws ClassNotFoundException, SQLException{
		Class.forName(driverName);
			Connection connection=
					DriverManager.getConnection(url,id,pass);
			PreparedStatement st = 
					connection.prepareStatement(sql);
			return st;
			/*st.setString(1,○○name);
			 * st.setInt(2,○○Id);
			 * st.executeUpdate();
			 * 
			 * */
	}
	
	//*******************************************
	
	public static ResultSet connectDB(String sql) 
			throws ClassNotFoundException, SQLException{
		//Select用
			PreparedStatement st = ConnectSQL.getSt(sql);
			ResultSet rs = st.executeQuery();
		return rs;
	}
}

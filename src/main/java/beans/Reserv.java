package beans;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Reserv {
	String rsv_code;
	String address;
	String name;
	String post_code;
	String dateTime;
	ArrayList<Furn> furnList;
	
	public Reserv(ResultSet rs){
		
		try{//受付のデータ
			this.address=rs.getString("ADDRESS");
			this.name=rs.getString("NAME");
			this.post_code=rs.getString("POST_CODE");
			//予約のデータ
			this.dateTime=rs.getString("DELIVERY_DATETIME");
			this.rsv_code=rs.getString("RESERV_CODE");
			
			this.furnList=new ArrayList<Furn>();
			System.out.println("fList:"+this.furnList);
			//Integer.parseInt(rs.getString("FURN_QUANTITY")));
			
		}catch (Exception e) {
			System.out.println("rsvコンストラクターでのError");
		}
	}
//getter
	public String getRsv_code() {
		return rsv_code;
	}
	public String getAddress() {
		return address;
	}
	public String getName() {
		return name;
	}
	public String getPost_code() {
		return post_code;
	}
	public String getDateTime() {
		return dateTime;
	}
	public List<Furn> getFurnList() {
		return furnList;
	}
	public int getFrunQuant(){
		return this.furnList.size();
	}
//FurnList系
	public void addFurnList(String frun_code){
		System.out.println("add_Frun"+frun_code);
		this.furnList.add(new Furn(frun_code));
	}
	
	@Override
	public String toString() {
		return "Reserv [rsv_code=" + rsv_code + "]";
	}

	public static class Furn{
		String furn_code;
		//String furn_name;//拡張
		public Furn(String frun_code){
			this.furn_code=frun_code;
		}
		public String toString() {
			return this.furn_code;
		}
	}
}

package servlet.deliver;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Reserv;
import sql_temp.ConnectSQL;

/**
 * Servlet implementation class OutputResultServlet
 */
@WebServlet("/mklist")
public class OutputResultServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public OutputResultServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		String contra=request.getParameter("deliverID");
		String date = request.getParameter("date");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String toDay = sdf.format(new Date());
		
		
		
		String sql="SELECT rsv.DELIVERY_DATETIME, rsv.RESERV_CODE,"
		+"odr.POST_CODE,odr.ADDRESS,FURN_QUANTITY,odrF.FURN_CODE,odr.NAME "//〒、住所、配達個数、家具番号
		+ "FROM RESERV rsv"
		+ " JOIN ORDERS odr ON odr.ORDER_NUM = rsv.ORDER_NUM"
		+ " JOIN ORDER_FURN odrF ON odr.ORDER_NUM = odrF.ORDER_NUM"
		+ " WHERE rsv.CONTRA_CODE=? AND rsv.DELIVERY_DATETIME>=?";
		//+"order by asc rsv.RESERV_CODE";
		//?1=C0001 ?2=2025-12-01
		
		
		ArrayList<Reserv> rsvList=new ArrayList<Reserv>();
		try{
			PreparedStatement ps =
                    ConnectSQL.getSt(sql);
            ps.setString(1,contra);
            ps.setString(2,date);
            ResultSet rs = ps.executeQuery();
            while(rs.next() != false){//予約番号が別なら継続
            	if((rsvList.isEmpty()) || ! rsvList.getLast().getRsv_code().equals(rs.getString("RESERV_CODE"))){
            	//訳：最終更新のReservと別の予約番号なら
            		Reserv rsv = new Reserv(rs);
            		rsvList.add(rsv);
            		System.out.println(rsvList.getLast());
            	}
            	rsvList.getLast().addFurnList(rs.getString("FURN_CODE"));
            	//家具追加
    		} 
        }catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("RSV_LIST", rsvList);
		request.setAttribute("DATE",date);
		request.setAttribute("TODAY",toDay);
		request.setAttribute("CONTRA",contra);
		System.out.println("outputRESUT__end");
		System.out.println(rsvList.size());
		request.getRequestDispatcher("deliver/outputresult.jsp")
		.forward(request, response);
	}
}


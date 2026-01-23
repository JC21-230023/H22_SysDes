package servlet.deliver;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sql_temp.ConnectSQL;

/**
 * Servlet implementation class OutputListServlet
 */
@WebServlet("/delivers")
//担当者がアクセスの想定、業者、日付を選択する

public class OutputListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public OutputListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String ALLdelivers=
			"SELECT CONTRA_CODE, CONTRA_NAME FROM CONTRA";
		
		try {
			ResultSet rs=ConnectSQL.connectDB(ALLdelivers);
			List<String[]> deliverList = new ArrayList<>();
			while(rs.next() != false) {
				String[] ss = new String[2];
				ss[0] = rs.getString("CONTRA_CODE");
				ss[1] = rs.getString("CONTRA_NAME");
				deliverList.add(ss);
			}
			request.setAttribute("CONTRA",deliverList);
		}catch(Exception e) {
			e.printStackTrace();
		}
		//System.out.println("deliver/outputListへ");
		
		request.getRequestDispatcher("deliver/Outputlist.jsp")
			.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		
		doGet(request, response);
	}

}

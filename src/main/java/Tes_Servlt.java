
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Area;
import sql_temp.ConnectSQL;

/**
 * Servlet implementation class Tes_Servlt
 */
@WebServlet("/Tes_Servlt")
public class Tes_Servlt extends HttpServlet implements ConnectSQL{
	private static final long serialVersionUID = 1L;

    public Tes_Servlt() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			ResultSet rs=
					ConnectSQL.connectDB("select AREA_CODE , AREA_NAME from AREA");
			/*
			List<String[]> area = new ArrayList<>();
			while(rs.next() != false) {
				String[] ss = new String[2];
				ss[0] = rs.getString("AREA_CODE");
				ss[1] = rs.getString("AREA_NAME");
				area.add(ss);
			}↑String[]　↓bean*/
			
			
			List<Area> area = new ArrayList<>();
			
			while(rs.next() != false) {
				Area areaBean=new Area(
						rs.getString("AREA_CODE"),
						rs.getString("AREA_NAME"));
				area.add(areaBean);
			}
			

			request.setAttribute("AREA", area);
		}catch (Exception e) {
			e.printStackTrace();
		}
		request.getRequestDispatcher("/area.jsp").forward(request, response);

	}

	

}

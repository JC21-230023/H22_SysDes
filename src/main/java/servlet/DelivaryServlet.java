package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/delivaryform")
public class DelivaryServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // --- お客様情報 ---
        request.setAttribute("zipcode", request.getParameter("zipcode"));
        request.setAttribute("address", request.getParameter("address"));
        request.setAttribute("name", request.getParameter("name"));
        request.setAttribute("tel", request.getParameter("tel"));

        // --- 家具 ---
        int furnitureCount =
            Integer.parseInt(request.getParameter("furnitureCount"));
        request.setAttribute("furnitureCount", furnitureCount);

        List<String> furnitureList = new ArrayList<>();
        for (int i = 1; i <= furnitureCount; i++) {
            furnitureList.add(request.getParameter("furniture" + i));
        }
        request.setAttribute("furnitureList", furnitureList);

        request.getRequestDispatcher("delivary.jsp")
               .forward(request, response);
    }
}




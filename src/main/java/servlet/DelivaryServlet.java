package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/delivaryform")
public class DelivaryServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 文字コード（日本語入力対応）
        request.setCharacterEncoding("UTF-8");

        // --- 基本項目 ---
        String zipcode = request.getParameter("zipcode");
        String address = request.getParameter("address");
        String name    = request.getParameter("name");
        String phone   = request.getParameter("phone");

        // --- 家具個数 ---
        int itemCount = parseIntSafe(request.getParameter("itemCount"), 0);

        // --- 動的項目は配列で取得 ---
        String[] itemNames  = request.getParameterValues("itemName");
        String[] itemWidths = request.getParameterValues("itemWidth");
        String[] itemDepths = request.getParameterValues("itemDepth");
        String[] itemHeights= request.getParameterValues("itemHeight");
        String[] itemWeights= request.getParameterValues("itemWeight");

        // 配列をまとめて扱いやすい形へ
        List<Map<String, String>> items = new ArrayList<>();
        int n = itemNames != null ? itemNames.length : 0;
        for (int i = 0; i < n; i++) {
            Map<String, String> one = new HashMap<>();
            one.put("name",  safeGet(itemNames,  i));
            one.put("width", safeGet(itemWidths, i));
            one.put("depth", safeGet(itemDepths, i));
            one.put("height",safeGet(itemHeights,i));
            one.put("weight",safeGet(itemWeights,i));
            items.add(one);
        }

        // JSP に渡す
        request.setAttribute("zipcode", zipcode);
        request.setAttribute("address", address);
        request.setAttribute("name", name);
        request.setAttribute("phone", phone);
        request.setAttribute("itemCount", itemCount);
        request.setAttribute("items", items);

        // 表示 JSP へフォワード
        RequestDispatcher rd = request.getRequestDispatcher("/delivary.jsp");
        rd.forward(request, response);
    }

    private int parseIntSafe(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }

    private String safeGet(String[] arr, int idx) {
        if (arr == null || idx >= arr.length) return "";
        return arr[idx] == null ? "" : arr[idx];
    }
}


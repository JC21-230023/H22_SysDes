<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>

<html>
<head>
<meta charset="UTF-8">
<title>SQL Executor</title>
<style>
    textarea {
        width: 600px;
        height: 120px;
    }
    table {
        border-collapse: collapse;
        margin-top: 15px;
    }
    th, td {
        border: 1px solid #444;
        padding: 5px 10px;
    }
</style>
</head>
<body>

<h2>SQL 実行ツール</h2>

<form action="<%= request.getContextPath() %>/DBoperation" method="get">
    <textarea name="sql">${SQL}</textarea><br>
    <input type="submit" value="実行">
</form>

<hr>

<h3>結果</h3>

<p>${MESSAGE}</p>

<%
    List<String[]> table = (List<String[]>) request.getAttribute("RESULT_TABLE");
%>

<%
if (table != null && !table.isEmpty()) {
%>

<table>
    <tr>
        <% 
            int colCount = table.get(0).length;
            for (int i = 1; i <= colCount; i++) {
        %>
            <th>COL <%= i %></th>
        <% } %>
    </tr>

    <% for (String[] row : table) { %>
        <tr>
            <% for (String col : row) { %>
                <td><%= col %></td>
            <% } %>
        </tr>
    <% } %>

</table>

<%
} else {
%>
    <p>表示できるデータはありません。</p>
<%
}
%>

</body>
</html>
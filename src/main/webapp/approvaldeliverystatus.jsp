<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List,java.util.Map" %>

<h2>配達ステータスリスト</h2>

<%
    String errorMsg = (String)request.getAttribute("errorMsg");
    if (errorMsg != null) {
%>
    <p style="color:red;"><%= errorMsg %></p>
<%
    }
%>

<form action="ApprovalDeliveryStatus" method="post">
<table border="1">
<tr>
    <th>予約番号</th>
    <th>受付番号</th>
    <th>配達予定日時</th>
    <th>配達日</th>
    <th>状態</th>
    <th>備考</th>
    <th>承認</th>
</tr>

<%
    List<Map<String, Object>> list =
        (List<Map<String, Object>>)request.getAttribute("reservList");

    if (list != null) {
        for (Map<String, Object> row : list) {
%>
<tr>
    <td><%= row.get("RESERV_CODE") %></td>
    <td><%= row.get("ORDER_NUM") %></td>
    <td><%= row.get("DELIVERY_DATETIME") %></td>
    <td><%= row.get("DEL_COMP_DATE") %></td>
    <td><%= row.get("DEL_STATUS") %></td>
    <td><%= row.get("REMARK") %></td>
    <td>
        <input type="checkbox" name="approveCodes"
               value="<%= row.get("RESERV_CODE") %>">
    </td>
</tr>
<%
        }
    }
%>
</table>

<br>
<input type="submit" value="承認">
</form>
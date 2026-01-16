<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>

<html>
<head>
<title>配達ステータス登録</title>
</head>
<body>

<h2>配達ステータス登録</h2>

<%
Map<String, Object> reserv =
    (Map<String, Object>)request.getAttribute("reserv");

List<Map<String,String>> furniList =
    (List<Map<String,String>>)request.getAttribute("furniList");
%>

<b>予約番号：</b><%= reserv.get("RESERV_CODE") %><br>
<b>配達予定日時：</b><%= reserv.get("DELIVERY_DATETIME") %><br>

<hr>

<b>家具一覧</b>
<table border="1">
<tr>
    <th>家具番号</th>
    <th>家具名</th>
</tr>

<%
for (Map<String,String> f : furniList) {
%>
<tr>
    <td><%= f.get("FURN_CODE") %></td>
    <td><%= f.get("FURN_NAME") %></td>
</tr>
<%
}
%>
</table>

<hr>

<form action="RegistDeliverystatusServlet" method="post">
<input type="hidden" name="reservCode"
       value="<%= reserv.get("RESERV_CODE") %>">

配達日：
<input type="date" name="delDate"><br><br>

状態：
<label><input type="radio" name="delStatus" value="01">正常</label>
<label><input type="radio" name="delStatus" value="02">不在</label>
<label><input type="radio" name="delStatus" value="03">異常</label>
<br><br>

備考：<br>
<textarea name="remark" rows="4" cols="40"></textarea><br><br>

<button type="button" onclick="history.back()">戻る</button>
<input type="submit" value="登録">
</form>

<%
String errorMsg = (String)request.getAttribute("errorMsg");
if (errorMsg != null) {
%>
<p style="color:red;"><%= errorMsg %></p>
<%
}
%>

</body>
</html>
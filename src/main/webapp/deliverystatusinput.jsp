<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<html>
<head>
<title>配達ステータス登録</title>
</head>
<body>

<h2>⑨ 配達ステータス登録</h2>

<form action="RegistDeliverystatusServlet" method="get">
    予約番号を入力してください<br>
    <input type="text" name="reservCode">
    <br><br>
    <button type="button" onclick="history.back()">戻る</button>
    <input type="submit" value="次へ">
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
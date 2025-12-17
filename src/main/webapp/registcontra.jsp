<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<html>
<head>
<title>業者新規登録</title>
</head>
<body>

<h2>⑤ 新規業者登録</h2>

<%
String errorMsg = (String)request.getAttribute("errorMsg");
if (errorMsg != null) {
%>
<p style="color:red;"><%= errorMsg %></p>
<%
}
%>

<form action="traderRegister" method="post">

<p>
業者名<br>
<input type="text" name="traderName"
 value="<%= request.getParameter("traderName") != null
 ? request.getParameter("traderName") : "" %>">
</p>

<p>
連絡先<br>
<input type="text" name="tel"
 value="<%= request.getParameter("tel") != null
 ? request.getParameter("tel") : "" %>">
</p>

<p>
担当地域<br>
<select name="delareaCode">
<option value="">-- 選択してください --</option>
<%
java.util.List delareaList =
    (java.util.List)request.getAttribute("delareaList");

String selected =
    request.getParameter("delareaCode");

if (delareaList != null) {
    for (Object obj : delareaList) {
        model.DelArea area = (model.DelArea)obj;
%>
<option value="<%= area.getDelareaCode() %>"
 <%= area.getDelareaCode().equals(selected) ? "selected" : "" %>>
 <%= area.getDelareaName() %>
</option>
<%
    }
}
%>
</select>
</p>

<p>
登録パスワード<br>
<input type="password" name="password">
</p>

<input type="submit" value="上記の内容で登録">
</form>

</body>
</html>
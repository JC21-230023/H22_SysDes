<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
String contraName = (String)request.getAttribute("contraName");
String tel = (String)request.getAttribute("tel");
String areaCode = (String)request.getAttribute("areaCode");
String password = (String)request.getAttribute("password");
%>

<html>
<head>
<title>新規業者登録 完了</title>
<style>
.box {
    width: 450px;
    padding: 20px;
    margin: 20px auto;
    background-color: #e6f5f0;
    border-radius: 20px;
}
.center { text-align: center; }
</style>
</head>

<body>

<h2 class="center">⑤ 新規業者登録が完了しました。</h2>

<div class="box">
    <p>業者名：<%= contraName %></p>
    <p>連絡先：<%= tel %></p>
    <p>担当地域コード：<%= areaCode %></p>
    <p>登録パスワード：<%= password %></p>
</div>

<div class="center">
    <form action="index2.html" method="get">
        <input type="submit" value="TOPに戻る">
    </form>
</div>

</body>
</html>
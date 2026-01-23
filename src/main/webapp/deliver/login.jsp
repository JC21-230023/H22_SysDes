<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>業者ログイン</title>
    <style>
        .error { color: red; }
        .login-box { width: 300px; margin: 100px auto; padding: 20px; border: 1px solid #ccc; border-radius: 10px; }
    </style>
</head>
<body>
    <div class="login-box">
        <h2>業者ログイン</h2>
        <%-- エラーがあれば表示 --%>
        <% if(request.getAttribute("error") != null) { %>
            <p class="error"><%= request.getAttribute("error") %></p>
        <% } %>
        
        <form action="LoginServlet" method="post">
            業者コード：<br>
            <input type="text" name="contraCode" required><br><br>
            パスワード：<br>
            <input type="password" name="password" required><br><br>
            <button type="submit">ログイン</button>
        </form>
    </div>
</body>
</html>
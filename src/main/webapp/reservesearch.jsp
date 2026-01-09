<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>配達予約検索</title>

<style>
body {
    font-family: "Meiryo", sans-serif;
    background-color: #f0f2f5;
    margin: 0;
    padding: 0;
}

.container {
    max-width: 500px;
    margin: 60px auto;
    background: #ffffff;
    padding: 40px 30px;
    border-radius: 12px;
    box-shadow: 0 4px 15px rgba(0,0,0,0.1);
}

h2 {
    text-align: center;
    margin-bottom: 15px;
    color: #333;
}

p {
    text-align: center;
    color: #666;
    font-size: 14px;
    margin-bottom: 30px;
}

label {
    display: block;
    font-weight: bold;
    margin-bottom: 6px;
    color: #444;
}

input[type="text"] {
    width: 100%;
    padding: 12px 14px;
    margin-bottom: 20px;
    font-size: 16px;
    border: 1px solid #ccc;
    border-radius: 8px;
    box-sizing: border-box;
}

input[type="text"]:focus {
    border-color: #007BFF;
    outline: none;
}

.error {
    background-color: #ffe0e0;
    color: #c00;
    padding: 12px;
    border-radius: 8px;
    margin-bottom: 20px;
    font-size: 14px;
    text-align: center;
}

.button-area {
    display: flex;
    flex-direction: column;
    gap: 12px;
}

button {
    font-size: 16px;
    padding: 12px 0;
    border-radius: 8px;
    border: none;
    cursor: pointer;
}

.search-btn {
    background-color: #007BFF;
    color: #fff;
}

.search-btn:hover {
    background-color: #0056b3;
}

.back-btn {
    background-color: #e0e0e0;
    color: #333;
}

.back-btn:hover {
    background-color: #d0d0d0;
}

@media (max-width: 500px) {
    .container {
        margin: 40px 20px;
        padding: 30px 20px;
    }
}
</style>
</head>

<body>

<div class="container">

<h2>配達予約検索</h2>
<p>予約番号を入力してください</p>

<%-- エラーメッセージ --%>
<%
String errorMessage = (String) request.getAttribute("errorMessage");
if (errorMessage != null) {
%>
<div class="error"><%= errorMessage %></div>
<%
}
%>

<form action="search" method="post">

    <label>予約番号</label>
    <input type="text"
           name="reservationCode"
           placeholder="半角英数字"
           required>

    <div class="button-area">
        <button type="submit" class="search-btn">検索</button>
        <button type="button"
                class="back-btn"
                onclick="location.href='index2.html'">
            戻る
        </button>
    </div>

</form>

</div>

</body>
</html>
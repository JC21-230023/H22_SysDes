<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<title>完了</title>
<style>
body {
    background-color: #fff6e5;
    font-family: sans-serif;
}

.container {
    width: 420px;
    margin: 80px auto;
    padding: 30px;
    background: #dff5f5;
    border-radius: 15px;
    text-align: center;
}
</style>
</head>

<body>

<div class="container">
    <h2>配達ステータス登録が完了しました。</h2>

    処理日時：<%= new java.util.Date() %><br><br>

    <button onclick="location.href='deliverystatusinput.jsp'">
        TOPに戻る
    </button>
</div>

</body>
</html>

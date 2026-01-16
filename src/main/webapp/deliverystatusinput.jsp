<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<title>配達ステータス登録</title>
<style>
body {
    background-color: #fff6e5;
    font-family: sans-serif;
}

.container {
    width: 420px;
    margin: 60px auto;
    padding: 30px;
    background: #ffffff;
    border: 1px solid #999;
    text-align: center;
}

.input-box {
    background: #dff5f5;
    padding: 20px;
    margin: 20px 0;
    border-radius: 10px;
}

input[type=text] {
    width: 80%;
    padding: 6px;
}

.buttons {
    margin-top: 20px;
}

button, input[type=submit] {
    padding: 4px 12px;
    font-size: 13px;
}
.error {
    color: red;
    margin-top: 10px;
}
</style>
</head>

<body>

<h2 style="text-align:center;">⑨ 配達ステータス登録</h2>

<div class="container">
    <form action="RegistDeliverystatusServlet" method="get">
        予約番号を入力してください
        <div class="input-box">
            <input type="text" name="reservCode">
        </div>

        <div class="buttons">
            <button type="button" onclick="history.back()">戻る</button>
            <input type="submit" value="次へ">
        </div>
    </form>

    <%
    String errorMsg = (String)request.getAttribute("errorMsg");
    if (errorMsg != null) {
    %>
        <div class="error"><%= errorMsg %></div>
    <%
    }
    %>
</div>

</body>
</html>

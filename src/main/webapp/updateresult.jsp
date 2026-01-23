<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>更新完了</title>

<style>
body {
    margin: 0;
    font-family: "Meiryo", "Hiragino Kaku Gothic ProN", sans-serif;
    background-color: #ffffff;
}

.container {
    width: 800px;
    margin: 60px auto;
    text-align: center;
}

h2 {
    font-size: 28px;
    margin-bottom: 30px;
}

.message {
    font-size: 20px;
    font-weight: bold;
    color: #2f6f73;
    margin-bottom: 40px;
}

.box {
    border: 2px solid #2f6f73;
    border-radius: 15px;
    padding: 25px;
    margin-bottom: 40px;
    font-size: 18px;
    line-height: 1.8;
}

.button-area {
    text-align: center;
}

button {
    font-size: 16px;
    padding: 10px 40px;
    border-radius: 10px;
    border: 2px solid #2f6f73;
    background-color: #ffffff;
    cursor: pointer;
}

button:hover {
    background-color: #eef6f7;
}
</style>
</head>

<body>

<div class="container">

    <h2>更新完了</h2>

    <div class="message">
        配達予約の更新が完了しました。
    </div>

    <div class="box">
        予約番号：<strong>${reservCode}</strong><br>
        配達日：<strong>${hopeDate}</strong><br>
        希望時間帯：
        <strong>
        <%
            String hopeTime = (String) request.getAttribute("hopeTime");
            if ("AM".equals(hopeTime)) {
        %>
            午前（9:00～12:00）
        <%
            } else if ("PM".equals(hopeTime)) {
        %>
            午後（13:00～17:00）
        <%
            }
        %>
        </strong>
    </div>

    <div class="button-area">
        <button type="button" onclick="location.href='reservesearch.jsp'">
            戻る
        </button>
    </div>

</div>

</body>
</html>

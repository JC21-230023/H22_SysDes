<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>業者情報更新完了</title>

<style>
body {
    font-family: "Hiragino Kaku Gothic ProN", "Meiryo", sans-serif;
    background-color: #ffffff;
}

.container {
    width: 600px;
    margin: 100px auto;
    text-align: center;
}

h1 {
    color: #2f6f73;
    margin-bottom: 40px;
}

.message-box {
    background-color: #dff3ef;
    padding: 40px;
    border-radius: 12px;
    font-size: 18px;
}

.button-area {
    margin-top: 50px;
}

input[type="button"] {
    padding: 12px 40px;
    font-size: 16px;
    margin: 0 20px;
    cursor: pointer;
}
</style>
</head>

<body>

<div class="container">

    <h1>更新完了</h1>

    <div class="message-box">
        <p>業者情報の更新が完了しました。</p>
    </div>

    <div class="button-area">
        <input type="button" value="業者情報再表示"
               onclick="location.href='testform.jsp';">
        <input type="button" value="へ戻る"
               onclick="location.href='index3.jsp';">
    </div>

</div>

</body>
</html>

<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>配達上限登録 完了</title>

<style>
body {
    margin: 0;
    font-family: "Hiragino Kaku Gothic ProN", "Meiryo", sans-serif;
    background-color: #ffffff;
}

.container {
    width: 900px;
    margin: 40px auto;
}

h2 {
    text-align: center;
    font-size: 30px;
    margin-bottom: 15px;
}

.complete-message {
    text-align: center;
    font-size: 22px;
    font-weight: bold;
    color: #2f6f73;
    margin-bottom: 30px;
}

.section {
    border: 2px solid #2f6f73;
    border-radius: 15px;
    padding: 25px 30px;
    margin-bottom: 30px;
}

.section h3 {
    margin-top: 0;
    margin-bottom: 20px;
    font-size: 20px;
}

.row {
    margin-bottom: 12px;
    font-size: 18px;
}

.button-area {
    text-align: center;
    margin-top: 30px;
}

button {
    font-size: 18px;
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

    <h2>配達上限登録 完了</h2>

    <div class="complete-message">
        配達上限の登録が完了しました
    </div>

    <div class="section">
        <h3>登録内容</h3>

        <div class="row">
            業者コード：<strong>${contraCode}</strong>
        </div>
        <div class="row">
            業者名：<strong>${contraName}</strong>
        </div>
        <div class="row">
            対象年月：<strong>${targetYm}</strong>
        </div>
    </div>

    <div class="button-area">
        <button onclick="location.href='limitform.jsp'">
            戻る
        </button>
    </div>

</div>
</body>
</html>

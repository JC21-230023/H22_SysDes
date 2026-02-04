<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>業者情報編集</title>

<style>
body {
    font-family: "Hiragino Kaku Gothic ProN", "Meiryo", sans-serif;
    background-color: #ffffff;
}

.container {
    width: 700px;
    margin: 60px auto;
}

h1 {
    text-align: center;
    margin-bottom: 40px;
}

.form-box {
    background-color: #dff3ef;
    padding: 40px;
    border-radius: 10px;
}

label {
    display: block;
    font-weight: bold;
    margin-top: 20px;
}

input[type="text"],
input[type="password"] {
    width: 100%;
    padding: 8px;
    font-size: 16px;
    margin-top: 6px;
    box-sizing: border-box;
}

.button-area {
    text-align: center;
    margin-top: 40px;
}

input[type="submit"],
input[type="button"] {
    padding: 10px 30px;
    font-size: 16px;
    margin: 10px;
}
</style>

<script>
let originalValues = {};

function enableEdit() {
    const fields = ["contraName", "contraPhonum", "delareaCode", "password"];

    // 値保存＋編集可能化
    fields.forEach(id => {
        const el = document.getElementById(id);
        originalValues[id] = el.value;
        el.disabled = false;
    });

    // ボタン状態
    document.getElementById("editBtn").disabled = true;     // 編集：押せない
    document.getElementById("cancelBtn").disabled = false; // 編集解除：押せる
    document.getElementById("submitBtn").disabled = false; // 編集確定：押せる
}

function cancelEdit() {
    const fields = ["contraName", "contraPhonum", "delareaCode", "password"];

    // 値を元に戻す＋編集不可
    fields.forEach(id => {
        const el = document.getElementById(id);
        el.value = originalValues[id];
        el.disabled = true;
    });

    // ボタン状態
    document.getElementById("editBtn").disabled = false;    // 編集：押せる
    document.getElementById("cancelBtn").disabled = true;  // 編集解除：押せない
    document.getElementById("submitBtn").disabled = true;  // 編集確定：押せない
}

function togglePassword() {
    const pw = document.getElementById("password");
    pw.type = (pw.type === "password") ? "text" : "password";
}
</script>



</head>
<body>

<div class="container">

<h1>業者情報</h1>

<div class="form-box">

<form action="editresult" method="post">

    <!-- 業者コード（変更不可） -->
    <label>業者コード</label>
    <input type="text" value="${contraCode}" readonly>
    <input type="hidden" name="contraCode" value="${contraCode}">

    <!-- 業者名 -->
    <label>業者名</label>
    <input type="text" id="contraName" name="contraName"
           value="${contraName}" disabled>

    <!-- 連絡先 -->
    <label>連絡先</label>
    <input type="text" id="contraPhonum" name="contraPhonum"
           value="${contraPhonum}" disabled>

    <!-- 担当地域コード -->
    <label>担当地域コード</label>
    <input type="text" id="delareaCode" name="delareaCode"
           value="${delareaCode}" disabled>

    <!-- パスワード -->
    <label>登録パスワード</label>
    <input type="password" id="password" name="password"
           value="${password}" disabled>
    <input type="button" value="表示／非表示"
           onclick="togglePassword();">

    <!-- ボタン -->
    <div class="button-area">
        <input type="button" id="editBtn" value="編集"
       onclick="enableEdit();">

		<input type="button" id="cancelBtn" value="編集解除"
       onclick="cancelEdit();" disabled>

		<input type="submit" id="submitBtn"
       value="編集確定" disabled>
        
        <input type="button" value="戻る"
       	onclick="location.href='${pageContext.request.contextPath}/deliver/index3.jsp';">
        
    </div>

</form>

</div>
</div>

</body>
</html>

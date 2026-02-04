<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>配達サービス管理システム（業者）</title>

<style>
body {
    margin: 0;
    font-family: "Hiragino Kaku Gothic ProN", "Meiryo", sans-serif;
    background-color: #ffffff;
}

.container {
    text-align: center;
    margin-top: 100px;
}

/* タイトル */
h1 {
    font-size: 42px;
    font-weight: bold;
    margin-bottom: 80px;
}

/* メニューボタン */
.menu-button {
    display: block;                 /* 縦並び */
    width: 800px;
    margin: 25px auto;              /* ボタン間余白 */
    padding: 40px 0;
    font-size: 28px;
    text-decoration: none;
    color: #000;
    border: 2px solid #2f6f73;
    border-radius: 25px;
    transition: background-color 0.3s;
}

/* ホバー時 */
.menu-button:hover {
    background-color: #eef6f7;
}

/* ログアウトボタン */
.logout-button {
    display: block;
    width: 300px;
    margin: 60px auto 0;
    padding: 20px 0;
    font-size: 18px;
    text-decoration: none;
    color: #fff;
    background-color: #999;
    border-radius: 20px;
    transition: background-color 0.3s;
}

.logout-button:hover {
    background-color: #777;
}
</style>
</head>

<body>
<% String tes=(String)session.getAttribute("loginUser");%>


<!-- 
contraCodeで業者コードを送信

 -->
<div class="container">
    <h1>配達サービス管理システム
    	<div>(業者:<%=tes%>)</div>
    </h1>
	
    <a href="../limitform.jsp" class="menu-button">
        配達上限登録
    </a>

    <a href="../EditFormServlet" class="menu-button">
    	業者情報確認・編集
	</a>
    

    <a href="../delivers" class="menu-button">
        翌日の配達依頼リスト表示
    </a>

    <a href="../deliverystatusinput.jsp" class="menu-button">
        配達ステータス登録
    </a>
    <br><br>
	<a href="../kadai.html">他機能確認</a>

    <!-- ログアウト -->
    <a href="./login.jsp" class="logout-button"> 
        ログアウト
    </a>
</div>
</body>
</html>

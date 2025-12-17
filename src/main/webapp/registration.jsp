<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List,java.util.Map"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>登録完了</title>

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

.reserv-number {
    text-align: center;
    font-size: 26px;
    font-weight: bold;
    background-color: #eef6f7;
    border: 2px solid #2f6f73;
    border-radius: 15px;
    padding: 20px;
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
    margin-bottom: 10px;
}

ul {
    padding-left: 20px;
}

li {
    margin-bottom: 6px;
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

<h2>登録完了</h2>

<div class="complete-message">
予約が完了しました
</div>

<div class="reserv-number">
予約番号：${reservCode}
</div>

<!-- お客様情報 -->
<div class="section">
<h3>お客様情報</h3>
<div class="row">郵便番号：${postCode}</div>
<div class="row">住所：${address}</div>
<div class="row">氏名：${name}</div>
<div class="row">電話番号：${phoneNum}</div>
</div>

<!-- 家具情報 -->
<div class="section">
<h3>家具情報</h3>
<div class="row">家具数：${furnitureCount} 点</div>

<ul>
<%
List<Map<String,String>> furnitureList =
    (List<Map<String,String>>) request.getAttribute("furnitureList");

if (furnitureList != null) {
    for (Map<String,String> f : furnitureList) {
%>
<li>
    家具番号：<%= f.get("code") %> ／ 家具名：<%= f.get("name") %>
</li>
<%
    }
}
%>
</ul>
</div>

<!-- 配達情報 -->
<div class="section">
<h3>配達情報</h3>
<div class="row">配達日：${hopeDate}</div>
<div class="row">時間帯：${hopeTime}</div>
</div>

<div class="button-area">
<button type="button" onclick="location.href='index1.html'">
TOPに戻る
</button>
</div>

</div>
</body>
</html>

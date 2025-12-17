<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>登録完了</title>
</head>
<body>

<h2>登録完了</h2>

<!-- 受付番号は非表示 -->
<p>予約番号：${reservCode}</p>
<p>郵便番号：${postCode}</p>
<p>住所：${address}</p>
<p>氏名：${name}</p>
<p>電話番号：${phoneNum}</p>

<p>家具数：${furnitureCount} 点</p>
<ul>
<%
List<String> furnitureList = (List<String>) request.getAttribute("furnitureList");
if (furnitureList != null) {
    for (String f : furnitureList) {
%>
<li><%= f %></li>
<%
    }
}
%>
</ul>

<p>配達日：${hopeDate}</p>
<p>時間帯：${hopeTime}</p>

<button type="button" onclick="location.href='index1.html'">TOPに戻る</button>

</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>登録内容確認</title>
</head>
<body>

<h2>登録内容</h2>

<h3>お客様情報</h3>
<p>郵便番号：${zipcode}</p>
<p>住所：${address}</p>
<p>氏名：${name}</p>
<p>電話番号：${tel}</p>

<h3>家具情報</h3>
<p>家具数：${furnitureCount} 点</p>

<ul>
<%
List<String> list = (List<String>) request.getAttribute("furnitureList");
for (String f : list) {
%>
  <li><%= f %></li>
<% } %>
</ul>

<hr>

<h2>配達希望日時</h2>

<form action="confirm" method="post">

  希望日：
  <input type="date" name="hopeDate" required><br><br>

  希望時間帯：
  <select name="hopeTime" required>
    <option value="">選択してください</option>
    <option value="午前">午前</option>
    <option value="午後">午後</option>
  </select><br><br>

  <!-- hiddenで引き回し -->
  <input type="hidden" name="zipcode" value="${zipcode}">
  <input type="hidden" name="address" value="${address}">
  <input type="hidden" name="name" value="${name}">
  <input type="hidden" name="tel" value="${tel}">
  <input type="hidden" name="furnitureCount" value="${furnitureCount}">

  <%
  for (int i = 0; i < list.size(); i++) {
  %>
    <input type="hidden" name="furniture<%= i+1 %>" value="<%= list.get(i) %>">
  <% } %>

  <button type="button"
        onclick="location.href='delivaryform.html'">
  戻る
  </button>
  <button type="submit">登録</button>
</form>

</body>
</html>


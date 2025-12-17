<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>配達登録フォーム</title>

<script>
function updateFurniture() {
  const count = document.getElementById("furnitureCount").value;
  const area = document.getElementById("furnitureArea");
  area.innerHTML = "";

  <%-- 家具番号の復元 --%>
  <% 
     List<String> furnitureCodes =
         (List<String>) request.getAttribute("furnitureCodes");
  %>

  const values = [
    <% if (furnitureCodes != null) {
         for (int i = 0; i < furnitureCodes.size(); i++) {
    %>
      "<%= furnitureCodes.get(i) %>"<%= (i < furnitureCodes.size()-1) ? "," : "" %>
    <% } } %>
  ];

  for (let i = 1; i <= count; i++) {
    const v = values[i-1] ? values[i-1] : "";
    area.innerHTML +=
      `家具番号${i}：
       <input type="text" name="furniture${i}" value="${v}" required><br>`;
  }
}
</script>
</head>

<body onload="updateFurniture()">

<h2>配達登録フォーム</h2>

<%-- エラーメッセージ表示 --%>
<%
String errorMessage = (String) request.getAttribute("errorMessage");
if (errorMessage != null) {
%>
<p style="color:red;"><%= errorMessage %></p>
<%
}
%>

<form action="delivaryform" method="post">

  郵便番号：
  <input type="text" name="zipcode"
         value="<%= request.getAttribute("zipcode") != null ? request.getAttribute("zipcode") : "" %>"
         required><br>

  住所：
  <input type="text" name="address"
         value="<%= request.getAttribute("address") != null ? request.getAttribute("address") : "" %>"
         required><br>

  氏名：
  <input type="text" name="name"
         value="<%= request.getAttribute("name") != null ? request.getAttribute("name") : "" %>"
         required><br>

  電話番号：
  <input type="text" name="tel"
         value="<%= request.getAttribute("tel") != null ? request.getAttribute("tel") : "" %>"
         required><br>

  家具数：
  <input type="number"
         id="furnitureCount"
         name="furnitureCount"
         min="1" max="10"
         value="<%= request.getAttribute("furnitureCount") != null ? request.getAttribute("furnitureCount") : 1 %>"
         onchange="updateFurniture()"><br>

  <div id="furnitureArea"></div>

  <br>
  <button type="button" onclick="location.href='index1.html'">戻る</button>
  <button type="submit">確認</button>

</form>

</body>
</html>

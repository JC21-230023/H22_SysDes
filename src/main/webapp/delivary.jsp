<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Map"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>配達内容確認</title>
</head>
<body>

<h2>登録内容確認</h2>

<p>郵便番号：<%= request.getAttribute("postCode") %></p>
<p>住所：<%= request.getAttribute("address") %></p>
<p>氏名：<%= request.getAttribute("name") %></p>
<p>電話番号：<%= request.getAttribute("phoneNum") %></p>

<p>家具数：<%= request.getAttribute("furnitureCount") %> 点</p>
<ul>
<%
    List<Map<String,String>> furnitureList = (List<Map<String,String>>) request.getAttribute("furnitureList");
    if (furnitureList != null) {
        for (Map<String,String> f : furnitureList) {
%>
<li><%= f.get("code") %>：<%= f.get("name") %></li>
<%
        }
    }
%>
</ul>

<h3>担当業者</h3>
<%
    String contraName = (String) request.getAttribute("contraName");
    boolean canRegister = (contraName != null);
    if (canRegister) {
%>
<p><strong><%= contraName %></strong></p>
<% } else { %>
<p style="color:red;">配達可能な業者が見つかりません</p>
<% } %>

<form action="registration" method="post">
<div>
    希望日：
    <select name="hopeDate" <%= canRegister ? "required" : "disabled" %>>
        <option value="">選択してください</option>
        <%
            List<String> availableDateList = (List<String>) request.getAttribute("availableDateList");
            if (availableDateList != null) {
                for (String d : availableDateList) {
        %>
        <option value="<%= d %>"><%= d %></option>
        <%
                }
            }
        %>
    </select>
</div>

<div>
    希望時間帯：
    <select name="hopeTime" <%= canRegister ? "required" : "disabled" %>>
        <option value="">選択してください</option>
        <option value="AM">午前（9:00～12:00）</option>
        <option value="PM">午後（13:00～17:00）</option>
    </select>
</div>

<!-- hidden fields -->
<input type="hidden" name="postCode" value="<%= request.getAttribute("postCode") %>">
<input type="hidden" name="address" value="<%= request.getAttribute("address") %>">
<input type="hidden" name="name" value="<%= request.getAttribute("name") %>">
<input type="hidden" name="phoneNum" value="<%= request.getAttribute("phoneNum") %>">
<input type="hidden" name="furnitureCount" value="<%= request.getAttribute("furnitureCount") %>">
<input type="hidden" name="contraCode" value="<%= request.getAttribute("contraCode") %>">

<%
    if (furnitureList != null) {
        for (int i = 0; i < furnitureList.size(); i++) {
            Map<String,String> f = furnitureList.get(i);
%>
<input type="hidden" name="furniture<%= i+1 %>" value="<%= f.get("code") %>">
<%
        }
    }
%>

<br>
<button type="button" onclick="location.href='delivaryform.html'">戻る</button>
<%
    if (canRegister) {
%>
<button type="submit">登録</button>
<% } else { %>
<button type="button" disabled>登録</button>
<% } %>

</form>
</body>
</html>

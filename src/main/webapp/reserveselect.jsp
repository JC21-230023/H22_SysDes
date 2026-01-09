<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List, java.util.Map, java.time.LocalDateTime, java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>予約内容確認</title>

<style>
body {
    font-family: "Meiryo", sans-serif;
    background: #ffffff;
}
.container {
    width: 800px;
    margin: 50px auto;
}
h2 {
    text-align: center;
    margin-bottom: 30px;
}
.section {
    border: 2px solid #2f6f73;
    border-radius: 15px;
    padding: 25px;
    margin-bottom: 30px;
}
.row {
    margin-bottom: 10px;
}
table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 15px;
}
th, td {
    border: 1px solid #ccc;
    padding: 10px;
    text-align: center;
}
th {
    background: #eef5f9;
}
.button-area {
    text-align: center;
    margin-top: 30px;
}
button {
    padding: 12px 40px;
    font-size: 18px;
    border-radius: 10px;
    cursor: pointer;
    margin: 0 10px;
}

/* 更新 */
.update-btn {
    border: 2px solid #2980b9;
    background: #ffffff;
    color: #2980b9;
}
.update-btn:hover {
    background: #e8f2fb;
}

/* キャンセル */
.cancel-btn {
    border: 2px solid #c0392b;
    background: #ffffff;
    color: #c0392b;
}
.cancel-btn:hover {
    background: #fdecea;
}

/* 戻る */
.back-btn {
    border: 2px solid #999;
    background: #ffffff;
}
.back-btn:hover {
    background: #f0f0f0;
}
</style>
</head>

<body>
<div class="container">

<h2>予約内容確認</h2>

<div class="section">
    <div class="row">予約番号：<strong><%= request.getAttribute("reservCode") %></strong></div>
    <div class="row">郵便番号：<%= request.getAttribute("postCode") %></div>
    <div class="row">住所：<%= request.getAttribute("address") %></div>
    <div class="row">氏名：<%= request.getAttribute("name") %></div>
    <div class="row">電話番号：<%= request.getAttribute("phoneNum") %></div>
    <div class="row">配達業者：<%= request.getAttribute("contraName") %></div>
    <div class="row">配達希望日：
        <%
            LocalDateTime deliveryDatetime =
                (LocalDateTime) request.getAttribute("deliveryDatetime");
            if (deliveryDatetime != null) {
                DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm");
                out.print(deliveryDatetime.format(formatter));
            } else {
                out.print("未設定");
            }
        %>
    </div>
    <div class="row">家具の個数：<%= request.getAttribute("furnQuantity") %> 点</div>
</div>

<div class="section">
<h3>家具一覧</h3>

<table>
<tr>
    <th>家具番号</th>
    <th>家具名</th>
</tr>

<%
List<Map<String,String>> furnList =
    (List<Map<String,String>>) request.getAttribute("furnList");

if (furnList != null && !furnList.isEmpty()) {
    for (Map<String,String> f : furnList) {
%>
<tr>
    <td><%= f.get("code") %></td>
    <td><%= f.get("name") %></td>
</tr>
<%
    }
} else {
%>
<tr>
    <td colspan="2">家具情報はありません</td>
</tr>
<%
}
%>
</table>
</div>

<!-- ボタンエリア -->
<div class="button-area">

    <!-- 戻る -->
    <button type="button"
            class="back-btn"
            onclick="location.href='reservesearch.jsp'">
        戻る
    </button>
    
    <!-- 更新 -->
    <form action="update" method="post" style="display:inline;">
        <input type="hidden" name="reservationCode"
               value="<%= request.getAttribute("reservCode") %>">
        <button type="submit" class="update-btn">更新する</button>
    </form>

    <!-- キャンセル -->
    <form action="cancelresult" method="post" style="display:inline;">
        <input type="hidden" name="reservationCode"
               value="<%= request.getAttribute("reservCode") %>">
        <button type="submit" class="cancel-btn">キャンセルする</button>
    </form>

</div>

</div>
</body>
</html>

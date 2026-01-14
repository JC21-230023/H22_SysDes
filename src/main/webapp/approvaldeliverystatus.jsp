<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List,java.util.Map" %>

<html>
<head>
<title>配達ステータスリスト</title>
<style>
body {
    font-family: sans-serif;
    background-color: #fff6e5;
}

.header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
}

.box {
    border: 1px solid #999;
    padding: 10px;
    background: #fff;
}

.status-box label {
    display: block;
}

.yellow-btn {
    background: yellow;
    border: 1px solid #999;
    padding: 6px 12px;
    font-weight: bold;
    cursor: pointer;
}

table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 10px;
}

th, td {
    border: 1px solid #333;
    padding: 4px;
    text-align: center;
}

.footer {
    margin-top: 10px;
    text-align: center;
}

.error {
    color: red;
    font-weight: bold;
}
</style>
</head>

<body>


<div class="header">
	<!-- 左上：戻るボタン（単独） -->
    <div>
        <button type="button" onclick="history.back()">戻る</button>
    </div>
    <!-- 左側：予定日カレンダー（GET） -->
    <form action="ApprovalDeliveryStatus" method="get">
        <div class="box">
            <b>予定日</b><br>
            <input type="date" name="deliveryDate"
                   value="<%= request.getParameter("deliveryDate") != null
                            ? request.getParameter("deliveryDate")
                            : "" %>">
            <br><br>
            <input type="submit" value="表示">
        </div>
    </form>

    <!-- 配達ステータス絞り込み（GET） -->
	<form action="ApprovalDeliveryStatus" method="get">
	    <div class="box status-box">
	        <b>配達ステータス</b><br>
	
	        <label>
	            <input type="checkbox" name="status" value="01"
	            <%= request.getParameterValues("status") != null &&
	                java.util.Arrays.asList(request.getParameterValues("status")).contains("01")
	                ? "checked" : "" %>>
	            正常
	        </label>
	
	        <label>
	            <input type="checkbox" name="status" value="02"
	            <%= request.getParameterValues("status") != null &&
	                java.util.Arrays.asList(request.getParameterValues("status")).contains("02")
	                ? "checked" : "" %>>
	            不在
	        </label>
	
	        <label>
	            <input type="checkbox" name="status" value="03"
	            <%= request.getParameterValues("status") != null &&
	                java.util.Arrays.asList(request.getParameterValues("status")).contains("03")
	                ? "checked" : "" %>>
	            再配
	        </label>
	
	        <br>
	        <input type="submit" value="絞り込み">
	    </div>
	</form>
</div>

<b>・未承認リスト</b>

<%
    String errorMsg = (String)request.getAttribute("errorMsg");
    if (errorMsg != null) {
%>
    <div class="error"><%= errorMsg %></div>
<%
    }
%>

<form action="ApprovalDeliveryStatus" method="post">
<table>
<tr>
    <th>予約番号</th>
    <th>受付番号</th>
    <th>家具数</th>
    <th>予定日</th>
    <th>配達日</th>
    <th>状態</th>
    <th>備考</th>
    <th>承認</th>
</tr>

<%
    List<Map<String, Object>> list =
        (List<Map<String, Object>>)request.getAttribute("reservList");

    if (list != null) {
        for (Map<String, Object> row : list) {
%>
<tr>
    <td><%= row.get("RESERV_CODE") %></td>
    <td><%= row.get("ORDER_NUM") %></td>
    <td>5</td>
    <td><%= row.get("DELIVERY_DATETIME") %></td>
    <td><%= row.get("DEL_COMP_DATE") %></td>
    <td><%= row.get("DEL_STATUS") %></td>
    <td><%= row.get("REMARK") %></td>
    <td>
        <input type="checkbox" name="approveCodes"
               value="<%= row.get("RESERV_CODE") %>">
    </td>
</tr>
<%
        }
    }
%>
</table>

<div class="footer">
    <button type="button" onclick="location.href='top.jsp'">
        TOPに戻る
    </button>
    <input type="submit" value="承認">
</div>

</form>

</body>
</html>
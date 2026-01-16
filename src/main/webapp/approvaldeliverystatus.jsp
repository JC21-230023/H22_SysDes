<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List,java.util.Map" %>

<%
    String approval = request.getParameter("approval");
    if (approval == null || approval.isEmpty()) {
        approval = "N"; // デフォルト未承認
    }
    boolean isApproved = "Y".equals(approval);
%>

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

<!-- ================= ヘッダー ================= -->
<div class="header">

    <!-- 戻る -->
    <button
	    type="button"
	    onclick="history.back()"
	    style="
	        padding: 2px 6px;
	        font-size: 12px;
	        height: 24px;
	        line-height: 20px;
	        width: auto;
	        display: inline-block;
	        vertical-align: middle;
	    "
	>
	戻る
	</button>
	
    <!-- 承認状態切替 -->
    <div>
        <form action="ApprovalDeliveryStatus" method="get" style="display:inline;">
            <input type="hidden" name="approval" value="N">
            <input type="submit" value="未承認リスト">
        </form>

        <form action="ApprovalDeliveryStatus" method="get" style="display:inline;">
            <input type="hidden" name="approval" value="Y">
            <input type="submit" value="承認済みリスト">
        </form>
    </div>

    <!-- 1日検索 -->
    <form action="ApprovalDeliveryStatus" method="get">
        <div class="box">
            <b>予定日（1日）</b><br>
            <input type="date" name="singleDate"
                   value="<%= request.getParameter("singleDate") != null
                            ? request.getParameter("singleDate") : "" %>">
            <input type="hidden" name="approval" value="<%= approval %>">
            <br><br>
            <input type="submit" value="表示">
        </div>
    </form>

    <!-- 期間検索 -->
    <form action="ApprovalDeliveryStatus" method="get">
        <div class="box">
            <b>予定日（期間）</b><br>
            開始日<br>
            <input type="date" name="fromDate"
                   value="<%= request.getParameter("fromDate") != null
                            ? request.getParameter("fromDate") : "" %>"><br><br>
            終了日<br>
            <input type="date" name="toDate"
                   value="<%= request.getParameter("toDate") != null
                            ? request.getParameter("toDate") : "" %>">
            <input type="hidden" name="approval" value="<%= approval %>">
            <br><br>
            <input type="submit" value="表示">
        </div>
    </form>

    <!-- ステータス絞り込み -->
    <form action="ApprovalDeliveryStatus" method="get">
        <div class="box status-box">
            <b>配達ステータス</b><br>

            <input type="hidden" name="approval" value="<%= approval %>">
            <input type="hidden" name="singleDate"
                   value="<%= request.getParameter("singleDate") != null
                            ? request.getParameter("singleDate") : "" %>">
            <input type="hidden" name="fromDate"
                   value="<%= request.getParameter("fromDate") != null
                            ? request.getParameter("fromDate") : "" %>">
            <input type="hidden" name="toDate"
                   value="<%= request.getParameter("toDate") != null
                            ? request.getParameter("toDate") : "" %>">

            <label><input type="checkbox" name="status" value="01"> 正常</label>
            <label><input type="checkbox" name="status" value="02"> 不在</label>
            <label><input type="checkbox" name="status" value="03"> 異常</label>

            <br>
            <input type="submit" value="絞り込み">
        </div>
    </form>
</div>

<div style="display:flex; justify-content:space-between; align-items:center; margin-top:10px;">
    <b>・<%= isApproved ? "承認済みリスト" : "未承認リスト" %></b>

    <!-- 全件表示に戻す -->
    <form action="ApprovalDeliveryStatus" method="get" style="margin:0;">
        <input type="hidden" name="approval" value="<%= approval %>">
        <input
            type="submit"
            value="条件クリア"
            style="
                padding: 2px 6px;
                font-size: 12px;
                height: 24px;
            "
        >
    </form>
</div>

<%
    String errorMsg = (String)request.getAttribute("errorMsg");
    if (errorMsg != null) {
%>
<div class="error"><%= errorMsg %></div>
<% } %>

<!-- ================= 一覧 ================= -->
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
    <% if (!isApproved) { %><th>承認</th><% } %>
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
    <td><%= row.get("FURN_QUANTITY") %></td>
    <td><%= row.get("DELIVERY_DATETIME") %></td>
    <td><%= row.get("DEL_COMP_DATE") == null ? "" : row.get("DEL_COMP_DATE") %></td>
    <td>
        <%
            String s = (String)row.get("DEL_STATUS");
            if ("01".equals(s)) out.print("正常");
            else if ("02".equals(s)) out.print("不在");
            else if ("03".equals(s)) out.print("異常");
        %>
    </td>
    <td><%= row.get("REMARK") == null ? "" : row.get("REMARK") %></td>
    <% if (!isApproved) { %>
    <td>
        <input type="checkbox" name="approveCodes"
               value="<%= row.get("RESERV_CODE") %>">
    </td>
    <% } %>
</tr>
<%
        }
    }
%>
</table>

<% if (!isApproved) { %>
<div class="footer">
    <input type="submit" value="承認">
</div>
<% } %>

</form>
</body>
</html>

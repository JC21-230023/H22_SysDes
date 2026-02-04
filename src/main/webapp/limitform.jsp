<%@ page contentType="text/html; charset=UTF-8" %>
<%
/* =========================
   ログインチェック＋業者コード取得
   ========================= */
String contraCode = (String)session.getAttribute("contraCode");
if (contraCode == null) {
    response.sendRedirect(request.getContextPath() + "/deliver/login.jsp");
    return;
}
%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>配達上限登録</title>

<style>
body{
    font-family: Meiryo;
    background: #f6f4ee;
    padding: 20px;
}
.container{
    max-width: 900px;
    margin: auto;
    background: #fff;
    padding: 20px;
    border-radius: 10px;
}
table{
    border-collapse: collapse;
    width: 100%;
}
th, td{
    border: 1px solid #ccc;
    padding: 8px;
    text-align: center;
}
th{
    background: #eef5f9;
}
input[type=number]{
    width: 80px;
}
.actions{
    text-align: center;
    margin-top: 15px;
}

/* 土日色分け */
.sun { background: #ffe4ec; }   /* 日曜 */
.sat { background: #e6f3ff; }   /* 土曜 */
</style>

<script>
function build(){
  const y = document.getElementById("year").value;
  const m = document.getElementById("month").value;

  const tbody = document.getElementById("tbody");
  tbody.innerHTML = "";

  const lastDay = new Date(y, m, 0).getDate();

  for(let d = 1; d <= lastDay; d++){
    const date = new Date(y, m - 1, d);
    const dow = date.getDay(); // 0:日, 6:土
    const iso = y + "-" + String(m).padStart(2,'0') + "-" + String(d).padStart(2,'0');

    let cls = "";
    if(dow === 0) cls = "sun";
    if(dow === 6) cls = "sat";

    const tr = document.createElement("tr");
    if (cls) tr.className = cls;

    tr.innerHTML =
      "<td>" + iso + "</td>" +
      "<td>" + ["日","月","火","水","木","金","土"][dow] + "</td>" +
      "<td><input type='number' name='am_" + iso + "' min='0' value='10'></td>" +
      "<td><input type='number' name='pm_" + iso + "' min='0' value='10'></td>";

    tbody.appendChild(tr);
  }
}
</script>

</head>

<body onload="build()">
<div class="container">

<h2>配達上限登録</h2>

<form action="limit" method="post">

<!-- 業者コード（表示のみ） -->
<p>
業者コード：
<strong><%= contraCode %></strong>
</p>

<!-- 年月選択 -->
<p>
<select id="year" name="year" onchange="build()">
<%
int nowYear = java.time.LocalDate.now().getYear();
for(int i = nowYear - 1; i <= nowYear + 1; i++){
%>
<option value="<%=i%>" <%= (i == nowYear ? "selected" : "") %>><%=i%></option>
<% } %>
</select>
年

<select id="month" name="month" onchange="build()">
<%
int nowMonth = java.time.LocalDate.now().getMonthValue();
for(int i = 1; i <= 12; i++){
%>
<option value="<%=i%>" <%= (i == nowMonth ? "selected" : "") %>><%=i%></option>
<% } %>
</select>
月
</p>

<!-- 上限入力テーブル -->
<table>
<thead>
<tr>
<th>日付</th>
<th>曜日</th>
<th>午前</th>
<th>午後</th>
</tr>
</thead>
<tbody id="tbody"></tbody>
</table>

<div class="actions">
<button type="button"
 onclick="location.href='${pageContext.request.contextPath}/deliver/index3.jsp'">
戻る
</button>
<button type="submit" name="mode" value="save">登録</button>
</div>

</form>
</div>
</body>
</html>

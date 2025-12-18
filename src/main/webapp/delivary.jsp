<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List,java.util.Map"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>配達内容確認</title>

<style>
body {
    margin: 0;
    font-family: "Hiragino Kaku Gothic ProN", "Meiryo", sans-serif;
    background-color: #ffffff;
}

/* 全体 */
.container {
    width: 900px;
    margin: 40px auto;
}

/* 見出し */
h2 {
    text-align: center;
    font-size: 28px;
    margin-bottom: 30px;
}

/* セクション枠 */
.section {
    border: 2px solid #2f6f73;
    border-radius: 15px;
    padding: 25px 30px;
    margin-bottom: 30px;
}

/* セクションタイトル */
.section h3 {
    margin-top: 0;
    margin-bottom: 20px;
    font-size: 20px;
}

/* 表示行 */
.row {
    margin-bottom: 10px;
}

/* 家具リスト */
ul {
    padding-left: 20px;
}
li {
    margin-bottom: 6px;
}

/* ===== カレンダー ===== */
.calendar {
    display: grid;
    grid-template-columns: repeat(7, 1fr);
    gap: 6px;
    margin-top: 15px;
}

.cal-head {
    text-align: center;
    font-weight: bold;
}

.day {
    text-align: center;
    padding: 12px 0;
    border-radius: 8px;
}

.day.disabled {
    background-color: #eee;
    color: #999;
}

.day.available {
    background-color: #dff3f3;
    cursor: pointer;
}

.day.available:hover {
    background-color: #bfe7ea;
}

.day.selected {
    background-color: #2f6f73;
    color: #fff;
}

/* セレクト */
select {
    padding: 6px;
    font-size: 16px;
}

/* ボタン */
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
    margin: 0 15px;
}

button:hover {
    background-color: #eef6f7;
}

button:disabled {
    color: #999;
    border-color: #ccc;
    background-color: #f5f5f5;
    cursor: not-allowed;
}
</style>
</head>

<body>
<div class="container">

<h2>登録内容確認</h2>

<!-- お客様情報 -->
<div class="section">
<h3>お客様情報</h3>
<div class="row">郵便番号：<%= request.getAttribute("postCode") %></div>
<div class="row">住所：<%= request.getAttribute("address") %></div>
<div class="row">氏名：<%= request.getAttribute("name") %></div>
<div class="row">電話番号：<%= request.getAttribute("phoneNum") %></div>
</div>

<!-- 家具情報 -->
<div class="section">
<h3>家具情報</h3>
<div class="row">家具数：<%= request.getAttribute("furnitureCount") %> 点</div>

<ul>
<%
List<Map<String,String>> furnitureList =
    (List<Map<String,String>>) request.getAttribute("furnitureList");

if (furnitureList != null) {
    for (Map<String,String> f : furnitureList) {
%>
<li>家具番号：<%= f.get("code") %> ／ 家具名：<%= f.get("name") %></li>
<%
    }
}
%>
</ul>
</div>

<!-- 業者情報 -->
<div class="section">
<h3>担当業者</h3>
<%
String contraName = (String) request.getAttribute("contraName");
boolean canRegister = (contraName != null);
%>

<% if (canRegister) { %>
<strong><%= contraName %></strong>
<% } else { %>
<p style="color:red;">配達可能な業者が見つかりません</p>
<% } %>
</div>

<!-- 登録フォーム -->
<form action="registration" method="post">

<div class="section">
<h3>配達希望日</h3>

<!-- ★ 月切替 -->
<div style="text-align:center;margin-bottom:10px;">
  <button type="button" onclick="changeMonth(-1)">＜ 前月</button>
  <span id="monthLabel" style="margin:0 20px;font-weight:bold;"></span>
  <button type="button" onclick="changeMonth(1)">次月 ＞</button>
</div>

<div id="calendar" class="calendar"></div>
<input type="hidden" name="hopeDate" id="hopeDate">

<!-- ★ エラーメッセージ -->
<p id="dateError" style="color:red;display:none;">配達希望日を選択してください</p>

<div class="row" style="margin-top:20px;">
希望時間帯：
<select name="hopeTime" id="hopeTime" <%= canRegister ? "required" : "disabled" %>>
<option value="">選択してください</option>
<option value="AM">午前（9:00～12:00）</option>
<option value="PM">午後（13:00～17:00）</option>
</select>
<p id="timeError" style="color:red;display:none;">希望時間帯を選択してください</p>
</div>
</div>


<!-- hidden -->
<input type="hidden" name="postCode" value="<%= request.getAttribute("postCode") %>">
<input type="hidden" name="address" value="<%= request.getAttribute("address") %>">
<input type="hidden" name="name" value="<%= request.getAttribute("name") %>">
<input type="hidden" name="phoneNum" value="<%= request.getAttribute("phoneNum") %>">
<input type="hidden" name="furnitureCount" value="<%= request.getAttribute("furnitureCount") %>">
<input type="hidden" name="contraCode" value="<%= request.getAttribute("contraCode") %>">

<input type="hidden" name="furnitureSize" value="<%= furnitureList.size() %>">
<%
for (int i = 0; i < furnitureList.size(); i++) {
    Map<String,String> f = furnitureList.get(i);
%>
<input type="hidden" name="furnitureCode<%= i %>" value="<%= f.get("code") %>">
<input type="hidden" name="furnitureName<%= i %>" value="<%= f.get("name") %>">
<%
}
%>

<div class="button-area">
<button type="button" onclick="location.href='delivaryform.jsp'">戻る</button>
<% if (canRegister) { %>
<button type="submit">登録</button>
<% } else { %>
<button type="button" disabled>登録</button>
<% } %>
</div>

</form>

</div>

<!-- ===== JavaScript ===== -->
<script>
/* ===== 配達可能日 ===== */
const availableDates = [
<%
List<String> availableDateList =
    (List<String>) request.getAttribute("availableDateList");
if (availableDateList != null) {
    for (int i = 0; i < availableDateList.size(); i++) {
%>
"<%= availableDateList.get(i) %>"<%= (i < availableDateList.size()-1) ? "," : "" %>
<%
    }
}
%>
];

const calendar   = document.getElementById("calendar");
const hopeDate   = document.getElementById("hopeDate");
const monthLabel = document.getElementById("monthLabel");

/* 表示中の月 */
let current = new Date();

/* ===== 選択可能な最小日：今日 + 2日（翌々日） ===== */
const minDate = new Date();
minDate.setHours(0,0,0,0);
minDate.setDate(minDate.getDate() + 2);

/* ===== 月切替 ===== */
function changeMonth(diff){
  current.setMonth(current.getMonth() + diff);
  renderCalendar();
}

/* ===== カレンダー描画 ===== */
function renderCalendar(){
  calendar.innerHTML = "";

  const year  = current.getFullYear();
  const month = current.getMonth();
  monthLabel.textContent = year + "年 " + (month + 1) + "月";

  /* 曜日ヘッダ */
  ["日","月","火","水","木","金","土"].forEach(d => {
    const h = document.createElement("div");
    h.textContent = d;
    h.className = "cal-head";
    calendar.appendChild(h);
  });

  /* 月初の空白 */
  const first = new Date(year, month, 1);
  for(let i = 0; i < first.getDay(); i++){
    calendar.appendChild(document.createElement("div"));
  }

  /* 日付セル */
  const last = new Date(year, month + 1, 0);
  for(let d = 1; d <= last.getDate(); d++){

    const cell = document.createElement("div");
    cell.textContent = d;
    cell.className = "day";

    const mm  = ("0" + (month + 1)).slice(-2);
    const dd  = ("0" + d).slice(-2);
    const ymd = year + "-" + mm + "-" + dd;

    const cellDate = new Date(year, month, d);
    cellDate.setHours(0,0,0,0);

    /* ===== 判定 ===== */
    if (
      availableDates.includes(ymd) &&
      cellDate >= minDate
    ) {
      // 明後日以降 & 配達可能
      cell.classList.add("available");
      cell.onclick = () => {
        document.querySelectorAll(".day.selected")
          .forEach(el => el.classList.remove("selected"));

        cell.classList.add("selected");
        hopeDate.value = ymd;
        document.getElementById("dateError").style.display = "none";
      };
    } else {
      // 今日・明日・過去日・業者不可日
      cell.classList.add("disabled");
    }

    calendar.appendChild(cell);
  }
}

/* ===== 未選択チェック ===== */
document.querySelector("form").addEventListener("submit", function(e){
  let ok = true;

  if(!hopeDate.value){
    document.getElementById("dateError").style.display = "block";
    ok = false;
  }

  if(!document.getElementById("hopeTime").value){
    document.getElementById("timeError").style.display = "block";
    ok = false;
  }

  if(!ok) e.preventDefault();
});

/* 初期表示 */
renderCalendar();
</script>



</body>
</html>

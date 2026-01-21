<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>配達日変更</title>

<style>
body {
    font-family: "Hiragino Kaku Gothic ProN", "Meiryo", sans-serif;
    background-color: #ffffff;
}

.container {
    width: 800px;
    margin: 40px auto;
}

h2 {
    text-align: center;
    margin-bottom: 30px;
}

/* ===== カレンダー ===== */
.calendar {
    display: grid;
    grid-template-columns: repeat(7, 1fr);
    gap: 6px;
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
    margin: 0 10px;
}

button:hover {
    background-color: #eef6f7;
}

select {
    font-size: 16px;
    padding: 6px;
}
</style>
</head>

<body>
<div class="container">

<h2>配達日変更</h2>

<form action="updateExecute" method="post">

<!-- 月切替 -->
<div style="text-align:center;margin-bottom:15px;">
  <button type="button" onclick="changeMonth(-1)">＜ 前月</button>
  <span id="monthLabel" style="margin:0 20px;font-weight:bold;"></span>
  <button type="button" onclick="changeMonth(1)">次月 ＞</button>
</div>

<div id="calendar" class="calendar"></div>

<input type="hidden" name="hopeDate" id="hopeDate">
<input type="hidden" name="reservationCode"
       value="<%= request.getAttribute("reservCode") %>">

<p id="dateError" style="color:red;display:none;text-align:center;">
配達希望日を選択してください
</p>

<!-- ===== 時間帯選択 ===== -->
<div style="text-align:center;margin-top:20px;">
  希望時間帯：
  <select name="hopeTime" id="hopeTime">
    <option value="">選択してください</option>
    <option value="AM">午前（9:00～12:00）</option>
    <option value="PM">午後（13:00～17:00）</option>
  </select>
</div>

<p id="timeError" style="color:red;display:none;text-align:center;">
時間帯を選択してください
</p>

<div class="button-area">
  <button type="button" onclick="location.href='reserveselect.jsp'">戻る</button>
  <button type="submit">更新する</button>
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

/* ===== 翌日以降のみ選択可 ===== */
const minDate = new Date();
minDate.setHours(0,0,0,0);
minDate.setDate(minDate.getDate() + 1);

/* 月切替 */
function changeMonth(diff){
  current.setMonth(current.getMonth() + diff);
  renderCalendar();
}

/* カレンダー描画 */
function renderCalendar(){
  calendar.innerHTML = "";

  const year  = current.getFullYear();
  const month = current.getMonth();
  monthLabel.textContent = year + "年 " + (month + 1) + "月";

  ["日","月","火","水","木","金","土"].forEach(d => {
    const h = document.createElement("div");
    h.textContent = d;
    h.className = "cal-head";
    calendar.appendChild(h);
  });

  const first = new Date(year, month, 1);
  for(let i = 0; i < first.getDay(); i++){
    calendar.appendChild(document.createElement("div"));
  }

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

    if (availableDates.includes(ymd) && cellDate >= minDate) {
      cell.classList.add("available");
      cell.onclick = () => {
        document.querySelectorAll(".day.selected")
          .forEach(el => el.classList.remove("selected"));

        cell.classList.add("selected");
        hopeDate.value = ymd;
        document.getElementById("dateError").style.display = "none";
      };
    } else {
      cell.classList.add("disabled");
    }

    calendar.appendChild(cell);
  }
}

/* 未選択チェック */
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

renderCalendar();
</script>

</body>
</html>

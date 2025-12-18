<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>配達受付フォーム</title>
<link rel="stylesheet" href="delivaryform.css">

<script>
/* ============================
 * 郵便番号 → 住所検索（ボタン）
 * ============================ */
async function searchAddress() {
    const zipInput = document.getElementsByName("zipcode")[0];
    const addressInput = document.getElementsByName("address")[0];

    const zip = zipInput.value.replace(/[^0-9]/g, "");

    if (zip.length !== 7) {
        alert("郵便番号は7桁で入力してください");
        return;
    }

    try {
        const res = await fetch(
            "https://postcode.teraren.com/postcodes/" + zip + ".json"
        );

        if (!res.ok) {
            alert("住所が見つかりませんでした");
            return;
        }

        const data = await res.json();

        const addr =
            (data.prefecture || "") +
            (data.city || "") +
            (data.suburb || "");

        if (addr !== "") {
            addressInput.value = addr;
        } else {
            alert("住所が見つかりませんでした");
        }
    } catch (e) {
        alert("住所検索に失敗しました");
        console.error(e);
    }
}

function updateFurnitureFields() {
    const count = document.getElementById("furnitureCount").value;
    const items = document.getElementsByClassName("furn-item");

    for (let i = 0; i < items.length; i++) {
        if (i < count) {
            items[i].style.display = "flex";
            items[i].querySelector("input").required = true;
        } else {
            items[i].style.display = "none";
            items[i].querySelector("input").required = false;
            items[i].querySelector("input").value = "";
        }
    }
}
</script>
<style>
body {
  font-family: "Meiryo", sans-serif;
  background-color: #f5f5f5;
}

.container {
  width: 800px;
  margin: 30px auto;
  background: #ffffff;
  padding: 25px 40px;
  border-radius: 8px;
}

h2 {
  text-align: center;
  margin-bottom: 25px;
}

.section {
  margin-bottom: 30px;
}

.section h3 {
  border-left: 5px solid #3a8dde;
  padding-left: 10px;
  margin-bottom: 15px;
}

label {
  display: block;
  margin-top: 12px;
  font-weight: bold;
}

input[type="text"],
input[type="number"] {
  width: 100%;
  padding: 8px;
  margin-top: 5px;
  background-color: #e8f3ff;
  border: 1px solid #888;
  border-radius: 3px;
  box-sizing: border-box;
}

.furniture-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px 40px;
  margin-top: 15px;
}

.furn-item {
  display: flex;
  align-items: center;
}

.furn-item span {
  width: 20px;
  font-weight: bold;
}

.buttons {
  text-align: center;
  margin-top: 30px;
}

button {
  font-size: 16px;
  padding: 10px 30px;
  margin: 0 15px;
  border-radius: 8px;
  border: 2px solid #333;
  background-color: #fff;
  cursor: pointer;
}

button:hover {
  background-color: #e0e0e0;
}

.error {
  background-color: #ffe0e0;
  color: #c00;
  padding: 10px;
  border-radius: 5px;
  margin-bottom: 15px;
}

</style>
</head>
<body onload="updateFurnitureFields()">

<div class="container">

<h2>配達受付フォーム</h2>

<%-- エラーメッセージ --%>
<%
String errorMessage = (String) request.getAttribute("errorMessage");
if (errorMessage != null) {
%>
<div class="error"><%= errorMessage %></div>
<%
}
%>

<form action="delivaryform" method="post">

<!-- =========================
     お客様情報
========================= -->
<div class="section">
  <h3>お客様情報</h3>

  <label>郵便番号（半角7桁・ハイフンなし）</label>
  <div style="display:flex; gap:10px; align-items:center;">
     <input type="text" name="zipcode"
     value="<%= request.getAttribute("zipcode") != null ? request.getAttribute("zipcode") : "" %>"
     placeholder="例：9830013"
     style="flex:1;"
     required>

     <button type="button" onclick="searchAddress()"
          style="padding:6px 14px; font-size:14px;">
    検索
     </button>
   </div>



  <label>住所（全角）</label>
  <input type="text" name="address"
         value="<%= request.getAttribute("address") != null ? request.getAttribute("address") : "" %>"
         placeholder="例：宮城県仙台市青葉区花京院1丁目3番1号AIマンション302"
         required>

  <label>氏名（漢字・スペースなし）</label>
  <input type="text" name="name"
         value="<%= request.getAttribute("name") != null ? request.getAttribute("name") : "" %>"
         placeholder="例：東北太郎" required>

  <label>電話番号（半角数字・ハイフンなし）</label>
  <input type="text" name="tel"
         value="<%= request.getAttribute("tel") != null ? request.getAttribute("tel") : "" %>"
         placeholder="例：09012345678" required>
</div>

<!-- =========================
     家具情報
========================= -->
<div class="section">
  <h3>家具情報</h3>

  <label>家具数</label>
  <input type="number" id="furnitureCount" name="furnitureCount"
         min="1" max="10"
         value="<%= request.getAttribute("furnitureCount") != null ? request.getAttribute("furnitureCount") : 1 %>"
         onchange="updateFurnitureFields()" required>

  <div class="furniture-grid">
  <%
    List<String> furnitureCodes =
        (List<String>) request.getAttribute("furnitureCodes");

    for (int i = 1; i <= 10; i++) {
        String val = "";
        if (furnitureCodes != null && furnitureCodes.size() >= i) {
            val = furnitureCodes.get(i-1);
        }
  %>
    <div class="furn-item">
      <span><%= i %></span>
      <input type="text" name="furniture<%= i %>"
             value="<%= val %>"
             placeholder="例：F00001">
    </div>
  <%
    }
  %>
  </div>
</div>

<!-- =========================
     ボタン
========================= -->
<div class="buttons">
  <button type="button" onclick="location.href='index1.html'">戻る</button>
  <button type="submit">確認</button>
</div>

</form>
</div>

</body>
</html>


<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>配達予約登録（確認）</title>
  <style>
    body { font-family: "Segoe UI","Noto Sans JP",sans-serif; margin: 24px; }
    .container { max-width: 900px; margin: 0 auto; }
    table { border-collapse: collapse; width: 100%; }
    th, td { border: 1px solid #ccc; padding: 8px; }
    th { background: #f3f3f3; }
    h2 { margin-top: 0; }
  </style>
</head>
<body>
  <div class="container">
    <h2>申込内容の確認</h2>

    <h3>お客様情報</h3>
    <table>
      <tr><th>郵便番号</th><td>${zipcode}</td></tr>
      <tr><th>住所</th><td>${address}</td></tr>
      <tr><th>氏名</th><td>${name}</td></tr>
      <tr><th>電話番号</th><td>${phone}</td></tr>
    </table>

    <h3 style="margin-top:24px;">家具情報（合計：${itemCount} 点）</h3>
    <table>
      <thead>
        <tr>
          <th>#</th><th>品名</th><th>幅(cm)</th><th>奥行(cm)</th><th>高さ(cm)</th><th>重量(kg)</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach items="${items}" var="it" varStatus="s">
          <tr>
            <td><c:out value="${s.index + 1}" /></td>
            <td><c:out value="${it.name}" /></td>
            <td><c:out value="${it.width}" /></td>
            <td><c:out value="${it.depth}" /></td>
            <td><c:out value="${it.height}" /></td>
            <td><c:out value="${it.weight}" /></td>
          </tr>
        </c:forEach>
      </tbody>
    </table>

    <p style="margin-top:16px;">
      sample.html戻る（修正）</a>
    </p>
  </div>
</body>
</html>
``

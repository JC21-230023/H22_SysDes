<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>新規業者登録 完了</title>
    <style>
        .box {
            width: 450px;
            padding: 20px;
            margin: 20px auto;
            background-color: #e6f5f0;
            border-radius: 20px;
        }
        .center {
            text-align: center;
        }
        .red {
            color: red;
            font-weight: bold;
        }
    </style>
</head>

<body>

<h2 class="center">⑤ 新規業者登録が完了しました。</h2>

<div class="box">
    <p>業者名　：<c:out value="${traderName}" /></p>
    <p>連絡先　：<c:out value="${tel}" /></p>
    <p>
        担当地域コード：
        <c:choose>
            <c:when test="${empty areaCode}">
                <span class="red">形式未確定</span>
            </c:when>
            <c:otherwise>
                <c:out value="${areaCode}" />
            </c:otherwise>
        </c:choose>
    </p>
    <p>登録パスワード：<c:out value="${password}" /></p>

    <hr>

    <p class="red center">
        業者コードは <c:out value="${traderCode}" /> です。
    </p>
</div>

<div class="center">
    <form action="top.jsp" method="get">
        <input type="submit" value="TOPに戻る">
    </form>
</div>

</body>
</html>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>依頼リスト作成</title>
<!-- 配達依頼リスト表示.jsp -->
</head>
<%
	Object obj = request.getAttribute("CONTRA");
	List<String[]> list = (List<String[]>) obj;
%>


<body>
<header>
	配達依頼リスト作成
</header>

<form action="mklist",method="get">
	<div >
		<select name="deliverID">
			<%for(String[] contra:list){%>
			<option value=<%=contra[0]%>>
				<%=contra[1]%>
			</option>
			<%}%>
		</select>
	</div>
	<div><!--  配達日付-->
		<input type="date" name="date">
	</div>
	<div><!-- 印刷フラグ(未完) -->
		<input type="checkbox" name="printFlg">
		<label for="prontFlg">印刷番号</label>
	</div>
	<button type="submit">一覧表示</button>
</form>

</body>
</html>
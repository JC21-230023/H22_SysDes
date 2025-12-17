<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- 配達依頼リスト表示.jsp -->
</head>
<body>
<header>
	配達依頼リスト
<form action="/deliver/">
	<div >
		<select>
			
			<%for(配達業者:配達業者全件){%>
			<option value=<%//業者ID%>>
				<%//=配達業者 %>
			</option>
			<%//}%>
		</select>
	</div>
	<div><!--  配達日付-->
		<input type="date">
	</div>
	<button type="submit">一覧表示</button>
</form>

<div><%//＝配達件数 %></div>
</header>
<!-- 区切り -->
<table>
<tr>
	<td class="back1">No</td>
	<td class="back2">配達先情報</td>
	<td class="back3">予約番号</td>
	<td class="back4">家具の個数</td>
	<td class="back5">家具番号</td>
	
</tr>
<%
for (String[] ss : productList) {
%>
<tr>
	<td class="back1"><%=ss[0]%></td>
	<td class="back2"><%=ss[1]%></td>
	<td class="back3"><%=ss[2]%></td>
	<td class="back4"><%=ss[3]%></td>
	<td class="back5"><%=ss[4]%></td>
</tr>
<%
}%>
</table>
<button><a></a> </button>
</body>
</html>
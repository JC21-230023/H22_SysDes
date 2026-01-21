<%@page import="beans.Reserv.Furn"%>
<%@page import="java.util.List"%>
<%@page import="beans.Reserv"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<%
Object obj = request.getAttribute("RSV_LIST");
List<Reserv> rsv_List = (List<Reserv>) obj;

String contra=
	(String)request.getAttribute("CONTRA");
String date =
	(String)request.getAttribute("DATE");
String today =
(String)request.getAttribute("TODAY");
%>


<body>
<!-- 区切り -->

<header>
	<div>依頼リスト</div>
	<div>企業名：
		<div><%=contra %></div>
	</div>
	<div>
		<div>作成日
			<%= today%>
		</div>
		
		<div>配達予定日
			<%=date %>
		</div>
		
		<div>配達先件数
			<%=rsv_List.size()%>
		</div>
	</div>
</header>

<table>
<tr>
	<td class="back1">No</td>
	<td class="back2">配達先情報</td>
	<td class="back3">予約番号</td>
	<td class="back4">家具の個数</td>
	<td class="back5">家具番号</td>
	
</tr>
<%
int i=1;
for (Reserv ss : rsv_List) {
%>
<tr>
	<td class="back1"><%=i++%></td>
	<td class="back2">
		<div>
			<%=ss.getAddress()%>
		</div>
		
		<div>
			<%=ss.getName()%>
		</div>
	</td>
	<td class="back3"><%=ss.getRsv_code()%></td>
	<td class="back4"><%=ss.getFrunQuant()%></td>
	<td class="back5">
	<%for (Furn f: ss.getFurnList()){%>
		<div>
			<%=f.toString()%>
		</div>	
	<%} %>
	</td>
</tr>
<%
}%>
</table>
</body>
</html>
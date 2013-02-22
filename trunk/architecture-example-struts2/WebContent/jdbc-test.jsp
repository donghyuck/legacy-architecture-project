<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="java.util.*,architecture.ee.services.*,architecture.ee.web.util.WebApplicationHelper" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>

<%
SqlQueryClient  client =  WebApplicationHelper.getComponent(SqlQueryClient.class) ;    
List list = (List)client.unitOfWork("unitofwork.SqlQueryTest", "getTableNames", "%");
%>
<%= list  %>
<br>
<%
List list2 = (List)client.unitOfWork("unitofwork.SqlQueryTest", "getTableData", "V2_USER");
%>
<%= list2  %>
</body>
</html> 
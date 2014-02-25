<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="java.util.*,architecture.ee.web.navigator.*,architecture.ee.services.*,architecture.ee.web.util.WebApplicationHelper" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>

<%
MenuRepository  repo =  WebApplicationHelper.getComponent(MenuRepository.class) ;   
Menu menu = repo.getMenu(1);
repo.refershMenu(menu);
MenuComponent mc = repo.getMenuComponent(menu, "USER_MENU");
%>
<%=  mc %>
<br>

</body>
</html> 
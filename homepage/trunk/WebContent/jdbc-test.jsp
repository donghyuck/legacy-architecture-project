<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="java.util.*,architecture.ee.jdbc.sqlquery.*,architecture.ee.jdbc.sqlquery.factory.*,architecture.ee.web.util.WebApplicationHelper" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>

<%

SqlQueryFactory  client =  WebApplicationHelper.getComponent(SqlQueryFactory.class) ;   
SqlQuery query = client.createSqlQuery();
Map<String, Object> additionalParameters = new HashMap<String, Object>(4);
additionalParameters.put("TABLE_NAME", "V2_USER");
query.setAdditionalParameters(additionalParameters);
List<Map<String, Object>> list = query.queryForList("BUSINESS.SELECT_TABLE", new Object[]{1}, new int[]{ java.sql.Types.NUMERIC });

//helper.parameter(1, java.sql.Types.NUMERIC);

//List<Map<String, Object>> list = helper.list(query, "BUSINESS.SELECT_TABLE");
%>
<%= list  %>
</body>
</html> 
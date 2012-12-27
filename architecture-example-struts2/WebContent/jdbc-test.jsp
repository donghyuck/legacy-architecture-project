<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="javax.sql.DataSource"%>
<%@ page import="org.springframework.jndi.*"%>
<%@ page import="org.springframework.jndi.support.*"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>

<%
JndiTemplate jt =  new JndiTemplate();
//JndiLocatorDelegate jt = new JndiLocatorDelegate();
//jt.setResourceRef(false);

//DataSource ds = jt.lookup("jdbc/EHRD_DS" , DataSource.class);
//Object obj = jt.lookup("jdbc/EHRD_DS", DataSource.class);

JndiObjectFactoryBean fb = new JndiObjectFactoryBean();
fb.setExpectedType(javax.sql.DataSource.class);
fb.setJndiName("jdbc/EHRD_DS");
//fb.setResourceRef(true);
fb.afterPropertiesSet();
Object obj = fb.getObject();
boolean check = false ;
if ( obj instanceof DataSource )
	check = true;

%>
<%= obj.getClass().getName()  %>
<%= check %>

</body>
</html>
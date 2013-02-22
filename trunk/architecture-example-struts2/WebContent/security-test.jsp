<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="java.util.*,architecture.user.util.*,architecture.common.util.*,architecture.common.i18n.*, architecture.ee.services.*,architecture.ee.web.util.WebApplicationHelper" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>

<%
	I18nTextManager  mgr =  WebApplicationHelper.getComponent(I18nTextManager.class) ;    
   // mgr.refresh();
	Map<Locale, ResourceBundle> map = mgr.getResourceBundles();
	for( Locale l : map.keySet() ){
		ResourceBundle b = map.get(l);
		Enumeration<String> enums = b.getKeys();
		while( enums.hasMoreElements()){
			String key = enums.nextElement();
			String value = b.getString(key);		
		%>
		
		<%= key  %>=<%= value %><br/>
		<%
		}
	}
	
	
	
%>
<%= I18nTextUtils.getText("login.buttion.label", "UI", Locale.KOREA ) %>
<%= SecurityHelper.getAuthentication().getAuthorities()  %>
<br>
</body>
</html> 
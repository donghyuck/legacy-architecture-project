<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title><decorator:title default="Main Page Templage ..." /></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="-1">        
    <script src="<%= request.getContextPath()  %>/js/yepnope.js"></script>
    <link href="<%= request.getContextPath()  %>/styles/normalize.css" rel="stylesheet" type="text/css" />
    <link href="<%= request.getContextPath()  %>/styles/main.css" rel="stylesheet" type="text/css" />
    <decorator:head />
  </head>
  <body>
  main
  <decorator:body />
  </body>
</html>
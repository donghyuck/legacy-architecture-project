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
    <decorator:head />
  </head>
  <body>
   <decorator:body />
  </body>
</html>
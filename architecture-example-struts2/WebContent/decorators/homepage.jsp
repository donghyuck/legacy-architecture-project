<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title>
	<decorator:title default="..." />
</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="-1">
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/bootstrap/3.0.0/bootstrap.css" />
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/kendo/kendo.common.min.css" />
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/kendo/kendo.metro.min.css" />
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/common/common.ui.css" />
<%
String userAgent = request.getHeader("user-agent");
if( userAgent.contains("MSIE 8.0") || userAgent.contains("MSIE 7.0") ){
%>
<script src="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/js/bootstrap/3.0.0/respond.min.js"></script>
<%
}
%>
<script src="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/js/yepnope/1.5.4/yepnope.min.js"></script>
<decorator:head />
<style>
	
	.header {
		padding-top : 10px;
		position: relative;
		font-family: "나눔 고딕", "BM_NANUMGOTHIC";
		 color: #5a5a5a;
	}
	
	.navbar {
		margin-bottom : 0px;
	}
	 
	.k-menu.k-header, .k-menu .k-item {
		/*border-color :#dadada;*/
	}
		
	#account-panel {
		margin-top : 0 px;
		padding-top : 10px;
	}	
	
	#account-panel  .dropdown-menu{
	 	width : 300px;;
	 	z-index: 1001;
	 	color: #000;
	}
</style>
</head>
<body onload="<decorator:getProperty property="body.onload" />" class="<decorator:getProperty property="body.class" />">
	<decorator:body />
</body>
</html>
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
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/kendo/kendo.common.min.css" />
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/kendo/kendo.bootstrap.min.css" />
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/bootstrap/3.1.0/bootstrap.css" />
<script src="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/js/yepnope/1.5.4/yepnope.min.js"></script>
<decorator:head />
<style>
	body { 
		padding: 0;
		padding-top: 51px;
		background-color : #FFFFFF;	
		margin: 0;
		
		height: 100%;
		background : url (<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/images/bg.png ) #FFF ;
	}
	
	.k-grid table tr.k-state-selected{
		background: #428bca;
		color: #ffffff; 
	}
	
</style>
</head>
<body onload="<decorator:getProperty property="body.onload" />"  data-color="<decorator:getProperty property="body.data-color"  class="<decorator:getProperty property="body.class" default="color4" />">
	<decorator:body />
</body>
</html>
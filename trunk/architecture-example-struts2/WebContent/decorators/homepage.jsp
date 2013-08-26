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
<script src="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/js/yepnope/1.5.4/yepnope.min.js"></script>
<decorator:head />
<style>

	.header {
		background-color: #2ba6cb;
		height : 70px;
		position: relative;
		color : #fff;		
		font-family: "나눔 고딕", "BM_NANUMGOTHIC";
	}

	.header h2 { color: #fff; font-family: "나눔 고딕", "BM_NANUMGOTHIC"; }
	.header h4 { color: #CCC; font-weight: 300; font-family: "나눔 고딕", "BM_NANUMGOTHIC"; }
	

	.k-menu.k-header, .k-menu .k-item {
		border-color :#dadada;
	}
	
	nav.top-bar {
		padding-right : 15px;
		margin-right : 20px;
		padding-left : 15px;
		margin-left : 20px;
		margin-top: 0px;
		padding-top : 2px;
		height : 36px;
		border-bottom:1px solid #dadada;
	}

	#wrap {
	  min-height: 100%;
	  height: auto !important;
	  height: 100%;
	  /* Negative indent footer by its height */
	  margin: 0 auto -25px;
	  /* Pad bottom by footer height */
	  padding: 0 0 25px;
	}	
	
	#wrap > .container {
	  padding: 25px 15px 0;
	}
		
	#account-panel {
		margin-top : 0 px;
		padding-top : 20px;
	}	
	
	#account-panel  .dropdown-menu{
	 	width : 80%;
	 }
	
</style>
</head>
<body onload="<decorator:getProperty property="body.onload" />" class="<decorator:getProperty property="body.class" />">
	<decorator:body />
</body>
</html>
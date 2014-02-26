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
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Expires" content="-1">
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/kendo/kendo.common.min.css" />
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/kendo/kendo.metro.min.css" />
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/bootstrap/3.1.0/bootstrap.css" />
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/common/common.ui.css" />
<script src="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/js/yepnope/1.5.4/yepnope.min.js"></script>
<decorator:head />
<style>
	#account-navbar  ul.dropdown-menu {
		min-width : 320px;
	}
	
	body { 
		padding-top: 51px; 
	}
	
	.k-grid table tr.k-state-selected{
		background: #428bca;
		color: #ffffff; 
	}		
	
	nav.personalized-navbar {
		min-height: 51px;
		height: 51px;
	}		

	/** Header css */
	header {
		background: #0070b8;
		background-size: cover;
		padding: 22px 0;
		margin-top : -20px;   /*navbar-fixed-top*/
		margin-bottom: 10px;
	}

	header.cloud {
	    color: white;
		background: #3ba5db url(/images/common/header/cloud-hero.png) bottom right no-repeat;
	}
.
	header h1 {
		color: white;
		font-weight: 300;
		margin-bottom: 0;
	}

	header h4 {
		color: white;
		font-weight: 300;
	}
		
</style>
</head>
<body onload="<decorator:getProperty property="body.onload" />" class="<decorator:getProperty property="body.class" />">
	<decorator:body />
</body>
</html>
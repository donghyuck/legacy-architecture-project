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
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/normalize/3.0.0/normalize.min.css" />
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/kendo/kendo.common.min.css" />
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/kendo/kendo.metro.min.css" />
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/bootstrap/3.1.0/bootstrap.min.css" />
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/common/common.style.css" />
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/common.themes/blue.css" />

<script src="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/js/yepnope/1.5.4/yepnope.min.js"></script>
<decorator:head />

<style>
/*
	#account-navbar  ul.dropdown-menu {
		min-width : 320px;
	}
*/	
	body { 
		/*padding-top: 51px; */
		/*color: #666;*/		
	}
	
	.k-grid table tr.k-state-selected{
		background: #5bc0de;
		color: #ffffff; 
	}		

	.copyright {
		font-size: 12px;
		padding: 11px 0 7px;
		/*background: #3e4753;*/
		/*border-top: solid 1px #777;*/
	}	
	.footer {
		color: #777;
		background: #eee;
		padding: 20px 0 30px;
	}	

	/** Header css */
	header {
		background: #0070b8;
		background-size: cover;
		padding: 12px 0;
		margin-bottom: 10px;
	}
	
	header.cloud {
		color: white;
		background: #3ba5db url(/images/common/header/cloud-hero.png) bottom right no-repeat;
	}
.
	header.cloud h1 ,  header.cloud  h2, header.cloud  h4 {
		color: white;
		font-weight: 300;
		margin-bottom: 0;
	}
	
	header.cloud h4 small {
		color: white;
	}	

	.k-editor-toolbar .k-tool {
		border : 0px;
		color: #34AADC;
	}

	.k-editor-toolbar .k-tool:hover {
		background-color: #428bca;
	}

	.k-editor-toolbar .k-state-hover {
		background-image: none, linear-gradient( to bottom, #428bca 0, #428bca 100%) ;
	}	
	
	.k-editor-toolbar .k-state-selected {
		background-color: #428bca;
	}
	/*		
	.navbar-nav>li>a.btn-link {
		padding-top: 14px;
		padding-bottom: 14px;
	}
	*/
				
</style>
</head>
<body onload="<decorator:getProperty property="body.onload" />" class="<decorator:getProperty property="body.class" default="color4" />">
	
	<decorator:body />
	
</body>
</html>
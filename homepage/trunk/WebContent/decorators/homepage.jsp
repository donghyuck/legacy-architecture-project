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

<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/fonts/nanumgothic.css" /> 
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/kendo/kendo.common.min.css" />
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/kendo/kendo.metro.min.css" />
<!-- 
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/font-awesome/4.1.0/font-awesome.min.css" />
-->
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/bootstrap/3.1.0/bootstrap.min.css" />
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/common/common.ui.css" />
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/common.themes/unify/style.css" />
<link rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/common.themes/unify/themes/default.css" id="style_color">
<%
	architecture.ee.web.util.UAgentInfo userAgentInfo = architecture.ee.web.util.ServletUtils.getUserAgentInfo(request);
	if( userAgentInfo.detectMSIE8() || userAgentInfo.detectMSIE9() ){
%>
<script src="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/js/common.plugins/respond.min.js"></script>
<%		
	}
%>
<script src="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/js/yepnope/1.5.4/yepnope.min.js"></script>
<decorator:head />
<style>

	/** Header css */
	.header {
	}	
	
	/** cloud header */	
	header.inkium {
		background: #fff url('http://img.inkium.com/homepage/sub/sub_consulting.jpg') bottom right repeat-x;
		height : 110px;
		margin-bottom: 20px;
	}
	
	header.inkium .header-sub-logo {
		background: transparent url('http://img.inkium.com/homepage/sub/sub_consulting_01.png') top  20px left;
		background-repeat: no-repeat;
	}	
		
	header.inkium .header-sub-logo  img {
		position: relative;
		left: 180px;
	}
		
	header.inkium .header-sub-title {
		background: transparent url('http://img.inkium.com/homepage/sub/sub_O.png') top 8px right;
		background-repeat: no-repeat;
		height: 130px;
	}	
	
	header.inkium .header-sub-title h2{
		font-size: 30px;
		text-align: right;
		font-weight: bold;
		margin: 0px;
		line-height: 1;
		padding: 32px 100px 0 12px;
	}
	
	header.inkium .header-sub-title h2 small{
		font-size: 50%;		
	}
		
	header.inkium .breadcrumb {
		float: right;
		top: 130px;
		right: 0;
		position: absolute;
	}
	
	
	.page-content {
		margin-top: 66px;
		font-size : 15px;
		color : #333333;
	}
	
	.page-content .page-content-title h2 {
		font-size : 30px;
		font-weight: bold;
		color : #333333;
	}
	
	.page-content .page-content-title h3 {
		padding-top: 20px;
		font-size : 22px;
		font-weight: bold;
		color : #333333;
	}
	
	.page-content .page-content-title:before {
		background-image: url( 'http://img.inkium.com/homepage/sub/line_black.gif'' )  left;
		background-repeat: no-repeat;
	}
		
	header.cloud {
		background: #fff url('/download/image/e6dL6VHTyT03R9e3m9QwK9o2vHTrFbI7bei7RT6mB9PYFQzK2fkhUE6czUSe8TLd') bottom right repeat-x;
		height : 110px;
		margin-bottom: 20px;
	}			
	header.cloud > div.container {
		color: white;
		background: url('/download/image/CzIW9fRq6y5hEH81CSw5bwjM2aWd0vCif7r0gVdpekikRIZCU8ktt9lKxnyld2jx') bottom right no-repeat;
		height : 110px;
	}	
	.cloud h2{	
		text-align: right;
		font-weight: bold;
		padding: 12px 12px 0 0;
	}		
	.cloud h4, 	.cloud h5 {
		text-align: right;
		color: #999;
	}	
	
	/** menu */
	.header .navbar-default .navbar-nav > li > a {
		font-size : 18px;
	}
	
	.header .dropdown-menu li a {
		font-size : 15px;
	}

	.navbar-collapse {
		max-height: 100%;
	}

	header.cloud  .breadcrumb {
		padding: 8px 15px;
		background: rgba( 130,157,182, .2);
		border-radius: 4px !important;
	}
				
</style>
</head>
<body onload="<decorator:getProperty property="body.onload" />" class="<decorator:getProperty property="body.class" default="" />">
	<!-- <div class="page-loader" ></div>-->
	<decorator:body />
</body>
</html>
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
<!-- 
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/normalize/3.0.0/normalize.min.css" />
 -->
 
<!--  kendo  -->
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/kendo/kendo.common.min.css" />
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/kendo/kendo.metro.min.css" />

<!--  bootstrap & awesome fonts -->
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/bootstrap/3.1.0/bootstrap.min.css" />
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/font-awesome/4.0.3/font-awesome.min.css" />

<!--  customize  -->
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/common/common.style.css" />

<script src="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/js/yepnope/1.5.4/yepnope.min.js"></script>
<decorator:head />

<style>
	
	.k-grid table tr.k-state-selected{
		background: #5bc0de;
		color: #ffffff; 
	}		

	.copyright {
		background: #ffffff;
		color: #707E8C;
		border-top: solid 0px #777;
	}	

	.copyright p {
		color: #777;
	}
	
	.footer {
		color: #777;
		background: #dadada;
		padding: 20px 0 30px;
	}	
	
	.footer h4{
		color: #777;
	}
	
	.footer p{
		color: #777;
	}
		
	
	/** Header css */
	
	header.cloud {
		background: #fff url('/download/image/e6dL6VHTyT03R9e3m9QwK9o2vHTrFbI7bei7RT6mB9PYFQzK2fkhUE6czUSe8TLd') bottom right repeat-x;
		/*
		background-size: cover;
		background: 
		linear-gradient(63deg, #f5f5f5 23%, transparent 23%) 7px 0, 
		linear-gradient(63deg, transparent 74%, #f5f5f5 78%), 
		linear-gradient(63deg, transparent 34%, #f5f5f5 38%, #f5f5f5 58%, transparent 62%), 
		#ecf0f1;
		background-size: 16px 48px;
		*/
		/*padding: 12px 0;*/
		height : 110px;
		margin-bottom: 20px;
	}
			
	header.cloud > div.container {
		color: white;
		background: url('/download/image/CzIW9fRq6y5hEH81CSw5bwjM2aWd0vCif7r0gVdpekikRIZCU8ktt9lKxnyld2jx') bottom right no-repeat;
		height : 110px;
	}
	
	.cloud h2{	
		padding: 12px 0;
		color: white;
	}		
	
	/*	
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



	.cloud h2{	
		color: white;
	}

	.cloud h4 {
		color: white;
	}
		
	.cloud h4 small {
		color: white;
	}	
	*/
	
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
				
</style>
</head>
<body onload="<decorator:getProperty property="body.onload" />" class="<decorator:getProperty property="body.class" default="color4" />">
	
	<decorator:body />
	
</body>
</html>
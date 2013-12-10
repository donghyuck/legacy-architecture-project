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
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/kendo/kendo.metro.min.css" />
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/bootstrap/3.0.0/bootstrap.css" />
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
	
	body { padding-top: 70px; }
	
		#account-panel {
			margin-top : 0 px;
			padding-top : 10px;
		}	
		
		
		#account-panel  .dropdown-menu{
		 	width : 300px;;
		 	z-index: 1001;
		 	color: #000;
		}
	
		/*Block Headline*/
		.headline {
			display: block;
			margin: 10px 0 25px 0;
			border-bottom:  2px solid #333333;
		}
		.headline h2, 
		.headline h3, 
		.headline h4 {
			color: #0e0e0e;
			margin: 0 0 -2px 0;
			padding-bottom: 5px;
			display: inline-block;
			border-bottom: 2px solid #00aba9;
		}
		.headline h2 {
			font-size: 18px;
		}
		
		.headline-md h2 {
			font-size: 18px;
		}
		
		.headline-md {
			margin-bottom: 15px;
		}
		
		/*Footer*/
		.footer {
			color: #0e0e0e;
			margin-top: 40px;
			padding: 20px 0 30px;
			background: #f5f5f5;
		}
		
		.footer.margin-top-0 {
			margin-top: 0;
		}
		
		.footer h1, 
		.footer h2, 
		.footer h3, 
		.footer h4, 
		.footer h5 {
			text-shadow: none;
			font-weight: normal !important; 
		} 
		
		.footer p,
		.footer a {
			font-size: 14px;
		} 
		.footer p {
			color: #333333;
		} 
		
		.footer address {
			color: #333333;
		}
		
		.footer  a {
			#color: #333333;
		}
		.footer a:hover {
			#color: #a8f85f;
		    -webkit-transition: all 0.4s ease-in-out;
		    -moz-transition: all 0.4s ease-in-out;
		    -o-transition: all 0.4s ease-in-out;
		    transition: all 0.4s ease-in-out;
		}	
		
		/*Copyright*/
		.copyright {
			font-size: 12px;
			padding: 11px 0 7px;
			background: #ffffff;
			border-top: solid 1px #e7e7e7;	
		}
		
		.copyright p {
			color: #333333;
		}
		
		.copyright p.copyright-space {
			margin-top: 12px;
		}
		
		.copyright a {
			margin: 0 5px;
			color: #333333;
		}
		.copyright a:hover {
			color: #428bca;
		}	
			
</style>
</head>
<body onload="<decorator:getProperty property="body.onload" />" class="<decorator:getProperty property="body.class" />">
	<decorator:body />
</body>
</html>
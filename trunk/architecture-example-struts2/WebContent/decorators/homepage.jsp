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
	
	#mainContent {
		padding-top : 66px;
	} 
	
	#account-panel  .dropdown-menu{
	 	width : 300px;;
	 	z-index: 1001;
	 	color: #000;
	}
	
	#my-messages .popover {
		font-family: "나눔 고딕", "BM_NANUMGOTHIC";
		width: 260px;
		margin-top: 20px;
		margin-right: 20px;
		margin-bottom: 20px;
		margin-left: 20px;
		float: left;
		display: block;
		position: relative;
		z-index: 1;
	 }
	 
	 .popover-title {
		font-family: "나눔 고딕", "BM_NANUMGOTHIC";
	 }
	/* Carousel base class */
	.dropdown-submenu{position:relative;}
	.dropdown-submenu>.dropdown-menu{top:0;left:100%;margin-top:-6px;margin-left:-1px;-webkit-border-radius:0 6px 6px 6px;-moz-border-radius:0 6px 6px 6px;border-radius:0 6px 6px 6px;}
	.dropdown-submenu:hover>.dropdown-menu{display:block;}
	.dropdown-submenu>a:after{display:block;content:" ";float:right;width:0;height:0;border-color:transparent;border-style:solid;border-width:5px 0 5px 5px;border-left-color:#cccccc;margin-top:5px;margin-right:-10px;}
	.dropdown-submenu:hover>a:after{border-left-color:#ffffff;}
	.dropdown-submenu.pull-left{float:none;}.dropdown-submenu.pull-left>.dropdown-menu{left:-100%;margin-left:10px;-webkit-border-radius:6px 0 6px 6px;-moz-border-radius:6px 0 6px 6px;border-radius:6px 0 6px 6px;}

	
	/* Carousel base class */
	h1 {
		font-family: "나눔 고딕", "BM_NANUMGOTHIC";
	}
	
	.carousel {
		margin-bottom: 10px;	
		height:400px;		
	}
	
	.carousel-caption {
		z-index: 10;
		text-shadow : none;
	}

	.carousel-inner { 
		text-align: center; 
		overflow: hidden;	
	}
	
	.carousel .item > img { 
		display: inline-block; 
		margin: 0 auto;

	}
	
	.carousel-control {
		color : #000;
	}

	.carousel-control:hover , .carousel-control:focus {
		color : #000;
	}
	.left.carousel-control , .right.carousel-control {
		background-image : none;
	}	

</style>
</head>
<body onload="<decorator:getProperty property="body.onload" />" class="<decorator:getProperty property="body.class" />">
	<decorator:body />
</body>
</html>
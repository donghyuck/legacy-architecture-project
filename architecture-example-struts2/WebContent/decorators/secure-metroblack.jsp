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
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/foundation/foundation.css" />
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/foundation/general_foundicons.css" />
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/kendo/kendo.common.min.css" />
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/kendo/kendo.black.min.css" />
<link  rel="stylesheet" type="text/css"  href="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/styles/common/common.ui.css" />
<script src="<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/js/yepnope/1.5.4/yepnope.min.js"></script>
<decorator:head />
<style>

	@media only screen and (min-width: 768px) {
		body{
			font-size: 10pt;
		}
	}
	
	@media only screen and (min-width: 1280px) {
		body{
			font-size: 11pt;
		}
	}
	
	@media only screen and (min-width: 1440px) {
		body{
			font-size: 14pt;
		}
	}
	
	.ui-panel {
		width: 65%;
		height:100%;
		top: 16px;
		background-color: #ffffff;
		border-color: #dadada;
		display: inline-block;
		position: absolute;
		z-index: 10001;
		border-style: solid;
		border-width: 1px;		
	}
	
	.panel-position-right {
	/* right: -17em; */
	}

	/* animated: panel right (for overlay and push) */
	.ui-panel-animate.ui-panel-position-right.ui-panel-display-overlay,
	.ui-panel-animate.ui-panel-position-right.ui-panel-display-push {
		right: 0;
		-webkit-transform: translate3d(17em,0,0);
		-moz-transform: translate3d(17em,0,0);
		transform: translate3d(17em,0,0);
	}
	/* panel right open */
	.ui-panel-position-right.ui-panel-display-reveal,  /* negate "panel right" for reveal */
	.ui-panel-position-right.ui-panel-open {
		right: 0;
	}	
	header { background-color: #2ba6cb; }
	
	header h1 { color: #fff; font-weight: 500; }
	
	header h4 { color: #fff; font-weight: 300; }
	

</style>
</head>
<body onload="<decorator:getProperty property="body.onload" />" class="<decorator:getProperty property="body.class" />">
	<decorator:body />
</body>
</html>
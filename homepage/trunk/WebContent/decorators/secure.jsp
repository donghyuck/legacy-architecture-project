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
/*
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
	}*/
	
		.header-1,
		.header-1:hover,
		.header-2,
		.header-2:hover {
			text-align: none;
			cursor: pointer;
			text-decoration: none !important;
			display: block;
			z-index: 100000000;
			border-top: 1px solid #eee;
			margin: 0 -9px 0 -9px;
		}

		.header-1,
		.header-1:hover {
			color: #333 !important;
			font-size: 18px;
			font-weight: 300;
			padding: 30px 18px 15px 18px;
			background: #fafafa;
			background: rgba(0,0,0,.02);
			margin-bottom: -15px;
		}

		.header-2,
		.header-2:hover {
			text-transform: uppercase;
			font-size: 11px;
			font-weight: 600;
			color: #888 !important;
			padding: 10px 18px 0 18px;
			margin-bottom: 15px;
			margin-top: 0;
		}

		.header-1 + .header-2 {
			margin-top: 10px;
			margin-bottom: 0;
		}

		.header-1 + .col-sm-12 {
			margin-top: 15px;
			padding-top: 15px;
			border-top: 1px solid #eee;
			margin-left: -9px;
			margin-right: -9px;
			padding-left: 18px;
			padding-right: 18px;
			width: auto !important;
			float: none;
		}

.page-details .details-full-name {
	font-size: 20px;
	font-weight: 300;
	line-height: 40px;
	padding-top: 15px;
	padding-bottom: 20px;
	position: relative;
	border-bottom-width: 2px;
	margin-bottom: 0;
	text-align: center
}

.page-details .details-content .tab-content {
	background: #fff
}

.page-details .details-block {
	margin-bottom: 18px;
	text-align: center
}

.page-details .details-photo {
	padding: 6px;
	display: inline-block;
	margin-bottom: 10px;
	border-radius: 999999px
}

.page-details .details-photo img {
	display: block;
	max-width: 100%;
	border-radius: 999999px
}

.page-details .left-col>.panel .panel-heading {
	padding-left: 10px;
	padding-right: 10px
}

.page-details .left-col>.panel .panel-body {
	padding: 10px;
	padding-bottom: 0
}

.page-details .left-col .list-group-item {
	background: none !important;
	border: none;
	padding-bottom: 0;
	margin-top: 7px;
	padding-left: 10px;
	padding-right: 10px
}

.page-details .details-skills .label {
	display: block;
	float: left;
	margin-right: 4px;
	margin-bottom: 4px
}

.page-details .details-list-icon {
	width: 24px;
	text-align: center;
	font-size: 14px
}

.page-details .tl-entry:before,.page-details .tl-header,.page-details .tl-icon
	{
	box-shadow: 0 0 0 4px #fff !important
}

.page-details .widget-followers {
	margin: -20px
}

.page-details .widget-followers .follower {
	padding: 13px 20px
}

.page-details .details-content-hr {
	margin-top: 30px;
	margin-bottom: 30px
}

@media ( min-width :768px) {
	.page-details .left-col {
		float: left;
		width: 220px
	}
	.page-details .right-col {
		overflow: hidden;
		padding-left: 20px
	}
	.page-details .details-content {
		margin-top: 0
	}
	.page-details .details-full-name {
		padding-left: 240px;
		padding-bottom: 50px;
		text-align: left
	}
	.page-details .details-row {
		margin-top: -36px
	}
	.page-details .details-block {
		margin-top: -70px
	}
	.details-content-hr {
		display: none
	}
}

			
	
</style>
</head>
<body onload="<decorator:getProperty property="body.onload" />"  data-color="<decorator:getProperty property="body.data-color" />" class="<decorator:getProperty property="body.class" default="" />">
	<div class="page-loader" ></div>
	<decorator:body />
</body>
</html>
<#ftl encoding="UTF-8"/>
<html decorator="secure">
<head>
		<title>관리자 메인</title>		
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/common/common.admin.style.css" />
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.0.3/font-awesome.min.css',
			'css!${request.contextPath}/styles/codedrop/codedrop.overlay.css',
			'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo.extension/kendo.ko_KR.js',
			'${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			'${request.contextPath}/js/bootstrap/3.0.3/bootstrap.min.js',
			'${request.contextPath}/js/jqueryui/jquery-ui-1.9.2.custom.min.js',	
			'${request.contextPath}/js/jquery.plugins/nicescroll/jquery.nicescroll.min.js',
			'${request.contextPath}/js/common/common.admin.js',/**
			'${request.contextPath}/js/common/common.admin.dashboard.js',*/
			'${request.contextPath}/js/common/common.models.js',       	    
			'${request.contextPath}/js/common/common.api.js',
			'${request.contextPath}/js/common/common.ui.js'
			],        	  	   
			complete: function() {      
				
				// END SCRIPT
			}
		}]);
		-->
		</script> 		 
		<style>

		</style>
	</head>
	<body data-color="grey" class="flat">
		<div id="main-wrapper">
			<div id="header">
				<h1><a href="./index.html">Unicorn Admin</a></h1>		
				<a id="menu-trigger" href="#"><i class="fa fa-bars"></i></a>	
			</div>		
			<div id="user-nav">

			</div>
			<div id="sidebar">
				<ul>
					<li><a href="index.html"><i class="fa fa-home"></i> <span>Dashboard</span></a></li>
					<li class="submenu">
						<a href="#"><i class="fa fa-flask"></i> <span>UI Lab</span> <i class="arrow fa fa-chevron-right"></i></a>
						<ul>
							<li><a href="interface.html">Interface Elements</a></li>
							<li><a href="jquery-ui.html">jQuery UI</a></li>
							<li><a href="buttons.html">Buttons &amp; icons</a></li>
						</ul>
					</li>
					<li class="submenu">
						<a href="#"><i class="fa fa-th-list"></i> <span>Form elements</span> <i class="arrow fa fa-chevron-right"></i></a>
						<ul>
							<li><a href="form-common.html">Common elements</a></li>
							<li><a href="form-validation.html">Validation</a></li>
							<li><a href="form-wizard.html">Wizard</a></li>
						</ul>
					</li>
					<li class="active"><a href="tables.html"><i class="fa fa-th"></i> <span>Tables</span></a></li>
					<li><a href="grid.html"><i class="fa fa-th-list"></i> <span>Grid Layout</span></a></li>
					<li class="submenu">
						<a href="#"><i class="fa fa-file"></i> <span>Sample pages</span> <i class="arrow fa fa-chevron-right"></i></a>
						<ul>
							<li><a href="invoice.html">Invoice</a></li>
							<li><a href="chat.html">Support chat</a></li>
							<li><a href="calendar.html">Calendar</a></li>
							<li><a href="gallery.html">Gallery</a></li>
							<li><a href="messages.html">Messages</a></li>
						</ul>
					</li>
					<li>
						<a href="charts.html"><i class="fa fa-signal"></i> <span>Charts &amp; graphs</span></a>
					</li>
					<li>
						<a href="widgets.html"><i class="fa fa-inbox"></i> <span>Widgets</span></a>
					</li>
				</ul>			
			</div>			
			<div id="content">
				<div id="content-header">
					<h1>Tables</h1>
					<div class="btn-group btn-group-xs">
						<a class="btn btn-large" title="Manage Files"><i class="fa fa-file fa-xs"></i></a>
						<a class="btn btn-large" title="Manage Users"><i class="fa fa-user"></i></a>
						<a class="btn btn-large" title="Manage Comments"><i class="fa fa-comment"></i><span class="label label-danger">5</span></a>
						<a class="btn btn-large" title="Manage Orders"><i class="fa fa-shopping-cart"></i></a>
					</div>
				</div>
				<div id="breadcrumb">
					<a href="#" title="Go to Home" class="tip-bottom"><i class="fa fa-home"></i> Home</a>
					<a href="#" class="current">Tables</a>					
				</div>
				<div class="row">
					<div class="col-xs-12">
						<div class="widget-box"></div>
					</div>
				</div>
			</div>
				
			<div class="row">
				<div id="footer" class="col-xs-12">
					2012 - 2013 &copy; Unicorn Admin. Brought to you by <a href="https://wrapbootstrap.com/user/diablo9983">diablo9983</a>
				</div>
			</div>
			
		</div>
		<#include "/html/common/common-system-templates.ftl" >			
	</body>    
</html>
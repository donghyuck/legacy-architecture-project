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
			'css!${request.contextPath}/styles/common.extension/animate.css',
			'css!${request.contextPath}/styles/common/common.admin.widgets.css',			
			'css!${request.contextPath}/styles/common/common.admin.rtl.css',			
			'css!${request.contextPath}/styles/common/common.admin.themes.css',			
			'${request.contextPath}/js/jquery/2.1.1/jquery-2.1.1.min.js',
			/*'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',*/
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo.extension/kendo.ko_KR.js',
			'${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			'${request.contextPath}/js/bootstrap/3.0.3/bootstrap.min.js',
			/*
			'${request.contextPath}/js/common/common.admin.init.js',*/
			'${request.contextPath}/js/common/common.admin.js',			
			'${request.contextPath}/js/common/common.models.js',       	    
			'${request.contextPath}/js/common/common.api.js',
			'${request.contextPath}/js/common/common.ui.js'
			],        	  	   
			complete: function() {      

/*
	init.push(function () {
		var setEqHeight = function () {
			$('#content-wrapper .row').each(function () {
				var $p = $(this).find('.stat-panel');
				if (! $p.length) return;
				$p.attr('style', '');
				var h = $p.first().height(), max_h = h;
				$p.each(function () {
					h = $(this).height();
					if (max_h < h) max_h = h;
				});
				$p.css('height', max_h);
			});
		};
		$('#equal-height').click(function () {
			if ($(this).hasClass('disabled')) return;
			$(this).addClass('disabled');
			setEqHeight();
			$(window).on('pa.resize', setEqHeight);
			$(window).resize();
		});
	});
	*/
	var init = [];
	
	window.PixelAdmin.start(init);


				
				// END SCRIPT
			}
		}]);
		-->
		</script> 		 
		<style>

		</style>
	</head>
	<body class="theme-default main-menu-animated">
<div id="main-wrapper">

	<div id="main-navbar" class="navbar navbar-inverse" role="navigation">
		<!-- Main menu toggle -->
		<button type="button" id="main-menu-toggle"><i class="navbar-icon fa fa-bars icon"></i><span class="hide-menu-text">HIDE MENU</span></button>
		
		<div class="navbar-inner">
			<!-- Main navbar header -->
			<div class="navbar-header">

				<!-- Logo -->
				<a href="index.html" class="navbar-brand">
					<div><img alt="Pixel Admin" src="assets/images/pixel-admin/main-navbar-logo.png"></div>
					PixelAdmin
				</a>

				<!-- Main navbar toggle -->
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#main-navbar-collapse"><i class="navbar-icon fa fa-bars"></i></button>

			</div> <!-- / .navbar-header -->

			<div id="main-navbar-collapse" class="collapse navbar-collapse main-navbar-collapse">
				<div>
					<ul class="nav navbar-nav">
						<li>
							<a href="#">Home</a>
						</li>
						<li class="dropdown">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown</a>
							<ul class="dropdown-menu">
								<li><a href="#">First item</a></li>
								<li><a href="#">Second item</a></li>
								<li class="divider"></li>
								<li><a href="#">Third item</a></li>
							</ul>
						</li>
					</ul> <!-- / .navbar-nav -->

					<div class="right clearfix">
						<ul class="nav navbar-nav pull-right right-navbar-nav">

							<li class="nav-icon-btn nav-icon-btn-danger dropdown">
								<a href="#notifications" class="dropdown-toggle" data-toggle="dropdown">
									<span class="label">5</span>
									<i class="nav-icon fa fa-bullhorn"></i>
									<span class="small-screen-text">Notifications</span>
								</a>

								<!-- NOTIFICATIONS -->

								<div class="dropdown-menu widget-notifications no-padding" style="width: 300px">
									<div class="notifications-list" id="main-navbar-notifications">

										<div class="notification">
											<div class="notification-title text-danger">SYSTEM</div>
											<div class="notification-description"><strong>Error 500</strong>: Syntax error in index.php at line <strong>461</strong>.</div>
											<div class="notification-ago">12h ago</div>
											<div class="notification-icon fa fa-hdd-o bg-danger"></div>
										</div> <!-- / .notification -->


									</div> <!-- / .notifications-list -->
									<a href="#" class="notifications-link">MORE NOTIFICATIONS</a>
								</div> <!-- / .dropdown-menu -->
							</li>
							<li class="nav-icon-btn nav-icon-btn-success dropdown">
								<a href="#messages" class="dropdown-toggle" data-toggle="dropdown">
									<span class="label">10</span>
									<i class="nav-icon fa fa-envelope"></i>
									<span class="small-screen-text">Income messages</span>
								</a>

								<!-- MESSAGES -->
								
								<!-- Javascript -->
								<script>
									//init.push(function () {
									//	$('#main-navbar-messages').slimScroll({ height: 250 });
									//});
								</script>
								<!-- / Javascript -->

								<div class="dropdown-menu widget-messages-alt no-padding" style="width: 300px;">
									<div class="messages-list" id="main-navbar-messages">
										<div class="message">
											/*<img src="assets/demo/avatars/2.jpg" alt="" class="message-avatar">*/
											<a href="#" class="message-subject">Lorem ipsum dolor sit amet.</a>
											<div class="message-description">
												from <a href="#">Robert Jang</a>
												&nbsp;&nbsp;·&nbsp;&nbsp;
												2h ago
											</div>
										</div> <!-- / .message -->

									</div> <!-- / .messages-list -->
									<a href="#" class="messages-link">MORE MESSAGES</a>
								</div> <!-- / .dropdown-menu -->
							</li>
<!-- /3. $END_NAVBAR_ICON_BUTTONS -->

							<li>
								<form class="navbar-form pull-left">
									<input type="text" class="form-control" placeholder="Search">
								</form>
							</li>

							<li class="dropdown">
								<a href="#" class="dropdown-toggle user-menu" data-toggle="dropdown">
									<img src="assets/demo/avatars/1.jpg">
									<span>John Doe</span>
								</a>
								<ul class="dropdown-menu">
									<li><a href="#"><span class="label label-warning pull-right">New</span>Profile</a></li>
									<li><a href="#"><span class="badge badge-primary pull-right">New</span>Account</a></li>
									<li><a href="#"><i class="dropdown-icon fa fa-cog"></i>&nbsp;&nbsp;Settings</a></li>
									<li class="divider"></li>
									<li><a href="pages-signin.html"><i class="dropdown-icon fa fa-power-off"></i>&nbsp;&nbsp;Log Out</a></li>
								</ul>
							</li>
						</ul> <!-- / .navbar-nav -->
					</div> <!-- / .right -->
				</div>
			</div> <!-- / #main-navbar-collapse -->
		</div> <!-- / .navbar-inner -->
	</div> <!-- / #main-navbar -->
<!-- /2. $END_MAIN_NAVIGATION -->


	<div id="main-menu" role="navigation">
		<div id="main-menu-inner">
			<div class="menu-content top" id="menu-content-demo">
				<!-- Menu custom content demo
					 CSS:        styles/pixel-admin-less/demo.less or styles/pixel-admin-scss/_demo.scss
					 Javascript: html/assets/demo/demo.js
				 -->
				<div>
					<div class="text-bg"><span class="text-slim">Welcome,</span> <span class="text-semibold">John</span></div>

					<img src="assets/demo/avatars/1.jpg" alt="" class="">
					<div class="btn-group">
						<a href="#" class="btn btn-xs btn-primary btn-outline dark"><i class="fa fa-envelope"></i></a>
						<a href="#" class="btn btn-xs btn-primary btn-outline dark"><i class="fa fa-user"></i></a>
						<a href="#" class="btn btn-xs btn-primary btn-outline dark"><i class="fa fa-cog"></i></a>
						<a href="#" class="btn btn-xs btn-danger btn-outline dark"><i class="fa fa-power-off"></i></a>
					</div>
					<a href="#" class="close">&times;</a>
				</div>
			</div>
			<ul class="navigation">
				<li>
					<a href="index.html"><i class="menu-icon fa fa-dashboard"></i><span class="mm-text">Dashboard</span></a>
				</li>
				<li class="mm-dropdown">
					<a href="#"><i class="menu-icon fa fa-th"></i><span class="mm-text">Layouts</span><span class="label label-warning">Updated</span></a>
					<ul>
						<li>
							<a tabindex="-1" href="layouts-grid.html"><span class="mm-text">Grid</span></a>
						</li>
						<li>
							<a tabindex="-1" href="layouts-main-menu.html"><i class="menu-icon fa fa-th-list"></i><span class="mm-text">Main menu</span><span class="label label-warning">Updated</span></a>
						</li>
					</ul>
				</li>
				<li>
					<a href="stat-panels.html"><i class="menu-icon fa fa-tasks"></i><span class="mm-text">Stat panels</span></a>
				</li>
				<li>
					<a href="widgets.html"><i class="menu-icon fa fa-flask"></i><span class="mm-text">Widgets</span></a>
				</li>
				<li class="mm-dropdown">
					<a href="#"><i class="menu-icon fa fa-desktop"></i><span class="mm-text">UI elements</span></a>
					<ul>
						<li>
							<a tabindex="-1" href="ui-buttons.html"><span class="mm-text">Buttons</span></a>
						</li>
						<li>
							<a tabindex="-1" href="ui-typography.html"><span class="mm-text">Typography</span></a>
						</li>
						<li>
							<a tabindex="-1" href="ui-tabs.html"><span class="mm-text">Tabs &amp; Accordions</span></a>
						</li>
						<li>
							<a tabindex="-1" href="ui-modals.html"><span class="mm-text">Modals</span></a>
						</li>
						<li>
							<a tabindex="-1" href="ui-alerts.html"><span class="mm-text">Alerts &amp; Tooltips</span></a>
						</li>
						<li>
							<a tabindex="-1" href="ui-components.html"><span class="mm-text">Components</span></a>
						</li>
						<li>
							<a tabindex="-1" href="ui-panels.html"><span class="mm-text">Panels</span></a>
						</li>
						<li>
							<a tabindex="-1" href="ui-jqueryui.html"><span class="mm-text">jQuery UI</span></a>
						</li>
						<li>
							<a tabindex="-1" href="ui-icons.html"><span class="mm-text">Icons</span></a>
						</li>
						<li>
							<a tabindex="-1" href="ui-utility-classes.html"><span class="mm-text">Utility classes</span></a>
						</li>
					</ul>
				</li>
				<li class="mm-dropdown">
					<a href="#"><i class="menu-icon fa fa-check-square"></i><span class="mm-text">Form components</span></a>
					<ul>
						<li>
							<a tabindex="-1" href="forms-layouts.html"><span class="mm-text">Layouts</span></a>
						</li>
						<li>
							<a tabindex="-1" href="forms-general.html"><span class="mm-text">General</span></a>
						</li>
						<li>
							<a tabindex="-1" href="forms-advanced.html"><span class="mm-text">Advanced</span></a>
						</li>
						<li>
							<a tabindex="-1" href="forms-pickers.html"><span class="mm-text">Pickers</span></a>
						</li>
						<li>
							<a tabindex="-1" href="forms-validation.html"><span class="mm-text">Validation</span></a>
						</li>
						<li>
							<a tabindex="-1" href="forms-editors.html"><span class="mm-text">Editors</span></a>
						</li>
					</ul>
				</li>
				<li>
					<a href="tables.html"><i class="menu-icon fa fa-table"></i><span class="mm-text">Tables</span></a>
				</li>
				<li>
					<a href="charts.html"><i class="menu-icon fa fa-bar-chart-o"></i><span class="mm-text">Charts</span></a>
				</li>
				<li class="mm-dropdown">
					<a href="#"><i class="menu-icon fa fa-files-o"></i><span class="mm-text">Pages</span><span class="label label-success">16</span></a>
					<ul>
						<li>
							<a tabindex="-1" href="pages-search.html"><span class="mm-text">Search results</span></a>
						</li>
						<li>
							<a tabindex="-1" href="pages-pricing.html"><span class="mm-text">Plans &amp; pricing</span></a>
						</li>
						<li>
							<a tabindex="-1" href="pages-faq.html"><span class="mm-text">FAQ</span></a>
						</li>
						<li>
							<a tabindex="-1" href="pages-profile.html"><span class="mm-text">Profile</span></a>
						</li>
						<li>
							<a tabindex="-1" href="pages-timeline.html"><span class="mm-text">Timeline</span></a>
						</li>
						<li>
							<a tabindex="-1" href="pages-signin.html"><span class="mm-text">Sign In</span></a>
						</li>
						<li>
							<a tabindex="-1" href="pages-signup.html"><span class="mm-text">Sign Up</span></a>
						</li>
						<li>
							<a tabindex="-1" href="pages-signin-alt.html"><span class="mm-text">Sign In Alt</span></a>
						</li>
						<li>
							<a tabindex="-1" href="pages-signup-alt.html"><span class="mm-text">Sign Up Alt</span></a>
						</li>
						<li>
							<a tabindex="-1" href="pages-invoice.html"><span class="mm-text">Invoice</span></a>
						</li>
						<li>
							<a tabindex="-1" href="pages-404.html"><span class="mm-text">Error 404</span></a>
						</li>
						<li>
							<a tabindex="-1" href="pages-500.html"><span class="mm-text">Error 500</span></a>
						</li>
						<li class="mm-dropdown">
							<a href="#"><i class="menu-icon fa fa-envelope"></i><span class="mm-text">Messages</span></a>
							<ul>
								<li>
									<a tabindex="-1" href="pages-inbox.html"><span class="mm-text">Inbox</span></a>
								</li>
								<li>
									<a tabindex="-1" href="pages-show-email.html"><span class="mm-text">Show message</span></a>
								</li>
								<li>
									<a tabindex="-1" href="pages-new-email.html"><span class="mm-text">New message</span></a>
								</li>
							</ul>
						</li>
						<li>
							<a tabindex="-1" href="pages-blank.html"><span class="mm-text">Blank page</span></a>
						</li>
					</ul>
				</li>
				<li>
					<a href="complete-ui.html"><i class="menu-icon fa fa-briefcase"></i><span class="mm-text">Complete UI</span></a>
				</li>
				<li>
					<a href="color-builder.html"><i class="menu-icon fa fa-tint"></i><span class="mm-text">Color Builder</span></a>
				</li>
				<li class="mm-dropdown">
					<a href="#"><i class="menu-icon fa fa-sitemap"></i><span class="mm-text">Menu levels</span><span class="badge badge-primary">6</span></a>
					<ul>
						<li>
							<a tabindex="-1" href="#"><span class="mm-text">Menu level 1.1</span><span class="badge badge-danger">12</span><span class="label label-info">21</span></a>
						</li>
						<li>
							<a tabindex="-1" href="#"><span class="mm-text">Menu level 1.2</span></a>
						</li>
						<li class="mm-dropdown">
							<a tabindex="-1" href="#"><span class="mm-text">Menu level 1.3</span><span class="label label-warning">5</span></a>
							<ul>
								<li>
									<a tabindex="-1" href="#"><span class="mm-text">Menu level 2.1</span></a>
								</li>
								<li class="mm-dropdown">
									<a tabindex="-1" href="#"><span class="mm-text">Menu level 2.2</span></a>
									<ul>
										<li class="mm-dropdown">
											<a tabindex="-1" href="#"><span class="mm-text">Menu level 3.1</span></a>
											<ul>
												<li>
													<a tabindex="-1" href="#"><span class="mm-text">Menu level 4.1</span></a>
												</li>
											</ul>
										</li>
										<li>
											<a tabindex="-1" href="#"><span class="mm-text">Menu level 3.2</span></a>
										</li>
									</ul>
								</li>
								<li>
									<a tabindex="-1" href="#"><span class="mm-text">Menu level 2.2</span></a>
								</li>
							</ul>
						</li>
					</ul>
				</li>
			</ul> <!-- / .navigation -->
			<div class="menu-content">
				<a href="pages-invoice.html" class="btn btn-primary btn-block btn-outline dark">Create Invoice</a>
			</div>
		</div> <!-- / #main-menu-inner -->
	</div> <!-- / #main-menu -->
<!-- /4. $MAIN_MENU -->


	<div id="content-wrapper">
		<div class="page-header">
			<h1><i class="fa fa-bar-chart-o page-header-icon"></i> 사용자 관리</h1>
		</div><!-- / .page-header -->

		<div class="note note-info padding-xs-vr">
		</div> <!-- / .note -->


	</div> <!-- / #content-wrapper -->
	<div id="main-menu-bg"></div>
</div> <!-- / #main-wrapper -->
		<#include "/html/common/common-system-templates.ftl" >			
	</body>    
</html>
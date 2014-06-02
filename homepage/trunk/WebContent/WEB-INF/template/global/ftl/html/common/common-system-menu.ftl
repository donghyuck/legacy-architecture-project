		<!-- START MENU -->	
		<#assign webSite = webSite />				
		<#assign webSiteMenu = action.getWebSiteMenu("SYSTEM_MENU") />
			
		<div id="main-menu" role="navigation">
			<div id="main-menu-inner">
				<div class="menu-content top" id="menu-content-demo">
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
			<#list webSiteMenu.components as item >
				<#if  item.components?has_content >
				<li>
					<a href="javascript:void(0);"><i class="menu-icon fa fa-dashboard"></i><span class="mm-text">${item.title}</span></a>
				</li>
				<#else>
				<li>
					<a href="${item.page}">${item.title}</a>
				</li>
				</#if>
			</#list>
			</ul> <!-- / .navigation -->
			<div class="menu-content">
				<a href="pages-invoice.html" class="btn btn-primary btn-block btn-outline dark">Create Invoice</a>
			</div>
		</div> <!-- / #main-menu-inner -->
	</div> <!-- / #main-menu -->

			
			<#if action.user.anonymous >
			</#if>		
			<#if action.webSite ?? >
			<#assign webSite = webSite />				
			<#assign webSiteMenu = action.getWebSiteMenu("SYSTEM_MENU") />
			<div class="header">
				<div class="topbar">
					<div class="container">
						<!-- Topbar Navigation -->
						<ul class="loginbar pull-right">
							<li>
								<i class="fa fa-globe"></i>
								<a>언어</a>
								<ul class="lenguages">
									<li class="active">
										<a href="#">한국어 <i class="fa fa-check"></i></a> 
									</li>
								</ul>
							</li>
							<li class="topbar-devider"></li>   
							<li><a href="##\">도움말</a></li>  
						</ul>
						<!-- End Topbar Navigation -->
					</div>
				</div>
				<nav class="navbar navbar-default" role="navigation">
					<div class="container">
						<!-- Brand and toggle get grouped for better mobile display -->
						<div class="navbar-header">					
							<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-responsive-collapse">
								<span class="sr-only">${webSite.description} toggle navigation</span>
								<span class="fa fa-bars"></span>
							</button>					
							<a class="navbar-brand" href="/main.do">
								<img id="logo-header" src="/download/logo/company/INKIUM" width="65%" alt="Logo">
							</a>
						</div>												
						<!-- Collect the nav links, forms, and other content for toggling -->
						<div class="collapse navbar-collapse navbar-responsive-collapse">
							<ul id="account-navbar" class="nav navbar-nav navbar-right"></ul>
							<!-- /account -->
							<ul class="nav navbar-nav">
								<#list webSiteMenu.components as item >
								<#if  item.components?has_content >
									<li class="dropdown">
										<a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown">${item.title}</a>
										<ul class="dropdown-menu">
										<#list item.components as sub_item >
											<#if sub_item.components?has_content >
												<li class="dropdown-submenu">
													<a href="#" class="dropdown-toggle" data-toggle="dropdown">${sub_item.title}</a>
													<ul class="dropdown-menu">
														<#list sub_item.components as sub_sub_item >
														<li><a href="${sub_item.page}">${ sub_sub_item.title }</a></li>
														</#list>
													</ul>
												</li>
											<#else>								
												<li><a href="${sub_item.page}">${sub_item.title}</a></li>
											</#if>								
										</#list>
										</ul>
									</li>
								<#else>
									<li>
										<a href="${item.page}">${item.title}</a>
									</li>
								</#if>
								</#list>
							</ul>				
						</div>						
					</div>
				</nav>
			</div>
			</#if>					
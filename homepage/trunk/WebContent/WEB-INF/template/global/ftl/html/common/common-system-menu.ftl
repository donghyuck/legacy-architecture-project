		<!-- START MENU -->	
		<#assign webSite = webSite />				
		<#assign webSiteMenu = action.getWebSiteMenu("SYSTEM_MENU") />
			
		<div id="main-menu" role="navigation">
			<div id="main-menu-inner">
				<div class="menu-content top" id="menu-content-demo">
				<div>
				<div class="text-bg"><span class="text-slim">Welcome,</span> <span class="text-semibold">${action.user.name}</span></div>
				<img src="/download/profile/${action.user.username}?width=100&height=150" alt="" class="">
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
				<li class="mm-dropdown">
					<a href="javascript:void(0);"><i class="menu-icon fa fa-dashboard"></i><span class="mm-text">${item.title}</span></a>
					<ul>
						<#list item.components as sub_item >
							<#if sub_item.components?has_content >
							<li class="dropdown-submenu">
								<a href="#" class="dropdown-toggle" data-toggle="dropdown"><span class="mm-text">${sub_item.title}</span></a>
								<ul class="dropdown-menu">
								<#list sub_item.components as sub_sub_item >
									<li><a href="${sub_item.page}"> <span class="mm-text">${ sub_sub_item.title }</span></a></li>
								</#list>
								</ul>
							</li>
							<#else>								
								<li><a tabindex="-1" href="${sub_item.page}"><span class="mm-text">${sub_item.title}</span></a></li>
							</#if>								
						</#list>
					</ul>					
				</li>
				<#else>
				<li>
					<a href="${item.page}"><span class="mm-text">${item.title}</span></a>
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
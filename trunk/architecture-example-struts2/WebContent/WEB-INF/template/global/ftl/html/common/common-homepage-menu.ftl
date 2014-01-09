		<!-- START MENU -->	
		<#if action.getMenuComponent("USER_MENU") ?? >
		<#assign menu = action.getMenuComponent("USER_MENU") />			
				<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
					<div class="container">
					<#if action.user.company ?? >
					<div class="navbar-header">					
						<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
							<span class="sr-only">Toggle navigation</span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
							<span class="icon-bar"></span>
						</button>					
						<a class="navbar-brand" href="/main.do">&nbsp;&nbsp;${action.user.company.displayName }</a>
					</div>			
					</#if>												
					<ul class="nav navbar-nav">
						<#list menu.components as item >
						<#if  item.components?has_content >
							<li class="dropdown">
								<a href="#" class="dropdown-toggle" data-toggle="dropdown">${item.title}<b class="caret"></b></a>
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
								<a href="#">${item.title}</a>
							</li>
						</#if>
						</#list>
					</ul>				
					<ul class="nav navbar-nav navbar-right">
						<li id="account-panel"></li>
						<li>
							<p class="navbar-text"></p>
						</li>
					</ul>
					</div>
				</nav>
			</#if>		
			<!-- END MENU -->		
			<!-- start My profile Modal -->
			<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			  <div class="modal-dialog">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			        <h4 class="modal-title" id="myModalLabel">프로필</h4>
			      </div>
			      <div class="modal-body">
						<p>Loading...</p>
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-default" data-dismiss="modal">최소</button>
			        <button type="button" class="btn btn-primary">확인</button>
			      </div>
			    </div><!-- /.modal-content -->
			  </div><!-- /.modal-dialog -->
			</div><!-- /.modal -->					
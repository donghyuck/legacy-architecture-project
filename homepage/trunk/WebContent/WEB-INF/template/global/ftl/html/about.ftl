<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title>기업소개</title>
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.1.0/font-awesome.min.css',
			'css!${request.contextPath}/styles/common.pages/common.timeline-v2.min',
			'css!${request.contextPath}/styles/common.themes/unify/themes/pomegranate.css',			
			'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo.extension/kendo.ko_KR.js',			
			'${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js',		
			'${request.contextPath}/js/bootstrap/3.1.0/bootstrap.min.js',
			'${request.contextPath}/js/common/common.models.js',			
			'${request.contextPath}/js/common/common.api.js',
			'${request.contextPath}/js/common/common.ui.js'],
			complete: function() {
							      
				// START SCRIPT	
				common.ui.setup({
					features:{
						backstretch : false
					}
				});	
				
				// ACCOUNTS LOAD	
				var currentUser = new User();			
				$("#account-navbar").extAccounts({
					externalLoginHost: "${ServletUtils.getLocalHostAddr()}",	
					<#if action.isAllowedSignIn() ||  !action.user.anonymous  >
					template : kendo.template($("#account-template").html()),
					</#if>
					authenticate : function( e ){
						e.token.copy(currentUser);
					}				
				});	
				
				<#if !action.user.anonymous >							
				</#if>	
				// END SCRIPT            
			}
		}]);	
		-->
		</script>		
		<style scoped="scoped">
		blockquote p {
			font-size: 15px;
		}

		.k-grid table tr.k-state-selected{
			background: #428bca;
			color: #ffffff; 
		}
		
		#announce-view .popover {
			position : relative;
			max-width : 500px;
		}
		.cbp_tmtimeline > li .cbp_tmicon { 
			position : relative;
		}
					
						
		</style>   	
	</head>
	<body>
		<div class="wrapper">
		<!-- START HEADER -->
		<#include "/html/common/common-homepage-menu.ftl" >	
		<#assign hasWebSitePage = action.hasWebSitePage("pages.about.pageId") />
		<#assign menuName = action.targetPage.getProperty("page.menu.name", "USER_MENU") />
		<#assign menuItemName = action.targetPage.getProperty("navigator.selected.name", "MENU_1_1") />
		<#assign current_menu = action.getWebSiteMenu(menuName, menuItemName) />
		
		<header class="cloud">
			<div class="container">
				<div class="col-lg-12">	
					<h2>${ current_menu.title }</h2>
					<h4><i class="fa fa-quote-left"></i>&nbsp;${ current_menu.description ? replace ("{displayName}" , action.webSite.company.displayName ) }&nbsp;<i class="fa fa-quote-right"></i></h4>
				</div>
			</div>
		</header>			
		<!-- END HEADER -->					
		<!-- START MAIN CONTENT -->	
		<div class="container">	
			<div class="row">
				<div class="col-lg-3 visible-lg">
					<div class="headline"><h4> ${current_menu.parent.title} </h4></div>  
                	<p class="margin-bottom-25"><small>${current_menu.parent.description!" " }</small></p>	                					
					<!-- start side menu -->		
					<div class="list-group">
					<#list current_menu.parent.components as item >
						<#if item.name ==  current_menu.name >
						<a href="${item.page}" class="list-group-item active">${ item.title } </a>
						<#else>
						<a href="${item.page}" class="list-group-item">${ item.title } </a>
						</#if>						
					</#list>										
					</div>	
					<!-- end side menu -->					
				</div><!-- ./col-lg-3 -->
				<div class="col-lg-9">
					<div class="row">
						<div class="col-sm-12">					

							<#if hasWebSitePage >							
							${ processedBodyText }
							</#if> 
							<!-- start of tabs -->					
							<div class="tab-v1">									
							<ul class="nav nav-tabs" id="aboutTab">
								<li><a href="#company-history" data-toggle="tab">회사연혁</a></li>
								<li><a href="#company-logo" data-toggle="tab">로고</a></li>
								<li><a href="#company-media" data-toggle="tab">쇼셜미디어</a></li>								
							</ul>
							<!-- Tab panes -->
							<div class="tab-content" style="min-height:300px;">
								<div class="tab-pane fade" id="company-history" style="min-height:300px;">
									<div class="margin-bottom-20"></div>
									<ul class="timeline-v2"></ul>						
								</div>
								<div class="tab-pane fade" id="company-logo" style="min-height:300px;">
									<div class="page-header page-nounderline-header padding-left-10 ">
										<h5><i class="fa fa-info"></i> <small>로고 파일은 AI 와 JPG 파일로 제공됩니다.</small></h5>
									</div>
									<div class="panel panel-default">
										<div class="panel-body">
										<#if action.webSite.getProperty("logo.downloadUrl", null )?? >
											<p class="pull-right">
											<a href="${action.webSite.getProperty("logo.downloadUrl", null )}" class="btn btn-info btn-sm"><i class="fa fa-download"></i> 다운로드</a>	
											</p>											
										</#if> 
											<img src="${request.contextPath}/download/logo/company/${action.webSite.company.name}" class="img-rounded" />
										</div>
									</div>	
								</div>
								<div class="tab-pane fade" id="company-media" style="min-height:300px;">
									<div id="social-media-area" class="row">
										<#list action.connectedCompanySocialNetworks  as item >	
										<div class="col-sm-6">						
											<div class="blank-top-5"></div>
											<div id="${item.serviceProviderName}-panel-${item.socialAccountId}" class="panel panel-default panel-flat">
												<div class="panel-heading">
													<i class="fa fa-${item.serviceProviderName}"></i>&nbsp;${item.serviceProviderName}
													<div class="k-window-actions panel-header-actions">
														<a role="button" href="#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-refresh">Refresh</span></a>
														<a role="button" href="#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-minimize">Minimize</span></a>
														<a role="button" href="#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-maximize">Maximize</span></a>
														<a role="button" href="#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-close">Close</span></a>
													</div>
												</div>		
												<div class="panel-body scrollable" style="min-height:200px; max-height:500px;">
													<ul class="media-list">
														<div id="${item.serviceProviderName}-streams-${item.socialAccountId}">&nbsp;</div>
													</ul>
												</div>							
											</div>										
										</div>
										</#list>												
									</div>
								</div>
							</div>
							</div><!-- end of tabs -->

						</div><!-- ./col-sm-12 -->		
					</div><!-- ./row -->					
				</div><!-- ./col-lg-9 -->				
			</div><!-- ./row -->
		</div><!-- ./container -->										 
		<!-- END MAIN CONTENT -->	
 		<!-- START FOOTER -->
		<#include "/html/common/common-homepage-footer.ftl" >		
		<!-- END FOOTER -->	
		</div><!-- /wrapper -->	
		<!-- START TEMPLATE -->
		<#include "/html/common/common-homepage-templates.ftl" >		
		<!-- END TEMPLATE -->
	</body>    
</html>
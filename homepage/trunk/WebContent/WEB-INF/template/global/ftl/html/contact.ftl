<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title>오시는길</title>
		<#compress>		
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.1.0/font-awesome.min.css',
			'css!${request.contextPath}/styles/common.themes/unify/themes/pomegranate.css',
			'css!${request.contextPath}/styles/common.plugins/animate.css',
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
			
				common.ui.setup({
					features:{
						backstretch : false
					}
				});	
				      
				// START SCRIPT	
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
				
		</style>   	
		</#compress>			
	</head>
	<body>
		<div class="page-loader"></div>
		<div class="wrapper">
		<!-- START HEADER -->
		<#include "/html/common/common-homepage-menu.ftl" >	
		<#assign hasWebSitePage = action.hasWebSitePage("pages.contact.pageId") />
		<#assign menuName = action.targetPage.getProperty("page.menu.name", "USER_MENU") />
		<#assign menuItemName = action.targetPage.getProperty("navigator.selected.name", "MENU_1_6") />
		<#assign current_menu = action.getWebSiteMenu(menuName, menuItemName) />
		<header class="inkium <#if current_menu.parent.css??>${current_menu.parent.css}</#if>">
			<div class="container">
				<div class="col-lg-3 visible-lg">	
					<div class="header-sub-logo">
					<img src="${current_menu.parent.image!"http://img.inkium.com/homepage/sub/sub_company_02.png"}"/>					
					</div>
				</div>
				<div class="col-lg-9">				
					<div class="header-sub-title">
						<h2 class="color-green">${action.targetPage.title}<br/>
						<small> ${action.targetPage.summary!}</small><h2>
					</div>								
					<ul class="breadcrumb">
				        <li><a href="main.do"><i class="fa fa-home fa-lg"></i></a></li>
				        <li><a href="">${current_menu.parent.title}</a></li>
				    	<li class="active">${current_menu.title}</li>
				    </ul>					
				</div>
			</div>
		</header>		
		<!-- END HEADER -->	
		<!-- START MAIN CONTENT -->	
		<div class="container  content no-padding-t">	
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
				</div>
				<div class="col-lg-9">
					<#if hasWebSitePage >							
					${ processedBodyText }
					</#if> 						
				</div>				
			</div>
		</div>									 
		
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
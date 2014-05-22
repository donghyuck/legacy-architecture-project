<#ftl encoding="UTF-8"/>
<html decorator="homepage">
	<head>
		<title> ${action.targetPage.title}</title>
		<#compress>	
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.0.3/font-awesome.min.css',
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

				// 1.  한글 지원을 위한 로케일 설정
				kendo.culture("ko-KR");
				      
				// START SCRIPT					
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
				// END SCRIPT            
			}
		}]);	
		-->
		</script>
		</#compress>	
	</head>
	<body>
		<!-- START HEADER -->
		<#include "/html/common/common-homepage-menu.ftl" >	
		<!-- END HEADER -->
		<header class="cloud">
			<div class="container">
				<div class="col-lg-12">	
					<h2>${action.targetPage.title}</h2>
					<h4><i class="fa fa-quote-left"></i>&nbsp;${action.targetPage.summary!} <i class="fa fa-quote-right"></i>&nbsp;</h4>
				</div>
			</div>
		</header>	
				
		<!-- START MAIN CONTENT -->	
		<div class="container layout">
			<#if action.isSetNavigator()  >
			<#assign current_menu = action.getNavigator() />
			<div class="row">
				<div class="col-lg-3 visible-lg">	
					<div class="headline"><h4> 회사소개 </h4></div>  
                	<p class="margin-bottom-25">&nbsp;</p>					
					<div class="list-group">
					<#list current_menu.parent.components as item >
						<#if item.name ==  current_menu.name >
						<a href="${item.page}" class="list-group-item active">${ item.title } </a>
						<#else>
						<a href="${item.page}" class="list-group-item">${ item.title } </a>
						</#if>						
					</#list>
					</div>
				</div>
				<div class="col-lg-9">		
					<div class="content-main-section" style="min-height:300px;">
					${ action.processedBodyText }
					</div>
				</div>
			</div>
			<#else>
			<div class="row">
				<div class="col-sm-12">
					<div class="content-main-section">
						<div class="page-header">
							<h2>${action.targetPage.title}</h2>
						</div>			
						${ action.processedBodyText }			
					</div>
				</div>
			</div>	
			</#if>
		</div>
		
		<!-- END MAIN CONTENT -->	
 		<!-- START FOOTER -->
		<#include "/html/common/common-homepage-footer.ftl" >		
		<!-- END FOOTER -->	
		<!-- START TEMPLATE -->
		<#include "/html/common/common-homepage-templates.ftl" >		
		<!-- END TEMPLATE -->	
	</body>
</html>
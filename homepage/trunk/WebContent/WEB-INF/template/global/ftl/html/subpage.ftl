<#ftl encoding="UTF-8"/>
<html decorator="homepage">
	<head>
		<title> ${action.targetPage.title}</title>
		<#compress>				
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/common.themes/pomegranate.css" />
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
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

				// 1-1.  한글 지원을 위한 로케일 설정
				common.api.culture();
				// 1-2.  페이지 렌딩
				common.ui.landing();		
				      
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
	<body class="color0">
		<!-- START HEADER -->
		<#include "/html/common/common-homepage-menu.ftl" >	
		<!-- END HEADER -->
		<header class="cloud">
			<div class="container">
				<div class="col-lg-12">	
					<#if action.isSetNavigator()  >
					<#assign current_menu = action.getNavigator() />					
							<ul class="pull-left breadcrumb">
				                <li><a href="main.do">홈</a></li>
				                <li><a href="">${current_menu.parent.title}</a></li>
				                <li class="active">${current_menu.title}</li>
				            </ul>
					</#if>					
					<h2 class="color-green">${action.targetPage.title}  <br/><small><i class="fa fa-quote-left"></i> ${action.targetPage.summary!} <i class="fa fa-quote-right"></i></small></h2>			
				</div>
			</div>
		</header>	
				
		<!-- START MAIN CONTENT -->	
		<div class="container layout">
			<#if action.isSetNavigator()  >
			<#assign current_menu = action.getNavigator() />
			<div class="row">
				<div class="col-lg-3 visible-lg">	
					<div class="headline"><h4> ${current_menu.parent.title} </h4></div>  
                	<p class="margin-bottom-25"><small>${current_menu.parent.description!" " }</small></p>					
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
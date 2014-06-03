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
	/*		'css!${request.contextPath}/styles/common/common.admin.rtl.css',			*/
			'css!${request.contextPath}/styles/common/common.admin.themes.css',
			/*'${request.contextPath}/js/jquery/2.1.1/jquery-2.1.1.min.js',*/
			'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo.extension/kendo.ko_KR.js',
			'${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',			
			'${request.contextPath}/js/bootstrap/3.0.3/bootstrap.min.js',			
			'${request.contextPath}/js/common.plugins/fastclick.js', 
			'${request.contextPath}/js/common.plugins/jquery.slimscroll.min.js', 
			'${request.contextPath}/js/common/common.admin.js',
			'${request.contextPath}/js/common/common.models.js',       	    
			'${request.contextPath}/js/common/common.api.js',
			'${request.contextPath}/js/common/common.ui.js',
			'${request.contextPath}/js/common/common.ui.admin.js'
			],
			complete: function() {
				// 1-1.  한글 지원을 위한 로케일 설정
				common.api.culture();
				// 1-2.  페이지 렌딩
				common.ui.landing();				
				// 1-3.  관리자  로딩
				var currentUser = new User();
				var targetCompany = new Company();	
				common.ui.admin.setup({
					authenticate : function(e){
						e.token.copy(currentUser);
					},
					companyChanged: function(item){
						item.copy(targetCompany);
					}
				});		
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
			<#include "/html/common/common-system-navigation.ftl" >	
			<div id="content-wrapper">
				<div class="page-header">
					<#assign selectedMenu = WebSiteUtils.getMenuComponent("SYSTEM_MENU", "MENU_2_1") />
					<h1><#if selectedMenu.isSetIcon() ><i class="fa ${selectedMenu.icon} page-header-icon"></i></#if> ${selectedMenu.title}  <small><i class="fa fa-quote-left"></i>${selectedMenu.description}<i class="fa fa-quote-right"></i></small></h1>
				</div><!-- / .page-header -->	
				<div class="row">
				
					<div class="col-sm-12">
						<div class="panel panel-default" style="min-height:300px;">
							<div class="panel-heading" style="padding:5px;">
							<span class="panel-title">회사 목록</span>
							<div class="panel-heading-controls">
								<div class="btn-group">
									<button type="button" class="btn btn-info btn-sm btn-control-group" data-action="menu"><i class="fa fa-sitemap"></i> 메뉴</button>
									<button type="button" class="btn btn-info btn-sm btn-control-group" data-action="role"><i class="fa fa-lock"></i> 권한 & 롤</button>
								</div>
								<div class="btn-group">
									<button type="button" class="btn btn-success btn-sm btn-control-group" data-action="group"><i class="fa fa-users"></i> 그룹관리</button>
									<button type="button" class="btn btn-success btn-sm btn-control-group" data-action="user"><i class="fa fa-user"></i> 사용자관리</button>
								</div>
								<button type="button" class="btn btn-defaultbtn-sm  btn-control-group btn-columns-expend" data-action="layout"><i class="fa fa-columns"></i></button>					
							</div>
							</div>
							<div class="panel-body" style="padding:5px;">
								<div class="row marginless paddingless">
									<div class="col-sm-12 body-group marginless paddingless">
										<div id="company-grid"></div>
									</div>
									<div id="company-details" class="col-sm-12 body-group marginless paddingless" style="display:none; padding-top:5px;"></div>
								</div>
							</div>
						</div>
					</div>				
				
				</div>
			</div> <!-- / #content-wrapper -->
			<div id="main-menu-bg">
			</div>
		</div> <!-- / #main-wrapper -->
		<#include "/html/common/common-system-templates.ftl" >			
	</body>    
</html>
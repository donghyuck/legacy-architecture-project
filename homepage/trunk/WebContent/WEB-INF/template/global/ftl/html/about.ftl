<#ftl encoding="UTF-8"/>
<html decorator="homepage">
	<head>
	<#compress>		
		<title>기업소개</title>
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.1.0/font-awesome.min.css',
			'css!${request.contextPath}/styles/common.pages/common.timeline-v2.min.css',
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

				$('#aboutTab').on( 'show.bs.tab', function (e) {
					//e.preventDefault();		
					var show_bs_tab = $(e.target);
					if( show_bs_tab.attr('href') == '#company-history' ){									
						if( $("#company-history .timeline-v2").text().trim().length == 0 ){
							var template = kendo.template($("#timeline-template").html());
							var dataSource = new kendo.data.DataSource({
				                transport: {
				                    read: {
				                        url: "/community/website-company-timeline.do?output=json",
				                        dataType: "json"
				                    }
				                },
				                schema : {
				                	data : "timelines",
				                	model : common.models.Timeline
				                },			                
				                requestStart: function() {
				                    kendo.ui.progress($("#company-history"), true);
				                },
				                requestEnd: function() {
				                    kendo.ui.progress($("#company-history"), false);
				                },
				                change: function() {
									$("#company-history .timeline-v2").html(kendo.render(template, this.view()));
				                }
				            });		
				            dataSource.read();
						}
						 
					} 				
				});				
				$('#aboutTab a:first').tab('show');
								
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
	</#compress>		
	</head>
	<body>
		<div class="page-loader"></div>
		<div class="wrapper">
		<!-- START HEADER -->
		<#include "/html/common/common-homepage-menu.ftl" >	
		<#assign hasWebSitePage = action.hasWebSitePage("pages.about.pageId") />
		<#assign menuName = action.targetPage.getProperty("page.menu.name", "USER_MENU") />
		<#assign menuItemName = action.targetPage.getProperty("navigator.selected.name", "MENU_1_1") />
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
		<div class="container content no-padding-t">	
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
							<#if hasWebSitePage >				
							<div class="page-content" style="min-height:300px;">				
							${ processedBodyText }
							</div>
							<#else>
					<div class="row">
						<div class="col-sm-12">							
							<!-- start of tabs -->					
							<div class="tab-v3">									
							<ul class="nav nav-tabs" id="aboutTab">
								<li><a href="#company-history" data-toggle="tab">회사연혁</a></li>
								<li><a href="#company-logo" data-toggle="tab">로고</a></li>					
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
							</div>
							</div><!-- end of tabs -->
						</div><!-- ./col-sm-12 -->		
					</div><!-- ./row -->								
							</#if> 					
							<a href="${action.webSite.getProperty("logo.downloadUrl", null )}" class="btn btn-info btn-sm"><i class="fa fa-download"></i> 다운로드</a>	
				</div><!-- ./col-lg-9 -->				
			</div><!-- ./row -->
		</div><!-- ./container -->										 
		<!-- END MAIN CONTENT -->	
 		<!-- START FOOTER -->
		<#include "/html/common/common-homepage-footer.ftl" >		
		<!-- END FOOTER -->	
		</div><!-- /wrapper -->	
		<!-- START TEMPLATE -->
		<script type="text/x-kendo-tmpl" id="timeline-template">
 			<li>
 				<time class="cbp_tmtime" datetime="">
 					<span>
	 				#if(isPeriod()){#
	 					<small>#: getFormattedStartDate() # ~ #: getFormattedEndDate() #</small>
	 				#}else{#
	 					<small>#: getFormattedStartDate() #</small>
	 				#}#				
					</span> 
					<span style='color:\\#e67e22;font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;'><strong>#: getEndDateYear() #</strong></span>
				</time>
				<i class="cbp_tmicon rounded-x hidden-xs"></i>
				<div class="cbp_tmlabel">
					#if(headline !== null && headline !== 'null'){#
					<h4>#:headline#</h4>
					#}#
					#if(body !== null && body !== 'null'){#					
					<p>#= body #</p>
					#}#
				</div>
			</li>
		</script>					
		<#include "/html/common/common-homepage-templates.ftl" >		
		<!-- END TEMPLATE -->
	</body>    
</html>
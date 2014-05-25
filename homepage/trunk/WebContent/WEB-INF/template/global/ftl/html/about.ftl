<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title>기업소개</title>
<#compress>			
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/common/common.timeline-v2.css" />
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
			
				// 1.  한글 지원을 위한 로케일 설정
				kendo.culture("ko-KR");
				      
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
				
				
				// Start : Company Social Content 
				<#list action.connectedCompanySocialNetworks  as item >				
				<#assign stream_name = item.serviceProviderName + "_streams_" + item.socialAccountId  />	
				<#assign panel_element_id = "#" + item.serviceProviderName + "-panel-" + item.socialAccountId  />												
				var ${stream_name} = new MediaStreams(${ item.socialAccountId}, "${item.serviceProviderName}" );
				<#if  item.serviceProviderName == "twitter" >
				${stream_name}.setTemplate ( kendo.template($("#twitter-timeline-template").html()) );				
				<#elseif  item.serviceProviderName == "facebook" >
				${stream_name}.setTemplate( kendo.template($("#facebook-homefeed-template").html()) );
				</#if>
				${stream_name}.createDataSource({ 
					transport : {
						parameterMap : function ( options,  operation) {
							return { objectType : 1 };
						} 
					}
				});							
				${stream_name}.dataSource.read();				
				$( "${panel_element_id} .panel-header-actions a").each(function( index ) {
					var header_action = $(this);
					header_action.click(function (e){
						e.preventDefault();		
						var header_action_icon = header_action.find('span');
						if (header_action.text() == "Minimize" || header_action.text() == "Maximize"){
							$("${panel_element_id} .panel-body").toggleClass("hide");				
							if( header_action_icon.hasClass("k-i-maximize") ){
								header_action_icon.removeClass("k-i-maximize");
								header_action_icon.addClass("k-i-minimize");
							}else{
								header_action_icon.removeClass("k-i-minimize");
								header_action_icon.addClass("k-i-maximize");
							}
						} else if (header_action.text() == "Refresh"){								
							${stream_name}.dataSource.read();							
						} 
					});								
				});				
				</#list>	
				<#if !action.user.anonymous >							
				</#if>	
				
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
		.timeline-v2 > li .cbp_tmlabel{ 
			color: #555;
		}
		
		</style>   	
</#compress>				
	</head>
	<body>		
	<div class="wrapper">
		<!-- START HEADER -->
		<#include "/html/common/common-homepage-menu.ftl" >	
		<#assign current_menu = action.getWebSiteMenu("USER_MENU", "MENU_1_1") />
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
					<div class="row">
						<div class="col-sm-12">		
							<#if action.hasWebSitePage("pages.about.pageId") >							
							${ processedBodyText }
							</#if> 
							<!-- start of tabs -->					
							<div class="tab-v1">									
							<ul class="nav nav-tabs" id="aboutTab">
								<li><a href="#company-history" data-toggle="tab">회사연역</a></li>
								<li><a href="#company-logo" data-toggle="tab">로고</a></li>
								<li><a href="#company-media" data-toggle="tab">쇼셜미디어</a></li>								
							</ul>
							<!-- Tab panes -->
							<div class="tab-content" style="min-height:300px;">
								<div class="tab-pane fade" id="company-history" style="min-height:300px;">
									<div class="margin-bottom-20"></div>								
									<!--
									<div class="panel panel-default">
										<div class="panel-body">
									-->		
									<ul class="timeline-v2"></ul>
									<!--				
										</div>
									</div> -->								
								</div>
								<div class="tab-pane fade" id="company-logo" style="min-height:300px;">
									<div class="page-header page-nounderline-header padding-left-10 ">
										<h5><i class="fa fa-info"></i> <small>로고 파일은 AI 와 JPG 형식을 제공됩니다.</small></h5>
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
						</div>
					</div>					
				</div>				
			</div>
		</div>		
	</div>
	<!-- /wrapper -->	
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
				
		<!-- END MAIN CONTENT -->	
 		<!-- START FOOTER -->
		<#include "/html/common/common-homepage-footer.ftl" >		
		<!-- END FOOTER -->	
		<!-- START TEMPLATE -->
		<#include "/html/common/common-homepage-templates.ftl" >		
		<!-- END TEMPLATE -->
	</body>    
</html>
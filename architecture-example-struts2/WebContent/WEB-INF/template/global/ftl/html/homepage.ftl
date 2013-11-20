<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title><#if action.user.company ?? >${action.user.company.displayName }<#else>::</#if></title>
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/bootstrap/3.0.0/font-awesome.css',			
			'css!${request.contextPath}/styles/bootstrap/3.0.0/social-buttons.css',
			'${request.contextPath}/js/jquery/1.9.1/jquery.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo/kendo.ko_KR.js',			
			'${request.contextPath}/js/bootstrap/3.0.0/bootstrap.min.js',
			'${request.contextPath}/js/bootstrap/3.0.0/tooltip.js',			
			'${request.contextPath}/js/common/holder.js',
			'${request.contextPath}/js/common/common.models.js',
			'${request.contextPath}/js/common/common.ui.js'],
			complete: function() {
			
				// 1.  한글 지원을 위한 로케일 설정
				kendo.culture("ko-KR");
				      
				// START SCRIPT	
				$("#top-menu").kendoMenu();
				$("#top-menu").show();
				var currentUser = new User({});			
				// ACCOUNTS LOAD	
				var accounts = $("#account-panel").kendoAccounts({
					dropdown : false,
					authenticate : function( e ){
						currentUser = e.token;						
					},
					<#if CompanyUtils.isallowedSignIn(action.company) ||  !action.user.anonymous  >
					template : kendo.template($("#account-template").html()),
					</#if>
					afterAuthenticate : function(){
						$('.dropdown-toggle').dropdown();
						//Holder.run();
						
						if( currentUser.anonymous ){
							var validator = $("#login-panel").kendoValidator({validateOnBlur:false}).data("kendoValidator");							
							$("#login-btn").click(function() { 
								$("#login-status").html("");
								if( validator.validate() )
								{								
									accounts.login({
										data: $("form[name=login-form]").serialize(),
										success : function( response ) {
											$("form[name='login-form']")[0].reset();               
											$("form[name='login-form']").attr("action", "/main.do").submit();										
										},
										fail : function( response ) {  
											$("#login-password").val("").focus();												
											$("#login-status").kendoAlert({ 
												data : { message: "입력한 사용자 이름 또는 비밀번호가 잘못되었습니다." },
												close : function(){	
													$("#login-password").focus();										
												 }
											}); 										
										},		
										error : function( thrownError ) {
											$("form[name='login-form']")[0].reset();                    
											$("#login-status").kendoAlert({ data : { message: "잘못된 접근입니다." } }); 									
										}																
									});															
								}else{	}
							});	
						}
					}
				});				
				
				// Start : Company Social Content 
				<#list action.companySocials  as item >				
					<#if item.serviceProviderName == "twitter">					
					var twitterTemplate = kendo.template($("#twitter-timeline-template").html());
					var twitterDataSource = new kendo.data.DataSource({
						transport: {
							read: {
								type : 'POST',
								type: "json",
								//url : '${request.contextPath}/social/get-twitter-usertimeline.do?output=json',
								url : '${request.contextPath}/social/get-twitter-hometimeline.do?output=json',
							},
							parameterMap: function (options, operation){
								if (operation == "read" && options) {										                        								                       	 	
									return { socialAccountId: ${ item.socialAccountId } };									                            	
								}
							} 
						},
						requestStart: function() {
							kendo.ui.progress($("#company-twitter-timeline"), true);
						},
						requestEnd: function() {
							kendo.ui.progress($("#company-twitter-timeline"), false);
						},
						change: function() {
							$("#company-twitter-timeline").html(kendo.render(twitterTemplate, this.view()));
						},
						error:handleKendoAjaxError,
						schema: {
							data : "homeTimeline"
						}
		            });            				
		            twitterDataSource.read();
		            
		            <#elseif item.serviceProviderName == "facebook">
					
					var facebookTemplate = kendo.template($("#facebook-homefeed-template").html());
					var facebookDataSource = new kendo.data.DataSource({
						transport: {
							read: {
								type : 'POST',
								type: "json",
								url : '${request.contextPath}/social/get-facebook-homefeed.do?output=json',
							},
							parameterMap: function (options, operation){
								if (operation == "read" && options) {										                        								                       	 	
									return { socialAccountId: ${ item.socialAccountId } };	                            	
								}
							} 
						},
						requestStart: function() {
							kendo.ui.progress($("#company-facebook-homefeed"), true);
						},
						requestEnd: function() {
							kendo.ui.progress($("#company-facebook-homefeed"), false);
						},
						change: function() {
							$("#company-facebook-homefeed").html(kendo.render(facebookTemplate, this.view()));
						},
						error:handleKendoAjaxError,
						schema: {
							data : "homeFeed"
						}
		            });            				
		            facebookDataSource.read();
					</#if>
					
					$("#${item.serviceProviderName}-panel .panel-header-actions a").each(function( index ) {
						var panel_header_action = $(this);						
						if( panel_header_action.text() == "Minimize" ){
							panel_header_action.click(function (e) {
								e.preventDefault();		
								$("#${item.serviceProviderName}-panel .panel-body").toggleClass("hide");								
								var panel_header_action_icon = panel_header_action.find('span');
								if( panel_header_action_icon.hasClass("k-i-minimize") ){
									panel_header_action.find('span').removeClass("k-i-minimize");
									panel_header_action.find('span').addClass("k-i-maximize");
								}else{
									panel_header_action.find('span').removeClass("k-i-maximize");
									panel_header_action.find('span').addClass("k-i-minimize");
								}								
							});
						} else if (panel_header_action.text() == "Refresh" ){
							panel_header_action.click(function (e) {
								e.preventDefault();		
								${item.serviceProviderName}DataSource.read();
							});
						}
					} );
								
				</#list>
				// End : Company Social Content 
								
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
		
		.jumbotron {
			background-color : #2eb3a6;
		}		
		</style>   	
	</head>
	<body id="doc">
		<!-- START HEADER -->
		<div class="navbar navbar-default navbar-fixed-top" role="navigation">
		<#include "/html/common/common-homepage-wide-menu.ftl" >	
		</div>
		<!-- END HEADER -->	
		<!-- START MAIN CONTENT -->
		<div class="blank-top-50" ></div>
		<div class="jumbotron layout">							
			<div class="container">
				<p><a class="btn btn-primary btn-default" role="button">Learn more</a></p>
			</div>
		</div>								 
		<div class="container layout">	
				<div class="row">
					<div class="col-lg-8">
					</div>								
				</div>		
				<div class="row">
					<div id ="notice-view-panel" class="col-lg-4">
						<div class="panel panel-warning">
							<div class="panel-heading">공지</div>
							<div class="panel-body">
								<div class="list-group">
									<a href="#" class="list-group-item">
										<h5 class="list-group-item-heading">오픈소스 기반의 프레임워크 기술 컨설팅을 제공합니다.</h5>
										<p class="list-group-item-text">
											<img src="${request.contextPath}/content/image.do?imageId=71" class="img-rounded img-responsive">
											오픈소스 기반의 프레임워크 기술 컨설팅을 제공합니다.
										</p>
									</a>
									<a href="#" class="list-group-item">
										<h4 class="list-group-item-heading">List group item heading</h4>
										<p class="list-group-item-text">Donec id elit non mi porta gravida at eget metus. Maecenas sed diam eget risus varius blandit.</p>
									</a>
									<a href="#" class="list-group-item">
										<h4 class="list-group-item-heading">List group item heading</h4>
										<p class="list-group-item-text">Donec id elit non mi porta gravida at eget metus. Maecenas sed diam eget risus varius blandit.</p>
									</a>
								</div>
							</div>
						</div>
					</div>											
					<div id="facebook-panel" class="col-lg-4">
						<div class="panel panel-info">
							<div class="panel-heading">
								<i class="icon-facebook"></i> 뉴스
								<div class="k-window-actions panel-header-actions">
									<a role="button" href="#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-refresh">Refresh</span></a>
									<a role="button" href="#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-minimize">Minimize</span></a>
									<a role="button" href="#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-maximize">Maximize</span></a>
									<a role="button" href="#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-close">Close</span></a>
								</div>
							</div>		
							<div class="panel-body">
								<ul class="media-list">
									<div id="company-facebook-homefeed"></div>
								</ul>
							</div>							
						</div>
					</div>
					<div id="twitter-panel" class="col-lg-4">
						<div class="panel panel-info">
							<div class="panel-heading">
								<i class="icon-twitter"></i> 뉴스
								<div class="k-window-actions panel-header-actions">
									<a role="button" href="#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-refresh">Refresh</span></a>
									<a role="button" href="#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-minimize">Minimize</span></a>
									<a role="button" href="#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-maximize">Maximize</span></a>	
									<!--
																	
									<a role="button" href="#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-close">Close</span></a>
									-->
								</div>							
							</div>
							<div class="panel-body">
								<ul class="media-list">
									<div id="company-twitter-timeline"></div>
								</ul>
							</div>
						</div>
					</div>					
				</div>		
			</div>		

		<div id="attach-window"></div>					
		<!-- END MAIN CONTENT -->			
 		<!-- START FOOTER -->
		<footer> 
		</footer>
		<!-- END FOOTER -->	
		<!-- START TEMPLATE -->

		<script type="text/x-kendo-tmpl" id="attachment-list-view-template">
			<div class="attach">			
			#if (contentType.match("^image") ) {#
				<img src="${request.contextPath}/secure/view-attachment.do?width=150&height=150&attachmentId=#:attachmentId#" alt="#:name# 이미지" class="img-responsive"/>
			# } else { #			
				<img src="http://placehold.it/146x146&amp;text=[file]"></a>
			# } #	
				<div class="attach-description">
					<h3>#:name#</h3>
					<p>#:size# 바이트</p>
				</div>
			</div>
		</script>		
		<script id="attachment-preview-template" type="text/x-kendo-template">	
			#if (contentType.match("^image") ) {#
				<img src="${request.contextPath}/secure/view-attachment.do?attachmentId=#:attachmentId#" alt="#:name# 이미지" class="img-responsive"/>
				<p class="blank-top-5">
						<a class="k-button" href="${request.contextPath}/secure/download-attachment.do?attachmentId=#= attachmentId #" >다운로드</a>
						<a class="k-button" href="${request.contextPath}/secure/download-attachment.do?attachmentId=#= attachmentId #" >삭제</a>					
				</p>				
			# } else { #		
				<div class="k-grid k-widget" style="width:100%;">
					<div style="padding-right: 17px;" class="k-grid-header">
						<div class="k-grid-header-wrap">
							<table cellSpacing="0">
								<thead>
									<tr>
										<th class="k-header">속성</th>
										<th class="k-header">값</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
					<div style="height: 199px;" class="k-grid-content">
						<table style="height: auto;" class="system-details" cellSpacing="0">
							<tbody>
								<tr>
									<td>파일</td>
									<td>#= name #</td>
								</tr>
								<tr class="k-alt">
									<td>종류</td>
									<td>#= contentType #</td>
								</tr>
								<tr>
									<td>크기(bytes)</td>
									<td>#= size #</td>
								</tr>				
								<tr>
									<td>다운수/클릭수</td>
									<td>#= downloadCount #</td>
								</tr>											
							</tbody>
						</table>	
					</div>
				</div>
				<p class="blank-top-5">
					<a class="k-button" href="${request.contextPath}/secure/download-attachment.do?attachmentId=#= attachmentId #" >다운로드</a>
					<a class="k-button" href="${request.contextPath}/secure/download-attachment.do?attachmentId=#= attachmentId #" >삭제</a>	
				</p>	
				# } #  	
		</script>		
		<#include "/html/common/common-homepage-social-templates.ftl" >		
		<#include "/html/common/common-homepage-templates.ftl" >		
		<!-- END TEMPLATE -->
	</body>    
</html>
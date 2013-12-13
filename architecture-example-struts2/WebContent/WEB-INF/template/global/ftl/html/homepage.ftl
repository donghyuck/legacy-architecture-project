<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title><#if action.user.company ?? >${action.user.company.displayName }<#else>::</#if></title>
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.0.3/font-awesome.min.css',
			'${request.contextPath}/js/jquery/1.9.1/jquery.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo/kendo.ko_KR.js',			
			'${request.contextPath}/js/bootstrap/3.0.0/bootstrap.min.js',
			'${request.contextPath}/js/bootstrap/3.0.0/tooltip.js',			
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
				
				// Announces 
				$("#announce-panel").data( "dataSource", 
	 				new kendo.data.DataSource({
						transport: {
							read: {
								type : 'POST',
								type: "json",
								url : '${request.contextPath}/community/list-announce.do?output=json'
							} 
						},
						requestStart: function() {
							kendo.ui.progress($("#announce-panel"), true);
						},
						requestEnd: function() {
							kendo.ui.progress($("#announce-panel"), false);
						},
						change: function() {
							$("#announce-panel table tbody").html(kendo.render(kendo.template($("#announcement-template").html()), this.view()));
							if( this.data().length > 0 ){
								viewAnnounce (this.at(0).announceId) ;
							}
						},
						error:handleKendoAjaxError,
						schema: {
							data : "targetAnnounces",
							model : Announce
						}
					})
				);								
								
				$("#announce-panel .panel-header-actions a").each(function( index ) {
						var panel_header_action = $(this);						
						if( panel_header_action.text() == "Minimize" ){
							panel_header_action.click(function (e) {
								e.preventDefault();		
								$("#announce-panel .panel-body").toggleClass("hide");								
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
								$("#announce-panel").data( "dataSource").read();
							});
						}
				} );				
				
				$("#announce-panel").data( "dataSource").read();
					
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
		
		function viewAnnounce (announceId){		
			var item = $("#announce-panel").data( "dataSource").get(announceId);			

			var template = kendo.template($('#announcement-view-template').html());			
			$("#announce-view").html(
				template(item)
			);
			kendo.bind($("#announce-view"), item );			
			
			$("#announce-view div button").each(function( index ) {			
				var announce_button = $(this);			
				if( announce_button.hasClass( 'custom-announce-modify') ){
					announce_button.click(function (e) { 
						e.preventDefault();					
																		
					} );
				}else if ( announce_button.hasClass('custom-announce-delete') ){
					announce_button.click(function (e) { 
						e.preventDefault();
						if( confirm("삭제하시겠습니까 ?") ) {
							// delete ...	
						}
					} );
				}			
			} );
		}		
		-->
		</script>		
		<style scoped="scoped">
		blockquote p {
			font-size: 15px;
		}

		#announce-view .popover {
			position : relative;
			max-width : 500px;
		}
						
		</style>   	
	</head>
	<body>
		<!-- START HEADER -->
		<#include "/html/common/common-homepage-menu.ftl" >	
		<!-- END HEADER -->	
		<!-- START MAIN CONTENT -->	
		<div class="container layout">
				<div calss="row">
					<div class="col-lg-8">
						<img src="${request.contextPath}/content/image.do?imageId=81" class="img-rounded img-responsive">
					</div>
					<div class="col-lg-4">
						<div class="blank-top-10" ></div>		
						<p><a class="btn btn-success btn-default" role="button">Learn more</a></p>
					</div>
				</div>					
		</div>
		<div class="blank-top-10" ></div>								 
		<div class="container layout">	
				<div class="row">
					<div class="col-lg-6">
						<!-- start announce panel -->
						<div id="announce-panel" >	
							<div class="panel panel-default">
								<div class="panel-heading">알림
									<div class="k-window-actions panel-header-actions">
										<a role="button" href="#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-refresh">Refresh</span></a>
										<a role="button" href="#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-minimize">Minimize</span></a>
										<a role="button" href="#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-maximize">Maximize</span></a>
										<a role="button" href="#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-close">Close</span></a>
									</div>
								</div>
								<div class="panel-body layout">					
									<div  id="announce-view"></div>
									<br><br>
									<table class="table table-hover">
										<tbody>										
											<tr>
												<th></th>	
												<td></td>
											</tr>										
										</tbody>
									</table>												
								</div>
							</div>		
						</div>
						<!-- end announce panel -->							
					</div>							
					<div class="col-lg-6">
					
					<div id="facebook-panel">
						<div class="panel panel-success">
							<div class="panel-heading">
								<i class="fa fa-facebook"></i>
								<div class="k-window-actions panel-header-actions">
									<a role="button" href="#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-refresh">Refresh</span></a>
									<a role="button" href="#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-minimize">Minimize</span></a>
									<a role="button" href="#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-maximize">Maximize</span></a>
									<a role="button" href="#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-close">Close</span></a>
								</div>
							</div>		
							<div class="panel-body scrollable" style="max-height:500px;">
									<ul class="media-list">
										<div id="company-facebook-homefeed"></div>
									</ul>
							</div>							
						</div>
					</div>
					
					<div id="twitter-panel">
						<div class="panel panel-success">
							<div class="panel-heading">
								<i class="fa fa-twitter"></i>
								<div class="k-window-actions panel-header-actions">
									<a role="button" href="#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-refresh">Refresh</span></a>
									<a role="button" href="#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-minimize">Minimize</span></a>
									<a role="button" href="#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-maximize">Maximize</span></a>	
									<!--																	
									<a role="button" href="#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-close">Close</span></a>
									-->
								</div>							
							</div>
							<div class="panel-body scrollable" style="max-height:500px;">
								<ul class="media-list">
									<div id="company-twitter-timeline"></div>
								</ul>
							</div>
						</div>
					</div>										
					</div>								
				</div>		
				<div class="row">

				</div>		
			</div>		

		<div id="attach-window"></div>					
		<!-- END MAIN CONTENT -->	

 		<!-- START FOOTER -->
		<#include "/html/common/common-homepage-footer.ftl" >		
		<!-- END FOOTER -->	
		<!-- START TEMPLATE -->
		<#include "/html/common/common-homepage-templates.ftl" >		
		<!-- END TEMPLATE -->
	</body>    
</html>
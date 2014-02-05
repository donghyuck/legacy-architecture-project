<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title>기업소개</title>
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.0.3/font-awesome.min.css',
			'${request.contextPath}/js/jquery/1.9.1/jquery.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo/kendo.ko_KR.js',			
			'${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js',		
			'${request.contextPath}/js/bootstrap/3.0.3/bootstrap.min.js',	
			'${request.contextPath}/js/common/common.models.min.js',
			'${request.contextPath}/js/common/common.ui.min.js'],
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
						//$('.dropdown-toggle').dropdown();
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
						if (header_action.text() == "Minimize"){
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
						
		</style>   	
	</head>
	<body>
		<!-- START HEADER -->
		<#include "/html/common/common-homepage-menu.ftl" >	
		<#assign current_menu = action.findMenuComponent("USER_MENU", "MENU_1_1") />
		<header class="cloud">
			<div class="container">
				<div class="col-lg-12">	
					<h1>${ current_menu.title }</h1>
					<h4><i class="fa fa-quote-left"></i>&nbsp; ${action.user.company.displayName}를 소개합니다.&nbsp;<i class="fa fa-quote-right"></i></h4>
				</div>
			</div>
		</header>				
		<!-- END HEADER -->					
		<!-- START MAIN CONTENT -->	
		<div class="container layout">	
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
							<div class="panel panel-info panel-flat panel-border-thick">
								<div class="panel-body">
									회사는 어떤 ..
								</div>
							</div>		
						</div>			
					</div>					
					<!-- social media -->
					<div id="social-media-area" class="row">
					<#list action.connectedCompanySocialNetworks  as item >	
						<div class="custom-panels-group col-sm-6"> 
								<div id="${item.serviceProviderName}-panel-${item.socialAccountId}" class="panel panel-default panel-flat panel-border-thick">
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
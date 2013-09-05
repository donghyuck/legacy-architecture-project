<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title></title>
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [			
			'${request.contextPath}/js/jquery/1.9.1/jquery.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo/kendo.ko_KR.js',			
			'${request.contextPath}/js/bootstrap/3.0.0/bootstrap.min.js',
			'${request.contextPath}/js/bootstrap/3.0.0/tooltip.js',
			'${request.contextPath}/js/common/common.models.js',
			'${request.contextPath}/js/common/common.ui.js'],
			complete: function() {      
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
					template : kendo.template($("#account-template").html()),
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
				// END SCRIPT            
			}
		}]);	
		-->
		</script> 		   
		
    <style scoped="scoped">

		</style>   	
	</head>
	<body id="doc">
		<!-- START HEADER -->
		<div class="header">
		<#include "/html/common/common-homepage-menu.ftl" >	
		</div>
		<!-- END HEADER -->			
		<!-- START MAIN CONTENT --> 
		<div id="wraps">
			<div class="container layout">		
				<div class="row">
					<div class="col-lg-12">												
								<div class="page-header">
						  	<img src="http://images.apple.com/ios/images/product_title.png"/>
						</div>						
					</div>
				</div>
				<div class="row">
					<div class="col-lg-8">												
						<img src="http://www.inkium.com/homepage/consulting/img/01.jpg" alt="..." class="img-rounded">
						<img src="http://www.inkium.com/homepage/consulting/img/02.jpg" alt="..." class="img-rounded">
						<img src="http://www.inkium.com/homepage/consulting/img/03.jpg" alt="..." class="img-rounded">
						<img src="http://www.inkium.com/homepage/consulting/img/04.jpg" alt="..." class="img-rounded">
					</div>
					<div class="col-lg-4">
						<div class="panel panel-default">
							<div class="panel-heading">Panel heading without title</div>
							<div class="panel-body">
							Panel content
							</div>
						</div>
						<div class="panel panel-default" id="my-messages">
							<div class="panel-heading">뉴스</div>					
							<div class="panel-body">						
								<div class="popover right" style="display:true;">
									<div class="arrow"></div>
									<!--<h3 class="popover-title">알림</h3>-->
									<div class="popover-content">
										<p>새로운 메시지가 없습니다.</p>
									</div>
								</div>
								<div class="popover left" style="display:true;">
									<div class="arrow"></div>
									<!--<h3 class="popover-title">알림</h3>-->
									<div class="popover-content">
										<p>새로운 메시지가 없습니다.</p>
									</div>
								</div>										
							</div>
						</div>													
					</div>		
				</div>
			</div>		
		</div>		
		<!-- END MAIN CONTENT -->		
 		<!-- START FOOTER -->
		<footer>
		</footer>
		<!-- END FOOTER -->	
		<#include "/html/common/common-homepage-templates.ftl" >		
		<!-- END TEMPLATE -->

	</body>    
</html>
<#ftl encoding="UTF-8"/>
<html decorator="secure-metroblack">
<head>
		<title>관리자 메인</title>
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'${request.contextPath}/js/jquery/1.9.1/jquery.min.js',	
       	    '${request.contextPath}/js/kendo/kendo.web.min.js',
       	    '${request.contextPath}/js/kendo/kendo.ko_KR.js',
       	    '${request.contextPath}/js/common/common.models.js',
       	    '${request.contextPath}/js/common/common.ui.js'
      	     ],        	  	   
			complete: function() {      
				// START SCRIPT
				
				var currentUser = new User({});			
				// ACCOUNTS LOAD	
				var accounts = $("#account").kendoAccounts({
					authenticate : function( e ){
						currentUser = e.token;						
					},
					template : kendo.template($("#account-template").html())
				});
								
				// LEFT MENU !				
				$("#topbar .open").kendoTopBar({ 
					template : kendo.template($("#top-menu-template").html()),
					data : currentUser,
					renderTo : $('.navigation'),
					dataSource : {
						transport: {
							read: {
								url: "/secure/get-company-menu-component.do?output=json",
								dataType: "json",
								data: {
									menuName: "SYSTEM_MENU"
								}
							}
						},
						schema: {
							data: "targetCompanyMenuComponent.components"
						}
					},
					select : function( item ){
						$("#content_frame_wrapper").attr( "src", item.action );
						 kendo.bind($("#content_title"), item );
					}
				 });				
				$("#content_frame_wrapper").css( "height", $(document).height() - 54 );
		
				// END SCRIPT
			}
		}]);
		-->
		</script> 		 
		<style>
			#datail_pane {
				background-color : #F5F5F5;
			}				
		</style>
	</head>
	<body>
		<!-- START HEADER -->
		<!-- END HEADER -->

		<!-- START MAIN CONTENT -->		
		<section class="container">		
			<div id="topbar">
				<button class="square sidebar open">
					<i>Toggle</i>
				</button>
				<article id="content_title"><span data-bind="text:title"></span><span class="desc" data-bind="text:description"></span></article>
				<div id="account"></div>				
			</div>			
			<div class="content">			
				<div class="navigation" style="display:none;"></div>
				<iframe id="content_frame_wrapper" class="content" src="/secure/main-company.do" frameborder="1" allowtransparency="true" hspace="0" style="width: 100%; height:100%;"></iframe>				
			</div>
		<section>
		<!-- END MAIN CONTENT -->					
		<!-- START FOOTER -->
		<!--
		<footer class="row"> 
		</footer>
		-->
		<!-- END FOOTER -->				
		<!-- 공용 템플릿 -->
		<#include "/html/common/common-templates.ftl" >
	</body>    
</html>
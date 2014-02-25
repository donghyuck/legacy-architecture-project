<#ftl encoding="UTF-8"/>
<html decorator="secure-metro">
<head>
		<title>관리자 메인</title>
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
       	    '${request.contextPath}/js/kendo/kendo.web.min.js',
       	    '${request.contextPath}/js/kendo/kendo.ko_KR.js',
       	    '${request.contextPath}/js/common/common.models.js',
       	    '${request.contextPath}/js/common/common.ui.js'],        	  	   
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
						var url = item.action ;
						if(url.indexOf("?") != -1){
							url = url + "&companyId=" + $("#companyList").data("kendoDropDownList").value();
						}else{
							url = url + "?companyId=" + $("#companyList").data("kendoDropDownList").value();
						}
						 
						$("#main-content-frame").attr( "src", url );
						 kendo.bind($("#content_title"), item );
					},
					doAfter : function( content ){						
						$("#companyList").kendoDropDownList({
							dataTextField: "displayName",
							dataValueField: "companyId",
							dataSource: {
								transport: {
									read: {
										type: "json",
										url: '${request.contextPath}/secure/list-company.do?output=json',
										type:'POST'
									}
								},
								schema: { 
									data: "companies",
									model : Company
								}
							}
						});					
						if( !user.isSystem )	
							$("#companyList").data("kendoDropDownList").readonly();
					}
				 });	
				$("#main-content-frame").css( "height", $(document).height() -51 );
				$("#main-content-frame").attr("src" , 	"/secure/system-info.do");							
				$(window).resize(function() {
					$("#main-content-frame").css( "height", $(document).height() -51 );
				});
				// END SCRIPT
			}
		}]);
		-->
		</script> 		 
		<style>
			body {
				overflow-y : hidden;
			}
					
			.container {
				padding-top : 51px;
				overflow-y : none;
			}
			
		</style>
	</head>
	<body>
		<!-- START HEADER -->
		<section>
			<div id="topbar">
				<button class="square sidebar open">
					<i>Toggle</i>
				</button>
				<article id="content_title"><span data-bind="text:title"></span><span class="desc" data-bind="text:description"></span></article>
				<div id="account"></div>				
			</div>			
			<div class="navigation" style="display:none;">
			</div>	
		</section>
		<!-- END HEADER -->
		<!-- START MAIN CONTENT -->		
		<section class="container">			
			<div class="row">			
			<iframe id="main-content-frame" style="width: 100%; height: 100%; background-color: #f5e5c5;" frameborder="0"  hspace="0"></iframe>		
			</div>
		<section>
		<!-- END MAIN CONTENT -->					
		<!-- START FOOTER -->
		<!-- END FOOTER -->				
		<!-- 공용 템플릿 -->
		<#include "/html/common/common-templates.ftl" >
	</body>    
</html>
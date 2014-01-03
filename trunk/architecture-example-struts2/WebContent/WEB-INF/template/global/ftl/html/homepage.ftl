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
			'${request.contextPath}/js/bootstrap/3.0.3/bootstrap.min.js',	
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
				
				// 1. Announces 				
				/**
				$("#announce-grid").data( "announcePlaceHolder", new Announce () );				
				$("#announce-grid").kendoGrid({
					dataSource : new kendo.data.DataSource({
						transport: {
							read: {
								type : 'POST',
								dataType : "json", 
								url : '${request.contextPath}/community/list-announce.do?output=json'
							},
							parameterMap: function(options, operation) {
								if (operation != "read" && options.models) {
									return {models: kendo.stringify(options.models)};
								}
							} 
						},
						pageSize: 10,
						error:handleKendoAjaxError,
						schema: {
							data : "targetAnnounces",
							model : Announce
						}
					}),
					sortable: true,
					height: 300,
					columns: [ 
						{field:"announceId", title: "ID", width: 50, attributes: { "class": "table-cell", style: "text-align: center " }} ,
						{field:"subject", title: "주제"}
					],
					selectable: "row",
					change: function(e) { 
						var selectedCells = this.select();
						if( selectedCells.length > 0){
							var selectedCell = this.dataItem( selectedCells );	    	
							var announcePlaceHolder = $("#announce-grid").data( "announcePlaceHolder" );
							announcePlaceHolder.announceId = selectedCell.announceId;
							announcePlaceHolder.subject = selectedCell.subject;
							announcePlaceHolder.body = selectedCell.body;
							announcePlaceHolder.startDate = selectedCell.startDate ;
							announcePlaceHolder.endDate = selectedCell.endDate;
							announcePlaceHolder.modifiedDate = selectedCell.modifiedDate;
							announcePlaceHolder.creationDate = selectedCell.creationDate;
							announcePlaceHolder.user = selectedCell.user;			
							announcePlaceHolder.editable = false;					 
							showAnnounce();	
						}
					},
					dataBound: function(e) {					
						var selectedCells = this.select();
						this.select("tr:eq(1)");
					}
				});
				**/							
				<#if !action.user.anonymous >				
				
				</#if>	
				// END SCRIPT            
			}
		}]);	
		
		function showAnnounce () {
			var announcePlaceHolder = $("#announce-grid").data( "announcePlaceHolder" );
			var template = kendo.template($('#announcement-view-template').html());			
			$("#announce-view").html( template(announcePlaceHolder) );
			kendo.bind($("#announce-view"), announcePlaceHolder );					
		}
				
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
										
				</div>
				<div class="col-lg-6">
											
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
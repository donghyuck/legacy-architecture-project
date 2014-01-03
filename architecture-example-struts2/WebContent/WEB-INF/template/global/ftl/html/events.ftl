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
					rowTemplate: kendo.template($("#rowTemplate").html()),
					sortable: true,
					height: 300,
					columns: [ 
						{field:"announceId", title: "ID", width: 50, attributes: { "class": "table-cell", style: "text-align: center " }} ,
						{field:"subject", title: "공지 & 이벤트"}
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
				<#if !action.user.anonymous >				
				
				</#if>	
				// END SCRIPT            
			}
		}]);	
		
		function showAnnounce () {
			var announcePlaceHolder = $("#announce-grid").data( "announcePlaceHolder" );
			var template = kendo.template($('#announcement-detail-panel-template').html());			
			$("#announce-panel").html( template(announcePlaceHolder) );
			kendo.bind($("#announce-panel"), announcePlaceHolder );	
			$("#announce-actions .nav a").each(function( index ) {
				var panel_footer_action = $(this);	
				var panel_footer_action_icon = panel_footer_action.find('i');
				//if( panel_footer_action.attr("href") == "list" ){
					panel_footer_action.click(function (e) {
						e.preventDefault();
						if( panel_footer_action_icon.hasClass("fa-bars") ){
							panel_footer_action.find('i').removeClass("fa-bars");
							panel_footer_action.find('i').addClass("fa-angle-up");
							kendo.fx($("#announce-grid")).expand("vertical").stop().play();							
						}else{
							panel_footer_action.find('i').removeClass("fa-angle-up");
							panel_footer_action.find('i').addClass("fa-bars");
							
							kendo.fx($("#announce-grid")).expand("vertical").stop().reverse();
						}		
						//$("#announce-grid").toggleClass("hide", 1000);				
					});		
				//}	
			} );
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
		
		<#assign current_menu = action.findMenuComponent("USER_MENU", "MENU_1") />
		<div class="jumbotron jumbotron-ad hidden-print jumbotron-page-header bg-metro-black">
		  <div class="container">
		    <h2 class="page-title"><i class="fa fa-building-o"></i>&nbsp; ${ current_menu.title }</h2>
		    <p></p>
		  </div>
		</div>	
				
		<!-- START MAIN CONTENT -->	
		<div class="container layout">	
			<div class="row">
				<div class="col-lg-3 visible-lg">
					<div class="list-group">
						<a href="${request.contextPath}/about.do" class="list-group-item">기업소개 </a>
					  <a href="${request.contextPath}/events.do" class="list-group-item active">공지 & 이벤트</a>
					  <a href="${request.contextPath}/customers.do" class="list-group-item">고객</a>
					  <a href="${request.contextPath}/press.do" class="list-group-item">뉴스</a>
					  <a href="${request.contextPath}/contact.do" class="list-group-item">오시는길</a>
					</div>					
				</div>
				<div class="col-lg-9">					
					<div id="announce-panel" style="min-height:200px;" >					
						<div class="alert alert-warning">
							새로운 공지 & 이벤트가 없습니다.						
						</div>
					</div>					
					<div id="announce-actions" class="content-block">
					<ul class="nav nav-pills">
						<li class="pull-right"><a href="#list"><i class="fa fa-bars"></i>&nbsp;목록</a></li>						
					</ul>
					<div class="panel panel-default">
						<div class="panel-heading"><i class="fa fa-bars"></i>&nbsp;목록
							<div class="k-window-actions panel-header-actions">
								<a role="button" href="#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-refresh">Refresh</span></a>
								<a role="button" href="#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-minimize">Minimize</span></a>
								<a role="button" href="#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-maximize">Maximize</span></a>
							</div>
						
						</div>
						<div class="panel-body">
							<div id="announce-grid"></div>
						</div>
					</div>					
					
					</div>
				</div>				
			</div>
		</div>									 
			
		<!-- END MAIN CONTENT -->	

		<script id="rowTemplate" type="text/x-kendo-tmpl">
			<tr>
				<td  class="text-center">#: announceId #</td>
				<td class="details">
					<span class="title">#: subject #</span><br>
					<small>기간 : #: kendo.toString(startDate, "yyyy.MM.dd hh:mm") # ~  #: kendo.toString(endDate, "yyyy.MM.dd hh:mm") #</small><br>
				</td>
			</tr>
		</script>
            
 		<!-- START FOOTER -->
		<#include "/html/common/common-homepage-footer.ftl" >		
		<!-- END FOOTER -->	
		<!-- START TEMPLATE -->
		<#include "/html/common/common-homepage-templates.ftl" >		
		<!-- END TEMPLATE -->
	</body>    
</html>
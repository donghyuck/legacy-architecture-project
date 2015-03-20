<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title>기업소개</title>
<#compress>		
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/common.themes/pomegranate.css" />
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
		/*	'css!${request.contextPath}/styles/jquery.extension/component.min.css',*/
			'css!${request.contextPath}/styles/common.extension/animate.css',
			'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo.extension/kendo.ko_KR.js',			
			'${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js',		
			'${request.contextPath}/js/bootstrap/3.1.0/bootstrap.min.js',			
			'${request.contextPath}/js/common/common.models.js',
			'${request.contextPath}/js/common/common.api.js',			
			'${request.contextPath}/js/common/common.ui.js',
			'${request.contextPath}/js/jquery.extension/modernizr.custom.js',
			'${request.contextPath}/js/jquery.extension/classie.js',
			],
			complete: function() {
			
				// 1-1.  한글 지원을 위한 로케일 설정
				common.api.culture();
				// 1-2.  페이지 렌딩
				common.ui.landing();		
				
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
				
				// 1. Announces 				
				//$("#announce-grid").data( "announcePlaceHolder", new Announce () );			
				var selectedAnnounceId = ${ParamUtils.getLongParameter(request, "announceId", 0 )} ;		
				$("#announce-grid").kendoGrid({
					dataSource: new kendo.data.DataSource({
						transport: {
							read: {
								type : 'POST',
								dataType : "json", 
								url : '${request.contextPath}/community/list-announce.do?output=json'
							},
							parameterMap: function(options, operation) {
								if (operation != "read" && options.models) {
									return {models: kendo.stringify(options.models)};
								}else{
									return {objectType:30}
								}
							},
						},
						pageSize: 15,
						error:common.api.handleKendoAjaxError,				
						schema: {
							data : "targetAnnounces",
							model : Announce
						}
					}),	
					columns: [
						{field: "subject", title: "제목", sortable : false },
						{hidden: isMobile(), field: "creationDate", title: "게시일", width: 120, format: "{0:yyyy.MM.dd}"}
					],
					sortable: true,
					pageable: false,
					selectable: "single",
					rowTemplate: kendo.template($("#announce-row-template").html()),
					height: 430,
					change: function(e) { 
						var selectedCells = this.select();
						var selectedCell = this.dataItem( selectedCells );	
						$("#announce-grid").data( "announcePlaceHolder", selectedCell );
						displayAnnouncement();
					},
					dataBound : function(e){
						if(selectedAnnounceId>0){
							this.select('tr[data-id="' + selectedAnnounceId + '"]');							
						}
					}			
				});							
				
		
				<#if !action.user.anonymous >				
				
				</#if>	
				// END SCRIPT            
			}
		}]);	
		
		function isMobile(){
			return kendo.support.mobileOS.device === 'iphone'  ? true : false ;
		}
		
		function displayAnnouncement () {			
		
			var announcePlaceHolder = $("#announce-grid").data( "announcePlaceHolder" );			
			var template = kendo.template($('#announcement-detail-panel-template').html());			
			$("#announce-view-panel").html( template(announcePlaceHolder) );
			kendo.bind($("#announce-view-panel"), announcePlaceHolder );					
			
			var listPanel = $('#announce-list-section');
			var viewPanel = $('#announce-view-content-section');			
			var fade = kendo.fx(listPanel).fade("out");			
			fade.play();
			
			setTimeout(function() {
				listPanel.addClass('hidden');
				fade.stop();
				viewPanel.removeClass("hidden");
				common.ui.animate(viewPanel, 'bounceIn');
			}, 300);			
			
			$("#announce-view-panel").find(".close").click(function (e) {				
				common.ui.animate(viewPanel, 'animated bounceOut');				
				setTimeout(function() {
					viewPanel.removeClass();
					viewPanel.addClass('hidden');					
					fade.reverse();					
					listPanel.removeClass('hidden');
				}, 800);
			});			
		}				
		-->
		</script>		
		<style scoped="scoped">
		blockquote p {
			font-size: 15px;
		}

		#announce-list-section .k-grid-header .k-header {
			text-align: center;
		}

		.content-main-section {
			/** background: #F98262;	 */	
			width: 100%;
			height: 100%;
			min-height:500px;
		}							
		</style>
</#compress>		   	
	</head>
	<body class="color0">
		<!-- START HEADER -->
		<#include "/html/common/common-homepage-menu.ftl" >	
		<#assign hasWebSitePage = action.hasWebSitePage("pages.events.pageId") />
		<#assign menuName = action.targetPage.getProperty("page.menu.name", "USER_MENU") />
		<#assign menuItemName = action.targetPage.getProperty("navigator.selected.name", "MENU_1_3") />
		<#assign current_menu = action.getWebSiteMenu(menuName, menuItemName) />
		<header class="cloud">
			<div class="container">
				<div class="col-lg-12">	
					<h2 class="color-green">${ current_menu.title }</h2>
					<h5><i class="fa fa-quote-left"></i>&nbsp;${ current_menu.description ? replace ("{displayName}" , action.webSite.company.displayName ) }&nbsp;<i class="fa fa-quote-right"></i></h5>
				</div>
			</div>
		</header>		
		<!-- END HEADER -->			
		<!-- START MAIN CONTENT -->	
		<div class="container layout">			
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
				</div>
				<div class="col-lg-9">		
					<div class="content-main-section">
						<div class="page-header padding-left-10">
							<h5><small>게시 기간이 지난 내용들은 목록에서 보여지지 않습니다.</small></h5>
						</div>	
														
						<section id="announce-list-section" style="">
							<div id="announce-grid"></div>
						</section>
						<section id="announce-view-content-section" class="hidden">						
							<div id="announce-view-panel"></div>
						</section>						
					</div>
				</div>				
			</div>
		</div>									 			
		<!-- END MAIN CONTENT -->	
		<script id="announce-row-template" type="text/x-kendo-tmpl">
			<tr data-uid="#: uid #" data-id="#:announceId#">
				<td><span class="label label-danger label-lightweight">공지</span>&nbsp;#: subject #	 </td>
				#if( !isMobile() ){ #	
				<td class="text-center">#: kendo.toString(creationDate, "yyyy.MM.dd") #</td>
				# } #
			</tr>
		</script>
						
		<script id="alert-message-template" type="text/x-kendo-tmpl">
			<div class="alert alert-warning">새로운 공지 & 이벤트가 없습니다.</div>
		</script>			
			
 		<!-- START FOOTER -->
		<#include "/html/common/common-homepage-footer.ftl" >		
		<!-- END FOOTER -->	
		<!-- START TEMPLATE -->
		<#include "/html/common/common-homepage-templates.ftl" >		
		<!-- END TEMPLATE -->
	</body>    
</html>
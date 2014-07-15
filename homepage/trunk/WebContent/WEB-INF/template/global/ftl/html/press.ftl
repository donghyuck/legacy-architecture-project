<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title>뉴스</title>
		<#compress>		
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.1.0/font-awesome.min.css',
			'css!${request.contextPath}/styles/common.themes/unify/themes/pomegranate.css',
			'css!${request.contextPath}/styles/common.plugins/animate.css',
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

				// START SCRIPT	
				common.ui.setup({
					features:{
						backstretch : false
					}
				});					
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

				var forumId = ${action.webSite.getLongProperty( "pages.press.forumId", 1)};		
				var selectedtopicId = ${ParamUtils.getLongParameter(request, "topicId", 0 )} ;		
				$("#topic-grid").kendoGrid({
					dataSource: new kendo.data.DataSource({
						transport: {
							read: {
								type : 'POST',
								dataType : "json", 
								url : '${request.contextPath}/community/list-forum-topics.do?output=json'
							},
							parameterMap: function(options, operation) {
								if (operation != "read" && options.models) {
									return {models: kendo.stringify(options.models)};
								}else{
									return {startIndex: options.skip, pageSize : options.pageSize, forumId: forumId}
								}
							},
						},
						pageSize: 15,
						error:common.api.handleKendoAjaxError,				
						schema: {
							total : "targetTopicCount",
							data : "targetTopics",
							model : common.models.ForumTopic
						},
						serverPaging: true
					}),	
					columns: [
						/*{field: "topicId", title: "ID", sortable : false , width:80 , attributes: { "class": "table-cell", style: "text-align: center " }},*/
						{field: "subject", title: "제목", sortable : false, template: '#: subject # <span class="label label-primary label-lightweight rounded">#= $.timeago(creationDate) #</span>' },						
						{field: "viewCnt", title: "조회수", width: "100px", sortable : false, attributes: { "class": "table-cell", style: "text-align: center " }, hidden: isMobile() },
						{field:"creationDate", title: "게시일", width: "100px", format: "{0:yyyy.MM.dd}", attributes: { "class": "table-cell", style: "text-align: center " }, hidden: isMobile()  }
					],
					sortable: true,
					pageable: { refresh:true, pageSizes:false,  messages: { display: ' {1} / {2}' }  },
					rowTemplate: kendo.template($("#topic-row-template").html()),
					selectable: "single",
					height: 430,
					change: function(e) { 
						var selectedCells = this.select();
						var selectedCell = this.dataItem( selectedCells );	
						if( selectedCells.length > 0){
							var selectedCell = this.dataItem( selectedCells );	  
							setTopicViewerSource(selectedCell); 
							displayTopic();
						}
					},
					dataBound: function(e) {		
						if( $('#topic-viewer').data("model") != null ) {
							$('#topic-viewer').data("model").hide();
						} else {
							if( selectedtopicId > 0 ){
								this.select('tr[data-id="' + selectedtopicId + '"]');						
							}
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
				
		function setTopicViewerSource(source){
			var renderToString = "topic-viewer";				
			var renderTo = $("#"+ renderToString );						
			if( !renderTo.data("topicPlaceHolder") ){
				renderTo.data("topicPlaceHolder", new common.models.ForumTopic() );
			}		
			source.copy(renderTo.data("topicPlaceHolder"));
		}		

		function displayTopic (){			
			var renderToString = "topic-viewer";
			var renderTo = $("#"+ renderToString );
			topicPlaceHolder =renderTo.data("topicPlaceHolder")	
			if( renderTo.text().length == 0 ){
				renderTo.html($('#topic-viewer-template').html());
				var topicModel = kendo.observable({
					topic : topicPlaceHolder,
					authorPhotoUrl : "${request.contextPath}/download/profile/inkium?width=150&height=150",
					show : function(){
						kendo.fx($('#topic-viewer-panel')).fadeOut().duration(700).reverse();
						kendo.fx($('#topic-viewer-panel')).slideIn("down").play();	
						this.shown = true;						
						$.ajax({ url : '${request.contextPath}/community/update-topic-view-count.do?output=json&topicId='+ this.topic.get('topicId')});
					},
					shown : false,
					hide : function () {
						kendo.fx($('#topic-viewer-panel')).slideIn("down").reverse();	
						kendo.fx($('#topic-viewer-panel')).fadeOut().duration(700).play();
						this.shown = false;
					},
					scrollup:function(e){
						$('html,body').animate({ scrollTop:  $('#topic-grid').offset().top }, 300);
					},
					scrolldown :function(e){
						$('html,body').animate({scrollTop: renderTo.offset().top - 80 }, 300);
					}
				});				
				renderTo.find("button[class*=custom-btn-list]").click( function (e){
					topicModel.scrollup();
				});
				renderTo.data("model", topicModel );
				kendo.bind( renderTo , topicPlaceHolder );
			}
			if(!renderTo.data("model").shown)
				renderTo.data("model").show();
			renderTo.data("model").set("authorPhotoUrl", "${request.contextPath}/download/profile/" + topicPlaceHolder.user.username + "?width=150&height=150" );
			renderTo.data("model").scrolldown();
		}
				
		-->
		</script>		
		<style scoped="scoped">

		.k-grid table tr.k-state-selected{
			background: #428bca;
			color: #ffffff; 
		}
		
		#topic-grid .k-grid-header .k-header {
			text-align: center;
		}

		.content-main-section {
			width: 100%;
			height: 100%;
			min-height:500px;
		}
		
		p, li, li a, td a {
			color: #555;
		}								
		</style>   
		</#compress>			
	</head>
	<body>
		<div class="page-loader"></div>
		<div class="wrapper">	
		<!-- START HEADER -->
		<#include "/html/common/common-homepage-menu.ftl" >	
		<#assign hasWebSitePage = action.hasWebSitePage("pages.press.pageId") />
		<#assign menuName = action.targetPage.getProperty("page.menu.name", "USER_MENU") />
		<#assign menuItemName = action.targetPage.getProperty("navigator.selected.name", "MENU_1_5") />
		<#assign current_menu = action.getWebSiteMenu(menuName, menuItemName) />
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
		<div class="container content no-padding-t">	
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
							<h5><small>게시일 순서로 뉴스가 보여집니다.</small></h5>
						</div>			
						<div id="topic-viewer-panel" class="panel panel-default" style="margin-bottom: 20px; display:none;">
							<div class="panel-body">													
								<div id="topic-viewer"></div>
							</div>
						</div>									
						<div id="topic-grid"></div>	
					</div>				
				</div>			
			</div>
		</div>									 		
		<!-- END MAIN CONTENT -->	
 		<!-- START FOOTER -->
		<#include "/html/common/common-homepage-footer.ftl" >		
		<!-- END FOOTER -->	
		</div><!-- /wrapper -->	
		<!-- START TEMPLATE -->
		<script id="topic-row-template" type="text/x-kendo-tmpl">
			<tr data-uid="#: uid #" data-id="#:topicId#">
				<td><a href="\\#">#: subject #</a> <!--<span class="label label-primary label-lightweight rounded">#= $.timeago(creationDate) #</span>--></td>
				#if(!isMobile()){#
				<td class="text-center">#: viewCnt #</td>
				<td class="text-center">#: kendo.toString(creationDate, "yyyy.MM.dd") #</td>
				#}#
			</tr>
		</script>
		<script type="text/x-kendo-tmpl" id="topic-viewer-template">		
			<div class="page-heading">
				<h4 data-bind="html:topic.subject" style="font-weight:bold;"></h4>		
				<hr class="devider">
				<ul class="list-unstyled">
					<li><span class="label label-info label-lightweight">게시일</span> <span class="text-muted" data-bind="text: topic.formattedCreationDate"></span></li>
					<li><span class="label label-info label-lightweight">조회수</span> <code data-bind="text: topic.viewCnt"></code></li>					
				</ul>
				<div class="media">
					<a class="pull-left" href="\\#">
						<img data-bind="attr:{ src:authorPhotoUrl }" width="30" height="30" class="img-rounded">
					</a>
					<div class="media-body">
						<ul class="list-unstyled">
							<li><span data-bind="visible:topic.user.nameVisible, text: topic.user.name"></span> <code data-bind="text: topic.user.username"></code></li>
							<li><span data-bind="visible:topic.user.emailVisible, text: topic.user.email"></span></li>
						</ul>					
					</div>		
				</div>					
			</div>
			<div class="margin-bottom-20"><hr class="devider"></div>
			<div data-bind="html:topic.content"></div>
			<div class="pull-right">
				<button  type="button" class="btn btn-info btn-sm custom-btn-list"><i class="fa fa-angle-double-down"></i> 목록</button>		
			</div>			
		</script>		
		<#include "/html/common/common-homepage-templates.ftl" >		
		<!-- END TEMPLATE -->
	</body>    
</html>
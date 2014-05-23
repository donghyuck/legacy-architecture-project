<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title>기업소개</title>
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.0.3/font-awesome.min.css',
			'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',
			'${request.contextPath}/js/jquery.extension/jquery.timeago.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo.extension/kendo.ko_KR.js',			
			'${request.contextPath}/js/bootstrap/3.1.0/bootstrap.min.js',
			'${request.contextPath}/js/common/common.models.js',			
			'${request.contextPath}/js/common/common.api.js',
			'${request.contextPath}/js/common/common.ui.js'],
			complete: function() {
			
				// 1.  한글 지원을 위한 로케일 설정
				kendo.culture("ko-KR");
				      
				// START SCRIPT	
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
				$("#news-panel").data( "newsPlaceHolder", new common.models.ForumTopic () );								
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
									return {forumId:1}
								}
							},
						},
						pageSize: 15,
						error:common.api.handleKendoAjaxError,				
						schema: {
							total : "targetTopicCount",
							data : "targetTopics",
							model : common.models.ForumTopic
						}
					}),	
					columns: [
						{field: "topicId", title: "ID", sortable : false , width:80 , attributes: { "class": "table-cell", style: "text-align: center " }},
						{field: "subject", title: "제목", sortable : false, template: '#: subject # <span class="label label-warning label-lightweight rounded">#= $.timeago(creationDate) #</span>' },						
						{field: "viewCnt", title: "조회수", width: "100px", sortable : false, attributes: { "class": "table-cell", style: "text-align: center " } },
						{field:"creationDate", title: "게시일", width: "100px", format: "{0:yyyy.MM.dd}", attributes: { "class": "table-cell", style: "text-align: center " } }
					],
					sortable: true,
					pageable: true,
					selectable: "single",
					height: 430,
					change: function(e) { 
						var selectedCells = this.select();
						var selectedCell = this.dataItem( selectedCells );	
						if( selectedCells.length > 0){
							var selectedCell = this.dataItem( selectedCells );	    							
							var newsPlaceHolder = $("#news-panel").data( "newsPlaceHolder" ); 
							selectedCell.copy(newsPlaceHolder);					
							$("#news-panel").data( "newsPlaceHolder", newsPlaceHolder ); // 로우 데이터 저장							 
							updateViewCount(selectedCell.topicId);
						}
					}			
				});									
				<#if !action.user.anonymous >				
				
				</#if>	
				// END SCRIPT            
			}
		}]);	
		
		function updateViewCount(topicId){
			// jquery http send
			jQuery.ajax({	
				url : '${request.contextPath}/community/update-topic-view-count.do?output=json&topicId='+topicId
				}).done(function(data){
					//alert('카운트 증가');
					
					showNewsPanel(); // 상세 화면 호출
				});
		}
		
		function showNewsPanel (){			
			var newsPlaceHolder = $("#news-panel").data( "newsPlaceHolder" ); // 데이터 GET
			//alert(newsPlaceHolder.subject);
			var template = kendo.template($('#news-view-template').html()); // 템플릿 GET
			console.log('1');
			$("#news-view").html( template(newsPlaceHolder) );	 //html 세팅
			console.log('2');
			kendo.bind($("#news-view"), newsPlaceHolder );	//데이터 바인딩
			console.log('3');
			$("#news-view button[class*=custom-list]").click( function (e){
					$('html,body').animate({ scrollTop:  0 }, 300);
			} ); //목록 버튼 이벤트 설정
			console.log('4');
			$('html,body').animate({scrollTop: $("#news-view").offset().top - 80 }, 300); // 화면이동 이벤트
			console.log('5');
			//alert($('#news-view').html());	
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
			/** background: #F98262;	 */	
			width: 100%;
			height: 100%;
			min-height:500px;
		}
						
		</style>   	
	</head>
	<body class="color0">
		<!-- START HEADER -->
		<#include "/html/common/common-homepage-menu.ftl" >	
		<#assign current_menu = action.getWebSiteMenu("USER_MENU", "MENU_1_5") />
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
					<div class="content-main-section">
						<div class="page-header padding-left-10">
							<h5><small>게시일 순서로 뉴스가 보여집니다.</small></h5>
						</div>						
						<div id="topic-grid"></div>		
					</div>				
				</div>				
			</div>
			<div id="news-panel" class="custom-panels-group col-sm-6" >
				<div class="panel-body">					
					<div  id="news-view"></div>
				</div>
			</div>					
		</div>	
				
		<!-- END MAIN CONTENT -->	

 		<!-- START FOOTER -->
		<#include "/html/common/common-homepage-footer.ftl" >		
		<!-- END FOOTER -->	
		<!-- START TEMPLATE -->		
		<script type="text/x-kendo-tmpl" id="news-view-template">		
			
			<div class="page-heading">
				<h4 data-bind="html:subject"></h4>				
			</div>
			
			<div class="media">
				<a class="pull-left" href="\\#">
				<img src="${request.contextPath}/download/profile/#: user.photoUrl #?width=150&height=150" width="30" height="30" class="img-rounded">
				</a>
				<div class="media-body">
					<h5 class="media-heading">
						# if( user.nameVisible ){#
						#: user.name # (#: user.username #)
						# } else { #
						#: user.username #
						# } # 		
						# if( user.emailVisible ){#
						<br>(#: user.email #)
						# } #	
					</h5>		
				</div>
			</div>	
			
			<div class="blank-top-5" ></div>
			<div data-bind="html:content"></div>	
			<div class="blank-top-5" ></div>
			<div class="btn-group pull-right">
				<button  type="button" class="btn btn-info btn-sm custom-list "><i class="fa fa-angle-double-up"></i> 목록</button>		
			</div>
		</script>
		<#include "/html/common/common-homepage-templates.ftl" >		
		<!-- END TEMPLATE -->
		
	</body>    
</html>
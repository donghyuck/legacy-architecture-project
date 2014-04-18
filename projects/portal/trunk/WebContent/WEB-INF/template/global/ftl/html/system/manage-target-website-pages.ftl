<#ftl encoding="UTF-8"/>
<html decorator="secure">
	<head>
		<title>시스템 정보</title>
<#compress>		
		<script type="text/javascript"> 
		yepnope([{
			load: [ 
			'css!${request.contextPath}/styles/font-awesome/4.0.3/font-awesome.min.css',
			'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',
			'${request.contextPath}/js/bootstrap/3.0.3/bootstrap.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo.extension/kendo.ko_KR.js',			 
			'${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js', 
			'${request.contextPath}/js/common/common.models.js',
			'${request.contextPath}/js/common/common.api.js',
			'${request.contextPath}/js/common/common.ui.js',
			'${request.contextPath}/js/common/common.ui.system.js',
			'${request.contextPath}/js/ace/ace.js',],        	   
			complete: function() {               
				
				// 1.  한글 지원을 위한 로케일 설정
				kendo.culture("ko-KR");
										
				// 2. ACCOUNTS LOAD		
				var currentUser = new User();
				var accounts = $("#account-panel").kendoAccounts({
					visible : false,
					authenticate : function( e ){
						currentUser = e.token.copy(currentUser);							
					}
				});			
														
				// 3.MENU LOAD 
				var companyPlaceHolder = new Company({ companyId: ${action.user.companyId} });
				
				$("#navbar").data("companyPlaceHolder", companyPlaceHolder);				
				var topBar = $("#navbar").extNavbar({
					template : $("#top-navbar-template").html(),
					items : [{ 
						name:"companySelector", 
						selector: "#companyDropDownList", 
						value: ${action.user.companyId}, 
						change : function (data){
							data.copy(companyPlaceHolder);
							kendo.bind($("#company-info"), companyPlaceHolder );
						}	
					}]
				});
												 
				 // 4. PAGE MAIN		
				 var sitePlaceHolder = new common.models.WebSite( {webSiteId: ${ action.targetWebSite.webSiteId}} );
				 $("#website-info").data("sitePlaceHolder", sitePlaceHolder );
				common.api.callback(  
				{
					url :"${request.contextPath}/secure/get-site.do?output=json", 
					data : { targetSiteId:  sitePlaceHolder.webSiteId },
					success : function(response){
						var site = new common.models.WebSite(response.targetWebSite);
						site.copy( sitePlaceHolder );
						kendo.bind($("#website-info"), sitePlaceHolder );
						$('button.btn-control-group').removeAttr("disabled");						
					}
				}); 
				
				
				 common.ui.handleButtonActionEvents(
					$("button.btn-control-group"), 
					{event: 'click', handlers: {
						'page-create' : function(e){
							kendo.fx($("#page-list-panel")).expand("vertical").duration(200).reverse();
							showPageEditor();							
						},
						'page-publish' : function(e){
							alert( "hello2" );				
						},						
						group : function(e){
							topBar.go('main-group.do');				
						}, 	
						user : function(e){
							topBar.go('main-user.do');			
						}, 							
						'page-delete' : function(e){
							alert( "hello3" );			
						},
						'page-editor-close' : function(e){
							kendo.fx($("#page-editor-panel")).expand("vertical").duration(200).reverse();								
							kendo.fx($("#page-list-panel")).expand("vertical").duration(200).play();				
						},
						back : function(e){
							goWebsite();					
						}																  						 
					}}
				);
			}	
		}]);
			
		function goWebsite (){					
			$("form[name='navbar-form'] input[name='targetSiteId']").val( $("#website-info").data("sitePlaceHolder").webSiteId );
			$("#navbar").data("kendoExtNavbar").go("view-site.do");							
		}
		
		function showPageEditor(){
		
			//var editor = ace.edit("htmleditor");
			//editor.getSession().setMode("ace/mode/html");
			//editor.getSession().setUseWrapMode(true);
		
			var renderToString = "webpage-editor" ;
			createEditor(renderToString);			
			kendo.fx($("#page-editor-panel")).expand("vertical").duration(200).play();
			
			
			
		}
		
		
		function createPageImageBroswer(renderToString, editor){			
			if( $("#"+ renderToString).length == 0 ){
				$('body').append('<div id="'+ renderToString +'"></div>');
			}					
			var renderTo = $("#"+ renderToString);	
			if(!renderTo.data("kendoExtImageBrowser")){
				var imageBrowser = renderTo.extImageBrowser({
					template : $("#image-broswer-template").html(),
					apply : function(e){						
						editor.data("kendoEditor").exec("inserthtml", { value : e.html } );
						imageBrowser.close();
					}				
				});
			}
			return renderTo.data("kendoExtImageBrowser");
		}
		
		function createPageLinkPopup(renderToString, editor){		
			if( $("#"+ renderToString).length == 0 ){
				$('body').append('<div id="'+ renderToString +'"></div>');
			}				
			var renderTo = $("#"+ renderToString);		
			if(!renderTo.data("kendoExtEditorPopup") ){		
				var hyperLinkPopup = renderTo.extEditorPopup({
					type : 'createLink',
					title : "하이퍼링크 삽입",
					template : $("#link-popup-template").html(),
					apply : function(e){						
						editor.data("kendoEditor").exec("inserthtml", { value : e.html } );
						hyperLinkPopup.close();
					}
				});
			}
			return renderTo.data("kendoExtEditorPopup");
		}
		
		function createEditor( renderToString ){			
			if( $("#"+ renderToString).length == 0 ){
				$('body').append('<div id="'+ renderToString +'"></div>');
			}			
			var renderTo = $("#"+ renderToString);			
			if(!renderTo.data("kendoEditor") ){					
				var imageBroswer = createPageImageBroswer( renderToString + "-imagebroswer", renderTo);				
				var linkPopup = createPageLinkPopup(renderToString + "-linkpopup", renderTo);	
				renderTo.kendoEditor({
						tools : [
							'bold',
							'italic',
							'insertUnorderedList',
							'insertOrderedList',
							{	
								name: "createLink",
								exec: function(e){
									linkPopup.show();
									return false;
								}								
							},
							'unlink',
							{	
								name: "insertImage",
								exec: function(e){
									imageBroswer.show();
									return false;
								}
							},
							'viewHtml'
						],
						stylesheets: [
							"${request.contextPath}/styles/bootstrap/3.1.0/bootstrap.min.css",
							"${request.contextPath}/styles/common/common.ui.css"
						]
				});
			}		
		}				
		</script>
		<style type="text/css" media="screen">

		.k-grid-content{
			height:200px;
		}			
		#htmleditor { 
			position: absolute;
			top: 0;
			right: 0;
			bottom: 0;
			left: 0;
		}
		</style>		
</#compress>		
	</head>
	<body>
		<!-- START HEADER -->
		<section id="navbar"></section>
		<!-- END HEADER -->
		<!-- START MAIN CONTNET -->
		
		<div class="container-fluid">		
			<div class="row">			
				<div class="page-header">
					<#assign selectedMenuItem = action.getWebSiteMenu("SYSTEM_MENU", "MENU_1_2") />
					<h1>${selectedMenuItem.title}     <small><i class="fa fa-quote-left"></i>&nbsp;${selectedMenuItem.description}&nbsp;<i class="fa fa-quote-right"></i></small></h1>
				</div>				
			</div>	
			<div class="row">	
				<div class="col-lg-12">
					<div class="panel panel-default" style="min-height:300px;">
						<div class="panel-heading" style="padding:5px;">
							<div class="btn-group">
								<button type="button" class="btn btn-info btn-control-group btn-sm" data-action="group"><i class="fa fa-users"></i> 그룹관리</button>
								<button type="button" class="btn btn-info btn-control-group btn-sm" data-action="user"><i class="fa fa-user"></i> 사용자관리</button>
							</div>			
							<div class="btn-group">
								<button type="button" class="btn btn-primary btn-control-group btn-sm" data-action="back" disabled="disabled"  title="이전 페이지로 이동" ><i class="fa fa-level-up"></i></button>			
							</div>														
						</div>
						<div id="page-list-panel" class="panel-body" style="padding:5px;">
							<div class="page-header page-nounderline-header text-primary" style="min-height: 45px;">
									<h5 >
										<small><i class="fa fa-info"></i> 웹 페이지는 게시 상태에서 보여집니다.</small>
									</h5>
									<div class="pull-right">
										<div class="btn-group">
										<button type="button" class="btn btn-primary btn-control-group btn-sm" data-toggle="button" data-action="page-create" disabled="disabled"><i class="fa fa-file"></i> 새 페이지</button>
										<button type="button" class="btn btn-primary btn-control-group btn-sm" data-toggle="button" data-action="page-publish" disabled="disabled"><i class="fa fa-external-link"></i> 게시</button>
										<button type="button" class="btn btn-primary btn-control-group btn-sm" data-toggle="button" data-action="page-delete" disabled="disabled"><i class="fa fa-trash-o"></i> 삭제</button>
										</div>										
									</div>
							</div>		
							페이지 그리드																						
						</div>					
						<div  id="page-editor-panel" class="panel-body" style="padding:5px; display:none;">	
							<div class="page-header page-nounderline-header text-primary" style="min-height: 45px;">
									<h5 >
										<small><i class="fa fa-info"></i> 웹 페이지는 게시 상태에서 보여집니다.</small>
									</h5>
									<div class="pull-right">
										<button type="button" class="btn btn-primary btn-control-group btn-sm" data-toggle="button" data-action="page-editor-close" disabled="disabled"><i class="fa fa-level-up"></i>  닫기</button>
									</div>
							</div>		
							
							
							<div class="panel panel-default">
								<div class="panel-heading" style="padding:5px;">
								
								</div>
								<div class="panel-body">
									 <textarea id="webpage-editor" rows="10" cols="30" style="height:440px"></textarea>
								</div>
							</div>							
										
						</div>
						<div class="panel-body" style="padding:5px;">
							
						</div>
					</div>	
				</div>
			</div>
		</div>				
		<div id="website-info" ></div>  
		<div id="account-panel" ></div>    		
		<!-- END MAIN CONTNET -->
		<!-- START FOOTER -->
		<footer>  		
		</footer>
		<!-- END FOOTER -->	
		<#include "/html/common/common-system-templates.ftl" >		
		<#include "/html/common/common-editor-templates.ftl" >	
	</body>
</html>
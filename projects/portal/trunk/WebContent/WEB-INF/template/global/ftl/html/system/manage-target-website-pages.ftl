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
							$("#page-list-panel").hide();
							kendo.fx($("#page-list-panel")).expand("vertical").duration(200).reverse();
							emptyPageEditorSource();
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
				
				createPageGrid();
			}	
		}]);
			
		function goWebsite (){					
			$("form[name='navbar-form'] input[name='targetSiteId']").val( $("#website-info").data("sitePlaceHolder").webSiteId );
			$("#navbar").data("kendoExtNavbar").go("view-site.do");							
		}

		function doPageEdit(){
			kendo.fx($("#page-list-panel")).expand("vertical").duration(200).reverse();
			showPageEditor();							
		}		
		
		function doPageDelete(){
			var grid = $("#website-page-grid").data("kendoGrid");
			var selectedCells = grid.select();
			var selectedCell = grid.dataItem( selectedCells ); 		
			alert("delete") ;
		}
				
		function createPageGrid(){
			if(!$("#website-page-grid").data('kendoGrid') ){
				var sitePlaceHolder = $("#website-info").data("sitePlaceHolder");
				$("#website-page-grid").kendoGrid({
                    dataSource: {
                    	serverFiltering: false,
                        transport: { 
                            read: { url:'${request.contextPath}/secure/list-website-page.do?output=json', type: 'POST' },
	                        parameterMap: function (options, type){
	                            return { startIndex: options.skip, pageSize: options.pageSize,  targetSiteId: sitePlaceHolder.webSiteId }
	                        }
                        },
                        schema: {
                            total: "targetPageCount",
                            data: "targetPages",
                            model: common.models.Page
                        },
                        error:handleKendoAjaxError,
                        batch: false,
                        pageSize: 15,
                        serverPaging: false,
                        serverFiltering: false,
                        serverSorting: false
                    },
                    columns: [
                    	{ field: "check_row", title: " ", filterable: false, sortable: false, locked: true, lockable: false, width: 30, template: "<input class='check_row' type='checkbox' />" },
                        { field: "pageId", title: "ID", width:50,  filterable: false, sortable: false , headerAttributes: { "class": "table-header-cell", style: "text-align: center" }, locked: true, lockable: false}, 
                        { field: "name", title: "이름", width: 200, headerAttributes: { "class": "table-header-cell", style: "text-align: center"}, locked: true , template: $('#webpage-name-template').html() }, 
                        { field: "title", title: "제목", width: 300 , headerAttributes: { "class": "table-header-cell", style: "text-align: center" }}, 
                        { field: "versionId", title: "버전", width: 80, headerAttributes: { "class": "table-header-cell", style: "text-align: center" } },
                        { field: "pageState", title: "상태", width: 120, headerAttributes: { "class": "table-header-cell", style: "text-align: center" }, template: '<span class="label label-info">#: pageState #</span>'},
                        { field: "user.username", title: "작성자", width: 100, headerAttributes: { "class": "table-header-cell", style: "text-align: center" } },
                        { field: "creationDate",  title: "생성일", width: 120,  format:"{0:yyyy.MM.dd}", headerAttributes: { "class": "table-header-cell", style: "text-align: center" } },
                        { field: "modifiedDate", title: "수정일", width: 120,  format:"{0:yyyy.MM.dd}", headerAttributes: { "class": "table-header-cell", style: "text-align: center" } } ],         
                    filterable: true,
                    sortable: true,
                    resizable: true,
                    pageable: { refresh:true, pageSizes:false,  messages: { display: ' {1} / {2}' }  },
                    selectable: 'row',
                    height: '100%',
                    change: function(e) {                    
                        var selectedCells = this.select();                 
  						if( selectedCells.length > 0){ 
							var selectedCell = this.dataItem( selectedCells ); 
							setPageEditorSource(selectedCell);
 						} 						
					},
					dataBound: function(e){		
						 var selectedCells = this.select();
						 if(selectedCells.length == 0 ){
						 //	var newUser = new User ();
						 //	newUser.copy($("#website-page-grid").data("pagePlaceHolder"));
						//	$("#user-details").hide();
						 }
					}
				}).data('kendoGrid');
			}
		}		

		
		function showPageEditor(){		
			var renderToString = "webpage-editor";
			createEditor(renderToString);
			$("button.btn-editor-control-group[data-action='page-editor-save']").attr('disabled', 'disabled');
			kendo.fx($("#page-editor-panel")).expand("vertical").duration(200).play();
		}

		
		function emptyPageEditorSource(){
			var newPage = new common.models.Page();
			newPage.objectId = $("#website-info").data("sitePlaceHolder").webSiteId ;
			setPageEditorSource(newPage);
		}

		function setPageEditorSource(source){
			var renderToString = "webpage-editor";				
			var renderTo = $("#"+ renderToString);						
			if( !renderTo.data("pagePlaceHolder") ){
				renderTo.data("pagePlaceHolder", new common.models.Page() );
				kendo.bind(renderTo, renderTo.data("pagePlaceHolder"));				
			}
			source.copy(renderTo.data("pagePlaceHolder"));
		}
				
		function createEditor( renderToString ){			
			var renderTo = $("#"+ renderToString);						
			if( !renderTo.data("pagePlaceHolder") ){
				var newPage = new common.models.Page();
				newPage.objectId = $("#website-info").data("sitePlaceHolder").webSiteId ;
				renderTo.data("pagePlaceHolder", newPage );
			}			
			var pagePlaceHolder = renderTo.data("pagePlaceHolder");			
			var bodyEditor =  $("#"+ renderToString +"-body" );			
			if(!bodyEditor.data("kendoEditor") ){								
				var pageEditorModel = kendo.observable({ 
					page : pagePlaceHolder,
					isVisible: true,
					isNew : function(){
						if( this.page.pageId <= 0 )
							return true;
						else 
							return false;	
					},
					doPublish: function(e){
						alert("publish");
					},
					showProps: function(e){
						alert("publish");
					},					
					doSave : function (e) {
						common.api.callback(  
						{
							url :"${request.contextPath}/secure/update-website-page.do?output=json", 
							data : { targetSiteId:  this.page.objectId, item: kendo.stringify(this.page) },
							success : function(response){
								common.ui.notification({title:"페이지 저장", message: "웹 페이지가 정상적으로 저장되었습니다.", type: "success" });
								var pageToUse = new common.models.Page(response.targetPage);																
								pageToUse.copy( pagePlaceHolder );
							},
							fail: function(){								
								common.ui.notification({title:"페이지 저장 오류", message: "시스템 운영자에게 문의하여 주십시오." });
								var newPage = new common.models.Page();
								newPage.copy(pagePlaceHolder);
							},
							requestStart : function(){
								kendo.ui.progress(renderTo, true);
							},
							requestEnd : function(){
								kendo.ui.progress(renderTo, false);
							}
						}); 							
					},
					doPreview : function (e) {
						alert("preview");
					}
				});				
				pageEditorModel.bind("change", function(e){
					//alert( e.field  + "   " + e.field.match('^page.'));					
					if( e.field.match('^page.')){ 						
						if( this.page.title.length > 0 && this.page.bodyText.length  > 0 )					
							$("button.btn-editor-control-group[data-action='page-editor-save']").removeAttr('disabled');
					}	
				});								
				kendo.bind(renderTo, pageEditorModel );													
				var imageBroswer = createPageImageBroswer( renderToString + "-imagebroswer", bodyEditor);				
				var linkPopup = createPageLinkPopup(renderToString + "-linkpopup", bodyEditor);	
				bodyEditor.kendoEditor({
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
				var htmleditor = ace.edit("htmleditor");
				htmleditor.getSession().setMode("ace/mode/html");
				htmleditor.getSession().setUseWrapMode(true);				
			}
					
		}	
				
		function createPageImageBroswer(renderToString, editor ){			
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
		
			
		</script>
		<style type="text/css" media="screen">

		.k-grid-content{
			height:200px;
		}			
		#htmleditor { 
			height: 400px;
			border: 1px solid #DDD;
			margin: 4px;
			border-radius: 4px;
			border-bottom-right-radius: 0px;
			margin-top: 5px;
		}
		
		table.k-editor{
			border : 0px;
			height : 400px;
		}
		#website-page-grid.k-grid td{
			overflow : visible;
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
						<div id="page-list-panel" class="panel-body">
							<div class="page-header page-nounderline-header text-primary" style="min-height: 45px;">
									<h5 >
										<small><i class="fa fa-info"></i> 웹 페이지는 게시 상태에서 보여집니다.</small>
									</h5>
									<div class="pull-right">
										<div class="btn-group">
										<button type="button" class="btn btn-primary btn-control-group btn-sm" data-action="page-create" disabled="disabled"><i class="fa fa-file"></i> 새 페이지</button>
										<button type="button" class="btn btn-primary btn-control-group btn-sm" data-action="page-publish" disabled="disabled"><i class="fa fa-external-link"></i> 게시</button>
										<button type="button" class="btn btn-primary btn-control-group btn-sm" data-action="page-delete" disabled="disabled"><i class="fa fa-trash-o"></i> 삭제</button>
										</div>										
									</div>
							</div>		
							<div id="website-page-grid"></div>																		
						</div>					
						<div  id="page-editor-panel" class="panel-body" style="padding:5px; display:none;">	
							<div  id="webpage-editor" class="container">
								<div class="row">
									<div class="col-lg-12">
										<div class="page-header page-nounderline-header text-primary" style="min-height: 45px;">
											<h5 >
												<small><i class="fa fa-info"></i> 웹 페이지는 게시 상태에서 보여집니다.</small>
											</h5>
											<div class="pull-right">
												<div class="btn-group">
													<button type="button" class="btn btn-primary btn-sm btn-editor-control-group" data-bind="click: onPublish, disabled: isNew">게시</button>
													<button type="button" class="btn btn-primary btn-sm btn-editor-control-group" data-action="page-editor-save" data-bind="click: doSave" disabled="disabled">저장</button>
													<button type="button" class="btn btn-primary btn-sm btn-editor-control-group" data-bind="click: showProps, disabled: isNew, invisible:isNew ">프로퍼티</button>
													<button type="button" class="btn btn-primary btn-sm btn-editor-control-group" data-bind="click: onPreview, disabled: isNew" >미리보기</button>													
												</div>						
												<button type="button" class="btn btn-primary btn-control-group btn-sm" data-action="page-editor-close" disabled="disabled">&times;  닫기</button>
											</div>
										</div>														
									</div>
								</div>						
								<div class="panel panel-default">
									<div class="panel-heading" style="padding:5px;">
										<input type="text" class="form-control" placeholder="페이지 제목" data-bind="value: page.title">
									</div>
									<div class="panel-body" style="padding:5px;">
										<div class="row">
											<div class="col-sm-6">
												<p><span class="label label-danger">버전</span><code><span data-bind="text: page.versionId">0</span></code></p>
												<p class="text-muted"><span class="label label-info">템플릿</span>  <small>페이지에 적용할 템플릿 파일 경로를 입력하세요</small></p>
												<input type="text" class="form-control" placeholder="템플릿 파일" value="/html/community/page.ftl">	
													
											</div>
											<div class="col-sm-6">												
												<p class="text-muted"><span class="label label-info">요약</span>  <small>페이지를 간략하게 설명하세요. </small></p>
												<textarea class="form-control" rows="3" data-bind="value: page.summary" placeholder="페이지 요약"></textarea>
											</div>
										</div>
										<textarea id="webpage-editor-body" rows="5" cols="30" data-bind="value: page.bodyText" placeholder="페이지 본문"></textarea>
									</div>
									<div class="panel-body">
										<div id="htmleditor"  data-text="value: page.bodyText" ></div>
									</div>
								</div>	
							</div>			
						</div>
						<div class="panel-body" style="padding:5px;" >
							
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
		<script id="webpage-name-template" type="text/x-kendo-template">	
			<div class="btn-group">
			  <button type="button" class="btn btn-success btn-sm dropdown-toggle" data-toggle="dropdown" style="z-index:2000;">
			    #= name # <span class="caret"></span>
			  </button>
			  <ul class="dropdown-menu" role="menu">
			    <li><a href="\\#" onclick="doPageEdit(); return false;">편집</a></li>
			    <li><a href="\\#">보기</a></li>
			    <li><a href="\\#" onclick="doPageDelete(); return false;">삭제</a></li>
			  </ul>
			</div>
		</script>			
		<#include "/html/common/common-system-templates.ftl" >		
		<#include "/html/common/common-editor-templates.ftl" >	
	</body>
</html>
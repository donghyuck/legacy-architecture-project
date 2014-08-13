<#ftl encoding="UTF-8"/>
<html decorator="secure">
<head>
		<title>관리자 메인</title>		
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/common.admin/pixel/pixel.admin.style.css" />
		<script type="text/javascript">
		<!--		
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.1.0/font-awesome.min.css',
			'css!${request.contextPath}/styles/common.plugins/animate.css',
			'css!${request.contextPath}/styles/common.admin/pixel/pixel.admin.widgets.css',			
			'css!${request.contextPath}/styles/common.admin/pixel/pixel.admin.rtl.css',
			'css!${request.contextPath}/styles/common.admin/pixel/pixel.admin.themes.css',
			'css!${request.contextPath}/styles/common.admin/pixel/pixel.admin.pages.css',	
			'css!${request.contextPath}/styles/perfect-scrollbar/perfect-scrollbar-0.4.9.min.css',
			'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',
			
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo.extension/kendo.ko_KR.js',
			'${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js',
			
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',			
			
			'${request.contextPath}/js/bootstrap/3.0.3/bootstrap.min.js',			
			
			'${request.contextPath}/js/common.plugins/fastclick.js', 
			'${request.contextPath}/js/common.plugins/jquery.slimscroll.min.js', 
			'${request.contextPath}/js/perfect-scrollbar/perfect-scrollbar-0.4.9.min.js', 
			
			'${request.contextPath}/js/common.admin/pixel.admin.min.js',
			
			'${request.contextPath}/js/common/common.models.js',       	    
			'${request.contextPath}/js/common/common.api.js',
			'${request.contextPath}/js/common/common.ui.js',
			'${request.contextPath}/js/common/common.ui.admin.js',
			
			'${request.contextPath}/js/ace/ace.js'
			],
			complete: function() {
				// 1-1.  한글 지원을 위한 로케일 설정
				common.api.culture();
				// 1-2.  페이지 렌딩
				common.ui.landing();				
				// 1-3.  관리자  로딩
				var currentUser = new User();
				
				var targetCompany = new Company();	
				
				common.ui.admin.setup({
					authenticate: function(e){
						e.token.copy(currentUser);
					},
					companyChanged: function(item){
						item.copy(targetCompany);
					},
					switcherChanged: function( name , value ){						
						if( value && !$('#company-list').is(":visible") ){
							$('#company-list').show();
						}else if ( !value && $('#company-list').is(":visible") && $('#company-details').is(":visible") ){
							hideCompanyDetails();
						}
					}
				});
				common.ui.handleButtonActionEvents(
					$("button.btn-control-group"), 
					{event: 'click', handlers: {
						'create-company' : function(e){
							$("#company-grid").data('kendoGrid').addRow();			
						}, 	
						user : function(e){
							topBar.go('main-user.do');								
						}, 	
						group : function(e){
							topBar.go('main-group.do');							
						}, 							
						menu : function(e){
							showMenuWindow();
						},
						role : function(e){					
							showRoleWindow();			
						}  						 
					}}
				);

				var company_grid = $("#company-grid").kendoGrid({
					dataSource: {	
						transport: { 
							read: { url:'${request.contextPath}/secure/list-company.do?output=json', type: 'POST' },
							create: { url:'${request.contextPath}/secure/create-company.do?output=json', type:'POST' },             
							update: { url:'${request.contextPath}/secure/update-company.do?output=json', type:'POST' },
							parameterMap: function (options, operation){	          
								if (operation != "read" && options) {
									return { companyId: options.companyId, item: kendo.stringify(options)};
								}else{
									return { startIndex: options.skip, pageSize: options.pageSize }
								}
							}
						},
						schema: {
							total: "totalCompanyCount",
							data: "companies",
							model : Company
						},
						pageSize: 15,
						serverPaging: true,
						serverFiltering: false,
						serverSorting: false,                        
						error: common.api.handleKendoAjaxError
					},
					columns: [
						{ field: "companyId", title: "ID", width:40,  filterable: false, sortable: false }, 
						{ field: "name",    title: "KEY",  filterable: true, sortable: true,  width: 80 }, 
						{ field: "displayName",   title: "이름",  filterable: true, sortable: true,  width: 100 }, 
						{ field: "domainName",   title: "도메인",  filterable: true, sortable: false,  width: 100 }, 
						{ field: "description", title: "설명", width: 200, filterable: false, sortable: false },
						{ command: [ { text:"상세보기", click: showCompanyDetails }, { name:"edit",  text: { edit: "수정", update: "저장", cancel: "취소"}  }  ], title: "&nbsp;", width: 180  }], 
					filterable: true,
					editable: "inline",
					selectable: 'row',
					height: '350',
					batch: false,              
					pageable: { refresh:true, pageSizes:false,  messages: { display: ' {1} / {2}' }  },					
					change: function(e) {
						// 1-1 SELECTED EVENT  
						var selectedCells = this.select();
						if( selectedCells.length === 0){								
						}
					},
					cancel: function(e){	
					
					},
					edit: function(e){	
						hideCompanyDetails()		
					},
					dataBound: function(e){   
						hideCompanyDetails(); 
					}	                    
				}); //.css("border", 0);
																			
				// END SCRIPT
			}
		}]);
		
		
		function showMenuWindow(){
			var renderToString = "menu-modal";
			var renderTo = $( '#' + renderToString );
			if( renderTo.length === 0 ){		
				$("#main-wrapper").append( kendo.template($('#menu-modal-template').html()) );				
				renderTo = $('#' + renderToString );
				renderTo.modal({
					backdrop: 'static'
				});				
				renderTo.on('hidden.bs.modal', function(e){
					closeMenuEditor();
				});
				renderTo.on('show.bs.modal', function(e){				
					if(! $("#menu-grid").data("kendoGrid")){				
						$('#menu-grid').kendoGrid({
							dataSource: {
								transport: { 
									read: { url:'${request.contextPath}/secure/list-menu.do?output=json', type:'post' }
								},
								batch: false, 
								schema: {
									total: "totalMenuCount",
									data: "targetMenus",
									model: Menu
								},
								pageSize: 15,
								serverPaging: true,
								serverFiltering: false,
								serverSorting: false,  
								error:common.api.handleKendoAjaxError
							},
							columns: [
								{ title: "ID", field: "menuId",  width:50 },
								{ title: "이름", field: "name", template:'#:name#  <a href="\\#" onclick="openMenuEditor(); return false;" class="btn btn-info btn-xs">메뉴 편집</a>'  }
							],
							pageable: { refresh:true, pageSizes:false,  messages: { display: ' {1} / {2}' }  },					
							resizable: true,
							editable : false,
							selectable : "row",
							scrollable: true,
							autoBind: false,
							change: function(e) {
								var selectedCells = this.select();
								if( selectedCells.length == 1){ 
									var selectedCell = this.dataItem( selectedCells );											 	
								}
							},
							dataBound: function(e){
							}
						});	
						renderTo.find('button[data-action="create-menu"]').click(function(e){				
							$("#menu-grid").data("kendoGrid").addRow();
							$("#menu-grid").data("kendoGrid").select("tr.k-grid-edit-row:first")
							openMenuEditor();
						});	
						renderTo.find('button[data-action="saveOrUpdate"]').click(function(e){
							alert("save");
						});
					}				
					$("#menu-grid").data("kendoGrid").dataSource.read();
				});
			}
			renderTo.modal('show');	
		} 

		function getSelectedMenu(){
			
			var renderTo = $("#menu-grid");
			var grid = renderTo.data('kendoGrid');			
			var selectedCells = grid.select();
			
			if( selectedCells.length == 0){
				return new Menu();
			}else{			
				var selectedCell = grid.dataItem( selectedCells );   
				return selectedCell;
			}
		}	
				
		function openMenuEditor(){
			var menuPlaceHolder = getSelectedMenu();		
			var renderTo = $("#menu-editor");
			var editor = ace.edit("xml-editor");			
			
			if( !renderTo.data("model"))
			{
				var  editorModel = kendo.observable({
					menu : new Menu()
				});					
				kendo.bind(renderTo, editorModel);
				renderTo.data("model", editorModel );					
				editor.setTheme("ace/theme/monokai");
				editor.getSession().setMode("ace/mode/xml");		
						
				$('#menu-editor button[data-action="editor-close"]').click(function(e){
					closeMenuEditor();
				});
				
				var switcher = $('#menu-editor input[role="switcher"][name="warp-switcher"]');
				
				if( switcher.length > 0 ){
					$(switcher).switcher();
					$(switcher).change(function(){
						editor.getSession().setUseWrapMode($(this).is(":checked"));
					});		
				}				
			}						
			menuPlaceHolder.copy( renderTo.data("model").menu );	
			editor.setValue(renderTo.data("model").menu.menuData);
			
			$('#menu-modal button[data-action="saveOrUpdate"]').removeClass("hidden");
			$('#menu-modal .modal-body:first').slideUp("slow", function(){
				$('.menu-editor-group:hidden').show();
			});
			
			/*
			
			common.ui.animate($('#menu-modal .modal-body:first'), 'fadeOutUp', function(){
				$('#menu-modal .modal-body:first').addClass("hidden");				
				$('.menu-editor-group[class~="hidden"]').removeClass('hidden');
			});
			*/
		}
		
		function closeMenuEditor(){
			if(getSelectedMenu().menuId < 0 ){
				$("#menu-grid").data("kendoGrid").removeRow("tr.k-grid-edit-row");
			}
			$('#menu-modal button[data-action="saveOrUpdate"]').addClass("hidden");
			
			$("#menu-editor").slideUp("slow", function(){
				$('#menu-editor .modal-body.menu-editor-group').hide();
				$('#menu-modal .modal-body:first.hidden').show();
			});
			common.ui.animate($("#menu-editor"), "fadeOutUp", function(){				
				
			});
		}
		
		
		function showRoleWindow(){
			var renderToString = "role-modal";
			var renderTo = $( '#' + renderToString );
			if( renderTo.length === 0 ){		
				$("#main-wrapper").append( kendo.template($('#role-modal-template').html()) );				
				renderTo = $('#' + renderToString );
				renderTo.modal({
					backdrop: 'static'
				});
				renderTo.on('show.bs.modal', function(e){
					if( ! $('#role-grid').data("kendoGrid")){
						// ROLE GRID 생성
						$('#role-grid').kendoGrid({
							dataSource: {
								transport: { 
									read: { url:'${request.contextPath}/secure/list-role.do?output=json', type:'post' }
								},						
								batch: false, 
								schema: {
									data: "roles",
									model: Role
								},
								error:common.api.handleKendoAjaxError
							},
							columns: [
								{ title: "ID", field: "roleId",  width:40 },
								{ title: "롤", field: "name" },
								{ title: "설명",   field: "description" }
							],
							pageable: false,
							resizable: true,
							editable : false,
							scrollable: true,
							/*height: 300,*/
							change: function(e) {
							}
						});		
					}
				});
			}
			renderTo.modal('show');
		}
		
		function getSelectedCompany(){
			var renderTo = $("#company-grid");
			var grid = renderTo.data('kendoGrid');
			var selectedCells = grid.select();
			var selectedCell = grid.dataItem( selectedCells );   
			return selectedCell;
		}
		
		function getCompanyDetailsModel () {
			var renderTo = $('#company-details');
			var model = renderTo.data("model");
			return model;
		}
		
		function hideCompanyDetails(){			
			if( $("#company-details").text().length > 0 && $("#company-details").is(":visible") ){
				var alwaysShowList = common.ui.admin.switcherEnabled("list-switcher");
				var animate = "slideOutLeft" ;
				if( alwaysShowList ){
					animate = "fadeOutUp" ;
				}
				common.ui.animate_v3($("#company-details"), animate, function(){  
					if( !$("#company-list").is(":visible") ){
						$("#company-list").show();
						//common.ui.animate_v3($("#company-list"), "slideInRight").show();
						common.ui.animateFadeIn($("#company-list"));
					} 
				});	
			}	
		}
		
		function showCompanyDetails(){
			var renderTo = $('#company-details');
			var companyPlaceHolder = getSelectedCompany();
			var alwaysShowList = common.ui.admin.switcherEnabled("list-switcher");			
			
			if( renderTo.text().length === 0 ){
				renderTo.html(kendo.template($('#company-details-template').html()));
				var detailsModel = kendo.observable({
					company : new Company(),
					logoUrl : "",
					memberCount : 0 ,
					groupCount : 0 
				});					
				detailsModel.bind("change", function(e){		
					if( e.field.match('^company.name')){ 						
						var sender = e.sender ;
						if( sender.company.companyId > 0 ){
							this.set("logoUrl", "/download/logo/company/" + sender.company.name );
							this.set("formattedCreationDate", kendo.format("{0:yyyy.MM.dd}",  sender.company.creationDate ));      
							this.set("formattedModifiedDate", kendo.format("{0:yyyy.MM.dd}",  sender.company.modifiedDate ));
						}
					}	
				});
				kendo.bind(renderTo, detailsModel );	
				renderTo.data("model", detailsModel );		
				$('#myTab').on( 'show.bs.tab', function (e) {		
					var show_bs_tab = $(e.target);
					switch( show_bs_tab.attr('href') ){
						case "#props" :
							createCompanyPropsPane($("#company-prop-grid"));
							break;
						case  '#users' :
							createCompanyMembersPane($('#company-user-grid'));
							break;
						case  '#groups' :	
							createCompanyGroupsPane($('#company-group-grid'));
							break;
					}	
				});
				renderTo.find(".panel-heading > button.close").click(function(e){
					hideCompanyDetails();
				});
			}
			companyPlaceHolder.copy( renderTo.data("model").company );
			
			$('#myTab a:first').tab('show');			
			if(alwaysShowList){				
				if(!renderTo.is(':visible'))
					common.ui.animate_v3(renderTo, 'fadeInDown').show() ;
				$('html,body').animate({scrollTop: renderTo.offset().top - 20 }, 500);	
			}else{			
			
				common.ui.animateFadeOut($("#company-list"), function(){
					common.ui.animate_v3(renderTo, 'slideInLeft').show() ;
				});
				
				
				//common.ui.animate_v3($("#company-list"), 'slideOutLeft', function(){
					//common.ui.animate_v3(renderTo, 'slideInLeft').show() ;
					//renderTo.show();
				//}) ;
			}			
			return false;
		}
		
		function createCompanyPropsPane(renderTo){
			var companyPlaceHolder = getSelectedCompany();
			if( ! renderTo.data("kendoGrid") ){
				renderTo.kendoGrid({
					dataSource: {
						transport: { 
							read: { url:'${request.contextPath}/secure/get-company-property.do?output=json', type:'post' },
							create: { url:'${request.contextPath}/secure/update-company-property.do?output=json', type:'post' },
							update: { url:'${request.contextPath}/secure/update-company-property.do?output=json', type:'post'  },
							destroy: { url:'${request.contextPath}/secure/delete-company-property.do?output=json', type:'post' },
							parameterMap: function (options, operation){			
								if (operation !== "read" && options.models) {
									return { companyId: getSelectedCompany().companyId, items: kendo.stringify(options.models)};
								} 
								return { companyId: getSelectedCompany().companyId }
							}
						},						
						batch: true, 
						schema: {
							data: "targetCompanyProperty",
							model: Property
						},
						error:common.api.handleKendoAjaxError
					},
					columns: [
						{ title: "속성", field: "name", width: 250 },
						{ title: "값",   field: "value" },
						{ command:  { name: "destroy", text:"삭제" },  title: "&nbsp;", width: 100 }
					],
					pageable: false,
					resizable: true,
					editable : true,
					scrollable: true,
					autoBind: false,
					height: 300,
					toolbar: [
						{ name: "create", text: "추가" },
						{ name: "save", text: "저장" },
						{ name: "cancel", text: "취소" }
					],				     
					change: function(e) {
					}
				});		
			}
			renderTo.data("kendoGrid").dataSource.read();
		}
		
		function createCompanyMembersPane(renderTo){
			var companyPlaceHolder = getSelectedCompany();
			if( ! renderTo.data("kendoGrid") ){	
				renderTo.kendoGrid({
					dataSource: {
						type: "json",
						transport: { 
							read: { url:'${request.contextPath}/secure/list-user.do?output=json', type: 'POST' },
							parameterMap: function (options, type){
								return { startIndex: options.skip, pageSize: options.pageSize,  companyId: getSelectedCompany().companyId }
							}
						},
						schema: {
							total: "totalUserCount",
							data: "users",
							model: User
						},
						error:common.api.handleKendoAjaxError,
						batch: false,
						pageSize: 10,
						serverPaging: true,
						serverFiltering: false,
						serverSorting: false 
					},
					height: 300,
					filterable: true,
					sortable: true,
					scrollable: true,
					autoBind: false,
					pageable: { refresh:true, pageSizes:false,  messages: { display: ' {1} / {2}' }  },
					selectable: "multiple, row",
					columns: [
						{ field: "userId", title: "ID", width:50,  filterable: false, sortable: false }, 
						{ field: "username", title: "아이디", width: 100 }, 
						{ field: "name", title: "이름", width: 100 }, 
						{ field: "email", title: "메일" },
						{ field: "creationDate", title: "생성일", filterable: false,  width: 100, format: "{0:yyyy/MM/dd}" } ],
					dataBound:function(e){
						getCompanyDetailsModel().set("memberCount", this.dataSource.total() );
					},
					toolbar: [{ name: "create-groups", text: "선택 사용자 소속 변경하기", imageClass:"k-icon k-i-folder-up" , className: "changeUserCompanyCustomClass" }]
				});												
			}	
			renderTo.data("kendoGrid").dataSource.read();
		}	
		
		function createCompanyGroupsPane(renderTo){
			var companyPlaceHolder = getSelectedCompany();
			if( ! renderTo.data("kendoGrid") ){	
					renderTo.kendoGrid({
						dataSource: {
							type: "json",
							transport: {
								read: { url:'${request.contextPath}/secure/list-company-group.do?output=json', type:'post' },
								destroy: { url:'${request.contextPath}/secure/remove-group-members.do?output=json', type:'post' },
								parameterMap: function (options, operation){
									if (operation !== "read" && options.models) {
										return { companyId: getSelectedCompany().companyId, items: kendo.stringify(options.models)};
									}
									return { companyId: getSelectedCompany().companyId }
								}
							},
							schema: {
								data: "companyGroups",
								model: Group
							},
							error:common.api.handleKendoAjaxError
						},
						height: 300,
						scrollable: true,
						editable: false,
						autoBind: false,
						columns: [
							{ field: "groupId", title: "ID", width:40,  filterable: false, sortable: false }, 
							{ field: "name",    title: "KEY",  filterable: true, sortable: true,  width: 100 },
							{ field: "displayName",    title: "그룹",  filterable: true, sortable: true,  width: 100 },
							{ field: "description",    title: "설명",  filterable: false,  sortable: false },
							{ field:"memberCount", title: "멤버", filterable: false,  sortable: false, width:50 }
						],
						dataBound:function(e){
							getCompanyDetailsModel().set("groupCount", this.dataSource.total() );
						},
						toolbar: [{ name: "create-groups", text: "디폴트 그룹 생성하기", imageClass:"k-icon k-i-folder-add" , className: "createGroupsCustomClass" }]
				});		
			}
			renderTo.data("kendoGrid").dataSource.read();
		}				
		
		-->
		</script> 		 
		<style>
			.k-grid-content {
				min-height:150px;
			}
			
			#menu-grid .k-grid-content {
				min-height:250px;
			}
			
			#xml-editor{
				position: absolute;
				top: 0;
				right: 0;
				bottom: 0;
				left: 0;
				min-height:400px;
			}
		</style>
	</head>
	<body class="theme-default main-menu-animated">
		<div id="main-wrapper">
			<#include "/html/common/common-system-navigation.ftl" >	
			<div id="content-wrapper">
				<#assign selectedMenu = WebSiteUtils.getMenuComponent("SYSTEM_MENU", "MENU_1_1") />
				<ul class="breadcrumb breadcrumb-page">
					<!--<div class="breadcrumb-label text-light-gray">You are here: </div>-->
					<li><a href="#">Home</a></li>
					<li><a href="${ selectedMenu.parent.page!"#" }">${selectedMenu.parent.title}</a></li>
					<li class="active"><a href="#">${selectedMenu.title}</a></li>
				</ul>
				<div class="page-header bg-dark-gray">		
					<div class="row">
						<h1 class="col-xs-12 col-sm-6 text-center text-left-sm"><#if selectedMenu.isSetIcon() ><i class="fa ${selectedMenu.icon} page-header-icon"></i></#if> ${selectedMenu.title}
							<p><small><i class="fa fa-quote-left"></i> ${selectedMenu.description} <i class="fa fa-quote-right"></i></small></p>
						</h1>
						<div class="col-xs-12 col-sm-6">
							<div class="row">
								<hr class="visible-xs no-grid-gutter-h">							
								<div class="pull-right col-xs-12 col-sm-auto">
									<h6 class="text-light-gray text-semibold text-xs" style="margin:20px 0 10px 0;">옵션</h6>
									<div class="btn-group">
										<button type="button" class="btn btn-primary btn-sm btn-control-group" data-action="menu"><i class="btn-label icon fa fa-sitemap"></i> 메뉴</button>
										<button type="button" class="btn btn-primary btn-sm btn-control-group" data-action="role"><i class="btn-label icon fa fa-lock"></i> 권한 & 롤</button>
									</div>									
								</div>
							</div>
						</div>
					</div>				
				</div><!-- / .page-header -->
				<div class="row">				
					<div class="col-sm-12">					
						<!-- details -->
						<div id="company-list" class="panel panel-default" style="min-height:300px;">
							<div class="panel-heading">
								<span class="panel-title"><i class="fa fa-align-justify"></i> 목록</span>
								<div class="panel-heading-controls">								
								<button class="btn btn-danger btn-labeled btn-control-group" data-action="create-company"><span class="btn-label icon fa fa-plus"></span> 회사 만들기 </button>
								</div>
							</div>
							<div id="company-grid" class="no-border"></div>	
						</div>
						<!-- /details -->
						<!-- list -->
						<div id="company-details" class="page-details" style="display:none;"></div><!-- /company details -->		
						<!-- /list -->
					</div>	
				</div>				
			</div> <!-- / #content-wrapper -->
			<div id="main-menu-bg">
			</div>
		</div> <!-- / #main-wrapper -->
		
		<script type="text/x-kendo-template" id="menu-modal-template">
		<div class="modal fade" id="menu-modal" tabindex="-1" role="dialog" aria-labelledby=".modal-title" aria-hidden="true">
			<div class="modal-dialog modal-lg animated slideDown">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title">메뉴</h4>
					</div>
					<div class="modal-body no-padding">
						<div class="padding-sm">
							<button class="btn btn-danger btn-flat btn-labeled" data-action="create-menu"><span class="btn-label icon fa fa-plus"></span> <small>새로운 메뉴 만들기</small></button>
						</div>
						<div id="menu-grid" class="no-border-hr no-border-b"></div>
					</div>					
					<div id="menu-editor" style="display:none;" class="modal-body no-padding">
						<div class="">
							<form class="form-horizontal">	
									<div class="row no-margin">
										<div class="col-sm-6"><button class="btn btn-primary btn-flat btn-labeled" data-action="editor-close" style="margin-top:15px;"><span class="btn-label icon fa fa-arrow-left"></span> <small>목록 보기</small></button>	</div>
										<div class="col-sm-3"></div>
										<div class="col-sm-3">
											<h6 class="text-light-gray text-semibold text-xs">줄바꿈 설정/해지</h6>
											<input type="checkbox" name="warp-switcher" data-class="switcher-primary" role="switcher" >	
										</div>
									</div>			
									<div class="row no-margin">
										<div class="col-sm-6">
											<div class="form-group no-margin-hr">
												<input type="text" name="name" class="form-control input-sm" placeholder="이름" data-bind="value: menu.name">
											</div>
										</div><!-- col-sm-6 -->
										<div class="col-sm-6">
											<div class="form-group no-margin-hr">
												<input type="text" name="title" class="form-control input-sm" placeholder="타이틀" data-bind="value: menu.title">
											</div>
										</div><!-- col-sm-6 -->
									</div>
									<div class="row no-margin">
										<div class="col-sm-12">
											<input type="text" name="description" class="form-control input-sm" placeholder="설명"  data-bind="value:menu.description" />
										</div>
									</div>				
							</form>																									
						</div>
						<div id="xml-editor" style="height:400px; position:relative;"></div>	
					</div>
					<div class="modal-footer">					
						<button type="button" class="btn btn-default btn-flat" data-dismiss="modal">닫기</button>
						<button type="button" class="btn btn-primary btn-flat disable hidden" data-action="saveOrUpdate">저장</button>
					</div>
				</div>
			</div>
		</div>				
		</script>
		
		<script type="text/x-kendo-template" id="role-modal-template">
		<div class="modal fade" id="role-modal" tabindex="-1" role="dialog" aria-labelledby=".modal-title" aria-hidden="true">
			<div class="modal-dialog animated swing">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title">권한 & 롤</h4>
					</div>
					<div class="modal-body">
						<div id="role-grid"></div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default btn-flat" data-dismiss="modal">닫기</button>
						<!--<button type="button" class="btn btn-primary">Save changes</button>-->
					</div>
				</div>
			</div>
		</div>				
		</script>		
		<script type="text/x-kendo-template" id="company-details-template">		
		<div class="panel">
			<div class="panel-heading">
				<span class="panel-title"><span class="label label-primary" data-bind="text: company.name"></span> <span class="text-semibold" data-bind="text:company.displayName"></span></span>
				<button type="button" class="close" aria-hidden="true">&times;</button>
			</div>			
			<div class="panel-body">
					<div class="details-row no-margin-t">					
						<div class="left-col">
							<div class="details-block no-margin-t">
								<div class="details-photo">
									<img data-bind="attr: { src: logoUrl }" alt="">
								</div>
								<br>
								<!--
								<a href="\\#" class="btn btn-success"><i class="fa fa-check"></i> Following</a> 
								<a href="\\#" class="btn"><i class="fa fa-comment"></i></a>-->
							</div>				
							<div class="panel panel-transparent">
								<div class="panel-heading">
									<span class="panel-title"  data-bind="text:company.description"></span>
									
								</div>
								<table class="table">
									<tbody>						
										<tr>
											<th><small>도메인</small></th>								
											<td><span data-bind="text:company.domainName"></span></td>
										</tr>	
										<tr>
											<th><small>생성일</small></th>								
											<td><span data-bind="text:formattedCreationDate"></span></td>
										</tr>	
										<tr>
											<th><small>수정일</small></th>								
											<td><span data-bind="text:formattedModifiedDate"></span></td>
										</tr>														
									</tbody>
								</table>
							</div>
						</div>
						<div class="right-col">
							<hr class="details-content-hr no-grid-gutter-h">	
							<div class="details-content">
								<ul id="myTab" class="nav nav-tabs nav-tabs-sm">
									<li><a href="\\#props" data-toggle="tab">프로퍼티</a></li>
									<li><a href="\\#groups" data-toggle="tab">그룹 <span class="badge badge-success" data-bind="text:groupCount, visible:groupCount ">0</span></a></li>
									<li><a href="\\#users" data-toggle="tab">사용자 <span class="badge badge-success" data-bind="text:memberCount, visible:memberCount">0</span></a></li>
								</ul>	
								<!-- .tab-content -->	
								<div class="tab-content tab-content-bordered no-padding">								
									<div class="tab-pane fade" id="props">				
										<div id="company-prop-grid" class="props no-border"></div>
									</div>
									<div class="tab-pane fade" id="groups">										
										<div id="company-group-grid"  class="groups no-border"></div>					
									</div>
									<div class="tab-pane fade" id="users">	
										<div id="company-user-grid"  class="users no-border"></div>
									</div>
								</div><!-- / .tab-content -->
							</div><!-- / .details-content -->
						</div><!-- / .right-col -->
					</div><!-- / .details-row -->	
			</div>
		</div>			
		</script>				
		<#include "/html/common/common-system-templates.ftl" >			
	</body>    
</html>
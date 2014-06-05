<#ftl encoding="UTF-8"/>
<html decorator="secure">
<head>
		<title>관리자 메인</title>		
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/common/common.admin.style.css" />
		<script type="text/javascript">
		<!--		
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.0.3/font-awesome.min.css',
			'css!${request.contextPath}/styles/common.extension/animate.css',
			'css!${request.contextPath}/styles/common/common.admin.widgets.css',			
			'css!${request.contextPath}/styles/common/common.admin.rtl.css',			
			'css!${request.contextPath}/styles/common/common.admin.themes.css',
			'css!${request.contextPath}/styles/common/common.admin.page.css',	
			/*'${request.contextPath}/js/jquery/2.1.1/jquery-2.1.1.min.js',*/
			'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo.extension/kendo.ko_KR.js',
			'${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',			
			'${request.contextPath}/js/bootstrap/3.0.3/bootstrap.min.js',			
			'${request.contextPath}/js/common.plugins/fastclick.js', 
			'${request.contextPath}/js/common.plugins/jquery.slimscroll.min.js', 
			'${request.contextPath}/js/common/common.admin.js',
			'${request.contextPath}/js/common/common.models.js',       	    
			'${request.contextPath}/js/common/common.api.js',
			'${request.contextPath}/js/common/common.ui.js',
			'${request.contextPath}/js/common/common.ui.admin.js'
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
					authenticate : function(e){
						e.token.copy(currentUser);
					},
					companyChanged: function(item){
						item.copy(targetCompany);
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
						{ command: [ {name:"edit",  text: { edit: "수정", update: "저장", cancel: "취소"}  }  ], title: "&nbsp;", width: 180  }], 
					filterable: true,
					editable: "inline",
					selectable: 'row',
					height: '100%',
					batch: false,              
					pageable: { refresh:true, pageSizes:false,  messages: { display: ' {1} / {2}' }  },					
					change: function(e) {
						// 1-1 SELECTED EVENT  
						var selectedCells = this.select();
						if( selectedCells.length > 0){
							// 1-1-1 선택된 로의 Company 정보로 selectedCompany 객체를 설정한다.
							var selectedCell = this.dataItem( selectedCells );	     
							if( selectedCell.companyId > 0 && selectedCell.companyId != selectedCompany.companyId ){
								selectedCompany.companyId = selectedCell.companyId;
								selectedCompany.name = selectedCell.name;
								selectedCompany.displayName = selectedCell.displayName;
								selectedCompany.domainName = selectedCell.domainName;
								selectedCompany.description = selectedCell.description;
								selectedCompany.modifiedDate = selectedCell.modifiedDate;
								selectedCompany.creationDate = selectedCell.creationDate;	                                 
								selectedCompany.formattedCreationDate  =  kendo.format("{0:yyyy.MM.dd}",  selectedCell.creationDate );      
								selectedCompany.formattedModifiedDate =  kendo.format("{0:yyyy.MM.dd}",  selectedCell.modifiedDate );    
								
								// 1-1-2 템플릿을 이용한 상세 정보 출력	
								$('#company-details').show().html(kendo.template($('#company-details-template').html()));	
								kendo.bind( $('#company-details'), selectedCompany );						

								if( ! $('#company-prop-grid').data("kendoGrid") ){													
												$('#company-prop-grid').kendoGrid({
													dataSource: {
														transport: { 
															read: { url:'${request.contextPath}/secure/get-company-property.do?output=json', type:'post' },
															create: { url:'${request.contextPath}/secure/update-company-property.do?output=json', type:'post' },
															update: { url:'${request.contextPath}/secure/update-company-property.do?output=json', type:'post'  },
															destroy: { url:'${request.contextPath}/secure/delete-company-property.do?output=json', type:'post' },
													 		parameterMap: function (options, operation){			
														 		if (operation !== "read" && options.models) {
														 			return { companyId: selectedCompany.companyId, items: kendo.stringify(options.models)};
																} 
																return { companyId: selectedCompany.companyId }
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
														{ title: "속성", field: "name" },
														{ title: "값",   field: "value" },
														{ command:  { name: "destroy", text:"삭제" },  title: "&nbsp;", width: 100 }
													],
													pageable: false,
													resizable: true,
													editable : true,
													scrollable: true,
													//height: 350,
													toolbar: [
														{ name: "create", text: "추가" },
														{ name: "save", text: "저장" },
														{ name: "cancel", text: "취소" }
													],				     
													change: function(e) {
													}
												});		
								}
																		
								$('#myTab a').click(function (e) {
									e.preventDefault(); 
									 if( $(this).attr('href') == '#props' ){	 									 
									 }else if( $(this).attr('href') == '#groups' ){		
										if( ! $('#company-group-grid').data("kendoGrid") ){	
											$('#company-group-grid').kendoGrid({
						   							dataSource: {
														type: "json",
														transport: {
															read: { url:'${request.contextPath}/secure/list-company-group.do?output=json', type:'post' },
															destroy: { url:'${request.contextPath}/secure/remove-group-members.do?output=json', type:'post' },
															parameterMap: function (options, operation){
																if (operation !== "read" && options.models) {
												 	    			return { companyId: selectedCompany.companyId, items: kendo.stringify(options.models)};
										            			}
											            		return { companyId: selectedCompany.companyId }
												   			}
														},
														schema: {
															data: "companyGroups",
															model: Group
														},
														error:common.api.handleKendoAjaxError
													},
													//height: 350,
													scrollable: true,
													editable: false,
													columns: [
														{ field: "groupId", title: "ID", width:40,  filterable: false, sortable: false }, 
														{ field: "name",    title: "KEY",  filterable: true, sortable: true,  width: 100 },
														{ field: "displayName",    title: "그룹",  filterable: true, sortable: true,  width: 100 },
														{ field: "description",    title: "설명",  filterable: false,  sortable: false },
														{ field:"memberCount", title: "멤버", filterable: false,  sortable: false, width:50 }
													],
													dataBound:function(e){},
													toolbar: [{ name: "create-groups", text: "디폴트 그룹 생성하기", imageClass:"k-icon k-i-folder-add" , className: "createGroupsCustomClass" }]
											});		
										}								 
									 }else if( $(this).attr('href') == '#users' ){						
										if( ! $('#company-user-grid').data("kendoGrid") ){	
											$('#company-user-grid').kendoGrid({
								   				dataSource: {
													type: "json",
													transport: { 
														read: { url:'${request.contextPath}/secure/list-user.do?output=json', type: 'POST' },
														parameterMap: function (options, type){
															return { startIndex: options.skip, pageSize: options.pageSize,  companyId: selectedCompany.companyId }
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
												//height: 350,
												filterable: true,
												sortable: true,
												scrollable: true,
												pageable: { refresh:true, pageSizes:false,  messages: { display: ' {1} / {2}' }  },
												selectable: "multiple, row",
												columns: [
													{ field: "userId", title: "ID", width:50,  filterable: false, sortable: false }, 
													{ field: "username", title: "아이디", width: 100 }, 
													{ field: "name", title: "이름", width: 100 }, 
													{ field: "email", title: "메일" },
													{ field: "creationDate", title: "생성일", filterable: false,  width: 100, format: "{0:yyyy/MM/dd}" } ],         
												dataBound:function(e){  },
												toolbar: [{ name: "create-groups", text: "선택 사용자 소속 변경하기", imageClass:"k-icon k-i-folder-up" , className: "changeUserCompanyCustomClass" }]
											});												
										}
									 }
									$(this).tab('show')
								});
							}	
						}
					},
					dataBound: function(e){   
						// 1-2 Company 데이터를 새로 읽어드리면 기존 선택된 정보들과 상세 화면을 클리어 한다. 
						var selectedCells = this.select();				
						if(selectedCells.length == 0 )
						{
							selectedCompany = new Company({});		    
							kendo.bind($(".tabular"), selectedCompany );
							$("#menu").hide(); 	
							$("#company-details").hide(); 	 		
						}   
					}	                    
				}); //.css("border", 0);
				
				// MENU WINDOW
				$('#menu-grid').data("menuPlaceHolder", new Menu() )	;
												
				// END SCRIPT
			}
		}]);
		
		
		function creatCompanyGrid(){
		
		} 
		
		-->
		</script> 		 
		<style>
		.k-grid-content{
			height:300px;
		}
					




				
		</style>
	</head>
	<body class="theme-default main-menu-animated">
		<div id="main-wrapper">
			<#include "/html/common/common-system-navigation.ftl" >	
			<div id="content-wrapper">
				<ul class="breadcrumb breadcrumb-page">
					<div class="breadcrumb-label text-light-gray">You are here: </div>
					<li><a href="#">Home</a></li>
					<li class="active"><a href="#">Dashboard</a></li>
				</ul>
				<div class="page-header bg-dark-gray">				
					<#assign selectedMenu = WebSiteUtils.getMenuComponent("SYSTEM_MENU", "MENU_2_1") />
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
						<div class="panel panel-default" style="min-height:300px;">
							<div class="panel-heading">
								<span class="panel-title"><i class="panel-title-icon fa fa-building-o"></i></span>
								<div class="panel-heading-controls">
									<span class="panel-heading-text"><em>Just some text with <a href="#">link</a></em>&nbsp;&nbsp;<span style="color: #ccc">|</span>&nbsp;&nbsp;</span>
									<button class="btn btn-danger btn-sm btn-control-group" data-action="create-company">회사 만들기 </button>
								</div>
							</div>
							<div id="company-grid" class="no-border"></div>	
						</div>
					</div>	
				</div>
				<div class="row">					
					<div class="col-sm-12">					
						<div id="company-details" style="display:none;"></div>					
					</div>
				</div>
				
				<div calss="page-details">
						<div class="profile-full-name">
			<span class="text-semibold">Denise Steiner</span>'s profile
		</div>
				
<div class="details-row">
			<div class="left-col">
				<div class="details-block">
					<div class="panel details-photo">
						<img src="/download/logo/company/INKIUM" alt="">
					</div><br>
					<a href="#" class="btn btn-success"><i class="fa fa-check"></i>&nbsp;&nbsp;Following</a>&nbsp;&nbsp;
					<a href="#" class="btn"><i class="fa fa-comment"></i></a>
				</div>
				
				<div class="panel panel-transparent">
					<div class="panel-heading">
						<span class="panel-title">About me</span>
					</div>
					<div class="panel-body">
						Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et <a href="#">dolore magna</a> aliqua.
					</div>
				</div>

				

				

				

			</div>
			<div class="right-col">

				<hr class="details-content-hr no-grid-gutter-h">
				
				<div class="details-content">

					<ul id="details-tabs" class="nav nav-tabs">
						<li class="">
							<a href="#details-tabs-board" data-toggle="tab">Board</a>
						</li>
						
						
						<li class="active">
							<a href="#details-tabs-following" data-toggle="tab">Following</a>
						</li>
					</ul>

					<div class="tab-content tab-content-bordered panel-padding">
						 <!-- / .tab-pane -->
						 <!-- / .tab-pane -->
						 <!-- / .tab-pane -->
						 <!-- / .tab-pane -->
					</div> <!-- / .tab-content -->
				</div>
			</div>
		</div>
</div>								
		
			</div> <!-- / #content-wrapper -->
			<div id="main-menu-bg">
			</div>
		</div> <!-- / #main-wrapper -->
		<script type="text/x-kendo-template" id="company-details-template">		
			<a href="\\#" class="header-2">상세정보</a>	
					<ul id="myTab" class="nav nav-tabs nav-tabs-sm">
						<li class="active"><a href="\\#props" data-toggle="tab">프로퍼티</a></li>
						<li><a href="\\#groups" data-toggle="tab">그룹 <span class="label label-success">22</span></a></li>
						<li><a href="\\#users" data-toggle="tab">사용자 <span class="label label-success">1234</span></a></li>
					</ul>						
				</div>
				<div class="tab-content tab-content-bordered no-padding">
					<div class="tab-pane active" id="props">
						<div class="alert alert-danger alert-dark no-border-radius">
							<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
							프로퍼티는 수정 후 저장 버튼을 클릭하여야 최종 반영됩니다.
						</div>						
						<div id="company-prop-grid" class="props no-border"></div>
					</div>
					<div class="tab-pane" id="groups">					
						<div class="alert alert-info alert-dark no-border-radius">
							<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
							그룹관리는  그룹관리를 사용하여 관리 하실수 있습니다.	     
						</div>						
						<div id="company-group-grid"  class="groups no-border"></div>					
					</div>
					<div class="tab-pane" id="users">
						<div class="alert alert-info alert-dark no-border-radius">
							<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
							사용자관리는 사용자관리를 사용하여 관리 하실수 있습니다.	     
						</div>			
						<div id="company-user-grid"  class="users no-border"></div>
					</div>				
				</div>
			</div>
		</script>				
		<#include "/html/common/common-system-templates.ftl" >			
	</body>    
</html>
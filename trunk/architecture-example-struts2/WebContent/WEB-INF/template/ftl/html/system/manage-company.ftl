<#ftl encoding="UTF-8"/>
<html decorator="secure-metro">
<head>
		<title>회사 관리</title>
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',
       	    '${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/bootstrap/3.0.0/bootstrap.min.js',
			'${request.contextPath}/js/bootstrap/3.0.0/tooltip.js',		       	    
       	    '${request.contextPath}/js/kendo/kendo.ko_KR.js',
       	    '${request.contextPath}/js/common/common.models.js',
       	    '${request.contextPath}/js/common/common.ui.js'
      	     ],        	  	   
			complete: function() {      
				// START SCRIPT           

				// ACCOUNTS LOAD		
				var currentUser = new User({});
				var accounts = $("#account-panel").kendoAccounts({
					visible : false,
					authenticate : function( e ){
						currentUser = e.token;						
					}
				});
								
				// 1. COMPANY GRID			        
				var selectedCompany = new Company();				
				
				// SPLITTER LAYOUT
				var splitter = $("#splitter").kendoSplitter({
                        orientation: "horizontal",
                        panes: [
                            { collapsible: false, min: "500px" },
                            { collapsible: true, collapsed: true, min: "500px" }
                        ]
                 }).data("kendoSplitter");
                 
                $("#splitter").css( "height", $(document).height() );
                $("#list_pane").css( "height", $(document).height() );
                
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
						error: handleKendoAjaxError
					},
					columns: [
						{ field: "companyId", title: "ID", width:40,  filterable: false, sortable: false }, 
						{ field: "name",    title: "KEY",  filterable: true, sortable: true,  width: 80 }, 
						{ field: "displayName",   title: "이름",  filterable: true, sortable: true,  width: 100 }, 
						{ field: "description", title: "설명", width: 200, filterable: false, sortable: false },
						{ command: [ {name:"edit",  text: { edit: "수정", update: "저장", cancel: "취소"}  }  ], title: "&nbsp;" }], 
					filterable: true,
					editable: "inline",
					selectable: 'row',
					height: 650,
					batch: false,
					toolbar: [ { name: "create", text: "회사 추가" }, { name: "view-roles", text: "롤 정보보기", className: "viewRoleCustomClass" }, { name: "view-menu", text: "메뉴관리", className: "viewMenuCustomClass" },
						{ name: "group-mgmt", text: "그룹관리", className: "goGroupMainCustomClass" }, { name: "user-mgmt", text: "사용자관리", className: "goUserMainCustomClass" } ],                    
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
								selectedCompany.description = selectedCell.description;
								selectedCompany.modifiedDate = selectedCell.modifiedDate;
								selectedCompany.creationDate = selectedCell.creationDate;	                                 
								selectedCompany.formattedCreationDate  =  kendo.format("{0:yyyy.MM.dd}",  selectedCell.creationDate );      
								selectedCompany.formattedModifiedDate =  kendo.format("{0:yyyy.MM.dd}",  selectedCell.modifiedDate );    
															
								$("#splitter").data("kendoSplitter").expand("#datail_pane");
								
								// 1-1-2 템플릿을 이용한 상세 정보 출력	
								$('#company-details').show().html(kendo.template($('#template').html()));	
								kendo.bind($(".details"), selectedCompany );
										                                 
								// 1-1-4 상세 탭 생성	                                 
								var company_tabs = $('#company-details').find(".tabstrip").kendoTabStrip({
									animation: {
										close: { duration: 200, effects: "fadeOut" },
										open: { duration: 200, effects: "fadeIn" }
									},
									select : function(e){
										// 1-1-4-1 상세 그룹 정보 GRID 생성 
										if( $( e.contentElement ).find('div').hasClass('groups') ){	
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
														error:handleKendoAjaxError
													},
													height: 300,
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
												
												$("#company-group-grid").attr('style','');	   										
											}
										// 1-1-4-2 DETAIL PROPS GRID 생성 
										}else if( $( e.contentElement ).find('div').hasClass('props') ){				         
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
														error:handleKendoAjaxError
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
													height: 300,
													toolbar: [
														{ name: "create", text: "추가" },
														{ name: "save", text: "저장" },
														{ name: "cancel", text: "취소" }
													],				     
													change: function(e) {
													}
												});													
												// GRID 화면 깨짐을 이한 속성 변경
												$("#company-prop-grid").attr('style','');	   	
											}
										// 1-1-4-3 상세 사용자 정보 GRID 생성 (디폴드로 보여줄 데이터이므로 1-1-5 에서 GRID 생성
										}else if( $( e.contentElement ).find('div').hasClass('users') ){
											if( ! $('#company-user-grid').data("kendoGrid") ){	
											
											}
										}					                      
									}
								}).css("border", "0");;
								// 1-1-5 DETAIL USERS GRID
								company_tabs.find(".users").kendoGrid({
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
										error:handleKendoAjaxError,
										batch: false,
										pageSize: 10,
										serverPaging: true,
										serverFiltering: false,
										serverSorting: false 
									},
									height: 350,
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
								
								//$("#company-user-grid").attr('style','');	   		
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
				}).css("border", 0);
				
				

				$('#company-grid').find(".goGroupMainCustomClass").click( function(){			
					$("form[name='fm1']").attr("action", "main-group.do" ).submit(); 
				} );
				
				$('#company-grid').find(".goUserMainCustomClass").click( function(){					
					$("form[name='fm1']").attr("action", "main-user.do" ).submit(); 
				} );
				
				// MENU WINDOW
				var selectedMenu = new Menu();
				$('#company-grid').find(".viewMenuCustomClass").click( function(){
					if(! $("#menu-grid").data("kendoGrid")){
						$('#menu-grid').kendoGrid({
							dataSource: {
								transport: { 
									read: { url:'${request.contextPath}/secure/list-menu.do?output=json', type:'post' }
								},
								batch: false, 
								schema: {
									total: "totalMenuCount",
									data: "menus",
									model: Menu
								},
								pageSize: 15,
								serverPaging: true,
								serverFiltering: false,
								serverSorting: false,  
								error:handleKendoAjaxError
							},
							columns: [
								//{ title: "ID", field: "menuId",  width:40 },
								{ title: "이름", field: "name", width:100 }
							],
							pageable: { refresh:true, pageSizes:false,  messages: { display: ' {1} / {2}' }  },					
							resizable: true,
							editable : false,
							selectable : "row",
							scrollable: true,
							height: 400,
							toolbar : [{ text: "메뉴 추가", className: "newMenuCustomClass"}] ,
							change: function(e) {
								var selectedCells = this.select();
								if( selectedCells.length == 1){ 
                            		var selectedCell = this.dataItem( selectedCells );                      
                            		selectedMenu.menuId = selectedCell.menuId;
                            		selectedMenu.name = selectedCell.name;
                            		selectedMenu.enabled = selectedCell.enabled;
                            		selectedMenu.description = selectedCell.description;
                            		selectedMenu.properties = selectedCell.properties;
                            		selectedMenu.menuData = selectedCell.menuData;
                            		selectedMenu.modifiedDate = selectedCell.modifiedDate;
                            		selectedMenu.creationDate = selectedCell.creationDate;	        
								 	kendo.bind($(".menu-details"), selectedMenu );
								 	$(".menu-details").show();
								 	
 									$('#update-menu-btn').bind('click' , function(){
 										if( selectedMenu.menuId > 0){
											$.ajax({
												type : 'POST',
												url : "${request.contextPath}/secure/update-menu.do?output=json",
												data : { menuId:selectedMenu.menuId, item: kendo.stringify( selectedMenu ) },
												success : function( response ){									
												    $('#menu-grid').data('kendoGrid').dataSource.read();	
												},
												error: handleKendoAjaxError,
												dataType : "json"
											});	
										}else{
											$.ajax({
												type : 'POST',
												url : "${request.contextPath}/secure/create-menu.do?output=json",
												data : { menuId:selectedMenu.menuId, item: kendo.stringify( selectedMenu ) },
												success : function( response ){									
												    $('#menu-grid').data('kendoGrid').dataSource.read();	
												},
												error: handleKendoAjaxError,
												dataType : "json"
											});											
										}
									});				 	
								}
							},
							dataBound: function(e){
								kendo.bind($(".menu-details"), {} );      	
								$(".menu-details").hide();
							}
						});	 						
						$("#menu-grid").attr('style','');	  

						$(".newMenuCustomClass").click( function (e){
							$(".menu-details").show();
							$("#menu-grid").data("kendoGrid").clearSelection();
							selectedMenu = new Menu();		
							kendo.bind($(".menu-details"), selectedMenu );      
						});
						
						if(! $("#menu-window").data("kendoWindow")){       
							// WINDOW 생성
							$(".menu-details").hide();
							$("#menu-window").kendoWindow({
								actions: ["Close"],
								resizable: false,
								modal: true,
								visible: false,
								title: '메뉴'
							});
						}			 		                   								
					}					
					var menuWindow = $("#menu-window").data("kendoWindow");
					//$("#menu-window").closest(".k-window").css({
					//	top: 70,
					//	left: 15,
					//});					
					menuWindow.maximize();
					menuWindow.open();
				});				
				
				// ROLE WINDOW				
				$('#company-grid').find(".viewRoleCustomClass").click( function(){															 
					if(! $("#perms-window").data("kendoWindow")){								
						// WINDOW 생성
						$("#perms-window").kendoWindow({							
							minHeight : 300,
							maxHeight : 500,
							minWidth :  200,
							maxWidth :  700,
							modal: true,
							visible: false,
							title: '권한 정보'
						});
					}					
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
								error:handleKendoAjaxError
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
							height: 300,
							change: function(e) {
							}
						});								
					}					
					var permsWindow = $("#perms-window").data("kendoWindow");
					$("#perms-window").closest(".k-window").css({
						top: 70,
						left: 15,
					});	
					permsWindow.open();					
				});
				           	                	                
				// END SCRIPT
			}
		}]);
		-->
		</script> 		 
		<style>
	    			#list_pane{height:700px;}
		</style>
	</head>
	<body>
		<!-- START HEADER -->
		<!-- END HEADER -->
		<!-- START MAIN CONTENT -->		
		<div class="container">
			<div class="row">
					<div id="splitter">
						<div id="list_pane">
							<div id="company-grid"></div>		
						</div>
						<div id="datail_pane">
							<div id="company-details"></div>
						</div>
					</div>		
			</div>
		</div>	
		<div id="account-panel"></div>				
		<!-- END MAIN CONTENT -->
		<div id="perms-window" style="display:none;" class="clearfix">
			<div class="alert alert-info">그룹 또는 사용자에게 부여 가능한 롤은 다음과 같습니다.</div>
			<div id="role-grid">	</div>            	
		</div>
		
		<div id="menu-window" style="display:none;">
		<div class="container layout">
			 <div class="row">
				<div class="col-4 col-lg-4"><div class="k-content"><div id="menu-grid"></div></div></div>
				<div class="col-8 col-lg-8">
					<div class="menu-details" style="dispaly:none">
					<div class="panel blank-space-5 gray">
						<form class="form-horizontal">
							<div class="form-group">
								<label class="col-lg-2 control-label" for="input-menu-name">이름</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" placeholder="이름" data-bind="value:name" id="input-menu-name"/>
								</div>
							</div>
							<div class="form-group">
								<label class="col-lg-2 control-label" for="input-menu-title">타이틀</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" placeholder="타이틀" data-bind="value:title" id="input-menu-title"/>
								</div>
							</div>				
							<div class="form-group">
								<label class="col-lg-2 control-label" >옵션</label>
								<div class="col-lg-10">
									<div class="checkbox">
										<label>
											<input type="checkbox"  name="enabled"  data-bind="checked: enabled" /> 사용여부
										</label>
									</div>
								</div>							
							</div>				
							<div class="form-group">
								<label class="col-lg-2 control-label" for="input-menu-description">설명</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" placeholder="설명" data-bind="value:description" id="input-menu-description"/>
								</div>
							</div>			
							<div class="form-group">
								<label class="col-lg-2 control-label" for="input-menu-xmldata">XML &nbsp<br/><span class="label label-danger">Important</span></label>
								<div class="col-lg-10">
									<textarea  data-bind="value: menuData" rows="10" id="input-menu-xmldata" class="form-control"></textarea>
								</div>
							</div>	
							<div class="form-group">
								<div class="col-lg-2" ></div>
								<div class="col-lg-10">
									<button id="update-menu-btn" class="k-button right">저장</button>
								</div>
							</div>								
						</form>								
					</div>
					</div>
				</div>
			</div>
		</div>
		</div>
			
		<!-- START FOOTER -->
		<!-- END FOOTER -->		
		
		<!-- 템플릿 -->
		<script type="text/x-kendo-template" id="template">								
			<div class="tabstrip">			
				<ul>
					<li>프로퍼티</li>
					<li>그룹 정보</li>
					<li class="k-state-active"> 사용자 정보</li>		                    
				</ul>
				<div>
					<div id="company-prop-grid" class="props"></div>
					<div class="box leftless rightless bottomless">
						<div class="alert alert-error">프로퍼티는 저장 버튼을 클릭하여야 최종 반영됩니다.</div>
					</div>
				</div> 	
				<div>
					<div id="company-group-grid"  class="groups"></div>		
					<div class="box leftless rightless bottomless">
						<div class="alert alert-info">그룹 관리는 그룹관리 메뉴를 사용하여 관리 하실수 있습니다</div>	  
					</div>              
				</div>        
				<div>
					<div id="company-user-grid"  class="users"></div>	
					<div class="box leftless rightless bottomless">
						<div class="alert alert-info">사용자 관리는  사용자 관리 메뉴를 사용하여 관리 하실수 있습니다.</div>	     
					</div>	
				</div>        
			</div>
			<form name="fm1" method="POST" accept-charset="utf-8" class="details">
				<input type="hidden" name="companyId" data-bind="value: companyId" />
			</form>					
		</script>	
		<!-- 공용 템플릿 -->
		<#include "/html/common-templates.ftl" >		        	
	</body>    
</html>
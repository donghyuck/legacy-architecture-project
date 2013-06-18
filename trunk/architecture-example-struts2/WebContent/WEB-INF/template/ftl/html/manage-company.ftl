<#ftl encoding="UTF-8"/>
<html decorator="secure">
<head>
		<title>회사 관리</title>
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [ 	      
			'css!${request.contextPath}/styles/jquery.pageslide/jquery.pageslide.css',
			'${request.contextPath}/js/jquery/1.9.1/jquery.min.js',
			'${request.contextPath}/js/jquery.pageslide/jquery.pageslide.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
       	    '${request.contextPath}/js/kendo/kendo.web.min.js',
       	    '${request.contextPath}/js/kendo/kendo.ko_KR.js',
       	    '${request.contextPath}/js/common/common.ui.js',
      	    '${request.contextPath}/js/common/common.models.js' ],        	  	   
			complete: function() {      
				// START SCRIPT           
				// 1. COMPANY GRID			        
				var selectedCompany = new Company();
				var splitter = $("#splitter").kendoSplitter({
                        orientation: "horizontal",
                        panes: [
                            { collapsible: true, min: "500px" },
                            { collapsible: true, collapsed: true, min: "500px" }
                        ]
                 });
                    
				$("header .open").pageslide({ modal: true });
				
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
					height: 598,
					batch: false,
					toolbar: [ { name: "create", text: "회사 추가" }, { name: "view-roles", text: "롤 정보보기", className: "viewRoleCustomClass" } ],                    
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
																
								// 1-1-3 툴바 생성
								$("#menu").kendoMenu({
									select: function(e){									
										if( selectedCompany.companyId > 0 ){
											var action = $(e.item).attr('action') ;
											$("form[name='fm1']").attr("action", action ).submit(); 
										}else{
											alert("선택된 회사가 없습니다. 회사를 먼저 선택하여주세요.");
										}
									}		
								}).css("background-color", "#F5F5F5").css("border-width", "0px 0px 1px");
								
								$("#menu").show();
										                                 
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
				
				$('#company-grid').find(".viewRoleCustomClass").click( function(){
					if(! $("#perms-window").data("kendoWindow")){
						// WINDOW 생성
						$("#perms-window").kendoWindow({
							minHeight : 200,
							maxHeight : 500,
							minWidth :  200,
							maxWidth :  700,
							modal: true,
							visible: false,
							title: '권한 정보'
						});
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
							height: 200,
							change: function(e) {
							}
						});																				
					}
					var permsWindow = $("#perms-window").data("kendoWindow");
					permsWindow.center();
					permsWindow.open();
				});
				           	                	                
				// END SCRIPT
			}
		}]);
		-->
		</script> 		 
		<style>
			#mainContent {
				margin-top: 5px;
			}
		</style>
	</head>
	<body>
		<!-- START HEADER -->
		<header>
			<div class="row layout">
				<div class="large-12 columns">
					<div class="big-box topless bottomless">
					<h1><a class="open" href="${request.contextPath}/secure/get-system-menu.do">Menu</a>회사관리</h1>
					<h4>회사를 관리하기 위한 기능을 제공합니다.</h4>
					</div>
				</div>
			</div>
		</header>
		<!-- END HEADER -->
		<!-- START MAIN CONTENT -->
		
		
		<section id="mainContent">		

					<div id="splitter" style="height:600px;">
						<div id="list_pane">
							<div class="row layout">
								<div class="large-12 columns">				
									<div id="company-grid" class="layout"></div>
								</div>
							</div>			
						</div>
						<div id="datail_pane">				
							<div class="row layout">
								<div class="large-12 columns">
									<ul id="menu" class="hide" >
										<li action="main-group.do">그룹 관리하기</li>
										<li action="main-user.do">사용자 관리하기</li>
									</ul>						
								</div>
							</div>			
							<div class="row layout">
								<div class="large-12 columns">
									<div id="company-details"></div>
								</div>
							</div>
						</div>
					</div>
					<div id="perms-window" style="display:none;">
						<div class="alert-box secondary">그룹 또는 사용자에게 부여 가능한 롤은 다음과 같습니다.</div>
						<div id="role-grid">	</div>            	
					</div>
					
		</section>
		<!-- END MAIN CONTENT -->
		<!-- START FOOTER -->
		<footer class="row"> 
		</footer>
		<!-- END FOOTER -->		
		<!-- 상세 정보 템플릿 -->
		<script type="text/x-kendo-template" id="template">				
			<form name="fm1" method="POST" accept-charset="utf-8" class="details">
				<input type="hidden" name="companyId" data-bind="value: companyId" />
			</form>						
			<div class="tabstrip">			
				<ul>
					<li>프로퍼티</li>
					<li>그룹 정보</li>
					<li class="k-state-active"> 사용자 정보</li>		                    
				</ul>
				<div>
					<div id="company-prop-grid" class="props"></div>
					<div class="box leftless rightless bottomless">
						<div class="alert-box secondary">프로퍼티는 저장 버튼을 클릭하여야 최종 반영됩니다.</div>
					</div>
				</div> 	
				<div>
					<div id="company-group-grid"  class="groups"></div>		
					<div class="box leftless rightless bottomless">
						<div class="alert-box secondary">그룹 관리는 그룹관리 메뉴를 사용하여 관리 하실수 있습니다</div>	  
					</div>              
				</div>        
				<div>
					<div id="company-user-grid"  class="users"></div>	
					<div class="box leftless rightless bottomless">
						<div class="alert-box secondary">사용자 관리는  사용자 관리 메뉴를 사용하여 관리 하실수 있습니다.</div>	     
					</div>	
				</div>        
			</div>
		</script>		            	
	</body>    
</html>
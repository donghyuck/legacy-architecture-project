<#ftl encoding="UTF-8"/>
<html decorator="secure">
    <head>
        <title>그룹 관리</title>
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

            	$(document).ready(function(){        
					// SPLITTER LAYOUT
					var splitter = $("#splitter").kendoSplitter({
	                        orientation: "horizontal",
	                        panes: [
	                            { collapsible: true, min: "500px" },
	                            { collapsible: true, collapsed: true, min: "500px" }
	                        ]
	                 });
	                 
					$("#company").kendoDropDownList({
                        dataTextField: "displayName",
                        dataValueField: "companyId",
                        dataSource: {
                            transport: {
                                read: {
                                    type: "json",
                                    url: '${request.contextPath}/secure/list-company.do?output=json',
									type:'POST'
                                }
                            },
                            schema: { 
                            		data: "companies",
                            		model : Company
                        	}
                        }
                    });
                    
                    $("header .open").pageslide({ modal: true });
                    
					$("#company").data("kendoDropDownList").readonly();
							                                 
					$("#menu").kendoMenu({
						select: function(e){							
							var action = $(e.item).attr('action') ;										
							if( action != '#' ){
								$("form[name='fm1']").attr("action", action ).submit(); 
							}
						}						
					}).css("background-color", "#F5F5F5").css("border-width", "0px 0px 1px");;
					
					$("#menu").show();									

					$("#go-comapny-btn").click( function(){
						$("form[name='fm1']").attr("action", "main-company.do" ).submit(); 
					}); 
								
			        // 1. GROUP GRID			        
			        var selectedGroup = new Group();		      
			        var group_grid = $("#group-grid").kendoGrid({
	                    dataSource: {	
	                        transport: { 
	                            read: { url:'${request.contextPath}/secure/list-group.do?output=json', type: 'POST' },
	                            create: { url:'${request.contextPath}/secure/create-group.do?output=json', type:'POST' },             
	                            update: { url:'${request.contextPath}/secure/update-group.do?output=json', type:'POST' },
		                        parameterMap: function (options, operation){	          
		                            if (operation != "read" && options) {
		                                return { companyId: $("#company").data("kendoDropDownList").value(), item: kendo.stringify(options)};
		                            }else{
		                                return { startIndex: options.skip, pageSize: options.pageSize , companyId: $("#company").data("kendoDropDownList").value() }
		                            }
		                        }                  
	                        },
	                        schema: {
	                            total: "totalGroupCount",
	                            data: "groups",
	                            model : Group
	                        },
	                        pageSize: 15,
	                        serverPaging: true,
	                        serverFiltering: false,
	                        serverSorting: false,                        
	                        error: handleKendoAjaxError
	                    },
	                    columns: [
	                        { field: "groupId", title: "ID", width:40,  filterable: false, sortable: false }, 
	                        { field: "name",    title: "KEY",  filterable: true, sortable: true,  width: 100 }, 
	                        { field: "displayName",    title: "이름",  filterable: true, sortable: true,  width: 100 }, 
	                        { field: "description", title: "설명", width: 200, filterable: false, sortable: false },
	                        { command:  [ {name:"edit",  text: { edit: "수정", update: "저장", cancel: "취소"}  }  ], title: "&nbsp;" }], 
	                    filterable: true,
	                    editable: "inline",
	                    selectable: 'row',
	                    height: "560",
	                    batch: false,
	                    toolbar: [ { name: "create", text: "그룹추가" } ],                    
	                    pageable: { refresh:true, pageSizes:false,  messages: { display: ' {1} / {2}' }  },                    
	                    change: function(e) {	                       
	                        var selectedCells = this.select();	                       
	                        if( selectedCells.length == 1){
	                             var selectedCell = this.dataItem( selectedCells );	                             
	                             if( selectedCell.groupId > 0 && selectedCell.groupId != selectedGroup.groupId ){       
	                                 selectedGroup.groupId = selectedCell.groupId;
	                                 selectedGroup.name = selectedCell.name;
	                                 selectedGroup.displayName = selectedCell.displayName;
	                                 selectedGroup.description = selectedCell.description;
	                                 selectedGroup.modifiedDate = selectedCell.modifiedDate;
	                                 selectedGroup.creationDate = selectedCell.creationDate;
	                                 selectedGroup.formattedCreationDate  =  kendo.format("{0:yyyy.MM.dd}",  selectedCell.creationDate );      
	                                 selectedGroup.formattedModifiedDate =  kendo.format("{0:yyyy.MM.dd}",  selectedCell.modifiedDate );         	                                 
	                                 selectedGroup.company = $("#company").data("kendoDropDownList").dataSource.get(  $("#company").data("kendoDropDownList").value()  );
	                                 
	                                 $("#splitter").data("kendoSplitter").expand("#datail_pane");
	                                 
	                                 // SHOW GROUP DETAILS ======================================	                                 	                                 
	                                 $('#group-details').show().html(kendo.template($('#template').html()));	                                 
	                                 kendo.bind($(".details"), selectedGroup );
									                                 
	                                // 2. GROUP TABS	                                
									var group_tabs = $('#group-details').find(".tabstrip").kendoTabStrip({
				                      animation: {
								        close: { duration: 200, effects: "fadeOut" },
								       	open: { duration: 200, effects: "fadeIn" }
				                      },
				                      select : function(e){			
				                      	// TAB - MEMBERS 
				                      	if( $( e.contentElement ).find('div').hasClass('members') ){
				                      	                
				                      	// TAB - ROLES                
				                      	}else if( $( e.contentElement ).find('div').hasClass('roles') ){				                      	
				                      		if( ! $('#group-role-select').data("kendoMultiSelect") ){	
				                      			var selectedRoleDataSource = new kendo.data.DataSource({
													transport: {
										            	read: { 
										            		url:'${request.contextPath}/secure/get-group-roles.do?output=json', 
										            		dataType: "json", 
										            		type:'POST',
										            		data: { groupId: selectedGroup.groupId }
												        }  
												    },
												    schema: {
									                	data: "groupRoles",
									                    model: Role
									                },
									                error:handleKendoAjaxError,
									                change: function(e) {                
						                        		var multiSelect = $("#group-role-select").data("kendoMultiSelect");
						                        		var selectedRoleIDs = "";
						                        		$.each(  selectedRoleDataSource.data(), function(index, row){  
						                        			if( selectedRoleIDs == "" ){
						                        			    selectedRoleIDs =  selectedRoleIDs + row.roleId ;
						                        			}else{
						                        				selectedRoleIDs = selectedRoleIDs + "," + row.roleId;
						                        			}
						                        		} );			                        		
						                        		multiSelect.value( selectedRoleIDs.split( "," ) );	 
									                }	                               
				                               });	
		                               
												$('#group-role-select').kendoMultiSelect({
				                                    placeholder: "롤 선택",
									                dataTextField: "name",
									                dataValueField: "roleId",
									                dataSource: {
									                    transport: {
									                        read: {
							                                    url: '${request.contextPath}/secure/list-role.do?output=json',
																dataType: "json",
																type: "POST"
									                        }
									                    },
									                    schema: { 
						                            		data: "roles",
						                            		model: Role
						                        		}
									                },
						                        	error:handleKendoAjaxError,
						                        	dataBound: function(e) {
						                        		 selectedRoleDataSource.read();   	
						                        	},			                        	
						                        	change: function(e){
						                        		var multiSelect = $("#group-role-select").data("kendoMultiSelect");			                        		
						                        		var list = new Array();			                        		                  		
						                        		$.each(multiSelect.value(), function(index, row){  
						                        			var item =  multiSelect.dataSource.get(row);
						                        			list.push(item);			                        			
						                        		});
						                        		
						                        		multiSelect.readonly();			                        		
							 							$.ajax({
												            dataType : "json",
															type : 'POST',
															url : "${request.contextPath}/secure/update-group-roles.do?output=json",
															data : { groupId:selectedGroup.groupId, items: kendo.stringify( list ) },
															success : function( response ){		
																// need refresh ..			
																// alert( kendo.stringify( response ) );							    
															},
															error:handleKendoAjaxError
														});												
														multiSelect.readonly(false);
						                        	}
									            });	
				                      		}
				                      		
				                      	// TAB - PROPS GRID	
				                      	} else if( $( e.contentElement ).find('div').hasClass('props') ){			
				                      		if( ! $('#group-prop-grid').data("kendoGrid") ){	
				                      			
												group_tabs.find(".props").kendoGrid({
													dataSource: {
														transport: { 
															read: { url:'${request.contextPath}/secure/get-group-property.do?output=json', type:'post' },
															create: { url:'${request.contextPath}/secure/update-group-property.do?output=json', type:'post' },
															update: { url:'${request.contextPath}/secure/update-group-property.do?output=json', type:'post'  },
															destroy: { url:'${request.contextPath}/secure/delete-group-property.do?output=json', type:'post' },
													 		parameterMap: function (options, operation){			
														 		if (operation !== "read" && options.models) {
														 			return { groupId: selectedGroup.groupId, items: kendo.stringify(options.models)};
																} 
																return { groupId: selectedGroup.groupId }
															}
														},						
														batch: true, 
														schema: {
															data: "targetGroupProperty",
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
				                                  $("#group-prop-grid").attr('style','');	   
				                      		}
				                      	}				                      
				                      }
				                  	}).css("border", "0px");                             
	                                  // 2-2. GROUP MEMBER GRID
	                                 group_tabs.find(".members").kendoGrid({
	                                     dataSource: {
									    	type: "json",
						                    transport: {
						                        read: { url:'${request.contextPath}/secure/list-group-user.do?output=json', type:'post' },			
												destroy: { url:'${request.contextPath}/secure/remove-group-members.do?output=json', type:'post' },
												parameterMap: function (options, operation){												                  
								                    if (operation !== "read" && options.models) {
												 	    return { groupId: selectedGroup.groupId, items: kendo.stringify(options.models)};
						                            } 
								                     return { groupId: selectedGroup.groupId,  startIndex: options.skip, pageSize: options.pageSize  }		
								                }                        
						                    },
						                    schema: {
						                            total: "totalGroupUserCount",
						                            data: "groupUsers",
						                            model: User
						                    },
						                    error:handleKendoAjaxError,
						                    autoSync: true,
						                    batch: true,
						                    serverPaging: true,
						                    serverSorting: false,
						                    serverFiltering: false,
						                    pageSize:10
	                                     },
									    scrollable: true,								    
						                sortable: true,
						                height: 350,
						                resizable: true,
						                editable: {
						                	update: false,
						                	destroy: true,
						                	confirmation: "선택하신 사용자를 그룹에서 	삭제하겠습니까?"	
						                },
						                pageable: { refresh:true, pageSizes:false,  messages: { display: ' {1} / {2}' }  },
						                toolbar: [
									         { name: "search", text: "멤버 검색", imageClass:"k-icon k-i-search" , className: "searchCustomClass" },
									         { name: "cancel", text: "취소"}
										 ],				      
						                columns: [
						                    { field: "userId", title: "ID", width:50,  filterable: false, sortable: false}, 
						                    { field: "username", title: "아이디", width: 80 }, 
						                    { field: "name", title: "이름", width: 80 }, 
						                    { field: "email", title: "메일", width: 80  },
						                    { command:  { name: "destroy", text:"삭제" },  title: "&nbsp;", width: 100 }	
						                ],
						                dataBound:function(e){   
						                	var group_memeber_grid = $('#group-member-grid').data('kendoGrid'); 
						                    selectedGroup.memberCount = group_memeber_grid.dataSource.total() ;			                    
						                	kendo.bind($(".tabstrip"), selectedGroup );
						                }                                                            
	                                 });	  
	                                group_tabs.find(".searchCustomClass").click(function(){	        	                                 	              
								    	// 3. SEARCH WINDOW 								    	
								    	if( !$("#search-window").data("kendoWindow") ){											
											// 3-1 SEARCH WINDOW 		
											
								    		$("#search-window").kendoWindow({
									    		width:460,
									    		resizable : false,
					                            title:  selectedGroup.company.name + " 사용자 검색",
					                            modal: true,
					                            visible: false
									    	});
									    	 // make focus user search input 									    	
											// 3-2 SEARCH RESULT GRID
											$("#search-result").kendoGrid({
										        dataSource: {
													type: "json",
													transport: {
														read: { url:'${request.contextPath}/secure/find-user.do?output=json', type:'post' },
														parameterMap: function (options, operation){							                
															if (operation !== "read" && options.models) {
													 			return { nameOrEmail: search_text, items: kendo.stringify(options.models) , companyId: selectedGroup.company.companyId };
															} 
										                    return { nameOrEmail:options.search_text,  startIndex: options.skip, pageSize: options.pageSize , companyId: selectedGroup.company.companyId };
														}                        
													},
													schema: {
										                   total: "foundUserCount",
										                   data: "foundUsers",
										                   model: User
										           },
										           serverPaging: false,
										           serverSorting: false,
										           serverFiltering: false,
										           pageSize:10,
										           error: handleKendoAjaxError                             
										        },
										    	scrollable: true,
										       	sortable: true,
										       	selectable: "multiple, row",
										       	height: 280,					       			      
										        columns: [
										           { field: "userId", title: "ID", width:25,  filterable: false, sortable: false }, 
										           { field: "username", title: "아이디", width: 100 }, 
										           { field: "name", title: "이름", width: 50 }, 
										           { field: "email", title: "메일", width: 100 },
										       ],
										       autoBind: true                                                            
										    });
										} 										
										$('#search-window').data("kendoWindow").center();       
								    	$("#search-window").data("kendoWindow").open();								    	
								    	$("#search-text").focus();								    	
	                                 });	                                
	                             }
	                        }else{
	                            selectedGroupId = 0 ;	                            
	                        }
						},					
						dataBound: function(e){   
						    var selectedCells = this.select();				
						    if(selectedCells.length == 0 )
						    {
						    	selectedGroup = new Group({});
						    	kendo.bind($(".details"), selectedGroup );		
								$("#group-details").hide(); 	 			    	
						    }   
						}					
	                }).css("border", "0px"); 	                	
	                
	                // 3-3 CLOSE SEARCH WINDOW
	                $("#close-search-window-btn").click( function() {	
	                	$("#search-window").data("kendoWindow").close();
	                	var user_search_grid = $('#search-result').data('kendoGrid') ;	                	
	                	$("#search-text").val("");
	                	if( user_search_grid.dataSource.total() > 0 ){	                		
	                		user_search_grid.dataSource.read({ search_text: "" });	
	                	}  
	                } );
	                
					// 4 SEARCHING  BUTTON             				
					$("#search-user-btn").click(function(){	
						var search_text = $("#search-text").val();
						var user_search_grid = $('#search-result').data('kendoGrid') ;
						user_search_grid.dataSource.read({ search_text: search_text });						
					});
											
					// 5. 	ADD MEMBERS BUTTON				
					$("#add-member-btn").click( function(){									    
					        var user_search_grid = $('#search-result').data('kendoGrid') ;			
					        var group_memeber_grid = $('#group-member-grid').data('kendoGrid');      
					        var selectedUsers = [];	        		        
					        $.each(  user_search_grid.select(), function(index, row){        
					            var selectedItem = user_search_grid.dataItem(row);	
								var selectedUser = new User({
										userId: selectedItem.userId ,
									    username: selectedItem.username,
									    name: selectedItem.name, 
									    email: selectedItem.email
					            });	
					            selectedUsers.push( selectedUser )
					        } );	
					        
					        $.ajax({
					            dataType : "json",
								type : 'POST',
								url : "${request.contextPath}/secure/add-group-members.do?output=json",
								data : { groupId:selectedGroup.groupId, items: kendo.stringify( selectedUsers ) },
								success : function( response ){								    
									$.each(  user_search_grid.select(), function(index, row){      
							        	user_search_grid.removeRow(row);
							        });	
									group_memeber_grid.dataSource.read();
								},
								error:handleKendoAjaxError
							});									
					       // user_search_grid.clearSelection();
	                });
	                
	                $("#update-role-btn").click(function(){	
	                
	                }); 
				});
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
			<div class="row full-width layout">
				<div class="large-12 columns">
					<div class="big-box topless bottomless">
					<h1><a class="open" href="${request.contextPath}/secure/get-system-menu.do">Menu</a>그룹관리</h1>
					<h4>그룹을 관리하기 위한 기능을 제공합니다.</h4>
					</div>
				</div>
			</div>
		</header>
	  	<!-- END HEADER -->	  	
	  	<!-- START MAIN CONTNET -->
		<section id="mainContent">
			<div id="splitter" style="height:600px;">
				<div id="list_pane">
					<div class="row full-width layout">
						<div class="large-12 columns" >
							<ul id="menu" style="display:none;" >
				                <li action="#">회사
				                	<ul>	                		    
				                		<li>
				                			<div style="padding: 10px;">
				                			<input id="company" type="hidden" style="width: 250px" value="${action.companyId}"/>
				                			</div>
				                		</li>
				                		<li>
				                			<div style="padding: 10px;">
				                				<button id="go-comapny-btn" class="k-button">회사 관리하기</button>
				                			</div>	                			
				                		</li>	                		
				                	</ul>
				                </li>        
				                <li action="main-user.do">사용자</li>     
				            </ul>							
						</div>
					</div>
					<div class="row full-width layout">
						<div class="large-12 columns" >
							<div id="group-grid"></div>	
						</div>
					</div>	
				</div>
				<div id="datail_pane">			
					<div class="row full-width layout">			
						<div class="large-12 columns" >
							<div id="group-details"></div>
						</div>				
					</div>						
				</div>
			</div>					
			<form name="fm1" method="POST" accept-charset="utf-8">
				<input type="hidden" name="companyId"  value="${action.companyId}" />
			</form>
		</section>	  		
		<div id="search-window" style="display:none;">			
			<div class="row layout">
				<div class="small-12 columns">
					<table width="100%">
						<tbody>
							<tr>
								<td><input type="text" id="search-text" placeholder="검색할 사용자 이름 또는 메일 주소"  class="k-textbox" style="width:300px;;" /></td>
								<td width="150"><a class="k-button" id="search-user-btn"><span class="k-icon k-i-search"></span>검색</a></td>						
							</tr>
						</tbody>
					</table>		
				</div>
			</div>
			<div class="row layout">
				<div class="small-12 columns">
					<div class="alert-box alert">
					검색 결과 목록에서 추가를 원하는 사용자을 선택 후 "멤버추가" 버튼을 클릭하여 멤버를 그룹에 추가합니다. 여러 사용자를 추가하는 경우 SHIFT 키를 누르고 여러 사용자들을 선택합니다. 
					</div>
				</div>
			</div>
			<div class="row layout">
				<div class="small-12 columns">
					<div id="search-result"></div>
				</div>
			</div>
			<div class="row layout">
				<div class="small-12 columns">
					<div class="box leftless bottomless">
					<a class="k-button" id="add-member-btn"><span class="k-icon k-i-folder-add"></span>선택된 사용자 그룹 멤버로 추가하기</a> &nbsp; 
					<a class="k-button right" id="close-search-window-btn"><span class="k-icon k-i-close"></span>닫기</a>
					</div>
				</div>
			</div>
		</div>	    
		<!-- END MAIN CONTNET -->
	  <footer>  
	  </footer>
	
		<script type="text/x-kendo-template" id="template">					
			<div class="details">										
				<div class="tabstrip">
					<ul>
						<li>프로퍼티</li>
						<li class="k-state-active">멤버</li>
						<li>롤</li>
					</ul>
					<div>
						<div id="group-prop-grid" class="props"></div>
						<div class="box leftless rightless bottomless">
							<div class="alert-box secondary">프로퍼티는 저장 버튼을 클릭하여야 최종 반영됩니다.</div>
						</div>	
					</div> 	
					<div>
						<div id="group-member-grid"  class="members"></div>
						<div class="box leftless rightless bottomless">
							<div class="alert-box secondary">멤버수:<span data-bind="text:memberCount">0</span> 명</div>
						</div>	
					</div>
					<div>
						<div class="big-box bottomless">
							<div class="alert-box secondary">그룹에서 부여된 롤은 멤버들에게 상속됩니다. 아래의 선택 박스에서 롤을 선택하여 주세요.</div>
						</div>	
						<div class="roles big-box">
							<div id="group-role-select"></div>
						</div>	
					</div>
				</div>
			</div>
		</script>		        
	<!-- END MAIN CONTENT  -->	  
    </body>
</html>
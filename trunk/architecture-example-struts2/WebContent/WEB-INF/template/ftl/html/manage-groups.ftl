<#ftl encoding="UTF-8"/>
<html decorator="banded">
    <head>
        <title>그룹 관리</title>
        <script type="text/javascript">
        <!--
        yepnope([{
            load: [ 	       
           	   '${request.contextPath}/js/kendo/kendo.web.min.js',
        	   'preload!${request.contextPath}/js/common.models.js',               
        	   'preload!${request.contextPath}/js/common.ui.js'
	        ],        	   
            complete: function() {      

            	$(document).ready(function(){

			        // 1. GROUP GRID			        
			        var selectedGroup = new Group({});		      
			        var group_grid = $("#group-grid").kendoGrid({
	                    dataSource: {	
	                        transport: { 
	                            read: { url:'${request.contextPath}/secure/list-group.do?output=json', type: 'POST' },
	                            create: { url:'${request.contextPath}/secure/create-group.do?output=json', type:'POST' },             
	                            update: { url:'${request.contextPath}/secure/update-group.do?output=json', type:'POST' },
		                        parameterMap: function (options, operation){	          
		                            if (operation != "read" && options) {
		                                return { groupId: options.groupId, item: kendo.stringify(options)};
		                            }else{
		                                return { startIndex: options.skip, pageSize: options.pageSize }
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
	                        { field: "name",    title: "이름",  filterable: true, sortable: true,  width: 100 }, 
	                        { field: "description", title: "설명", width: 100, filterable: false, sortable: false },
	                        { command: [ {name:"edit", text:"수정"}  ], title: "&nbsp;" }], 
	                    filterable: true,
	                    editable: "inline",
	                    selectable: 'row',
	                    height: 500,
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
	                                 selectedGroup.description = selectedCell.description;
	                                 selectedGroup.modifiedDate = selectedCell.modifiedDate;
	                                 selectedGroup.creationDate = selectedCell.creationDate;
	                                	                                
	                                // 2. GROUP TABS
	                                $('#group-tabs').show().html(kendo.template($('#template').html()));
									var group_tabs = $('#group-tabs').find(".tabstrip").kendoTabStrip({
				                      animation: {
								        close: { duration: 200, effects: "fadeOut" },
								       	open: { duration: 200, effects: "fadeIn" }
				                      },
				                      select : function(e){			
				                      	if( $( e.contentElement ).find('div').hasClass('members') ){
				                      
				                      	}
				                      
				                      }
				                  	});       
				                  		                                 	                                 
	                                 // 2-1. GROUP PROPERTY GRID
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
										scrollable: false,
										height: 250,
										toolbar: [
											{ name: "create", text: "추가" },
											{ name: "save", text: "저장" },
											{ name: "cancel", text: "취소" }
										],				     
										change: function(e) {
										}
	                                  });
	                                  
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
						                height: 300,
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
						                    { command:  { name: "destroy" },  title: "&nbsp;", width: 100 }	
						                ],
						                dataBound:function(e){   
						                	var group_memeber_grid = $('#group-member-grid').data('kendoGrid'); 
						                    selectedGroup.memberCount = group_memeber_grid.dataSource.total() ;
						                	kendo.bind($(".tabstrip"), selectedGroup );
						                }                                                            
	                                 });	                                 
	                                
	                                // 2-3 ROLE GRID
									group_tabs.find(".roles").kendoGrid({
	                                     dataSource: {
									    	type: "json",
						                    transport: {
						                        read: { url:'${request.contextPath}/secure/get-group-roles.do?output=json', type:'post' },	
												parameterMap: function (options, operation){												                  
								                    if (operation !== "read" && options.models) {
												 	    return { groupId: selectedGroup.groupId, items: kendo.stringify(options.models)};
						                            } 
								                     return { groupId: selectedGroup.groupId  }		
								                }                        
						                    },
						                    schema: {
						                            data: "groupRoles",
						                            model: Role
						                    },
						                    error:handleKendoAjaxError,
						                    serverPaging: false,
						                    serverSorting: false,
						                    serverFiltering: false
	                                     },
									    scrollable: false,								    
						                sortable: true,
						                height: 300,
						                resizable: true,
						                editable: {
						                	update: false,
						                	destroy: true,
						                	confirmation: "선택하신 롤을 그룹에서 	삭제하겠습니까?"	
						                },      
						                columns: [
						                    { field: "roleId", title: "ID", width:50,  filterable: false, sortable: false}, 
						                    { field: "name", title: "롤", width: 80 }, 
						                    { field: "description", title: "설명", width: 80 }
						                ],
						                dataBound:function(e){   
						                }                                                            
	                                 });	     
	                                 	                                
	                                 group_tabs.find(".searchCustomClass").click(function(){	        	
								    	// 3. SEARCH WINDOW 
								    	$("#search-window").show();     
								    	if( !$("#search-window").data("kendoWindow") ){							    	
																			   
											// 3-1 SEARCH WINDOW 		
								    		$("#search-window").kendoWindow({
									    		width:450,
									    		height: 450,
									    		resizable : false,
									    		title:false
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
													 			return { nameOrEmail: search_text, items: kendo.stringify(options.models) };
															} 
										                    return { nameOrEmail:options.search_text,  startIndex: options.skip, pageSize: options.pageSize  };
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
										       autoBind: false                                                            
										    });
										} 										
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
						        $('#group-tabs').hide();
						}					
	                }); 	                	
	                
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
								type : 'POST',
								url : "${request.contextPath}/secure/add-group-members.do?output=json",
								data : { groupId:selectedGroup.groupId, items: kendo.stringify( selectedUsers ) },
								success : function( response ){									
							        $.each(  user_search_grid.select(), function(index, row){        
							        	user_search_grid.removeRow(row);
							        });									
									group_memeber_grid.dataSource.read();											
								},
								error: function( xhr, ajaxOptions, thrownError){								
								},
								dataType : "json"
							});		
													
					        user_search_grid.clearSelection();
	                }); 
				});
            }
        }]);
     	-->
        </script> 		        
    </head>
    <body>
    
    <!-- Main Page Title -->   
	<!-- START MAIN HEADER  -->   
	<header>
		<div class="row">
			<div class="twelve columns">
				<h1>그룹 관리</h1>
				<h4>그룹 관리 프로그램</h4>
			</div>
		</div>
	</header>
	<!-- END MAIN HEADER  -->   	            
	<!-- START MAIN CONTENT  -->     
	<section class="row" style="padding-top:10px;" >
	    <div class="six columns">
			<div id="search-window" style="display:none;" class="k-success-colored">			
				<table width="100%">
					<tbody>
						<tr>
							<td><input type="text" id="search-text" placeholder="검색할 사용자 이름 또는 메일"  class="k-textbox" style="width:300px;;" /></td>
							<td width="150"><a class="k-button" id="search-user-btn"><span class="k-icon k-i-search"></span>검색</a></td>						
						</tr>
						<tr>
							<td colspan=2>
							<div class="alert-box">
							검색 결과 목록에서 추가를 원하는 사용자을 선택 후 "멤버추가" 버튼을 클릭하여 멤버를 그룹에 추가합니다. 여러 사용자를 추가하는 경우 SHIFT 키를 누르고 여러 사용자들을 선택합니다. 
							</div>
							</td>
						</tr>						
						<tr>
							<td colspan=2><div id="search-result"></div></td>
						</tr>
						<tr>
							<td colspan=2 class="right"><a class="k-button" id="add-member-btn"><span class="k-icon k-i-plus"></span>멤버추가</a> &nbsp;  
							<a class="k-button" id="close-search-window-btn"><span class="k-icon k-i-close"></span>닫기</a></td>
						</tr>						
					</tbody>
				</table> 
			</div>	    	
	    	<div id="group-grid"></div>	    
	    </div>
	    <div class="six columns">	       
	        <div class="row">
	            <div class="twelve columns" style="padding:0px;"><div id="group-tabs"></div></div>	        
	        </div>	             
	    </div>     
		<script type="text/x-kendo-template" id="template">		
			<div class="tabstrip">
                <ul>
                    <li>
                        프로퍼티
                    </li>
                    <li class="k-state-active">
                       멤버
                    </li>
                    <li>
                    롤
                    </li>
                </ul>
                <div><div id="group-prop-grid" class="props"></div>
	                	<p/>
	                	<div class="alert-box success">프로퍼티는 저장 버튼을 클릭하여야 최종 반영됩니다.</div>
                </div> 	
                <div><div id="group-member-grid"  class="members"></div>
                	    <p/>
	                	<div class="alert-box success">멤버수:<span data-bind="text:memberCount">0</span> 명</div>
                </div>
                <div><div id="group-role-grid"  class="roles"></div></div>                			                
            </div>
        </script>
	</section>
	<!-- END MAIN CONTENT  -->  		
	<section class="row">
	    <div class="twelve columns">
	        <hr style="margin-top:10px;margin-bottom:10px;" />
			<ul class="breadcrumbs">
			  <li><a href="${request.contextPath}/main.do">홈</a></li>
			  <li class="unavailable"><a href="#">시스템</a></li>
			  <li class="current"><a href="#">그룹 관리</a></li>
			</ul>
		</div>
	</section>
	
	<!-- End Main Content and Breadcrumbs -->	    
    </body>
</html>
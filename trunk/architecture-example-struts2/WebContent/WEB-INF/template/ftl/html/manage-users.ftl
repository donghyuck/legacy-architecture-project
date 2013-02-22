<#ftl encoding="UTF-8"/>
<html decorator="banded">
    <head>
        <title>사용자 관리</title>
        <script type="text/javascript">                
        yepnope([{
            load: [             
        	   '${request.contextPath}/js/kendo/kendo.web.min.js',
        	   'preload!${request.contextPath}/js/common.models.js',               
        	   'preload!${request.contextPath}/js/common.ui.js'
        	   ],
        	   
            complete: function() {   
                        	            	
	            var selectedUser = new User ({});		            
		        // 1. USER GRID 		        
				var user_grid = $("#user-grid").kendoGrid({
                    dataSource: {
                        transport: { 
                            read: { url:'${request.contextPath}/secure/list-user.do?output=json', type: 'POST' },
	                        parameterMap: function (options, type){
	                            return { startIndex: options.skip, pageSize: options.pageSize }
	                        }
                        },
                        schema: {
                            total: "totalUserCount",
                            data: "users",
                            model: User
                        },
                        batch: false,
                        pageSize: 15,
                        serverPaging: true,
                        serverFiltering: false,
                        serverSorting: false
                    },
                    columns: [
                        { field: "userId", title: "ID", width:50,  filterable: false, sortable: false }, 
                        { field: "username", title: "아이디", width: 100 }, 
                        { field: "name", title: "이름", width: 100 }, 
                        { field: "email", title: "메일" },
                        { field: "creationDate", title: "생성일", filterable: false,  width: 100, format: "{0:yyyy/MM/dd}" } ],         
                    filterable: true,
                    sortable: true,
                    pageable: { refresh:true, pageSizes:false,  messages: { display: ' {1} / {2}' }  },
                    selectable: 'row',
                    height: 500,
                    change: function(e) {                    
                        var selectedCells = this.select();
  						if( selectedCells.length == 1){  						
                             var selectedCell = this.dataItem( selectedCells );                             
							 selectedUser.userId = selectedCell.userId ;         
							 selectedUser.username = selectedCell.username ;             
							 selectedUser.name = selectedCell.name ;
							 selectedUser.email = selectedCell.email ;
							 selectedUser.creationDate = selectedCell.creationDate ;
							 selectedUser.lastLoggedIn = selectedCell.lastLoggedIn ;         							 
							 selectedUser.formattedLastLoggedIn =  kendo.format("{0:yyyy.MM.dd}",  selectedUser.lastLoggedIn  );							 
							 selectedUser.lastProfileUpdate = selectedCell.lastProfileUpdate ;                 
							 selectedUser.formattedLastProfileUpdate =  kendo.format("{0:yyyy.MM.dd}",  selectedUser.lastProfileUpdate  );                   
							 selectedUser.enabled = selectedCell.enabled ;              
							 selectedUser.nameVisible = selectedCell.nameVisible ;          
							 selectedUser.emailVisible = selectedCell.emailVisible ; 							 
							 if( selectedUser.userId > 0 ){							 	
							 	
							 	// 2. USER DETAILS
							 	kendo.bind($(".tabular"), selectedUser );							
							 	
							 	// 3. USER TABS 	
							 	$('#user-tabs').show().html(kendo.template($('#template').html()));
							 	var user_tabs = $('#user-tabs').find(".tabstrip").kendoTabStrip({
				                      animation: {
								        close: {  duration: 200, effects: "fadeOut" },
								       	open: { duration: 200, effects: "fadeIn" }
				                      },
				                      select : function(e){				                      				                      				                      
				                          if( $( e.contentElement ).find('div').hasClass('groups') ){
				                          	if( ! user_tabs.find(".groups").data("kendoGrid") ){	
												// 3-1 USER GROUP GRID
											    user_tabs.find(".groups").kendoGrid({
				   										dataSource: {
													    	type: "json",
										                    transport: {
										                        read: { url:'${request.contextPath}/secure/list-user-groups.do?output=json', type:'post' },
																destroy: { url:'${request.contextPath}/secure/remove-group-members.do?output=json', type:'post' },
																parameterMap: function (options, operation){
												                    if (operation !== "read" && options.models) {
																 	    return { userId: selectedUser.userId, items: kendo.stringify(options.models)};
										                            }
												                    return { userId: selectedUser.userId }
												                }
										                    },
										                    schema: {
										                    	data: "userGroups",
										                    	model: Group
										                    },
										                    error:handleKendoAjaxError
					                                     },
													    height: 200,
													    scrollable: false,
													    editable: false,
										                columns: [
									                        { field: "groupId", title: "ID", width:40,  filterable: false, sortable: false }, 
									                        { field: "name",    title: "이름",  filterable: true, sortable: true,  width: 100 },
									                       { command:  { text: "삭제", click : function(e){									                       		
									                       		if( confirm("정말로 삭제하시겠습니까?") ){
																	var selectedGroup = this.dataItem($(e.currentTarget).closest("tr"));									                       		
										                       		$.ajax({
																		type : 'POST',
																		url : "/secure/remove-group-members.do?output=json",
																		data : { groupId:selectedGroup.groupId, items: '[' + kendo.stringify( selectedUser ) + ']'  },
																		success : function( response ){									
																	        $('#user-group-grid').data('kendoGrid').dataSource.read();				
																		},
																		error:handleKendoAjaxError,
																		dataType : "json"
																	});								                       		
									                       		}
									                       }},  title: "&nbsp;", width: 100 }	
										                ],
										                dataBound:function(e){
										                
										                }
											    });
  
				                          	}				                          	
				                          } else if( $( e.contentElement ).find('div').hasClass('props') ){
				                          	if( !user_tabs.find(".props").data("kendoGrid") ){	
				                          	}		                          	
				                          }
				                      } 
				                });		
				                
				                // 3-1 USER PROPERTY GRID				         
				                user_tabs.find(".props").kendoGrid({
								     dataSource: {
										transport: { 
											read: { url:'${request.contextPath}/secure/get-user-property.do?output=json', type:'post' },
										    create: { url:'${request.contextPath}/secure/update-user-property.do?output=json', type:'post' },
										    update: { url:'${request.contextPath}/secure/update-user-property.do?output=json', type:'post'  },
										    destroy: { url:'${request.contextPath}/secure/delete-user-property.do?output=json', type:'post' },
										 	parameterMap: function (options, operation){
										 		if (operation !== "read" && options.models) {
				                                	return { userId: selectedUser.userId, items: kendo.stringify(options.models)};
				                                } 
						                        return { userId: selectedUser.userId }
						                    }
										 },						
										 batch: true, 
										 schema: {
					                            data: "targetUserProperty",
					                            model: {
					                                id:"name",
					                                fields: {
					                                    name: { type: "string" },
					                                    value: { type: "string" }
					                                }
					                            }
					                     },
					                     error:handleKendoAjaxError
								     },
								     columns: [
								         { title: "속성", field: "name" },
								         { title: "값",   field: "value" },
								         { command:  { name: "destroy", text:"삭제" },  title: "&nbsp;", width: 100 }
								     ],
								     autoBind: true, 
								     pageable: false,
								     scrollable: true,
								     height: 200,
						             editable: {
						                	update: false,
						                	destroy: true,
						                	confirmation: "선택하신 프로퍼티를 삭제하겠습니까?"	
						             },
								     toolbar: [
								         { name: "create", text: "추가" },
				                         { name: "save", text: "저장" },
				                         { name: "cancel", text: "취소" }
									 ],				     
								     change: function(e) {  
								     }
							    });
							 }			     
                        }else{
                            selectedUser = new User ({});
                        }                       
					},
					dataBound: function(e){		
						 var selectedCells = this.select();
						 if(selectedCells.length == 0 ){											      
						     selectedUser = new User ({});
						 }
					}
                });                 
            }	
        }]);      
        </script>
    </head>
    <body>  
	<!-- START MAIN HEADER  -->   
	<header>
		<div class="row">
			<div class="twelve columns">
				<h1>사용자 관리</h1>
				<h4>사용자 관리 프로그램</h4>
			</div>
		</div>
	</header>
	<!-- END MAIN HEADER  -->   	            
	<!-- START MAIN CONTENT  -->   
    <section class="row" style="padding-top:10px;" >
	    <div class="seven columns"><div id="user-grid"></div></div>
	    <div class="five columns">
	        <div class="row">	        
		        <table class="tabular" width="100%">
					<tr>
					    <th rowspan="6" align="center"><a href="#"><img src="http://placehold.it/100x150"></a></th>
					    <td width="110px">이름</td> 
					    <td width="120px"><span data-bind="text:name"></span></td>
				    </tr>
					<tr>
				    	<td>마지막 방문일</td> 
				    	<td><span data-bind="text: formattedLastLoggedIn"></span></td> 
				    </tr>
					<tr>
				    	<td>계정사용</td> 
				    	<td><input type="checkbox"  name="enabled"  data-bind="checked: enabled" /></td>
				    </tr>
					<tr>
				    	<td>이름공개</td> 
				    	<td><input type="checkbox"  name="nameVisible"  data-bind="checked: nameVisible" /></td>
				    </tr>
					<tr>
				    	<td>메일공개</td> 
				    	<td><input type="checkbox"  name="emailVisible"  data-bind="checked: emailVisible" /></td>
				    </tr>
					<tr>
				    	<td colspan=2><a class="k-button">변경</a>&nbsp;<a class="k-button">비밀번호변경</a></td>
				    </tr>				    
				</table>	        		
	        </div>
	        <div class="row">
	        	<div id="user-tabs">       	
	        	</div>
	        </div>
			<script type="text/x-kendo-template" id="template">		
				<div class="tabstrip">
	                <ul>
	                    <li class="k-state-active">
	                        프로퍼티
	                    </li>
	                    <li>
	                       그룹
	                    </li>
	                    <li>
	                       권한
	                    </li>	                    
	                </ul>
	                <div>
	                	<div id="user-props-grid" class="props"></div>
	                	<p/>
	                	<div class="alert-box success">프로퍼티는 저장 버튼을 클릭하여야 최종 반영됩니다.</div>
	                </div>
	                <div><div id="user-group-grid" class="groups"></div></div>			
	                <div><div id="user-role-grid" class="roles"></div></div>				                
	            </div>
	        </script>	        	    
	    </div>
	</section>	
	<!-- END MAIN CONTENT  -->   
	<!-- START Breadcrumbs -->
	<section class="row">
	    <div class="twelve columns">
	        <hr style="margin-top:10px;margin-bottom:10px;" />
			<ul class="breadcrumbs">
			  <li><a href="${request.contextPath}/main.do">홈</a></li>
			  <li class="unavailable"><a href="#">시스템</a></li>
			  <li class="current"><a href="#">사용자 관리</a></li>
			</ul>
	    </div>
	</section>
	<!-- End Breadcrumbs -->
    </body>
</html>
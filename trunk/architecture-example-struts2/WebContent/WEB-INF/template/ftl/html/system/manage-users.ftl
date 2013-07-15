<#ftl encoding="UTF-8"/>
<html decorator="secure">
    <head>
        <title>사용자 관리</title>
        <script type="text/javascript">                
        yepnope([{
            load: [ 
			'${request.contextPath}/js/jquery/1.9.1/jquery.min.js',			
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
       	    '${request.contextPath}/js/kendo/kendo.web.min.js',
       	    '${request.contextPath}/js/kendo/kendo.ko_KR.js',
       	    '${request.contextPath}/js/common/common.models.js',
       	    '${request.contextPath}/js/common/common.ui.js'],
            complete: function() {       
                    
                kendo.culture("ko-KR");
                
				// ACCOUNTS LOAD		
				var currentUser = new User({});
				var accounts = $("#accounts-panel").kendoAccounts({
					authenticate : function( e ){
						currentUser = e.token;						
					}
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
                        },
                        dataBound: function(e){
                        }
                });	                
				$("#company").data("kendoDropDownList").readonly();		
								
				$("#menu").kendoMenu({
						select: function(e){							
							var action = $(e.item).attr('action') ;
							if( action != '#' ){
								$("form[name='fm1']").attr("action", action ).submit(); 
							}
						}						
				}).css("border-width", "1px 1px 0px");;
				
				$("#menu").show();	
					
				$("#go-comapny-btn").click( function(){
					$("form[name='fm1']").attr("action", "main-company.do" ).submit(); 
				}); 
				
	            var selectedUser = new User ({});	
		        // 1. USER GRID 		        
				var user_grid = $("#user-grid").kendoGrid({
                    dataSource: {
                    	serverFiltering: true,
                        transport: { 
                            read: { url:'${request.contextPath}/secure/list-user.do?output=json', type: 'POST' },
	                        parameterMap: function (options, type){
	                            return { startIndex: options.skip, pageSize: options.pageSize,  companyId: $("#company").data("kendoDropDownList").value() }
	                        }
                        },
                        schema: {
                            total: "totalUserCount",
                            data: "users",
                            model: User
                        },
                        error:handleKendoAjaxError,
                        batch: false,
                        pageSize: 15,
                        serverPaging: true,
                        serverFiltering: false,
                        serverSorting: false
                    },
                    columns: [
                        { field: "userId", title: "ID", width:50,  filterable: false, sortable: false , headerAttributes: { "class": "table-header-cell", style: "text-align: center" } }, 
                        { field: "username", title: "아이디", width: 100, headerAttributes: { "class": "table-header-cell", style: "text-align: center" } }, 
                        { field: "name", title: "이름", width: 100 , headerAttributes: { "class": "table-header-cell", style: "text-align: center" }}, 
                        { field: "email", title: "메일", headerAttributes: { "class": "table-header-cell", style: "text-align: center" } },
                        { field: "enabled", title: "사용여부", width: 90, headerAttributes: { "class": "table-header-cell", style: "text-align: center" } },
                        { field: "creationDate",  title: "생성일", width: 120,  format:"{0:yyyy/MM/dd}", headerAttributes: { "class": "table-header-cell", style: "text-align: center" } },
                        { field: "modifiedDate", title: "수정일", width: 120,  format:"{0:yyyy/MM/dd}", headerAttributes: { "class": "table-header-cell", style: "text-align: center" } } ],         
                    filterable: true,
                    sortable: true,
                    pageable: { refresh:true, pageSizes:false,  messages: { display: ' {1} / {2}' }  },
                    selectable: 'row',
                    height: 600,
                    toolbar: [
					 	{ name: "create-user", text: "새로운 사용자 생성하기", className: "createUserCustomClass" } ],
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
							selectedUser.properties = selectedCell.properties;							 							 
							selectedUser.company = $("#company").data("kendoDropDownList").dataSource.get(  $("#company").data("kendoDropDownList").value()  );
							var observable = new kendo.data.ObservableObject( selectedUser ); 
							
							 if( selectedUser.userId > 0 ){						
							 	 	
							 	// 2. USER DETAILS
							 	 $("#splitter").data("kendoSplitter").expand("#datail_pane");
							 	 
							 	// 3. USER TABS 	
							 	$('#user-details').show().html(kendo.template($('#template').html()));							 	
	                            kendo.bind($(".details"), selectedUser );      								
							
							 	if( selectedUser.properties.imageId ){
							 		var photoUrl = '${request.contextPath}/secure/view-image.do?width=200&height=300&imageId=' + selectedUser.properties.imageId ;
							 	 	$('#user-photo').attr( 'src', photoUrl );
							 	}								
								$("#files").kendoUpload({
								 	multiple : false,
								 	showFileList : false,
								    localization:{ select : '사진변경' , dropFilesHere : '업로드할 이미지를 이곳에 끌어 놓으세요.' },
								    async: {
									    saveUrl:  '${request.contextPath}/secure/save-user-image.do?output=json',							   
									    autoUpload: true
								    },
								    upload: function (e) {								         
								         var imageId = -1;
								         if( selectedUser.properties.imageId ){
								         	imageId = selectedUser.properties.imageId
								         }
								    	 e.data = { userId: selectedUser.userId , imageId:imageId  };									    								    	 		    	 
								    },
								    success : function(e) {								    
								    	if( e.response.targetUserImage ){
								    		selectedUser.properties.imageId = e.response.targetUserImage.imageId;
								    		var photoUrl = '${request.contextPath}/secure/view-image.do?width=200&height=300&imageId=' + selectedUser.properties.imageId ;
							 	 			$('#user-photo').attr( 'src', photoUrl );
								    	}				
								    }					   
								});	                    
					            $('#change-password-btn').bind( 'click', function(){
					                $('#change-password-window').kendoWindow({
				                            width: "400px",
				                            minWidth: "300px",
				                            minHeight: "250px",
				                            title: "패스워드 변경",
				                            modal: true,
				                            visible: false
				                        });
				                    $('#change-password-window').data("kendoWindow").center();        
				                    $('#password2').focus();                
					            	$('#change-password-window').data("kendoWindow").open();	            	
					            });					            
					            $('#do-change-password-btn').bind( 'click', function(){	            	
					            	var doChangePassword = true ;	            	
					            	if( $('#password2').val().length < 6 ){
					            		alert ('패스워드는 최소 6 자리 이상으로 입력하여 주십시오.') ;	     
					            		doChangePassword = false ;
					            		$('#password2').val("");        
					            		$('#password3').val("");           		
					            		$('#password2').focus();   
					            		return false;
					            	}					            
				                   	if( doChangePassword && $('#password2').val() != $('#password3').val() ){
				                   		doChangePassword = false;
				                   	    alert( '패스워드가 같지 않습니다. 다시 입력하여 주십시오.' );      
				                   	    $('#password3').val("");
				                   	    $('#password3').focus();               
				                   	    return false; 	   
				                   	} 				
									if(doChangePassword) {
				                   	    selectedUser.password = $('#password2').val();                   	    
										$.ajax({
												type : 'POST',
												url : "${request.contextPath}/secure/update-user.do?output=json",
												data : { userId:selectedUser.userId, item: kendo.stringify( selectedUser ) },
												success : function( response ){	
												    $('#user-grid').data('kendoGrid').dataSource.read();	
												},
												error:handleKendoAjaxError,
												dataType : "json"
											});	
										selectedUser.password = '' ;                   	    	
				                   	}            	                   	
					            } ); 
				                $('#update-user-btn').bind('click' , function(){
									$.ajax({
										type : 'POST',
										url : "${request.contextPath}/secure/update-user.do?output=json",
										data : { userId:selectedUser.userId, item: kendo.stringify( selectedUser ) },
										success : function( response ){									
										    $('#user-grid').data('kendoGrid').dataSource.read();	
										},
										error: handleKendoAjaxError,
										dataType : "json"
									});										
									if(visible){
										slide.reverse();						
										visible = false;		
										$("#detail-panel").hide();				
									}
				                }); 	                							 	
							 	//kendo.bind($(".tabular"), selectedUser );							 					
							 	var user_tabs = $('#user-details').find(".tabstrip").kendoTabStrip({
									animation: {
								    	close: {  duration: 200, effects: "fadeOut" },
								       	open: { duration: 200, effects: "fadeIn" }
				                    },
									select : function(e){			
										
										// TAB - ATTACHMENT TAB
										if( $( e.contentElement ).find('div').hasClass('attachments') ){					   
											if( ! $("#attach-upload").data("kendoUpload") ){	
												$("#attach-upload").kendoUpload({
					                      			multiple : true,
					                      			showFileList : true,
					                      			localization : { select: '파일 선택', remove:'삭제', dropFilesHere : '업로드할 파일을 이곳에 끌어 놓으세요.' , 
					                      				uploadSelectedFiles : '파일 업로드',
					                      				cancel: '취소' 
					                      			 },
					                      			 async: {
													    saveUrl:  '${request.contextPath}/secure/save-user-attachments.do?output=json',							   
													    autoUpload: false
												    },
												    upload:  function (e) {		
												    	e.data = { userId: selectedUser.userId };		
												    },
												    success : function(e) {	
												    	$('#attach-grid').data('kendoGrid').dataSource.read(); 
												    }
					                      		});				
											}				                   
											   		         
											if( ! $("#attach-grid").data("kendoGrid") ){	
												$("#attach-grid").kendoGrid({
							                        dataSource: {
							                        	autoSync: true,
							                            type: 'json',
							                            transport: {
							                                read: { url:'${request.contextPath}/secure/get-user-attachements.do?output=json', type: 'POST' },		
							                                destroy: { url:'${request.contextPath}/secure/delete-user-attachment.do?output=json', type:'POST' },						                                
									                        parameterMap: function (options, operation){
									                        	 if (operation != "read" && options) {										                        								                       	 	
									                        	 	return { userId: selectedUser.userId, attachmentId :options.attachmentId };									                            	
									                            }else{
									                            	return { userId: selectedUser.userId };
									                            }
									                        }								                         
							                            },
							                            error:handleKendoAjaxError,
							                            schema: {
							                            	model: Attachment,
							                            	data : "targetUserAttachments"
							                            }
							                        },
							                        height:300,
							                        scrollable:  true,
							                        sortable: true,
							                        editable: {
									                	update: false,
									                	destroy: true,
									                	confirmation: "선택하신 첨부파일을 삭제하겠습니까?"
									                },
							                        columns: [{
							                        		title: "ID",
							                        		width: 50,
							                                field:"attachmentId",
							                                filterable: false
							                            },
							                            {
							                                field: "name",
							                                title: "이름",
							                                template: '#= name  #',
							                                width: 150
							                            },
							                             {
							                                field: "contentType",
							                                title: "유형",
							                                width: 80 /**
							                            }, {
							                                field: "modifiedDate",
							                                title: "수정일",
							                                width: 80,
							                                format: "{0:yyyy/MM/dd}" **/
							                            },
							                            { command: [ { name: "download", text: "미리보기" ,click: function(e)  {
									                            	var tr = $(e.target).closest("tr"); 
														          	var item = this.dataItem(tr);
							                            			if(! $("#download-window").data("kendoWindow")){
							                            				$("#download-window").kendoWindow({
							                            					actions: ["Minimize", "Maximize", "Close"],
							                            					minHeight : 200,
							                            					maxHeight : 500,
							                            					minWidth :  200,
							                            					maxWidth :  600,
							                            					modal: true,
							                            					visible: false
							                            				});
							                            			}
							                            			
							                            			var downloadWindow = $("#download-window").data("kendoWindow");
							                            			downloadWindow.title( item.name );							                            			
							                            		 	var template = kendo.template($("#download-window-template").html());
							                            			downloadWindow.content( template(item) );
							                            			$("#download-window").closest(".k-window").css({
																	     top: 65,
																	     left: 10,
																	 });						                            			
							                            			downloadWindow.open();
							                            		}
							                            	}, 
							                            	{ name: "destroy", text: "삭제" } ],  title: "&nbsp;", width: 160  }					                            
							                        ],
							                        dataBound: function(e) {
							                        }
							                    });
							                    $("#attach-grid").attr('style', '');
											}
				                    	}
				                    	
										// TAB - GROUP TAB --------------------------------------------------------------------------
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
												                    return { userId: selectedUser.userId };
												                }
										                    },
										                    schema: {
										                    	data: "userGroups",
										                    	model: Group
										                    },
										                    error:handleKendoAjaxError
					                                     },
													    scrollable: true,
													    height:200,
													    editable: false,
										                columns: [
									                        { field: "groupId", title: "ID", width:40,  filterable: false, sortable: false }, 
									                        { field: "displayName",    title: "이름",   filterable: true, sortable: true,  width: 100 },
									                        { command:  { text: "삭제", click : function(e){									                       		
									                       		if( confirm("정말로 삭제하시겠습니까?") ){
																	var selectedGroup = this.dataItem($(e.currentTarget).closest("tr"));									                       		
										                       		$.ajax({
																		type : 'POST',
																		url : "/secure/remove-group-members.do?output=json",
																		data : { groupId:selectedGroup.groupId, items: '[' + kendo.stringify( selectedUser ) + ']'  },
																		success : function( response ){									
																	        $('#user-group-grid').data('kendoGrid').dataSource.read();
																	        $('#group-role-selected').data("kendoMultiSelect").dataSource.read();
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
											    $("#user-group-grid").attr('style','');	    
				                          	}	
				                        // TAB 3 - PROPS  											
										} else if( $( e.contentElement ).find('div').hasClass('props') ){
											if( !user_tabs.find(".props").data("kendoGrid") ){	
				                          					                          	
											}	
										// TAB - ROLES --------------------------------------------------------------		                          	
										} else if( $( e.contentElement ).find('div').hasClass('roles') ){										
											// SELECTED GROUP ROLES
											if( !$('#group-role-selected').data('kendoMultiSelect') ){
												$('#group-role-selected').kendoMultiSelect({
				                                    placeholder: "NONE",
									                dataTextField: "name",
									                dataValueField: "roleId",
									                dataSource: {
									                    transport: {
									                        read: {
							                                    url: '${request.contextPath}/secure/get-user-group-roles.do?output=json',
																dataType: "json",
																type: "POST",
																data: { userId: selectedUser.userId }
									                        }
									                    },
									                    schema: { 
						                            		data: "userGroupRoles",
						                            		model: Role
						                        		}
									                },
						                        	error:handleKendoAjaxError,
						                        	dataBound: function(e) {
						                        		var multiSelect = $("#group-role-selected").data("kendoMultiSelect");
						                        		var selectedRoleIDs = "";
						                        		$.each(  multiSelect.dataSource.data(), function(index, row){  
						                        			if( selectedRoleIDs == "" ){
						                        			    selectedRoleIDs =  selectedRoleIDs + row.roleId ;
						                        			}else{
						                        				selectedRoleIDs = selectedRoleIDs + "," + row.roleId;
						                        			}
						                        		} );			                        		
						                        		multiSelect.value( selectedRoleIDs.split( "," ) );
						                        		multiSelect.readonly();		
						                        	}
									            });	
											}									    
											// SELECT USER ROLES
											if( !$('#user-role-select').data('kendoMultiSelect') ){											
												var selectedRoleDataSource = new kendo.data.DataSource({
													transport: {
										            	read: { 
										            		url:'${request.contextPath}/secure/get-user-roles.do?output=json', 
										            		dataType: "json", 
										            		type:'POST',
										            		data: { userId: selectedUser.userId }
												        }  
												    },
												    schema: {
									                	data: "userRoles",
									                    model: Role
									                },
									                error:handleKendoAjaxError,
									                change: function(e) {                
						                        		var multiSelect = $("#user-role-select").data("kendoMultiSelect");
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
				                               												
												$('#user-role-select').kendoMultiSelect({
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
						                        		var multiSelect = $("#user-role-select").data("kendoMultiSelect");			                        		
						                        		var list = new Array();			                        		                  		
						                        		$.each(multiSelect.value(), function(index, row){  
						                        			var item =  multiSelect.dataSource.get(row);
						                        			list.push(item);			                        			
						                        		});			                        		
						                        		multiSelect.readonly();						                        		
							 							$.ajax({
												            dataType : "json",
															type : 'POST',
															url : "${request.contextPath}/secure/update-user-roles.do?output=json",
															data : { userId: selectedUser.userId, items: kendo.stringify( list ) },
															success : function( response ){		
																// need refresh ..
															},
															error:handleKendoAjaxError
														});												
														multiSelect.readonly(false);
						                        	}
									            });
											}										
										}
									} 
				                });
				                
				                // GROUP SELECT COMBO BOX
								var company_combo = $("#company-combo").kendoComboBox({
									autoBind: false,
									placeholder: "회사 선택",
			                        dataTextField: "displayName",
			                        dataValueField: "companyId",
								    dataSource: $("#company").data("kendoDropDownList").dataSource 
								});			
													
								$("#company-combo").data("kendoComboBox").value( 
								 	$("#company").data("kendoDropDownList").value() 
								 );	
								 
								$("#company-combo").data("kendoComboBox").readonly();
																
								$("#group-combo").kendoComboBox({
									autoBind: false,
									placeholder: "그룹 선택",
			                        dataTextField: "displayName",
			                        dataValueField: "groupId",
			                        cascadeFrom: "company-combo",			                       
								    dataSource:  {
										type: "json",
									 	serverFiltering: true,
										transport: {
											read: { url:'${request.contextPath}/secure/list-company-group.do?output=json', type:'post' },
											parameterMap: function (options, operation){											 	
											 	return { companyId:  options.filter.filters[0].value };
											}
										},
										schema: {
											data: "companyGroups",
											model: Group
										},
										error:handleKendoAjaxError
									}
								});	
								
								// ADD USER TO SELECTED GROUP 
								$("#add-to-member-btn").click( function ( e ) {
									 $.ajax({
							            dataType : "json",
										type : 'POST',
										url : "${request.contextPath}/secure/add-group-member.do?output=json",
										data : { groupId:  $("#group-combo").data("kendoComboBox").value(), item: kendo.stringify( selectedUser ) },
										success : function( response ){																		    
											 $("#user-group-grid").data("kendoGrid").dataSource.read();
											 $('#group-role-selected').data("kendoMultiSelect").dataSource.read();
										},
										error:handleKendoAjaxError
									});	
								} );
																				                
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
					                            model: Property
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
						                	update: true,
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
							    $("#user-props-grid").attr('style','');	    
							 }			     
                        }else{
                            selectedUser = new User ({});
                        }                       
					},
					dataBound: function(e){		
						 var selectedCells = this.select();
						 if(selectedCells.length == 0 ){								      
						     selectedUser = new User ({});
						     kendo.bind($(".tabular"), selectedUser );	
							$("#user-details").hide(); 	 					     
						 }
					}
                }).css("border", "0px").data('kendoGrid');
               
                
                // SPLITTER LAYOUT
				var splitter = $("#splitter").kendoSplitter({
					orientation: "horizontal",
					panes: [
						{ scrollable : true, min: 500},
						{ collapsed: true, collapsible: true, scrollable : true, min: 550 }
					]
				});
				
            }	
        }]);      
        
        </script>
		<style>			
			#splitter {
				height : 600px;
			}
			
			#datail_pane {
				background-color : #F5F5F5;
			}
			
 			#user-details .k-content 
		    {
		        height: "100%";
		    }			
			 #user-details .k-content 
		    {
		        height: "100%";
		        overflow: auto;
		    }
		    
		</style>
    </head>
	<body>
		<!-- START HEADER -->
		<!--
		<header>
			<div class="row full-width layout">
				<div class="large-12 columns">
					<div class="big-box topless bottomless">
					<h1><a class="open" href="#">Menu</a>사용자관리</h1>
					<h4 class="desc">사용자을 관리하기 위한 기능을 제공합니다.</h4>
					</div>
				</div>
			</div>
		</header>
		-->
		<!-- END HEADER -->
		<!-- START MAIN CONTNET -->
		<section id="mainContent">		
			<div class="row full-width">
				<div class="large-6 columns" >
					<div class="k-content">
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
								<li action="main-group.do">그룹</li>     
							</ul>  	
						</div>		
				</div>
				<div class="large-6 columns" >
					<div class="k-content">								
				</div>				
			</div>
			<div class="row full-width">
				<div class="large-12 columns" >				
					<div id="splitter">
						<div id="list_pane">
							<div class="row full-width layout">
								<div class="large-12 columns" >
									
								</div>
							</div>			
							<div class="row full-width layout">			
								<div class="large-12 columns" >
									<div id="user-grid"></div>
								</div>
							</div>		
						</div>
						<div id="datail_pane">
							<div class="row full-width layout">			
								<div class="large-12 columns" >
									<div id="user-details"></div>
								</div>				
							</div>
						</div>
					</div>				
				</div>
			</div>				
			<form name="fm1" method="POST" accept-charset="utf-8">
				<input type="hidden" name="companyId"  value="${action.companyId}" />
			</form>	
		</section>	
		<div id="change-password-window" style="display:none; width:500px;">
			<form>
				<p>
					    	6~16자의 영문 대소문자, 숫자, 특수문자를 조합하여
							사용하실 수 있습니다.
							생년월일, 전화번호 등 개인정보와 관련된 숫자,
							연속된 숫자와 같이 쉬운 비밀번호는 다른 사람이 쉽게
							알아낼 수 있으니 사용을 자제해 주세요.
							이전에 사용했던 비밀번호나 타 사이트와는 다른 비밀번호를
							사용하고, 비밀번호는 주기적으로 변경해주세요.
							<div class="alert-box alert">비밀번호에 특수문자를 추가하여 사용하시면
							기억하기도 쉽고, 비밀번호 안전도가 높아져 도용의 위험이
							줄어듭니다.	</div>    	
					</p>
					    	<table class="tabular" width="100%">	    	
								<tr>
						    		<td>새 비밀번호</td> 
						    		<td><input type="password" id="password2" name="password2" class="k-textbox"  placeholder="비밀번호" required validationMessage="비밀번호를 입력하여 주세요." /></td>
						    	</tr>	
								<tr>
						    		<td>새 비밀번호 확인</td> 
						    		<td><input type="password" id="password3" name="password3" class="k-textbox"  placeholder="비밀번호" required validationMessage="비밀번호를 입력하여 주세요." /></td>
						    	</tr>							    	
					    	</table>				
					    	<table>
					    		<tr>
					    			<td>
					    				<button id="do-change-password-btn" class="k-button">확인</button>
										<span style="padding-left:5px;"></span>
										<button class="k-button" type="reset">다시입력</button></div>	
					    			</td>
					    		</tr>
					    	</table>									
			</form>
		</div>  
  		<div id="download-window"></div>    
		
		<div id="pageslide" style="left: -300px; right: auto; display: none;">	
			<div id="accounts-panel"></div>
		</div>
						  
  		<!-- END MAIN CONTNET -->
		<!--
		<footer>  
		</footer>    
		-->
		<script id="download-window-template" type="text/x-kendo-template">				
			#if (contentType.match("^image") ) {#
				<img src="${request.contextPath}/secure/view-attachment.do?attachmentId=#= attachmentId #" style="border:0;"/>
			# } else { #
			<table class="tabular" width="100%">
			  <thead>
			    <tr>
			      <th>이름</th>
			      <th>유형</th>
			      <th>크기</th>
			      <th>&nbsp;</th>
			    </tr>
			  </thead>
			  <tbody>
			    <tr>
			      <td  width="200">#= name #</td>
			      <td  width="150">#= contentType #</td>
			      <td  width="150">#= size # 바이트</td>
			      <td  width="150"><a class="k-button" href="${request.contextPath}/secure/download-attachment.do?attachmentId=#= attachmentId #" >다운로드</a></td>
			    </tr>
			  </tbody>
			</table>				
			# } #  		
		</script>	
		<script type="text/x-kendo-template" id="template">

				<div class="big-box">
					<div class="tabstrip">
						<ul>
							<li class="k-state-active">
							기본정보
							</li>	                	
							<li>
							프로퍼티
							</li>
							<li>
							그룹
							</li>
							<li>
							권한
							</li>	                   
							<li>
							첨부파일
							</li>	   
						</ul>	          
						<div class="full-height">
							<div class="row full-width layout">
								<div class="large-3 columns">
									<div class="big-box">
										<a href="\\#"  class="th"><img id="user-photo" src="http://placehold.it/100x150" border="0" /></a>
										<input name="uploadImage" id="files" type="file" />
									</div>								
								</div>
								<div class="large-9 columns details">
									<fieldset>
										<legend>기본정보</legend>
										<div class="row">
										      <div class="small-6 columns">
										        <label>이름</label>
										        <input type="text" class="k-textbox" placeholder="large-6.columns" data-bind="value:name">
										      </div>
										</div>      
										<div class="row">      
										      <div class="small-6 columns">
										        <label>메일</label>
										        <input type="text" class="k-textbox" placeholder="large-6.columns" data-bind="value:email">
										      </div>
										</div>
									</fieldset>
									<fieldset>
										<legend>옵션</legend>																		
										<div class="row">
										      <div class="small-3 columns">
										        <label class="">이름공개</label>
										      </div>
										      <div class="small-3 columns">										      
										      	<input type="checkbox" name="nameVisible"  data-bind="checked: nameVisible" />										        					        
										      </div>
										      <div class="small-3 columns">
										        <label class="">메일공개</label>				        
										      </div>		
										      <div class="small-3 columns">
										        <input type="checkbox"  name="emailVisible"  data-bind="checked: emailVisible" />						        
										      </div>											      								      									      
										</div>			
										<div class="row">
										      <div class="small-3 columns">
										        <label class="">계정사용</label>
										      </div>
										      <div class="small-3 columns">
										      	<input type="checkbox"  name="enabled"  data-bind="checked: enabled" />											        					        
										      </div>			
										      <div class="small-3 columns"></div>
										      <div class="small-3 columns"></div>											      						      								      									      
										</div>																																																
									</fieldset>
									<div class="alert-box secondary">
										<div class="row">
										      <div class="large-6 columns">
										        개인정보 수정일							        
										      </div>
										      <div class="large-6 columns">
										        <span data-bind="text: formattedLastProfileUpdate"></span>		 	
										      </div>										      
										</div>					
										<div class="row">
										      <div class="large-6 columns">
										        마지막 방문일						        
										      </div>
										      <div class="large-6 columns">
										        <span data-bind="text: formattedLastLoggedIn"></span>
										      </div>										      
										</div>							
									</div>									
								</div>
							</div>
							<div class="row full-width">
								<div class="small-9 small-offset-3 columns">
										<button id="update-user-btn" class="k-button">정보 변경</button>&nbsp;
										<button id="change-password-btn" class="k-button right">비밀번호변경</button>
								</div>
							</div>							
	                </div>	                	      
	        		<div class="full-height">
        				<div id="user-props-grid" class="props" style="height:0px;"/>
        				<div class="box leftless rightless bottomless">
	                		<div class="alert-box secondary">프로퍼티는 저장 버튼을 클릭하여야 최종 반영됩니다.</div>
	                	</div>
	                </div>
	                <div class="full-height">
	                    <div class="alert-box secondary">
		                    <input id="company-combo" style="width: 180px" />
		                    <input id="group-combo" style="width: 180px" />
		                    <button id="add-to-member-btn" class="k-button">그룹 맴버로 추가</button>
		                    <br/><br/>멤버로 추가하려면 리스트 박스에서 그룹을 선택후 "그룹 멤버로 추가" 버튼을 클릭하세요.
	                    </div>
	                	<div id="user-group-grid" class="groups"></div>
	                </div>			
	                <div class="full-height" style="height:400px;">
	                	<div class="roles">
	                		<div class="row full-width layout">
	                			<div class="large-12 columns">	
	                				<div class="big-box">
	                				<div class="alert-box secondary">다음은 그룹에 부여된 롤입니다. 그룹에서 부여된 롤은 그룹 관리에서 변경할 수 있습니다.</div>
	                				<div id="group-role-selected"></div>
	                				</div>
	                			</div>
	                		</div>
	                		<div class="row full-width layout">
	                			<div class="large-12 columns">	
	                				<div class="big-box bottomless">
	                					<div class="alert-box">다음은 사용자에게 직접 부여된 롤입니다. 그룹에서 부여된 롤을 제외한 롤들만 아래의 선택박스에서 사용자에게 부여 또는 제거하세요.</div>	                				
	                				</div>
	                			</div>
	                		</div>	  
							<div class="row full-width layout">
	                			<div class="large-12 columns">	
	                				<div class="big-box topless">
	                					<div id="user-role-select"></div>                				
	                				</div>
	                			</div>
	                		</div>	
						</div>	
	                </div>			
	                <div class="full-height">	                	    
	                    <div class="alert-box secondary">
		                    <input id="attach-upload" name="uploadFile" type="file" />
		                    <p/>
		                    업로드할 파일을 "선택" 버튼에  이곳에 끌어 놓거나,  "선택" 버튼을 클릭하여 업로드할 파일들을 선택한 다음 "업로드" 버튼을 클릭하세요.
	                    </div>
	                	<div id="attach-grid" class="attachments"></div>
	                </div>	
				</div>
		</script>
		<!-- 공용 템플릿 -->
		<#include "/html/common-templates.ftl" >		
		<!-- END MAIN CONTENT  -->
    </body>
</html>
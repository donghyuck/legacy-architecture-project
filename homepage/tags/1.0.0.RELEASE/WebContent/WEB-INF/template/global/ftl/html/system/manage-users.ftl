<#ftl encoding="UTF-8"/>
<html decorator="secure">
    <head>
	<#compress>
        <title>사용자 관리</title>
        <script type="text/javascript">                
        yepnope([{
            load: [ 
			'css!${request.contextPath}/styles/font-awesome/4.0.3/font-awesome.min.css',			
			'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',
       	    '${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',      	    
			'${request.contextPath}/js/kendo.extension/kendo.ko_KR.js',			 
			'${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js', 
			'${request.contextPath}/js/bootstrap/3.0.3/bootstrap.min.js',       	    
       	    '${request.contextPath}/js/common/common.models.js',
       	    '${request.contextPath}/js/common/common.api.js',       	    
       	    '${request.contextPath}/js/common/common.ui.js',
      		'${request.contextPath}/js/common/common.ui.system.js'],        	   
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
				var companyPlaceHolder = new Company({ companyId: ${action.targetCompany.companyId} });
				$("#navbar").data("companyPlaceHolder", companyPlaceHolder);				
				var topBar = $("#navbar").extNavbar({
					template : $("#top-navbar-template").html(),
					items : [{ 
						name:"companySelector", 
						selector: "#companyDropDownList", 
						value: ${action.user.companyId}, 
						change : function(data){
							data.copy(companyPlaceHolder);
							// kendo.bind($("#company-info"), companyPlaceHolder );
						}	
					}]
				});
		
				// 4. CONTENT
				common.ui.handleButtonActionEvents(
					$("button.btn-control-group"), 
					{event: 'click', handlers: {
						company : function(e){
							$("form[name='fm1'] input").val(companyPlaceHolder.companyId);
							$("form[name='fm1']").attr("action", "main-company.do" ).submit(); 						
						},
						group : function(e){
							$("form[name='fm1'] input").val(companyPlaceHolder.companyId);
							$("form[name='fm1']").attr("action", "main-group.do" ).submit(); 						
						}, 	
						site : function(e){
							$("form[name='fm1'] input").val(companyPlaceHolder.companyId);
							$("form[name='fm1']").attr("action", "main-site.do" ).submit(); 						
						}, 							
						addUser : function(e){
							alert("add user");				
						},
						top : function(e){					
							$('html,body').animate({ scrollTop:  0 }, 300);
						}  						 
					}}
				);

				$("#user-grid").data("userPlaceHolder", new User());
				// 1. USER GRID 		        
				var user_grid = $("#user-grid").kendoGrid({
                    dataSource: {
                    	serverFiltering: true,
                        transport: { 
                            read: { url:'${request.contextPath}/secure/list-user.do?output=json', type: 'POST' },
	                        parameterMap: function (options, type){
	                            return { startIndex: options.skip, pageSize: options.pageSize,  companyId: companyPlaceHolder.companyId }
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
                        { field: "userId", title: "ID", width:50,  filterable: false, sortable: false , headerAttributes: { "class": "table-header-cell", style: "text-align: center" }, locked: true, lockable: false}, 
                        { field: "username", title: "아이디", width: 150, headerAttributes: { "class": "table-header-cell", style: "text-align: center"}, locked: true  }, 
                        { field: "name", title: "이름", width: 150 , headerAttributes: { "class": "table-header-cell", style: "text-align: center" }}, 
                        { field: "email", title: "메일", width: 200, headerAttributes: { "class": "table-header-cell", style: "text-align: center" } },
                        { field: "enabled", title: "사용여부", width: 120, headerAttributes: { "class": "table-header-cell", style: "text-align: center" } },
                        { field: "creationDate",  title: "생성일", width: 120,  format:"{0:yyyy.MM.dd}", headerAttributes: { "class": "table-header-cell", style: "text-align: center" } },
                        { field: "modifiedDate", title: "수정일", width: 120,  format:"{0:yyyy.MM.dd}", headerAttributes: { "class": "table-header-cell", style: "text-align: center" } } ],         
                    filterable: true,
                    sortable: true,
                    resizable: true,
                    pageable: { refresh:true, pageSizes:false,  messages: { display: ' {1} / {2}' }  },
                    selectable: 'row',
                    height: '100%',
                    /*toolbar: [
					 	{ name: "create-user", text: "새로운 사용자 생성하기", className: "createUserCustomClass" } ],*/
                    change: function(e) {                    
                        var selectedCells = this.select();                 
  						if( selectedCells.length > 0){ 
							var selectedCell = this.dataItem( selectedCells ); 
							selectedCell.copy($("#user-grid").data("userPlaceHolder"));
							if( selectedCell.userId	> 0 ){									
								showUserDetails();
							}
 						}
					},
					dataBound: function(e){		
						 var selectedCells = this.select();
						 if(selectedCells.length == 0 ){
						 	var newUser = new User ();
						 	newUser.copy($("#user-grid").data("userPlaceHolder"));
							$("#user-details").hide();
						 }
					}
				}).data('kendoGrid');
			}	
		}]);

		/**
		* Show user detailis
		*/
		function showUserDetails(){		
			var selectedUser = $("#user-grid").data("userPlaceHolder");			
			if( $('#user-details').text().trim().length	== 0 ){
				$('#user-details').show().html(kendo.template($('#user-details-template').html()));
				
				selectedUser.bind("change", function(e) {
					$('#update-user-btn').removeAttr('disabled');
				});
				
				$('button.btn-control-group[data-action="top"]').click(
					function(e){						
						$('html,body').animate({ scrollTop:  0 }, 300);
					}  		
				);
								
				if(!$("#files").data("kendoUpload")){
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
									    	/**if( e.response.targetUserImage ){
									    		selectedUser.properties.imageId = e.response.targetUserImage.imageId;
									    		var photoUrl = '${request.contextPath}/secure/view-image.do?width=150&height=200&imageId=' + selectedUser.properties.imageId ;
								 	 			$('#user-photo').attr( 'src', photoUrl );
									    	}	**/			
									    	$('#user-photo').attr( 'src', common.api.user.photoUrl( selectedUser, 150, 200 ) );
									    }					   
					});			
				}	
					// password window
					$('#change-password-btn').bind( 'click', function(){
					                $('#change-password-window').kendoWindow({
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
					// update user info
					$('#update-user-btn').bind('click' , function(){
									$.ajax({
										type : 'POST',
										url : "${request.contextPath}/secure/update-user.do?output=json",
										data : { userId:selectedUser.userId, item: kendo.stringify( selectedUser ) },
										success : function( response ){									
										    $('#user-grid').data('kendoGrid').dataSource.read();	
										},
										error: common.api.handleKendoAjaxError,
										dataType : "json"
									});	
									/*									
									if(visible){
										slide.reverse();						
										visible = false;		
										$("#detail-panel").hide();				
									}*/
					}); 	
					// user tabs
					$('#myTab').on( 'show.bs.tab', function (e) {
						//e.preventDefault();		
						var show_bs_tab = $(e.target);
						if(show_bs_tab.attr('href') == '#props' ) {
							if(!$("#user-props-grid").data("kendoGrid")){
 									$("#user-props-grid").kendoGrid({
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
									         { title: "속성", field: "name" , width: 200,  locked:true},
									         { title: "값",   field: "value", width: 200, },
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
 							}						
						}else if(show_bs_tab.attr('href') == '#groups' ) {

							if( !$("#company-combo").data("kendoComboBox") ){
											var company_combo = $("#company-combo").kendoComboBox({
												autoBind: false,
												placeholder: "회사 선택",
						                        dataTextField: "displayName",
						                        dataValueField: "companyId",
											    dataSource: $("#navbar").data('kendoExtNavbar').items()[0].dataSource
											});
											$("#company-combo").data("kendoComboBox").value( 
												selectedUser.company.companyId
											);
											$("#company-combo").data("kendoComboBox").readonly();
										}
										if( !$("#group-combo").data("kendoComboBox") ){
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
										}
									
										if( ! $("#user-group-grid").data("kendoGrid") ){	
											// 3-3 USER GROUP GRID
											$("#user-group-grid").kendoGrid({
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
									                        { field: "displayName",    title: "그룹",   filterable: true, sortable: true,  width: 100 },
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
										}			
										
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
																
						}else if(show_bs_tab.attr('href') == '#roles' ) {
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
					});															
			}
			
			kendo.bind($(".details"), selectedUser );			
			$('#user-photo').attr( 'src', common.api.user.photoUrl( selectedUser, 150, 200 ) );
			$('#update-user-btn').attr('disabled', '');
			$('#myTab a:first').tab('show') ;
			$('html,body').animate({scrollTop: $("#user-details").offset().top - 55 }, 300);
		}
        </script>
		<style>			
		.k-grid-content{
			height:300px;
		}		
		
		#user-details .dropdown-menu {
			top: 190px;
			left: 50px; 
			padding : 20px;
			min-width:300px;
		}
		#change-password-window .container {
			width:500px;
		}		
		</style>
		</#compress>
    </head>
	<body class="color0">
		<!-- START HEADER -->
		<section id="navbar" class="layout"></section>
		<!-- END HEADER -->
		<!-- START MAIN CONTNET -->
		<div class="container-fluid">	
			<div class="row">			
				<div class="col-12 col-lg-12">					
				<div class="page-header">
					<#assign selectedMenuItem = action.getWebSiteMenu("SYSTEM_MENU", "MENU_1_4") />
					<h1>${selectedMenuItem.title}     <small><i class="fa fa-quote-left"></i>&nbsp;${selectedMenuItem.description}&nbsp;<i class="fa fa-quote-right"></i></small></h1>
				</div>			
				</div>		
			</div>
			<div class="row">		
				<div class="col-sm-12">
					<div class="panel panel-default" style="min-height:300px;" >
						<div class="panel-heading selected-company-info" style="padding:5px;">						
							<div class="btn-group">
							<#if request.isUserInRole('ROLE_SYSTEM' )>
								<button type="button" class="btn btn-info btn-sm  btn-control-group" data-action="company"><i class="fa fa-building-o"></i>  회사관리</button>				
							</#if>				
								<button type="button" class="btn btn-info btn-sm  btn-control-group" data-action="site"><i class="fa fa-sitemap"></i>  웹사이트 관리</button>
								<button type="button" class="btn btn-info btn-sm  btn-control-group" data-action="group"><i class="fa fa-users"></i>  그룹관리</button>
							</div>
						<#if request.isUserInRole('ROLE_ADMIN' ) || request.isUserInRole('ROLE_SYSTEM' )>
							<button type="button" class="btn btn-info btn-sm  btn-control-group" data-action="addUser"><i class="fa fa-plus"></i>  사용자 추가</button>
						</#if>
						</div>
						<div class="panel-body" style="padding:5px;">
							<div id="user-grid"></div>
							<div id="file-preview-panel" class="custom-panels-group"></div>
						</div>	
						<div class="panel-body" style="padding:5px;">
							<div id="user-details" style="display:none;"></div>
						</div>					
					</div>				
				</div>			
			</div>							
			<form name="fm1" method="POST" accept-charset="utf-8">
				<input type="hidden" name="companyId"  value="${action.targetCompany.companyId}" />
			</form>	
		</div>		
		<div id="change-password-window" style="display:none;">
		<div class="container layout">	
			<div class="row">
				<div class="col-12 col-xs-12">
					<p>
					    	6~16자의 영문 대소문자, 숫자, 특수문자를 조합하여
							사용하실 수 있습니다.
							생년월일, 전화번호 등 개인정보와 관련된 숫자,
							연속된 숫자와 같이 쉬운 비밀번호는 다른 사람이 쉽게
							알아낼 수 있으니 사용을 자제해 주세요.
							이전에 사용했던 비밀번호나 타 사이트와는 다른 비밀번호를
							사용하고, 비밀번호는 주기적으로 변경해주세요.
							<div class="alert alert-danger">비밀번호에 특수문자를 추가하여 사용하시면
							기억하기도 쉽고, 비밀번호 안전도가 높아져 도용의 위험이
							줄어듭니다.	
							</div>    	
					</p>	
				</div>
			</div>
			<div class="row">
				<div class="col-12 col-xs-12">									
					<form class="form-horizontal">
						<div class="form-group">
							<label class="col-lg-5 control-label" for="password2">새 비밀번호</label>
							<div class="col-lg-7">
								<input type="password" id="password2" name="password2" class="form-control" placeholder="비밀번호" required validationMessage="비밀번호를 입력하여 주세요." />
							</div>
						</div>	
						<div class="form-group">
							<label class="col-lg-5 control-label" for="password3">새 비밀번호 확인</label>
							<div class="col-lg-7">
								<input type="password" id="password3" name="password3" class="form-control"  placeholder="비밀번호 확인" required validationMessage="비밀번호를 입력하여 주세요." />
							</div>
						</div>		
						<div class="form-group">	
							<div class="col-lg-4"></div>
							<div class="col-lg-8">
								<div class="btn-group">
									<button id="do-change-password-btn" class="btn btn-primary">확인</button>
									<button class="btn btn-default" type="reset">다시입력</button></div>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>	
		</div>
		</div>
		  
  		<div id="download-window"></div>    
		<div id="accounts-panel"></div>
		<form name="fm1" method="POST" accept-charset="utf-8" class="details">
			<input type="hidden" name="companyId" value="0" />
		</form>			  
  		<!-- END MAIN CONTNET -->
		<!--
		<footer>  
		</footer>    
		-->
		
		
		<script id="download-window-template" type="text/x-kendo-template">				
			#if (contentType.match("^image") ) {#
				<img src="${request.contextPath}/secure/view-attachment.do?attachmentId=#= attachmentId #" class="img-responsive" alt="#= name #" />
			# } else { #
			
			# } #  
			<div class="blank-top-5"></div>		
			<table class="table table-bordered blank-top-5">
			  <thead>
			    <tr>
			      <th>이름</th>
			      <th>유형</th>
			      <th>크기(bytes)</th>
			      <th>&nbsp;</th>
			    </tr>
			  </thead>
			  <tbody>
			    <tr>
			      <td  width="300">#= name #</td>
			      <td  width="150">#= contentType #</td>
			      <td  width="200">#= size #</td>
			      <td  width="150"><a class="btn btn-default" href="${request.contextPath}/secure/download-attachment.do?attachmentId=#= attachmentId #" ><i class="fa fa-download"></i> 다운로드</a></td>
			    </tr>
			  </tbody>
			</table>				
		</script>			
		
		<!--  User Detetails Template -->
		<script type="text/x-kendo-template" id="user-details-template">			
			<div class="panel panel-default marginless details" >
			<!--
				<div class="panel-heading" >
					<i class="fa fa-male"></i>&nbsp;<span data-bind="text: name"></span>
					<button type="button" class="close" aria-hidden="true">&times;</button>
				</div>
			-->					
				<div class="panel-body" style="padding:5px;">			
					<div class="row">
						<div class="col-lg-6 col-xs-12">
						<!--  start basic info -->	
						<div class="page-header page-nounderline-header">
						  <h5><small><i class="fa fa-info"></i> 이미지를 수정하시려면 이미지를 클릭하세요.</small>
							<button type="button" class="btn btn-link btn-control-group" data-action="top"><i class="fa fa-angle-double-up fa-lg"></i></button>
						  </h5>
						</div>						
					<div class="media">
						<a class="pull-left dropdown-toggle" href="\\#" data-toggle="dropdown">
							<img id="user-photo" class="img-thumbnail media-object"  src="${request.contextPath}/images/common/anonymous.png" border="0"/>			
						</a>
						<ul class="dropdown-menu">
							<li role="presentation" class="dropdown-header">마우스로 사진을 끌어 놓으세요.</li>
							<li>
								<input name="uploadImage" id="files" type="file" class="pull-right" />
							</li>
						</ul>								
						<div class="media-body">
							<h4 class="media-heading"></h4>
							<table class="table">
								<tbody>
									<tr>
										<th class="col-lg-3 col-sm-4">아이디</th>
										<td><input type="text" class="form-control" placeholder="아이디" disabled data-bind="value:username"/></td>
									</tr> 
									<tr>
										<th class="col-lg-3 col-sm-4">이름</th>
										<td><input type="text" class="form-control" placeholder="이름" data-bind="value:name"/></td>
									</tr> 
									<tr>
										<th class="col-lg-3 col-sm-4">메일</th>
										<td><input type="email" class="form-control" placeholder="메일주소" data-bind="value:email"/></td>
									</tr> 
									<tr>
										<th class="col-lg-3 col-sm-4">옵션</th>
										<td>
											<div class="checkbox">
												<label>
													<input type="checkbox" name="nameVisible"  data-bind="checked: nameVisible" />	이름공개
												</label>
											</div>		
											<div class="checkbox">
												<label>
													<input type="checkbox"  name="emailVisible"  data-bind="checked: emailVisible" />	메일공개
												</label>
											</div>	
											<div class="checkbox">
												<label>
													<input type="checkbox"  name="enabled"  data-bind="checked: enabled" />계정사용여부
												</label>
											</div>										
										</td>
									</tr> 																		
									<tr>
										<th class="col-lg-3 col-sm-4"><small>마지막 프로파일 수정일</small></th>
										<td><small><span data-bind="text: lastProfileUpdate" data-format="{yyyy.MM.dd }"></span></small></td>
									</tr>  
									<tr>
										<th class="col-lg-3 col-sm-4"><small>마지막 로그인 일자</small></th>
										<td><small><span data-bind="text: lastLoggedIn" data-format="yyyy.MM.dd HH:mm:ss"></span></small></td>
									</tr>  
								</tbody>
							</table>					
						</div>
					</div>
							<div class="btn-group pull-right">
								<button id="update-user-btn" disabled class="btn btn-primary">정보 변경</button>
								<#if request.isUserInRole('ROLE_SYSTEM' )>
								<button id="change-password-btn" class="btn btn-primary">비밀번호변경</button>					
								</#if>
							</div>						
						<!-- end basic info -->
						</div>	
						<div class="col-lg-6 col-xs-12">
						<!-- start additional info -->
							<div class="page-header page-nounderline-header">
							  <h5><small><i class="fa fa-info"></i> 탭을 선택하여 개인의 추가 옵션(프로퍼티, 그룹, 롤)을 변경할 수 있습니다.</small></h5>
							</div>						
							<ul id="myTab" class="nav nav-tabs">
								<li><a href="\\#props" data-toggle="tab">프로퍼티</a></li>
								<li><a href="\\#groups" data-toggle="tab">그룹</a></li>
								<li><a href="\\#roles" data-toggle="tab">롤</a></li>
								<!--<li><a href="\\#attachments" data-toggle="tab">첨부파일</a></li>-->
							</ul>			
							<div class="tab-content">
								<div class="tab-pane fade" id="props">
									<span class="help-block"><i class="fa fa-circle-o"></i><small> 프로퍼티는 수정 후 저장 버튼을 클릭하여야 최종 반영됩니다.</small></span>
									<div id="user-props-grid" class="props"></div>
								</div>
								<div class="tab-pane fade" id="groups">
									<span class="help-block"><i class="fa fa-circle-o"></i><small> 멤버로 추가하려면 리스트 박스에서 그룹을 선택후 "그룹 멤버로 추가" 버튼을 클릭하세요.</small></span>
										<div class="form-horizontal">
											<div class="form-group">
												<label for="company-combo" class="col-sm-2 control-label"><small>회사</small></label>
												<input id="company-combo" style="width: 180px" />
											</div>
											<div class="form-group">
												<label for="group-combo" class="col-sm-2 control-label"><small>그룹</small></label>
												<input id="group-combo" style="width: 180px" />
											</div>
											<div class="form-group">
												<div class="col-sm-offset-2 col-sm-10">
												<button id="add-to-member-btn" class="btn btn-info btn-sm"><i class="fa fa-plus"></i> 그룹 맴버로 추가</button>
												</div>
											</div>
										</div>							
									<div id="user-group-grid" class="groups"></div>									
								</div>							
								<div class="tab-pane fade" id="roles">
									<div class="row">
										<div class="col-sm-12">
											<span class="help-block"><i class="fa fa-circle-o"></i> 다음은 맴버로 가입된 그룹으로 부터 상속된 롤입니다. <small>그룹에서 상속된 롤은 그룹 관리에서 변경할 수 있습니다.</small></span>
										</div>
									</div>	
									<div class="row">	
										<div class="col-sm-offset-1 col-sm-11">
											<div id="group-role-selected"></div>
										</div>
									</div>
									<div class="row">
										<div class="col-sm-12">
											<span class="help-block"><i class="fa fa-circle-o"></i> 다음은 사용자에게 직접 부여된 롤입니다. <small>그룹에서 부여된 롤을 제외한 롤들만 아래의 선택박스에서 사용자에게 부여 또는 제거하세요.</small></span>
										</div>
									</div>	
									<div class="row">	
										<div class="col-sm-offset-1 col-sm-11">
											<div id="user-role-select"></div> 
										</div>
									</div>
								</div>			
							</div>
						<!-- end additional info -->
						</div>
					</div>
				</div>
			</div>			
		</div>
		</script>			
		<!-- 공용 템플릿 -->
		<div id="account-panel"></div>    	
		<#include "/html/common/common-system-templates.ftl" >	
		<!-- END MAIN CONTENT  -->
    </body>
</html>
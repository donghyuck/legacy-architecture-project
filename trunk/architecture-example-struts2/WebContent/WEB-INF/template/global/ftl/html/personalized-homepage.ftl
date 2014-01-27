<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title><#if action.user.company ?? >${action.user.company.displayName }<#else>::</#if></title>
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.0.3/font-awesome.min.css',
			'${request.contextPath}/js/jquery/1.9.1/jquery.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			'${request.contextPath}/js/kendo/kendo.web.js',
			'${request.contextPath}/js/kendo/kendo.ko_KR.js',			
			'${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js',			
			'${request.contextPath}/js/bootstrap/3.0.3/bootstrap.min.js',
			'${request.contextPath}/js/pdfobject/pdfobject.js',
			'${request.contextPath}/js/common/common.models.min.js',
			'${request.contextPath}/js/common/common.ui.js',
			'${request.contextPath}/js/common/common.classie.js'],
			complete: function() {
			
				// 1.  한글 지원을 위한 로케일 설정
				kendo.culture("ko-KR");
				
				// 2.  MEUN 설정
				// START SCRIPT	
				$("#top-menu").kendoMenu();
				$("#top-menu").show();
												
				var menuRight  = document.getElementById( 'cbp-spmenu-s2' );
				$("#show-right-slide").click(function (e) {
					classie.toggle( this, 'active' );
					classie.toggle( menuRight, 'cbp-spmenu-open' );				
				});
				$("#hide-right-slide").click(function (e) {
					classie.toggle( this, 'active' );
					classie.toggle( menuRight, 'cbp-spmenu-open' );				
				});
												
				$("#personalized-area").data("sizePlaceHolder", { oldValue: 6 , newValue : 6} );	
				$("input[name='personalized-area-col-size']").on("change", function () {					
					var grid_col_size = $("#personalized-area").data("sizePlaceHolder");
					grid_col_size.oldValue = grid_col_size.newValue;
					grid_col_size.newValue = this.value;			
					$(".custom-panels-group").each(function( index ) {
						var custom_panels_group = $(this);				
						custom_panels_group.removeClass("col-sm-" + grid_col_size.oldValue );		
						custom_panels_group.addClass("col-sm-" + grid_col_size.newValue );		
					});
				});											
							
				// 3. ACCOUNTS LOAD	
				var currentUser = new User({});			
				var accounts = $("#account-panel").kendoAccounts({
					dropdown : false,
					authenticate : function( e ){
						currentUser = e.token;			
					},
					<#if CompanyUtils.isallowedSignIn(action.company) ||  !action.user.anonymous  || action.view! == "personalized" >
					template : kendo.template($("#account-template").html()),
					</#if>
					afterAuthenticate : function(){							
						//$('.dropdown-toggle').dropdown();
						if( currentUser.anonymous ){
							$("#account-panel .custom-external-login-groups button").each(function( index ) {
									var external_login_button = $(this);
									external_login_button.click(function (e){																												
										var target_media = external_login_button.attr("data-target");
										$.ajax({
											type : 'POST',
											url : "${request.contextPath}/community/get-socialnetwork.do?output=json",
											data: { media: target_media },
											success : function(response){
												if( response.error ){
													// 연결실패.
												} else {	
													window.open( response.authorizationUrl ,'popUpWindow','height=500,width=600,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no,status=yes')
												}
											},
											error:handleKendoAjaxError												
										});	
									});								
							});						
							var validator = $("#login-panel").kendoValidator({validateOnBlur:false}).data("kendoValidator");
							$("#login-btn").click(function() { 
								$("#login-status").html("");
								if( validator.validate() )
								{								
									accounts.login({
										data: $("form[name=login-form]").serialize(),
										success : function( response ) {
											var refererUrl = "/main.do";
											if( response.item.referer ){
												refererUrl = response.item.referer;
											}
											$("form[name='login-form']")[0].reset();    
											$("form[name='login-form']").attr("action", refererUrl ).submit();						
										},
										fail : function( response ) {  
											$("#login-password").val("").focus();												
											$("#login-status").kendoAlert({ 
												data : { message: "입력한 사용자 이름 또는 비밀번호가 잘못되었습니다." },
												close : function(){	
													$("#login-password").focus();										
												 }
											}); 										
										},		
										error : function( thrownError ) {
											$("form[name='login-form']")[0].reset();                    
											$("#login-status").kendoAlert({ data : { message: "잘못된 접근입니다." } }); 									
										}																
									});															
								}else{	}
							});	
						}
					}
				});	
				
				// 4. CONTENT 	
				$("#announce-panel").data( "announcePlaceHolder", new Announce () );
				// 1. Announces 								
				$("#announce-grid").kendoGrid({
					dataSource : new kendo.data.DataSource({
						transport: {
							read: {
								type : 'POST',
								dataType : "json", 
								url : '${request.contextPath}/community/list-announce.do?output=json'
							},
							parameterMap: function(options, operation) {
								if (operation != "read" && options.models) {
									return {models: kendo.stringify(options.models)};
								}
							} 
						},
						pageSize: 10,
						error:handleKendoAjaxError,
						schema: {
							data : "targetAnnounces",
							model : Announce
						}
					}),
					sortable: true,
					height: 300,
					columns: [ 
						{field:"announceId", title: "ID", width: 50, attributes: { "class": "table-cell", style: "text-align: center " }} ,
						{field:"subject", title: "주제"}
					],
					selectable: "row",
					change: function(e) { 
						var selectedCells = this.select();
						if( selectedCells.length > 0){
							var selectedCell = this.dataItem( selectedCells );	    							
							var announcePlaceHolder = $("#announce-panel").data( "announcePlaceHolder" );
							announcePlaceHolder.announceId = selectedCell.announceId;
							announcePlaceHolder.subject = selectedCell.subject;
							announcePlaceHolder.body = selectedCell.body;
							announcePlaceHolder.startDate = selectedCell.startDate ;
							announcePlaceHolder.endDate = selectedCell.endDate;
							announcePlaceHolder.modifiedDate = selectedCell.modifiedDate;
							announcePlaceHolder.creationDate = selectedCell.creationDate;
							announcePlaceHolder.user = selectedCell.user;							
							if( announcePlaceHolder.user.userId == $("#account-panel").data("currentUser").userId ){
								announcePlaceHolder.modifyAllowed = true;
							}else{
								announcePlaceHolder.modifyAllowed = false;
							}
							$("#announce-panel").data( "announcePlaceHolder", announcePlaceHolder );							 
							showAnnouncePanel();	
						}
					},
					dataBound: function(e) {					
						var selectedCells = this.select();
						this.select("tr:eq(1)");
					}
				});
				
				$("#announce-panel .panel-header-actions a").each(function( index ) {
					var panel_header_action = $(this);		
					panel_header_action.click(function (e) {
						e.preventDefault();
						if( panel_header_action.text() == "Minimize" ){
							$("#announce-panel .panel-body").toggleClass("hide");								
							var panel_header_action_icon = panel_header_action.find('span');
							if( panel_header_action_icon.hasClass("k-i-minimize") ){
								panel_header_action.find('span').removeClass("k-i-minimize");
								panel_header_action.find('span').addClass("k-i-maximize");
							}else{
								panel_header_action.find('span').removeClass("k-i-maximize");
								panel_header_action.find('span').addClass("k-i-minimize");
							}							
						}else if (panel_header_action.text() == "Close"){	
							$("#announce-panel" ).hide();
						}
					});
				});	
															
				// 4. Right Tabs

				$('#myTab a').click(function (e) {
					e.preventDefault();	
					$(this).tab('show');
				});	
				
				$('#myTab').on( 'show.bs.tab', function (e) {
					//e.preventDefault();		
					var show_bs_tab = $(e.target);
					if( show_bs_tab.attr('href') == '#my-streams' ){						
						if( !$("#my-social-streams-grid" ).data('kendoGrid') ){ 											
							$("#my-social-streams-grid").kendoGrid({
								dataSource : new kendo.data.DataSource({
									transport: {
										read: {
											type : 'POST',
											dataType : "json", 
											url : '${request.contextPath}/community/list-my-socialnetwork.do?output=json'
										} 
									},
									pageSize: 10,
									error:handleKendoAjaxError,				
									schema: {
										data : "connectedSocialNetworks",
										model : SocialNetwork
									},
								}),
								selectable: "single",
								rowTemplate: kendo.template( '<tr><td><i class="fa fa-#: serviceProviderName#"></i>&nbsp; #: serviceProviderName#</td></tr>' ),				
								change: function(e) { 				
									var selectedCells = this.select();
									if( selectedCells.length == 1){
										var selectedCell = this.dataItem( selectedCells );		
										$("#my-social-streams-grid").data("streamsPlaceHolder", selectedCell);
										if( ! $("#my-social-streams-grid").data(selectedCell.serviceProviderName + "-streams-" + selectedCell.socialAccountId ) ){										
											var selectedStreams = new MediaStreams(selectedCell.socialAccountId, selectedCell.serviceProviderName);							
											if( selectedStreams.name == 'twitter'){
												selectedStreams.setTemplate ( kendo.template($("#twitter-timeline-template").html()) );											
											}else if ( selectedStreams.name == 'facebook'){
												selectedStreams.setTemplate( kendo.template($("#facebook-homefeed-template").html()) );
											}
											selectedStreams.createDataSource({});											
											$("#my-social-streams-grid").data(selectedCell.serviceProviderName + "-streams-" + selectedCell.socialAccountId , selectedStreams )
										}
										displaySocialPanel();
									}							
								},
								dataBound: function(e) {									
								},
								height: 300
							});	
						}
					} else if( show_bs_tab.attr('href') == '#my-files' ){					
						if( !$('#attachment-list-view').data('kendoListView') ){								
							var attachementTotalModle = kendo.observable({ 
								totalAttachCount : "0",
								totalImageCount : "0",
								totalFileCount : "0"							
							});
							kendo.bind($("#attachment-list-view-filter"), attachementTotalModle );						
							$("#attachment-list-view").kendoListView({
								dataSource: {
									type: 'json',
									transport: {
										read: { url:'${request.contextPath}/community/list-my-attachement.do?output=json', type: 'POST' },		
										destroy: { url:'${request.contextPath}/community/delete-my-attachment.do?output=json', type:'POST' },                                
										parameterMap: function (options, operation){
											if (operation != "read" && options) {										                        								                       	 	
												return { attachmentId :options.attachmentId };									                            	
											}else{
												return { };
											}
										}
									},
									pageSize: 12,
									error:handleKendoAjaxError,
									schema: {
										model: Attachment,
										data : "targetAttachments",
										total : "totalTargetAttachmentCount"
									},
									sort: { field: "attachmentId", dir: "desc" },
									filter :  { field: "contentType", operator: "neq", value: "" }
								},
								selectable: "single",									
								change: function(e) {									
									var data = this.dataSource.view() ;
									var item = data[this.select().index()];		
									$("#attachment-list-view").data( "attachPlaceHolder", item );												
									displayAttachmentPanel( ) ;	
								},
								navigatable: false,
								template: kendo.template($("#attachment-list-view-template").html()),								
								dataBound: function(e) {
									var attachment_list_view = $('#attachment-list-view').data('kendoListView');
									var filter =  attachment_list_view.dataSource.filter().filters[0].value;
									var totalCount = attachment_list_view.dataSource.total();
									if( filter == "image" ) 
									{
										attachementTotalModle.set("totalImageCount", totalCount);
									} else if ( filter == "application" ) {
										attachementTotalModle.set("totalFileCount", totalCount);
									} else {
										attachementTotalModle.set("totalAttachCount", totalCount);
									}
								}
							});																	
							$("#attachment-list-view").on("mouseenter",  ".attach", function(e) {
									kendo.fx($(e.currentTarget).find(".attach-description")).expand("vertical").stop().play();
								}).on("mouseleave", ".attach", function(e) {
									kendo.fx($(e.currentTarget).find(".attach-description")).expand("vertical").stop().reverse();
							});															
							$("ul#attachment-list-view-filter li").find("a").click(function(){					
								var attachment_list_view = $('#attachment-list-view').data('kendoListView');
								$("ul#attachment-list-view-filter li.active").removeClass("active");
								$(this).parent().addClass("active");
								var filter_id =  $(this).attr('id') ;
								switch(filter_id){
									case "attachment-list-view-filter-1" :
										attachment_list_view.dataSource.filter(  { field: "contentType", operator: "neq", value: "" } ) ; 
										break;
									case "attachment-list-view-filter-2":
										attachment_list_view.dataSource.filter( { field: "contentType", operator: "startswith", value: "image" }) ; 
										break;
									case "attachment-list-view-filter-3":
										attachment_list_view.dataSource.filter( { field: "contentType", operator: "startswith", value: "application" }) ; 
										break;											
								}
							});							
							$("#pager").kendoPager({
								refresh : true,
								buttonCount : 5,
								dataSource : $('#attachment-list-view').data('kendoListView').dataSource
							});								
							$("#my-files .btn-group button").each(function( index ) { 
								var control_button = $(this);								
								var control_button_icon = control_button.find("i");				
								if( control_button_icon.hasClass("fa-upload")){
									control_button.click( function(e){									
										if( !$('#attachment-files').data('kendoUpload') ){		
											$("#attachment-files").kendoUpload({
												 	multiple : false,
												 	width: 300,
												 	showFileList : false,
												    localization:{ select : '파일 선택' , dropFilesHere : '업로드할 파일을 이곳에 끌어 놓으세요.' },
												    async: {
														saveUrl:  '${request.contextPath}/community/save-my-attachments.do?output=json',							   
														autoUpload: true
												    },
												    upload: function (e) {								         
												    	 e.data = {};														    								    	 		    	 
												    },
												    success : function(e) {								    
														if( e.response.targetAttachment ){
															e.response.targetAttachment.attachmentId;
															// LIST VIEW REFRESH...
															$('#attachment-list-view').data('kendoListView').dataSource.read(); 
														}				
													}
											});						
										}
										$("#my-files .panel").toggleClass("hide");										
										$("#my-file-upload").toggleClass("hide");										
									});									
								}								
							});									
						}
					} else if(show_bs_tab.attr('href') == '#my-photo-stream' ){							
						if( !$('#photo-list-view').data('kendoListView') ){
							$("#photo-list-view").kendoListView({
								dataSource: {
									type: 'json',
									transport: {
										read: { url:'${request.contextPath}/community/list-my-image.do?output=json', type: 'POST' }
									},
									pageSize: 12,
									error:handleKendoAjaxError,
									schema: {
										model: Image,
										data : "targetImages",
										total : "totalTargetImageCount"
									},
									sort: { field: "imageId", dir: "desc" }
								},
								selectable: "single",									
								change: function(e) {									
									var data = this.dataSource.view() ;
									var item = data[this.select().index()];									
									item.index = this.select().index();			
									item.page = $("#photo-list-pager").data("kendoPager").page();													
									$("#photo-list-view").data( "photoPlaceHolder", item );														
									displayPhotoPanel( ) ;										
								},
								navigatable: false,
								template: kendo.template($("#photo-list-view-template").html()),								
								dataBound: function(e) {
								}
							});														
							$("#photo-list-view").on("mouseenter",  ".attach", function(e) {
									kendo.fx($(e.currentTarget).find(".attach-description")).expand("vertical").stop().play();
								}).on("mouseleave", ".attach", function(e) {
									kendo.fx($(e.currentTarget).find(".attach-description")).expand("vertical").stop().reverse();
							});											
							$("#photo-list-pager").kendoPager({
								refresh : true,
								buttonCount : 5,
								dataSource : $('#photo-list-view').data('kendoListView').dataSource
							});								
							$("#my-photo-stream .btn-group button").each(function( index ) { 
								var control_button = $(this);								
								var control_button_icon = control_button.find("i");				
								if( control_button_icon.hasClass("fa-upload")){
									control_button.click( function(e){			
										if( !$("#photo-files").data("kendoUpload")	){						
											$("#photo-files").kendoUpload({
												 	multiple : true,
												 	width: 300,
												 	showFileList : false,
												    localization:{ select : '사진 선택' , dropFilesHere : '업로드할 사진들을 이곳에 끌어 놓으세요.' },
												    async: {
														saveUrl:  '${request.contextPath}/community/update-my-image.do?output=json',							   
														autoUpload: true
												    },
												    upload: function (e) {				
												    	 e.data = {};							
												    },
												    success : function(e) {								    
														if( e.response.targetImage ){
															e.response.targetImage.imageId;
															// LIST VIEW REFRESH...
															$('#photo-list-view').data('kendoListView').dataSource.read();
														}				
													}
											});		
										}										
										$("#my-photo-stream .panel").toggleClass("hide");										
										$("#my-photo-upload").toggleClass("hide");										
									});
								}								
							});
						}
					}					
				});

				$('#myTab a:first').tab('show') ;
				// END SCRIPT 
			}
		}]);	
		
		
		
		/** Announce View Panel */		
		function editAnnouncePanel (){
			var announcePlaceHolder = $("#announce-panel").data( "announcePlaceHolder" );				
			if( announcePlaceHolder.modifyAllowed ){						
				var template = kendo.template($('#announcement-edit-template').html());
				$("#announce-view").html( template(announcePlaceHolder) );	
				kendo.bind($("#announce-view"), announcePlaceHolder );			
				if(!$("#announce-body-editor").data("kendoEditor") ){
					$("#announce-body-editor").kendoEditor({
						tools : [
							'bold',
							'italic',
							'underline',
							'strikethrough',
							'justifyLeft',
							'justifyCenter',
							'justifyRight',
							'justifyFull',
							'insertUnorderedList',
							'insertOrderedList',
							'indent',
							'outdent',
							'createLink',
							'unlink',
							'insertImage',
							'subscript',
							'superscript',
							'createTable',
							'addRowAbove',
							'addRowBelow',
							'addColumnLeft',
							'addColumnRight',
							'deleteRow',
							'deleteColumn',
							'viewHtml',
							'formatting',
							'fontName',
							'fontSize',
							'foreColor',
							'backColor'
						],
						imageBrowser: {
							messages: {
								uploadFile: "이미지 업로드",
								orderBy: "정렬",
								orderByName: "이름",
								orderBySize: "크기",
								directoryNotFound: "A directory with this name was not found.",
								emptyFolder: "Empty Folder",
								deleteFile: 'Are you sure you want to delete "{0}"?',
								invalidFileType: "The selected file \"{0}\" is not valid. Supported file types are {1}.",
								overwriteFile: "A file with name \"{0}\" already exists in the current directory. Do you want to overwrite it?",
								dropFilesHere: "이미지 파일을 이미지 업로드 버튼에 끌어 놓으세요.",
								search: "검색"
							},
							schema: {
								model: {
									id: "imageId",
									fields : {
										imageId : { fieldId: "imageId" },
										name : { fieldId: "name" },
										type : { fieldId: "contentType" , parse: function() { return "f" ; }},
										size : { fieldId : "size" }										
									}
								},
								data : "targetImages"
							},			
							transport: {
								read: {
									url: "${request.contextPath}/community/imagebrowser-list.do?output=json",
									type: "POST",
									dataType: "json",
									data: { 
										objectType: 1,
										objectId: $("#account-panel").data("currentUser" ).company.companyId  
									}
								},						
								thumbnailUrl: function( path, name, imageId ){
									return "${request.contextPath}/community/download-image.do?width=150&height=150&imageId=" + imageId ;
								},
								uploadData : {
									objectType: 1,
									objectId: $("#account-panel").data("currentUser" ).company.companyId  
								},	
								uploadUrl: "${request.contextPath}/community/imagebrowser-update.do?output=json",							
								imageUrl: function(path, imageId){
									return "${request.contextPath}/community/download-image.do?imageId=" + imageId;
								}
							}
						}
					});
				}				
				$("#announce-view div button").each(function( index ) {			
					var panel_button = $(this);			
					if( panel_button.hasClass( 'custom-update') ){
						panel_button.click(function (e) { 
							e.preventDefault();					
							var data = $("#announce-panel").data( "announcePlaceHolder" );	
							
							alert( kendo.stringify( data ) );
							$.ajax({
									dataType : "json",
									type : 'POST',
									url : '${request.contextPath}/community/update-announce.do?output=json',
									data : { announceId: data.announceId, item: kendo.stringify( data ) },
									success : function( response ){		
										$('#announce-grid').data('kendoGrid').dataSource.read();	
									},
									error:handleKendoAjaxError
							});	
						} );
					}else if ( panel_button.hasClass('custom-delete') ){
						panel_button.click(function (e) { 
							e.preventDefault();
							if( confirm("삭제하시겠습니까 ?") ) {
							}
						} );
					}else if ( panel_button.hasClass('custom-cancle') ){
						panel_button.click(function (e) { 
							showAnnouncePanel();
						} );
					}			
				} );							
			}
		}
		
		function showAnnouncePanel (){			
			var announcePlaceHolder = $("#announce-panel").data( "announcePlaceHolder" );						
			var template = kendo.template($('#announcement-view-template').html());
			$("#announce-view").html( template(announcePlaceHolder) );	
			kendo.bind($("#announce-view"), announcePlaceHolder );				
			if( announcePlaceHolder.editable ){
				$("#announce-view button[class*=custom-edit]").click( function (e){
					editAnnouncePanel();
				} );
			}
			$("#announce-panel" ).show();
		}	
								
		<!-- ============================== -->
		<!-- display social streams panel                        -->
		<!-- ============================== -->						
		function displaySocialPanel ( ){
			var streamsPlaceHolder = $("#my-social-streams-grid").data("streamsPlaceHolder");
			var streamsProvider = $("#my-social-streams-grid").data( streamsPlaceHolder.serviceProviderName + "-streams-" + streamsPlaceHolder.socialAccountId ) ;
			var renderToString =  streamsPlaceHolder.serviceProviderName + "-panel-" + streamsPlaceHolder.socialAccountId ;		
			if( $("#" + renderToString ).length == 0  ){						
				// create new panel 
				var grid_col_size = $("#personalized-area").data("sizePlaceHolder");
				var template = kendo.template($("#social-view-panel-template").html());				
				//$("#social-view-panels").append( template( streamsPlaceHolder ) );												
				$("#personalized-area").append( template( streamsPlaceHolder ) );						
				$( '#'+ renderToString ).parent().addClass("col-sm-" + grid_col_size.newValue );												
				$( '#'+ renderToString + ' .panel-header-actions a').each(function( index ) {
					var social_header_action = $(this);
					social_header_action.click(function (e){
						e.preventDefault();		
						var social_header_action_icon = social_header_action.find('span');
						if (social_header_action.text() == "Minimize"){
							$( "#"+ renderToString +" .panel-body").toggleClass("hide");				
							if( social_header_action_icon.hasClass("k-i-maximize") ){
								social_header_action_icon.removeClass("k-i-maximize");
								social_header_action_icon.addClass("k-i-minimize");
							}else{
								social_header_action_icon.removeClass("k-i-minimize");
								social_header_action_icon.addClass("k-i-maximize");
							}
						} else if (social_header_action.text() == "Refresh"){								
							streamsProvider.dataSource.read();							
						} else if (social_header_action.text() == "Close"){	
							$("#" + renderToString ).parent().remove();
						}
					});			
				} );			
			} 
			$("#" + renderToString ).parent().show();
			if(streamsProvider.dataSource.total() == 0 )
			{
					streamsProvider.dataSource.read();
			}	
		}		

		<!-- ============================== -->
		<!-- display attachement panel                          -->
		<!-- ============================== -->			
		function displayAttachmentPanel(){			
		
			var renderToString =  "attachement-panel-0";	
			var attachPlaceHolder = $("#attachment-list-view").data( "attachPlaceHolder" );		
			
			if( $("#" + renderToString ).length == 0  ){			
				var grid_col_size = $("#personalized-area").data("sizePlaceHolder");
				var template = kendo.template('<div id="#: panelId #" class="custom-panels-group col-sm-#: colSize#" style="display:none;"></div>');				
				$("#personalized-area").append( template( {panelId:renderToString, colSize: grid_col_size.newValue } ) );	
			}	
						
			if( !$( "#" + renderToString ).data("extPanel") ){					
				$("#" + renderToString ).data("extPanel", 
					$("#" + renderToString ).extPanel({
						template : kendo.template($("#file-panel-template").html()),
						data : attachPlaceHolder,
						refresh : true
					})
				 );
			}else{
				$("#" + renderToString ).data("extPanel").data(attachPlaceHolder);
			}
			
			//CHANGE
			var panel = $("#" + renderToString ).data("extPanel");
			panel.show();
			
			/**	 
			
			var template = kendo.template($('#file-view-template').html());			
			$('#' + renderToString ).html( template(attachPlaceHolder) );	
			kendo.bind($('#' + renderToString ), attachPlaceHolder );					
			if( attachPlaceHolder.contentType == "application/pdf" ){
				var loadSuccess = new PDFObject({ url: "${request.contextPath}/community/view-my-attachment.do?attachmentId=" + attachPlaceHolder.attachmentId, pdfOpenParams: { view: "FitV" } }).embed("pdf-view");				
			}	
			$("#" + renderToString + " button").each(function( index ) {		
				var panel_button = $(this);
				panel_button.click(function (e) { 
					e.preventDefault();					
					if( panel_button.hasClass( 'custom-attachment-delete') ){
						$.ajax({
							dataType : "json",
							type : 'POST',
							url : '${request.contextPath}/community/delete-my-attachment.do?output=json',
							data : { attachmentId: attachPlaceHolder.attachmentId },
							success : function( response ){
								$('#' + renderToString ).remove();
							},
							error:handleKendoAjaxError
						});	
					}
					if( panel_button.hasClass( 'close') ){
						$('#' + renderToString ).remove();
					}
				});
			});							
			$("#update-attach-file").kendoUpload({
				multiple: false,
				async: {
					saveUrl:  '${request.contextPath}/community/update-my-attachment.do?output=json',							   
					autoUpload: true
				},
				localization:{ select : '파일 변경하기' , dropFilesHere : '새로운 파일을 이곳에 끌어 놓으세요.' },	
				upload: function (e) {				
					e.data = { attachmentId: $("#attach-view-panel").data( "attachPlaceHolder").attachmentId };														    								    	 		    	 
				},
				success: function (e) {				
					if( e.response.targetAttachment ){
						 $("#attach-view-panel").data( "attachPlaceHolder",  e.response.targetAttachment  );
						kendo.bind($("#attach-view-panel"), e.response.targetAttachment );
					}
				} 
			});			
			$('#' + renderToString ).show();	
			**/
			
			
						
		}	
						
		<!-- ============================== -->
		<!-- display photo  panel                                  -->
		<!-- ============================== -->			
		function displayPhotoPanel(){			
		
			var renderToString =  "photo-panel-0";	
			if( $("#" + renderToString ).length == 0  ){			
				var grid_col_size = $("#personalized-area").data("sizePlaceHolder");
				var template = kendo.template('<div id="#: panelId #" class="custom-panels-group col-sm-#: colSize#" style="display:none;"></div>');				
				$("#personalized-area").append( template( {panelId:renderToString, colSize: grid_col_size.newValue } ) );	
			}			
				
			if( !$("#" + renderToString ).data("extPanel") ){					
				$("#" + renderToString ).data("extPanel", 
					$("#" + renderToString ).extPanel({
						title : "포토",
						template : kendo.template($("#photo-panel-template").html())
					})
				 );					
				 	 
				$("#" + renderToString ).find(".custom-photo-delete").click(function (e) { 
					e.preventDefault();			
					$.ajax({
						dataType : "json",
						type : 'POST',
						url : '${request.contextPath}/community/delete-my-image.do?output=json',
						data : { imageId: $("#photo-list-view").data( "photoPlaceHolder").imageId },
						success : function( response ){
							$("#" + renderToString ).remove();
						},
						error:handleKendoAjaxError
					});	
				});
				
				$("#update-photo-file").kendoUpload({
						multiple: false,
						async: {
							saveUrl:  '${request.contextPath}/community/update-my-image.do?output=json',
							autoUpload: true
						},
						localization:{ select : '사진 선택' , dropFilesHere : '새로운 사진파일을 이곳에 끌어 놓으세요.' },	
						upload: function (e) {				
							e.data = { imageId: $("#photo-list-view").data( "photoPlaceHolder").imageId };
						},
						success: function (e) {				
							if( e.response.targetImage ){
								$('#photo-list-view').data('kendoListView').dataSource.read();								
								var item = e.response.targetImage;
								item.index = $("#photo-list-view").data( "photoPlaceHolder" ).index;			
								item.page = $("#photo-list-view").data( "photoPlaceHolder" ).page				
								$("#photo-list-view").data( "photoPlaceHolder",  item );
								displayPhotoPanel();
							}
						} 
				});	
			}
			
			var photoPlaceHolder = $("#photo-list-view").data( "photoPlaceHolder");		
			var panel = $("#" + renderToString ).data("extPanel");
			var editable = $("#account-panel").data("currentUser" ).userId == photoPlaceHolder.objectId ;
			
			panel.data().set( "title",  photoPlaceHolder.name ) ;
			panel.data().set("imageId", photoPlaceHolder.imageId );
			panel.data().set("editable", editable );
			
			var template = kendo.template($('#photo-view-template').html());
			$("#" + renderToString ).find(".panel-body:first").html( template(photoPlaceHolder) );

			$( '#' + renderToString +' .pager li').each(function( index ) { 
				var panel_pager = $(this);				
				if( panel_pager.hasClass('previous') ){
					panel_pager.click(function (e) { 
						e.preventDefault();						
						var current_index = $("#photo-list-view").data( "photoPlaceHolder").index;				
						var previous_index = current_index-1;	
						var listView =  $('#photo-list-view').data('kendoListView');	
						var list_view_pager = $("#photo-list-pager").data("kendoPager");
						var current_page = list_view_pager.page();						
						if( current_index > 0 ){							
							var item = listView.dataSource.view()[previous_index];
							item.index = previous_index;		
							item.page = current_page ;					
							$("#photo-list-view").data( "photoPlaceHolder", item );														
							displayPhotoPanel( );
						} else {
							if( current_page > 1 ){
								list_view_pager.page(current_page - 1);
								listView.select(listView.element.children().last());
							}
						}
					});
				}else if ( panel_pager.hasClass('next') ){ 
					panel_pager.click(function (e) { 
						e.preventDefault();
						var current_index = $("#photo-list-view").data( "photoPlaceHolder").index;				
						var next_index = current_index + 1;				
						var listView =  $('#photo-list-view').data('kendoListView');
						var total_index = listView.dataSource.view().length -1 ;
						var list_view_pager = $("#photo-list-pager").data("kendoPager");
						var current_page = list_view_pager.page();												
						if( current_index < total_index  ){
							var item = listView.dataSource.view()[next_index];
							item.index = next_index;
							item.page = current_page ;							
							$("#photo-list-view").data( "photoPlaceHolder", item );
							displayPhotoPanel( );						
						}else {
							if( current_page <  list_view_pager.totalPages() ){
								list_view_pager.page(current_page+1);
								listView.select(listView.element.children().first());
							}
						}						
					});				
				}
			} );													
			panel.show();			
		}				
		-->
		</script> 		   
		
		<style scoped="scoped">

		.k-tiles-arrange label {
			font-weight : normal;		
		}
		.k-tiles li.k-state-selected {
			border-color: #428bca;
		}
		.k-imagebrowser ul li.k-state-selected{
			background: #428bca;
			color: #ffffff; 
			border-color : #428bca; 
		}
		
		#pdf-view {
			height: 500px;
			margin: 0 auto;
			border: 0px solid #787878;
		}
		
		#pdf-view p {
		   padding: 1em;
		}
		
		#pdf-view object {
		   display: block;
		   border: solid 1px #787878;
		}

		.k-dropzone {
		#	width: 808px;
		#	height: 219px;
			border: 5px dashed #c0dcf2;
			background: 0;
			text-align: center;
			padding: 34px 10px 10px;
			-webkit-border-radius: 18px;
			-moz-border-radius: 18px;
			border-radius: 18px;
			margin: 0 0 10px;
			position: relative;
			z-index: 2000;
		}
		
		.k-grid table tr.k-state-selected{
			background: #428bca;
			color: #ffffff; 
		}

		.media, .media .media {
			margin-top: 5px;
		}
	
		.popover {
			font-family: "나눔 고딕", "BM_NANUMGOTHIC";
			width: 90%;
			margin-top: 5px;
			margin-right: 20px;
			margin-bottom: 10px;
			margin-left: 20px;
			float: left;
			display: block;
			position: relative;
			z-index: 1;
			min-width: 200px;
			max-width: 500px;
			-webkit-box-shadow: none;
			box-shadow: none;
			background-clip: none;			
		 }
		 
		.popover.left {
			float: right;
		  
		  }
		  
		  .popover.right {

		  }		  
		 
		 .popover-title {
			font-family: "나눔 고딕", "BM_NANUMGOTHIC";
		 }

		.k-callout-n {
		border-bottom-color: #787878;
		}	
				
		.k-callout-w {
			border-right-color: #787878;
		}
		
		.k-callout-e {
		border-left-color: #787878;
		}	
		
		#attachment-list-view, #photo-list-view, #photo-gallery-view {
			min-height: 320px;
			min-width: 320px;
			padding: 0px;
			border: 0px;
			margin-bottom: -1px;
		}
        		                		
		.attach
		{
			float: left;
            position: relative;
            width: 160px;
            height: 160px;
            padding: 0;
			cursor: pointer;
			overflow: hidden;
		}
		
		.attach img
		{
			width: 160px;
			height: 160px;
		}
		
		.attach-description {
			position: absolute;
			top: 0;
			width: 160px	;
			height: 0;
			overflow: hidden;
			background-color: rgba(0,0,0,0.8)
		}
	
		.attach h3
		{
			margin: 0;
            padding: 10px 10px 0 10px;
            line-height: 1.1em;
            font-size : 12px;
            font-weight: normal;
            color: #ffffff;
            word-wrap: break-word;
		}

		.attach p {
			color: #ffffff;
			font-weight: normal;
			padding: 0 10px;
			font-size: 12px;
        }
        
		.k-listview:after, .attach dl:after {
			content: ".";
			display: block;
			height: 0;
			clear: both;
			visibility: hidden;
        }
        
        .k-pager-wrap {
        	border : 0px;
        	border-width: 0px;
        }

		.k-editor-inline {
			margin: 0;
			#padding: 21px 21px 11px;
			border-width: 0;
			box-shadow: none;
			background: none;
		}

		.k-editor-inline.k-state-active {
			border-width: 1px;
			#padding: 20px 20px 10px;
			#background: none;
			#border-color : red;
  			border-color: #66afe9;
			#  outline: 0;
			-webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075), 0 0 8px rgba(102, 175, 233, 0.6);
			box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075), 0 0 8px rgba(102, 175, 233, 0.6);
		}

		.inline-column-editor {
			display: inline-block;
			vertical-align: top;
			max-width: 600px;
			width: 100%;
		}
		.cbp-spmenu {
			padding: 5px;
			background-color: rgba(0,0,0,0.2);
		}
		
		.cbp-spmenu-right {
			right: -400px;
		}
		
		.cbp-spmenu-vertical{
			width: 400px;
		}
		
		
		</style>   	
	</head>
	<body id="doc" class="cbp-spmenu-push">
		<!-- START HEADER -->
		<#include "/html/common/common-homepage-menu.ftl" >	
		<!-- END HEADER -->	
		<!-- START MAIN CONTENT -->
			<div  class="container" style="min-height:600px;">		
				<div class="row">
					<div class="col-sm-6">
						레이아웃 <div class="btn-group" data-toggle="buttons">
						  <label class="btn btn-primary">
						    <input type="radio" name="personalized-area-col-size" value="12"><i class="fa fa-square"></i>
						  </label>
						  <label class="btn btn-primary active">
						    <input type="radio" name="personalized-area-col-size"  value="6"> <i class="fa fa-th-large"></i>
						  </label>
						  <label class="btn btn-primary">
						    <input type="radio" name="personalized-area-col-size"  value="4"> <i class="fa fa-th"></i>
						  </label>
						</div>
					</div>
					<div class="col-sm-6">
						
					</div>
				</div>				
				<div id="personalized-area" class="row blank-top-5">					
						<!-- start announce panel -->						
						<div id="announce-panel" class="custom-panels-group col-sm-6" style="display:none;">	
							<div class="panel panel-default">
								<div class="panel-heading">알림
									<div class="k-window-actions panel-header-actions">										
										<a role="button" href="#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-refresh">Refresh</span></a>
										<a role="button" href="#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-minimize">Minimize</span></a>
										<a role="button" href="#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-maximize">Maximize</span></a>										
										<a role="button" href="#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-close">Close</span></a>
									</div>									
								</div>
								<div class="panel-body">					
									<div  id="announce-view"></div>
								</div>
							</div>		
						</div>
					</div>										
				</div>
			</div>		
		<div id="attach-window"></div>					
		<!-- END MAIN CONTENT -->		
 		<!-- START FOOTER -->
		<#include "/html/common/common-homepage-footer.ftl" >		
		<!-- END FOOTER -->	
		
		<!-- START TEMPLATE -->				
		<script type="text/x-kendo-tmpl" id="attachment-list-view-template">
			<div class="attach">			
			#if (contentType.match("^image") ) {#
				<img src="${request.contextPath}/community/view-my-attachment.do?width=150&height=150&attachmentId=#:attachmentId#" alt="#:name# 이미지" class="img-responsive"/>
			# } else { #			
				<img src="http://placehold.it/146x146&amp;text=[file]"></a>
			# } #	
				<div class="attach-description">
					<h3>#:name#</h3>
					<p>#:size# 바이트</p>
				</div>
			</div>
		</script>	
		<script type="text/x-kendo-tmpl" id="photo-list-view-template">
			<div class="attach">			
			#if (contentType.match("^image") ) {#
				<img src="${request.contextPath}/community/view-my-image.do?width=150&height=150&imageId=#:imageId#" alt="#:name# 이미지" class="img-responsive"/>
			# } else { #			
				<img src="http://placehold.it/146x146&amp;text=[file]"></a>
			# } #	
				<div class="attach-description">
					<h3>#:name#</h3>
					<p>#:size# 바이트</p>
				</div>
			</div>
		</script>					
		<#include "/html/common/common-homepage-templates.ftl" >		
		<!-- END TEMPLATE -->
		<!-- START SIDE MENU -->
		<#include "/html/community/personalized-hompage-side-menu.ftl" >	
		<!-- END SIDE MENU -->	
	</body>    
</html>
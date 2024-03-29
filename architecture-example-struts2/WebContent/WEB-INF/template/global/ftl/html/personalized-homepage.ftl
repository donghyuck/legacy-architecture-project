<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title><#if action.user.company ?? >${action.user.company.displayName }<#else>::</#if></title>
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.0.3/font-awesome.min.css',
			'css!${request.contextPath}/styles/codedrop/cbpSlidePushMenus.css',
			'css!${request.contextPath}/styles/codedrop/fullscreenOverlay.css',
			'${request.contextPath}/js/jquery/1.9.1/jquery.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			'${request.contextPath}/js/kendo/kendo.web.js',
			'${request.contextPath}/js/kendo/kendo.ko_KR.js',			
			'${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js',
			'${request.contextPath}/js/bootstrap/3.1.0/bootstrap.min.js',
			'${request.contextPath}/js/pdfobject/pdfobject.js',
			'${request.contextPath}/js/common/common.models.js',
			'${request.contextPath}/js/common/common.api.js',
			'${request.contextPath}/js/common/common.ui.min.js',
			'${request.contextPath}/js/common/common.modernizr.custom.js',
			'${request.contextPath}/js/common/common.classie.min.js',
			'${request.contextPath}/js/jqeury.waypoints/waypoints.min.js'],
			complete: function() {			
			
				// 1.  한글 지원을 위한 로케일 설정
				kendo.culture("ko-KR");
				
				// 2.  MEUN 설정
				// START SCRIPT	
				//$("#top-menu").kendoMenu();
				//$("#top-menu").show();
																																									
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
	
				var slide_effect = kendo.fx($("body div.overlay")).fadeIn();
								
				$("#personalized-controls-menu").on( "click" , function(e){						
					$('body').toggleClass('modal-open');		
					
					if( $('#personalized-controls-section').hasClass("hide") )
						$('#personalized-controls-section').removeClass("hide");	
						
					$('body div.overlay').toggleClass('hide');		
					
					slide_effect.play().then(function(){							
						$('#personalized-controls-section').toggleClass('cbp-spmenu-open');
					});				
				});
				
				$("#personalized-controls-menu-close").on( "click" , function(e){						
					$('body').toggleClass('modal-open');		
					$('#personalized-controls-section').toggleClass('cbp-spmenu-open');					
					setTimeout(function() {
						slide_effect.reverse().then(function(){
						$('body div.overlay').toggleClass('hide');
						});
					}, 100);					
				});
								
				$('#personalized-area').waypoint(function(direction){				
					if( direction = 'down' ){
						 $('nav').first().toggleClass('hide');
						 $('.personalized-navbar').toggleClass('up');			
						 $('personalized-area').toggleClass('blank-top-50');			
					} else if (  direction = 'up' ){										
						$('nav').first().toggleClass('hide');		
						$('.personalized-navbar').toggleClass('up');			
						$('personalized-area').toggleClass('blank-top-50');					
					}
				}, { offset: 50 });												
											
				// 3. ACCOUNTS LOAD	
				var currentUser = new User({});			
				var accounts = $("#account-navbar").kendoAccounts({
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
							$("#account-navbar .custom-external-login-groups button").each(function( index ) {
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
													window.open( response.authorizationUrl + '&display=popup'  ,'popUpWindow','height=500,width=600,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no,status=yes')
												}
											},
											error:handleKendoAjaxError												
										});	
									});								
							});						
							var validator = $("#login-navbar").kendoValidator({validateOnBlur:false}).data("kendoValidator");
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
				
				// 1. Announces 							
				$("#announce-panel").data( "announcePlaceHolder", new Announce () );	
				createNoticeGrid();
																			
				// 4. Right Tabs

				/*$('#myTab a').click(function (e) {
					e.preventDefault();	
					$(this).tab('show');
				});	
				*/
								
				$('#myTab').on( 'show.bs.tab', function (e) {
					//e.preventDefault();		
					var show_bs_tab = $(e.target);
					if( show_bs_tab.attr('href') == '#my-notice' ){						
						createNoticeGrid();											
					} else if( show_bs_tab.attr('href') == '#my-streams' ){						
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
								height: 150
							});	
						}
					} else if( show_bs_tab.attr('href') == '#my-files' ){					
						if( !$('#attachment-list-view').data('kendoListView') ){														
							var attachementTotalModle = kendo.observable({ 
								totalAttachCount : "0",
								totalImageCount : "0",
								totalFileCount : "0"							
							});							
							kendo.bind($("#attachment-list-filter"), attachementTotalModle );													
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
												 return { startIndex: options.skip, pageSize: options.pageSize }
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
							$("#attachment-list-view").on("mouseenter",  ".img-wrapper", function(e) {
									kendo.fx($(e.currentTarget).find(".img-description")).expand("vertical").stop().play();
								}).on("mouseleave", ".img-wrapper", function(e) {
									kendo.fx($(e.currentTarget).find(".img-description")).expand("vertical").stop().reverse();
							});															
														
							$("input[name='attachment-list-view-filters']").on("change", function () {
								var attachment_list_view = $('#attachment-list-view').data('kendoListView');
								switch(this.value){
									case "all" :
										attachment_list_view.dataSource.filter(  { field: "contentType", operator: "neq", value: "" } ) ; 
										break;
									case "image" :
										attachment_list_view.dataSource.filter( { field: "contentType", operator: "startswith", value: "image" }) ; 
										break;
									case "file" :
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
										$("#my-files .side1").toggleClass("hide");										
										$("#my-files .side2").toggleClass("hide");										
									});									
								}else if (control_button_icon.hasClass("fa-th-list")){
									control_button.click( function(e){		
										$("#my-files .side1").toggleClass("hide");										
										$("#my-files .side2").toggleClass("hide");										
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
										read: { url:'${request.contextPath}/community/list-my-image.do?output=json', type: 'POST' },
										parameterMap: function (options, operation){
											if (operation != "read" && options) {										                        								                       	 	
												return { imageId :options.imageId };									                            	
											}else{
												 return { startIndex: options.skip, pageSize: options.pageSize }
											}
										}
									},
									pageSize: 12,
									error:handleKendoAjaxError,
									schema: {
										model: Image,
										data : "targetImages",
										total : "totalTargetImageCount"
									},
									/*sort: { field: "imageId", dir: "desc" },*/
									serverPaging: true
								},
								selectable: "single",									
								change: function(e) {									
									var data = this.dataSource.view() ;
									var item = data[this.select().index()];									
									item.index = this.select().index();			
									item.page = $("#photo-list-pager").data("kendoPager").page();				
									item.photoUrl = '${request.contextPath}/community/view-my-image.do?imageId=' +item.imageId ;
									item.photoId = 'photo-' + +item.imageId ;								
									item.photoIdLink = '#' + item.photoId;									
									item.previous = ( item.index > 0 || item.page > 1 ) ;
									item.next = true ;
									
									$("#photo-list-view").data( "photoPlaceHolder", item );														
									displayPhotoPanel( ) ;										
								},
								navigatable: false,
								template: kendo.template($("#photo-list-view-template").html()),								
								dataBound: function(e) {
								}
							});														
							$("#photo-list-view").on("mouseenter",  ".img-wrapper", function(e) {
									kendo.fx($(e.currentTarget).find(".img-description")).expand("vertical").stop().play();
								}).on("mouseleave", ".img-wrapper", function(e) {
									kendo.fx($(e.currentTarget).find(".img-description")).expand("vertical").stop().reverse();
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
										$("#my-photo-stream .side1").toggleClass("hide");										
										$("#my-photo-stream .side2").toggleClass("hide");										
									});									
								}else if (control_button_icon.hasClass("fa-th-list")){
									control_button.click( function(e){		
										$("#my-photo-stream .side1").toggleClass("hide");										
										$("#my-photo-stream .side2").toggleClass("hide");										
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
		
		function createNoticeGrid(){
			if( !$("#announce-grid").data('kendoGrid') ){
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
							if( announcePlaceHolder.user.userId == $("#account-navbar").data("currentUser").userId ){
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

				$("#my-notice .btn-group button").each(function( index ) { 
					var control_button = $(this);								
					var control_button_icon = control_button.find("i");				
					if (control_button_icon.hasClass("fa-plus")){
						control_button.click( function(e){								
							// editer start .....							
																	
							if( $( "#announce-creator" ).children().length < 1 ){
								$("#announce-creator").data( "announcePlaceHolder" , new Announce() );	
								var announcePlaceHolder = $("#announce-creator").data( "announcePlaceHolder" );	
								var template = kendo.template($('#announcement-edit-template').html());
								$("#announce-creator").html( template(announcePlaceHolder) );
								kendo.bind($("#announce-creator"), announcePlaceHolder );			
								createEditor($("#announce-creator .editor"));	
								$("#announce-creator div button").each(function( index ) {			
									var panel_button = $(this);			
									if( panel_button.hasClass( 'custom-update') ){
										panel_button.click(function (e) { 
											e.preventDefault();					
											var data = $("#announce-creator").data( "announcePlaceHolder" );												
											var hasError = false;
											if( data.subject == null || data.subject == '' ){
												hasError = true;
											}											
											if( data.body == null || data.body == '' ){
												hasError = true;
											}											
											if( hasError ) {
												$("#announce-creator .form-group").addClass("has-error");
												var template = kendo.template('<div class="alert alert-danger">#:message#</div>');
												$("#announce-creator .status").html(template({message: "모든 항목을 입력하여 주세요. " }));
											}
											if( !hasError ) {											
												$.ajax({
													dataType : "json",
													type : 'POST',
													url : '${request.contextPath}/community/update-announce.do?output=json',
													data : { announceId: data.announceId, item: kendo.stringify( data ) },
													success : function( response ){		
														$('#announce-grid').data('kendoGrid').dataSource.read();	
														$("#my-notice .btn-group button").first().click();
													},
													error:handleKendoAjaxError
												});				
											}							
										} );
									}			
								} );									
							}else{
								$("#announce-creator .form-group").removeClass("has-error");
								$("#announce-creator .status").html("");
								$("#announce-creator").data( "announcePlaceHolder" ).reset();	
							}
							$("#my-notice .side1").toggleClass("hide");
							$("#my-notice .side2").toggleClass("hide");
						});						
					}else if (control_button_icon.hasClass("fa-th-list")){
						control_button.click( function(e){										
							$("#my-notice .side2").toggleClass("hide");
							$("#my-notice .side1").toggleClass("hide");
						});								
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
							kendo.fx($("#announce-panel" )).zoom("in").startValue(0).endValue(1).reverse().then( function(e){							
								$("#announce-panel" ).hide();
							});							
						}
					});
				});	
				
			}	
		}	
		
		
		/** Announce View Panel */		
		function createEditor( renderTo ){			
			if(!renderTo.data("kendoEditor") ){				
				renderTo.kendoEditor({
						tools : [
							'bold',
							'italic',
							
							/* 'underline', 
							'strikethrough',
							'justifyLeft',
							'justifyCenter',
							'justifyRight',
							'justifyFull',
							*/
							'insertUnorderedList',
							'insertOrderedList',
							/* 
							'indent',
							
							'outdent',*/
							'createLink',
							'unlink',
							'insertImage',
							/*
							'subscript',
							'superscript',
							'createTable',
							'addRowAbove',
							'addRowBelow',
							'addColumnLeft',
							'addColumnRight',
							'deleteRow',
							'deleteColumn',
							*/
							'viewHtml'
							/*,
							'formatting',
							'fontName',
							'fontSize',
							'foreColor',
							'backColor'*/
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
										objectId: $("#account-navbar").data("currentUser" ).company.companyId  
									}
								},						
								thumbnailUrl: function( path, name, imageId ){
									return "${request.contextPath}/community/download-image.do?width=150&height=150&imageId=" + imageId ;
								},
								uploadData : {
									objectType: 1,
									objectId: $("#account-navbar").data("currentUser" ).company.companyId  
								},	
								uploadUrl: "${request.contextPath}/community/imagebrowser-update.do?output=json",							
								imageUrl: function(path, imageId){
									return "${request.contextPath}/community/download-image.do?imageId=" + imageId;
								}
							}
						}
					});
				}		
		}
		
		function editAnnouncePanel (){		
			var announcePlaceHolder = $("#announce-panel").data( "announcePlaceHolder" );		
			if( announcePlaceHolder.modifyAllowed ){						
				var template = kendo.template($('#announcement-edit-template').html());
				$("#announce-view").html( template(announcePlaceHolder) );	
				kendo.bind($("#announce-view"), announcePlaceHolder );		
				createEditor($("#announce-panel .editor"));		
				$("#announce-view div button").each(function( index ) {			
					var panel_button = $(this);			
					if( panel_button.hasClass( 'custom-update') ){
						panel_button.click(function (e) { 
							e.preventDefault();					
							var data = $("#announce-panel").data( "announcePlaceHolder" );								
							//alert( kendo.stringify( data ) );
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
			kendo.fx($("#announce-panel")).zoom("in").startValue(0).endValue(1).play();
			
		}	
		
		
		function createPanel(){
					
			var renderTo = ui.generateGuid();
			var grid_col_size = $("#personalized-area").data("sizePlaceHolder");			
			var template = kendo.template($("#empty-panel-template").html());	
			$("#personalized-area").append( template( { id: renderTo, colSize: grid_col_size.newValue } ) );
			$( '#'+ renderTo + ' .panel-header-actions a').each(function( index ) {
				var panel_header_action = $(this);
				panel_header_action.click(function (e){
					e.preventDefault();		
					var panel_header_action_icon = panel_header_action.find('span');
					switch( panel_header_action_icon.text() )
					{
						case "Minimize" :
							$( "#"+ renderTo +" .panel-body").toggleClass("hide");		
							panel_header_action.toggleClass("hide");		
							break;
						case "Refresh" :
							break;
						case "Close" :							
							kendo.fx($( '#'+ renderTo )).zoom("in").startValue(0).endValue(1).reverse().then( function(e){							
								$("#" + renderTo ).remove();
							});							
							break;	
						case "Custom" :
							break;																		
					}
				});		
			});					
			
			//$( '#'+ renderTo ).show();
			kendo.fx($( '#'+ renderTo )).zoom("in").startValue(0).endValue(1).play();
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
						refresh : true, 
						afterChange : function ( data ){
							if( data.contentType == "application/pdf" ){
								var loadSuccess = new PDFObject({ url: "${request.contextPath}/community/view-my-attachment.do?attachmentId=" + data.attachmentId, pdfOpenParams: { view: "FitV" } }).embed("pdf-view");				
							}		
							
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
						},
						commands:[
							{ selector :   "#" + renderToString + " .panel-body:first .btn", 
							  handler : function(e){
								e.preventDefault();
								var _ele = $(this);
								if( _ele.hasClass( 'custom-delete') ){
									alert( $("#attachment-list-view").data( "attachPlaceHolder" ).attachmentId );
									/**
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
									*/								
								}
							}}
						]
					})
				 );				 
			}else{
				$("#" + renderToString ).data("extPanel").data(attachPlaceHolder);
			}			
			//CHANGE
			var panel = $("#" + renderToString ).data("extPanel");
			panel.show();		
		}	
						
		<!-- ============================== -->
		<!-- display photo  panel                                  -->
		<!-- ============================== -->
		
		
		function displayPhotoPanel(){			
		
			var renderToString =  "photo-panel-0";	
			var photoPlaceHolder = $("#photo-list-view").data( "photoPlaceHolder");		
			
			//alert(photoPlaceHolder.imageId);
			
			if( $("#" + renderToString ).length == 0  ){			
				var grid_col_size = $("#personalized-area").data("sizePlaceHolder");
				var template = kendo.template('<div id="#: panelId #" class="custom-panels-group col-sm-#: colSize#" style="display:none;"></div>');				
				$("#personalized-area").append( template( {panelId:renderToString, colSize: grid_col_size.newValue } ) );	
			}				
			
			if( !$("#" + renderToString ).data("extPanel") ){					
				$("#" + renderToString ).data("extPanel", 
					$("#" + renderToString ).extPanel({
						template : kendo.template($("#photo-panel-template").html()),
						data : photoPlaceHolder,
						commands:[
							{ selector :   "#" + renderToString + " .panel-body:first .btn", 
							  handler : function(e){
								e.preventDefault();
								var _ele = $(this);
								if( _ele.hasClass( 'custom-delete') ){
									//alert( $("#photo-list-view").data( "photoPlaceHolder").imageId );
									/**
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
									*/								
								}
							}}
						]
					})
				 );					
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
				
				$("#" + renderToString ).data("extPanel").body().find('.pager li').each(function( index ) { 
					var panel_pager = $(this);	
					if( panel_pager.hasClass('previous') ){
						panel_pager.click(function (e) { 
							e.preventDefault();								
							previousPhoto();
						});
					}else if ( panel_pager.hasClass('next') ){ 
						panel_pager.click(function (e) { 
							e.preventDefault();
							nextPhoto();
						});
					}	
				});	 	
				 
			}else{
				$("#" + renderToString ).data("extPanel").data(photoPlaceHolder);
				kendo.bind($("#" + renderToString ).data("extPanel").body(), $("#" + renderToString ).data("extPanel").data());
			}			
			var panel = $("#" + renderToString ).data("extPanel");
			panel.show();			
		}	
												
		function previousPhoto (){
			var current_index = $("#photo-list-view").data( "photoPlaceHolder").index;				
			var previous_index = current_index - 1;	
			var listView =  $('#photo-list-view').data('kendoListView');	
			var list_view_pager = $("#photo-list-pager").data("kendoPager");
			var current_page = list_view_pager.page();						
			if( current_index > 0 ){							
				var item = listView.dataSource.view()[previous_index];
				item.index = previous_index;		
				item.page = current_page ;		
				item.photoUrl = '${request.contextPath}/community/view-my-image.do?imageId=' +item.imageId ;
				item.photoId = '#photo-' + +item.imageId ;				
				item.photoIdLink = '#' + item.photoId;										
				item.previous = ( item.index > 0 || item.page > 1 ) ;
				item.next = true ;									
				$("#photo-list-view").data( "photoPlaceHolder", item );														
				displayPhotoPanel( );
			} else {
				if( current_page > 1 ){
					list_view_pager.page(current_page - 1);
					listView.select(listView.element.children().last());
				}
			}		
		}
		
		function nextPhoto (){
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
				item.photoUrl = '${request.contextPath}/community/view-my-image.do?imageId=' +item.imageId ;
				item.photoId = '#photo-' + +item.imageId ;					
				item.photoIdLink = '#' + item.photoId;									
				item.previous = ( item.index > 0 || item.page > 1 ) ;
				item.next = true ;										
				$("#photo-list-view").data( "photoPlaceHolder", item );
				displayPhotoPanel( );						
			}else {
				if( current_page <  list_view_pager.totalPages() ){
					list_view_pager.page(current_page+1);
					listView.select(listView.element.children().first());
				}
			}						
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
			right : 0px;
			text-align: center;
			padding: 34px 10px 10px;
			-webkit-border-radius: 18px;
			-moz-border-radius: 18px;
			border-radius: 18px;
			margin: 0 0 10px;
			position: relative;
			z-index: 9000;
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
		
		
		
		 #photo-gallery-view {
			min-height: 320px;
			min-width: 320px;
			width: 100%;
			padding: 0px;
			border: 0px;
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
		
		/** image grid  */		
		#photo-list-view.k-listview ,#attachment-list-view.k-listview {
			width: 100%;
			padding: 0px;
			border: 0px;		
			min-height: 200px;
		}

		.img-wrapper {
			float: left;
			position: relative;
			width: 32.99%;
			height: 170px;
			padding: 0;
			cursor: pointer;
			overflow: hidden;		
		}
		
		.img-wrapper img{
			width: 100%;
			height: 100%;
		}
		
		.img-description {
			position: absolute;
			top: 0;
			width: 100%	;
			height: 0;
			overflow: hidden;
			background-color: rgba(0,0,0,0.8)
		}
	
		.img-wrapper h3
		{
			margin: 0;
            padding: 10px 10px 0 10px;
            line-height: 1.1em;
            font-size : 12px;
            font-weight: normal;
            color: #ffffff;
            word-wrap: break-word;
		}

		.img-wrapper p {
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
			background : transparent;
		}

		table.k-editor {
			height: 400px;
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
		
		.navbar{
			margin-bottom: 0px;
		}
				
		#personalized-controls {
			position: absolute;
			top: 50px;
			left:0;
			min-height: 300px;
			padding: 10px;
			width: 100%;
			z-index: 1000;
			overflow: hidden;
			background-color: rgba(91,192,222,0.8)		
		}		
		
		#personalized-controls-section{
			margin-top: 0px;
			padding : 0px;
		}
		
		#personalized-controls-section.cbp-spmenu-vertical {
			width: 565px;
		}
		
		#personalized-controls-section.cbp-spmenu-right {
			right: -565px;
			z-index: 2000;
		}
		
		#personalized-controls-section.cbp-spmenu-right.cbp-spmenu-open {
			right : 0px;
			overflow-x:hidden;
			overflow-y:auto;			
		}

		@media (max-width: 768px ) {
			#personalized-controls-section.cbp-spmenu-vertical {
				width: 100%;
			}			
			#personalized-controls-section.cbp-spmenu-right {
				right: -100%;
			}		
		} 
		
		.cbp-spmenu {
			background : #ffffff;
		}
		
		.cbp-spmenu-vertical header {
			1px solid #258ecd;
			margin : 0px;
			padding : 5px;
			color : #000000;
			background : #5bc0de; /* transparent;        	*/
			height: 90px;        	
		}

		.overlay-hugeinc {
			display:none;
			position: relative;
		}
		
		.overlay-hugeinc.open{
			display:block;
		}
		
		
		.cbp-hsmenu-wrapper .cbp-hsmenu {
			width:100%;
		}
		
		.cbp-hsmenu > li > a {
			color: #fff;
			font-size: 1em;
			line-height: 3em;
			display: inline-block;
			position: relative;
			z-index: 10000;
			outline: none;
			text-decoration: none;
		}
		
		</style>   	
	</head>
	<body id="doc" class="bg-gray">
		<!-- START HEADER -->
		<#include "/html/common/common-homepage-menu.ftl" >			
		<nav class="personalized-navbar navbar" role="navigation">
			<div class="container">
				<ul class="nav navbar-nav navbar-right">					
					<li class="navbar-btn hidden-xs">
						<div class="btn-group navbar-btn" data-toggle="buttons">
							<label class="btn btn-info">
								<input type="radio" name="personalized-area-col-size" value="12"><i class="fa fa-square"></i>
							</label>
							<label class="btn btn-info active">
						 		<input type="radio" name="personalized-area-col-size"  value="6"> <i class="fa fa-th-large"></i>
							</label>
							<label class="btn btn-info">
								<input type="radio" name="personalized-area-col-size"  value="4"> <i class="fa fa-th"></i>
							</label>
						</div>										
					</li>
					<p class="navbar-text">&nbsp;</p>
					<li><a href="#" class="btn-img"><i class="fa fa-chevron-up"></i></a></li>
					<li><a href="#" class="btn-img"><i class="fa fa-chevron-down"></i></a></li>
					<li><a  id="personalized-controls-menu" href="#" class="btn-img"><i class="fa fa-cog"></i></a></li>
				</ul>
			</div>
		</nav>
		<!-- END HEADER -->	
		<!-- START MAIN CONTENT -->
		<section class="container" style="min-height:600px;">		
			<div id="personalized-area" class="row blank-top-50">				
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
		</section>		
		<div class="overlay hide"></div>
		
		<!-- start side menu -->
		<section class="cbp-spmenu cbp-spmenu-vertical cbp-spmenu-right hide"  id="personalized-controls-section">			
			<header>							
				<div class="btn-group">
					<button type="button" class="btn btn-info"><i class="fa fa-cog"></i></button>
					<button type="button" class="btn btn-info"><i class="fa fa-comment"></i></button>
					<button type="button" class="btn btn-info"><i class="fa fa-envelope"></i></button>
				</div>			
				<!--<button id="personalized-controls-menu-close" type="button" class="close" aria-hidden="true"><i class="fa fa-times fa-2x"></i></button>-->
				
				<button id="personalized-controls-menu-close" type="button" class="btn-close">Close</button>
			</header>
					
			<!--
			<div class="blank-space-5">	
				<nav class="cbp-hsmenu-wrapper" id="cbp-hsmenu-wrapper">
					<div class="cbp-hsinner">
						<ul class="cbp-hsmenu">
							<li>
								<a href="#">응용프로그램</a>
								<ul class="cbp-hssubmenu cbp-hssub-rows">
									<li><a href="#"><i class="fa fa-picture-o fa-2x"></i><span>이미지</span></a></li>
									<li><a href="#"><img src="images/2.png" alt="img02"/><span>파일</span></a></li>
									<li><a href="#"><img src="images/3.png" alt="img03"/><span>Heavenly Ale</span></a></li>
									<li><a href="#"><img src="images/4.png" alt="img04"/><span>Juicy Lemonade</span></a></li>
									<li><a href="#"><img src="images/5.png" alt="img05"/><span>Wise Whiskey</span></a></li>
								</ul>
							</li>
						</ul>		
					</div>
				</nav>
				-->
				<div class="blank-top-5" ></div>	
									<ul class="nav nav-tabs" id="myTab" style="padding-left:5px;">
										<li><a href="#my-notice" tabindex="-1" data-toggle="tab">공지 & 이벤트</a></li>	
										<li><a href="#my-streams" tabindex="-1" data-toggle="tab">쇼셜</a></li>							
										<#if !action.user.anonymous >	
										<li><a href="#my-photo-stream" tabindex="-1" data-toggle="tab">포토</a></li>
										<li><a href="#my-files" tabindex="-1" data-toggle="tab">파일</a></li>							
										</#if>						
									</ul>								
									<div class="tab-content" style="background-color : #FFFFFF; padding:5px;">	
										<div class="tab-pane" id="my-notice">
											<section class="side2 hide">
												<div class="btn-group">			
													<button type="button" class="btn btn-info"><i class="fa fa-th-list"></i>&nbsp;  목록보기</button>
												</div>
												<div id="announce-creator" class="blank-top-15" ></div>
											</section>
											<section class="side1">
												<div class="btn-group">			
													<button type="button" class="btn btn-info"><i class="fa fa-plus"></i> &nbsp; 공지 & 이벤트 추가</button>		
												</div>		
												<div class="blank-top-5" ></div>
												<div id="announce-grid"></div>
											</section>											
										</div>
										<!-- start social tab-content -->		
										<div class="tab-pane" id="my-streams">							
											<table id="my-social-streams-grid">
												<colgroup>
													<col/>
												</colgroup>
												<thead>
													<tr>
														<th>미디어</th>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td></td>
													</tr>
												</tbody>
											</table>												
										<div class="blank-top-5" ></div>				
										<div class="alert alert-flat alert-info fade in">
											<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
											<p><i class="fa fa-info"></i>쇼셜 미디어를 선택하시면, 해당 미디어의 최신 뉴스를 볼수 있습니다.  미디어 추가는 프로파일의 쇼셜네크워크에서 제공합니다. </p>
										</div>		
									</div><!-- end socia tab-content -->				
									<!-- start attachement tab-pane -->
									<div class="tab-pane" id="my-files">
										<section class="side1">
											<div class="btn-group">
												<button type="button" class="btn btn-info"><i class="fa fa-upload"></i> &nbsp; 파일업로드</button>	
											</div>			
											<div class="btn-group" data-toggle="buttons" id="attachment-list-filter">
												<label class="btn btn-warning active">
													<input type="radio" name="attachment-list-view-filters"  value="all"> 전체 (<span data-bind="text: totalAttachCount"></span>)
												</label>
												<label class="btn btn-warning">
													<input type="radio" name="attachment-list-view-filters"  value="image"><i class="fa fa-filter"></i> 이미지
												</label>
												<label class="btn btn-warning">
													<input type="radio" name="attachment-list-view-filters"  value="file"><i class="fa fa-filter"></i> 파일
												</label>	
											</div>
											<div class="blank-top-5" ></div>		
											<div class="panel panel-default panel-flat">
												<div class="panel-body scrollable" style="max-height:450px;">
													<div id="attachment-list-view" ></div>
												</div>	
												<div class="panel-footer" style="padding:0px;">
													<div id="pager" class="k-pager-wrap"></div>
												</div>
											</div>																							
										</section>
										<section class="side2 hide">
											<div class="btn-group">			
												<button type="button" class="btn btn-info"><i class="fa fa-th-list"></i>&nbsp; 목록보기</button>		
											</div>									
											<div class="blank-top-5 "></div>
											<#if !action.user.anonymous >									
											<input name="uploadAttachment" id="attachment-files" type="file" />				
											<div class="alert alert-info alert-flat"><strong>파일 선택</strong> 버튼을 클릭하여 직접 파일을 선택하거나, 아래의 영역에 파일을 끌어서 놓기(Drag & Drop)를 하세요.</div>					
											</#if>									
										</section>																													
									</div><!-- end attachements  tab-pane -->		
									<!-- start photos  tab-pane -->
									<div class="tab-pane" id="my-photo-stream">
										<section class="side1">
											<div class="btn-group">			
												<button type="button" class="btn btn-info"><i class="fa fa-upload"></i> &nbsp; 사진업로드</button>		
											</div>		
											<div class="blank-top-5" ></div>
											<div class="panel panel-default panel-flat">								
												<div class="panel-body scrollable" style="max-height:450px;">
													<div id="photo-list-view" ></div>
												</div>	
												<div class="panel-footer" style="padding:0px;">
													<div id="photo-list-pager" class="k-pager-wrap"></div>
												</div>
											</div>																
										</section>							
										<section class="side2 hide">
											<div class="btn-group">			
												<button type="button" class="btn btn-info"><i class="fa fa-th-list"></i>&nbsp; 목록보기</button>			
											</div>									
											<div id="my-photo-upload">
												<#if !action.user.anonymous >		
												<div class="blank-top-5 "></div>	
												<input name="uploadPhotos" id="photo-files" type="file" />	
												<div class="alert alert-info alert-flat"><strong>사진 선택</strong> 버튼을 클릭하여 사진을 직접 선택하거나, 아래의 영역에 사진를 끌어서 놓기(Drag & Drop)을 끌어서 놓기(Drag & Drop)를 하세요.</div>
												</#if>							
											</div>																		
										</section>				
									</div><!-- end photos  tab-pane -->
								</div><!-- end of tab content -->					
			</div>	
		</section>
		
					
		<!-- END MAIN CONTENT -->		
 		<!-- START FOOTER -->
		<#include "/html/common/common-homepage-footer.ftl" >		
		<!-- END FOOTER -->	
		
		<!-- START TEMPLATE -->				
		<script type="text/x-kendo-template" id="empty-panel-template">			
			<div id="#: id #" class="custom-panels-group col-sm-#: colSize#" style="min-height:200px; display:none;" data-role="panel">					
				<div class="panel panel-flat panel-default">
					<div class="panel-heading">페널
						<div class="k-window-actions panel-header-actions">		
							<a role="button" href="\\#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-custom">Custom</span></a>								
							<a role="button" href="\\#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-refresh">Refresh</span></a>
							<a role="button" href="\\#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-minimize">Minimize</span></a>
							<a role="button" href="\\#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-maximize">Maximize</span></a>										
							<a role="button" href="\\#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-close">Close</span></a>
						</div>									
					</div>
					<div class="panel-body hide"></div>
					<div class="panel-body"></div>			
				</div>
			</div>			
		</script>
				
		<script type="text/x-kendo-tmpl" id="attachment-list-view-template">
			<div class="img-wrapper">			
			#if (contentType.match("^image") ) {#
				<img src="${request.contextPath}/community/view-my-attachment.do?width=150&height=150&attachmentId=#:attachmentId#" alt="#:name# 이미지" />
			# } else { #			
				<img src="http://placehold.it/146x146&amp;text=[file]"></a>
			# } #	
				<div class="img-description">
					<h3>#:name#</h3>
					<p>#:size# 바이트</p>
				</div>
			</div>
		</script>	
		<script type="text/x-kendo-tmpl" id="photo-list-view-template">
			<div class="img-wrapper">			
			#if (contentType.match("^image") ) {#
				<img src="${request.contextPath}/community/view-my-image.do?width=150&height=150&imageId=#:imageId#" alt="#:name# 이미지" />
			# } else { #			
				<img src="http://placehold.it/146x146&amp;text=[file]"></a>
			# } #	
				<div class="img-description">
					<h3>#:name#</h3>
					<p>#:size# 바이트</p>
				</div>
			</div>
		</script>					
		<#include "/html/common/common-homepage-templates.ftl" >		
		<!-- END TEMPLATE -->
	</body>    
</html>
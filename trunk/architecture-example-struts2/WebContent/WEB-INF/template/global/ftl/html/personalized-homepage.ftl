<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title><#if action.user.company ?? >${action.user.company.displayName }<#else>::</#if></title>
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/bootstrap/3.0.0/font-awesome.css',			
			'css!${request.contextPath}/styles/bootstrap/3.0.0/social-buttons.css',
			'${request.contextPath}/js/jquery/1.9.1/jquery.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo/kendo.ko_KR.js',			
			'${request.contextPath}/js/kendo/cultures/kendo.culture.en-US.min.js',		
			'${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js',			
			'${request.contextPath}/js/bootstrap/3.0.0/bootstrap.min.js',
			'${request.contextPath}/js/bootstrap/3.0.0/tooltip.js',			
			'${request.contextPath}/js/common/holder.js',
			'${request.contextPath}/js/common/common.models.js',
			'${request.contextPath}/js/common/common.ui.js'],
			complete: function() {
			
				// 1.  한글 지원을 위한 로케일 설정
				kendo.culture("ko-KR");
				      
				// START SCRIPT	
				$("#top-menu").kendoMenu();
				$("#top-menu").show();
				var currentUser = new User({});			
				// ACCOUNTS LOAD	
				var accounts = $("#account-panel").kendoAccounts({
					dropdown : false,
					authenticate : function( e ){
						currentUser = e.token;						
					},
					<#if CompanyUtils.isallowedSignIn(action.company) ||  !action.user.anonymous  >
					template : kendo.template($("#account-template").html()),
					</#if>
					afterAuthenticate : function(){
						$('.dropdown-toggle').dropdown();
						if( currentUser.anonymous ){
							var validator = $("#login-panel").kendoValidator({validateOnBlur:false}).data("kendoValidator");							
							$("#login-btn").click(function() { 
								$("#login-status").html("");
								if( validator.validate() )
								{								
									accounts.login({
										data: $("form[name=login-form]").serialize(),
										success : function( response ) {
											$("form[name='login-form']")[0].reset();               
											$("form[name='login-form']").attr("action", "/main.do").submit();										
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
				
				// 1. Announces 
				$("#announce-panel").data( "announcePlaceHolder", new Announce () );
				$("#announce-panel").data( "dataSource", 
	 				new kendo.data.DataSource({
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
						requestStart: function() {
							kendo.ui.progress($("#announce-panel"), true);
						},
						requestEnd: function() {
							kendo.ui.progress($("#announce-panel"), false);
						},
						change: function(e) {							
							if( this.hasChanges()){
								$(".custom-announce-modify").removeAttr("disabled");
							}else{
								$("#announce-panel table tbody").html(kendo.render(kendo.template($("#announcement-template").html()), this.view()));
								if( this.data().length > 0 ){
									viewAnnounce (this.at(0).announceId) ;
								}
							}
						},
						error:handleKendoAjaxError,
						schema: {
							data : "targetAnnounces",
							model : Announce
						}
					})
				);								
								
				$("#announce-panel .panel-header-actions a").each(function( index ) {
						var panel_header_action = $(this);						
						if( panel_header_action.text() == "Minimize" ){
							panel_header_action.click(function (e) {
								e.preventDefault();		
								$("#announce-panel .panel-body").toggleClass("hide");								
								var panel_header_action_icon = panel_header_action.find('span');
								if( panel_header_action_icon.hasClass("k-i-minimize") ){
									panel_header_action.find('span').removeClass("k-i-minimize");
									panel_header_action.find('span').addClass("k-i-maximize");
								}else{
									panel_header_action.find('span').removeClass("k-i-maximize");
									panel_header_action.find('span').addClass("k-i-minimize");
								}								
							});
						} else if (panel_header_action.text() == "Refresh" ){
							panel_header_action.click(function (e) {
								e.preventDefault();		
								$("#announce-panel").data( "dataSource").read();
							});
						}
				} );								
				
				$("#announce-panel").data( "dataSource").read();
				// social data sources
				$("#social-view-panels").data( "providers", new kendo.data.ObservableObject({}) );
				<#list action.companySocials as item >
					<#assign elementId = "'#" + item.serviceProviderName + "-streams'"  />					
					$("#social-view-panels").data( "providers").set( "${item.serviceProviderName}" ,  {
						<#if  item.serviceProviderName == "twitter" >
						template : kendo.template($("#twitter-timeline-template").html())	,			
						<#elseif item.serviceProviderName == "facebook" >
						template : kendo.template($("#facebook-homefeed-template").html()),
						</#if>		
						dataSource : new kendo.data.DataSource({
							transport: {
								read: {
									type : 'POST',
									type: "json",
									<#if  item.serviceProviderName == "twitter" >
									url : '${request.contextPath}/social/get-twitter-hometimeline.do?output=json',			
									<#elseif item.serviceProviderName == "facebook" >
									url : '${request.contextPath}/social/get-facebook-homefeed.do?output=json',
									</#if>	
								},
								parameterMap: function (options, operation){
									if (operation == "read" && options) {										                        								                       	 	
										return { socialAccountId: ${ item.socialAccountId } };									                            	
									}
								} 
							},
							requestStart: function() {
								kendo.ui.progress($(${elementId}), true);
							},
							requestEnd: function() {
								kendo.ui.progress($(${elementId}), false);
							},
							change: function() {
								$(${elementId}).html(kendo.render( $("#social-view-panels").data( "providers").get( "${item.serviceProviderName}" ).template, this.view()));
							},
							error:handleKendoAjaxError,
							schema: {
							<#if  item.serviceProviderName == "twitter" >
								data : "homeTimeline"
							<#elseif item.serviceProviderName == "facebook" >
								data : "homeFeed"
							</#if>	
							}						
						})
				});							
				</#list>
								
								
				$.each( $('#my-messages').find( '.social-connect-btn button' ) , function ( i, item ){					
					$(item).click( function(){ 
						var socialProvider = $(item).attr('data-provider');
						if( typeof (socialProvider) == 'string' ){
 							showSocialPanel( socialProvider );	
 						}
 					});	
				});				
				
				$("#attach-view-panel").data( "attachPlaceHolder", new Image () );								
				$('#myTab a').click(function (e) {
					e.preventDefault();					
					if(  $(this).attr('href') == '#my-messages' ){					
						
					} else if(  $(this).attr('href') == '#my-attachments' ){
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
									//this.dataSource.page();
									var item = data[this.select().index()];		
									$("#attach-view-panel").data( "attachPlaceHolder", item );														
									openPreviewWindow( ) ;	
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
							// ListView Filter 									
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
							// Attachment Pager
							$("#pager").kendoPager({
								refresh : true,
								buttonCount : 5,
								dataSource : $('#attachment-list-view').data('kendoListView').dataSource
							});													
							$("#attachment-files").kendoUpload({
								 	multiple : false,
								 	width: 300,
								 	showFileList : false,
								    localization:{ select : '파일 업로드' , dropFilesHere : '업로드할 파일을 이곳에 끌어 놓으세요.' },
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
					} else if(  $(this).attr('href') == '#my-photos' ){
					
					}
					$(this).tab('show')
				});				
				// END SCRIPT            
			}
		}]);	
						
		function showSocialPanel ( provider ){			
			var elementId =  provider + "-panel";			
			if( $("#" + elementId ).length == 0  ){						
				// create new social panel 
				var template = kendo.template($("#social-view-panel-template").html());		
				$("#social-view-panels").append( template( { provider:provider} ) );
												
				// get dataSource
				var dataSource = $("#social-view-panels").data( "providers").get( provider ).dataSource;
				if( dataSource.total() == 0 )
				{
					dataSource.read();
				} 								
				$( '#'+ elementId + ' .panel-header-actions a').each(function( index ) {
					var social_header_action = $(this);
					social_header_action.click(function (e){
						e.preventDefault();		
						var social_header_action_icon = social_header_action.find('span');
						if (social_header_action.text() == "Minimize"){
							$( "#"+ elementId +" .panel-body").toggleClass("hide");				
							if( social_header_action_icon.hasClass("k-i-maximize") ){
								social_header_action_icon.removeClass("k-i-maximize");
								social_header_action_icon.addClass("k-i-minimize");
							}else{
								social_header_action_icon.removeClass("k-i-minimize");
								social_header_action_icon.addClass("k-i-maximize");
							}
						} else if (social_header_action.text() == "Refresh"){	
							socialServiceProviders[ provider ].dataSource.read();
						} else if (social_header_action.text() == "Close"){	
							$("#" + elementId ).hide();
						}
					});			
				} );			
			} else {
				$("#" + elementId ).show();
			} 
		}		
		
		function openPreviewWindow(){						
			var attachPlaceHolder = $("#attach-view-panel").data( "attachPlaceHolder");
			var template = kendo.template($('#image-view-template').html());
			$('#attach-view-panel').html( template(attachPlaceHolder) );				
			kendo.bind($("#attach-view-panel"), attachPlaceHolder );		
						
			$("#attach-view-panel button").each(function( index ) {		
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
								$('#announce-panel').show();
								$('#attach-view-panel').hide();
							},
							error:handleKendoAjaxError
						});	
					}
					if( panel_button.hasClass( 'close') ){
						$('#announce-panel').show();
						$('#attach-view-panel').hide();						
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
			
			$('#attach-view-panel').show();
			$('#announce-panel').hide();	
		}		
				
		function viewAnnounce (announceId){							
			var item = $("#announce-panel").data( "dataSource").get(announceId);				
			var announcePlaceHolder = $("#announce-panel").data( "announcePlaceHolder" );
			announcePlaceHolder.announceId = item.announceId;
			announcePlaceHolder.subject = item.subject;
			announcePlaceHolder.body = item.body;
			announcePlaceHolder.startDate = item.startDate ;
			announcePlaceHolder.endDate = item.endDate;
			announcePlaceHolder.modifiedDate = item.modifiedDate;
			announcePlaceHolder.creationDate = item.creationDate;
			announcePlaceHolder.user = item.user;			
			var observable = new kendo.data.ObservableObject(announcePlaceHolder);
			observable.bind("change", function(e) {				
				$(".custom-announce-modify").removeAttr("disabled");
			});																		
			var template = kendo.template($('#announcement-view-template').html());			
			$("#announce-view").html(
				template(announcePlaceHolder)
			);			
			kendo.bind($("#announce-view"), announcePlaceHolder );					
			$("#announce-view div button").each(function( index ) {			
				var announce_button = $(this);			
				if( announce_button.hasClass( 'custom-announce-modify') ){
					announce_button.click(function (e) { 
						e.preventDefault();					
						var updateId = announce_button.attr('data-announceId');
						var updateItem = $("#announce-panel").data( "dataSource").get(updateId);		
							alert(  kendo.stringify( announcePlaceHolder ) );
						$.ajax({
								dataType : "json",
								type : 'POST',
								url : '${request.contextPath}/community/update-announce.do?output=json',
								data : { announceId: announcePlaceHolder.announceId, item: kendo.stringify( announcePlaceHolder ) },
								success : function( response ){		
									$("#announce-panel").data( "dataSource").read();
								},
								error:handleKendoAjaxError
						});	
						
					} );
				}else if ( announce_button.hasClass('custom-announce-delete') ){
					announce_button.click(function (e) { 
						e.preventDefault();
						if( confirm("삭제하시겠습니까 ?") ) {
							// delete ...	
						}
					} );
				}			
			} );
		}			
		-->
		</script> 		   
		
		<style scoped="scoped">

		#social-meida-panel .popover {
			font-family: "나눔 고딕", "BM_NANUMGOTHIC";
			width: 100%;
			margin-top: 20px;
			margin-right: 20px;
			margin-bottom: 20px;
			margin-left: 20px;
			float: left;
			display: block;
			position: relative;
			z-index: 1;
			max-width: 340px;
		 }
		 
		 .popover-title {
			font-family: "나눔 고딕", "BM_NANUMGOTHIC";
		 }
	 		
		.carousel {
		margin-top: 60px;		
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
		
		#attachment-list-view {
			min-height: 300px;
			min-width: 300px;
			padding: 5px;
			border: 0px;
			margin-bottom: -1px;
		}
        		                		
		.attach
		{
			float: left;
            position: relative;
            width: 150px;
            height: 150px;
            padding: 0;
			cursor: pointer;
		}
		
		.attach img
		{
			width: 150px;
			height: 150px;
		}
		
		.attach-description {
            position: absolute;
            top: 0;
            width: 150px	;
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
		
		</style>   	
	</head>
	<body id="doc">
		<!-- START HEADER -->
		<#include "/html/common/common-homepage-menu.ftl" >	
		<!-- END HEADER -->	
		<!-- START MAIN CONTENT -->
			<div class="container layout">							
				<div class="row">					
					<div class="col-lg-8">						
						<!-- start announce panel -->
						<div id="announce-panel" >	
							<div class="panel panel-default">
								<div class="panel-heading">알림
									<div class="k-window-actions panel-header-actions">
										<a role="button" href="#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-refresh">Refresh</span></a>
										<a role="button" href="#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-minimize">Minimize</span></a>
										<a role="button" href="#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-maximize">Maximize</span></a>
										<a role="button" href="#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-close">Close</span></a>
									</div>
								</div>
								<div class="panel-body layout">					
									<div  id="announce-view"></div>
									<br><br>
									<table class="table table-hover">
										<tbody>										
											<tr>
												<th></th>	
												<td></td>
											</tr>										
										</tbody>
									</table>												
								</div>
							</div>		
						</div>
						<!-- end announce panel -->			
						
						<!-- start image view panel -->
						<div id="attach-view-panel"></div>				
						<!-- end image view panel -->		
						<!-- start social view panels -->
						<div id="social-view-panels"></div>	
						<!-- end social view panels -->						
					</div>							
					<div class="col-lg-4">
						<ul class="nav nav-tabs" id="myTab">
							<li class="active"><a href="#my-messages">My 쇼셜</a></li>
							<li><a href="#my-attachments">My 파일</a></li>
							<li><a href="#my-images">My 포토</a></li>
						</ul>								
						<!-- start  of tab content -->				
						<div class="tab-content">			
							<!-- start messages -->				
							<div class="tab-pane active" id="my-messages">							
								<div class="blank-top-15" ></div>		
								<div class="panel panel-default">
									<div class="panel-heading">소셜미디어 연결 버튼을 클릭하여, 새로운 쇼셜미디어 계정을 추가할 수 있습니다.</div>
									<div class="panel-body">								
										<div class="btn-group">
											<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
											쇼셜미디어 추가 <span class="caret"></span>
											</button>
											<ul class="dropdown-menu" role="menu">
												<li><a href="#"><i class="icon-facebook"></i> &nbsp;페이스북 연결</a></li>
												<li><a href="#"><i class="icon-twitter"></i> &nbsp;트위터 연결</a></li>
												<li class="divider"></li>
												<li><a href="#">쇼셜미디어 계정 관리</a></li>
											</ul>
										</div>
									</div>
								</div>
								<div class="panel panel-primary">
									<div class="panel-heading"> 쇼셜미디어 버튼을 클릭하면, 쇼셜미디어 최신 뉴스를 볼수 있습니다.</div>
									<div class="panel-body">
										<div class="btn-group social-connect-btn">
											<#list action.companySocials as item >	
											<button class="btn btn-primary" data-provider="${item.serviceProviderName}"  type="submit"><i class="icon-${item.serviceProviderName}"></i> &nbsp; ${item.serviceProviderName}</button>
											</#list>	
										</div>									
									</div>
								</div>					
							</div>			
							<!-- end messages -->				
							<!-- start attachement -->
							<div class="tab-pane" id="my-attachments">
								<div class="blank-top-15" ></div>				
								<#if !action.user.anonymous >			
								<input name="uploadAttachment" id="attachment-files" type="file" />	
								<div class="blank-top-5 "></div>
								</#if>
								<div class="panel panel-success">								
									<div class="panel-heading">
										<ul id="attachment-list-view-filter" class="nav nav-pills">
											<li class="active">
												<a href="#"  id="attachment-list-view-filter-1"><span class="badge pull-right" data-bind="text: totalAttachCount"></span>전체</a>
											</li>
											<li>
												<a href="#"  id="attachment-list-view-filter-2"><span class="badge pull-right" data-bind="text: totalImageCount"></span>사진</a>
											</li>
											<li><a href="#"  id="attachment-list-view-filter-3"><span class="badge pull-right" data-bind="text: totalFileCount"></span>파일</a>
											</li>									  
										</ul>										
									</div>
									<div class="panel-body scrollable" style="max-height:450px;">
										<div id="attachment-list-view" ></div>
									</div>	
									<div class="panel-footer" style="padding:0px;">
										<div id="pager" class="k-pager-wrap"></div>
									</div>
								</div>																					
							</div>
							<!-- end attachements -->		
							<!-- start photos -->
							<div class="tab-pane" id="my-photos">
								<div class="blank-top-15" ></div>				
								<#if !action.user.anonymous >			
								<input name="uploadPhotos" id="photo-files" type="file" />	
								<div class="blank-top-5 "></div>
								</#if>		
								<div class="panel panel-danger">								
									<div class="panel-heading"></div>
									<div class="panel-body scrollable" style="max-height:450px;">
										<div id="photo-list-view" ></div>
									</div>	
									<div class="panel-footer" style="padding:0px;">
										<div id="photo-list-pager" class="k-pager-wrap"></div>
									</div>
								</div>																				
							</div>
							<!-- end photos -->
						</div>
						<!-- end of tab content -->						
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
				<img src="${request.contextPath}/secure/view-attachment.do?width=150&height=150&attachmentId=#:attachmentId#" alt="#:name# 이미지" class="img-responsive"/>
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
	</body>    
</html>
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
					<#if CompanyUtils.isallowedSignIn(action.company) >
					template : kendo.template($("#account-template").html()),
					</#if>
					afterAuthenticate : function(){
						$('.dropdown-toggle').dropdown();
						Holder.run();
						
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

				<#list action.companySocials as item >	
				socialServiceProviders.${item.serviceProviderName} = { 
					dataSource: null,
					<#if  item.serviceProviderName == "twitter" >
					url : '${request.contextPath}/social/get-twitter-hometimeline.do?output=json',
					data : "homeTimeline",
					template : kendo.template($("#twitter-timeline-template").html())
					<#elseif item.serviceProviderName == "facebook" >
					url : '${request.contextPath}/social/get-facebook-homefeed.do?output=json',
					data : "homeFeed",
					template : kendo.template($("#facebook-homefeed-template").html())			
					</#if>
				};
				</#list>
						
				$.each( $('#my-messages').find( '.social-connect-btn button' ) , function ( i, item ){					
					$(item).click( function(){ 
						var socialProvider = $(item).attr('data-provider');
						var socialAccountId = $(item).attr('data-account');
						if( typeof (socialProvider) == 'string' && typeof (socialAccountId) == 'string' ){
 							showSocialPanel( socialProvider, socialAccountId );	
 						}
 					});	
				});										
										
				$('#myTab a').click(function (e) {
					e.preventDefault();					
					if(  $(this).attr('href') == '#my-messages' ){					
						//$('#my-messages').popover('show');	
						
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
										read: { url:'${request.contextPath}/accounts/get-user-attachements.do?output=json', type: 'POST' },		
										destroy: { url:'${request.contextPath}/accounts/delete-user-attachment.do?output=json', type:'POST' },                                
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
										data : "userAttachments"
									},
									sort: { field: "attachmentId", dir: "desc" },
									filter :  { field: "contentType", operator: "neq", value: "" }
								},
								selectable: "single",									
								change: function(e) {									
									var data = this.dataSource.view() ;
									this.dataSource.page();
									var item = data[this.select().index()];				
									openPreviewWindow( item ) ;
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
														
							$("#attachment-list-view").on(
								"mouseenter", 
								".attach", 
								function(e) {
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
							$("#pager").kendoPager({
								refresh : true,
								dataSource : $('#attachment-list-view').data('kendoListView').dataSource
							});
																					
							$("#attachment-files").kendoUpload({
								 	multiple : false,
								 	width: 300,
								 	showFileList : false,
								    localization:{ select : '파일 업로드' , dropFilesHere : '업로드할 파일을 이곳에 끌어 놓으세요.' },
								    async: {
									    saveUrl:  '${request.contextPath}/accounts/save-user-attachments.do?output=json',							   
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
					}
					$(this).tab('show')
				});				
				// END SCRIPT            
			}
		}]);	

		// Social DataSource
		var socialServiceProviders = { } ;
						
		function showSocialPanel ( provider, socialId ){			
			var elementId =  provider + "-panel";			
			if( $("#" + elementId ).length == 0  ){
			
				// create new social panel 
				var template = kendo.template($("#social-view-panel-template").html());		
				$("#social-view-panels").append( template( { provider:provider} ) );
				
				// create dataSource
				if ( socialServiceProviders[ provider ].dataSource == null ){
					var socialStreamsElementId = provider + "-streams" ;
					alert ( "id:" + socialStreamsElementId );
					socialServiceProviders[ provider ].dataSource = new kendo.data.DataSource({
						transport: {
							read: {
								type : 'POST',
								type: "json",
								url : socialServiceProviders[ provider ].url,
							},
							parameterMap: function (options, operation){
								if (operation == "read" && options) {										                        								                       	 	
									return { socialAccountId: socialId };									                            	
								}
							} 
						},
						requestStart: function() {
							kendo.ui.progress($("#" + socialStreamsElementId ), true);
						},
						requestEnd: function() {
							kendo.ui.progress($("#" + socialStreamsElementId ), false);
						},
						change: function() {
							$("#" + socialStreamsElementId ).html(kendo.render(socialServiceProviders[ provider ].template, this.view()));
						},
						error:handleKendoAjaxError,
						schema: {
							data : socialServiceProviders[ provider ].data
						}
		            });			
		           socialServiceProviders[ provider ].dataSource.read();
				}				
				
				$( '#'+ elementId + ' .panel-header-actions a').each(function( index ) {
					var social_header_action = $(this);
					social_header_action.click(function (e){
						e.preventDefault();		
						var social_header_action_icon = social_header_action.find('span');
						if (social_header_action.text() == "Maximize"){
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
		
		function openPreviewWindow( item ){						
			var template = kendo.template($('#image-view-template').html());
			$('#image-view-panel').html( template(item) );				
			kendo.bind($("#image-view-panel"), item );
			
			$('#image-view-panel').show();
			$('#notice-view-panel').hide();
															
			$('#image-view-btn-close').click(function(){
				$('#notice-view-panel').show();
				$('#image-view-panel').hide();			
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
			padding: 0px;
			border: 0px;
			margin-bottom: -1px;
			vertical-align: middle;
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
        
        
		</style>   	
	</head>
	<body id="doc">
		<!-- START HEADER -->
		<div class="navbar navbar-default navbar-fixed-top" role="navigation">
		<#include "/html/common/common-homepage-wide-menu.ftl" >	
		</div>
		<!-- END HEADER -->	
		<!-- START MAIN CONTENT --> 
			<div id="mainContent" class="container layout">							
				<div class="row">								
				
					<div class="col-lg-8">						
						<div id="notice-view-panel" class="panel panel-warning">
							<div class="panel-heading">알림											
							</div>
							<div class="panel-body">
								<h3>소개</h3>
								<p>${action.company.displayName} ..ddd </p>
							</div>
						</div>						
						<div id="image-view-panel"></div>						
						<!-- start social view panels -->
						<div id="social-view-panels"></div>	
						<!-- end social view panels -->						
					</div>		
					
					<div class="col-lg-4">
						<ul class="nav nav-tabs" id="myTab">
							<li class="active"><a href="#my-messages">My 쇼셜</a></li>
							<li><a href="#my-attachments">My 파일</a></li>
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
											쇼셜미디어 연결 <span class="caret"></span>
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
									<div class="panel-heading"> 쇼셜미디어 버튼을 클릭하면, 쇼셜미디어 사이트에서 정보를 가져올 수 있습니다.</div>
									<div class="panel-body">
										<div class="btn-group social-connect-btn">
											<#list action.companySocials as item >	
											<button class="btn btn-primary" data-account="${ item.socialAccountId }" data-provider="${item.serviceProviderName}"  type="submit"><i class="icon-${item.serviceProviderName}"></i> &nbsp; ${item.serviceProviderName}</button>
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
								<div class="panel panel-default">
									<div class="panel-body">
										<input name="uploadAttachment" id="attachment-files" type="file" />
									</div>
								</div>	
								</#if>
								<div class="panel panel-default">								
									<div class="panel-heading">
										<ul id="attachment-list-view-filter" class="nav nav-pills">
											<li class="active">
												<a href="#"  id="attachment-list-view-filter-1"><span class="badge pull-right" data-bind="text: totalAttachCount"></span>전체</a>
											</li>
											<li>
												<a href="#"  id="attachment-list-view-filter-2"><span class="badge pull-right" data-bind="text: totalImageCount"></span>사진</a>
											</li>
											<li>
												<a href="#"  id="attachment-list-view-filter-3"><span class="badge pull-right" data-bind="text: totalFileCount"></span>파일</a>
											</li>									  
										</ul>										
									</div>
									<div class="panel-body">
										<div id="attachment-list-view" ></div>
									</div>	
									<div class="panel-footer" style="background-color:#FFFFFF;">
											<div id="pager" class="k-pager-wrap"></div>
									</div>
								</div>																					
							</div>
							<!-- end attachements -->		
						</div>
						<!-- end of tab content -->						
					</div>				
				</div>
			</div>		
			

		<div id="attach-window"></div>					
		<!-- END MAIN CONTENT -->		
 		<!-- START FOOTER -->
		<footer> 
		</footer>
		<!-- END FOOTER -->	
		<!-- START TEMPLATE -->
		<script type="text/x-kendo-tmpl" id="social-view-panel-template">
		<div id="#: provider #-panel" class="panel panel-success">
			<div class="panel-heading">
				<i class="icon-#: provider #"></i> &nbsp; #: provider #
				<div class="k-window-actions panel-header-actions">
					<a role="button" href="\\#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-refresh">Refresh</span></a>
					<a role="button" href="\\#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-minimize">Minimize</span></a>
					<a role="button" href="\\#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-maximize">Maximize</span></a>
					<a role="button" href="\\#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-close">Close</span></a>
				</div>							
			</div>
			<div class="panel-body">
				<ul class="media-list">
					<div id="#:provider#-streams">데이터가 없습니다..</div>
				</ul>
			</div>
		</div>				
		</script>
		
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
		<script id="attachment-preview-template" type="text/x-kendo-template">	
			#if (contentType.match("^image") ) {#
				<img src="${request.contextPath}/secure/view-attachment.do?attachmentId=#:attachmentId#" alt="#:name# 이미지" class="img-responsive"/>
				<p class="blank-top-5">
						<a class="k-button" href="${request.contextPath}/secure/download-attachment.do?attachmentId=#= attachmentId #" >다운로드</a>
						<a class="k-button" href="${request.contextPath}/secure/download-attachment.do?attachmentId=#= attachmentId #" >삭제</a>
				</p>				
			# } else { #		
				<div class="k-grid k-widget" style="width:100%;">
					<div style="padding-right: 17px;" class="k-grid-header">
						<div class="k-grid-header-wrap">
							<table cellSpacing="0">
								<thead>
									<tr>
										<th class="k-header">속성</th>
										<th class="k-header">값</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
					<div style="height: 199px;" class="k-grid-content">
						<table style="height: auto;" class="system-details" cellSpacing="0">
							<tbody>
								<tr>
									<td>파일</td>
									<td>#= name #</td>
								</tr>
								<tr class="k-alt">
									<td>종류</td>
									<td>#= contentType #</td>
								</tr>
								<tr>
									<td>크기(bytes)</td>
									<td>#= size #</td>
								</tr>				
								<tr>
									<td>다운수/클릭수</td>
									<td>#= downloadCount #</td>
								</tr>											
							</tbody>
						</table>	
					</div>
				</div>
				<p class="blank-top-5">
					<a class="k-button" href="${request.contextPath}/secure/download-attachment.do?attachmentId=#= attachmentId #" >다운로드</a>
					<a class="k-button" href="${request.contextPath}/secure/download-attachment.do?attachmentId=#= attachmentId #" >삭제</a>	
				</p>	
				# } #  	
		</script>		
		<#include "/html/common/common-homepage-social-templates.ftl" >		
		<#include "/html/common/common-homepage-templates.ftl" >		
		<!-- END TEMPLATE -->
	</body>    
</html>
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
				
				<#if !action.user.anonymous >
				// Start : Company Social Content 
				<#list action.companySocials  as item >	
					<#if item.serviceProviderName == "twitter">					
					var template = kendo.template($("#twitter-timeline-template").html());
					var dataSource = new kendo.data.DataSource({
						transport: {
							read: {
								type : 'POST',
								type: "json",
								//url : '${request.contextPath}/social/get-twitter-usertimeline.do?output=json',
								url : '${request.contextPath}/social/get-twitter-hometimeline.do?output=json',
							},
							parameterMap: function (options, operation){
								if (operation == "read" && options) {										                        								                       	 	
									return { socialAccountId: ${ item.socialAccountId } };									                            	
								}
							} 
						},
						requestStart: function() {
							kendo.ui.progress($("#company-twitter-timeline"), true);
						},
						requestEnd: function() {
							kendo.ui.progress($("#company-twitter-timeline"), false);
						},
						change: function() {
							$("#company-twitter-timeline").html(kendo.render(template, this.view()));
						},
						error:handleKendoAjaxError,
						schema: {
							data : "homeTimeline"
						}
		            });            				
		            dataSource.read();
		            
		            <#elseif item.serviceProviderName == "facebook">
					
					var facebookTemplate = kendo.template($("#facebook-homefeed-template").html());
					var facebookDataSource = new kendo.data.DataSource({
						transport: {
							read: {
								type : 'POST',
								type: "json",
								url : '${request.contextPath}/social/get-facebook-homefeed.do?output=json',
							},
							parameterMap: function (options, operation){
								if (operation == "read" && options) {										                        								                       	 	
									return { socialAccountId: ${ item.socialAccountId } };	                            	
								}
							} 
						},
						requestStart: function() {
							kendo.ui.progress($("#company-facebook-homefeed"), true);
						},
						requestEnd: function() {
							kendo.ui.progress($("#company-facebook-homefeed"), false);
						},
						change: function() {
							$("#company-facebook-homefeed").html(kendo.render(facebookTemplate, this.view()));
						},
						error:handleKendoAjaxError,
						schema: {
							data : "homeFeed"
						}
		            });            				
		            facebookDataSource.read();
					</#if>
								
				</#list>
				// End : Company Social Content 
				
				$('#myTab a').click(function (e) {
					e.preventDefault();					
					
					if(  $(this).attr('href') == '#my-messages' ){					
						$('#my-messages').popover('show');	
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
				</#if>	
				// END SCRIPT            
			}
		}]);	
		
		<#if !action.user.anonymous >
		function openPreviewWindow( item ){					
			var template = kendo.template($('#image-view-template').html());
			$('#image-view-panel').html( template(item) );	
			kendo.bind($("#image-view-panel"), item );
			if( $('#image-view-panel').hasClass('hide') ){
				$('#image-view-panel').removeClass('hide');
			}			
			if( !$('#notice-view-panel').hasClass('hide') ){
				$('#notice-view-panel').addClass("hide");
			}
			$('#image-view-btn-close').click(function(){
				if( $('#notice-view-panel').hasClass('hide') ){
					$('#notice-view-panel').removeClass('hide');
				}					
				if( !$('#image-view-panel').hasClass('hide') ){
					$('#image-view-panel').addClass('hide');
				}				
			} );
			
		}			
		</#if>		
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
					<div id ="notice-view-panel" class="col-lg-8">
						<div class="panel panel-warningandang	">
							<div class="panel-heading">알림
							<button id="social-view-btn-close" type="button" class="close">&times;</button>
							</div>
							<div class="panel-body">
								<h3>소개</h3>
								<p>${action.company.displayName} ..ddd </p>
							</div>
						</div>
					</div>					
				</div>		
				<div class="row">
					<div id="facebook-panel" class="col-lg-8">
						<div class="panel panel-info">
							<div class="panel-heading">
								<i class="icon-facebook"></i>
								
								<button id="facebook-panel-refresh-btn" type="button" class="close "><span class="k-icon k-i-refresh"></span></button>
								<button id="facebook-panel-minimize-btn" type="button" class="close"><span class="k-icon k-i-minimize"></span></button>
								<button id="facebook-panel-restore-btn" type="button" class="close hidden"><span class="k-icon k-i-restore"></span></button>
								<button id="facebook-panel-close-btn" type="button" class="close hidden"><span class="k-icon k-i-close"></span></button>
							</div>		
							<div class="panel-body">
								<ul class="media-list">
									<div id="company-facebook-homefeed"></div>
								</ul>
							</div>							
						</div>
					</div>
					<div id="twitter-panel" class="col-lg-4">
						<div class="panel panel-success">
							<div class="panel-heading">
								<i class="icon-twitter"></i>
								<button id="twitter-panel-refresh-btn" type="button" class="close "><span class="k-icon k-i-refresh"></span></button>
								<button id="twitter-panel-minimize-btn" type="button" class="close"><span class="k-icon k-i-minimize"></span></button>
								<button id="twitter-panel-restore-btn" type="button" class="close hidden"><span class="k-icon k-i-restore"></span></button>
								<button id="twitter-panel-close-btn" type="button" class="close hidden"><span class="k-icon k-i-close"></span></button>								
							</div>
							<div class="panel-body">
								<ul class="media-list">
									<div id="company-twitter-timeline"></div>
								</ul>
							</div>
						</div>
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
		<script type="text/x-kendo-tmpl" id="facebook-homefeed-template">
		#if (type != "STATUS") {#
		<li class="media">
		    <a class="pull-left" href="\\#">
		    	<img class="media-object" src="http://graph.facebook.com/#=from.id#/picture" alt="프로파일 이미지" class="img-rounded">
		    </a>
		    <div class="media-body">
		      <h4 class="media-heading">#: from.name # (#: kendo.toString(updatedTime, "D") #)</h4>
		     	
		     	<span class="label label-primary">#: type #</span>
		     	
		     	#if ( message !=null ) { #
		     	<br><br>
		     	#: message #
		     	# } #		
		     	     	
		     	#if ( name !=null ) { #
		     	<br>#: name  #
		     	# } #
		     	     	
		     	#if ( type == 'LINK' ) { #
		     	<br><br>
		     	<span class="glyphicon glyphicon-link"></span>&nbsp;<a href="#: link #">#: link #</a>
		     	# } else if ( type == 'PHOTO' ) { #
		     	<br><br>
		     	<img src="#: picture.replace("_s.", "_n.")  #" alt="media" class="img-rounded img-responsive">
		     	# } else { #		     	
		     		#if ( picture !=null ) { #
		     		<br><br><img src="#: picture.replace("_s.", "_n.")  #" alt="media" class="img-rounded img-responsive">
		     		# } #		     		
		     		#if ( source !=null ) { #
		     		<br><br>source : <span class="glyphicon glyphicon-link"></span>&nbsp;<a href="#: source #">#: source #</a>
		     		# } #
		     	# } #
		     	#if ( caption !=null ) { #
		     	<br>
		     	<blockquote>
		     	<p>#: caption #</p>
				</blockquote>
		     	# } #				     	
		     	#if ( typeof( description ) == 'string'  ) { #
		     	<br>
		     	<p class="text-info">...
		     	#: description #
		     	</p>
		     	# } #		
		    </div>
		  </li>					
			# } #  	
		</script>		
		<script type="text/x-kendo-tmpl" id="twitter-timeline-template">
		<li class="media">
		    <a class="pull-left" href="\\#">
		      <img src="#: user.profileImageUrl #" alt="#: user.name#" class="media-object">
		    </a>
		    <div class="media-body">
		      <h4 class="media-heading">#: user.name # (#: kendo.toString(createdAt, "D") #)</h4>
		     	#: text #      	
							# for (var i = 0; i < entities.urls.length ; i++) { #					
							# var url = entities.urls[i] ; #		
							<br><span class="glyphicon glyphicon-link"></span>&nbsp;<a href="#: url.expandedUrl  #">#: url.displayUrl #</a>
							 # } #	
							<p>
							# for (var i = 0; i < entities.media.length ; i++) { #					
							# var media = entities.media[i] ; #					
							<img src="#: media.mediaUrl #" width="100%" alt="media" class="img-rounded">
							# } #
							</p>
							#if (retweeted) {#					
						<div class="media">
							<a class="pull-left" href="\\#">
								<img src="#: retweetedStatus.user.profileImageUrl #" width="100%" alt="media" class="img-rounded">
							</a>
							<div class="media-body">
								<h4 class="media-heading">#: retweetedStatus.user.name #</h4>
							</div>
						</div>						
							# } #
		    </div>
		  </li>					
		</script>
		<script type="text/x-kendo-tmpl" id="twitter-timeline-template2">
			<div class="popover left" style="display:true;">
				<div class="arrow"></div>
				<div class="popover-content">
					<p>#: text #</p>
					<p>
					# for (var i = 0; i < entities.media.length ; i++) { #					
					# var media = entities.media[i] ; #					
					<img src="#: media.mediaUrl #" width="100%" alt="media" class="img-rounded">
					# } #
					</p>
					<img src="#: user.profileImageUrl #" alt="#: user.name#" class="img-thumbnail">
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
		<#include "/html/common/common-homepage-templates.ftl" >		
		<!-- END TEMPLATE -->
	</body>    
</html>
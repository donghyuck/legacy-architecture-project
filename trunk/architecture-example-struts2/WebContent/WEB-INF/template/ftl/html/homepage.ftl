<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title>::</title>
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [			
			'${request.contextPath}/js/jquery/1.9.1/jquery.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			'${request.contextPath}/js/foundation/custom.modernizr.js',
			'${request.contextPath}/js/foundation/foundation.min.js',
			'${request.contextPath}/js/foundation/foundation.topbar.js',
			'${request.contextPath}/js/foundation/foundation.dropdown.js',
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo/kendo.ko_KR.js',
			'${request.contextPath}/js/common/common.ui.min.js',
			'${request.contextPath}/js/common/common.models.js',
			'${request.contextPath}/js/common/common.api.js', ], 
			complete: function() {      
				// START SCRIPT								
				$("#top-menu").kendoMenu();
				$("#top-menu").show();				
				$(document).foundation();	
				var currentUser = api.accounts.Token;				
										
				api.accounts.load({
					success : function( token ){								
						currentUser = token;
						if( !currentUser.anonymous ){
							// LEFT TOP PANEL FOR USER
							var photoUrl = 'http://placehold.it/100x150&amp;text=[No Photo]';
			            	if( currentUser.properties.imageId ){
			            		photoUrl = '${request.contextPath}/accounts/view-image.do?width=100&height=150&imageId=' + currentUser.properties.imageId ;
			            	}
			            	currentUser.photoUrl = photoUrl ;		            
			            	kendo.bind($("#top-user-status"), currentUser ); 			            				            	
							var userDetailsTemplate = kendo.template( $('#template').html() );							
							$('#user-status-panel').html( userDetailsTemplate( currentUser ) );	
						}else{
							// LEFT TOP PANEL FOR LOGIN
							$('#user-login-panel').html($('#template4').html());
							var validator = $("#login-form").kendoValidator({validateOnBlur:false}).data("kendoValidator");
							$("#login-btn").click(function() {           		    	
								$("#login-status").html("");
								if( validator.validate() )
								{
									api.accounts.login({
										data: $("form[name=fm1]").serialize(),
										success : function( response ) {
											$("form[name='fm1']")[0].reset();               	                            
											$("form[name='fm1']").attr("action", "/main.do?view=homepage").submit();										
										},
										fail : function( response ) {
											$("#login-btn").kendoAnimate("slideIn:up");          
											$("#password").val("").focus();												
											$("#login-status").kendoAlert({ 
												data : { message: "입력한 사용자 이름 또는 비밀번호가 잘못되었습니다." },
												close : function(){	
													$("#password").focus();										
												 }
											}); 										
										},		
										error : function( thrownError ) {
											$("form[name='fm1']")[0].reset();                    
											$("#login-status").kendoAlert({ data : { message: "잘못된 접근입니다." } }); 
											$("#login").kendoAnimate("slideIn:up");										
										}																
									});															
								}else{        			      
									$("#login-btn").kendoAnimate("slideIn:up"); 
								}
							});	
						}						
					}
				});		
										
				$("#tabstrip").show();				
				$("#tabstrip").kendoTabStrip({
					animation:  {
						open: {
							effects: "fadeIn"
						}
					},
					select : function(e){			
						// TAB - ATTACHMENT TAB
						if( $( e.contentElement ).hasClass('attachments') ){	
							if( !$('#attachment-list-view').data('kendoListView') ){	
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
										pageSize: 6,
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
									template: kendo.template($("#template2").html()),
									dataBound: function(e) {
									}
								});								
								$("#attachment-list-view").on("mouseenter", ".attach", 
									function(e) {
										kendo.fx($(e.currentTarget).find(".attach-description")).expand("vertical").stop().play();
									 }).on("mouseleave", ".attach", function(e) {
										kendo.fx($(e.currentTarget).find(".attach-description")).expand("vertical").stop().reverse();
								});								
								// ListView Filter 									
								$("dl#attachment-list-view-filter dd").find("a").click(function(){					
									var attachment_list_view = $('#attachment-list-view').data('kendoListView');
									$("dl#attachment-list-view-filter dd.active").removeClass("active");
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
								<#if !action.user.anonymous >														
								$("#attachment-files").kendoUpload({
								 	multiple : false,
								 	width: 300,
								 	showFileList : false,
								    localization:{ select : '업로드' , dropFilesHere : '업로드할 파일을 이곳에 끌어 놓으세요.' },
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
								</#if>
							}						
						}
					}	
				});				
			// END SCRIPT            
			}
		}]);		
		
		function openPreviewWindow( item ){
			if(! $("#attach-window").data("kendoWindow")){
				$("#attach-window").kendoWindow({
					actions: ["Minimize", "Maximize", "Close"],
					minHeight : 300,
					minWidth : 300,
					maxWidth : 800,
					width: "500px",
					modal: false,
					visible: false
				});
			}										
			var attachWindow = $("#attach-window").data("kendoWindow");
			var template = kendo.template($("#template3").html());
			attachWindow.title( item.name );
			attachWindow.content( template(item) );
			$("#attach-window").closest(".k-window").css({
			     top: 70,
			     left: 15,
			 });
			attachWindow.open();		
		}
		-->
		</script> 		   
		
    <style scoped="scoped">
    	
    	.login-panel {
			max-width : 280px;
		}

		.alert-box {
			margin-top: 5px;
			margin-bottom: 0px;
		}
        span.k-tooltip, span.k-tooltip-validation {        	
        	font-size : 90%;
	 		border-radius: 5px;
	 		margin-top: 5px;
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
			min-width: 286px;
			padding: 0px;
			border: 0px;
			margin-bottom: -1px;
		}
        		                		
		.attach
		{
			float: left;
            position: relative;
            width: 144px;
            height: 144px;
            padding: 0;
			cursor: pointer;
		}
		
		.attach img
		{
			width: 144px;
			height: 144px;
		}
		
		.attach-description {
            position: absolute;
            top: 0;
            width: 144px	;
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
		<header>
			<div class="row top layout">
				<div class="small-8 columns">
					<div class="big-box topless bottomless">
						<h1>메인페이지</h1>
						<#if action.user.anonymous > 
						<h4>안녕하세요.</h4>
						<#else>
						<h4>안녕하세요. ${ action.user.name} 님</h4>
						</#if>
					</div>
				</div>								
				<div class="small-4 columns">	
					<div style="padding-top:65px;">			
				 	<#if action.user.anonymous > 
				 	<#else>
				 	<div id="top-user-status">				 					 	
						<a id="user-details" href="#" data-dropdown="drop1" class="button small" style="padding:0px;"><img data-bind="attr: { src: photoUrl, alt: name, title: name }" src="http://placehold.it/100x150&amp;text=[No Photo]"  width="30" height="30"/><span class="k-icon k-i-arrow-s"></span></a>	 
					<div>		
				 	</#if>
				 	</div>
				</div>
			</div> 
		</header>
		<!-- END HEADER -->
		<nav class="top-bar">
			<div class="row  layout">
				<div class="large-12 columns">
					<ul id="top-menu" style="display:none;">
					<#if action.getMenuComponent("USER_MENU") ?? >
						<#assign menu = action.getMenuComponent("USER_MENU") />
						<#list menu.components as item >
							<li>
							${item.title}
								<#if item.components?has_content >
									<ul>
									<#list item.components as sub_item >
										<li>${sub_item.title}
										<#if sub_item.components?has_content >
											<ul>
												<#list sub_item.components as sub_sub_item >
												<li>${ sub_sub_item.title }</li>
												</#list>
											</ul>
										</#if>
										</li>
									</#list>
									</ul>
								</#if>
							</li>
						</#list>
					<#else>
						<!-- 메뉴 데이터 없음  -->
					</#if>
				</div>
			</div>
		</nav>
		<!-- START MAIN CONTENT --> 
		<div id="mainContent">		
				
		<div class="row layout">
			<div class="large-8 columns"> 		
				<div class="big-box topless leftless bottomless">		
				<h3>소개</h3>
				<p>지금 보는 페이지는 공개소스 jquery 기반의 kendoui 와 foundation js 를 사용하여 구현되었다. 현재 예시로 구현된 관리자 화면 보러가기 <a href="/secure/main-company.do" class="k-button">클릭</a> </p>
				</div>
			</div>
			<div class="large-4 columns">			
				<div id="user-login-panel"></div>					
				<!-- START TABS -->
				<div id="tabstrip" style="display:none; background-color:#f5f5f5;">
					<ul>
						<li class="k-state-active">쪽지</li>
						<li>내 파일	</li>
					</ul>
					<div>새로운 메시지가 없습니다.</div>
					<div class="attachments">
						<#if !action.user.anonymous >	
						<div class="row layout">
							<div class="small-12 columns big-box">	
								<input name="uploadAttachment" id="attachment-files" type="file" />		
							</div>
						</div>
						</#if>
						<div class="row layout">
							<div class="small-12 columns big-box">					
								<dl id="attachment-list-view-filter"  class="sub-nav">
									<dt>필터:</dt>
									<dd class="active"><a href="#" id="attachment-list-view-filter-1">전체</a></dd>
									<dd><a href="#" id="attachment-list-view-filter-2">이미지</a></dd>
									<dd><a href="#" id="attachment-list-view-filter-3">파일</a></dd>
								</dl>
							</div>
						</div>
						<div class="row layout">
							<div class="small-12 columns">					
								<div id="attachment-list-view" ></div>
							</div>
						</div>
						<div class="row layout">
							<div class="small-12 columns">
								<div id="pager" class="k-pager-wrap"></div>
							</div>
						</div>				
					</div>				
				</div>
				<!-- END TABS -->	
			</div>						
		</div>
		</div>
		
		<div id="drop1" data-dropdown-content class="f-dropdown small">
			<div id="user-status-panel"></div>
		</div>		
		
		<div id="attach-window"></div>
					
		<!-- END MAIN CONTENT -->		
 		<!-- START FOOTER -->
		<footer> 
		</footer>
		<!-- END FOOTER -->	
		<!-- START TEMPLATE -->
		<script id="template" type="text/x-kendo-template">
			<div class="row layout">
				<div class="small-4 columns">
					<div class="big-box">
						<img id="user-details-photo" src="#: photoUrl #" />
					</div>							
				</div>
				<div class="small-8 columns">
					<div class="box">
					<h4>#: name #</h4>
					#if (isSystem ) {#
					<a href="/secure/main.do"><span class="round alert label">시스템</span></a>
					# } #
					<p style="font-color:000000;">
							#: email #
					</p>					
					</div>							
				</div>								
			</div>					
			<div class="row layout">
				<div class="small-12 columns">
					<div class="gray-big-box">
						<button class="k-button">계정설정 &nbsp;<span class="k-icon k-i-custom"></span></button>
						<a class="k-button right" href="${request.contextPath}/logout" >로그아웃</a>
					</div>
				</div>
			</div>
		</script>
		
		<script type="text/x-kendo-tmpl" id="template2">
			<div class="attach">			
			#if (contentType.match("^image") ) {#
				<img src="${request.contextPath}/secure/view-attachment.do?width=144&height=144&attachmentId=#:attachmentId#" alt="#:name# 이미지"/>
			# } else { #			
				<img src="http://placehold.it/144x144&amp;text=[file]"></a>
			# } #	
				<div class="attach-description">
					<h3>#:name#</h3>
					<p>#:size# 바이트</p>
				</div>
			</div>
		</script>		
		<script id="template3" type="text/x-kendo-template">				
			#if (contentType.match("^image") ) {#
				<img src="${request.contextPath}/secure/view-attachment.do?attachmentId=#= attachmentId #" style="border:0;"/>
				<p>
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
				<p>
				<a class="k-button" href="${request.contextPath}/secure/download-attachment.do?attachmentId=#= attachmentId #" >다운로드</a>
				<a class="k-button" href="${request.contextPath}/secure/download-attachment.do?attachmentId=#= attachmentId #" >삭제</a>	
				</p>	
				# } #  		
		</script>		
		<script id="template4" type="text/x-kendo-template">	
			<div id="login-form" class="light-gray boder-bottomless-box">		
				<form name="fm1" method="POST" accept-charset="utf-8">
				<input type="hidden" id="output" name="output" value="json" />		
				<div class="row">
					<div class="small-8 columns">
						<div class="row bottomspace">
							<div class="small-12 columns">
								<input type="text" id="username" name="username"  class="k-textbox" pattern="[^0-9][A-Za-z]{2,20}" placeholder="아이디 or 이메일" required validationMessage="아이디를 입력하여 주세요." />
							</div>
						</div>	
						<div class="row">
							<div class="small-12 columns">
								<input type="password" id="password" name="password" class="k-textbox"  placeholder="비밀번호" required validationMessage="비밀번호를 입력하여 주세요." />
							</div>
						</div>					
					</div>
					<div class="small-4 columns">
						<button type="button" id="login-btn" class="k-button right">로그인</button>
					</div>		
				</div>	
				</form>
				<div class="row">
					<div id="login-status" class="small-12 columns">
					</div>
				</div>
			</div>		
		</script>		
		<!-- END TEMPLATE -->

	</body>    
</html>
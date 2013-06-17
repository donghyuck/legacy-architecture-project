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
			'${request.contextPath}/js/foundation/foundation.min.js',
			'${request.contextPath}/js/foundation/foundation.dropdown.js',			
			'${request.contextPath}/js/foundation/zepto.js',
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo/kendo.ko_KR.js',
			'${request.contextPath}/js/common/common.ui.min.js',
			'${request.contextPath}/js/common/common.models.js' ], 
			complete: function() {      
				// START SCRIPT
				$(document).foundation();
				var currentUser = new User ({});					
				$.ajax({
					type : 'POST',
					url : "${request.contextPath}/accounts/get-user.do?output=json",
					success : function( response ){						
						currentUser = new User ( response.currentUser );		  
						var photoUrl = 'http://placehold.it/100x150&amp;text=[No Photo]';
		            	if( currentUser.properties.imageId ){
		            		photoUrl = '${request.contextPath}/secure/view-image.do?width=100&height=150&imageId=' + currentUser.properties.imageId ;
		            	}
		            	currentUser.photoUrl = photoUrl ;
						var userDetailsTemplate = kendo.template( $('#template').html() );
						$('#account-details').html( userDetailsTemplate( currentUser ) );
					},
					error:handleKendoAjaxError,
					dataType : "json"
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
										pageSize: 10,
										error:handleKendoAjaxError,
										schema: {
											model: Attachment,
											data : "userAttachments"
										}
									},
									selectable: "single",									
									change: function(e) {
										var data = this.dataSource.view() ;
										var item = data[this.select().index()];
										
										if(! $("#attach-window").data("kendoWindow")){
											$("#attach-window").kendoWindow({
												actions: ["Minimize", "Maximize", "Close"],
												minHeight : 300,
												minWidth : 300,
												maxWidth : 800,
												modal: false,
												visible: false
											});
										}
										
										var attachWindow = $("#attach-window").data("kendoWindow");
										var template = kendo.template($("#template3").html());
										attachWindow.title( item.name );
										attachWindow.content( template(item) );
										$("#attach-window").closest(".k-window").css({
										     top: 65,
										     left: 10,
										 });
										attachWindow.open();
									},
									template: kendo.template($("#template2").html())
								});
								
								$("#pager").kendoPager({
									dataSource: $('#attachment-list-view').data('kendoListView').dataSource	
								});
            
								$("#attachment-list-view").on("mouseenter", ".attach", 
									function(e) {
										kendo.fx($(e.currentTarget).find(".attach-description")).expand("vertical").stop().play();
									 }).on("mouseleave", ".attach", function(e) {
										kendo.fx($(e.currentTarget).find(".attach-description")).expand("vertical").stop().reverse();
								});        
								
								// Filter 					
											
								$("dl#attachment-list-view-filter dd").find("a").click(function(){			
									
									$("dl#attachment-list-view-filter dd.active").removeClass("active");
									$(this).parent().addClass("active");
									
									var filter_id =  $(this).attr('id') ;
									if( filter_id == "attachment-list-view-filter-1" ){
										$('#attachment-list-view').data('kendoListView').dataSource.filter({}) ; 
									}else if (filter_id == "attachment-list-view-filter-2" ) {
										$('#attachment-list-view').data('kendoListView').dataSource.filter( { field: "contentType", operator: "startswith", value: "image" }) ; 
									}else if (filter_id == "attachment-list-view-filter-3" ) {
										$('#attachment-list-view').data('kendoListView').dataSource.filter( { field: "contentType", operator: "startswith", value: "application" }) ; 
									}
								});
								
								$("#attachment-files").kendoUpload({
								 	multiple : false,
								 	width: 150,
								 	showFileList : false,
								    localization:{ select : '파일 선택' , dropFilesHere : '업로드할 파일을 이곳에 끌어 놓으세요.' },
								    async: {
									    saveUrl:  '${request.contextPath}/accounts/save-user-attachments.do?output=json',							   
									    autoUpload: true
								    },
								    upload: function (e) {								         
								    	 e.data = {  };														    								    	 		    	 
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
					}	
				});
				
			// END SCRIPT            
			}
		}]);
		-->
		</script> 		   
		
    <style scoped="scoped">
    
		.k-widget.k-tooltip {
			border-color: #787878;
			background-color: #FFFFFF;
			color: #787878;
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
			min-height: 500px;
            width: 270px;
            margin: 0 auto;
            padding-top:15px;
            border:0px;
		}
        		                		
		.attach
		{
			float: left;
            position: relative;
            width: 130px;
            height: 130px;
            padding: 0;
			cursor: pointer;
		}
		
		.attach img
		{
			width: 130px;
			height: 130px;
		}
		
		.attach-description {
            position: absolute;
            top: 0;
            width: 130px;
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
        
		</style>   	
	</head>
	<body id="doc">
		<!-- START HEADER -->
		<header>
			<div class="row top layout">
				<div class="large-8 columns">
					<div class="big-box topless bottomless">
						<h1>메인페이지</h1>
						<#if action.user.anonymous > 
						<h4>안녕하세요.</h4>
						<#else>
						<h4>안녕하세요. ${ action.user.name} 님</h4>
						</#if>
					</div>
				</div>
				<div class="large-4 columns">	
					<div style="padding-top:25px;">			
				 	<#if action.user.anonymous > 
			 		<a href="${request.contextPath}/accounts/login.do" class="k-button" >로그인</a></strong>	
				 	<#else>
			 		<a id="user-details" href="#" data-dropdown="drop1" class="k-button">${ action.user.name}&nbsp;&nbsp;<span class="k-icon k-i-arrow-s"></span></a>
				 	</#if>
				 	</div>
				</div>
			</div>
		</header>		
		<!-- END HEADER -->
		<!-- START MAIN CONTENT --> 
		
		<div class="row layout">
			<div class="large-7 columns"> 
				<div id="attach-window" ></div>
				<h3>소개</h3>
				<p>지금 보는 페이지는 공개소스 jquery 기반의 kendoui 와 foundation js 를 사용하여 구현되었다. 현재 예시로 구현된 관리자 화면 보러가기 <a href="/secure/main-company.do" class="k-button">클릭</a> </p>
			</div>
		<div class="large-4 columns">
		<h5>....</h5>
		<!-- Clicking this placeholder fires the mapModal Reveal modal -->
		<div id="tabstrip" style="display:none; background-color:#f5f5f5;">
			<ul>
				<li class="k-state-active">
				쪽지 
				</li>
				<li>
				내 파일
				</li>
			</ul>
			<div>새로운 메시지가 없습니다.</div>
			<div class="attachments">
				<div class="row layout">
					<div class="small-12 columns">	
						<input name="uploadAttachment" id="attachment-files" type="file" />		
					</div>
				</div>
				<div class="row layout">
					<div class="small-12 columns">					
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
		</div>				
      <p>
...
      </p>
      <p>
...
      </p>
    </div>
    
    
  </div>
  
		<div id="drop1" data-dropdown-content class="f-dropdown">
			<div id="account-details" class="k-content" style="background-color:#F5F5F5;"></div>
		</div>		
		<!-- END MAIN CONTENT -->		
 		<!-- START FOOTER -->
		<footer> 
		</footer>
		<!-- END FOOTER -->	
		<!-- START TEMPLATE -->
		<script id="template" type="text/x-kendo-template">
			<div class="row layout">
				<div class="small-12 columns">
					<div class="big-box">
						<img id="user-details-photo" src="#: photoUrl #" />
					</div>							
				</div>
			</div>	
			<div class="row layout">
				<div class="small-12 columns">
					<div class="big-box" >
					#: name #
					<p style="font-color:000000;">
							#: email #
					</p>								
					</div>
				</div>
			</div>	
			<div class="row layout">
				<div class="small-12 columns">
					<div class="big-box" style="height:50px;">
					<a class="k-button right" href="${request.contextPath}/logout" >로그아웃</a><div class="box right"></div><button class="k-button right">계정설정</button>
					</div>
				</div>
			</div>
		</script>
		<script type="text/x-kendo-tmpl" id="template2">
			<div class="attach">			
			#if (contentType.match("^image") ) {#
				<img src="${request.contextPath}/secure/view-attachment.do?width=120&height=120&attachmentId=#:attachmentId#" alt="#:name# 이미지"/>
			# } else { #			
				<img src="http://placehold.it/120x120&amp;text=[file]"></a>
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
		<!-- END TEMPLATE -->

	</body>    
</html>
<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title>::</title>
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [			
			'${request.contextPath}/js/jquery/1.9.1/jquery.min.js',
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo/kendo.ko_KR.js',			
			'${request.contextPath}/js/bootstrap/3.0.0/bootstrap.min.js',
			'${request.contextPath}/js/bootstrap/3.0.0/dropdown.js',
       	    '${request.contextPath}/js/common/holder.js',
       	    '${request.contextPath}/js/common/common.models.js',
       	    '${request.contextPath}/js/common/common.ui.js'], 
       	    
			complete: function() {      
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
					template : kendo.template($("#account-template").html()),
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
				
				$('#myTab a').click(function (e) {
					e.preventDefault();
					if(  $(this).attr('href') == '#my-attachments' ){
						if( !$('#attachment-list-view').data('kendoListView') ){	
							
							var attachementTotalModle = kendo.observable({ 
								totalAttachCount : "0",
								totalImageCount : "X",
								totalFileCount : "X"							
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
							<#if !action.user.anonymous >														
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
							</#if>							
						}								
					}
					$(this).tab('show')
				})
				
				// END SCRIPT            
			}
		}]);	
		
		function openPreviewWindow( item ){
			if(! $("#attach-window").data("kendoWindow")){
				$("#attach-window").kendoWindow({
					actions: ["Minimize", "Maximize", "Close"],
					minHeight : 300,
					minWidth : 300,
					width : '65%',
					height: 620,
					modal: false,
					visible: false,
					draggable : false,
					pinned: true,
					position: { top: 5 }
				});
			}										
			var attachWindow = $("#attach-window").data("kendoWindow");
			var template = kendo.template($("#attachment-preview-template").html());
			attachWindow.title( item.name );
			attachWindow.content( template(item) );
			$("#attach-window").closest(".k-window").css({
			     top: 5,
			     left: 5,
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
		<div class="header">		
			<div class="container">
				<div class="row layout">
					<div class="col-lg-8">
						<h2><#if action.user.company ?? > ${action.user.company.name} </#if></h2>
					</div>								
					<div class="col-lg-4">					 	
						<div id="account-panel" class="visible-lg"></div>
					</div>
				</div> 
			</div>
		</div>
		<!-- END HEADER -->	
		<div class="container blank-top-5">		
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
					</ul>	
			</div>
		<!-- START MAIN CONTENT --> 
		<div id="wrap">
			<div class="container layout">		
				<div class="row">
					<div class="col-lg-8">
						<div class="panel panel-default">
							<div class="panel-heading">알림</div>
							<div class="panel-body">
								<h3>소개</h3>
								<p>지금 보는 페이지는 공개소스 jquery 기반의 kendoui 와 foundation js 를 사용하여 구현되었다. </p>
							</div>
						</div>
					</div>
					<div class="col-lg-4 visible-lg">
						<ul class="nav nav-tabs" id="myTab">
							<li class="active"><a href="#my-messages">메시지</a></li>
							<li><a href="#my-attachments">첨부파일</a></li>
						</ul>						
						<div class="tab-content">
							<div class="tab-pane active" id="my-messages"><span class="glyphicon glyphicon-comment"></span> 새로운 메시지가 없습니다.</div>
							<div class="tab-pane" id="my-attachments">
								<div class="container">
									<div class="row blank-top-5">
									<div class="col-lg-12">
										<ul id="attachment-list-view-filter" class="nav nav-pills">
											<li class="active">
												<a href="#"  id="attachment-list-view-filter-1">
													<span class="badge pull-right" data-bind="text: totalAttachCount">&nbsp;</span>
													전체
												</a>
											</li>
											<li>
												<a href="#"  id="attachment-list-view-filter-2">
													<span class="badge pull-right" data-bind="text: totalImageCount">&nbsp;</span>
													이미지
												</a>
											</li>
											<li>
												<a href="#"  id="attachment-list-view-filter-3">
													<span class="badge pull-right" data-bind="text: totalFileCount">&nbsp;</span>
													파일
												</a>
											</li>									  
										</ul>
									</div>
									</div>								
									<#if !action.user.anonymous >	
									<div class="row blank-top-5">
										<input name="uploadAttachment" id="attachment-files" type="file" />
									</div>	
									</#if>
									<div class="row blank-top-5">
										<div class="col-lg-12" align="center"><div id="attachment-list-view" ></div></div>
									</div>
									<div class="row">
										<div class="col-lg-12"><div id="pager" class="k-pager-wrap"></div></div>
									</div>
								</div>														
							</div>
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
		<script type="text/x-kendo-tmpl" id="attachment-list-view-template">
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
		<script id="attachment-preview-template" type="text/x-kendo-template">				
			#if (contentType.match("^image") ) {#
				<img src="${request.contextPath}/secure/view-attachment.do?attachmentId=#= attachmentId #" style="border:0;" class="img-rounded" />
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
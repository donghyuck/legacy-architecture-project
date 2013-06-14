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
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo/kendo.ko_KR.js',
			'${request.contextPath}/js/common/common.ui.min.js',
			'${request.contextPath}/js/common/common.models.js' ],        	  	   
			complete: function() {      
				// START SCRIPT
				
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
		            	
						$("#user-details").kendoTooltip({
		                    content: userDetailsTemplate( currentUser ),
		                    width: 300,
		                    autoHide: false,
		                    showOn: "click",
		                    position: "bottom",
		                });	            	
					},
					error:handleKendoAjaxError,
					dataType : "json"
				});
				
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
											read: { url:'${request.contextPath}/secure/get-user-attachements.do?output=json', type: 'POST' },		
											destroy: { url:'${request.contextPath}/secure/delete-user-attachment.do?output=json', type:'POST' },                                
											parameterMap: function (options, operation){
												if (operation != "read" && options) {										                        								                       	 	
													return { userId: currentUser.userId, attachmentId :options.attachmentId };									                            	
												}else{
													return { userId: currentUser.userId };
												}
											}								                         
										},
										error:handleKendoAjaxError,
										schema: {
											model: Attachment,
											data : "targetUserAttachments"
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
												minWidth :  300,
												modal: false,
												visible: false
											});
											
										}
										
										var attachWindow = $("#attach-window").data("kendoWindow");
										var template = kendo.template($("#template3").html());
										attachWindow.title( item.name );
										attachWindow.content( template(item) );
										
										//attachWindow.center();
										attachWindow.open();
									},
									template: kendo.template($("#template2").html())
								});
								
								$("#attachment-list-view").on("mouseenter", ".attach", 
									function(e) {
										kendo.fx($(e.currentTarget).find(".attach-description")).expand("vertical").stop().play();
									 }).on("mouseleave", ".attach", function(e) {
										kendo.fx($(e.currentTarget).find(".attach-description")).expand("vertical").stop().reverse();
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
				<div class="large-4 columns">
					<h2>마이그로닉스</h2>
				</div>
				<div class="large-8 columns text-right">				
				 	<#if action.user.anonymous > 
			 		<a href="${request.contextPath}/accounts/login.do" class="k-button" >로그인</a></strong>	
				 	<#else>
			 		<a id="user-details" class="k-button">${ action.user.name}&nbsp;<span class="k-icon k-i-arrow-s"></span></a>
				 	</#if>
				</div>
			</div>
		</header>		
		<!-- END HEADER -->
		<!-- START MAIN CONTENT --> 
		
		<div class="row layout">
			<div class="large-7 columns"> 
			<div id="attach-window" ></div>
				<h3>Get in Touch!</h3>
				<p>We'd love to hear from you. You can either reach out to us as a whole and one of our awesome team members will get back to you, or if you have a specific question reach out to one of our staff. We love getting email all day <em>all day</em>.</p>
				
 		
      <div class="section-container tabs" data-section>
        <section class="section">
          <h5 class="title"><a href="#panel1">Contact Our Company</a></h5>
          <div class="content" data-slug="panel1">
            <form>
              <div class="row collapse">
                <div class="large-2 columns">
                  <label class="inline">Your Name</label>
                </div>
                <div class="large-10 columns">
                  <input type="text" id="yourName" placeholder="Jane Smith">
                </div>
              </div>
              <div class="row collapse">
                <div class="large-2 columns">
                  <label class="inline"> Your Email</label>
                </div>
                <div class="large-10 columns">
                  <input type="text" id="yourEmail" placeholder="jane@smithco.com">
                </div>
              </div>
              <label>What's up?</label>
              <textarea rows="4"></textarea>
              <button type="submit" class="radius button">Submit</button>
            </form>
          </div>
        </section>
        <section class="section">
          <h5 class="title"><a href="#panel2">Specific Person</a></h5>
          <div class="content" data-slug="panel2">
            <ul class="large-block-grid-5">
              <li><a href="mailto:mal@serenity.bc.reb"><img src="http://placehold.it/200x200&amp;text=[person]">Mal Reynolds</a></li>
              <li><a href="mailto:zoe@serenity.bc.reb"><img src="http://placehold.it/200x200&amp;text=[person]">Zoe Washburne</a></li>
              <li><a href="mailto:jayne@serenity.bc.reb"><img src="http://placehold.it/200x200&amp;text=[person]">Jayne Cobb</a></li>
              <li><a href="mailto:doc@serenity.bc.reb"><img src="http://placehold.it/200x200&amp;text=[person]">Simon Tam</a></li>
              <li><a href="mailto:killyouwithmymind@serenity.bc.reb"><img src="http://placehold.it/200x200&amp;text=[person]">River Tam</a></li>
              <li><a href="mailto:leafonthewind@serenity.bc.reb"><img src="http://placehold.it/200x200&amp;text=[person]">Hoban Washburne</a></li>
              <li><a href="mailto:book@serenity.bc.reb"><img src="http://placehold.it/200x200&amp;text=[person]">Shepherd Book</a></li>
              <li><a href="mailto:klee@serenity.bc.reb"><img src="http://placehold.it/200x200&amp;text=[person]">Kaywinnet Lee Fry</a></li>
              <li><a href="mailto:inara@guild.comp.all"><img src="http://placehold.it/200x200&amp;text=[person]">Inarra Serra</a></li>
            </ul>
          </div>
        </section>
      </div>
    </div>
 
    <!-- End Contact Details -->
 

 
    <div class="large-4 columns">
      <h5>Map</h5>
      <!-- Clicking this placeholder fires the mapModal Reveal modal -->
	<div id="tabstrip">
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
			<button class="k-button">파일업로드</button>
			<div id="attachment-list-view" ></div>
		</div>
	</div>				
      <p>
        <a href="" data-reveal-id="mapModal"><img src="http://placehold.it/400x280"></a><br />
        <a href="" data-reveal-id="mapModal">View Map</a>
      </p>
      <p>
        123 Awesome St.<br />
        Barsoom, MA 95155
      </p>
    </div>
    
    
  </div>
  
 
		<!-- END MAIN CONTENT -->
		
		<!-- START TEMPLATE -->
		<script id="template" type="text/x-kendo-template">
			<div class="row layout">
				<div class="small-5 columns"><img id="user-details-photo" src="#: photoUrl #" /></div>
				<div class="small-7 columns text-left">
					#: name #
					<p style="font-color:000000;">
							#: email #
					</p>
					<button class="k-button">계정설정</button>&nbsp;<a class="k-button" href="${request.contextPath}/logout" >로그아웃</a>							
				</div>
			</div>	
		</script>
		<script type="text/x-kendo-tmpl" id="template2">
			<div class="attach">			
			#if (contentType.match("^image") ) {#
				<img src="${request.contextPath}/secure/view-attachment.do?attachmentId=#:attachmentId#" alt="#:name# 이미지"/>
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
			<a class="k-button" href="${request.contextPath}/secure/download-attachment.do?attachmentId=#= attachmentId #" >변경</a>	
			<a class="k-button" href="${request.contextPath}/secure/download-attachment.do?attachmentId=#= attachmentId #" >삭제</a>	
			</p>
		# } else { #
		
			<div class="k-grid k-widget" style="width:500px;">
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
			<a class="k-button" href="${request.contextPath}/secure/download-attachment.do?attachmentId=#= attachmentId #" >변경</a>	
			<a class="k-button" href="${request.contextPath}/secure/download-attachment.do?attachmentId=#= attachmentId #" >삭제</a>	
			</p>	
		# } #  		
	</script>		
		<!-- END TEMPLATE -->
		<!-- START FOOTER -->
		<footer> 
		</footer>
		<!-- END FOOTER -->	
	</body>    
</html>
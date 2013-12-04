<script type="text/x-kendo-tmpl" id="announcement-template">
	<tr class="announce-item" onclick="viewAnnounce(#: announceId#);">
		<th>#: announceId#</th>
		<td>#: subject#</td>
	</tr>
</script>
<script type="text/x-kendo-tmpl" id="announcement-view-template">
# if ( user.userId == ${action.user.userId } && "${action.view!}" == "personalized" ) {#  	
	<input type="text" placeholder="Enter name" data-bind="value: subject"  class="form-control" placeholder=".col-xs-4" />		
	<div class="blank-top-5" ></div>		
	<ul class="list-group">
		<li class="list-group-item">
		시작일시: <input data-role="datetimepicker" data-bind="value:startDate" style="width: 220px; height:28px;">
		<div class="blank-top-5" ></div>
		종료일시: <input data-role="datetimepicker" data-bind="value:endDate" style="width: 220px; height:28px;">
		</li>
		<li class="list-group-item">
		<div class="media">
			<a class="pull-left" href="\\#">
			#if ( user.properties.imageId != null ) {# 
			<img src="${request.contextPath}/accounts/view-image.do?width=100&height=150&imageId=#: user.properties.imageId#" width="30" height="30" class="img-thumbnail">	
			# } else {  #	
			<img src="${request.contextPath}/images/common/anonymous.png" width="30" height="30" class="img-circle">
			# } #
			</a>
			<div class="media-body">
				<h5 class="media-heading">
					# if( user.nameVisible ){#
					#: user.name # (#: user.username #)
					# } else { #
					#: user.username #
					# } # 		
					# if( user.emailVisible ){#
					<br>(#: user.email #)
					# } #	
				</h5>
			</div>
		</div>			
		</li>
	</ul>
	<div contentEditable class="inline-body-editor" data-role="editor" 
					data-tools="['italic',
									'underline',
									'strikethrough']" 
				data-bind="value:body"></div>			
	<div class="blank-top-5" ></div>				
	<div class="pull-right">
			<div class="btn-group">
			<button type="button" class="btn btn-primary custom-announce-modify" data-announceId="#: announceId #" disabled="disabled">수정</button>
			<button type="button" class="btn btn-danger custom-announce-delete" data-announceId="#: announceId #" disabled="disabled">삭제</button>
			</div>
	</div>		
	
# } else {  #
		<h4 data-bind="html:subject"></h4>
		<small class="text-muted">기간 : #: kendo.toString(startDate, "yyyy.MM.dd hh:mm") # ~  #: kendo.toString(endDate, "yyyy.MM.dd hh:mm") #</small><br>
		<div class="media">
			<a class="pull-left" href="\\#">
			#if ( user.properties.imageId != null ) {# 
			<img src="${request.contextPath}/accounts/view-image.do?width=100&height=150&imageId=#: user.properties.imageId#" width="30" height="30" class="img-thumbnail">	
			# } else {  #	
			<img src="${request.contextPath}/images/common/anonymous.png" width="30" height="30" class="img-circle">
			# } #
			</a>
			<div class="media-body">
				<h5 class="media-heading">
					# if( user.nameVisible ){#
					#: user.name # (#: user.username #)
					# } else { #
					#: user.username #
					# } # 		
					# if( user.emailVisible ){#
					<br>(#: user.email #)
					# } #	
				</h5>		
			</div>
		</div>	
		<div class="blank-top-5" ></div>
		<div data-bind="html:body"></div>
# } #				
</script>


<script type="text/x-kendo-tmpl" id="social-view-panel-template">
		<div id="#: provider #-panel" class="panel panel-success">
			<div class="panel-heading">
				<i class="icon-#: provider #"></i> &nbsp; #: provider # &nbsp; 소식
				<div class="k-window-actions panel-header-actions">
					<a role="button" href="\\#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-refresh">Refresh</span></a>
					<a role="button" href="\\#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-minimize">Minimize</span></a>
					<a role="button" href="\\#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-maximize">Maximize</span></a>
					<a role="button" href="\\#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-close">Close</span></a>
				</div>							
			</div>
			<div class="panel-body scrollable" style="max-height:500px;">
				<ul class="media-list">
					<div id="#:provider#-streams">데이터가 없습니다..</div>
				</ul>
			</div>
		</div>				
</script>

<script type="text/x-kendo-tmpl" id="facebook-homefeed-template">
		#if (type != "STATUS") {#
		<li class="media">
		    <a class="pull-left" href="\\#">
		    	<img class="media-object img-circle" src="http://graph.facebook.com/#=from.id#/picture" alt="프로파일 이미지">
		    </a>
		    <div class="media-body">
		      <h5 class="media-heading">
		      <span class="label label-primary">#: type #</span><br><br>
		      #: from.name #  (#: ui.util.prettyDate(updatedTime) #) 
		      </h5>		     	
		     	#if ( typeof( message ) == 'string'  ) { #
		     	<br>
		     	#: message #
		     	# } #				     	     	
		     	#if ( name !=null ) { #
		     	<br>#: name  #
		     	# } #		     	     	
		     	#if ( type == 'LINK' ) { #
		     	<br>
		     	<span class="glyphicon glyphicon-link"></span>&nbsp;<a href="#: link #">#: link #</a>
		     	# } else if ( type == 'PHOTO' ) { #
		     		<br>
		     		<img src="#: picture.replace("_s.", "_n.")  #" alt="media" class="img-rounded img-responsive">
		     	# } else { #		     	
		     		#if ( picture !=null ) { #
		     		<br><img src="#: picture.replace("_s.", "_n.")  #" alt="media" class="img-rounded img-responsive">
		     		# } #		     		
		     		#if ( source !=null ) { #
		     		<br>source : <span class="glyphicon glyphicon-link"></span>&nbsp;<a href="#: source #">#: source #</a>
		     		# } #
		     	# } #
		     	
		     	#if ( typeof( caption ) == 'string'  ) { #
		     	<br><br>
		     	<blockquote>
		     	<p>#: caption #</p>
				</blockquote>
		     	# } #		     	
		     	#if ( typeof( description ) == 'string'  ) { #
		     	<blockquote><p class="text-muted"><small>
		     	#: description #
		     	</small></p>
		     	</blockquote>
		     	# } #				     							
				# for (var i = 0; i < comments.length ; i++) { #												
				# var comment = comments[i] ; #							
					<div class="media">
						<a class="pull-left" href="\\#">
							<img class="media-object img-circle" src="http://graph.facebook.com/#=comment.from.id#/picture" alt="프로파일 이미지" class="img-circle">
						</a>	
						<div class="media-body">
							 <h6 class="media-heading">#: comment.from.name # &nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-thumbs-up"></span>#:comment.likesCount#</h6>
							 <p class="text-muted">#:comment.message#</p>
						</div>				
					</div>				
				# } #
			</div>
		</li>					
		# } #  	
</script>		
<script type="text/x-kendo-tmpl" id="twitter-timeline-template">
		<li class="media">
		    <a class="pull-left" href="\\#">
		      <img src="#: user.profileImageUrl #" alt="#: user.name#" class="media-object img-circle">
		    </a>
		    <div class="media-body">
		      <h5 class="media-heading">#: user.name # (#: ui.util.prettyDate(createdAt) #)</h5>
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
		

<script type="text/x-kendo-template" id="image-view-template">
	<div class="panel panel-default">
		<div class="panel-heading">#= name # 미리보기<button id="image-view-btn-close" type="button" class="close">&times;</button></div>
		<div class="panel-body">
			#if (contentType.match("^image") ) {#
			<img src="${request.contextPath}/community/view-my-attachment.do?attachmentId=#:attachmentId#" alt="#:name# 이미지" class="img-responsive"/>
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
			# } #  	
			<p class="blank-top-5">
				<input name="update-attach-file" id="update-attach-file" type="file" />
				<div class="blank-top-5 "></div>
				<a class="btn btn-default" href="${request.contextPath}/community/download-my-attachment.do?attachmentId=#= attachmentId #" >다운로드</a>
				<button  type="button" class="btn btn-danger custom-attachment-delete"  data-for-attachmentId="#:attachmentId #" >삭제</button>					
			</p>					
		</div>
	</div>
</script>

<script type="text/x-kendo-template" id="photo-gallery-template">
	<div class="panel panel-default">
		<div class="panel-heading">My 포토 갤러리		
			<div class="k-window-actions panel-header-actions">
				<a role="button" href="\\#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-refresh">Refresh</span></a>
				<a role="button" href="\\#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-minimize">Minimize</span></a>
				<a role="button" href="\\#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-maximize">Maximize</span></a>
				<a role="button" href="\\#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-close">Close</span></a>
			</div>		
		</div>
		<div class="panel-body">
			<div id="photo-gallery-pager" class="k-pager-wrap"></div>
			<div id="photo-gallery-view"></div>
		</div>
	</div>		
</script>

<script type="text/x-kendo-template" id="photo-gallery-view-template">
	<div class="inner clearfix">
		<div class="bx-wrapper" style="max-width: 100%;">
			<div class="bx-viewport" style="width: 100%; overflow: hidden; position: relative; height: 190px;">
				<ul class="bxslider" style="width: auto; position: relative;">
					<li style="float: none; list-style: none; position: absolute; width: 865px; z-index: 50; display: list-item;">
						<img src="${request.contextPath}/community/view-my-image.do?imageId=#:imageId#" class="img-responsive" alt="#:name# 이미지"/>
						<div class="bx-caption">
							<span>
								<h5>#:name#</h5><p class="text-muted">#: modifiedDate #</p>
							</span>
						</div>
					</li>
				</ul>
			</div>
			<div class="bx-controls bx-has-pager bx-has-controls-direction">
				<div class="bx-pager bx-default-pager">
					<div class="bx-controls-direction">
						<a class="bx-prev" href="">Prev</a>
						<a class="bx-next" href="">Next</a>
					</div>
				</div>
			</div>
		</div>
	</div>	
			
</script>

<script type="text/x-kendo-template" id="photo-view-template">
	<div class="panel panel-default">
		<div class="panel-heading">#= name # 미리보기<button id="image-view-btn-close" type="button" class="close">&times;</button></div>
		<div class="panel-body">
			<img src="${request.contextPath}/community/view-my-image.do?imageId=#:imageId#" alt="#:name# 이미지" class="img-responsive"/>
			<p class="blank-top-5">
				<input name="update-photo-file" id="update-photo-file" type="file" />
				<div class="blank-top-5 "></div>
				<a class="btn btn-default" href="${request.contextPath}/community/download-my-image.do?imageId=#= imageId #" >다운로드</a>
				<button  type="button" class="btn btn-danger custom-photo-delete"  data-for-imageId="#:imageId #" >삭제</button>					
			</p>					
		</div>
	</div>
</script>

<script id="account-template" type="text/x-kendo-template">	
	<div class="dropdown">
		<a href="\\#" class="dropdown-toggle" data-toggle="dropdown">
		#if (photoUrl != null && photoUrl != 'null' && photoUrl != '')  { #
			<img src="#:photoUrl#"  width="30" height="30" alt="#:name#"/><span class="k-icon k-i-arrow-s"></span>
		# } else { # 
			<img src="${request.contextPath}/images/common/anonymous.png"  width="30" height="30"/><span class="k-icon k-i-arrow-s"></span>
		# } #	
		</a>
		<ul class="dropdown-menu">
			# if ( !anonymous ) { # 
			<li>
				<div class="blank-space-5">	
					<ul class="media-list">
						<li class="media">
							<a class="pull-left" href="\\#">
								#if (photoUrl != null && photoUrl != 'null' && photoUrl != '')  { #
								<img class="media-object img-thumbnail" src="#:photoUrl#" />
								# } else { # 
								<img class="media-object img-thumbnail" src="http://placehold.it/100x150&amp;text=[No Photo]" />
								# } #	
							</a>
							<div class="media-body" style="color:ccc;">
								<p class="text-muted"><strong> #:name#</strong></p>
								<p class="text-muted"> #:email #</p>								
								<ul class="nav nav-pills nav-stacked">
									<li class="active">
										<a href="\\#">
										<span class="badge pull-right">3</span>
										Home
										</a>
									</li>
									<li>
										<a href="\\#">
										<span class="badge pull-right">1</span>
										알림
										</a>
									</li>
									<li>
										<a href="\\#">
										<span class="badge pull-right">2</span>
										메시지
										</a>
									</li>																			
								</ul>
							</div>
						</li>
					</ul>
				</div>
			</li>
			<li class="divider"></li>
			<li><a href="\\#">프로필 보기</a></li>
			<li><a href="/main.do?view=personalized">마이 페이지</a></li>
			#if (isSystem ) {#
			<li><a href="/secure/main-site.do">시스템 관리하기</a></li>
			# } #
			<li class="divider"></li>
			<li><a href="/logout">로그아웃</a></li>
			# } else { #  			
			<li>
				<div class="container layout">
					<div class="row blank-top-5 ">
						<div class="col-lg-12">
							<button class="btn btn-block btn-facebook"><i class="icon-facebook"></i> | 페이스북으로 로그인</button>
						</div>
					</div>		
					<div class="row blank-top-5 ">
						<div class="col-lg-12">
							<button class="btn btn-block btn-twitter"><i class="icon-twitter"></i> | 트위터로 로그인</button>
						</div>
					</div>					
				</div>
			</li>
			<li class="divider"></li>
			<li>
				<div class="container bg-white layout" id="login-panel">													
					<div class="row blank-top-5 ">
						<div class="col-lg-12">
							<form role="form" name="login-form" method="POST" accept-charset="utf-8">
								<input type="hidden" id="output" name="output" value="json" />		
								<div class="form-group">
									<label for="login-username">아이디 또는 이메일</label>
									<input type="text" class="form-control" id="login-username" name="username" placeholder="아이디 또는 이메일" required validationMessage="아이디 또는 이메일을 입력하여 주세요.">
								</div>
								<div class="form-group">
									<label for="login-password">비밀번호</label>
										<input type="password" class="form-control" id="login-password" name="password"  placeholder="비밀번호"  required validationMessage="비밀번호를 입력하여 주세요." >
								</div>				 
								<button type="button" id="login-btn" class="btn btn-primary btn-block">로그인</button>
							</form>						
						</div>
					</div>
					<div class="row">
						<div class="col-lg-12">
							<div id="login-status" class="blank-top-5"/>
						</div>
					</div>	
				</div>
			</li>
			<li class="divider"></li>
			<li><a href="\\#">아이디/비밀번호찾기</a></li>
			<li><a href="\\#">회원가입</a></li>
			# } #
		</ul>
	</div>									
</script>
		
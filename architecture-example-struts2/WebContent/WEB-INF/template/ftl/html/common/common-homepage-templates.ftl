<script id="account-template" type="text/x-kendo-template">	
	<div class="dropdown">
		#:name#
		<a class="dropdown-toggle" data-toggle="dropdown">
		#if (photoUrl != null && photoUrl != 'null' && photoUrl != '')  { #
			<img src="#:photoUrl#"  width="30" height="30" /><span class="k-icon k-i-arrow-s"></span>
		# } else { # 
			<img src="http://placehold.it/100x150&amp;text=[No Photo]"  width="30" height="30"/><span class="k-icon k-i-arrow-s"></span>
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
			#if (isSystem ) {#
			<li><a href="/secure/main.do">시스템 관리하기</a></li>
			# } #
			<li class="divider"></li>
			<li><a href="/logout">로그아웃</a></li>
			# } else { #  
			<li>
				<div class="container bg-white layout" id="login-panel">
					<div class="row">
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
			<li><a href="\\#">회원가입</a></li>
			# } #
		</ul>
	</div>									
</script>
		
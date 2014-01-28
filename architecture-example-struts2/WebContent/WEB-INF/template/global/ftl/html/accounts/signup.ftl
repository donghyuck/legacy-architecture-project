<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title>회원가입</title>
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.0.3/font-awesome.min.css',
			'${request.contextPath}/js/jquery/1.9.1/jquery.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo/kendo.ko_KR.js',			
			'${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js',		
			'${request.contextPath}/js/bootstrap/3.0.3/bootstrap.min.js',	
			'${request.contextPath}/js/common/common.cbpBGSlideshow.min.js',
			'${request.contextPath}/js/jquery.imagesloaded/imagesloaded.min.js',
			'${request.contextPath}/js/common/modernizr.custom.js',			
			'${request.contextPath}/js/common/common.models.min.js',
			'${request.contextPath}/js/common/common.ui.min.js'],
			complete: function() {
			
				// 1.  한글 지원을 위한 로케일 설정
				kendo.culture("ko-KR");
				      
				// START SCRIPT	
				cbpBGSlideshow.init();

				$('#signup-window').modal({show:true, backdrop:false});

				// END SCRIPT            
			}
		}]);	
		-->
		</script>		

		</style>   	
	</head>
	<body>
		<div class="main">
			<ul id="cbp-bislideshow" class="cbp-bislideshow">
				<li><img src="${request.contextPath}/community/download-image.do?imageId=175" alt="image01"/></li>
				<li><img src="${request.contextPath}/community/download-image.do?imageId=808" alt="image02"/></li>
				<li><img src="${request.contextPath}/community/download-image.do?imageId=810" alt="image03"/></li>
			</ul>
				<!-- 
				<div id="cbp-bicontrols" class="cbp-bicontrols">
					<span class="fa cbp-biprev"></span>
					<span class="fa cbp-bipause"></span>
					<span class="fa cbp-binext"></span>
				</div>
				 -->
		</div>


	<!-- Modal -->
	<div class="modal fade" id="signup-window" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title">회원가입</h4>
				</div>
				<div class="modal-body">
					<div class="container" style="width:100%;">
						<div class="row blank-top-5 ">
								<div class="col-sm-6">
									<button class="btn btn-block btn-primary btn-lg custom-social-groups"  data-target="facebook"><i class="fa fa-facebook"></i> | 페이스북으로 회원가입</button>
								</div>
								<div class="col-sm-6">
									<button class="btn btn-block btn-info btn-lg custom-social-groups" data-target="twitter"><i class="fa fa-twitter"></i> | 트위터로 회원가입</button>
								</div>
						</div>	
						<div class="row blank-top-15">
							<div class="col-sm-6">
							
							</div>
							<div class="col-sm-6">
								<form role="form">
									<div class="form-group">
										<label for="signupInputName">이름</label>
										<input type="text" class="form-control" id="signupInputName" placeholder="이름">
									</div>
									<div class="form-group">
										<label for="signupInputUsername">아이디</label>
										<input type="text" class="form-control" id="signupInputUsername" placeholder="아이디">
									</div>									
									<div class="form-group">
										<label for="signupInputEmail">이메일 주소</label>
										<input type="email" class="form-control" id="signupInputEmail" placeholder="이메일 주소">
									</div>
									<div class="form-group">
										<label for="signupInputPassword1">비밀번호</label>
										<input type="password" class="form-control" id="signupInputPassword1" placeholder="비밀번호">
									</div>
									<div class="form-group">
										<label for="signupInputPassword2">비밀번호 확인</label>
										<input type="password" class="form-control" id="signupInputPassword2" placeholder="비밀번호 확인">
									</div>									
									<div class="checkbox">
										<label>
											<input type="checkbox"  id="signupInputAgree"> 서비스 약관과 개인정보취급방침 및 개인정보 수집항목•이용목적•보유기간에 동의합니다.
										</label>
									</div>
								</form>
							</div>
						</div>
					</div>
					<!--

					
					<div class="container" style="width:100%;">					
						<div class="row blank-top-15">
							<div class="col-lg-12">
								<form name="fm1" class="form-horizontal" role="form" method="POST" accept-charset="utf-8">
									<input type="hidden" id="output" name="output" value="json" />		    
									<div class="form-group">
										<label for="username" class="col-lg-3 control-label">아이디</label>
										<div class="col-lg-4">
											<input type="text" class="form-control"  id="username" name="username"  pattern="[^0-9][A-Za-z]{2,20}" placeholder="아이디" required validationMessage="아이디를 입력하여 주세요.">
										</div>
									</div>
									<div class="form-group">
										<label for="password" class="col-lg-3 control-label">비밀번호</label>
										<div class="col-lg-4">
											<input type="password" class="form-control" id="password" name="password"  placeholder="비밀번호" required validationMessage="비밀번호를 입력하여 주세요." >
										</div>
									</div>
									<div class="form-group">
										<div class="col-lg-offset-3 col-lg-9">
											<div class="checkbox">
												<label>
													<input type="checkbox">로그인 상태유지  
												</label>
											</div>
										</div>
									</div>
									<div class="col-lg-12">
										<div id="status">								
										</div>
									</div>
								</form>						
							</div>
							<div class="col-lg-12"></div>
						</div>
					</div>
					-->
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-info" >확인</button>
					<!--
					<div class="btn-group ">
						<button type="button" class="btn btn-info" >아이디/비밀번호찾기</button>
						<a id="signup"  href="${request.contextPath}/accounts/login.do"  class="btn btn-info">로그인</a>
					</div>
					-->
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal-dialog -->
	</div><!-- /.modal -
			
	</body>    
</html>
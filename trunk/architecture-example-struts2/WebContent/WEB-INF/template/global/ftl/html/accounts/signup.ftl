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

				$("#signup-window button.custom-social-groups").each(function( index ) {
					var external_button = $(this);
					external_button.click(function (e){	
						$("#status").html("");																																
						var target_media = external_button.attr("data-target");
						$.ajax({
							type : 'POST',
							url : "${request.contextPath}/community/get-socialnetwork.do?output=json",
							data: { media: target_media },
							success : function(response){
								if( response.error ){
									// 연결실패.
								} else {	
									window.open( response.authorizationUrl + '&display=popup&isSignup=ture' ,'popUpWindow','height=500,width=600,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no,status=yes')
								}
							},
							error:handleKendoAjaxError												
						});
					});		
				});						
				
				$("#signup-form :input:visible:enabled:first").focus();				
				$("#signup-form").data("validatorPlaceHolder", new kendo.data.ObservableObject({}) );			
				$("#signup-form").kendoValidator({
					errorTemplate: '<span class="help-block">#=message#</span>',
					rules: {
						verifyPasswords:function(input){
							var ret = true;
							if (input.is("[name=signupInputPassword2]")) {
								ret = input.val() === $("#signupInputPassword1").val();
							}
							return ret;
						},
						userNameAvailable: function(input){
							var validate = input.data('available');
							if (typeof validate !== 'undefined' && validate !== false) {							
								var validatorPlaceHolder = $("#signup-form").data("validatorPlaceHolder");
								var input_id = input.attr('id');
								var available_cache= validatorPlaceHolder.get(input_id);						
	
								if( typeof available_cache !== 'undefined' ){
								alert("--------------" + available_cache) ;
									return available_cache;
								}else{
									$.ajax({
										type : 'POST',
										url : "${request.contextPath}/accounts/check-username-available.do?output=json",
										dataType: 'json',
										data: { username: input.val() },
										success : function(response){									
											if (typeof response.usernameAvailable !== 'undefined' && response.usernameAvailable ){
												validatorPlaceHolder.set( input_id, response.usernameAvailable );												
												var validator = $("#signup-form").data("kendoValidator");		
												alert("--------------" +  response.usernameAvailable ) ;
												validator.validateInput($(input_id));
											}											
										},
										error:handleKendoAjaxError
									});	
									return false;
								}								
							}
							return true;
						}
					},
					messages: {
						verifyPasswords : "비밀번호가 일치하지 않습니다.",
						userNameAvailable : function( input ) {
							return "확인중..."
						}
					},
					validateOnBlur : false
				});
				
				$(":button.logout").click( function(e) {					
					$(this).button("로그아웃...");
					var text_danger = $(this).parent().parent();
					$.ajax({
						type : 'GET',
						url : "${request.contextPath}/logout?output=json",
						success : function(response){
							text_danger.remove();
							$("fieldset").each(function( index ) {					
								$(this).removeAttr("disabled"); 
							});
							$("#form :input:visible:enabled:first").focus();
						},
						error:handleKendoAjaxError												
					});						
				} );
				
				$(":button.homepage").click( function(e) {					
					homepage();					
				} );				
				
				$(":button.signup").click( function(e) {					
					var validatable = $("#signup-form").data("kendoValidator");					
					$("#signup-form :input").each( function() {
						var _input = $(this);
						if( validatable.validateInput( _input ) ){
							_input.parent().addClass("has-success");
						}else{
							_input.parent().addClass("has-error");
						}
					});				
				});				
				// END SCRIPT            
			}
		}]);	
		
		
		function signupCallbackResult( provider, data  ){
			if( data == null ){
				homepage();					
			}else{
				var sf = new SignupForm(data);	
				var fm = $("form[name='fm1']");				
				fm.data("signupPlaceHolder", sf);				
				kendo.bind(fm, sf );				
			}			
		}
		
		function homepage(){
			$("form[name='fm1']")[0].reset();               	   
			$("form[name='fm1']").attr("action", "/main.do").submit();
		}
		
		-->
		</script>
		<style>

		.k-widget.k-tooltip-validation {
			background-color: transparent ;
			color: #a94442;
			border-width: 0;
		}
		
		.k-tooltip {
			-webkit-box-shadow : 0 0 0 0 ;
			box-shadow : 0 0 0 0;
		}
		
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
				<div class="modal-body bg-gray">
					<fieldset <#if !action.user.anonymous >disabled</#if>>
						<div class="col-sm-6">
							<button class="btn btn-block btn-primary btn-lg custom-social-groups"  data-target="facebook"><i class="fa fa-facebook"></i> | 페이스북으로 회원가입</button>
						</div>
						<div class="col-sm-6">
							<button class="btn btn-block btn-info btn-lg custom-social-groups" data-target="twitter"><i class="fa fa-twitter"></i> | 트위터로 회원가입</button>
						</div>
					</fieldset>		
				</div>
				<div class="modal-body">
						<div class="row">
							<div class="col-sm-12">
								<div id="status"></div>								
								<#if !action.user.anonymous >
								<p class="text-danger">
								<i class="fa fa-info"></i> 현재 로그인 상태입니다.  
								<div class="btn-group ">
									<button type="button" class="btn btn-info btn-sm homepage" ><i class="fa fa-home"></i> 홈페이지 이동</button>
									<button type="button" data-loading-text="로그아웃중 ..." class="btn btn-danger btn-sm logout">로그아웃</button>								
								</div>
								</p>
								</#if>								
							</div>
						</div>	
						<div class="row blank-top-15">
							<div class="col-sm-5"></div>
							<div class="col-sm-7">
								<form role="form" id="signup-form" name="fm1" method="POST" accept-charset="utf-8" >
									<fieldset <#if !action.user.anonymous >disabled</#if>>
										<div class="form-group">
											<label for="signupInputName">이름</label>
											<input type="text" class="form-control" id="signupInputName" name="signupInputName" placeholder="이름" data-bind="value: name" required data-required-msg="이름을 입력하여 주십시오." >
										</div>
										<div class="form-group">
											<label for="signupInputUsername">아이디</label>
											<input type="text" class="form-control" id="signupInputUsername" name="signupInputUsername" placeholder="아이디" data-bind="value: username" data-available  required data-required-msg="아이디를 입력하여 주십시오." >
											 <span data-for="RetireDate"></span>
										</div>									
										<div class="form-group">
											<label for="signupInputEmail">이메일 주소</label>
											<input type="email" class="form-control" id="signupInputEmail" name="signupInputEmail"  placeholder="이메일 주소" data-bind="value: email" required  data-required-msg="메일주소를 입력하여 주십시오." data-email-msg="메일주소 형식이 바르지 않습니다." >
										</div>
										<div class="form-group">
											<label for="signupInputPassword1">비밀번호</label>
											<input type="password" class="form-control" id="signupInputPassword1" name="signupInputPassword1"  placeholder="비밀번호" data-bind="value: password1" required data-required-msg="비밀번호를 입력하여 주십시오.">
										</div>
										<div class="form-group">
											<label for="signupInputPassword2">비밀번호 확인</label>
											<input type="password" class="form-control" id="signupInputPassword2" name="signupInputPassword2"  placeholder="비밀번호 확인" data-bind="value: password2" required data-required-msg="비밀번호를 다시한번 입력하여 주십시오.">
										</div>									
										<div class="checkbox">
											<label>
												<input type="checkbox"  id="signupInputAgree" name="signupInputAgree" required validationMessage="회원가입을 위하여 동의가 필요합니다."> 서비스 약관과 개인정보취급방침 및 개인정보 수집항목•이용목적•보유기간에 동의합니다.
											</label>
										</div>
									</fieldset>	
								</form>
							</div>
						</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-info signup">확인</button>
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
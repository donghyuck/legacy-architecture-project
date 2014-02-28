<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title>회원가입</title>
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.0.3/font-awesome.min.css',
			'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo/kendo.ko_KR.js',			
			'${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js',		
			'${request.contextPath}/js/bootstrap/3.0.3/bootstrap.min.js',	
			'${request.contextPath}/js/common/common.cbpBGSlideshow.min.js',
			'${request.contextPath}/js/jquery.imagesloaded/imagesloaded.min.js',
			'${request.contextPath}/js/common/common.modernizr.custom.js',
			'${request.contextPath}/js/common/common.classie.min.js',
			'${request.contextPath}/js/common/common.models.js',
			'${request.contextPath}/js/common/common.api.js',
			'${request.contextPath}/js/common/common.ui.js'],
			complete: function() {
			
				// 1.  한글 지원을 위한 로케일 설정
				kendo.culture("ko-KR");
				
				// START SCRIPT	
				
				var photo_template = kendo.template('<li><img src="${request.contextPath}/community/view-streams-photo.do?key=#= externalId#" alt="이미지"/></li>');
				
				common.api.photoStreamsDataSource.fetch(function(){
					var data = this.data();
					$.each( data , function(index, item ){
						$('#cbp-bislideshow').append(photo_template(item));
						alert("a");
					});
					alert("a");
					cbpBGSlideshow.init();					
				});
				
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
				
				//$("#signup-form :input:visible:enabled:first").select();				
				$("#signup-form").data("signupPlaceHolder", new  SignupForm({}) );		
				$("#signup-form").data("validatorPlaceHolder", new kendo.data.ObservableObject({}) );			
				
				var validator = $("#signup-form").kendoValidator({
					errorTemplate: '<span class="help-block">#=message#</span>',
					rules: {
						verifyPasswords:function(input){
							var ret = true;
							if (input.is("[name=signupInputPassword2]")) {
								ret = input.val() === $("#signupInputPassword1").val();
							}
							return ret;
						},
						agree: function(input){
							var ret = true;
							if (input.is("[name=signupInputAgree]")) {
								ret = input.is(':checked');  
							}
							return ret;							
						},
						userNameAvailable: function(input){
							var validate = input.data('available');
							if (typeof validate !== 'undefined' && validate !== false) {							
								var validatorPlaceHolder = $("#signup-form").data("validatorPlaceHolder");
								//var input_id = input.attr('id');
								
								var available_cache= validatorPlaceHolder.get(input.val() );						
								if( typeof available_cache !== 'undefined' ){
									return available_cache;
								}else{
									$.ajax({
										type : 'POST',
										url : "${request.contextPath}/accounts/check-username-available.do?output=json",
										dataType: 'json',
										data: { username: input.val() },
										success : function(response){			
											if (typeof response.usernameAvailable !== 'undefined' ){	
												validatorPlaceHolder.set( input.val(), response.usernameAvailable );				
												if( validator.validateInput(input) ){
													input.parent().addClass("has-success");
												}
											}											
										},
										error:handleKendoAjaxError
									});	
								}	
								return false;							
							}
							return true;
						}
					},
					messages: {
						verifyPasswords : "비밀번호가 일치하지 않습니다.",
						userNameAvailable : function( input ) {
							var validatorPlaceHolder = $("#signup-form").data("validatorPlaceHolder");
							var available_cache= validatorPlaceHolder.get(input.val() );			
							if( typeof available_cache !== 'undefined' && !available_cache){
								return "사용할 수 없는 아이디입니다.";
							}			
							return "확인중...";
						}
					},
					validateOnBlur : false
				}).data("kendoValidator");
				
				$("#signup-form :input").each(function( index ) {				
					var input_to_use = $(this);
					var inputs = input_to_use.parents("form").eq(0).find(":input");					
					input_to_use.focusout(function(){							
						if( validateRequired( input_to_use ) ){
							if( validator.validateInput( input_to_use ) ){
								input_to_use.parent().removeClass("has-error");
								input_to_use.parent().addClass("has-success");
							}else{
								input_to_use.parent().removeClass("has-success");
								input_to_use.parent().addClass("has-error");
							}
						}else{
							input_to_use.parent().removeClass("has-error");
							input_to_use.parent().removeClass("has-success");					
							input_to_use.parent().find("span").remove();	
						}
					});
					input_to_use.keydown(function(e) {
						var keycode = (event.keyCode ? event.keyCode : event.which);						
						if(keycode == '13'){						
							if( index+1 == inputs.length){
								if( !input_to_use.is(':checked') ){
									input_to_use.attr('checked', true );
								}
								$(":button.signup").focus();
							}							
							inputs[index+1].focus();
						}
					});
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
						
				
				$(":button.signup").click( function(e) {	
					var hasError = false;				
					$("#signup-form :input").each(function( index ) {				
						var input_to_use = $(this);
						if( validateRequired( input_to_use ) ){
							var valid = validator.validateInput( input_to_use ) ;
							if(valid ){
								input_to_use.parent().removeClass("has-error");
								input_to_use.parent().addClass("has-success");
							}else{
								if( !hasError ){
									input_to_use.focus();
								}
								hasError = true;							
								input_to_use.parent().removeClass("has-success");
								input_to_use.parent().addClass("has-error");
							}
						}else{
							input_to_use.parent().removeClass("has-error");
							input_to_use.parent().removeClass("has-success");
							input_to_use.parent().find("span").remove();	
							if( !$("#signupInputAgree").is(':checked')){
								$("#signupInputAgree").focus();
							}							
						}
					});			
				});				
				
				$(":button.homepage").click( function(e) {					
					homepage();					
				} );		
								
				// END SCRIPT            
			}
		}]);	
		
		function validateRequired ( input ) {
			var signupPlaceHolder = getSignupPlaceHolder();
			if( signupPlaceHolder.isExternal() ){				
				if(input.is("[name=signupInputEmail]") || 
					input.is("[name=signupInputPassword1]") || 
					input.is("[name=signupInputPassword2]") 
				){
					return false;
				} 
			}
			return true;			
		}
		
		function signupCallbackResult( media, code , exists  ){
			if(exists){
				if( code != null && code != ''  ){						
					var onetime_url =  "${request.contextPath}/community/" + media + "-callback.do?output=json";			
					common.api.signin({
						url : onetime_url,
						onetime:  code,
						success : function(response){
							//$("form[name='fm']")[0].reset();               	    
							//$("form[name='fm']").attr("action", "/main.do").submit();
							homepage();
						}
					}); 
				}else{
					alert( media +  "인증에 실패하였습니다." );
				}			
			}else{
				// register ... 
				
			}		
		}
		
		function getSignupPlaceHolder(){
			var signupPlaceHolder =  $("#signup-form").data("signupPlaceHolder");				
			return signupPlaceHolder ;	
		}
		
		function homepage(){
			window.location.replace("/main.do");
			//$("#signup-form")[0].reset();               	   
			//$("#signup-form").attr("action", "/main.do").submit();
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
					<a href="${request.contextPath}/accounts/login.do"  class="btn btn-link">로그인</a>
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
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
									window.open( response.authorizationUrl + '&display=popup' ,'popUpWindow','height=500,width=600,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no,status=yes')
								}
							},
							error:handleKendoAjaxError												
						});
					});		
				});						
				
				$(":button.logout").click( function(e) {
					var text_danger = $(this).parent();
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
				
				$("#form :input:visible:enabled:first").focus();
				
				// END SCRIPT            
			}
		}]);	
		
		
		function signupCallbackResult( data  ){
			if( data == null ){
				$("#status").kendoAlert({ 
					data : { message : "이미가입된 계정입니다." }
				});
			}else{
				
			}
		}
				
		-->
		</script>		
		<style>   
		
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
							<fieldset <#if !action.user.anonymous >disabled</#if>>
								<div class="col-sm-6">
									<button class="btn btn-block btn-primary btn-lg custom-social-groups"  data-target="facebook"><i class="fa fa-facebook"></i> | 페이스북으로 회원가입</button>
								</div>
								<div class="col-sm-6">
									<button class="btn btn-block btn-info btn-lg custom-social-groups" data-target="twitter"><i class="fa fa-twitter"></i> | 트위터로 회원가입</button>
								</div>
							</fieldset>		
						</div>	
						<div class="row blank-top-15">
							<div class="col-sm-5">
								<div id="status"></div>
								
								<#if !action.user.anonymous >
								<p class="text-danger">
								<i class="fa fa-info"></i> 로그인 상태입니다.  <button type="button" class="btn btn-danger btn-sm logout">로그아웃</button>
								</p>
								</#if>
								
							</div>
							<div class="col-sm-7">
								<form role="form">
									<fieldset <#if !action.user.anonymous >disabled</#if>>
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
									</fieldset>	
								</form>
							</div>
						</div>
					</div>
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
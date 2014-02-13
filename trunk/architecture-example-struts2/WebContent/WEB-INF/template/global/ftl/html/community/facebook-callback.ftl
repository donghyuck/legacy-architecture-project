<#ftl encoding="UTF-8"/>
<html decorator="secure-metro">
	<head>
		<title>페이스북 인증</title>
		<script type="text/javascript"> 
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.0.3/font-awesome.min.css',
			'css!${request.contextPath}/styles/common/common.ui.modaleffects.css',
			'${request.contextPath}/js/jquery/1.9.1/jquery.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			'${request.contextPath}/js/kendo/kendo.web.js',
			'${request.contextPath}/js/bootstrap/3.1.0/bootstrap.min.js',
			'${request.contextPath}/js/common/common.models.min.js',
			'${request.contextPath}/js/common/common.api.js',
			'${request.contextPath}/js/common/common.ui.min.js',
			'${request.contextPath}/js/common/common.classie.min.js',
			'${request.contextPath}/js/common/common.ui.modaleffects.js'],
			complete: function() {				
			
			<#if action.user.anonymous >			
				<#if action.findUser()?exists >
				var onetimeCode = "${action.oneTimeSecureCode}";
				if(typeof window.opener.handleCallbackResult != "undefined"){
					// 로그인 처리 수행을 부모 창에서 수행....
					common.api.signin({
						url : "${request.contextPath}/community/facebook-callback.do?output=json",
						onetime:  onetimeCode,
						success : function(response){
							//alert( kendo.stringify( response ) ) ;
						},
						fail : function(response){
							//alert( kendo.stringify( response ) ) ;
						}
					}); 			
					
				} else if(typeof window.opener.signupCallbackResult != "undefined"){
					// 회원가입 : 이미 연결이 존재함. 이사이트에서 보여줌.
					window.opener.signupCallbackResult("facebook", null);
				}else{
					// 기타
					common.api.signin({
						url : "${request.contextPath}/community/facebook-callback.do?output=json",
						onetime:  onetimeCode,
						success : function(response){
							window.opener.location.reload(true);
							window.close();	
						}
					}); 												
				}				
				<#else>			
				if(typeof window.opener.signupCallbackResult != "undefined"){					
					var profile = ${ HtmlUtils.objectToJson( action.getUserProfile() ) };
					window.opener.signupCallbackResult("facebook", {
						media: "facebook",
						id: profile.id,
						firstName: profile.firstName,
						lastName: profile.lastName,
						name : profile.name,
						email: profile.email,
						gender : profile.gender,
						locale : profile.locale,
						location : profile.location,
						timezone : profile.timezone,
						education: profile.education,
						work : profile.work
						
					});
				}	
				</#if>					
				//window.close();				
			<#else>					
			
				PieDraw();
				
				if( window.opener.location.href.indexOf("/secure/") > -1  ){
					// 관리자 모드..
					
				}else if( window.opener.location.href.indexOf("/accounts/") > -1  ){
					// 로그인/회원가입 모드..
					
				}else{			
					// 프로파일 수정 모드	
					var success = false;			
					var mySocialNetwork = new  SocialNetwork({});		
					mySocialNetwork.accessToken = "${action.accessToken!''}";
					mySocialNetwork.accessSecret = "${action.accessSecret!''}"
					mySocialNetwork.serviceProviderName = "facebook" 
					mySocialNetwork.username = "${action.getUserProfile().getId()}";
					
					$.ajax({
						type : 'POST',
						url : '${request.contextPath}/community/update-socialnetwork.do?output=json',
						data: { item: kendo.stringify(mySocialNetwork) },
						beforeSend: function(){							
						},
						success : function(response){
							if( response.error ){
							// 연결실패.
							} else {														
								success = true;
							}
						},
						error: handleKendoAjaxError
					});				
					
					if(typeof window.opener.handleSocialCallbackResult != "undefined"){
						window.opener.handleSocialCallbackResult(success);							
					}	
					// window.close();
				}					
			</#if>				
			}	
		}]);
		
		
		</script>		
	</head>
	<body class="color3">
		
						
		<div class="container">
			<div class="row">
				<div class="col-sm-12">						
					<!--<button class="md-trigger btn btn-danger" data-modal="modal-1">Fade in &amp; Scale</button>-->
				</div>
			</div>
		</div>
		
		<div class="md-modal md-effect-1" id="modal-1">
			<div class="md-content">
				<h3>Modal Dialog</h3>
				<div>
					<p>This is a modal window. You can do the following things with it:</p>
					<ul>
						<li><strong>Read:</strong> modal windows will probably tell you something important so don't forget to read what they say.</li>
						<li><strong>Look:</strong> a modal window enjoys a certain kind of attention; just look at it and appreciate its presence.</li>
						<li><strong>Close:</strong> click on the button below to close the modal.</li>
					</ul>
					<button class="md-close">확인</button>
				</div>
			</div>
		</div>
		<div class="md-overlay"></div><!-- the overlay element -->	
	</body>
</html>
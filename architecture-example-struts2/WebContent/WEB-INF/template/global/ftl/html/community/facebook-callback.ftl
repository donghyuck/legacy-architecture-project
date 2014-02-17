<#ftl encoding="UTF-8"/>
<html decorator="secure-metro">
	<head>
		<title>페이스북 인증</title>
		<script type="text/javascript"> 
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.0.3/font-awesome.min.css',
			'${request.contextPath}/js/jquery/1.9.1/jquery.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			'${request.contextPath}/js/kendo/kendo.web.js',
			'${request.contextPath}/js/bootstrap/3.1.0/bootstrap.min.js',
			'${request.contextPath}/js/common/common.models.min.js',
			'${request.contextPath}/js/common/common.api.js',
			'${request.contextPath}/js/common/common.ui.min.js',
			'${request.contextPath}/js/common/common.classie.min.js'],
			complete: function() {							
			
			<#if action.userProfile?exists >
			var onetimeCode = "${action.oneTimeSecureCode}";												
			<#else>	
			var onetimeCode = null;					
			</#if>				
			<#if action.user.anonymous >			
				<#if action.findUser()?exists >					
				if( typeof window.opener.handleCallbackResult == "function"){			
					window.opener.handleCallbackResult("facebook", onetimeCode, true);
					window.close();						
				} else {
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
				var template = kendo.template($('#account-not-found-alert-template').html());
				
				alert(${action.userProfile.username});
				
				$("#status").html(template({media: "facebook"}));				
				if(typeof window.opener.handleCallbackResult != "undefined"){		
						window.opener.handleCallbackResult("facebook", onetimeCode , false);
						window.close();							
				}else{
						window.opener.location.href = "${request.contextPath}/accounts/signup.do";
						window.close();
				}
				</#if>		
			<#else>					
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
					<div id="status"></div>
				</div>
			</div>
		</div>		
		<#include "/html/common/common-popup-templates.ftl" >		
	</body>
</html>
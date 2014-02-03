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
			'${request.contextPath}/js/bootstrap/3.0.3/bootstrap.min.js',
			'${request.contextPath}/js/pdfobject/pdfobject.js',
			'${request.contextPath}/js/common/common.models.min.js',
			'${request.contextPath}/js/common/common.ui.min.js'],
			complete: function() {				
			<#if action.user.anonymous >			
				<#if action.findUser()?exists >
				if(typeof window.opener.handleCallbackResult != "undefined"){
					window.opener.handleCallbackResult(${action.signIn()?string("true","false")});							
				} else if(typeof window.opener.signupCallbackResult != "undefined"){
					window.opener.signupCallbackResult("facebook", null);
				}else{
					//window.opener.location.reload(${action.signIn()?string("true","false")});
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
				window.close();				
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
					window.close();
				}					
			</#if>				
			}	
		}]);
			
		</script>		
	</head>
	<body>
	</body>
</html>
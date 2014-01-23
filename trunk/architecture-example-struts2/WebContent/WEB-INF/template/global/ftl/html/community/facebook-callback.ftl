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
				
				var mySocialNetwork = new  SocialNetwork({});
				mySocialNetwork.accessToken = "${action.accessToken!''}";
				mySocialNetwork.accessSecret = "${action.accessSecret!''}"
				mySocialNetwork.serviceProviderName = "facebook" 
				var success = false;								
								
				if( window.opener.location.href.indexOf("secure") > -1){
					alert("is admin company register social...");
				}
				
				var userProfile = ${ HtmlUtils.objectToJson(socialNetwork.socialServiceProvider.authenticate() ) };
				alert( kendo.stringify(userProfile) );
				
				<#if action.user.anonymous >
					<#assign foundUser  = action.findUser() >
					<#if foundUser >
					자동 로그인 후 메인 페이지 리프레쉬...
					window.opener.location.reload(true);
					<#else>
					회원가입
					 </#if>					
				<#else>				
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
				</#if>				
			}	
		}]);
		</script>		
	</head>
	<body>
	</body>
</html>
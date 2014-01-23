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
				
				var userProfile = ${ HtmlUtils.objectToJson(socialNetwork.socialServiceProvider.userProfile) };
				alert(  window.location.href +"," +kendo.stringify(userProfile) );
				<#if action.user.anonymous >
					// 회원 가입된 경우는 자동 로그인 그렇지 않는 경우는 회원 가입 .. 처리....					
				<#else>				
				$.ajax({
					type : 'POST',
					url : '${request.contextPath}/community/update-socialnetwork.do?output=json',
					data: { item: kendo.stringify(mySocialNetwork) },
					beforeSend: function(){							
					},
					success : function(response){
						alert( stringify( response ) );
						if( response.error ){
						// 연결실패.
						} else {														
							success = true;
						}
					},
					error: handleKendoAjaxError
				});				
				window.opener.handleSocialCallbackResult(success);		
				</#if>				
			}	
		}]);
		</script>		
	</head>
	<body>
	</body>
</html>
<#ftl encoding="UTF-8"/>
<html decorator="secure-metro">
	<head>
		<title>트위터 인증</title>
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
				mySocialNetwork.serviceProviderName = "twitter" 
				var success = false;				

				<#if action.user.anonymous >
					<#if action.findUser()?exists >		
						window.opener.location.reload(${action.signIn()});
						window.close();
					<#else>					
						var userProfile = ${ HtmlUtils.objectToJson( action.getUserProfile() ) };
						alert( kendo.stringify(userProfile) );				
					 </#if>					
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
				window.close();		
				</#if>
			}	
		}]);
		</script>		
	</head>
	<body>
	</body>
</html>
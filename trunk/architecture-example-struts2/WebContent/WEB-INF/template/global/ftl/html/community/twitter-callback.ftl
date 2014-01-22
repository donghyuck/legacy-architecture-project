<#ftl encoding="UTF-8"/>
<html decorator="secure-metro">
	<head>
		<title>페이스북 인증이 성공하였습니다.</title>
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
							alert( kendo.stringify(response.socialNewtork) );	
						}
					},
					error: handleKendoAjaxError
				});				
				window.opener.handleSocialCallbackResult(success);		
			}	
		}]);
		</script>		
	</head>
	<body>
	</body>
</html>
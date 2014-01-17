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
			'${request.contextPath}/js/common/common.models.js',
			'${request.contextPath}/js/common/common.ui.js'],
			complete: function() {
				
				var mySocialNetwork = new  SocialNetwork({});
				mySocialNetwork.accessToken = "${action.accessToken!''}";
				mySocialNetwork.accessSecret = "${action.accessSecret!''}"
				mySocialNetwork.serviceProviderName = "facebook" 
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
<div class="progress progress-striped active">
  <div class="progress-bar"  role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
    <span class="sr-only">100% Complete</span>
  </div>
</div>
	</body>
</html>
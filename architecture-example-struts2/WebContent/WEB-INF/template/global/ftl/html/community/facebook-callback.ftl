<#ftl encoding="UTF-8"/>
<html decorator="secure-metro">
	<head>
		<title>페이스북 인증이 성공하였습니다.</title>
		<script type="text/javascript"> 
        yepnope([{
            load: [ 
			'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',
			'${request.contextPath}/js/bootstrap/3.0.0/bootstrap.min.js',
       	    '${request.contextPath}/js/kendo/kendo.web.min.js',
       	    '${request.contextPath}/js/kendo/kendo.ko_KR.js',       	   
       	    '${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js', 
       	    '${request.contextPath}/js/common/common.models.js',
       	    '${request.contextPath}/js/common/common.apis.js',
       	    '${request.contextPath}/js/common/common.ui.js'], 
            complete: function() {               
							
			}	
		}]);
		</script>		
	</head>
	<body>	
		code : <br>
		${action.code!''}
		
		<br><br><br><br><br><br>
		token : <br>
		${action.token!''}
						
	</body>
</html>
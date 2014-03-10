<#ftl encoding="UTF-8"/>
<html decorator="secure-metro">
	<head>
		<title>페이스북 인증</title>
		<script type="text/javascript"> 
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.0.3/font-awesome.min.css',
			'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			'${request.contextPath}/js/kendo/kendo.web.js',
			'${request.contextPath}/js/bootstrap/3.1.0/bootstrap.min.js',
			'${request.contextPath}/js/common/common.models.min.js',
			'${request.contextPath}/js/common/common.api.js',
			'${request.contextPath}/js/common/common.ui.min.js',
			'${request.contextPath}/js/common/common.classie.min.js'],
			complete: function() {	
				<#if action.userProfile?exists >
					// 인증 성공..
					${ action.getOnetime() }
				<#else>	
					// 인증 실패..
				</#if>
			}	
		}]);
		
		
		</script>		
	</head>
	<body class="color7">						
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
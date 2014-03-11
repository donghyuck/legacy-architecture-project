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
					<#assign before_domain = ServletUtils.getDomainName(action.referer, false) >
					<#assign after_domain = ServletUtils.getDomainName( request.getRequestURL().toString() , false) >
					// 1. 인증 성공..
					var onetime = ${ action.getOnetime() }
					var domain0 = ${before_domain} 
					var domain2 =${after_domain} 
					
					<#if before_domain !=  after_domain >
						${response.sendRedirect("http://" + before_domain + "/community/facebook-callback.do?onetime=" + onetime  )}
					</#if>
					var domain2 = ${action.referer}
					<#if action.user.anonymous >			
						// 1-1. 로그인 필요. 
						${action.user.company.name}
					<#else>		
						// 1-1. 이미 로그인됨.
						
					</#if>
				<#else>	
					// 2. 인증 실패..
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
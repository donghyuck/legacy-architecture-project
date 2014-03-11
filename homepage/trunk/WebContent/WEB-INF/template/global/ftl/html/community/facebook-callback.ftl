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
					<#assign onetime = action.onetime >
					<#assign before_domain = ServletUtils.getDomainName(action.referer, false) >
					<#assign after_domain = ServletUtils.getDomainName( request.getRequestURL().toString() , false) >
					// 1. 인증 성공
					<#if before_domain !=  after_domain >
						${response.sendRedirect("http://" + before_domain + "/community/facebook-callback.do?onetime=" + onetime  )}						
					<#else>						
						<#if action.user.anonymous >
							// is anonymous
							var onetime = '${onetime}' ;
							<#if action.findUser()?exists >
							// is connected 						
							if(typeof window.opener.handleCallbackResult == "function"){		
								window.opener.handleCallbackResult("facebook", onetime , true);
								window.close();						
							}else if( typeof window.opener.signupCallbackResult == "function"){			
								window.opener.signupCallbackResult("facebook", onetime, true);
							}else{
							
							}		
							<#else>
							// is not connected 
							var template = kendo.template($('#account-not-found-alert-template').html());
							$("#status").html(template({
								media: "facebook",
								id : "${action.userProfile.name}",
								name: "${action.userProfile.name}"
							}));
							
							</#if>
						<#else>
						
						</#if>
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
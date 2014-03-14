<#ftl encoding="UTF-8"/>
<html decorator="secure-metro">
	<head>
		<title>트위터 인증</title>
		<#compress>
		<script type="text/javascript"> 
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.0.3/font-awesome.min.css',
			'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',
			'${request.contextPath}/js/jquery.cookie/jquery.cookie.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			'${request.contextPath}/js/kendo/kendo.web.js',
			'${request.contextPath}/js/bootstrap/3.1.0/bootstrap.min.js',
			'${request.contextPath}/js/common/common.models.js',
			'${request.contextPath}/js/common/common.api.js',
			'${request.contextPath}/js/common/common.ui.min.js'],
			complete: function() {
				
				document.domain = "${ServletUtils.getLocalHostAddr()}"; 
				
				<#if action.userProfile?exists >
					<#assign onetime = action.onetime >
					<#assign after_domain = ServletUtils.getDomainName( request.getRequestURL().toString() , false) >
					
					common.api.user.domain({	
						url : "http://${ServletUtils.getLocalHostAddr()}/community/my-domain.do?output=json",		
						success: function(data){
							alert( kendo.stringify( data ) ) ;
						}
					});
					
					alert( $.cookie('domainName', { expires: 1, path: '/community', domain: '${ServletUtils.getLocalHostAddr()}', secure: false }));
							
					//${after_domain}
										
				<#else>	
					// 2. 인증 실패..
				</#if>	
			}	
		}]);
		</script>		
		</#compress>
	</head>
	<body class="color2">						
		<div class="container">
			<div class="row">
				<div class="col-sm-12">						
					<div id="status"></div>
				</div>
			</div>
		</div>		
		<script type="text/x-kendo-template" id="account-not-found-alert-template">
			<div class="alert alert-info alert-dismissable">
				<img class="media-object img-circle" src="#=user.profileImageUrl#" alt="프로파일 이미지">
				<p>연결되지 않는 #=media# 계정입니다. 회원가입을 하시겠습니까?</p>
				<p> <button type="button" class="btn btn-primary"><i class="fa fa-check"></i> &nbsp; 예</button> <button type="button" class="btn btn-info" onclick="javascript: window.close();">아니오</button></p>	
			</div>
		</script>
	</body>	
</html>
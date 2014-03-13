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
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			'${request.contextPath}/js/kendo/kendo.web.js',
			'${request.contextPath}/js/bootstrap/3.1.0/bootstrap.min.js',
			'${request.contextPath}/js/common/common.models.js',
			'${request.contextPath}/js/common/common.api.js',
			'${request.contextPath}/js/common/common.ui.min.js'],
			complete: function() {
			
				${ action.referer }
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
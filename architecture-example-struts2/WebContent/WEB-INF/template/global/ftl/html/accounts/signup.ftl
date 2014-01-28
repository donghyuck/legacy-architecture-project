<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title>회원가입</title>
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.0.3/font-awesome.min.css',
			'${request.contextPath}/js/jquery/1.9.1/jquery.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo/kendo.ko_KR.js',			
			'${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js',		
			'${request.contextPath}/js/bootstrap/3.0.3/bootstrap.min.js',	
			'${request.contextPath}/js/common/common.cbpBGSlideshow.min.js',
			'${request.contextPath}/js/jquery.imagesloaded/imagesloaded.min.js',
			'${request.contextPath}/js/common/modernizr.custom.js',			
			'${request.contextPath}/js/common/common.models.min.js',
			'${request.contextPath}/js/common/common.ui.min.js'],
			complete: function() {
			
				// 1.  한글 지원을 위한 로케일 설정
				kendo.culture("ko-KR");
				      
				// START SCRIPT	
				cbpBGSlideshow.init();

				// END SCRIPT            
			}
		}]);	
		-->
		</script>		

		</style>   	
	</head>
	<body>
		<div class="main">
			<ul id="cbp-bislideshow" class="cbp-bislideshow">
				<li><img src="${request.contextPath}/community/download-image.do?imageId=175" alt="image01"/></li>
				<li><img src="${request.contextPath}/community/download-image.do?imageId=808" alt="image02"/></li>
				<li><img src="${request.contextPath}/community/download-image.do?imageId=810" alt="image03"/></li>
			</ul>
				<!-- 
				<div id="cbp-bicontrols" class="cbp-bicontrols">
					<span class="fa cbp-biprev"></span>
					<span class="fa cbp-bipause"></span>
					<span class="fa cbp-binext"></span>
				</div>
				 -->
		</div>
		<div  class="container" style="min-height:600px;">		
			<div class="row">
				<div class="col-lg-6"></div>
				<div class="col-lg-6"></div>
			</div>		
		</div>			
	</body>    
</html>
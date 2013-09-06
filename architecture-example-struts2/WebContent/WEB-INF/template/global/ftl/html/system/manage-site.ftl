<#ftl encoding="UTF-8"/>
<html decorator="secure-metro">
    <head>
        <title>시스템 정보</title>
        <script type="text/javascript">                
        
        yepnope([{
            load: [ 
			'${request.contextPath}/js/jquery/1.9.1/jquery.min.js',
			'${request.contextPath}/js/bootstrap/3.0.0/bootstrap.min.js',
       	    '${request.contextPath}/js/kendo/kendo.web.min.js',
       	    '${request.contextPath}/js/kendo/kendo.dataviz.min.js',
       	    '${request.contextPath}/js/kendo/kendo.ko_KR.js',       	   
       	    '${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js', 
       	    '${request.contextPath}/js/common/common.models.js',
       	    '${request.contextPath}/js/common/common.ui.js'],        	   
            complete: function() {               
            
                // 1.  한글 지원을 위한 로케일 설정
                kendo.culture("ko-KR");
		

				// ACCOUNTS LOAD		
				var currentUser = new User({});
				var accounts = $("#account-panel").kendoAccounts({
					visible : false,
					authenticate : function( e ){
						currentUser = e.token;						
					}
				});
				
				$('#myTab a').click(function (e) {
					e.preventDefault();
					if(  $(this).attr('href') == '#setup-info' ){
					
					}else if(  $(this).attr('href') == '#database-info' ){
										
					}
					$(this).tab('show');
				});
			}	
		}]);
		</script>
		<style>						
		</style>
	</head>
	<body>
		<!-- START HEADER -->
		<!-- END HEADER -->
		<!-- START MAIN CONTNET -->
		<div class="container layout">
			<div class="row blank-top-5">			
				<div class="col-12 col-lg-12">
					<ul class="nav nav-tabs" id="myTab">
					  <li class="active"><a href="#license-info">기본 설정 정보</a></li>
					  <li><a href="#setup-info">템플릿 관리</a></li>
					  <li><a href="#system-info">이미지 관리</a></li>
					  <li><a href="#database-info">첨부파일 관리</a></li>
					</ul>
					<div class="tab-content">
						<div class="tab-pane active" id="license-info">
							<div class="blank-space-5">
										<table class="table table-striped .table-hover license-details">
											<tbody>
												<tr>
													<th>도메인</th>
													<td><span data-bind="text: licenseId"></span></td>
												</tr>								
												<tr>
													<th>로고</th>
													<td><span data-bind="text: name"></span></td>
												</tr>			
												<tr>
													<th>메뉴</th>
													<td><span data-bind="text: version.versionString"></span></td>
												</tr>				
												<tr>
													<th>테마</th>
													<td><span class="label label-info"><span data-bind="text: edition"></span></span></td>
												</tr>																
												<tr>
													<th>타입</th>
													<td><span class="label label-danger"><span data-bind="text: type"></span></span></td>
												</tr>																	
												<tr>
													<th>발급일</th>
													<td><span data-bind="text: creationDate"></span></td>
												</tr>	
												<tr>
													<th>발급대상</th>
													<td><span data-bind="text: client.company"></span>(<span data-bind="text: client.name"></span>)</td>
												</tr>	
										 	</tbody>
										</table>
							</div>
						</div>
						<div class="tab-pane" id="setup-info">
							<div class="big-box">
								<div class="panel">
									<div id="setup-props-grid" ></div>
								</div>
							</div>		
						</div>
						<div class="tab-pane" id="system-info">
							<div class="big-box">
								<div class="panel">
								<table class="table table-striped .table-hover system-details">
									<tbody>
										<tr>
											<th>운영시스템</th>
											<td><span data-bind="text: operatingSystem"></span></td>
										</tr>
										<tr>
											<th>시스템 언어</th>
											<td><span data-bind="text: systemLanguage"></span></td>
										</tr>						
										<tr>
											<th>표준 시간대</th>
											<td><span data-bind="text: systemTimezone"></span></td>
										</tr>																			
										<tr>
											<th>시스템 날짜</th>
											<td><span data-bind="text: date"></span></td>
										</tr>
										<tr>
											<th>시스템 시간</th>
											<td><span data-bind="text: time"></span></td>
										</tr>							
										<tr>
											<th>임시 디렉터리</th>
											<td><span data-bind="text: tempDirectory"></span></td>
										</tr>									
										<tr>
											<th>파일 시스템 인코딩</th>
											<td><span data-bind="text: fileSystemEncoding"></span></td>
										</tr>
										<tr>
											<th>작업 디렉터리</th>
											<td><span data-bind="text: workingDirectory"></span></td>
										</tr>										
										<tr>
											<th>자바 실행환경</th>
											<td><span data-bind="text: javaRuntime"></span></td>
										</tr>			
										<tr>
											<th width="150">자바 벤더</th>
											<td><span data-bind="text: javaVendor"></span></td>
										</tr>		
										<tr>
											<th>자바 버전</th>
											<td><span data-bind="text: javaVersion"></span></td>
										</tr>			
										<tr>
											<th>가상머신</th>
											<td><span data-bind="text: javaVm"></span></td>
										</tr>	
										<tr>
											<th>가상머신 벤더</th>
											<td><span data-bind="text: jvmVendor"></span></td>
										</tr>
										<tr>
											<th>가상머신 버전</th>
											<td><span data-bind="text: jvmVersion"></span></td>
										</tr>																				
										<tr>
											<th>가상머신 구현 버전</th>
											<td><span data-bind="text: jvmImplementationVersion"></span></td>
										</tr>											
										<tr>
											<th>가상머신 옵션</th>
											<td><span data-bind="text: jvmInputArguments"></span></td>
										</tr>											
									</tbody>
								</table>	
								</div>
							</div>	
						</div>
						<div class="tab-pane" id="database-info">
							<div class="big-box">
								<div class="panel">
								<div id="database-info-grid" ></div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>				
		<div id="account-panel"></div>		
		<!-- END MAIN CONTNET -->
		<!-- START FOOTER -->
		<footer>  		
		</footer>
		<!-- END FOOTER -->
	</body>
</html>
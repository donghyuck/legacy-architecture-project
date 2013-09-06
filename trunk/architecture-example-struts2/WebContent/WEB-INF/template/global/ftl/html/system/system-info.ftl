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
				
				$.ajax({
					type : 'POST',
					url : '${request.contextPath}/secure/view-system-details.do?output=json',
					success : function( response ){
						var data = response ;	
						kendo.bind($(".system-details"), data.systemInfo );			
						kendo.bind($(".license-details"), data.licenseInfo );					
					},
					error:handleKendoAjaxError,
					dataType : "json"
				});				
																
				var dataSource = new kendo.data.DataSource({
					transport: {
						read: {
							url: '${request.contextPath}/secure/view-system-memory.do?output=json', // the remove service url
							type:'POST',
							dataType : 'json'
						}
					},
					error:handleKendoAjaxError,
					schema: { 
						data: function(response){
							return [ response ] ; 
						}
                    },
					change: function( e 	) { // subscribe to the CHANGE event of the data source
						var data = this.data()[0];						
						kendo.bind($(".memory-details"), data.memoryInfo );						
						if( ! $("#mem-gen-gauge").data("kendoRadialGauge") ){
							$("#mem-gen-gauge").kendoRadialGauge({
								theme: "white",
								pointer: {
									value: data.memoryInfo.usedHeap.megabytes,
									color: "#ea7001"									
								},
								scale: {
									majorUnit: 100,
									minorUnit: 10,
									startAngle: -30,
                            		endAngle: 210,
									max: data.memoryInfo.maxHeap.megabytes,
									ranges: [
										{
		                                    from:  ( data.memoryInfo.maxHeap.megabytes -  ( ( data.memoryInfo.maxHeap.megabytes / 10 ) * 2 ) ) ,
		                                    to:  ( data.memoryInfo.maxHeap.megabytes -  data.memoryInfo.maxHeap.megabytes / 10 ) ,
		                                    color: "#ff7a00"
		                                }, {
		                                    from: ( data.memoryInfo.maxHeap.megabytes -  data.memoryInfo.maxHeap.megabytes / 10 ) ,
		                                    to: data.memoryInfo.maxHeap.megabytes,
		                                    color: "#c20000"
		                                }
	                            	]			
								}
							});						
						}else{
							$("#mem-gen-gauge").data("kendoRadialGauge").value( data.memoryInfo.usedHeap.megabytes );
						}	
											
						if( ! $("#perm-gen-gauge").data("kendoRadialGauge") ){	
							$("#perm-gen-gauge").kendoRadialGauge({
								theme: "white",
								pointer: {
									value: data.memoryInfo.usedPermGen.megabytes,
									color: "#ea7001"		
								},
								scale: {
									majorUnit: 15,
									minorUnit: 5,
									startAngle: 90,
                            		endAngle: 240,
									max: data.memoryInfo.maxPermGen.megabytes,
									ranges: [
										{
		                                    from:  ( data.memoryInfo.maxPermGen.megabytes -  ( ( data.memoryInfo.maxPermGen.megabytes / 10 ) * 2 ) ) ,
		                                    to:  ( data.memoryInfo.maxPermGen.megabytes -  data.memoryInfo.maxPermGen.megabytes / 10 ) ,
		                                    color: "#ff7a00"
		                                }, {
		                                    from: ( data.memoryInfo.maxPermGen.megabytes -  data.memoryInfo.maxPermGen.megabytes / 10 ) ,
		                                    to: data.memoryInfo.maxPermGen.megabytes,
		                                    color: "#c20000"
		                                }
	                            	]								
								}
							});		
						}else{
							$("#perm-gen-gauge").data("kendoRadialGauge").value( data.memoryInfo.usedPermGen.megabytes );
						}	
					}
				});
				
				dataSource.read();		
								
				var timer = setInterval(function () {
					dataSource.read();
					//clearInterval(timer);
					}, 3000);					
				
				
				$('#myTab a').click(function (e) {
					e.preventDefault();
					if(  $(this).attr('href') == '#setup-info' ){
						if(!$("#setup-props-grid").data("kendoGrid")){
							$('#setup-props-grid').kendoGrid({
								     dataSource: {
										transport: { 
											read: { url:'${request.contextPath}/secure/view-system-setup-props.do?output=json', type:'post' }									
										 },
										 schema: {
					                            data: "setupApplicationProperties",
					                            model: Property
					                     },
					                     error:handleKendoAjaxError
								     },
								     columns: [
								         { title: "속성", field: "name" },
								         { title: "값",   field: "value" }
								     ],
									pageable: false,
									resizable: true,
									editable : false,
									scrollable: true,
									height: 200,	     
									change: function(e) {}
							});			
							$("#setup-props-grid").attr('style','');	    				
						}
					}else if(  $(this).attr('href') == '#database-info' ){
						if(! $("#database-info-grid").data("kendoGrid")){
								$('#database-info-grid').kendoGrid({
									dataSource: {
										transport: { 
											read: { url:'${request.contextPath}/secure/view-system-databases.do?output=json', type:'post' }
										},						
										batch: false, 
										schema: {
										data: "databaseInfos",
											model: DatabaseInfo
										},
										error:handleKendoAjaxError
									},
									columns: [
										{ title: "데이터베이스", field: "databaseVersion"},
										{ title: "JDBC 드라이버", field: "driverName + ' ' + driverVersion" },
										{ title: "ISOLATION", field: "isolationLevel", width:90 },
									],
									pageable: false,
									resizable: true,
									editable : false,
									scrollable: true,
									height: 200,
									change: function(e) {
									}
								});									
						}					
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
			<div class="row big-box">			
				<div class="col-6 col-lg-6">
					<div class="panel panel-success">
						<div class="panel-heading">Heap 메모리</div>
						<div id="mem-gen-gauge"></div>						
						<div class="panel-footer">
							<table class="table table-striped memory-details">
							 	<thead>
							 		<tr>
            							<th>Total Memory</th>
            							<th>Used Memory</th>
            						</tr>	
							 	</thead>
							 	<tbody>
							 		<tr>
										<td><span data-bind="text: maxHeap.megabytes"></span>MB</td>
										<td><span data-bind="text: usedHeap.megabytes"></span>MB</td>
							 		</tr>
							 	</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="col-6 col-lg-6">
					<div class="panel panel-success">
						<div class="panel-heading">PermGen 메모리</div>
						<div id="perm-gen-gauge"></div>						
						<div class="panel-footer">
							<table class="table table-striped memory-details">
							 	<thead>
							 		<tr>
            							<th>Total Memory</th>
            							<th>Used Memory</th>
            						</tr>	
							 	</thead>
							 	<tbody>
							 		<tr>
										<td><span data-bind="text: maxPermGen.megabytes"></span>MB</td>	
										<td><span data-bind="text: usedPermGen.megabytes"></span>MB</td>
							 		</tr>
							 	</tbody>
							</table>
						</div>
					</div>
				</div>				
			</div>			
			<div class="row big-box">			
				<div class="col-12 col-lg-12">
					<ul class="nav nav-tabs" id="myTab">
					  <li class="active"><a href="#license-info">라이센스 정보</a></li>
					  <li><a href="#setup-info">셋업 프로퍼티 정보</a></li>
					  <li><a href="#system-info">시스템 프로퍼티 정보</a></li>
					  <li><a href="#database-info">데이터베이스 정보</a></li>
					</ul>
					<div class="tab-content">
						<div class="tab-pane active" id="license-info">
							<div class="big-box">
								<div class="panel">
									<table class="table table-striped .table-hover license-details">
										<tbody>
											<tr>
												<th>발급 ID</th>
												<td><span data-bind="text: licenseId"></span></td>
											</tr>								
											<tr>
												<th>제품</th>
												<td><span data-bind="text: name"></span></td>
											</tr>			
											<tr>
												<th>버전</th>
												<td><span data-bind="text: version.versionString"></span></td>
											</tr>				
											<tr>
												<th>에디션</th>
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
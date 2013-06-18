<#ftl encoding="UTF-8"/>
<html decorator="secure">
    <head>
        <title>시스템 정보</title>
        <script type="text/javascript">                
        
        yepnope([{
            load: [ 
			'css!${request.contextPath}/styles/jquery.pageslide/jquery.pageslide.css',
			'${request.contextPath}/js/jquery/1.9.1/jquery.min.js',
			'${request.contextPath}/js/jquery.pageslide/jquery.pageslide.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
       	    '${request.contextPath}/js/kendo/kendo.web.min.js',
       	    '${request.contextPath}/js/kendo/kendo.dataviz.min.js',
       	    '${request.contextPath}/js/kendo/kendo.ko_KR.js',       	   
       	    '${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js',      
       	    '${request.contextPath}/js/common/common.ui.min.js',
      	    '${request.contextPath}/js/common/common.models.min.js' ],        	   
            complete: function() {               
            
                // 1.  한글 지원을 위한 로케일 설정
                kendo.culture("ko-KR");
		
				$("header .open").pageslide( { modal:true });
				
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
				
				$("#panelbar").kendoPanelBar({
					expandMode: "single",
					select: function( e ){			
						
						if(  $(e.item).hasClass('setup') ){

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
								     change: function(e) {  
								     }
								});			
								$("#setup-props-grid").attr('style','');	    				
							}
						}
														
						if(  $(e.item).hasClass('props') ){
							if(!$("#application-props-grid").data("kendoGrid")){
								$('#application-props-grid').kendoGrid({
								     dataSource: {
										transport: { 
											read: { url:'${request.contextPath}/secure/view-system-application-props.do?output=json', type:'post' }									
										 },
										 schema: {
					                            data: "applicationProperties",
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
								     change: function(e) {  
								     }
								});			
								$("#application-props-grid").attr('style','');	    				
							}
						}
						if(  $(e.item).hasClass('database') ){
							// DATABASE INFO
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
					}
				});
				$("#panelbar").show();
			}	
		}]);
		</script>
		<style>						
		</style>
	</head>
	<body>
		<!-- START HEADER -->
		<header>
			<div class="row full-width layout">
				<div class="large-12 columns">
					<div class="big-box topless bottomless">
					<h1><a class="open" href="${request.contextPath}/secure/get-system-menu.do">Menu</a>시스템 정보</h1>
					<h4>시스템 정보를 보여줍니다.</h4>
					</div>
				</div>
			</div>
		</header>
		<!-- END HEADER -->
		<!-- START MAIN CONTNET -->
		<section id="mainContent">
			<div class="row  layout">			
				<div class="large-12 columns" >
					<h4>메모리 사용현황</h4>
				</div>
			</div>			
			<div class="row layout">			
				<div class="large-6 columns" >
							<div id="mem-gen-gauge"></div>   
		                	<div id="mem-gen-info" class="k-content">
								<table class="tabular memory-details" width="100%">
									<thead>
										<tr>
											<th colspan="2">Memory</th>
										</tr>
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
				<div class="large-6 columns" >
					<div id="perm-gen-gauge"></div> 
	                		<div id="perm-gen-info" class="k-content">
								<table class="tabular memory-details" width="100%">
									<thead>
										<tr>
											<th colspan="2">PermGen Memory</th>
										</tr>
										<tr>
											<th>Total PermGen Memory</th>
											<th>Used PermGen Memory</th>
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
			<div class="row  layout">			
				<div class="large-12 columns" >
					<h4>시스템 정보</h4>
				</div>
			</div>						
			<div class="row  layout">			
				<div class="large-12 columns" >
						<ul id="panelbar"  style="display:none;">
							<li class="k-state-active">
								<span class="k-link k-state-selected">라이센스 정보</span>					
								<div class="k-content">
									<div class="k-grid k-widget">
										<div style="padding-right: 17px;" class="k-grid-header">
											<div class="k-grid-header-wrap">
												<table cellSpacing="0">
													<thead>
														<tr>
															<th class="k-header">속성</th>
															<th class="k-header">값</th>
														</tr>
													</thead>
												</table>
											</div>
										</div>
										<div style="height: 199px;" class="k-grid-content">
											<table style="height: auto;" class="license-details" cellSpacing="0">
												<tbody>
													<tr>
														<td>발급 ID</td>
														<td><span data-bind="text: licenseId"></span></td>
													</tr>								
													<tr class="k-alt">
														<td>제품</td>
														<td><span data-bind="text: name"></span></td>
													</tr>			
													<tr>
														<td>버전</td>
														<td><span data-bind="text: version.versionString"></span></td>
													</tr>				
													<tr class="k-alt">
														<td>에디션</td>
														<td><span data-bind="text: edition"></span></td>
													</tr>																
													<tr>
														<td>타입</td>
														<td><span data-bind="text: type"></span></td>
													</tr>																	
													<tr class="k-alt">
														<td>발급일</td>
														<td><span data-bind="text: creationDate"></span></td>
													</tr>	
													<tr>
														<td>발급대상</td>
														<td><span data-bind="text: client.company"></span>(<span data-bind="text: client.name"></span>)</td>
													</tr>											
												</tbody>
											</table>	
										</div>
									</div>	
								</div>
							</li>
			                <li class="setup">
			                    셋업 프로퍼티 정보
			                    <div class="content">
			                    	<div id="setup-props-grid" ></div>
			                    </div>
			                </li>	
			                <li class="props">
			                    응용프로그램 프로퍼티 정보
			                    <div class="content">
			                    	<div id="application-props-grid" ></div>                    	
			                    </div>
			                </li>				
							<li>
								시스템 정보
								<div class="content">
									<div class="k-grid k-widget">
										<div style="padding-right: 17px;" class="k-grid-header">
											<div class="k-grid-header-wrap">
												<table cellSpacing="0">
													<thead>
														<tr>
															<th class="k-header">속성</th>
															<th class="k-header">값</th>
														</tr>
													</thead>
												</table>
											</div>
										</div>
										<div style="height: 199px;" class="k-grid-content">
											<table style="height: auto;" class="system-details" cellSpacing="0">
												<tbody>
													<tr>
														<td>운영시스템</td>
														<td><span data-bind="text: operatingSystem"></span></td>
													</tr>
													<tr class="k-alt">
														<td>시스템 언어</td>
														<td><span data-bind="text: systemLanguage"></span></td>
													</tr>						
													<tr>
														<td>표준 시간대</td>
														<td><span data-bind="text: systemTimezone"></span></td>
													</tr>																			
													<tr class="k-alt">
														<td>시스템 날짜</td>
														<td><span data-bind="text: date"></span></td>
													</tr>
													<tr>
														<td>시스템 시간</td>
														<td><span data-bind="text: time"></span></td>
													</tr>							
													<tr class="k-alt">
														<td>임시 디렉터리</td>
														<td><span data-bind="text: tempDirectory"></span></td>
													</tr>									
													<tr>
														<td>파일 시스템 인코딩</td>
														<td><span data-bind="text: fileSystemEncoding"></span></td>
													</tr>
													<tr class="k-alt">
														<td>작업 디렉터리</td>
														<td><span data-bind="text: workingDirectory"></span></td>
													</tr>										
													<tr>
														<td>자바 실행환경</td>
														<td><span data-bind="text: javaRuntime"></span></td>
													</tr>			
													<tr class="k-alt">
														<td width="150">자바 벤더</td>
														<td><span data-bind="text: javaVendor"></span></td>
													</tr>		
													<tr>
														<td>자바 버전</td>
														<td><span data-bind="text: javaVersion"></span></td>
													</tr>			
													<tr class="k-alt">
														<td>가상머신</td>
														<td><span data-bind="text: javaVm"></span></td>
													</tr>	
													<tr>
														<td>가상머신 벤더</td>
														<td><span data-bind="text: jvmVendor"></span></td>
													</tr>
													<tr class="k-alt">
														<td>가상머신 버전</td>
														<td><span data-bind="text: jvmVersion"></span></td>
													</tr>																				
													<tr>
														<td>가상머신 구현 버전</td>
														<td><span data-bind="text: jvmImplementationVersion"></span></td>
													</tr>											
													<tr class="k-alt">
														<td>가상머신 옵션</td>
														<td><span data-bind="text: jvmInputArguments"></span></td>
													</tr>											
												</tbody>
											</table>	
										</div>
									</div>									
								</div>                    
							</li>
							<li class="database">
								데이터베이스 정보
								<div class="content">
									<div id="database-info-grid" ></div>
								</div>
							</li>
            			</ul>				
				</div>		
			</div>
		</div>				
		<!-- END MAIN CONTNET -->
		<!-- START FOOTER -->
		<footer>  		
		</footer>
		<!-- END FOOTER -->
	</body>
</html>
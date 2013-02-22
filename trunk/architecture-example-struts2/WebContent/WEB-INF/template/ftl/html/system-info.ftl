<#ftl encoding="UTF-8"/>
<html decorator="banded">
    <head>
        <title>사용자 관리</title>
        <script type="text/javascript">                
        yepnope([{
            load: [             
               'css!${request.contextPath}/styles/kendo/kendo.dataviz.min.css',
        	   '${request.contextPath}/js/kendo/kendo.web.min.js',
        	   'preload!${request.contextPath}/js/common.models.js',               
        	   'preload!${request.contextPath}/js/common.ui.js',
        	   '${request.contextPath}/js/kendo/kendo.dataviz.min.js'
        	   ],        	   
            complete: function() {   
            	$(document).foundationTabs();        
					var freeMem = [] ;
					var usedMem = [] ;
					var categoryList = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,17,18,19,20];
					var POINT = 20 ;
 					$("#chart").kendoChart({
                        theme: $(document).data("kendoSkin") || "default",
                        title: {
                            text: "Memory Usage"
                        },
                        legend: {
                            position: "bottom"
                        },
                        chartArea: {
                            background: ""
                        },
                        seriesDefaults: {
                            type: "line"
                        },
                        series: [{
                            name: "Used Memory",
                            data: usedMem
                        }, {
                            name: "Free Memory",
                            data: freeMem
                        }],
                        valueAxis: {
                            labels: {
                                format: "{0}MB"
                            }
                        },
                        categoryAxis: {
                            categories: categoryList
                        },
                        tooltip: {
                            visible: true,
                            format: "{0}MB"
                        }
                    });

                    setTimeout(function() {
                        // Initialize the chart with a delay to make sure
                        // the initial animation is visible        
                        
                                   
                    }, 200);
                    
                    setInterval(function(){
                        POINT = POINT + 1 ;
                        if( POINT > 60 )
                            POINT = 1 ;
                            
                        if( categoryList.length > 20 ){
                            categoryList.shift();
                       		categoryList.push(POINT);
                    	}

                   		var v = POINT * 19;
                   		freeMem.push(v ) ;
                   		usedMem.push(10*1.5 );
                   	
                   		if (freeMem.length > 20) {
                   		     freeMem.shift();    
                   		}
                   		
                   		if (usedMem.length > 20) {
                   		     usedMem.shift();    
                   		}
                   	                        
                        $("#chart").data("kendoChart").refresh();
                    }, 1000);
            	
            }	
        }]);      
        
        
        </script>
    </head>
    <body>  
	<!-- START MAIN HEADER  -->   
	<header>
		<div class="row">
			<div class="twelve columns">
				<h1>시스템 정보</h1>
				<h4>시스템 정보 모니터링 프로그램</h4>
			</div>
		</div>
	</header>
	<!-- END MAIN HEADER  -->   	            
	<!-- START MAIN CONTENT  -->   
    <section class="row" style="padding-top:10px;" >
    	<div class="three columns">
			<dl class="vertical tabs">
			  <dd class="active"><a href="#vertical1">메모리</a></dd>
			  <dd><a href="#vertical2">디스크</a></dd>
			  <dd><a href="#vertical3">로그</a></dd>
			</dl>
    	</div>	      
    	<div class="nine columns">    	
	    	<ul class="tabs-content">
			  <li id="vertical1Tab">			  
				<div id="example" class="k-content">
	            <div class="chart-wrapper">
	                <button id="playButton" class="k-button" onclick="playButtonClick()">모니터링 시작</button>
	                <div id="chart"></div>
	            </div>
				</div>

			  </li>
			  <li id="vertical2Tab">This is simple tab 2s content. Now you see it!</li>
			  <li id="vertical3Tab">This is simple tab 3s content.</li>
			</ul>    	
    	</div>	    
  
	</section>	
	<!-- END MAIN CONTENT  -->   
	<!-- START Breadcrumbs -->
	<section class="row">
	    <div class="twelve columns">
	        <hr style="margin-top:10px;margin-bottom:10px;" />
			<ul class="breadcrumbs">
			  <li><a href="${request.contextPath}/main.do">홈</a></li>
			  <li class="unavailable"><a href="#">시스템</a></li>
			  <li class="current"><a href="#">사용자 관리</a></li>
			</ul>
	    </div>
	</section>
	<!-- End Breadcrumbs -->
    </body>
</html>
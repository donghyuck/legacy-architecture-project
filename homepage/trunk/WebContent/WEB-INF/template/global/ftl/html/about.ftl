<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title>기업소개</title>
<#compress>			
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/common/common.timeline.css" />
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.0.3/font-awesome.min.css',
			'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo.extension/kendo.ko_KR.js',			
			'${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js',		
			'${request.contextPath}/js/bootstrap/3.1.0/bootstrap.min.js',
			'${request.contextPath}/js/common/common.models.js',			
			'${request.contextPath}/js/common/common.api.js',
			'${request.contextPath}/js/common/common.ui.js'],
			complete: function() {
			
				// 1.  한글 지원을 위한 로케일 설정
				kendo.culture("ko-KR");
				      
				// START SCRIPT	

				// ACCOUNTS LOAD	
				var currentUser = new User();			
				$("#account-navbar").extAccounts({
					externalLoginHost: "${ServletUtils.getLocalHostAddr()}",	
					<#if action.isAllowedSignIn() ||  !action.user.anonymous  >
					template : kendo.template($("#account-template").html()),
					</#if>
					authenticate : function( e ){
						e.token.copy(currentUser);
					}				
				});	
				
				
				// Start : Company Social Content 
				<#list action.connectedCompanySocialNetworks  as item >				
				<#assign stream_name = item.serviceProviderName + "_streams_" + item.socialAccountId  />	
				<#assign panel_element_id = "#" + item.serviceProviderName + "-panel-" + item.socialAccountId  />												
				var ${stream_name} = new MediaStreams(${ item.socialAccountId}, "${item.serviceProviderName}" );
				<#if  item.serviceProviderName == "twitter" >
				${stream_name}.setTemplate ( kendo.template($("#twitter-timeline-template").html()) );				
				<#elseif  item.serviceProviderName == "facebook" >
				${stream_name}.setTemplate( kendo.template($("#facebook-homefeed-template").html()) );
				</#if>
				${stream_name}.createDataSource({ 
					transport : {
						parameterMap : function ( options,  operation) {
							return { objectType : 1 };
						} 
					}
				});							
				${stream_name}.dataSource.read();				
				$( "${panel_element_id} .panel-header-actions a").each(function( index ) {
					var header_action = $(this);
					header_action.click(function (e){
						e.preventDefault();		
						var header_action_icon = header_action.find('span');
						if (header_action.text() == "Minimize" || header_action.text() == "Maximize"){
							$("${panel_element_id} .panel-body").toggleClass("hide");				
							if( header_action_icon.hasClass("k-i-maximize") ){
								header_action_icon.removeClass("k-i-maximize");
								header_action_icon.addClass("k-i-minimize");
							}else{
								header_action_icon.removeClass("k-i-minimize");
								header_action_icon.addClass("k-i-maximize");
							}
						} else if (header_action.text() == "Refresh"){								
							${stream_name}.dataSource.read();							
						} 
					});								
				});				
				</#list>	
				<#if !action.user.anonymous >							
				</#if>	
				
				$('#aboutTab').on( 'show.bs.tab', function (e) {
					//e.preventDefault();		
					var show_bs_tab = $(e.target);
					if( show_bs_tab.attr('href') == '#company-history' ){					
						
						 var dataSource = new kendo.data.DataSource({
			                transport: {
			                    read: {
			                        url: "/community/website-company-timeline.do?output=json",
			                        dataType: "json"
			                    }
			                },
			                schema : {
			                	data : "timelines",
			                	model : common.models.Timeline
			                },			                
			                requestStart: function() {
			                    kendo.ui.progress($("#company-history"), true);
			                },
			                requestEnd: function() {
			                    kendo.ui.progress($("#company-history"), false);
			                },
			                change: function() {
								//$("#company-history").html(kendo.render(template, this.view()));
			                }
			            });
						dataSource.read();            
					} 				
				});				
				$('#aboutTab a:first').tab('show');
				// END SCRIPT            
			}
		}]);	
		-->
		</script>		
		<style scoped="scoped">
		blockquote p {
			font-size: 15px;
		}

		.k-grid table tr.k-state-selected{
			background: #428bca;
			color: #ffffff; 
		}
		
		#announce-view .popover {
			position : relative;
			max-width : 500px;
		}
		.cbp_tmtimeline > li .cbp_tmicon { 
			position : relative;
		}				
		.timeline-v2 > li .cbp_tmlabel{ 
			color: #555;
		}
		
		</style>   	
</#compress>				
	</head>
	<body class="color0">
		<!-- START HEADER -->
		<#include "/html/common/common-homepage-menu.ftl" >	
		<#assign current_menu = action.getWebSiteMenu("USER_MENU", "MENU_1_1") />
		<header class="cloud">
			<div class="container">
				<div class="col-lg-12">	
					<h2>${ current_menu.title }</h2>
					<h4><i class="fa fa-quote-left"></i>&nbsp;${ current_menu.description ? replace ("{displayName}" , action.webSite.company.displayName ) }&nbsp;<i class="fa fa-quote-right"></i></h4>
				</div>
			</div>
		</header>			
		<!-- END HEADER -->					
		<!-- START MAIN CONTENT -->	
		<div class="container">	
			<div class="row">
				<div class="col-lg-3 visible-lg">
					<!-- start side menu -->		
					<div class="list-group">
					<#list current_menu.parent.components as item >
						<#if item.name ==  current_menu.name >
						<a href="${item.page}" class="list-group-item active">${ item.title } </a>
						<#else>
						<a href="${item.page}" class="list-group-item">${ item.title } </a>
						</#if>						
					</#list>										
					</div>	
					<!-- end side menu -->					
				</div>
				<div class="col-lg-9">
					<div class="row">
						<div class="col-sm-12">		
							<#if action.hasWebSitePage("pages.about.pageId") >							
							${ processedBodyText }
							</#if> 
							<!-- start of tabs -->					
							<div class="tab-v1">									
							<ul class="nav nav-tabs" id="aboutTab">
								<li><a href="#company-history" data-toggle="tab">연역</a></li>
								<li><a href="#company-logo" data-toggle="tab">로고</a></li>
								<li><a href="#company-media" data-toggle="tab">쇼셜미디어</a></li>								
							</ul>
							<!-- Tab panes -->
							<div class="tab-content">
								<div class="tab-pane fade" id="company-history">
								<!--
									<div class="panel panel-default">
										<div class="panel-body">
								-->		
<ul class="timeline-v2">
                    <li>
                        <time class="cbp_tmtime" datetime=""><span>4/1/08</span> <span>January</span></time>
                        <i class="cbp_tmicon rounded-x hidden-xs"></i>
                        <div class="cbp_tmlabel">
                            <h2>Our first step</h2>
                            <div class="row">
                                <div class="col-md-4">
                                    <img class="img-responsive" src="assets/img/job/high-rated-job-3.1.jpg" alt=""> 
                                    <div class="md-margin-bottom-20"></div>
                                </div>
                                <div class="col-md-8">    
                                    <p>Winter purslane courgette pumpkin quandong komatsuna fennel green bean cucumber watercress. Pea sprouts wattle seed rutabaga okra yarrow cress avocado grape.</p> 
                                    <p>Cabbage lentil cucumber chickpea sorrel gram garbanzo plantain lotus root bok choy squash cress potato.</p>
                                </div>
                            </div>        
                        </div>
                    </li>
                    <li>
                        <time class="cbp_tmtime" datetime=""><span>7/2/09</span> <span>February</span></time>
                        <i class="cbp_tmicon rounded-x hidden-xs"></i>
                        <div class="cbp_tmlabel">
                            <h2>First achievements</h2>
                            <p>Caulie dandelion maize lentil collard greens radish arugula sweet pepper water spinach kombu courgette lettuce. Celery coriander bitterleaf epazote radicchio shallot winter purslane collard greens spring onion squash lentil. Artichoke salad bamboo shoot black-eyed pea brussels sprout garlic kohlrabi.</p>
                            <div class="row">
                                <div class="col-sm-6">
                                    <ul class="list-unstyled">
                                        <li><i class="fa fa-check color-green"></i> Donec id elit non mi porta gravida</li>
                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
                                        <li><i class="fa fa-check color-green"></i> Responsive Bootstrap Template</li>
                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
                                    </ul>
                                </div>
                                <div class="col-sm-6">
                                    <ul class="list-unstyled">
                                        <li><i class="fa fa-check color-green"></i> Donec id elit non mi porta gravida</li>
                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
                                        <li><i class="fa fa-check color-green"></i> Responsive Bootstrap Template</li>
                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </li>
                    <li>
                        <time class="cbp_tmtime" datetime=""><span>28/6/12</span> <span>May</span></time>
                        <i class="cbp_tmicon rounded-x hidden-xs"></i>
                        <div class="cbp_tmlabel">
                            <h2>Difficulties</h2>
                            <p>Parsley amaranth tigernut silver beet maize fennel spinach. Ricebean black-eyed pea maize scallion green bean spinach cabbage jícama bell pepper carrot onion corn plantain garbanzo. Sierra leone bologi komatsuna celery peanut swiss chard silver beet squash dandelion maize chicory burdock tatsoi dulse radish wakame beetroot.</p>
                        </div>
                    </li>
                    <li>
                        <time class="cbp_tmtime" datetime=""><span>11/3/10</span> <span>March</span></time>
                        <i class="cbp_tmicon rounded-x hidden-xs"></i>
                        <div class="cbp_tmlabel">
                            <h2>Our Popularity</h2>
                            <p>Parsnip lotus root celery yarrow seakale tomato collard greens tigernut epazote ricebean melon tomatillo soybean chicory broccoli beet greens peanut salad. Lotus root burdock bell pepper chickweed shallot groundnut pea sprouts welsh onion wattle seed pea salsify turnip scallion peanut arugula bamboo shoot onion swiss chard.</p>

                            <div class="margin-bottom-20"></div>

                            <div class="row">
                                <div class="col-sm-6">
                                    <!-- Progress Bar Text -->
                                    <div class="progress-bar-text">
                                        <p class="text-left">HTML &amp; CSS</p>
                                        <p class="text-right">91%</p>
                                        <div class="progress progress-u progress-xs">
                                            <div class="progress-bar progress-bar-u progress-bar-u-success" role="progressbar" aria-valuenow="91" aria-valuemin="0" aria-valuemax="100" style="width: 91%">
                                            </div>
                                        </div>
                                    </div>    
                                    <!-- End Progress Bar Text -->
                                
                                    <!-- Progress Bar Text -->
                                    <div class="progress-bar-text">
                                        <p class="text-left">Web Animation</p>
                                        <p class="text-right">55%</p>
                                        <div class="progress progress-u progress-xs">
                                            <div class="progress-bar progress-bar-u progress-bar-u-info" role="progressbar" aria-valuenow="55" aria-valuemin="0" aria-valuemax="100" style="width: 55%">
                                            </div>
                                        </div>
                                    </div>    
                                    <!-- End Progress Bar Text -->
                                </div>    

                                <div class="col-sm-6">
                                    <!-- Progress Bar Text -->
                                    <div class="progress-bar-text">
                                        <p class="text-left">Web Design</p>
                                        <p class="text-right">67%</p>
                                        <div class="progress progress-u progress-xs">
                                            <div class="progress-bar progress-bar-u progress-bar-u-danger" role="progressbar" aria-valuenow="67" aria-valuemin="0" aria-valuemax="100" style="width: 67%">
                                            </div>
                                        </div>
                                    </div>    
                                    <!-- End Progress Bar Text -->

                                    <!-- Progress Bar Text -->
                                    <div class="progress-bar-text">
                                        <p class="text-left">PHP &amp; Javascript</p>
                                        <p class="text-right">73%</p>
                                        <div class="progress progress-u progress-xs">
                                            <div class="progress-bar progress-bar-u progress-bar-u-warning" role="progressbar" aria-valuenow="73" aria-valuemin="0" aria-valuemax="100" style="width: 73%">
                                            </div>
                                        </div>
                                    </div>    
                                    <!-- End Progress Bar Text -->
                                </div>   
                            </div>    
                        </div>
                    </li>
                    <li>
                        <time class="cbp_tmtime" datetime=""><span>2/4/11</span> <span>April</span></time>
                        <i class="cbp_tmicon rounded-x hidden-xs"></i>
                        <div class="cbp_tmlabel">
                            <h2>Back to the past</h2>
                            <p>Peanut gourd nori welsh onion rock melon mustard jícama. Desert raisin amaranth kombu aubergine kale seakale brussels sprout pea. Black-eyed pea celtuce bamboo shoot salad kohlrabi leek squash prairie turnip catsear rock melon chard taro broccoli turnip greens. Fennel quandong potato watercress ricebean swiss chard garbanzo. Endive daikon brussels sprout lotus root silver beet epazote melon shallot.</p>
                        </div>
                    </li>
                    <li>
                        <time class="cbp_tmtime" datetime=""><span>18/7/13</span> <span>June</span></time>
                        <i class="cbp_tmicon rounded-x hidden-xs"></i>
                        <div class="cbp_tmlabel">
                            <h2>Unify in recent years</h2>
                            <p>Caulie dandelion maize lentil collard greens radish arugula sweet pepper water spinach kombu courgette lettuce. Celery coriander bitterleaf epazote radicchio shallot winter purslane collard greens spring onion squash lentil. Artichoke salad bamboo shoot black-eyed pea brussels sprout garlic kohlrabi.</p>
                            <p>Bitterleaf celery coriander epazote radicchio shallot winter purslane collard greens spring onion squash lentil. Artichoke salad bamboo shoot black-eyed pea brussels sprout.</p>

                            <div class="margin-bottom-20"></div>

                            <div class="row">
                                <div class="col-md-4 col-xs-6">
                                    <img class="img-responsive md-margin-bottom-10" src="assets/img/job/high-rated-job-3.3.jpg" alt="">
                                </div>
                                <div class="col-md-4 col-xs-6">
                                    <img class="img-responsive md-margin-bottom-10" src="assets/img/job/high-rated-job-5.1.jpg" alt="">
                                </div>
                                <div class="col-md-4 col-xs-6">
                                    <img class="img-responsive md-margin-bottom-10" src="assets/img/job/high-rated-job-5.3.jpg" alt="">
                                </div>
                            </div>
                        </div>
                    </li>
                </ul>										
										<!--
											<ul class="cbp_tmtimeline">
												<li>
													<time class="cbp_tmtime" datetime="2013-04-10 18:30"><span>2012.04.03</span> <span>4월</span></time>
													<div class="cbp_tmicon"></div>
													<div class="cbp_tmlabel">
														<h2>Ricebean black-eyed pea</h2>
														<p>Winter purslane courgette pumpkin quandong komatsuna fennel green bean cucumber watercress. Pea sprouts wattle seed rutabaga okra yarrow cress avocado grape radish bush tomato ricebean black-eyed pea maize eggplant. Cabbage lentil cucumber chickpea sorrel gram garbanzo plantain lotus root bok choy squash cress potato summer purslane salsify fennel horseradish dulse. Winter purslane garbanzo artichoke broccoli lentil corn okra silver beet celery quandong. Plantain salad beetroot bunya nuts black-eyed pea collard greens radish water spinach gourd chicory prairie turnip avocado sierra leone bologi.</p>
													</div>
												</li>
												<li>
													<time class="cbp_tmtime" datetime="2013-04-11T12:04"><span>4/11/13</span> <span>12:04</span></time>
													<div class="cbp_tmicon cbp_tmicon-screen"></div>
													<div class="cbp_tmlabel">
														<h2>Greens radish arugula</h2>
														<p>Caulie dandelion maize lentil collard greens radish arugula sweet pepper water spinach kombu courgette lettuce. Celery coriander bitterleaf epazote radicchio shallot winter purslane collard greens spring onion squash lentil. Artichoke salad bamboo shoot black-eyed pea brussels sprout garlic kohlrabi.</p>
													</div>
												</li>
												<li>
													<time class="cbp_tmtime" datetime="2013-04-13 05:36"><span>4/13/13</span> <span>05:36</span></time>
													<div class="cbp_tmicon cbp_tmicon-mail"></div>
													<div class="cbp_tmlabel">
														<h2>Sprout garlic kohlrabi</h2>
														<p>Parsnip lotus root celery yarrow seakale tomato collard greens tigernut epazote ricebean melon tomatillo soybean chicory broccoli beet greens peanut salad. Lotus root burdock bell pepper chickweed shallot groundnut pea sprouts welsh onion wattle seed pea salsify turnip scallion peanut arugula bamboo shoot onion swiss chard. Avocado tomato peanut soko amaranth grape fennel chickweed mung bean soybean endive squash beet greens carrot chicory green bean. Tigernut dandelion sea lettuce garlic daikon courgette celery maize parsley komatsuna black-eyed pea bell pepper aubergine cauliflower zucchini. Quandong pea chickweed tomatillo quandong cauliflower spinach water spinach.</p>
													</div>
												</li>
												<li>
													<time class="cbp_tmtime" datetime="2013-04-15 13:15"><span>4/15/13</span> <span>13:15</span></time>
													<div class="cbp_tmicon cbp_tmicon-phone"></div>
													<div class="cbp_tmlabel">
														<h2>Watercress ricebean</h2>
														<p>Peanut gourd nori welsh onion rock melon mustard j챠cama. Desert raisin amaranth kombu aubergine kale seakale brussels sprout pea. Black-eyed pea celtuce bamboo shoot salad kohlrabi leek squash prairie turnip catsear rock melon chard taro broccoli turnip greens. Fennel quandong potato watercress ricebean swiss chard garbanzo. Endive daikon brussels sprout lotus root silver beet epazote melon shallot.</p>
													</div>
												</li>
												<li>
													<time class="cbp_tmtime" datetime="2013-04-16 21:30"><span>4/16/13</span> <span>21:30</span></time>
													<div class="cbp_tmicon cbp_tmicon-earth"></div>
													<div class="cbp_tmlabel">
														<h2>Courgette daikon</h2>
														<p>Parsley amaranth tigernut silver beet maize fennel spinach. Ricebean black-eyed pea maize scallion green bean spinach cabbage j챠cama bell pepper carrot onion corn plantain garbanzo. Sierra leone bologi komatsuna celery peanut swiss chard silver beet squash dandelion maize chicory burdock tatsoi dulse radish wakame beetroot.</p>
													</div>
												</li>
												<li>
													<time class="cbp_tmtime" datetime="2013-04-17 12:11"><span>4/17/13</span> <span>12:11</span></time>
													<div class="cbp_tmicon cbp_tmicon-screen"></div>
													<div class="cbp_tmlabel">
														<h2>Greens radish arugula</h2>
														<p>Caulie dandelion maize lentil collard greens radish arugula sweet pepper water spinach kombu courgette lettuce. Celery coriander bitterleaf epazote radicchio shallot winter purslane collard greens spring onion squash lentil. Artichoke salad bamboo shoot black-eyed pea brussels sprout garlic kohlrabi.</p>
													</div>
												</li>
												<li>
													<time class="cbp_tmtime" datetime="2013-04-18 09:56"><span>4/18/13</span> <span>09:56</span></time>
													<div class="cbp_tmicon cbp_tmicon-phone"></div>
													<div class="cbp_tmlabel">
														<h2>Sprout garlic kohlrabi</h2>
														<p>Parsnip lotus root celery yarrow seakale tomato collard greens tigernut epazote ricebean melon tomatillo soybean chicory broccoli beet greens peanut salad. Lotus root burdock bell pepper chickweed shallot groundnut pea sprouts welsh onion wattle seed pea salsify turnip scallion peanut arugula bamboo shoot onion swiss chard. Avocado tomato peanut soko amaranth grape fennel chickweed mung bean soybean endive squash beet greens carrot chicory green bean. Tigernut dandelion sea lettuce garlic daikon courgette celery maize parsley komatsuna black-eyed pea bell pepper aubergine cauliflower zucchini. Quandong pea chickweed tomatillo quandong cauliflower spinach water spinach.</p>
													</div>
												</li>
											</ul>		
											-->	
											<!--				
										</div>
									</div> -->
								</div>
								<div class="tab-pane fade" id="company-logo" style="min-height:300px;">
											<div class="page-header page-nounderline-header padding-left-10 ">
												<h5><i class="fa fa-info"></i> <small>로고 파일은 AI 와 JPG 형식을 제공됩니다.</small></h5>
											</div>
											<img src="${request.contextPath}/download/logo/company/${action.webSite.company.name}" class="img-rounded" />
											<p class="pull-right">
											<button type="button" class="btn btn-info btn-sm"><i class="fa fa-download"></i> 다운로드</button>	
											</p>
								</div>
								<div class="tab-pane fade" id="company-media" style="min-height:300px;">
									<div id="social-media-area" class="row">
										<#list action.connectedCompanySocialNetworks  as item >	
										<div class="col-sm-6">						
											<div class="blank-top-5"></div>
											<div id="${item.serviceProviderName}-panel-${item.socialAccountId}" class="panel panel-default panel-flat">
												<div class="panel-heading">
													<i class="fa fa-${item.serviceProviderName}"></i>&nbsp;${item.serviceProviderName}
													<div class="k-window-actions panel-header-actions">
														<a role="button" href="#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-refresh">Refresh</span></a>
														<a role="button" href="#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-minimize">Minimize</span></a>
														<a role="button" href="#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-maximize">Maximize</span></a>
														<a role="button" href="#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-close">Close</span></a>
													</div>
												</div>		
												<div class="panel-body scrollable" style="min-height:200px; max-height:500px;">
													<ul class="media-list">
														<div id="${item.serviceProviderName}-streams-${item.socialAccountId}">&nbsp;</div>
													</ul>
												</div>							
											</div>										
										</div>
										</#list>												
									</div>
								</div>
							</div>
							</div><!-- end of tabs -->
						</div>
					</div>					
				</div>				
			</div>
		</div>		
		<!-- END MAIN CONTENT -->	
 		<!-- START FOOTER -->
		<#include "/html/common/common-homepage-footer.ftl" >		
		<!-- END FOOTER -->	
		<!-- START TEMPLATE -->
		<#include "/html/common/common-homepage-templates.ftl" >		
		<!-- END TEMPLATE -->
	</body>    
</html>
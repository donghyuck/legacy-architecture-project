<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title><#if action.webSite ?? >${action.webSite.displayName }<#else>::</#if></title>
		<#compress>		
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/common/common.ui.portfolio-v1.css" -->
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/common/common.img-hover.css" />
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/bxslider/jquery.bxslider.css" />
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/common.themes/pomegranate.css" />
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',						
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo.extension/kendo.ko_KR.js',			
			'${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js',			
			'${request.contextPath}/js/bootstrap/3.1.0/bootstrap.min.js',
			
			'${request.contextPath}/js/bxslider/jquery.bxslider.min.js',
			'${request.contextPath}/js/common/common.modernizr.custom.min.js',
			
			/*
			'${request.contextPath}/js/common/common.classie.min.js',
			'${request.contextPath}/js/common/common.img-hover.js',
			*/
			'${request.contextPath}/js/common/common.models.js',
			'${request.contextPath}/js/common/common.api.js',
			'${request.contextPath}/js/common/common.ui.js'],
			complete: function() {

				// 1-1.  한글 지원을 위한 로케일 설정
				common.api.culture();
				// 1-2.  페이지 렌딩
				common.ui.landing();		
				
				// ACCOUNTS LOAD				
				var currentUser = new User();			
				$("#account-navbar").extAccounts({
					externalLoginHost: "${ServletUtils.getLocalHostAddr()}",	
					<#if WebSiteUtils.isAllowedSignIn(action.webSite) ||  !action.user.anonymous  >
					template : kendo.template($("#account-template").html()),
					</#if>
					authenticate : function( e ){
						e.token.copy(currentUser);
					}				
				});
				
				var announcement = new common.api.Announcement({pageSize: 3}); 		
				var forum = new common.api.Forum({forumId:1, pageSize: 3});
				 				
				var template = kendo.template('<li class="overflow-hidden"><i class="fa fa-check color-green"></i> #:subject#</li>');
				
				announcement.dataSource().bind('change', function(){
					$("#announce-view").html(kendo.render(template, this.view()))
				}).read();
				
				forum.dataSource().bind('change', function(){
					$("#news-view").html(kendo.render(template, this.view()))
				}).read();
				
								 
				/**
				$('.bxslider').bxSlider({
					maxSlides: 3,
					minSlides: 3,
					slideWidth: 100,
					slideMargin: 15,
					pager : false,
					onSliderLoad : function(){
						alert("000");
						$('#family-list').removeClass('hidden');
					}
				}); 
				*/ 
				<#if !action.user.anonymous ></#if>	
			}
		}]);	
			
		-->
		</script>		
		<style scoped="scoped">
		.copyright{
			background: #fff;
		}
		.bx-wrapper .bx-viewport {
			-moz-box-shadow: none;
			-webkit-box-shadow: none;
			box-shadow: none;
			border: none;
			left: -5px;
			background: transparent;
			-webkit-transform:none;
			-moz-transform: none;
			-ms-transform: none;
			-o-transform: none;
			transform: none;
		}
		/** 
		15 > 5 
		30 > 
		10 , -10
		*/
		#services-view .col-sm-6 {
			padding-right: 10px;
			padding-left: 0px;
		}
		
		#services-view .view {
			margin-bottom: 10px;
		}	
		
		#services-view .view-tenth img {
			/*left: 5px;
			margin-left: -5px;
			*/
		}	
		
		</style>   
		</#compress>			
	</head>
	<body>
		<div class="wrapper">	
		<!-- START HEADER -->
		<#include "/html/common/common-homepage-menu.ftl" >	
		<!-- END HEADER -->	
		<!-- START MAIN CONTENT -->			
		<!-- content
		================================================== -->
		<div style="background-color: #f5f5f5; min-height:300px;"> 
		<div class="container content">	
			<div class="row margin-bottom-20">
				<div class="col-lg-6">					
					<img src="${request.contextPath}/download/image/81/main_visual1.jpg" class="img-responsive"  alt="">
				</div>			
				<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
					<!-- product & service -->				
					<div id="services-view" class="row">
						<div class="col-sm-6">
							<div class="view view-tenth">
								<img class="img-responsive" src="${request.contextPath}/download/image/93/main_consulting.jpg" alt="" />
								<div class="mask">
									<div class="headline">HRD 컨설팅</div>
									<p class="text-left">기업의 인재육성전략 실현을 위한 HRD 통합 솔루션</p>
									<a href="portfolio_item.html" class="info">Read More</a>
									</div>                
								</div>
							<div class="view view-tenth">
								<img class="img-responsive" src="${request.contextPath}/download/image/94/main-ehrd.jpg" alt="" />
								<div class="mask">
									<div class="headline">인키움 e-HRD</div>
									<p class="text-left">조직의 인재양성전략 실현을 위한 최적의 e-HRD 솔루션</p>
									<a href="portfolio_item.html" class="info">더 알아보기</a>
								</div>                
							</div>
							<div class="view view-tenth">
								<img class="img-responsive" src="${request.contextPath}/download/image/97/main_icap.jpg" alt="" />
								<div class="mask">
									<div class="headline">iCAP</div>
									<p class="text-left">우리회사 인재양성 교육채널 온라인 통합 연수원</p>
									<a href="portfolio_item.html" class="info">더 알아보기</a>
								</div>                
							</div>	
							<div class="view view-tenth">
								<img class="img-responsive" src="${request.contextPath}/download/image/95/main-edu.jpg" alt="" />
								<div class="mask">
									<div class="headline">위탁교육</div>
									<p class="text-left">위탁교육</p>
									<a href="portfolio_item.html" class="info">더 알아보기</a>
								</div>                
							</div>																
						</div>
						<div class="col-sm-6">
							<div class="view view-tenth">
								<img class="img-responsive" src="${request.contextPath}/download/image/96/main-studymart.jpg" alt="" />
								<div class="mask">
									<div class="headline">스터디마트</div>
									<p class="text-left">우리회사 인재양성 교육채널 온라인 통합 연수원</p>
									<a href="portfolio_item.html" class="info">더 알아보기</a>
								</div>                
							</div>
							<div class="view view-tenth">
								<img class="img-responsive" src="${request.contextPath}/download/image/91/main_university.jpg" alt="" />
								<div class="mask">
									<div class="headline">대학사업</div>
									<p class="text-left">대학경쟁력 확보를 위한 토탈 서비스</p>
									<a href="portfolio_item.html" class="info">더 알아보기</a>
								</div>                
							</div>								
							<div class="view view-tenth">
								<div class="easy-bg-v2 rgba-red" style="z-index:1000">New</div>
								<img class="img-responsive" src="${request.contextPath}/download/image/92/main_vod.jpg" alt="" />
								<div class="mask">
									<div class="headline">직립보행</div>
									<p class="text-left">비즈니스 메너 동영상 컨텐츠</p>
									<a href="portfolio_item.html" class="info">더 알아보기</a>
								</div>                
							</div>								
						</div>
					</div><!--/row-->				
					<!-- end of product & service -->			
				</div>
			</div>
			<div class="row">				
				<div class="col-md-4">		
					<div class="easy-block-v1">
						<div class="easy-block-v1-badge rgba-purple">서비스 바로 가기</div>
						<div class="blank-space-25"></div>
						<div class="clearfix">
							<ul id="family-list" class="bxslider list-inline family-site">
								<li>
						                    <a target="_blank" href="http://www.astd.co.kr/">
						                    	<em class="overflow-hidden"><img src="${request.contextPath}/download/image/72/banner_astd.gif" alt="" /></em>
						                        <span>
						                            <strong>문의</strong>
						                            <small>02 2081 1092</small>
						                        </span>
						                    </a>
						                </li>
						                <li>
						                    <a target="_blank" href="http://www.icpi.co.kr">
						                    	<em class="overflow-hidden"><img src="${request.contextPath}/download/image/74/banner_icp.gif" alt="" /></em>
						                        <span>
						                           <strong>문의</strong>
						                            <small>02 2081 1026</small>
						                        </span>
						                    </a>
						                </li>
						                <li>
						                    <a href="#" onclick="window.open('http://www.wiznel.com/outer.do?method=getWithholdReceiptFrom','test','width=920 height=500 scrollbars=yes')">
						                    	<em class="overflow-hidden"><img src="${request.contextPath}/download/image/73/banner_receipt.gif" alt="" /></em>
						                        <span>
						                            <strong>문의</strong>
						                            <small>02 2081 1016</small>
						                        </span>
						                    </a>
								</li>
							</ul>
						</div>
					</div>				
				</div>
				
				<div class="col-md-4">
					<div class="easy-block-v1">		
	                     <div class="easy-block-v1-badge rgba-blue">공지</div>       			
	                     <div class="blank-space-25"></div>			
										<ul id="announce-view" class="list-unstyled" style="min-height:100px;">
	                                        <li class="overflow-hidden"><i class="fa fa-check color-green"></i> Donec id elit non mi porta gravida</li>
	                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
	                                        <li><i class="fa fa-check color-green"></i> Responsive Bootstrap Template</li>
	                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
	                                    </ul>    
						<a class="btn-u btn-u-xs pulse-shrink" href="${request.contextPath}/events.do">더보기</a>	
					</div>				
				</div>
				
				<div class="col-md-4">
					<div class="easy-block-v1">		
	                     <div class="easy-block-v1-badge rgba-blue">뉴스</div>       			
	                     <div class="blank-space-25"></div>		
										<ul id="news-view" class="list-unstyled" style="min-height:100px;">
	                                        <li><i class="fa fa-check color-green"></i> Donec id elit non mi porta gravida</li>
	                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
	                                        <li><i class="fa fa-check color-green"></i> Responsive Bootstrap Template</li>
	                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
	                                    </ul>        
						<a class="btn-u btn-u-xs" href="${request.contextPath}/press.do">더보기</a>	
					</div>
				</div>
							
			</div>					
		</div><!-- /content -->	
		</div>
		<!-- END MAIN CONTENT -->	
		</div><!-- /wrapper -->	
 		<!-- START FOOTER -->
		<#include "/html/common/common-homepage-footer.ftl" >		
		<!-- END FOOTER -->	
		
		<!-- START TEMPLATE -->
		<#include "/html/common/common-homepage-templates.ftl" >		
		<!-- END TEMPLATE -->
	</body>    
</html>
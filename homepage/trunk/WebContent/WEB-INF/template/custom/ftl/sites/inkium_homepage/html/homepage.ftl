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
	
			'${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js',			
			'${request.contextPath}/js/kendo.extension/kendo.ko_KR.js',					
			'${request.contextPath}/js/bootstrap/3.1.0/bootstrap.min.js',
			'${request.contextPath}/js/bxslider/jquery.bxslider.min.js',
			'${request.contextPath}/js/common/common.modernizr.custom.min.js',
			'${request.contextPath}/js/common/common.models.js',
			'${request.contextPath}/js/common/common.api.js',
			'${request.contextPath}/js/common/common.ui.js'],
			complete: function() {

				// 1-1.  한글 지원을 위한 로케일 설정
				common.api.culture();
				// 1-2.  페이지 렌딩
				common.ui.landing();		
				// 1-3. 페이지 이동 모듈
				common.api.teleportation();
				
				
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
				var template = kendo.template(
					'<li class="overflow-hidden"><i class="fa fa-check color-green"></i> ' +
					'#if ( typeof announceId === "number" ) { # <a href="javascript:teleportToNotice( #= announceId # );"> # } else { # <a href="javascript:teleportToNews(#= topicId #)"> # } #  #:subject#</a> ' +  
					'#if( typeof viewCnt === "number") {#<small class="hex">(#: viewCnt #)</small>#}#</li>'
				);				
				announcement.dataSource().bind('change', function(){
					$("#announce-view").html(kendo.render(template, this.view()))
				}).read();
				
				forum.dataSource().bind('change', function(){					
					$("#news-view").html(kendo.render(template, this.view()))
				}).read();
											 							 
				<#if !action.user.anonymous ></#if>	
			}
		}]);	
			
		function teleportToNotice (announceId){
			common.api.teleportation().teleport({
				action : '${request.contextPath}/events.do',
				announceId : announceId
			});
		}
		
		function teleportToNews (topicId){
			common.api.teleportation().teleport({
				action : '${request.contextPath}/press.do',
				topicId : topicId
			});
		}
		
			
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
		
		.family-site a img {
			width: 100px;
		}

		#services-view .col-sm-6 {
			padding-right: 11px;
			padding-left: 11px;
		}
		
		#services-view .view {
			margin-bottom: 10px;
		}	
		
		#services-view .view-tenth img {
			left: 10;
			margin-left: -10px;
		}
			
		#services-view .view p {
			padding: 20px 10px 20px 10px;
		}
		
		#services-view .view a.info {
		color: #fff;
		background: #000;
		margin-top: -20px;
		text-transform: none;
		}
			
		/*Summary-Content
		------------------------------------*/
		.summary-content .summaries li {
			padding: 6px 0;
			border-bottom: 1px dotted #ccc;
		}
		
		.summary-content .hex {
			color: #999
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
				<div class="col-lg-6 visible-lg">					
					<img src="${request.contextPath}/download/image/81/main_visual1.jpg" class="img-responsive"  alt="">
				</div>			
				<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
					<!-- product & service -->				
					<div id="services-view" class="row">
						<div class="col-sm-6">
							<div class="view view-tenth">
								<img class="img-responsive" src="${request.contextPath}/download/image/93/main_consulting.jpg" alt="" />
								<div class="mask">
									<p>기업의 인재육성전략 실현을 위한 HRD 토탈 솔루션 서비스</p>
									<a href="${request.contextPath}/page.do?name=PAGE-32" class="info">더 알아보기</a>
									</div>                
								</div>
							<div class="view view-tenth">
								<img class="img-responsive" src="${request.contextPath}/download/image/94/main-ehrd.jpg" alt="" />
								<div class="mask">
									<p>조직의 인재양성전략 실현을 위한 최적의 e-HRD 솔루션</p>
									<a href="${request.contextPath}/page.do?name=PAGE-21" class="info">더 알아보기</a>
								</div>                
							</div>
							<div class="view view-tenth">
								<img class="img-responsive" src="${request.contextPath}/download/image/97/main_icap.jpg" alt="" />
								<div class="mask">
									<p>역량기반의 인재양성 역량진단 서비스</p>									
									<a href="${request.contextPath}/page.do?name=PAGE-31" class="info">더 알아보기</a>
								</div>                
							</div>	
							<div class="view view-tenth">
								<img class="img-responsive" src="${request.contextPath}/download/image/95/main-edu.jpg" alt="" />
								<div class="mask">
									<p class="text-left">국내 및 해외 교육과정에 대한 기획, 실행, 평가 및 사후관리에 이르는 기업교육 전 부문 위탁 서비스</p>
									<a href="${request.contextPath}/page.do?name=PAGE-35" class="info">더 알아보기</a>
								</div>                
							</div>																
						</div>
						<div class="col-sm-6">
							<div class="view view-tenth">
								<img class="img-responsive" src="${request.contextPath}/download/image/96/main-studymart.jpg" alt="" />
								<div class="mask">
									<p>우리회사 인재양성 교육채널 온라인 통합 연수원</p>
									<a href="${request.contextPath}/page.do?name=PAGE-34" class="info">더 알아보기</a>
								</div>                
							</div>
							<div class="view view-tenth">
								<img class="img-responsive" src="${request.contextPath}/download/image/91/main_university.jpg" alt="" />
								<div class="mask">
									<p class="text-left">대학경쟁력 확보를 위한 토탈 서비스</p>
									<a href="${request.contextPath}/page.do?name=PAGE-36" class="info">더 알아보기</a>
								</div>                
							</div>								
							<div class="view view-tenth">
								<div class="easy-bg-v2 rgba-red" style="z-index:1000">New</div>
								<img class="img-responsive" src="${request.contextPath}/download/image/92/main_vod.jpg" alt="" />
								<div class="mask">
									<p>비즈니스 메너 동영상 컨텐츠</p>
									<a href="${request.contextPath}/page.do?name=PAGE-51" class="info">더 알아보기</a>
								</div>                
							</div>								
						</div>
					</div><!--/row-->				
					<!-- end of product & service -->			
				</div>
			</div>
			<hr/>
			<div class="row summary-content">				
				<div class="col-md-4">				
					<div class="easy-block-v2">
						<h5 class="heading-md"><strong>서비스 바로가기</strong></h5>
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
					<div class="easy-block-v2">		
						<h5 class="heading-md"><strong>공지&이벤트</strong></h5>
						<ul  id="announce-view" class="list-unstyled summaries" style="min-height:100px;">						
							<li><small class="hex">등록된 뉴스가 없습니다.</small></li>
						</ul>		
						<a class="btn-u btn-u-xs pulse-shrink" href="${request.contextPath}/events.do">더보기</a>	
					</div>				
				</div>				
				<div class="col-md-4">
					<div class="easy-block-v2">		
						<!--<div class="easy-bg-v2 rgba-blue" style="z-index:1000"></div>-->
						<h5 class="heading-md"><strong>뉴스</strong></h5>
						<ul id="news-view" class="list-unstyled summaries" style="min-height:100px;">
							<li><small class="hex">등록된 뉴스가 없습니다.</small></li>
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
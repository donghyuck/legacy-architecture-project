<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title><#if action.webSite ?? >${action.webSite.displayName }<#else>::</#if></title>
		<#compress>		
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/common/common.ui.portfolio-v1.css" />
		
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/common/common.img-hover.css" />
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
			
			'${request.contextPath}/js/common/common.modernizr.custom.min.js',
			/*
			'${request.contextPath}/js/common/common.classie.min.js',
			'${request.contextPath}/js/common/common.img-hover.js',
			*/
			'${request.contextPath}/js/common/common.models.js',
			'${request.contextPath}/js/common/common.api.js',
			'${request.contextPath}/js/common/common.ui.js'],
			complete: function() {
						
				// 1.  한글 지원을 위한 로케일 설정
				kendo.culture("ko-KR");				
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
				<#if !action.user.anonymous ></#if>	
			}
		}]);	
			
		-->
		</script>		
		<style scoped="scoped">
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
		
		<div class="container content">	
			<div class="row margin-bottom-20">
				<div class="visible-lg col-lg-6">
				메이이미지위치
				</div>			
				<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
					<!-- product & service -->				
					<div class="row">
						<div class="col-sm-6">
							<div class="view view-tenth">
								<img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/4.jpg" alt="" />
								<div class="mask">
									<div class="headline">HRD 컨설팅</div>
									<p class="text-left">기업의 인재육성전략 실현을 위한 HRD 통합 솔루션</p>
									<a href="portfolio_item.html" class="info">Read More</a>
									</div>                
								</div>
							</div>
						<div class="col-sm-6">
							<div class="view view-tenth">
								<img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/3.jpg" alt="" />
								<div class="mask">
									<div class="headline">스터디마트</div>
									<p class="text-left">우리회사 인재양성 교육채널 온라인 통합 연수원</p>
									<a href="portfolio_item.html" class="info">더 알아보기</a>
								</div>                
							</div>
						</div>
					</div>	
					<div class="row">	
						<div class="col-sm-6">
							<div class="view view-tenth">
								<img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/2.jpg" alt="" />
								<div class="mask">
									<div class="headline">인키움 e-HRD</div>
									<p class="text-left">조직의 인재양성전략 실현을 위한 최적의 e-HRD 솔루션</p>
									<a href="portfolio_item.html" class="info">더 알아보기</a>
								</div>                
							</div>
							<div class="view view-tenth">
								<img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/1.jpg" alt="" />
								<div class="mask">
									<div class="headline">iCAP</div>
									<p class="text-left">우리회사 인재양성 교육채널 온라인 통합 연수원</p>
									<a href="portfolio_item.html" class="info">더 알아보기</a>
								</div>                
							</div>	
							<div class="view view-tenth">
								<img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/5.jpg" alt="" />
								<div class="mask">
									<div class="headline">iCAP</div>
									<p class="text-left">우리회사 인재양성 교육채널 온라인 통합 연수원</p>
									<a href="portfolio_item.html" class="info">더 알아보기</a>
								</div>                
							</div>																		
						</div>
						<div class="col-sm-6">
							<div class="view view-tenth">
								<img class="img-responsive" src="${request.contextPath}/download/image/RfGo6CKryJR56ww6GREJ3W9Jt9uRB5UtlhhZChY4yyP76niwN2OQAqkhDUUgRnGu" alt="" />
								<div class="mask">
									<div class="headline">국내/국외 위탁교육</div>
									<p class="text-left">국내 및 해외 교육과정에 대한 기획, 실행, 평가 및 사후관리에 이르는 기업교육 전 부문 위탁수행 서비스</p>
									<a href="portfolio_item.html" class="info">더 알아보기</a>
								</div>                
							</div>	
							<div class="view view-tenth">
								<div class="easy-bg-v2 rgba-red" style="z-index:1000">New</div>
								<img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/6.jpg" alt="" />
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
						<div calss="col-sm-6">
							<ul class="list-inline our-clients" id="effect-2">
								<li>
									<figure>
										<img src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients2/ea-canada.png" alt="">
										<div class="img-hover">
											<h4>
												ASTD 참가
												<small>문의 02 2081 1092</small>
											</h4>
											
										</div>
									</figure>
								</li> 	
								<li>
									<figure>
										<img src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients2/ea-canada.png" alt="">
										<div class="img-hover">
											<h4>ICPI 자격증</h4>
											<p>문의 02 2081 1026</p>
										</div>
									</figure>
								</li>
								<li>
									<figure>
										<img src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients2/ea-canada.png" alt="">
										<div class="img-hover">
											<h4>원천징수 영수증</h4>
											<p>문의 02 2081 1016</p>
										</div>
									</figure>
								</li>								
							</ul>						
						</div>
						<div calss="col-sm-6">
						news
						</div>
					</div>

		</div><!-- /content -->	
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
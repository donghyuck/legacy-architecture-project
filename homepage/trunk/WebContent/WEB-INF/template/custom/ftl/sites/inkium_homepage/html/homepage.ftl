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
		.copyright{
			background: #fff;
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
		<div style="background-color: #f5f5f5;"> 
		<div class="container content">	
			<div class="row margin-bottom-20">
				<div class="col-lg-6">					
					<div class="row">
						<div class="visible-lg col-lg-12">
							<img src="${request.contextPath}/download/image/81/main_visual1.jpg" class="img-responsive"  alt="">
						</div>
					</div>
					<div class="row">
						<div class="col-lg-6">
							<div class="tag-box tag-box-v7 paddingless">				
								<p class="text-muted" style="padding:10px;"><small><i class="fa fa-info"></i> 서비스 바로 가기</small></p>												
								<ul class="list-inline">
									<li><img src="${request.contextPath}/download/image/72/banner_astd.gif" alt="" class="img-responsive" style="width:75px;"></li>
									<li><img src="${request.contextPath}/download/image/74/banner_icp.gif" alt="" class="img-responsive" style="width:75px;"></li>
									<li><img src="${request.contextPath}/download/image/73/banner_receipt.gif" alt="" class="img-responsive" style="width:75px;"></li>
								</ul>	
							</div>
						</div>
						<div class="col-lg-6">
							<div class="tag-box tag-box-v7">				
								<p class="text-muted"><small><i class="fa fa-info"></i> 뉴스</small></p>												
									<ul class="list-unstyled">
                                        <li><i class="fa fa-check color-green"></i> Donec id elit non mi porta gravida</li>
                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
                                        <li><i class="fa fa-check color-green"></i> Responsive Bootstrap Template</li>
                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
                                    </ul>
							</div>
						</div>
						<div class="col-lg-6">
							<div class="panel panel-default">
                            <div class="panel-body">
                            	<p class="text-muted" style="padding:10px;"><small><i class="fa fa-info"></i> 서비스 바로 가기</small></p>												
								<ul class="list-inline">
									<li><img src="${request.contextPath}/download/image/72/banner_astd.gif" alt="" class="img-responsive" style="width:75px;"></li>
									<li><img src="${request.contextPath}/download/image/74/banner_icp.gif" alt="" class="img-responsive" style="width:75px;"></li>
									<li><img src="${request.contextPath}/download/image/73/banner_receipt.gif" alt="" class="img-responsive" style="width:75px;"></li>
								</ul>	
                             </div>
                        	</div>
						</div>				
						<div class="col-lg-6">
							<div class="panel panel-default">
                            <div class="panel-body">
                            	<p class="text-muted" style="padding:10px;"><small><i class="fa fa-info"></i> 공지 | 뉴스 </small></p>												
									<ul class="list-unstyled">
                                        <li><i class="fa fa-check color-green"></i> Donec id elit non mi porta gravida</li>
                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
                                        <li><i class="fa fa-check color-green"></i> Responsive Bootstrap Template</li>
                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
                                    </ul>
                                    <hr/>
									<ul class="list-unstyled">
                                        <li><i class="fa fa-check color-green"></i> Donec id elit non mi porta gravida</li>
                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
                                        <li><i class="fa fa-check color-green"></i> Responsive Bootstrap Template</li>
                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
                                    </ul>
                             </div>
                        	</div>
						</div>			
						<div class="col-lg-6">
						<div class="easy-block-v1">
								<div class="easy-block-v1-badge rgba-blue">서비스 바로 가기</div>
                            	<p style="padding:20px;"></p>											
								<ul class="list-inline">
									<li><img src="${request.contextPath}/download/image/72/banner_astd.gif" alt="" class="img-responsive" style="width:75px;"></li>
									<li><img src="${request.contextPath}/download/image/74/banner_icp.gif" alt="" class="img-responsive" style="width:75px;"></li>
									<li><img src="${request.contextPath}/download/image/73/banner_receipt.gif" alt="" class="img-responsive" style="width:75px;"></li>
								</ul>							
						</div>
						</div>
						<div class="col-lg-6">
				<div class="easy-block-v1">		
                     <div class="easy-block-v1-badge rgba-blue">공지 | 뉴스</div>       			
                     <p style="padding:20px;"></p>				
									<ul class="list-unstyled">
                                        <li><i class="fa fa-check color-green"></i> Donec id elit non mi porta gravida</li>
                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
                                        <li><i class="fa fa-check color-green"></i> Responsive Bootstrap Template</li>
                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
                                    </ul>
                                    <hr/>
									<ul class="list-unstyled">
                                        <li><i class="fa fa-check color-green"></i> Donec id elit non mi porta gravida</li>
                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
                                        <li><i class="fa fa-check color-green"></i> Responsive Bootstrap Template</li>
                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
                                    </ul>                    		
				</div>
						</div>
						<div class="col-lg-6">
						
				<div class="easy-block-v2">
                    <div class="easy-bg-v2 rgba-red">New</div>
                    <img class="img-responsive" src="${request.contextPath}/download/image/96/main-studymart.jpg" alt="" />
                    <!--<h3>스터디마트</h3>
                    <ul class="list-unstyled">
                        <li><span class="color-green">콘텐츠:</span>3만개</li>
                        <li><span class="color-green">고객사:</span> 150 </li>
                    </ul>    -->
                    <p class="pull-right ">
                     <a class="btn-u btn-u-xs" href="#">더 알아보기</a>
                    </p>
                   
                </div>
						
						</div>				
					</div><!-- /row -->
				</div>			
				<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
					<!-- product & service -->				
					<div class="row">
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
							<div class="panel panel-default">
                            <div class="panel-body">
                            	<p class="text-muted" style="padding:10px;"><small><i class="fa fa-info"></i> 서비스 바로 가기</small></p>												
								<ul class="list-inline">
									<li><img src="${request.contextPath}/download/image/72/banner_astd.gif" alt="" class="img-responsive" style="width:75px;"></li>
									<li><img src="${request.contextPath}/download/image/74/banner_icp.gif" alt="" class="img-responsive" style="width:75px;"></li>
									<li><img src="${request.contextPath}/download/image/73/banner_receipt.gif" alt="" class="img-responsive" style="width:75px;"></li>
								</ul>	
                             </div>
                        	</div>							
						</div>
						<div class="col-md-8">
<div class="panel panel-default">
                            <div class="panel-body">
                            	<p class="text-muted" style="padding:10px;"><small><i class="fa fa-info"></i> 공지 | 뉴스 </small></p>												
									<ul class="list-unstyled">
                                        <li><i class="fa fa-check color-green"></i> Donec id elit non mi porta gravida</li>
                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
                                        <li><i class="fa fa-check color-green"></i> Responsive Bootstrap Template</li>
                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
                                    </ul>
                                    <hr/>
									<ul class="list-unstyled">
                                        <li><i class="fa fa-check color-green"></i> Donec id elit non mi porta gravida</li>
                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
                                        <li><i class="fa fa-check color-green"></i> Responsive Bootstrap Template</li>
                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
                                    </ul>
                             </div>
                        	</div>								
						</div>
					</div>					
					<div class="row">
						<div class="col-md-4">		
							<div class="tag-box tag-box-v7">							
<p class="text-muted"><small><i class="fa fa-info"></i> 서비스 바로가기</small></p>		
								<ul class="list-inline our-family" id="effect-2">
									<li>
										<figure>
											<img src="${request.contextPath}/download/image/72/banner_astd.gif" alt="">
											<div class="img-hover rounded">
												<figcaption>
													ASTD
													<a class="btn-u btn-u-red   btn-u-xs rounded" target="_blank" href="http://www.astd.co.kr/">바로가기</a>
												</figcaption>
												
											</div>
										</figure>
									</li> 	
									<li>
										<figure>
											<img src="${request.contextPath}/download/image/74/banner_icp.gif" alt="">
											<div class="img-hover rounded">
												<figcaption>
												ICP
												<a class="btn-u btn-u-red   btn-u-xs rounded" target="_blank" href="http://www.icpi.co.kr">바로가기</a>
												</figcaption>
												
											</div>
										</figure>
									</li>
									<li>
										<figure>
											<img src="${request.contextPath}/download/image/73/banner_receipt.gif" alt="">
											<div class="img-hover rounded">
												<figcaption>
												영수증
												<a class="btn-u btn-u-red  btn-u-xs rounded">출력</a>
												</figcaption>
												
											</div>
										</figure>
									</li>								
								</ul>		
							</div><!-- /quick link -->	
						</div>
						<div class="col-md-8">
							<div class="tag-box tag-box-v7">		
<p class="text-muted"><small><i class="fa fa-info"></i> 공지 | 뉴스 </small></p>	
									<ul class="list-unstyled">
                                        <li><i class="fa fa-check color-green"></i> Donec id elit non mi porta gravida</li>
                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
                                        <li><i class="fa fa-check color-green"></i> Responsive Bootstrap Template</li>
                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
                                    </ul>
                                    <hr/>
									<ul class="list-unstyled">
                                        <li><i class="fa fa-check color-green"></i> Donec id elit non mi porta gravida</li>
                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
                                        <li><i class="fa fa-check color-green"></i> Responsive Bootstrap Template</li>
                                        <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
                                    </ul>
							</div><!-- /news -->	
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
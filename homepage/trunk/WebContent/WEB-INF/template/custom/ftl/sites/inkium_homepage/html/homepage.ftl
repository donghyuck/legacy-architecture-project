<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title><#if action.webSite ?? >${action.webSite.displayName }<#else>::</#if></title>
		<#compress>		
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/common/common.ui.portfolio-v1.css" />
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
				fdasf
				</div>			
				<div class="col-lg-6 col-md-12 col-sm-12 col-xs-12">
					<!-- product & service -->				
					<div class="row">
						<div class="col-sm-6">
							<div class="view view-tenth">
								<img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/4.jpg" alt="" />
								<div class="mask">
									<div class="headline">HRD 컨설팅</div>
									<p class="text-left">HRD 관련 분야의 토탈 솔루션을 제공하여 유기적으로 시스템 연계까지 구현하는 최적의 컨설팅 솔루션..</p>
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
            <div class="col-sm-6">
                <div class="view view-tenth">
                    <img class="img-responsive" src="${request.contextPath}/download/image/RfGo6CKryJR56ww6GREJ3W9Jt9uRB5UtlhhZChY4yyP76niwN2OQAqkhDUUgRnGu" alt="" />
                    <div class="mask">
                        <div class="headline">인키움 HRD컨설팅</div>
                        <p class="text-left">다양한 업종과 규모의 기업에서 수행한 프로젝트 경험 및 HRD 전문지식을 보유한 전문 컨설턴트의 역량을 바탕으로 각 프로세스별 방법론을 보유하여 HRD 관련 분야의 토탈 솔루션을 제공하여 유기적으로 시스템 연계까지 구현하는 최적의 컨설팅 솔루션..</p>
                        <a href="portfolio_item.html" class="info">더 알아보기</a>
                    </div>                
                </div>
            </div>
            <div class="col-sm-6 ">
                <div class="view view-tenth" >
                    <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/2.jpg" alt=""  style="min-height:400px;"/>
                    <div class="mask">
                        <h2>3</h2>
                        <p>At vero eos et accusamus et iusto odio dignissimos dolores et quas molestias excepturi sint occaecati cupiditate non provident.</p>
                        <a href="portfolio_item.html" class="info">Read More</a>
                    </div>                
                </div>
            </div>
            <div class="col-sm-6">
                <div class="view view-tenth">
                    <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/3.jpg" alt="" />
                    <div class="mask">
                        <h2>4</h2>
                        <p>At vero eos et accusamus et iusto odio dignissimos dolores et quas molestias excepturi sint occaecati cupiditate non provident.</p>
                        <a href="portfolio_item.html" class="info">Read More</a>
                    </div>                
                </div>
            </div>   
            <div class="col-sm-6">
                <div class="view view-tenth">
                    <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/3.jpg" alt="" />
                    <div class="mask">
                        <h2>5</h2>
                        <p>At vero eos et accusamus et iusto odio dignissimos dolores et quas molestias excepturi sint occaecati cupiditate non provident.</p>
                        <a href="portfolio_item.html" class="info">Read More</a>
                    </div>                
                </div>
            </div>                                    
        </div><!--/row-->

        <div class="row"> 
            <div class="col-sm-6">
                <div class="view view-tenth">
                    <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/3.jpg" alt="" />
                    <div class="mask">
                        <h2>Portfolio Item</h2>
                        <p>At vero eos et accusamus et iusto odio dignissimos dolores et quas molestias excepturi sint occaecati cupiditate non provident.</p>
                        <a href="portfolio_item.html" class="info">Read More</a>
                    </div>                
                </div>
            </div>
            <div class="col-sm-6">
                <div class="view view-tenth">
                    <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/4.jpg" alt="" />
                    <div class="mask">
                        <h2>Portfolio Item</h2>
                        <p>At vero eos et accusamus et iusto odio dignissimos dolores et quas molestias excepturi sint occaecati cupiditate non provident.</p>
                        <a href="portfolio_item.html" class="info">Read More</a>
                    </div>                
                </div>
            </div>
        </div><!--/row-->				
<!-- end of product & service -->					
				</div>
			</div>
    	
    	<!-- Recent Works -->
        <div class="row margin-bottom-20">
            <div class="col-md-3 col-sm-6">
                <div class="thumbnails thumbnail-style thumbnail-kenburn">
                	<div class="thumbnail-img">
                        <div class="overflow-hidden">
                            <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/2.jpg" alt="">
                        </div>
                        <a class="btn-more hover-effect" href="#">read more +</a>					
                    </div>
                    <div class="caption">
                        <h3><a class="hover-effect" href="#">Project One</a></h3>
                        <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, justo sit amet risus etiam porta sem.</p>
                    </div>
                </div>
            </div>
            <div class="col-md-3 col-sm-6">
                <div class="thumbnails thumbnail-style thumbnail-kenburn">
                    <div class="thumbnail-img">
                        <div class="overflow-hidden">
                            <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/3.jpg" alt="">
                        </div>
                        <a class="btn-more hover-effect" href="#">read more +</a>                   
                    </div>
                    <div class="caption">
                        <h3><a class="hover-effect" href="#">Project Two</a></h3>
                        <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, justo sit amet risus etiam porta sem.</p>
                    </div>
                </div>
            </div>
            <div class="col-md-3 col-sm-6">
                <div class="thumbnails thumbnail-style thumbnail-kenburn">
                    <div class="thumbnail-img">
                        <div class="overflow-hidden">
                            <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/9.jpg" alt="">
                        </div>
                        <a class="btn-more hover-effect" href="#">read more +</a>                   
                    </div>
                    <div class="caption">
                        <h3><a class="hover-effect" href="#">Project Three</a></h3>
                        <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, justo sit amet risus etiam porta sem.</p>
                    </div>
                </div>
            </div>
            <div class="col-md-3 col-sm-6">
                <div class="thumbnails thumbnail-style thumbnail-kenburn">
                    <div class="thumbnail-img">
                        <div class="overflow-hidden">
                            <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/10.jpg" alt="">
                        </div>
                        <a class="btn-more hover-effect" href="#">read more +</a>                   
                    </div>
                    <div class="caption">
                        <h3><a class="hover-effect" href="#">Project Four</a></h3>
                        <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, justo sit amet risus etiam porta sem.</p>
                    </div>
                </div>
            </div>
        </div>
    	<!-- End Recent Works -->

    	<!-- Info Blokcs -->
    	<div class="row margin-bottom-30">
        	<!-- Welcome Block -->
    		<div class="col-md-8 md-margin-bottom-40">
    			<div class="headline"><h2>Welcome To Unify</h2></div>
                <div class="row">
                    <div class="col-sm-4">
                        <img class="img-responsive margin-bottom-20" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/6.jpg" alt="">
                    </div>
                    <div class="col-sm-8">
                        <p>Unify is an incredibly beautiful responsive Bootstrap Template for corporate and creative professionals. It works on all major web browsers, tablets and phone.</p>
                        <ul class="list-unstyled margin-bottom-20">
                            <li><i class="fa fa-check color-green"></i> Donec id elit non mi porta gravida</li>
                            <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
                            <li><i class="fa fa-check color-green"></i> Responsive Bootstrap Template</li>
                            <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
                        </ul>                    
                    </div>
                </div>

                <blockquote class="hero-unify">
                    <p>Award winning digital agency. We bring a personal and effective approach to every project we work on, which is why. Unify is an incredibly beautiful responsive Bootstrap Template for corporate professionals.</p>
                    <small>홍</small>
                </blockquote>
            </div><!--/col-md-8-->        

            <!-- Latest Shots -->
            <div class="col-md-4">
    			<div class="headline"><h2>Latest Shots</h2></div>
    			<div id="myCarousel" class="carousel slide carousel-v1">
                    <div class="carousel-inner">
                        <div class="item active">
                            <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/5.jpg" alt="">
                            <div class="carousel-caption">
                                <p>Facilisis odio, dapibus ac justo acilisis gestinas.</p>
                            </div>
                        </div>
                        <div class="item">
                            <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/4.jpg" alt="">
                            <div class="carousel-caption">
                                <p>Cras justo odio, dapibus ac facilisis into egestas.</p>
                            </div>
                            </div>
                        <div class="item">
                            <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/3.jpg" alt="">
                            <div class="carousel-caption">
                                <p>Justo cras odio apibus ac afilisis lingestas de.</p>
                            </div>
                        </div>
                    </div>
                    
                    <div class="carousel-arrow">
                        <a class="left carousel-control" href="#myCarousel" data-slide="prev">
                            <i class="fa fa-angle-left"></i>
                        </a>
                        <a class="right carousel-control" href="#myCarousel" data-slide="next">
                            <i class="fa fa-angle-right"></i>
                        </a>
                    </div>
    			</div>
            </div><!--/col-md-4-->
    	</div>	
    	<!-- End Info Blokcs -->

        <!-- Our Clients -->

        <!-- End Our Clients -->
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
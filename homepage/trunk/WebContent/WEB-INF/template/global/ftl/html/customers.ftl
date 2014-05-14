<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title>기업소개</title>
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.0.3/font-awesome.min.css',
			'css!${request.contextPath}/styles/owl.carousel/owl.carousel.css',
			'css!${request.contextPath}/styles/owl.carousel/owl.theme.css',
			'css!${request.contextPath}/styles/common/common.ui.carousel.css',
			'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo.extension/kendo.ko_KR.js',			
			'${request.contextPath}/js/bootstrap/3.1.0/bootstrap.min.js',
			'${request.contextPath}/js/owl.carousel/owl.carousel.min.js',			
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
				
				<#if !action.user.anonymous >				
				
				</#if>	
				 common.ui.initializeOwlCarousel();
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
						
		</style>   	
	</head>
	<body class="color0">
		<!-- START HEADER -->
		<#include "/html/common/common-homepage-menu.ftl" >	
		<#assign current_menu = action.getWebSiteMenu("USER_MENU", "MENU_1_4") />
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
		<div class="container layout">	
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
					<div class="page-header text-primary">
						<h4><i class="fa fa-angle-right"></i> 주요 고객사<small> 공공.금융기관, 정보통신, 서비스/유통, 제조/건설등 국내 여러 기업이 인키움의 제품과 서비스를 사용하고 있습니다.</small></h4>
					</div>					
					<div class="owl-carousel-v1 margin-bottom-50">
						<h3 class="heading-md">고객사</h3>
						<p>공공.금융기관, 정보통신, 서비스/유통, 제조/건설등 국내 여러 기업이 인키움의 제품과 서비스를 사용하고 있습니다.</p>
						<div class="owl-navigation">
							<div class="customNavigation">
								<a class="owl-btn prev-v1"><i class="fa fa-angle-left"></i></a>
								<a class="owl-btn next-v1"><i class="fa fa-angle-right"></i></a>
							</div>
						</div><!--/navigation-->					
						<div class="owl-slider">
							<div class="item">
								<img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients2/emirates.png" alt="">
							</div>
							<div class="item">
								<img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients2/marianos.png" alt="">
							</div>
							<div class="item">
								<img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients2/ucweb.png" alt="">
							</div>
							<div class="item">
								<img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients2/national-geographic.png" alt="">
							</div>
							<div class="item">
								<img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients2/much-more.png" alt="">
							</div>
							<div class="item">
								<img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients2/baderbrau.png" alt="">
							</div>
							<div class="item">
								<img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients2/co-wheels.png" alt="">
							</div>
							<div class="item">
								<img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients2/bellfield.png" alt="">
							</div>
							<div class="item">
								<img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients2/fddw.png" alt="">
							</div>
						</div>	
					</div>
					<div class="margin-bottom-60"><hr></div>					
					<div class="owl-carousel-v2 owl-carousel-style-v1 margin-bottom-50">
                    <h3 class="heading-md">제휴 파트너 </h3>
                    <p>고객에게 더나은 가치를 제공하고 위하여 여러 국내 업체들과 제휴 관계에 있습니다.<br>

                    <div class="owl-slider-v2">
                        <div class="item">
                            <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients2/baderbrau.png" alt="">
                        </div>
                        <div class="item">
                            <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients2/fddw.png" alt="">
                        </div>
                        <div class="item">
                            <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients2/inspiring.png" alt="">
                        </div>
                        <div class="item">
                            <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients2/ea-canada.png" alt="">
                        </div>
                        <div class="item">
                            <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients2/marianos.png" alt="">
                        </div>
                        <div class="item">
                            <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients2/bellfield.png" alt="">
                        </div>
                        <div class="item">
                            <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients2/national-geographic.png" alt="">
                        </div>
                        <div class="item">
                            <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients2/emirates.png" alt="">
                        </div>
                        <div class="item">
                            <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4assets/img/clients2/co-wheels.png" alt="">
                        </div>
                    </div>

                    <div class="owl-navigation">
                        <div class="customNavigation">
                            <a class="owl-btn prev-v2"><i class="fa fa-angle-left"></i></a>
                            <a class="owl-btn next-v2"><i class="fa fa-angle-right"></i></a>
                        </div>
                    </div><!--/navigation-->              
                </div>    
                <!-- End Owl Carousel v2 --> 					
						
				</div>				
			</div>
		</div>									 
		<div class="container layout">						
				<div class="row">
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
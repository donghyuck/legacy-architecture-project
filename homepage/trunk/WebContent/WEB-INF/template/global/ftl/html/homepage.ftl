<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title><#if action.webSite ?? >${action.webSite.displayName }<#else>::</#if></title>
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/layer-slider/layerslider.css" />
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/layer-slider/skins/fullwidth/skin.css" />
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.0.3/font-awesome.min.css',
			'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			
			'${request.contextPath}/js/jquery.plugins/easing/jquery.easing.1.3.js',
			'${request.contextPath}/js/layer-slider/jquery.transit.modified.js',
			'${request.contextPath}/js/layer-slider/layerslider.transitions.js',
			'${request.contextPath}/js/layer-slider/layerslider.kreaturamedia.jquery.js',
			
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
			
				$('#layerslider').layerSlider({
					skinsPath : '/styles/layer-slider/skins/',
					skin : 'fullwidth',
					thumbnailNavigation : 'hover',
					hoverPrevNext : true,
					responsive : true,
					responsiveUnder : 960,
					sublayerContainer : 960
		        });
				
				<#if !action.user.anonymous ></#if>	
			}
		}]);	
			
		-->
		</script>		
		<style scoped="scoped">
		.da-slider{
			background: transparent url(${request.contextPath}/images/slider-bg.jpg) repeat 0% 0%;
		}
		</style>   	
	</head>
	<body>
		<div class="wrapper">	
		<!-- START HEADER -->
		<#include "/html/common/common-homepage-menu.ftl" >	
		<!-- END HEADER -->	
		<!-- START MAIN CONTENT -->	
		
	<!-- Slider
	================================================== -->
	<div class="layer_slider">
        <div id="layerslider-container-fw">        
            <div id="layerslider" style="width: 100%; height: 500px; margin: 0px auto; visibility: visible;" class="ls-container ls-fullwidth"><div class="ls-webkit-hack"></div>
                <!--First Slide-->
                <div class="ls-inner" style="background-color: transparent; width: 1104px; height: 500px;"><div class="ls-lt-container ls-overflow-hidden" style="width: 1104px; height: 500px;"><div class="ls-curtiles" style="overflow: hidden;"><div class="ls-lt-tile" style="width: 1104px; height: 71px; overflow: hidden;"><div class="ls-curtile" style="top: 0px; left: -1104px; -webkit-transform: rotate(0deg) scale(1, 1); opacity: 1;"><img src="assets/img/sliders/layer/bg2.jpg" style="width: 1920px; height: 500px; margin-left: -408px; margin-top: 0px;"></div></div><div class="ls-lt-tile" style="width: 1104px; height: 71px; overflow: hidden;"><div class="ls-curtile" style="top: 0px; left: -1104px; -webkit-transform: rotate(0deg) scale(1, 1); opacity: 1;"><img src="assets/img/sliders/layer/bg2.jpg" style="width: 1920px; height: 500px; margin-left: -408px; margin-top: -71px;"></div></div><div class="ls-lt-tile" style="width: 1104px; height: 71px; overflow: hidden;"><div class="ls-curtile" style="top: 0px; left: -1104px; -webkit-transform: rotate(0deg) scale(1, 1); opacity: 1;"><img src="assets/img/sliders/layer/bg2.jpg" style="width: 1920px; height: 500px; margin-left: -408px; margin-top: -142px;"></div></div><div class="ls-lt-tile" style="width: 1104px; height: 71px; overflow: hidden;"><div class="ls-curtile" style="top: 0px; left: -1104px; -webkit-transform: rotate(0deg) scale(1, 1); opacity: 1;"><img src="assets/img/sliders/layer/bg2.jpg" style="width: 1920px; height: 500px; margin-left: -408px; margin-top: -213px;"></div></div><div class="ls-lt-tile" style="width: 1104px; height: 71px; overflow: hidden;"><div class="ls-curtile" style="top: 0px; left: -1104px; -webkit-transform: rotate(0deg) scale(1, 1); opacity: 1;"><img src="assets/img/sliders/layer/bg2.jpg" style="width: 1920px; height: 500px; margin-left: -408px; margin-top: -284px;"></div></div><div class="ls-lt-tile" style="width: 1104px; height: 71px; overflow: hidden;"><div class="ls-curtile" style="top: 0px; left: -1104px; -webkit-transform: rotate(0deg) scale(1, 1); opacity: 1;"><img src="assets/img/sliders/layer/bg2.jpg" style="width: 1920px; height: 500px; margin-left: -408px; margin-top: -355px;"></div></div><div class="ls-lt-tile" style="width: 1104px; height: 74px; overflow: hidden;"><div class="ls-curtile" style="top: 0px; left: -1104px; -webkit-transform: rotate(0deg) scale(1, 1); opacity: 1;"><img src="assets/img/sliders/layer/bg2.jpg" style="width: 1920px; height: 500px; margin-left: -408px; margin-top: -426px;"></div></div></div><div class="ls-nexttiles"><div class="ls-lt-tile" style="width: 1104px; height: 71px; overflow: hidden;"><div class="ls-nexttile" style="top: 0px; left: 0px; opacity: 1; -webkit-transform: rotate(0deg) rotateX(0deg) rotateY(0deg) scale(1, 1);"><img src="assets/img/sliders/layer/bg1.jpg" style="width: 1920px; height: 500px; margin-left: -408px; margin-top: 0px;"></div></div><div class="ls-lt-tile" style="width: 1104px; height: 71px; overflow: hidden;"><div class="ls-nexttile" style="top: 0px; left: 0px; opacity: 1; -webkit-transform: rotate(0deg) rotateX(0deg) rotateY(0deg) scale(1, 1);"><img src="assets/img/sliders/layer/bg1.jpg" style="width: 1920px; height: 500px; margin-left: -408px; margin-top: -71px;"></div></div><div class="ls-lt-tile" style="width: 1104px; height: 71px; overflow: hidden;"><div class="ls-nexttile" style="top: 0px; left: 0px; opacity: 1; -webkit-transform: rotate(0deg) rotateX(0deg) rotateY(0deg) scale(1, 1);"><img src="assets/img/sliders/layer/bg1.jpg" style="width: 1920px; height: 500px; margin-left: -408px; margin-top: -142px;"></div></div><div class="ls-lt-tile" style="width: 1104px; height: 71px; overflow: hidden;"><div class="ls-nexttile" style="top: 0px; left: 0px; opacity: 1; -webkit-transform: rotate(0deg) rotateX(0deg) rotateY(0deg) scale(1, 1);"><img src="assets/img/sliders/layer/bg1.jpg" style="width: 1920px; height: 500px; margin-left: -408px; margin-top: -213px;"></div></div><div class="ls-lt-tile" style="width: 1104px; height: 71px; overflow: hidden;"><div class="ls-nexttile" style="top: 0px; left: 0px; opacity: 1; -webkit-transform: rotate(0deg) rotateX(0deg) rotateY(0deg) scale(1, 1);"><img src="assets/img/sliders/layer/bg1.jpg" style="width: 1920px; height: 500px; margin-left: -408px; margin-top: -284px;"></div></div><div class="ls-lt-tile" style="width: 1104px; height: 71px; overflow: hidden;"><div class="ls-nexttile" style="top: 0px; left: 0px; opacity: 1; -webkit-transform: rotate(0deg) rotateX(0deg) rotateY(0deg) scale(1, 1);"><img src="assets/img/sliders/layer/bg1.jpg" style="width: 1920px; height: 500px; margin-left: -408px; margin-top: -355px;"></div></div><div class="ls-lt-tile" style="width: 1104px; height: 74px; overflow: hidden;"><div class="ls-nexttile" style="top: 0px; left: 0px; opacity: 1; -webkit-transform: rotate(0deg) rotateX(0deg) rotateY(0deg) scale(1, 1);"><img src="assets/img/sliders/layer/bg1.jpg" style="width: 1920px; height: 500px; margin-left: -408px; margin-top: -426px;"></div></div></div></div><div class="ls-layer ls-active" style="width: 1104px; height: 500px; visibility: visible; display: none; left: auto; right: 0px; top: 0px; bottom: auto;">

                    <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/sliders/layer/bg1.jpg" class="ls-bg" alt="Slide background" style="padding: 0px; border-width: 0px; width: 1920px; height: 500px; margin-left: -960px; margin-top: -250px; visibility: visible;">

                    <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/mockup/iphone1.png" alt="Slider Image" class="ls-s-1" style="top: 110px; left: 312px; width: 305px; height: 641px; padding: 0px; border-width: 0px; opacity: 1; -webkit-transform: rotate(0deg) scale(1, 1); margin-left: -41.51172189706483px; margin-top: 0px; display: block; visibility: visible;">

                    <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/mockup/iphone.png" alt="Slider image" class="ls-s-1" style="top: 60px; left: 112px; width: 305px; height: 641px; padding: 0px; border-width: 0px; opacity: 1; -webkit-transform: rotate(0deg) scale(1, 1); margin-left: -350.196997268533px; margin-top: 0px; display: block; visibility: visible;">

                    <span class="ls-s-1" style="text-transform: uppercase; line-height: 45px; font-size: 35px; color: rgb(255, 255, 255); top: 200px; left: 662px; width: auto; height: auto; padding: 0px; border-width: 0px; opacity: 1; -webkit-transform: rotate(0deg) scale(1, 1); margin-left: 0px; margin-top: -369.5090100975879px; display: block; visibility: visible;">
                        Fully Responsive <br> Bootstrap 3 Template
                    </span>

                    <a class="btn-u btn-u-orange ls-s1" href="#" style="padding: 9px 20px; font-size: 25px; top: 340px; left: 662px; width: auto; height: auto; border-width: 0px; opacity: 1; -webkit-transform: rotate(0deg) scale(1, 1); margin-left: 0px; margin-top: 218.21398234109523px; display: block; visibility: visible;">
                        Download Now
                    </a>
                </div><div class="ls-layer" style="width: 0px; height: 500px; visibility: visible; display: none; left: 0px; right: auto; top: 0px; bottom: auto;">
                    <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/bg/5.jpg" class="ls-bg" alt="Slide background" style="padding: 0px; border-width: 0px; width: 1600px; height: 1200px; margin-left: -800px; margin-top: -600px;">

                    <i class="fa fa-chevron-circle-right ls-s-1" style="color: rgb(255, 255, 255); font-size: 24px; top: 70px; left: 112px; width: auto; height: auto; padding: 0px; border-width: 0px; opacity: 1; -webkit-transform: rotate(0deg) scale(1, 1); margin-left: 0px; margin-top: -96px; display: block; visibility: visible;"></i> 

                    <span class="ls-s-2" style="color: rgb(255, 255, 255); font-weight: 200; font-size: 22px; top: 70px; left: 142px; width: auto; height: auto; padding: 0px; border-width: 0px; opacity: 1; -webkit-transform: rotate(0deg) scale(1, 1); margin-left: 0px; margin-top: -450px; display: block; visibility: visible;">
                        Fully Responsive and Easy to Customize
                    </span>

                    <i class="fa fa-chevron-circle-right ls-s-1" style="color: rgb(255, 255, 255); font-size: 24px; top: 120px; left: 112px; width: auto; height: auto; padding: 0px; border-width: 0px; opacity: 1; -webkit-transform: rotate(0deg) scale(1, 1); margin-left: 0px; margin-top: -146px; display: block; visibility: visible;"></i> 

                    <span class="ls-s-2" style="color: rgb(255, 255, 255); font-weight: 200; font-size: 22px; top: 120px; left: 142px; width: auto; height: auto; padding: 0px; border-width: 0px; opacity: 1; -webkit-transform: rotate(0deg) scale(1, 1); margin-left: 0px; margin-top: -450px; display: block; visibility: visible;">
                        Revolution and Layer Slider Included 
                    </span>

                    <i class="fa fa-chevron-circle-right ls-s-1" style="color: rgb(255, 255, 255); font-size: 24px; top: 170px; left: 112px; width: auto; height: auto; padding: 0px; border-width: 0px; opacity: 1; -webkit-transform: rotate(0deg) scale(1, 1); margin-left: 0px; margin-top: -196px; display: block; visibility: visible;"></i> 

                    <span class="ls-s-2" style="color: rgb(255, 255, 255); font-weight: 200; font-size: 22px; top: 170px; left: 142px; width: auto; height: auto; padding: 0px; border-width: 0px; opacity: 1; -webkit-transform: rotate(0deg) scale(1, 1); margin-left: 0px; margin-top: -450px; display: block; visibility: visible;">
                        1000+ Glyphicons Pro and Font Awesome Icons 
                    </span>

                    <i class="fa fa-chevron-circle-right ls-s-1" style="color: rgb(255, 255, 255); font-size: 24px; top: 220px; left: 112px; width: auto; height: auto; padding: 0px; border-width: 0px; opacity: 1; -webkit-transform: rotate(0deg) scale(1, 1); margin-left: 0px; margin-top: -246px; display: block; visibility: visible;"></i> 

                    <span class="ls-s-2" style="color: rgb(255, 255, 255); font-weight: 200; font-size: 22px; top: 220px; left: 142px; width: auto; height: auto; padding: 0px; border-width: 0px; opacity: 1; -webkit-transform: rotate(0deg) scale(1, 1); margin-left: 0px; margin-top: -450px; display: block; visibility: visible;">
                        Revolution and Layer Slider Included 
                    </span>

                    <i class="fa fa-chevron-circle-right ls-s-1" style="color: rgb(255, 255, 255); font-size: 24px; top: 270px; left: 112px; width: auto; height: auto; padding: 0px; border-width: 0px; opacity: 1; -webkit-transform: rotate(0deg) scale(1, 1); margin-left: 0px; margin-top: -296px; display: block; visibility: visible;"></i> 

                    <span class="ls-s-2" style="color: rgb(255, 255, 255); font-weight: 200; font-size: 22px; top: 270px; left: 142px; width: auto; height: auto; padding: 0px; border-width: 0px; opacity: 1; -webkit-transform: rotate(0deg) scale(1, 1); margin-left: 0px; margin-top: -450px; display: block; visibility: visible;">
                        60+ Template Pages and 20+ Plugins Included
                    </span>

                    <a class="btn-u btn-u-blue ls-s1" href="#" style="padding: 9px 20px; font-size: 25px; top: 340px; left: 112px; width: auto; height: auto; border-width: 0px; opacity: 1; -webkit-transform: rotate(0deg) scale(1, 1); margin-left: 0px; margin-top: 225px; display: block; visibility: visible;">
                        Twitter Bootstrap 3
                    </a>

                    <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/mockup/iphone1.png" alt="Slider Image" class="ls-s-1" style="top: 30px; left: 722px; width: 305px; height: 641px; padding: 0px; border-width: 0px; opacity: 1; -webkit-transform: rotate(0deg) scale(1, 1); margin-left: 0px; margin-top: 471px; display: block; visibility: visible;">
                </div><div class="ls-layer" style="width: 0px; height: 500px; left: 0px; right: auto; top: 0px; bottom: auto; visibility: visible; display: none;">
                    <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/sliders/layer/bg2.jpg" class="ls-bg" alt="Slide background" style="padding: 0px; border-width: 0px; width: 1920px; height: 500px; margin-left: -960px; margin-top: -250px; visibility: visible;">

                    <span class="ls-s-1" style="color: rgb(119, 119, 119); line-height: 45px; font-weight: 200; font-size: 35px; top: 100px; left: 122px; width: auto; height: auto; padding: 0px; border-width: 0px; opacity: 1; -webkit-transform: rotate(0deg) scale(1, 1); margin-left: 0px; margin-top: 401px; display: block; visibility: visible;">
                        Unify is Fully Responsive <br> Twitter Bootstrap 3 Template
                    </span>

                    <a class="btn-u btn-u-green ls-s-1" href="#" style="padding: 9px 20px; font-size: 25px; top: 220px; left: 122px; width: auto; height: auto; border-width: 0px; opacity: 1; -webkit-transform: rotate(0deg) scale(1, 1); margin-left: 0px; margin-top: 281px; display: block; visibility: visible;">
                        Find Out More
                    </a>

                    <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/mockup/iphone.png" alt="Slider Image" class="ls-s-1" style="top: 30px; left: 742px; width: 305px; height: 641px; padding: 0px; border-width: 0px; opacity: 1; -webkit-transform: rotate(0deg) scale(1, 1); margin-left: 0px; margin-top: 471px; display: block; visibility: visible;">
                </div><div class="ls-circle-timer" style="display: block;"><div class="ls-ct-left"><div class="ls-ct-rotate" style="-webkit-transform: rotate(0deg);"><div class="ls-ct-hider"><div class="ls-ct-half"></div></div></div></div><div class="ls-ct-right"><div class="ls-ct-rotate" style="-webkit-transform: rotate(88.92deg);"><div class="ls-ct-hider"><div class="ls-ct-half"></div></div></div></div><div class="ls-ct-center"></div></div></div>
                <!--End First Slide-->

                <!--Second Slide-->
                
                <!--End Second Slide-->

                <!--Third Slide-->
                
                <!--End Third Slide-->
            <div class="ls-loading-container" style="z-index: -1; display: none;"><div class="ls-loading-indicator"></div></div><a class="ls-nav-prev" href="#" style="visibility: visible; display: none;"></a><a class="ls-nav-next" href="#" style="visibility: visible; display: none;"></a><div class="ls-bottom-nav-wrapper" style="visibility: visible;"><a class="ls-nav-start ls-nav-start-active" href="#"></a><span class="ls-bottom-slidebuttons"><a href="#" class="ls-nav-active"></a><a href="#" class=""></a><a href="#" class=""></a><div class="ls-thumbnail-hover" style="width: 100px; height: 60px;"><div class="ls-thumbnail-hover-inner" style="visibility: hidden; display: block;"><div class="ls-thumbnail-hover-bg"></div><div class="ls-thumbnail-hover-img" style="width: 100px; height: 60px;"><img style="height: 60px;"></div><span></span></div></div></span><a class="ls-nav-stop" href="#"></a></div><div class="ls-shadow"></div></div>         
        </div>
    </div>
    <!-- content
    ================================================== -->
<div class="container content">	
    	
    	<!-- Recent Works -->
        <div class="headline"><h2>Recent Works</h2></div>
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
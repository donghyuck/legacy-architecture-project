<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title><#if action.webSite ?? >${action.webSite.displayName }<#else>::</#if></title>
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/flexslider/flexslider.css" />
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/parallax-slider/parallax-slider.css" />
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.0.3/font-awesome.min.css',
			'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			
			'${request.contextPath}/js/headroom/headroom.min.js',
			'${request.contextPath}/js/headroom/jquery.headroom.min.js',
			
			'${request.contextPath}/js/flexslider/jquery.flexslider-min.js',
			'${request.contextPath}/js/common/common.modernizr.custom.js',
			'${request.contextPath}/js/parallax-slider/jquery.cslider.js',
			
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
			
				$('#da-slider').cslider();
				$('#clients-flexslider').flexslider({
					animation: "slide",
					easing: "swing",
					animationLoop: true,
					itemWidth: 1,
					itemMargin: 1,
					minItems: 2,
					maxItems: 9,
					controlNav: false,
					directionNav: false,
					move: 2
				 });				
				// top nav bar 
				$("nav.navbar:first").headroom();				
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
	<div class="slider-inner">
        <div id="da-slider" class="da-slider" style="background-position: 50% 0%;">
            <div class="da-slide da-slide-toleft">
                <h2><i>CLEAN &amp; FRESH</i> <br> <i>FULLY RESPONSIVE</i> <br> <i>DESIGN</i></h2>
                <p><i>Lorem ipsum dolor amet</i> <br> <i>tempor incididunt ut</i> <br> <i>veniam omnis </i></p>
                <div class="da-img"><img src="http://htmlstream.com/preview/unify-v1.4/assets/plugins/parallax-slider/img/1.png" alt=""></div>
            </div>
            <div class="da-slide da-slide-fromright da-slide-current">
                <h2><i>RESPONSIVE VIDEO</i> <br> <i>SUPPORT AND</i> <br> <i>MANY MORE</i></h2>
                <p><i>Lorem ipsum dolor amet</i> <br> <i>tempor incididunt ut</i></p>
                <div class="da-img">
    				<iframe src="http://player.vimeo.com/video/47911018" width="530" height="300" frameborder="0" webkitallowfullscreen="" mozallowfullscreen="" allowfullscreen=""></iframe> 
                </div>
            </div>
            <div class="da-slide">
                <h2><i>USING BEST WEB</i> <br> <i>SOLUTIONS WITH</i> <br> <i>HTML5/CSS3</i></h2>
                <p><i>Lorem ipsum dolor amet</i> <br> <i>tempor incididunt ut</i> <br> <i>veniam omnis </i></p>
                <div class="da-img"><img src="http://htmlstream.com/preview/unify-v1.4/assets/plugins/parallax-slider/img/html5andcss3.png" alt="image01"></div>
            </div>
            <div class="da-arrows">
                <span class="da-arrows-prev"></span>
                <span class="da-arrows-next"></span>		
            </div>
        <nav class="da-dots"><span class=""></span><span class="da-dots-current"></span><span></span></nav></div>
	</div>
    <!-- content
    ================================================== -->
<div class="container content">	
    	<!-- Service Blocks -->
    	<div class="row margin-bottom-30">
        	<div class="col-md-4">
        		<div class="service">
                    <i class="fa fa-compress service-icon"></i>
        			<div class="desc">
        				<h4>Fully Responsive</h4>
                        <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus etiam sem...</p>
        			</div>
        		</div>	
        	</div>
        	<div class="col-md-4">
        		<div class="service">
                    <i class="fa fa-cogs service-icon"></i>
        			<div class="desc">
        				<h4>HTML5 + CSS3</h4>
                        <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus etiam sem...</p>
        			</div>
        		</div>	
        	</div>
        	<div class="col-md-4">
        		<div class="service">
                    <i class="fa fa-rocket service-icon"></i>
        			<div class="desc">
        				<h4>Launch Ready</h4>
                        <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus etiam sem...</p>
        			</div>
        		</div>	
        	</div>			    
    	</div>
    	<!-- End Service Blokcs -->

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
                    <small>CEO, Jack Bour</small>
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
        <div id="clients-flexslider" class="flexslider home clients">
            <div class="headline"><h2>Family Site</h2></div>    
            
        <div class="flex-viewport" style="overflow: hidden; position: relative;"><ul class="slides" style="width: 3400%; -webkit-transition: 0.6s; transition: 0.6s; -webkit-transform: translate3d(-1012.3333333333335px, 0px, 0px);">
                <li style="width: 125.66666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients/hp_grey.png" alt=""> 
                        <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients/hp.png" class="color-img" alt="">
                    </a>
                </li>
                <li style="width: 125.66666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients/vadafone_grey.png" alt=""> 
                        <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients/vadafone.png" class="color-img" alt="">
                    </a>
                </li>
                <li style="width: 125.66666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients/walmart_grey.png" alt=""> 
                        <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients/walmart.png" class="color-img" alt="">
                    </a>
                </li>
                <li style="width: 125.66666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients/shell_grey.png" alt=""> 
                        <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients/shell.png" class="color-img" alt="">
                    </a>
                </li>
                <li style="width: 125.66666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients/natural_grey.png" alt=""> 
                        <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients/natural.png" class="color-img" alt="">
                    </a>
                </li>
                <li style="width: 125.66666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients/aztec_grey.png" alt=""> 
                        <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients/aztec.png" class="color-img" alt="">
                    </a>
                </li>
                <li style="width: 125.66666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients/gamescast_grey.png" alt=""> 
                        <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients/gamescast.png" class="color-img" alt="">
                    </a>
                </li>
                <li style="width: 125.66666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients/cisco_grey.png" alt=""> 
                        <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients/cisco.png" class="color-img" alt="">
                    </a>
                </li>
                <li style="width: 125.66666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients/everyday_grey.png" alt=""> 
                        <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients/everyday.png" class="color-img" alt="">
                    </a>
                </li>
                <li style="width: 125.66666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients/cocacola_grey.png" alt=""> 
                        <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/clients/cocacola.png" class="color-img" alt="">
                    </a>
                </li>
            </ul></div></div><!--/flexslider-->
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
<#ftl encoding="UTF-8"/>
<html decorator="unify">
<head>
		<title>기업소개</title>
		<script type="text/javascript">
		<!--

		var jobs = [];	
				
		yepnope([{
			load: [
			'css!<@spring.url "/styles/font-awesome/4.2.0/font-awesome.min.css"/>',
			'css!<@spring.url "/styles/bootstrap.themes/unify/colors/blue.css"/>',		
			'css!<@spring.url "/styles/bootstrap.themes/unify/pages/feature_timeline-v2.css"/>',		
			'css!<@spring.url "/styles/common/common.flat-icons.css"/>',		
			'<@spring.url "/js/jquery/1.10.2/jquery.min.js"/>',
			'<@spring.url "/js/jgrowl/jquery.jgrowl.min.js"/>',
			'<@spring.url "/js/kendo/kendo.web.min.js"/>',
			'<@spring.url "/js/kendo.extension/kendo.ko_KR.js"/>',			
			'<@spring.url "/js/kendo/cultures/kendo.culture.ko-KR.min.js"/>',		
			'<@spring.url "/js/bootstrap/3.2.0/bootstrap.min.js"/>',
			'<@spring.url "/js/common/common.ui.core.js"/>',							
			'<@spring.url "/js/common/common.ui.data.js"/>',
			'<@spring.url "/js/common/common.ui.community.js"/>'],
			complete: function() {
			
				common.ui.setup({
					features:{
						wallpaper : false,
						lightbox : true,
						spmenu : false,
						morphing : false,
						accounts : {
							authenticate : function(e){
								e.token.copy(currentUser);
								if( !currentUser.anonymous ){		
															 
								}
							} 
						}						
					},
					jobs:jobs
				});	

				// ACCOUNTS LOAD	
			var currentUser = new common.ui.data.User();			
			
				<#if !action.user.anonymous >							
				</#if>	
				// END SCRIPT            
			}
		}]);	
		-->
		</script>		
		<style scoped="scoped">						
		</style>   	
	</head>
	<body>
		<div class="page-loader"></div>
		<div class="wrapper">
			<!-- START HEADER -->
			<#include "/html/common/common-homepage-menu.ftl" >	
			<#if action.webSite ?? >
				<#assign webSiteMenu = action.getWebSiteMenu("USER_MENU") />
				<#assign navigator = WebSiteUtils.getMenuComponent(webSiteMenu, "MENU_1_1") />
			<header  class="cloud <#if navigator.parent.css??>${navigator.parent.css}</#if>">			
			<script>
				jobs.push(function () {
					$(".navbar-nav li[data-menu-item='${navigator.parent.name}']").addClass("active");
				});
			</script>
				<div class="breadcrumbs">
					<div class="container">
						<h1 class="pull-left">${ navigator.title }
							<small>
								<i class="fa fa-quote-left"></i>&nbsp;${ navigator.description ? replace ("{displayName}" , action.webSite.company.displayName ) }&nbsp;<i class="fa fa-quote-right"></i>
							</small>
						</h1>
						<ul class="pull-right breadcrumb">
							<li><a href="main.do"><i class="fa fa-home fa-lg"></i></a></li>
							<li><a href="">${navigator.parent.title}</a></li>
							<li class="active">${navigator.title}</li>
						</ul>
					</div>
				</div>	
			</header>					
			<!-- START MAIN CONTENT -->	
			<div class="container content">
				<div class="row">
					<div class="col-lg-3 visible-lg">	
						<div class="headline"><h4> ${navigator.parent.title} </h4></div>  
	                	<p class="margin-bottom-25"><small>${navigator.parent.description!" " }</small></p>					
						<div class="list-group">
						<#list navigator.parent.components as item >
							<#if item.name ==  navigator.name >
							<a href="${item.page}" class="list-group-item active">${ item.title } </a>
							<#else>
							<a href="${item.page}" class="list-group-item">${ item.title } </a>
							</#if>						
						</#list>
						</div>
					</div>
					<div class="col-lg-9">		
						<div class="content-main-section" style="min-height:300px;">


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


						</div>
					</div>
				</div>
			</div><!-- /.container -->		

<div class="container content">		
    	<div class="row margin-bottom-40">
        	<div class="col-md-6 md-margin-bottom-40">
                <p>Unify is an incredibly beautiful responsive Bootstrap Template for corporate and creative professionals. It works on all major web browsers, tablets and phone. Lorem sequat ipsum dolor lorem sit amet, consectetur adipiscing dolor elit. Unify is an incredibly beautiful responsive Bootstrap Template for It works on all major web.</p>
                <ul class="list-unstyled">
                    <li><i class="fa fa-check color-green"></i> Donec id elit non mi porta gravida</li>
                    <li><i class="fa fa-check color-green"></i> Corporate and Creative</li>
                    <li><i class="fa fa-check color-green"></i> Responsive Bootstrap Template</li>
                    <li><i class="fa fa-check color-green"></i> Elit non mi porta gravida</li>
                    <li><i class="fa fa-check color-green"></i> Award winning digital agency</li>
                </ul><br>

                <!-- Blockquotes -->
                <blockquote class="hero-unify">
                    <p>Award winning digital agency. We bring a personal and effective approach to every project we work on, which is why.</p>
                    <small>CEO Jack Bour</small>
                </blockquote>
            </div>

        	<div class="col-md-6 md-margin-bottom-40">
                <div class="responsive-video">
                    <iframe src="http://player.vimeo.com/video/9679622" frameborder="0" webkitallowfullscreen="" mozallowfullscreen="" allowfullscreen=""></iframe> 
                </div>
            </div>
        </div><!--/row-->

    	<!-- Meer Our Team -->
    	<div class="headline"><h2>Meet Our Team</h2></div>
        <div class="row team">
            <div class="col-sm-3">
                <div class="thumbnail-style">
                    <img class="img-responsive" src="assets/img/team/1.jpg" alt="">
                    <h3><a>Jack Bour</a> <small>Chief Executive Officer</small></h3>
                    <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, justo sit amet risus etiam porta sem...</p>
                    <ul class="list-unstyled list-inline team-socail">
                    	<li><a href="#"><i class="fa fa-facebook"></i></a></li>
                    	<li><a href="#"><i class="fa fa-twitter"></i></a></li>
                    	<li><a href="#"><i class="fa fa-google-plus"></i></a></li>
                    </ul>
                </div>
            </div>
            <div class="col-sm-3">
                <div class="thumbnail-style">
                    <img class="img-responsive" src="assets/img/team/3.jpg" alt="">
                    <h3><a>Kate Metus</a> <small>Project Manager</small></h3>
                    <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, justo sit amet risus etiam porta sem...</p>
                    <ul class="list-unstyled list-inline team-socail">
                    	<li><a href="#"><i class="fa fa-facebook"></i></a></li>
                    	<li><a href="#"><i class="fa fa-twitter"></i></a></li>
                    	<li><a href="#"><i class="fa fa-google-plus"></i></a></li>
                    </ul>
                </div>
            </div>
            <div class="col-sm-3">
                <div class="thumbnail-style">
                    <img class="img-responsive" src="assets/img/team/2.jpg" alt="">
                    <h3><a>Porta Gravida</a> <small>VP of Operations</small></h3>
                    <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, justo sit amet risus etiam porta sem...</p>
                    <ul class="list-unstyled list-inline team-socail">
                    	<li><a href="#"><i class="fa fa-facebook"></i></a></li>
                    	<li><a href="#"><i class="fa fa-twitter"></i></a></li>
                    	<li><a href="#"><i class="fa fa-google-plus"></i></a></li>
                    </ul>
                </div>
            </div>
            <div class="col-sm-3">
                <div class="thumbnail-style">
                    <img class="img-responsive" src="assets/img/team/4.jpg" alt="">
                    <h3><a>Donec Elit</a> <small>Director, R &amp; D Talent</small></h3>
                    <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, justo sit amet risus etiam porta sem...</p>
                    <ul class="list-unstyled list-inline team-socail">
                    	<li><a href="#"><i class="fa fa-facebook"></i></a></li>
                    	<li><a href="#"><i class="fa fa-twitter"></i></a></li>
                    	<li><a href="#"><i class="fa fa-google-plus"></i></a></li>
                    </ul>
                </div>
            </div>
        </div><!--/team-->
    	<!-- End Meer Our Team -->

        <!-- Our Clients -->
        <div id="clients-flexslider" class="flexslider home clients">
            <div class="headline"><h2>Our Clients</h2></div>    
            
        <div class="flex-viewport" style="overflow: hidden; position: relative;"><ul class="slides" style="width: 3400%; -webkit-transition: 0.6s; transition: 0.6s; -webkit-transform: translate3d(-506.666666666667px, 0px, 0px);">
                <li style="width: 125.666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="assets/img/clients/hp_grey.png" alt=""> 
                        <img src="assets/img/clients/hp.png" class="color-img" alt="">
                    </a>
                </li>
                <li style="width: 125.666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="assets/img/clients/igneus_grey.png" alt=""> 
                        <img src="assets/img/clients/igneus.png" class="color-img" alt="">
                    </a>
                </li>
                <li style="width: 125.666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="assets/img/clients/vadafone_grey.png" alt=""> 
                        <img src="assets/img/clients/vadafone.png" class="color-img" alt="">
                    </a>
                </li>
                <li style="width: 125.666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="assets/img/clients/walmart_grey.png" alt=""> 
                        <img src="assets/img/clients/walmart.png" class="color-img" alt="">
                    </a>
                </li>
                <li style="width: 125.666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="assets/img/clients/shell_grey.png" alt=""> 
                        <img src="assets/img/clients/shell.png" class="color-img" alt="">
                    </a>
                </li>
                <li style="width: 125.666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="assets/img/clients/natural_grey.png" alt=""> 
                        <img src="assets/img/clients/natural.png" class="color-img" alt="">
                    </a>
                </li>
                <li style="width: 125.666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="assets/img/clients/aztec_grey.png" alt=""> 
                        <img src="assets/img/clients/aztec.png" class="color-img" alt="">
                    </a>
                </li>
                <li style="width: 125.666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="assets/img/clients/gamescast_grey.png" alt=""> 
                        <img src="assets/img/clients/gamescast.png" class="color-img" alt="">
                    </a>
                </li>
                <li style="width: 125.666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="assets/img/clients/cisco_grey.png" alt=""> 
                        <img src="assets/img/clients/cisco.png" class="color-img" alt="">
                    </a>
                </li>
                <li style="width: 125.666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="assets/img/clients/everyday_grey.png" alt=""> 
                        <img src="assets/img/clients/everyday.png" class="color-img" alt="">
                    </a>
                </li>
                <li style="width: 125.666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="assets/img/clients/cocacola_grey.png" alt=""> 
                        <img src="assets/img/clients/cocacola.png" class="color-img" alt="">
                    </a>
                </li>
                <li style="width: 125.666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="assets/img/clients/spinworkx_grey.png" alt=""> 
                        <img src="assets/img/clients/spinworkx.png" class="color-img" alt="">
                    </a>
                </li>
                <li style="width: 125.666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="assets/img/clients/shell_grey.png" alt=""> 
                        <img src="assets/img/clients/shell.png" class="color-img" alt="">
                    </a>
                </li>
                <li style="width: 125.666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="assets/img/clients/natural_grey.png" alt=""> 
                        <img src="assets/img/clients/natural.png" class="color-img" alt="">
                    </a>
                </li>
                <li style="width: 125.666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="assets/img/clients/gamescast_grey.png" alt=""> 
                        <img src="assets/img/clients/gamescast.png" class="color-img" alt="">
                    </a>
                </li>
                <li style="width: 125.666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="assets/img/clients/everyday_grey.png" alt=""> 
                        <img src="assets/img/clients/everyday.png" class="color-img" alt="">
                    </a>
                </li>
                <li style="width: 125.666666666667px; float: left; display: block;">
                    <a href="#">
                        <img src="assets/img/clients/spinworkx_grey.png" alt=""> 
                        <img src="assets/img/clients/spinworkx.png" class="color-img" alt="">
                    </a>
                </li>
            </ul></div></div><!--/flexslider-->
        <!-- //End Our Clients -->
    </div>
			
			<!-- END MAIN CONTENT -->	
			</#if>	
	 		<!-- START FOOTER -->
			<#include "/html/common/common-homepage-footer.ftl" >		
			<!-- END FOOTER -->
		</div><!-- ./wrapper -->	
		<!-- START TEMPLATE -->
		<#include "/html/common/common-homepage-templates.ftl" >		
		<!-- END TEMPLATE -->
	</body>    
</html>
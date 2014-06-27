<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title><#if action.webSite ?? >${action.webSite.displayName }<#else>::</#if></title>
		<#compress>		
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/bxslider/jquery.bxslider.css">
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/layer-slider/layerslider.css" />
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/layer-slider/skins/fullwidth/skin.css" />
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/common/common.img-hover.css" />
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/common/common.ui.portfolio-v2.css" />
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/common.pages/onepage.css" />
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/common.themes/pomegranate.css" />
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',								
			'${request.contextPath}/js/kendo/kendo.web.min.js',			
			'${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js',			
			'${request.contextPath}/js/kendo.extension/kendo.ko_KR.js',		
						
			'${request.contextPath}/js/bootstrap/3.1.0/bootstrap.min.js',
			
			'${request.contextPath}/js/bxslider/jquery.bxslider.min.js',			
			'${request.contextPath}/js/common.plugins/jquery.mixitup.js',		
						
			'${request.contextPath}/js/jquery.plugins/easing/jquery.easing.1.3.js',
			'${request.contextPath}/js/layer-slider/jquery.transit.modified.js',
			'${request.contextPath}/js/layer-slider/layerslider.transitions.js',
			'${request.contextPath}/js/layer-slider/layerslider.kreaturamedia.jquery.js',		

			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',	
						
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

				$('.bxslider').bxSlider({
					maxSlides: 4,
					minSlides: 4,
					slideWidth: 360,
					slideMargin: 10,
				}); 

            $('.bxslider1').bxSlider({
                minSlides: 3,
                maxSlides: 3,
                slideWidth: 360,
                slideMargin: 10
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
		        				
		        				$('.sorting-grid').mixItUp();
		        								
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
					if(this.view().length>0)
						$("#announce-view").html(kendo.render(template, this.view()))
				}).read();
				
				forum.dataSource().bind('change', function(){		
					if(this.view().length>0)			
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

<!--=== Slider ===-->
    <div class="layer_slider">
        <div id="layerslider-container-fw">        
            <div id="layerslider" style="width: 100%; height: 500px; margin: 0px auto; ">
                <!--First Slide-->
                <div class="ls-layer" style="slidedirection: right; transition2d: 24,25,27,28; ">

                    <img src="http://fc01.deviantart.net/fs70/f/2012/036/d/9/d9fa639fc4b7eb93f4f28c891eef35d9-d4othd2.jpg" class="ls-bg" alt="Slide background">

                    <img src="http://th05.deviantart.net/fs70/PRE/i/2013/153/1/b/disney_university___taran_by_hyung86-d67k5q2.png" alt="Slider Image" class="ls-s-1" style=" top:110px; left: 240px; slidedirection : left; slideoutdirection : bottom; durationin : 1500; durationout : 1500; ">

                    <img src="http://th05.deviantart.net/fs71/PRE/i/2013/272/9/a/disney_university___jane_by_hyung86-d6oh4zo.png" alt="Slider image" class="ls-s-1" style=" top:60px; left: 40px; slidedirection : left; slideoutdirection : bottom; durationin : 2500; durationout : 2500; ">

					<i class="fa fa-chevron-circle-right ls-s-1" style=" color: #fff; font-size: 24px; top:70px; left: 40px; slidedirection : left; slideoutdirection : top; durationin : 1500; durationout : 500; "></i> 
                    <span class="ls-s-2" style=" color: #fff; font-weight: 200; font-size: 22px; top:70px; left: 70px; slidedirection : top; slideoutdirection : bottom; durationin : 1500; durationout : 500; ">
                      센스있는 신입사원이 되는 첫 걸음.
                    </span>
                    <i class="fa fa-chevron-circle-right ls-s-1" style=" color: #fff; font-size: 24px; top:120px; left: 40px; slidedirection : left; slideoutdirection : top; durationin : 2500; durationout : 1500; "></i> 
                    <span class="ls-s-2" style=" color: #fff; font-weight: 200; font-size: 22px; top:120px; left: 70px; slidedirection : top; slideoutdirection : bottom; durationin : 2500; durationout : 1500; ">
                    예절, 규범 그리고 약속의 기본인성을 바탕으로 제작
                    </span>
                     <i class="fa fa-chevron-circle-right ls-s-1" style=" color: #fff; font-size: 24px; top:170px; left: 40px; slidedirection : left; slideoutdirection : top; durationin : 3500; durationout : 3500; "></i> 

                    <span class="ls-s-2" style=" color: #fff; font-weight: 200; font-size: 22px; top:170px; left: 70px; slidedirection : top; slideoutdirection : bottom; durationin : 3500; durationout : 2500; ">
                      직장의 비즈니스 매너, 영상으로 기본을 익히다.
                    </span>
                                                           
                    <span class="ls-s-1" style=" text-transform: uppercase; line-height: 45px; font-size:30px; color:#fff; top:200px; left: 590px; slidedirection : top; slideoutdirection : bototm; durationin : 3500; durationout : 3500; ">
                  	취업역량강화 동영상 <br/>
					<p class="" style="font-size:50px; font-weight:bold; color:#fff;">직립보행</p>
                    </span>

                    <a class="btn-u btn-u-blue ls-s1" href="${request.contextPath}/page.do?name=PAGE-51" style=" padding: 9px 20px; font-size:25px; top:340px; left: 590px; slidedirection : bottom; slideoutdirection : top; durationin : 3500; durationout : 3500; ">
                        더 알아보기
                    </a>
                </div>
                <!--End First Slide-->

                <!--Second Slide-->
                <div class="ls-layer" style="slidedirection: top; ">
                    <img src="http://fc01.deviantart.net/fs70/f/2014/167/c/8/c8f52fa7ddf1db4654ea37a7a6fdc9af-d7moekm.jpg" class="ls-bg" alt="Slide background">

                    <i class="fa fa-chevron-circle-right ls-s-1" style=" color: #fff; font-size: 24px; top:70px; left: 40px; slidedirection : left; slideoutdirection : top; durationin : 1500; durationout : 500; "></i> 

                    <span class="ls-s-2" style=" color: #fff; font-weight: 200; font-size: 22px; top:70px; left: 70px; slidedirection : top; slideoutdirection : bottom; durationin : 1500; durationout : 500; ">
                        임직원의 역량개발과 경쟁력 향상 및 업무성과 달성을 위해
                    </span>

                    <i class="fa fa-chevron-circle-right ls-s-1" style=" color: #fff; font-size: 24px; top:120px; left: 40px; slidedirection : left; slideoutdirection : top; durationin : 2500; durationout : 1500; "></i> 

                    <span class="ls-s-2" style=" color: #fff; font-weight: 200; font-size: 22px; top:120px; left: 70px; slidedirection : top; slideoutdirection : bottom; durationin : 2500; durationout : 1500; ">
                        지신이 수행하는 업무에서 요구하는 역량에 대한 수준 및 적합도,
                    </span>

                    <i class="fa fa-chevron-circle-right ls-s-1" style=" color: #fff; font-size: 24px; top:170px; left: 40px; slidedirection : left; slideoutdirection : top; durationin : 3500; durationout : 3500; "></i> 

                    <span class="ls-s-2" style=" color: #fff; font-weight: 200; font-size: 22px; top:170px; left: 70px; slidedirection : top; slideoutdirection : bottom; durationin : 3500; durationout : 2500; ">
                        희망 부서에서 요구하는 역량에 대한 수준을 진단하여
                    </span>

                    <i class="fa fa-chevron-circle-right ls-s-1" style=" color: #fff; font-size: 24px; top:220px; left: 40px; slidedirection : left; slideoutdirection : top; durationin : 4500; durationout : 3500; "></i> 

                    <span class="ls-s-2" style=" color: #fff; font-weight: 200; font-size: 22px; top:220px; left: 70px; slidedirection : top; slideoutdirection : bottom; durationin : 4500; durationout : 3500; ">
                        개개인의 수준을 파악하고 수준별로 맞춤 교육을 제공하는
                    </span>

                    <i class="fa fa-chevron-circle-right ls-s-1" style=" color: #fff; font-size: 24px; top:270px; left: 40px; slidedirection : left; slideoutdirection : top; durationin : 5500; durationout : 4500; "></i> 

                    <span class="ls-s-2" style=" color: #fff; font-weight: 200; font-size: 22px; top:270px; left: 70px; slidedirection : top; slideoutdirection : bottom; durationin : 5500; durationout : 4500; ">
                       역량진단 서비스
                    </span>

                    <a class="btn-u btn-u-blue ls-s1" href="#" style=" padding: 9px 20px; font-size:25px; top:340px; left: 40px; slidedirection : bottom; slideoutdirection : bottom; durationin : 6500; durationout : 3500; ">
                      iCAP
                    </a>

                    <img src="http://fc04.deviantart.net/fs37/f/2008/261/b/1/Smile_by_Yingz.jpg" alt="Slider Image" class="ls-s-1" style=" top:30px; left: 650px; slidedirection : right; slideoutdirection : bottom; durationin : 1500; durationout : 1500; ">
                </div>
                <!--End Second Slide-->

                <!--Third Slide-->
                <div class="ls-layer" style="slidedirection: right; transition2d: 92,93,105; ">
                    <img src="http://fc08.deviantart.net/fs70/i/2014/049/0/4/kiki_s_delivery_service_by_jauni-d7702kw.png" class="ls-bg" alt="Slide background">

                    <span class="ls-s-1" style=" color: #fff; line-height:45px; font-weight: 200; font-size: 35px; top:100px; left: 50px; slidedirection : top; slideoutdirection : bottom; durationin : 1000; durationout : 1000; ">
                       <span style="font-size:50px; font-weight:bold; "> 국내 위탁 교육 </span><br> 기업 교육 전 부문 위탁 서비스
                    </span>

                    <a class="btn-u btn-u-green ls-s-1" href="#" style=" padding: 9px 20px; font-size:25px; top:220px; left: 50px; slidedirection : bottom; slideoutdirection : bottom; durationin : 2000; durationout : 2000; ">
                        더 알아보기
                    </a>

                    <img src="http://fc08.deviantart.net/fs71/i/2011/021/3/7/athenastock__business_people_by_athenastock-d37pinu.jpg" alt="Slider Image" class="ls-s-1" style=" top:30px; left: 670px; slidedirection : right; slideoutdirection : bottom; durationin : 3000; durationout : 3000; ">
                </div>
                <!--End Third Slide-->
            </div>         
        </div>
    </div><!--/layer_slider-->
    <!--=== End Slider ===-->
		<div class="container content">	

    <!-- content
    ================================================== -->
       <!-- Portfolio Sorting Blocks -->
        <div class="sorting-block">
            <ul class="sorting-nav sorting-nav-v1 text-center">
                <li class="filter" data-filter="all">전체</li>
                <li class="filter" data-filter=".category-1">기업서비스</li>
                <li class="filter" data-filter=".category-2">대학서비스</li>
                <li class="filter" data-filter=".category-3">자격사업</li>
            </ul>

            <ul class="row sorting-grid">
                <li class="col-md-3 col-sm-6 col-xs-12 mix category-1" data-cat="1" >
                    <a href="#">
                        <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/11.jpg" alt="">
                        <span class="sorting-cover">
                            <span>국내위탁교육</span>
                            <p></p>
                        </span>
                    </a>
                </li>
                <li class="col-md-3 col-sm-6 col-xs-12 mix category-1" data-cat="1">
                    <a href="#">
                        <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/12.jpg" alt="">
                        <span class="sorting-cover">
                            <span>해외연수</span>
                            <p></p>
                        </span>
                    </a>
                </li>
                <li class="col-md-3 col-sm-6 col-xs-12 mix category-1" data-cat="1">
                    <a href="#">
                        <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/13.jpg" alt="">
                        <span class="sorting-cover">
                            <span>HRD 컨설팅</span>
                            <p></p>
                        </span>
                    </a>
                </li>
                <li class="col-md-3 col-sm-6 col-xs-12 mix category-1 " data-cat="1">
                    <a href="#">
                        <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/3.jpg" alt="">
                        <span class="sorting-cover">
                            <span>HRD 자문 서비스</span>
                            <p></p>
                        </span>
                    </a>
                </li>
                <li class="col-md-3 col-sm-6 col-xs-12 mix category-1" data-cat="1">
                    <a href="#">
                        <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/2.jpg" alt="">
                        <span class="sorting-cover">
                            <span>INKIUM e-HRD</span>
                            <p></p>
                        </span>
                    </a>
                </li>
                <li class="col-md-3 col-sm-6 col-xs-12 mix category-1" data-cat="1">
                    <a href="#">
                        <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/6.jpg" alt="">
                        <span class="sorting-cover">
                            <span>iCAP</span>
                            <p></p>
                        </span>
                    </a>
                </li>
                <li class="col-md-3 col-sm-6 col-xs-12 mix category-1" data-cat="1">
                    <a href="#">
                        <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/8.jpg" alt="">
                        <span class="sorting-cover">
                            <span>Studymart</span>
                            <p></p>
                        </span>
                    </a>
                </li>
                <li class="col-md-3 col-sm-6 col-xs-12 mix category-1" data-cat="1">
                    <a href="#">
                        <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/1.jpg" alt="">
                        <span class="sorting-cover">
                            <span>iNTV</span>
                            <p></p>
                        </span>
                    </a>
                </li>
                <li class="col-md-3 col-sm-6 col-xs-12 mix  category-2" data-cat="1">
                    <a href="#">
                        <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/11.jpg" alt="">
                        <span class="sorting-cover">
                            <span>제품/서비스 명</span>
                            <p>제품 / 서비스 한줄 요약</p>
                        </span>
                    </a>
                </li>
                <li class="col-md-3 col-sm-6 col-xs-12 mix category-3 category-2" data-cat="3">
                    <a href="#">
                        <img class="img-responsive" src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/12.jpg" alt="">
                        <span class="sorting-cover">
                            <span>제품/서비스 명</span>
                            <p>제품 / 서비스 한줄 요약</p>
                        </span>
                    </a>
                </li>
            </ul>
        
            <div class="clearfix"></div>
			</div>
			<!-- End Portfolio Sorting Blocks -->
        	</div><!-- /content -->
        	
        		
			<!-- END MAIN CONTENT -->	
			<div class="one-page fade in">
				<div class="one-page-inner one-dark">
				
				
			<div class="container">			
				<div class="row">
					<div class="col-md-4">
						<div class="headline">
							<h3 class="heading-sm no-top-space">공지 & 이벤트</h3>
							<div class="headline-controls"><a class="btn-u btn-u-blue btn-u-xs" href="${request.contextPath}/press.do">더보기</a></div>
						</div>						
						<ul  id="announce-view" class="list-unstyled summaries" style="min-height:100px;">						
							<li><small class="hex">진행중인 공지 또는 이벤트가 없습니다.</small></li>
						</ul>		
								
					</div>
					<div class="col-md-4">
						<div class="headline">
							<h3 class="heading-sm no-top-space">뉴스</h3>
							<div class="headline-controls"><a class="btn-u btn-u-blue btn-u-xs" href="${request.contextPath}/press.do">더보기</a></div>
						</div>
						<ul  id="news-view" class="list-unstyled summaries" style="min-height:100px;">						
							<li><small class="hex">등록된 뉴스가 없습니다.</small></li>
						</ul>	
					</div>
					<div class="col-md-4">
						<div class="headline hidden"><h5>최근 활동</h5></div>
						<div id="myCarousel" class="carousel slide carousel-v1">
			                    <div class="carousel-inner">
			                        <div class="item active">
			                            <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/5.jpg" alt="">
			                            <div class="carousel-caption">
			                                <p>XX 고객사 위탁교육실시</p>
			                            </div>
			                        </div>
			                        <div class="item">
			                            <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/4.jpg" alt="">
			                            <div class="carousel-caption">
			                                <p>무신무신...</p>
			                            </div>
			                            </div>
			                        <div class="item">
			                            <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/3.jpg" alt="">
			                            <div class="carousel-caption">
			                                <p>여름 수련회....</p>
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
						</div>
					</div>
				</div>	
			</div></div>	
				
		</div><!-- /wrapper -->	

 		<!-- START FOOTER -->
		<#include "/html/common/common-homepage-footer.ftl" >		
		<!-- END FOOTER -->		
		<!-- START TEMPLATE -->
		<#include "/html/common/common-homepage-templates.ftl" >		
		<!-- END TEMPLATE -->
	</body>    
</html>
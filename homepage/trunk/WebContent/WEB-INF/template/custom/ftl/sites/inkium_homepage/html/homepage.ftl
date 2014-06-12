<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title><#if action.webSite ?? >${action.webSite.displayName }<#else>::</#if></title>
		<#compress>		
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/bxslider/jquery.bxslider.css">
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/layer-slider/layerslider.css" />
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/layer-slider/skins/fullwidth/skin.css" />
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/common/common.img-hover.css" />
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/common.themes/pomegranate.css" />
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',
			'${request.contextPath}/js/bxslider/jquery.bxslider.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',						
			'${request.contextPath}/js/kendo/kendo.web.min.js',
	
			'${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js',			
			'${request.contextPath}/js/kendo.extension/kendo.ko_KR.js',					
			'${request.contextPath}/js/bootstrap/3.1.0/bootstrap.min.js',
			
			'${request.contextPath}/js/bxslider/jquery.bxslider.min.js',
			'${request.contextPath}/js/jquery.plugins/easing/jquery.easing.1.3.js',
			'${request.contextPath}/js/layer-slider/jquery.transit.modified.js',
			'${request.contextPath}/js/layer-slider/layerslider.transitions.js',
			'${request.contextPath}/js/layer-slider/layerslider.kreaturamedia.jquery.js',		
						
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
								
				$('#layerslider').layerSlider({
					skinsPath : '/styles/layer-slider/skins/',
					skin : 'fullwidth',
					thumbnailNavigation : 'hover',
					hoverPrevNext : true,
					responsive : true,
					responsiveUnder : 960,
					sublayerContainer : 960
		        });
		        				
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
												 
				if( 	kendo.support.touch && kendo.support.mobileOS ){
					$('.view.view-tenth').bind("click", function(e){ 
						$(this).find('a').click();
					});				
				}
											 							 
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

                    <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/sliders/layer/bg1.jpg" class="ls-bg" alt="Slide background">

                    <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/mockup/iphone1.png" alt="Slider Image" class="ls-s-1" style=" top:110px; left: 240px; slidedirection : left; slideoutdirection : bottom; durationin : 1500; durationout : 1500; ">

                    <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/mockup/iphone.png" alt="Slider image" class="ls-s-1" style=" top:60px; left: 40px; slidedirection : left; slideoutdirection : bottom; durationin : 2500; durationout : 2500; ">

                    <span class="ls-s-1" style=" text-transform: uppercase; line-height: 45px; font-size:35px; color:#fff; top:200px; left: 590px; slidedirection : top; slideoutdirection : bototm; durationin : 3500; durationout : 3500; ">
                        Fully Responsive <br> Bootstrap 3 Template
                    </span>

                    <a class="btn-u btn-u-orange ls-s1" href="#" style=" padding: 9px 20px; font-size:25px; top:340px; left: 590px; slidedirection : bottom; slideoutdirection : top; durationin : 3500; durationout : 3500; ">
                        Download Now
                    </a>
                </div>
                <!--End First Slide-->

                <!--Second Slide-->
                <div class="ls-layer" style="slidedirection: top; ">
                    <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/bg/5.jpg" class="ls-bg" alt="Slide background">

                    <i class="fa fa-chevron-circle-right ls-s-1" style=" color: #fff; font-size: 24px; top:70px; left: 40px; slidedirection : left; slideoutdirection : top; durationin : 1500; durationout : 500; "></i> 

                    <span class="ls-s-2" style=" color: #fff; font-weight: 200; font-size: 22px; top:70px; left: 70px; slidedirection : top; slideoutdirection : bottom; durationin : 1500; durationout : 500; ">
                        Fully Responsive and Easy to Customize
                    </span>

                    <i class="fa fa-chevron-circle-right ls-s-1" style=" color: #fff; font-size: 24px; top:120px; left: 40px; slidedirection : left; slideoutdirection : top; durationin : 2500; durationout : 1500; "></i> 

                    <span class="ls-s-2" style=" color: #fff; font-weight: 200; font-size: 22px; top:120px; left: 70px; slidedirection : top; slideoutdirection : bottom; durationin : 2500; durationout : 1500; ">
                        Revolution and Layer Slider Included 
                    </span>

                    <i class="fa fa-chevron-circle-right ls-s-1" style=" color: #fff; font-size: 24px; top:170px; left: 40px; slidedirection : left; slideoutdirection : top; durationin : 3500; durationout : 3500; "></i> 

                    <span class="ls-s-2" style=" color: #fff; font-weight: 200; font-size: 22px; top:170px; left: 70px; slidedirection : top; slideoutdirection : bottom; durationin : 3500; durationout : 2500; ">
                        1000+ Glyphicons Pro and Font Awesome Icons 
                    </span>

                    <i class="fa fa-chevron-circle-right ls-s-1" style=" color: #fff; font-size: 24px; top:220px; left: 40px; slidedirection : left; slideoutdirection : top; durationin : 4500; durationout : 3500; "></i> 

                    <span class="ls-s-2" style=" color: #fff; font-weight: 200; font-size: 22px; top:220px; left: 70px; slidedirection : top; slideoutdirection : bottom; durationin : 4500; durationout : 3500; ">
                        Revolution and Layer Slider Included 
                    </span>

                    <i class="fa fa-chevron-circle-right ls-s-1" style=" color: #fff; font-size: 24px; top:270px; left: 40px; slidedirection : left; slideoutdirection : top; durationin : 5500; durationout : 4500; "></i> 

                    <span class="ls-s-2" style=" color: #fff; font-weight: 200; font-size: 22px; top:270px; left: 70px; slidedirection : top; slideoutdirection : bottom; durationin : 5500; durationout : 4500; ">
                        60+ Template Pages and 20+ Plugins Included
                    </span>

                    <a class="btn-u btn-u-blue ls-s1" href="#" style=" padding: 9px 20px; font-size:25px; top:340px; left: 40px; slidedirection : bottom; slideoutdirection : bottom; durationin : 6500; durationout : 3500; ">
                        Twitter Bootstrap 3
                    </a>

                    <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/mockup/iphone1.png" alt="Slider Image" class="ls-s-1" style=" top:30px; left: 650px; slidedirection : right; slideoutdirection : bottom; durationin : 1500; durationout : 1500; ">
                </div>
                <!--End Second Slide-->

                <!--Third Slide-->
                <div class="ls-layer" style="slidedirection: right; transition2d: 92,93,105; ">
                    <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/sliders/layer/bg2.jpg" class="ls-bg" alt="Slide background">

                    <span class="ls-s-1" style=" color: #777; line-height:45px; font-weight: 200; font-size: 35px; top:100px; left: 50px; slidedirection : top; slideoutdirection : bottom; durationin : 1000; durationout : 1000; ">
                        Unify is Fully Responsive <br> Twitter Bootstrap 3 Template
                    </span>

                    <a class="btn-u btn-u-green ls-s-1" href="#" style=" padding: 9px 20px; font-size:25px; top:220px; left: 50px; slidedirection : bottom; slideoutdirection : bottom; durationin : 2000; durationout : 2000; ">
                        Find Out More
                    </a>

                    <img src="http://htmlstream.com/preview/unify-v1.4/assets/img/mockup/iphone.png" alt="Slider Image" class="ls-s-1" style=" top:30px; left: 670px; slidedirection : right; slideoutdirection : bottom; durationin : 3000; durationout : 3000; ">
                </div>
                <!--End Third Slide-->
            </div>         
        </div>
    </div><!--/layer_slider-->
    <!--=== End Slider ===-->
		<div class="container content">	
<!-- Service Links -->
        <div class="row margin-bottom-40">
            <div class="col-lg-12">
                <div class="headline"><h3>서비스 바로가기</h3></div>
                <ul class="bxslider recent-work">
                    <li>
                        <a href="#">
                            <em class="overflow-hidden"><img src="${request.contextPath}/download/image/151/family_astd_2014.PNG" alt="" /></em>
                            <span>
                                <strong>ASTD</strong>
                                <i>문의 : 02 2081 1092</i>
                            </span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <em class="overflow-hidden"><img src="${request.contextPath}/download/image/74/banner_icp.gif" alt="" /></em>
                            <span>
                                <strong>ICP</strong>
                                <i>문의 : 02 2081 1026</i>
                            </span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <em class="overflow-hidden"><img src="${request.contextPath}/download/image/73/banner_receipt.gif" alt="" /></em>
                            <span>
                                <strong>원천징수 영수증 발급</strong>
                                <i>문의 : 02 2081 1016</i>
                            </span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <em class="overflow-hidden"><img src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/4.jpg" alt="" /></em>
                            <span>
                                <strong>Food Truck Quinoa Nesciunt</strong>
                                <i>Craft labore wes anderson cred</i>
                            </span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <em class="overflow-hidden"><img src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/5.jpg" alt="" /></em>
                            <span>
                                <strong>Happy New Year</strong>
                                <i>Anim pariatur cliche reprehenderit</i>
                            </span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <em class="overflow-hidden"><img src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/6.jpg" alt="" /></em>
                            <span>
                                <strong>Award Winning Agency</strong>
                                <i>Responsive Bootstrap Template</i>
                            </span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <em class="overflow-hidden"><img src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/7.jpg" alt="" /></em>
                            <span>
                                <strong>Wolf Moon Officia</strong>
                                <i>Pariatur prehe cliche reprehrit</i>
                            </span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <em class="overflow-hidden"><img src="http://htmlstream.com/preview/unify-v1.4/assets/img/main/8.jpg" alt="" /></em>
                            <span>
                                <strong>Food Truck Quinoa Nesciunt</strong>
                                <i>Craft labore wes anderson cred</i>
                            </span>
                        </a>
                    </li>
                </ul>                    
            </div>
        </div><!--/row-->
        <!-- End Service Links  -->
<div class="row margin-bottom-30">
            <!-- Accordion-->
        	<div class="col-md-7">
    			<div class="headline"><h2>서비스 바로가기</h2></div>
                <div class="panel-group acc-v1" id="accordion">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                                    ASTD ICE 인키움 벤치마킹연수 
                                </a>
                            </h4>
                        </div>
                        <div id="collapseOne" class="panel-collapse collapse" style="height: 0px;">
                            <div class="panel-body">
                                <div class="row">
                                    <div class="col-md-4">
                                        <img class="img-responsive" src="${request.contextPath}/download/image/72/banner_astd.gif" alt="">
                                    </div>
                                    <div class="col-md-8">
                                        세계 최대 규모의 인적자원개발 협회 ASTD 벤치마킹연수를 통해  여러분과, 또 여러분의 조직을 한 단계 발전시킬 기회를 얻을 수 있습니다.
                                        </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">
                                    ICP 프레젠테이션 자격시험 
                                </a>
                            </h4>
                        </div>
                        <div id="collapseTwo" class="panel-collapse collapse" style="height: 0px;">
                            <div class="panel-body">
                            	 <a href="#" class="btn-u btn-u-blue pull-right">서비스 바로가기</a>
                                <div class="row">
                                    <div class="col-md-8">
                                   
                                        <p>개인의 발표 능력을 검증하는 전통의 프레젠테이션 자격시험</p>
                                        
                                        <ul class="list-unstyled who">
                                            <li><i class="fa fa-check color-green"></i>한국 직업능력개발원으로 부터 공인된 등록된 민간자격</li>
                                            <li><i class="fa fa-check color-green"></i>자격등록번호 : 제 2009-0247호</li>
                                            <li><a href="tel:+0220811026"><i class="fa fa-phone"></i>02-2081-1026</a></li>
                                        </ul>
                                    </div>
                                    <div class="col-md-4">
                                        <img class="img-responsive" src="${request.contextPath}/download/image/74/banner_icp.gif" alt="">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4 class="panel-title">
                                <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseThree">
                                   원천징수 영수증 출력
                                </a>
                            </h4>
                        </div>
                        <div id="collapseThree" class="panel-collapse collapse" style="height: 0px;">
                            <div class="panel-body">                             
								<a href="#" class="btn-u btn-u-blue pull-right">서비스 바로가기</a>
	                              <ul class="list-unstyled who margin-bottom-30">
	                              	<li>위탁 강의를 진행하신 강사분들을 위한 원천징수 영수증 출력을 위한 서비스입니다.</li>
	                                <li><a href="tel:+0220811004"><i class="fa fa-phone"></i>02-2081-1004</a></li>
				                 </ul>
                             </div>
                        </div>
                    </div>
                </div><!--/acc-v1-->
            </div><!--/col-md-7-->
            <!-- End Accordion-->

            <!-- Latest Shots -->
            <div class="col-md-5">
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
            </div><!--/col-md-5-->
            <!-- End Latest Shots -->
        </div>
        		
		</div><!-- /content -->	
		<!-- END MAIN CONTENT -->	
		</div><!-- /wrapper -->	
<!--=== Footer ===-->
<div class="footer bg-pomegranate">
	<div class="container layout">
		<div class="row">
			<div class="col-md-4">
				<div class="footer-links">				
					<div class="footer-links-body">
						<h4 class="text-center">공지 & 이벤트</h4>


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
			</div><!--/col-md-4-->  
			<div class="col-md-4">
				<div class="footer-notice">					
					<div class="footer-notice-body">
						<h4 class="text-center">공지 & 이벤트</h4>
						<ul  id="announce-view" class="list-unstyled summaries" style="min-height:100px;">						
							<li><small class="hex">등록된 뉴스가 없습니다.</small></li>
						</ul>				
						<a class="btn-u btn-u-red btn-u-xs" href="${request.contextPath}/events.do">더보기</a>	
					</div>		
						
				</div>
			</div><!--/col-md-4-->
			<div class="col-md-4">
				<div class="footer-news">					
					<div class="footer-news-body">
						<h4 class="text-center">뉴스</h4>
						<ul  id="announce-view" class="list-unstyled summaries" style="min-height:100px;">						
							<li><small class="hex">등록된 뉴스가 없습니다.</small></li>
						</ul>		
						<a class="btn-u btn-u-red btn-u-xs" href="${request.contextPath}/press.do">더보기</a>	
					</div>						
				</div>
			</div><!--/col-md-4-->
		</div><!--/row-->   
	</div><!--/container--> 
</div><!--/footer-->    
<!--=== End Footer ===-->
		
 		<!-- START FOOTER -->
		<#include "/html/common/common-homepage-footer.ftl" >		
		<!-- END FOOTER -->	
		
		<!-- START TEMPLATE -->
		<#include "/html/common/common-homepage-templates.ftl" >		
		<!-- END TEMPLATE -->
	</body>    
</html>
<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title><#if action.webSite ?? >${action.webSite.displayName }<#else>::</#if></title>
		<#compress>		
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/jquery.bxslider/jquery.bxslider.css">
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/layer-slider/layerslider.css" />
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/layer-slider/skins/fullwidth/skin.css" />
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/common/common.img-hover.css" />
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/common.pages/common.portfolio-v2.css" />
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/common.pages/common.onepage.css" />
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/font-awesome/4.1.0/font-awesome.min.css" />
		<link  rel="stylesheet" type="text/css"  href="${request.contextPath}/styles/common.themes/unify/themes/pomegranate.css" />
		<script type="text/javascript">
		<!--

		
		yepnope([{
			load: [
			'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',								
			'${request.contextPath}/js/kendo/kendo.web.min.js',			
			'${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js',			
			'${request.contextPath}/js/kendo.extension/kendo.ko_KR.js',								
			'${request.contextPath}/js/bootstrap/3.1.0/bootstrap.min.js',			
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',		
			
			'${request.contextPath}/js/jquery.bxslider/jquery.bxslider.min.js',		
				
			'${request.contextPath}/js/jquery.mixitup/jquery.mixitup.js',								
			'${request.contextPath}/js/jquery.easing/jquery.easing.1.3.js',
			'${request.contextPath}/js/layer-slider/jquery.transit.modified.js',
			'${request.contextPath}/js/layer-slider/layerslider.transitions.js',
			'${request.contextPath}/js/layer-slider/layerslider.kreaturamedia.jquery.js',
			'${request.contextPath}/js/common/common.models.js',
			'${request.contextPath}/js/common/common.api.js',
			'${request.contextPath}/js/common/common.ui.js'],
			complete: function() {

				common.ui.setup({
					features:{
						backstretch : false,
						lightbox : false,
						spmenu : false
					}
				});
						
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
					responsiveUnder : 990,
					sublayerContainer : 1170
		        });
		        				
		        $('.sorting-grid').mixItUp({
			        load: {
						filter: '.category-1'
					}
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
		/* Potopolio-Content
		------------------------------------*/
		.container.content {
			padding-top: 25px;
		}
		
		.sorting-block .sorting-nav {
			margin-bottom: 25px;
		}
		
		.sorting-block .sorting-nav-v1 li {
			font-weight: bold;
			font-style: normal;
		}
		
		</style>   
		</#compress>			
	</head>
	<body>
		<div class="page-loader"></div>
		<div class="wrapper">	
		<!-- START HEADER -->
		<#include "/html/common/common-homepage-menu.ftl" >	
		<!-- END HEADER -->	
		<!-- START MAIN CONTENT -->			
		<!-- content
		================================================== -->

<!--=== Slider ===-->
    <div class="layer_slider hidden-xs">
        <div id="layerslider-container-fw">        
            <div id="layerslider" style="width: 100%; height: 450px; margin: 0px auto; ">
                <!--First Slide-->
                <div class="ls-layer" data-ls="slidedelay:500;transition2d:25;">
                    <img src="http://img.inkium.com/homepage/slider/main_icap_bg.jpg" class="ls-bg" alt="Slide background">
					<span class="ls-s-1" style=" color: #fff; line-height:55px; font-weight: bold; font-size: 40px; top:80px; left: 10px; slidedirection : top; slideoutdirection : bottom; durationin : 1500; durationout : 1000; ">
                       성과지향형 역량진단 서비스<br> 
                       <span style="font-size:65px; font-weight:bold; "> iCAP </span>
                    </span>
					 
					<span class="ls-s-2" style=" color: #fff; padding: 9px 0px; font-size:14px;font-weight:bold; line-height:24px; top:190px; left: 10px; slidedirection : top; slideoutdirection : bottom; durationin : 2000; durationout : 2000; ">
					전략적 교육체계 수립! 성과 중심의 인재육성 가이드 제공! <br> 
					체계적인 인적자원의 개발 및 관리 지원! <br> 
					조직 및 개인의 경쟁력 강화! 성장비전 가시화! <br> 
					</span>
					<img src="http://img.inkium.com/homepage/slider/main_icap_02.png" alt="Slider Image" 
						class="ls-s-1" 
						style=" top:320px; left: 10px; slidedirection : bottom; slideoutdirection : bottom; durationin : 1500; durationout : 1500; ">
					 <img src="http://img.inkium.com/homepage/slider/main_icap_01.png" alt="Slider Image" class="ls-s-1" style=" top:0px; left: 450px; slidedirection : left; slideoutdirection : bottom; durationin : 1500; durationout : 1500; ">
               		 <img src="http://img.inkium.com/homepage/slider/main_O.png" alt="Slider Image" class="ls-s-1" style=" top:10px; left: 890px; slidedirection : right; slideoutdirection : bottom; durationin : 2000; durationout : 1500; ">
                </div>
                

                <!--End First Slide-->

                <!--Second Slide -->
                <div class="ls-layer" data-ls="transition2d:93;">
                    <img src="http://img.inkium.com/homepage/slider/main_consulting_bg.jpg" class="ls-bg" alt="Slide background">
					<span class="ls-s-1" style=" color: #fff; line-height:55px; font-weight: bold; font-size: 40px; top:80px; left: 10px; slidedirection : top; slideoutdirection : bottom; durationin : 1500; durationout : 1000; ">
                       <span style="font-size:65px; font-weight:bold; "> HRD 컨설팅 </span>
                    </span>					 
					<span class="ls-s-2" style=" color: #fff; padding: 9px 0px; font-size:14px;font-weight:bold; line-height:24px; top:150px; left: 10px; slidedirection : top; slideoutdirection : bottom; durationin : 2000; durationout : 2000; ">
					고객의 Performance Issue를 해결하기 위해<br> 
					다년간의 프로젝트 수행 노하우와 전문성을 기반으로 <br> 
					최적의 HRD Solution 제공<br> 
					</span>
					<img src="http://img.inkium.com/homepage/slider/main_consulting_03.png" alt="Slider Image" 
						class="ls-s-1" 
						style=" top:320px; left: 10px; slidedirection : bottom; slideoutdirection : bottom; durationin : 1500; durationout : 1500; ">
					<img src="http://img.inkium.com/homepage/slider/main_consulting_01" alt="Slider Image" 
						class="ls-s-1" 
						style=" top:0px; left: 300px; slidedirection : top; slideoutdirection : bottom; durationin : 2000; durationout : 1500; ">
               		 <img src="http://img.inkium.com/homepage/slider/main_consulting_02" alt="Slider Image" 
               		 	class="ls-s-2" 
               		 	style=" top:0px; left: 640px; slidedirection : left; slideoutdirection : right; durationin : 2500; durationout : 1500; ">
               		 <img src="http://img.inkium.com/homepage/slider/main_O.png" alt="Slider Image" class="ls-s-1" style=" top:10px; left: 890px; slidedirection : right; slideoutdirection : bottom; durationin : 2000; durationout : 1500; ">
                </div>
                <!--End Second Slide-->
                <!--Third Slide-->
                
                <div class="ls-layer" style="slidedirection: right; transition2d: 92,93,105">
                    <img src="http://img.inkium.com/homepage/slider/main_contents_bg.jpg" class="ls-bg" alt="Slide background">
					
		           <i class="fa fa-chevron-circle-right ls-s-1" style=" color: #fff; font-size: 24px; top:70px; left: 40px; slidedirection : left; slideoutdirection : top; durationin : 1500; durationout : 500; "></i>
		            <span class="ls-s-2" style=" color: #fff; font-weight: 200; font-size: 22px; top:70px; left: 70px; slidedirection : top; slideoutdirection : bottom; durationin : 1500; durationout : 500; ">
		                직업능력개발원 공식 민간 PT전문자격증!
		            </span>		
		            <i class="fa fa-chevron-circle-right ls-s-1" style=" color: #fff; font-size: 24px; top:120px; left: 40px; slidedirection : left; slideoutdirection : top; durationin : 2500; durationout : 1500; "></i> 		
		            <span class="ls-s-2" style=" color: #fff; font-weight: 200; font-size: 22px; top:120px; left: 70px; slidedirection : top; slideoutdirection : bottom; durationin : 2500; durationout : 1500; ">
		               대학생의 취업을 위한 발표 능력 UP!
		            </span>		
		            <i class="fa fa-chevron-circle-right ls-s-1" style=" color: #fff; font-size: 24px; top:170px; left: 40px; slidedirection : left; slideoutdirection : top; durationin : 3500; durationout : 3500; "></i>		
		            <span class="ls-s-2" style=" color: #fff; font-weight: 200; font-size: 22px; top:170px; left: 70px; slidedirection : top; slideoutdirection : bottom; durationin : 3500; durationout : 2500; ">
		               직장인의 보고능력, 수주를 위한 프레젠테이션 능력 UP!
		            </span>					
					
					<span class="ls-s-1" style=" color: #fff; line-height:55px; font-weight: bold; font-size: 28px; top:200px; left: 40px; slidedirection : top; slideoutdirection : bottom; durationin : 4500; durationout : 1000; ">
                      전문자격시험<br> 
                       <span style="font-size:65px; font-weight:bold; "> ICPP </span>
                    </span>
					<img src="http://img.inkium.com/homepage/slider/main_contents_03.png" alt="Slider Image" 
						class="ls-s-1" 
						style=" top:320px; left: 10px; slidedirection : bottom; slideoutdirection : bottom; durationin : 1000; durationout : 1500; ">					
					<img src="http://img.inkium.com/homepage/slider/main_contents_01.png" alt="Slider Image" 
						class="ls-s-1" 
						style=" top:0px; left: 400px; slidedirection : top; slideoutdirection : bottom; durationin : 1000; durationout : 1500; ">
               		 <img src="http://img.inkium.com/homepage/slider/main_contents_02.png" alt="Slider Image" 
               		 	class="ls-s-2" 
               		 	style=" top:200px; left: 650px; slidedirection : left; slideoutdirection : up; durationin : 1500; durationout : 1500; ">
               		 <img src="http://img.inkium.com/homepage/slider/main_O.png" alt="Slider Image" class="ls-s-1" style=" top:10px; left: 890px; slidedirection : right; slideoutdirection : bottom; durationin : 1000; durationout : 1500; ">
               		                		 
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
				<li class="filter hidden-xs" data-filter="all">전체</li>
				<li class="filter" data-filter=".category-1">기업서비스</li>
				<li class="filter" data-filter=".category-2">대학서비스</li>
				<li class="filter" data-filter=".category-3">자격사업</li>
			</ul>
			<ul class="row sorting-grid">
		    <#assign potopolioMenu = action.getWebSiteMenu("PHOTOPOLIO_MENU") />
			<#list potopolioMenu.components as item >
			    
				<li class="col-md-3 col-sm-6 col-xs-12 mix ${item.category}" data-cat="${item.data}" >
					<a href="<#if item.page?? >${item.page}<#else>#</#if >">
					<img class="img-responsive" src="${item.image}" alt="">
					<span class="sorting-cover">
						<span>${item.title}</span>
						<p></p>
					</span>
					</a>
				</li>
			
			</#list>		
			</ul>
        
            <div class="clearfix"></div>
			</div>
			<!-- End Portfolio Sorting Blocks -->
        	</div><!-- /content -->
        	
        		
			<!-- END MAIN CONTENT -->	
			<div class="one-page no-border">
				<div class="one-page-inner one-grey">				
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
			</div>
			</div>
	 		<!-- START FOOTER -->
			<#include "/html/common/common-homepage-footer.ftl" >		
		<!-- END FOOTER -->		
		</div><!-- /wrapper -->	
		<!-- START TEMPLATE -->
		<#include "/html/common/common-homepage-templates.ftl" >		
		<!-- END TEMPLATE -->
	</body>    
</html>
<#ftl encoding="UTF-8"/>
<html decorator="secure">
	<head>
		<title>::</title>
		<script type="text/javascript">
		<!--
			yepnope([{
				load: [			
				'css!${request.contextPath}/styles/kendo/kendo.metroblack.min.css',				
				'${request.contextPath}/js/jquery/1.9.1/jquery.min.js',
				'${request.contextPath}/js/foundation/foundation.min.js',
				'${request.contextPath}/js/foundation/foundation.dropdown.js',
				'${request.contextPath}/js/foundation/zepto.js',
				'${request.contextPath}/js/kendo/kendo.web.min.js',
				'${request.contextPath}/js/kendo/kendo.ko_KR.js',
				'${request.contextPath}/js/common/common.models.js' ],        	  	   
				complete: function() {      
					// START SCRIPT
					$(document).foundation();
					$("#menu").kendoMenu({
						orientation : 'vertical',
						direction : 'down',
						popupCollision: false,
						select: function(e){							
							var action = $(e.item).attr('action') ;
							if( action != '#' ){
								$("form[name='fm1']").attr("action", action ).submit(); 
							}
						}	
					}).css({
						width: "250px",
						marginRight: "0px"
					});					
					$("#container").show();
					// END SCRIPT            
				}
			}]);
		-->
		</script> 
		<style scoped="scoped">
			body {
				background-color: #0e0e0e;
			}	
		
		</style>   	
	</head>
	<body id="doc">
		<!-- START HEADER -->
		<header>			
		</header>		
		<!-- END HEADER -->
		<!-- START MAIN CONTENT --> 
		
		<section id="container" style="display:none">
			<div class="row layout big-box leftless rightless">
				<div class="small-6 columns" >		
					<a href="#" data-dropdown="drop1" class="k-button left">${ action.user.name}&nbsp;&nbsp;<span class="k-icon k-i-arrow-s"></span></a>
				</div>			
			</div>
			<div class="row layout">
				<div class="small-12 columns" >		
					<ul id="menu">
						<li action="${request.contextPath}/secure/main-company.do">회사관리</li>
						<li action="${request.contextPath}/secure/main-group.do">그룹관리</li>
						<li action="${request.contextPath}/secure/main-user.do">사용자관리</li>
						<li action="${request.contextPath}/secure/main-system.do">시스템 정보</li>
					</ul>						
				</div>
			</div>	
			<form name="fm1" method="POST" accept-charset="utf-8" class="details" target="parent">
				<input type="hidden" name="companyId" value="${ action.user.companyId}"/>
			</form>			
		</section>			
		<!-- END MAIN CONTENT -->
		<!-- START TEMPLATE -->
		<div id="drop1" data-dropdown-content class="f-dropdown">				
			<div class="row layout">
				<div class="small-12 columns">
					<div class="big-box">
				<#assign  props = action.user.properties >	
				<#if props.imageId ?? >					
					<img id="user-details-photo" src="${request.contextPath}/secure/view-image.do?width=100&height=150&imageId=${ props.imageId }" />
				<#else>					
					<img id="user-details-photo" src="http://placehold.it/100x150&amp;text=[No Photo]" />
				</#if>		
					</div>			
				</div>
			</div>
			<div class="row layout">
				<div class="small-12 columns">
					<div class="big-box" >
					<h4> ${action.user.name} </h4>
					<p style="font-color:000000;">
							${action.user.email}
					</p>
					</div>
				</div>
			</div>					
			<div class="row layout">		
				<div class="small-12 columns">					 
					<a class="k-button right" href="${request.contextPath}/logout" >로그아웃</a><div class="box right"></div><button class="k-button right">계정설정</button>
				</div>			
			</div>
		</div>
		<!-- END TEMPLATE -->
		<!-- START FOOTER -->
		<footer> 
		</footer>
		<!-- END FOOTER -->	
	</body>    
</html>
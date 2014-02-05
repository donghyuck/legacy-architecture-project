<#ftl encoding="UTF-8"/>
<html decorator="homepage">
<head>
		<title>기업소개</title>
		<script type="text/javascript">
		<!--
		yepnope([{
			load: [
			'css!${request.contextPath}/styles/font-awesome/4.0.3/font-awesome.min.css',
			'css!${request.contextPath}/styles/jquery.extension/component.css',
			'${request.contextPath}/js/jquery/1.9.1/jquery.min.js',
			'${request.contextPath}/js/jgrowl/jquery.jgrowl.min.js',
			'${request.contextPath}/js/kendo/kendo.web.min.js',
			'${request.contextPath}/js/kendo/kendo.ko_KR.js',			
			'${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js',		
			'${request.contextPath}/js/bootstrap/3.0.3/bootstrap.min.js',	
			'${request.contextPath}/js/common/common.models.min.js',
			'${request.contextPath}/js/common/common.ui.min.js',
			'${request.contextPath}/js/jquery.extension/modernizr.custom.js',
			'${request.contextPath}/js/jquery.extension/boxlayout.min.js',
			],
			complete: function() {
			
				// 1.  한글 지원을 위한 로케일 설정
				kendo.culture("ko-KR");
				      
				// START SCRIPT	
				$("#top-menu").kendoMenu();
				$("#top-menu").show();
				var currentUser = new User({});			
				// ACCOUNTS LOAD	
				var accounts = $("#account-panel").kendoAccounts({
					dropdown : false,
					authenticate : function( e ){
						currentUser = e.token;						
					},
					<#if CompanyUtils.isallowedSignIn(action.company) ||  !action.user.anonymous  >
					template : kendo.template($("#account-template").html()),
					</#if>
					afterAuthenticate : function(){
						//$('.dropdown-toggle').dropdown();
						if( currentUser.anonymous ){
							var validator = $("#login-panel").kendoValidator({validateOnBlur:false}).data("kendoValidator");							
							$("#login-btn").click(function() { 
								$("#login-status").html("");
								if( validator.validate() )
								{								
									accounts.login({
										data: $("form[name=login-form]").serialize(),
										success : function( response ) {
											$("form[name='login-form']")[0].reset();               
											$("form[name='login-form']").attr("action", "/main.do").submit();										
										},
										fail : function( response ) {  
											$("#login-password").val("").focus();												
											$("#login-status").kendoAlert({ 
												data : { message: "입력한 사용자 이름 또는 비밀번호가 잘못되었습니다." },
												close : function(){	
													$("#login-password").focus();										
												 }
											}); 										
										},		
										error : function( thrownError ) {
											$("form[name='login-form']")[0].reset();                    
											$("#login-status").kendoAlert({ data : { message: "잘못된 접근입니다." } }); 									
										}																
									});															
								}else{	}
							});	
						}
					}
				});				

//Boxlayout.init();

				// 1. Announces 				
				
				//var effect =  kendo.fx($("#announce-list-view-panel")).fadeOut().duration(700); 
				$("#announce-list-view").data( "announcePlaceHolder", new Announce () );	
				$("#announce-list-view").kendoListView({
					dataSource: new kendo.data.DataSource({
						transport: {
							read: {
								type : 'POST',
								dataType : "json", 
								url : '${request.contextPath}/community/list-announce.do?output=json'
							},
							parameterMap: function(options, operation) {
								if (operation != "read" && options.models) {
									return {models: kendo.stringify(options.models)};
								}
							} 
						},
						pageSize: 10,
						error:handleKendoAjaxError,				
						schema: {
							data : "targetAnnounces",
							model : Announce
						}
					}),
					selectable: "single",
					template: kendo.template($("#announce-list-view-template").html()),
					change: function(e) { 
						var data = this.dataSource.view() ;
						var selectedCell = data[this.select().index()];		
						$("#announce-list-view").data( "announcePlaceHolder", selectedCell );
						//effect.play();					
						displayAnnouncement();							
					},
					dataBound: function(e) {
						if( this.dataSource.data().length == 0 ){
						//	$("#announce-view-panel").html( 
						//		$('#alert-message-template').html() 
						//	);
						}							
						//this.select( this.element.children().first() );				
					}
				});
            			
				$("#announce-list-view-panel .panel-header-actions a").each(function( index ) {
						var panel_header_action = $(this);						
						if( panel_header_action.text() == "Minimize" ||  panel_header_action.text() == "Maximize" ){
							panel_header_action.click(function (e) {
								e.preventDefault();		
								$("#announce-list-view-panel .panel-body, #announce-list-view-panel .list-group ").toggleClass("hide");
								var panel_header_action_icon = panel_header_action.find('span');
								if( panel_header_action_icon.hasClass("k-i-minimize") ){
									panel_header_action.find('span').removeClass("k-i-minimize");
									panel_header_action.find('span').addClass("k-i-maximize");
								}else{
									panel_header_action.find('span').removeClass("k-i-maximize");
									panel_header_action.find('span').addClass("k-i-minimize");
								}								
							});
						} else if (panel_header_action.text() == "Refresh" ){
							panel_header_action.click(function (e) {
								e.preventDefault();		
								$("#announce-list-view").data( "kendoListView").refresh();
							});
						}
				} );											
				<#if !action.user.anonymous >				
				
				</#if>	
				// END SCRIPT            
			}
		}]);	
		
		function displayAnnouncement () {					
			var announcePlaceHolder = $("#announce-list-view").data( "announcePlaceHolder" );
			var template = kendo.template($('#announcement-detail-panel-template').html());			
			$("#announce-view-panel").html( template(announcePlaceHolder) );
			kendo.bind($("#announce-view-panel"), announcePlaceHolder );	
			
			$("#announce-view-panel").find(".close").click(function (e) {
				//$("#announce-view-panel").hide();
				//kendo.fx($("#announce-list-view-panel")).slideIn().duration(700).play(); 
				kendo.fx($("#announce-view-panel")).fade("out").duration(700).play();
				//$("#announce-view-panel").hide();
			});
		
			 kendo.fx($("#announce-view-panel")).fade("in").duration(700).play();			
			//$("#announce-view-panel").show();
			
		}				
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

		.k-listview div.k-state-selected{
			background: #ffffff;
			color: #ffffff; 
		}

		.k-listview:after
		{
			content: ".";
			display: block;
			height: 0;
			clear: both;
			visibility: hidden;
		}
		
		.k-listview
		{
			padding: 0;
			min-width: 690px;
			min-height: 100px;
		}
				
		#announce-list-view {
			padding: 0px;
			border: 0px;		
		}
				
		.announcement {
			cursor: pointer;
		}
							
		</style>   	
	</head>
	<body>
		<!-- START HEADER -->
		<#include "/html/common/common-homepage-menu.ftl" >	
		<#assign current_menu = action.findMenuComponent("USER_MENU", "MENU_1_2") />
		<header class="cloud">
			<div class="container">
				<div class="col-lg-12">	
					<h1>${ current_menu.title }</h1>
					<h4><i class="fa fa-quote-left"></i>&nbsp;모든 이벤트와 공지사항을 한눈에 ~! &nbsp;<i class="fa fa-quote-right"></i></h4>
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
				<!--
					<div id="bl-main" class="bl-main">
						<section id="bl-work-section">
							<div class="bl-box">
								<h2 class="bl-icon bl-icon-works">Works</h2>
							</div>
							<div class="bl-content">
								<h2>My Works</h2>
								<p>Mung bean maize dandelion sea lettuce catsear bunya nuts ricebean tatsoi caulie horseradish pea.</p>
								<ul id="bl-work-items">
									<li data-panel="panel-1"><a href="#"><img src="images/1.jpg" /></a></li>
									<li data-panel="panel-2"><a href="#"><img src="images/2.jpg" /></a></li>
									<li data-panel="panel-3"><a href="#"><img src="images/3.jpg" /></a></li>
									<li data-panel="panel-4"><a href="#"><img src="images/4.jpg" /></a></li>
								</ul>
								<p>Illustrations by <a href="http://dribbble.com/isaac317/click">Isaac Montemayor</a></p>
							</div>
							<span class="bl-icon bl-icon-close"></span>
						</section>					
						<div class="bl-panel-items" id="bl-panel-work-items">
							<div data-panel="panel-1">
								<div>
									<img src="images/1.jpg" />
									<h3>Fixie bespoke</h3>
									<p>Iphone artisan direct trade ethical Austin. Fixie bespoke banh mi ugh, deep v vinyl hashtag. Tumblr gentrify keffiyeh pop-up iphone twee biodiesel. Occupy american apparel freegan cliche. Mustache trust fund 8-bit jean shorts mumblecore thundercats. Pour-over small batch forage cray, banjo post-ironic flannel keffiyeh cred ethnic semiotics next level tousled fashion axe. Sustainable cardigan keytar fap bushwick bespoke.</p>
								</div>
							</div>
							<div data-panel="panel-2">
								<div>
									<img src="images/2.jpg" />
									<h3>Chillwave mustache</h3>
									<p>Squid vinyl scenester literally pug, hashtag tofu try-hard typewriter polaroid craft beer mlkshk cardigan photo booth PBR. Chillwave 90's gentrify american apparel carles disrupt. Pinterest semiotics single-origin coffee craft beer thundercats irony, tumblr bushwick intelligentsia pickled. Narwhal mustache godard master cleanse street art, occupy ugh selfies put a bird on it cray salvia four loko gluten-free shoreditch.</p>
								</div>
							</div>
							<div data-panel="panel-3">
								<div>
									<img src="images/3.jpg" />
									<h3>Austin hella</h3>
									<p>Ethical cray wayfarers leggings vice, seitan banksy small batch ethnic master cleanse. Pug chillwave etsy, scenester meh occupy blue bottle tousled blog tonx pinterest selvage mixtape swag cosby sweater. Synth tousled direct trade, hella Austin craft beer ugh chambray.</p>
								</div>
							</div>
							<div data-panel="panel-4">
								<div>
									<img src="images/4.jpg" />
									<h3>Brooklyn dreamcatcher</h3>
									<p>Fashion axe 90's pug fap. Blog wayfarers brooklyn dreamcatcher, bicycle rights retro YOLO. Wes anderson lomo 90's food truck 3 wolf moon, twee jean shorts. Iphone small batch twee wolf yr before they sold out. Brooklyn echo park cred, portland pug selvage flannel seitan tousled mcsweeney's.</p>
								</div>
							</div>
							
							<nav>
								<span class="bl-next-work">&gt; Next Project</span>
								<span class="bl-icon bl-icon-close"></span>
							</nav>
						</div>
					</div>
				-->
					<div id="event-area" class="bl-main bl-expand-item">						
						<section id="bl-list-section" class="bl-expand bl-expand-top">
							<div id="announce-list-view" class="bl-content">

							</div>
						</section>
						
						<div class="bl-content">
						<div id="announce-list-view-panel" class="panel panel-default borderless shadowless">
							<!--
							<div class="panel-heading">
								&nbsp;
								<div class="k-window-actions panel-header-actions">
									<a role="button" href="#" class="k-window-action k-link"><span role="presentation" class="k-icon k-i-refresh">Refresh</span></a>
									<a role="button" href="#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-minimize">Minimize</span></a>
									<a role="button" href="#" class="k-window-action k-link hide"><span role="presentation" class="k-icon k-i-maximize">Maximize</span></a>
								</div>			
							</div>
							-->
							<div class="panel-body" style="padding-top:25px;">
								
							</div>
						</div>						
						</div>
						<div id="announce-view-panel" style="display:none;"></div>
						
						
					</div>	
				</div>				
			</div>
		</div>									 			
		<!-- END MAIN CONTENT -->	
		<script id="announce-list-view-template" type="text/x-kendo-tmpl">
		<div class="announcement col-sm-6 col-md-4">
			<div class="thumbnail">
				<img src="http://fc00.deviantart.net/fs71/f/2010/190/8/2/Notice_by_kerokero13.jpg" alt="...">
				<div class="caption">
					<h5>#: subject #</h5>
					<p class="text-muted"><small>#: kendo.toString(startDate, "yyyy.MM.dd hh:mm") # ~  #: kendo.toString(endDate, "yyyy.MM.dd hh:mm") #</small></p>
				</div>
			</div>
		</div>
		</script>
		
		<script id="rowTemplate" type="text/x-kendo-tmpl">
			<tr>
				<td  class="text-center">#: announceId #</td>
				<td class="details">
					<span class="title">#: subject #</span><br>
					<small>기간 : #: kendo.toString(startDate, "yyyy.MM.dd hh:mm") # ~  #: kendo.toString(endDate, "yyyy.MM.dd hh:mm") #</small><br>
				</td>
			</tr>
		</script>
		
		<script id="alert-message-template" type="text/x-kendo-tmpl">
			<div class="alert alert-warning">새로운 공지 & 이벤트가 없습니다.</div>
		</script>				
 		<!-- START FOOTER -->
		<#include "/html/common/common-homepage-footer.ftl" >		
		<!-- END FOOTER -->	
		<!-- START TEMPLATE -->
		<#include "/html/common/common-homepage-templates.ftl" >		
		<!-- END TEMPLATE -->
	</body>    
</html>
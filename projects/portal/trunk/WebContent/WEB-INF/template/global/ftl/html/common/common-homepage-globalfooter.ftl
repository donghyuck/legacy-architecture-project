<!--=== Footer ===-->
<footer class="footer-global">
	<div class="footer">
		<div class="container">
			<#if action.webSite ?? >
				<#assign webSiteMenu = action.getWebSiteMenu(pageMenuName) />
				<#assign navigator = WebSiteUtils.getMenuComponent(webSiteMenu, pageMenuItemName) />				
			<!-- breadcombs -->
			<div class="row">
				<div class="col-sm-12">
							<div class="breadcrumb-v1">
								<ul class="breadcrumb">
									<li><a href="main.do"><i class="fa fa-home fa-lg"></i></a></li>
									<li><a href="">${navigator.parent.title}</a></li>
									<li class="active">${navigator.title}</li>
								</ul>
							</div>				
				</div>
			</div>
			<!--  /.breadcombs -->
			</#if>
			<div class="row">
				<!-- About -->
				<div class="col-md-3 md-margin-bottom-40">
					<div class="footer-logo">
						<img src="<@spring.url '/download/logo/company/${action.webSite.company.name}'/>" height="42"   alt="로고 이미지">
					</div>
					<p>${action.webSite.company.description}</p>					
				</div>
				<!-- End About -->
				<div class="col-md-3 md-margin-bottom-40">
					<div class="headline"><h3>최근 글</h3></div>
					<script type="text/javascript">
					<!--
							jobs.push( function(){
								var announcement = common.ui.datasource(
									'<@spring.url "/data/announce/list.json"/>',
									{
										schema: {
											data : "announces",
											model : common.ui.data.Announce,
											total : "totalCount"
										},
										pageSize: 5											
									}
								);
								var template = kendo.template($("#footer-notice-template").html());		
								announcement.bind('change', function(){
									if(this.view().length>0){
										$("#footer-announcement").html(kendo.render(template, this.view()))
									}
								}).read(); 					
							});
					-->
					</script>
					<ul id="footer-announcement" class="list-unstyled latest-list">
					</ul>	
				</div>
				<div class="col-md-3 md-margin-bottom-40">
					<div class="headline"><h3>링크</h3></div>
				</div>
				<div class="col-md-3 md-margin-bottom-40">
					<div class="headline"><h3>링크</h3></div>				
				</div>
			</div><!-- /.row -->
		</div><!-- /.container -->
	</div><!-- /.footer -->	
	<!-- copyright-->	
	<div class="copyright">
		<div class="container">
			<div class="row">
				<div class="col-md-12 col-sm-12">
					<div class="copyright-text">
						<#if action.webSite ?? >${.now?string("yyyy")} &copy; ${action.webSite.company.displayName }. 모든 권리 보유.<#else></#if>
						<a href="<@spring.url '/content.do?contentId=2'/>">개인정보 취급방침</a> | <a href="<@spring.url '/content.do?contentId=1'/>">이용약관</a>
					</div>
				</div>
			</div><!--/row--> 
		</div><!--/container--> 
	</div><!--/copyright--> 
</footer>
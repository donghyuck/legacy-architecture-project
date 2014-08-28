		<!--=== Footer ===-->
		<#assign websiteFooterMenu = action.getWebSiteMenu("FOOTER_MENU") />
		<footer>
			<div class="footer">
					<div class="container">
						<div class="footer-wrapper">
							<div class="row">
								<!-- Footer Col. -->
								<div class="col-md-3 col-sm-3 footer-col">
									<div class="footer-content">
										<div class="footer-content-logo">
											<img src="${request.contextPath}/download/logo/company/${action.webSite.company.name}" class="img-responsive"  alt="로고 이미지" />
										</div>
										<div class="footer-content-text">
												<ul class="list-unstyled">
												<li class="text-muted">사업자 등록번호 : 220-81-71637</li>
												<li class="text-muted">대표이사 : 조재천</li>
												<li class="text-muted">주소 : 서울특별시 구로구 디지털로 30길 31 (구로동) 코오롱빌란트2차 701호 ~ 704호</li>
												<li class="text-muted"> TEL: 02 555 1945 | FAX: 02 2081 1090</li>
											</ul>	
										</div>
									</div>
								</div>
								<!-- //Footer Col.// -->
								<!-- Footer Col. -->
								<div class="col-md-3 col-sm-3 footer-col">
									<div class="footer-title"></div>
									<div class="footer-content"></div>
								</div><!-- //Footer Col.// -->
								<!-- Footer Col. -->
								<div class="col-md-3 col-sm-3 footer-col">
									<#list websiteFooterMenu.components as item >
									<#if  item.components?has_content >
									<div class="footer-title no-margin-b">${item.title}</div>
									<div class="footer-content">		
									<ul class="list-unstyled">
										<#list item.components as sub_item >
										<li><a href="${sub_item.page}" target="${sub_item.target!"_self"} ">
										<#if sub_item.image??  >
											<img src="${sub_item.image}" class="img-circle" width="${sub_item.width!"100"}"/> ${sub_item.title}
										<#else>
											<i class="fa ${sub_item.icon!"fa-angle-right"}"></i> ${sub_item.title}
										 </#if >	</a></li>
										</#list>
									</ul>
									</div>
									</#if>
									</#list>	
								</div>
								<!-- //Footer Col.// -->
								<!-- Footer Col. -->
								<div class="col-md-3 col-sm-3 footer-col">
									<div class="footer-title no-margin-b">상품 문의</div>
									<div class="footer-content">				
									<p>문의전화 : 02 2081 1023</p>					
									<a class="btn-u btn-u-blue btn-block" href="#" onclick="window.open('http://www.wiznel.com/outer.do?method=getWithholdReceiptFrom','test','width=920 height=500 scrollbars=yes')" >원천징수 영수증 출력</a>
									
									<!--		
									<hr/>	
									<ul class="social-icons">
						               <li><a href="#" data-original-title="Facebook" class="social_facebook"></a></li>
						                <li><a href="#" data-original-title="Twitter" class="social_twitter"></a></li>
						            </ul> 		
						            -->							
									</div>
								</div>
								<!-- //Footer Col.// -->								
							</div>
						</div>
					</div>
				<div class="copyright">
					<div class="container">
						<div class="row">
							<div class="col-md-12 col-sm-12">
								<div class="copyright-text">
									<span class="text-danger pull-right hidden-xs">본 사이트의 콘텐츠는 저작권법의 보호를 받는바, 무단 전재, 복사, 배포 등을 금합니다</span>
									 <#if action.webSite ?? >${.now?string("yyyy")} &copy; ${action.webSite.company.displayName }. 모든 권리 보유.<#else></#if>
								</div>								
							</div>
						</div><!--/row--> 
					</div><!--/container--> 
				</div><!--/copyright--> 
			</div><!-- ./footer -->		
		</footer>
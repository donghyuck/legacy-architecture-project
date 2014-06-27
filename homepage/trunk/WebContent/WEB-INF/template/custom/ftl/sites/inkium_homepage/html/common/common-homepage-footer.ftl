		<!--=== Footer ===-->
		<#assign websiteFooterMenu = action.getWebSiteMenu("FOOTER_MENU") />
		<div class="footer bg-dark">
			<div class="container layout">
				<div class="row">
					<div class="col-md-8">
						<div class="row">
							<#list websiteFooterMenu.components as item >
							<#if  item.components?has_content >
							<div class="col-sm-4">
								<h2 class="heading-sm no-top-space"><strong>${item.title}  ${item.module} ${item.target} ${item.icon}</strong></h2>
								<ul class="list-unstyled">
									<#list item.components as sub_item >
									<li><a href="${sub_item.page}"><i class="fa fa-angle-right"></i> ${sub_item.title}</a></li>
									</#list>
								</ul>
							</div>
							</#if>
							</#list>
							<!--
							<div class="col-sm-4">
							<h3 class="heading-sm no-top-space"><strong>인재 육성 솔루션</strong></h3>
							<ul class="list-unstyled">
                                <li><i class="fa fa-angle-right"></i> 인키움 HRD 컨설팅</li>
                                <li><i class="fa fa-angle-right"></i> 인키움 HRD</li>
                                <li><i class="fa fa-angle-right"></i> iCAP</li>
                            </ul>							                            		
							</div>
							<div class="col-sm-4">
								<h3 class="heading-sm no-top-space"><strong>Family site</strong></h3>
								<ul class="list-unstyled">
									<li><a href="http://www.studymart.co.kr/" target="_blank"><i class="fa fa-angle-right"></i> 스터디마트</a></li>
									<li><a href="http://www.icpi.co.kr/" target="_blank"><i class="fa fa-angle-right"></i> ICP</a></li>
									<li><a href="http://www.astd.co.kr/" target="_blank"><i class="fa fa-angle-right"></i> ASTD</a></li>
								</ul>	
								<h3 class="heading-sm no-top-space"><strong>쇼셜 미디어</strong></h3>
								<ul class="social-icons">
			                        <li><a href="#" data-original-title="Facebook" class="social_facebook"></a></li>
			                        <li><a href="#" data-original-title="Twitter" class="social_twitter"></a></li>
			                   	</ul>                    
							</div>
							-->
						</div>
					</div><!--/col-md-8-->
					<div class="col-md-4">
					<!--
													<h3 class="heading-sm no-top-space"><strong>쇼셜 미디어</strong></h3>
								<ul class="social-icons">
			                        <li><a href="#" data-original-title="Facebook" class="social_facebook"></a></li>
			                        <li><a href="#" data-original-title="Twitter" class="social_twitter"></a></li>
			                   	</ul>-->
			                   	
							<a class="btn-u btn-u-blue btn-block" href="#" onclick="window.open('http://www.wiznel.com/outer.do?method=getWithholdReceiptFrom','test','width=920 height=500 scrollbars=yes')" ><i class="fa fa-print fa-lg"></i> 원천징수 영수증 출력 (02 2081 1016)</a>		
					<!--
						<div class="footer-links">					
							<div class="footer-links-body">
							<p>&nbsp;</p>
							<a class="btn-u btn-u-blue btn-block" href="${request.contextPath}/press.do">원천징수 영수증 출력 (02 2081 1016)</a>			
							<a class="btn-u btn-u-blue btn-block" href="${request.contextPath}/press.do">ASTD 벤치마킹연수 (02 2081 1092)</a>		
							<a class="btn-u btn-u-blue btn-block" href="${request.contextPath}/press.do">ICP 프레젠티이션 자격시험 (02 2081 1026)</a>		
							</div>						
						</div>
						-->

			                   							
					</div><!--/col-md-4-->
				</div><!--/row-->   
			</div><!--/container--> 
		</div><!--/footer-->    
		<!--=== End Footer ===-->		
<!--=== Copyright ===-->
<div class="copyright">
    <div class="container layout">
        <div class="row">
            <div class="col-md-9">
				<ul class="list-unstyled">
				    <li>서울특별시 구로구 디티털로 30길 31 (구로동) 코오롱빌란트2차 701호 ~ 704호 (주)인키움 | 대표이사 : 조재천 | TEL: 02 555 1965 | FAX: 02 2081 1090</li>
				    <li>공정거래위원회 고시 제2000-1호에 따른 사업자 등록번호 안내 220-81-71637</li>
				</ul>
                <p class="copyright-space">
                    <#if action.webSite ?? >${.now?string("yyyy")} &copy; ${action.webSite.company.displayName }. 모든 권리 보유.<#else></#if>
                </p>
            </div>
            <div class="col-md-3 hidden">  
                <a href="javascript:return void(0);">
                    <img src="${request.contextPath}/download/image/LQwoV8Jx4vXnEPGzIlAiYWuC19cqNKPlh60LDbLzvTja9m0UjX1ok2ap79apr5V1?width=80&height=80" class="pull-right" alt="2012 Best HRD 인증" />
                </a>
            </div>
        </div><!--/row-->
    </div><!--/container--> 
</div><!--/copyright--> 
<!--=== End Copyright ===-->
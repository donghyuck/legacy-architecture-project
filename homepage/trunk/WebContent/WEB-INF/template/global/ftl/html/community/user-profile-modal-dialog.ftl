<#ftl encoding="UTF-8"/>
<html decorator="none">
	<body>
		<script type="text/javascript">
		<!--
		
			$("#my-profile-dialog form button").each(function( index ) {
				var dialog_action = $(this);		
				dialog_action.click(function (e) {
					e.preventDefault();					
					if( $(this).hasClass("custom-modify") ){
						alert("update");
					}else if( $(this).hasClass("custom-password-change") ){ 
						alert("password");				
					}
				});
			});	

			$('#my-profile-tab a').click(function (e) {
				e.preventDefault();				
				if( $(this).attr('href') == '#profile-social-network' ){					
					if( !$("#my-social-network-grid" ).data('kendoGrid') ){
						$("#my-social-network-grid").kendoGrid({
							dataSource: common.api.social.dataSource({type:'all'}),
							selectable: "single",
							rowTemplate: kendo.template($("#social-network-grid-row-template").html()),
							change: function(e) { 				
								var selectedCells = this.select();
								if( selectedCells.length == 1){
									var selectedCell = this.dataItem( selectedCells );	    									
									$("#my-social-network-grid").data( "networkPlaceHolder", selectedCell );			
									if( selectedCell.connected ){				
										common.api.social.profile({
											media : selectedCell.serviceProviderName ,
											data: { socialNetworkId: selectedCell.socialAccountId },
											success : function(response){
												var myMediaAccountTemplate = kendo.template($('#my-social-network-account-details-template').html());			
												$("#my-social-network-account-details").html( myMediaAccountTemplate(response) );	
											},
											beforeSend : function() {
												kendo.ui.progress($("#my-social-network-account-details"), true);
											},
											complete : function(){
												kendo.ui.progress($("#my-social-network-account-details"), false);
											}												
										});
									}	
								}							
							},
							dataBound: function(e) {
								$("#my-social-network-grid button").each(function( index ) {
									var grid_action = $(this);									
									if( grid_action.hasClass("custom-social-network-connect") ){
										grid_action.click(function (e) {
											e.preventDefault();	
											var networkPlaceHolder = $("#my-social-network-grid").data( "networkPlaceHolder");
											goSocialPopup(networkPlaceHolder);
										});
									} else if( grid_action.hasClass("custom-social-network-disconnect") ){
										grid_action.click(function (e) {
											e.preventDefault();			
											var networkPlaceHolder = $("#my-social-network-grid").data( "networkPlaceHolder");
											alert( networkPlaceHolder.socialAccountId +  " 준비중입니다." ) ;									
										});									 	
									 }
								});
							},
							height: 300
						});								
					}				
				}
				$(this).tab('show')
			})		 	
						
			
			if(!$("#my-photo-upload").data("kendoUpload")){
				$("#my-photo-upload").kendoUpload({
					multiple : false,
					showFileList : false,
					localization:{ select : '사진변경' , dropFilesHere : '업로드할 이미지를 이곳에 끌어 놓으세요.' },
					async: {
						saveUrl:  '${request.contextPath}/community/update-my-photo.do?output=json',
						autoUpload: true
					},
					upload: function (e) {								         						    								    	 		    	 
					},
					success : function(e) {								    
						if( e.response.photo ){
							var _currentUser = $("#account-panel").data("currentUser" );
							$('#my-photo-image').attr( 'src', common.api.user.photoUrl( currentUser, 100, 150 ) );
						}				
					}	
				});
			}	
			
			// Popup window code
			function goSocialPopup(socialnetwork) {
				var target_url = "http://${ServletUtils.getLocalHostAddr()}/community/connect-socialnetwork.do?media=" + socialnetwork.serviceProviderName + "&domainName=" + document.domain + "&socialNetworkId=" + socialnetwork.socialAccountId ; 
				window.open( 
					target_url,
					'popUpWindow', 
					'height=500, width=600, left=10, top=10, resizable=yes, scrollbars=yes, toolbar=yes, menubar=no, location=no, directories=no, status=yes');					
			}
			
			function handleSocialCallbackResult( media, onetime, success ){
				if( success ){				
					if( $("#my-social-network-grid" ).data('kendoGrid') ){
						var my_social_network_grid = $("#my-social-network-grid" ).data('kendoGrid');
						var selectedCells = my_social_network_grid.select();
						if( selectedCells.length > 0){
							var selectedCell = my_social_network_grid.dataItem( selectedCells );
														
							common.api.social.update({
								data : {
									media: media,
									onetime: onetime,
									socialNetworkId: selectedCell.socialAccountId
								},
								success : function() {
									my_social_network_grid.dataSource.read();
								}							
							});
						}
					}
				}else{
					alert( "연결에 실패하였습니다." );
				}
			}
		-->
		</script>
		<style>			
		#my-profile-dialog .dropdown-menu {
			top: 120px;
			left: 50px; 
			padding : 20px;
			min-width:300px;
		}				
		</style>			
		
		<div id="my-profile-dialog">
			<div class="modal-content">			
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myProfileModalLabel">내정보</h4>
				</div>
				<div class="modal-body">
					<#if user.lastProfileUpdate?? >
					<div class="alert alert-danger block-space-10">							
						<i class="fa fa-info"></i> 마지막으로 <span data-bind="text: lastProfileUpdate">${ user.lastProfileUpdate }</span> 에 수정하였습니다.
					</div>				
					</#if>
					<p class="text-muted"><small><i class="fa fa-info"></i> 사진를 클릭하면 새로운 사진을 업로드 하실 수 있습니다.</small></p>
					<div class="media">
						<a class="pull-left dropdown-toggle" href="#" data-toggle="dropdown">
							<img id="my-photo-image" class="media-object img-thumbnail" src="${request.contextPath}/download/profile/${user.username}?width=100&height=150" />
						</a>
						<ul class="dropdown-menu">
							<li role="presentation" class="dropdown-header">마우스로 사진을 끌어 놓으세요.</li>
							<li>
								<input name="my-photo-upload" id="my-photo-upload" type="file" class="pull-right" />
							</li>
						</ul>									
						<div class="media-body">				
							<form class="form-horizontal" role="form">
								<fieldset disabled>
									<div class="form-group">
										<label class="col-sm-2 control-label"><small>아이디</small></label>
										<div class="col-sm-10">
											<h5 data-bind="text:username" >${ user.username }</h5>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label"><small>이름</small></label>
										<div class="col-sm-10">
											<input type="email" class="form-control" placeholder="이름" data-bind="value:name" value="${ user.name }"/>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label"><small>메일</small></label>
										<div class="col-sm-10">
											<input type="email" class="form-control" placeholder="메일" data-bind="value:email" value="${ user.email }"/>
										</div>
									</div>
									<div class="form-group">
										<div class="col-sm-offset-2 col-sm-10">
											<label class="checkbox-inline">
												<input type="checkbox" data-bind="checked: nameVisible" <#if user.nameVisible >checked="checked"</#if>> 이름 공걔
											</label>
											<label class="checkbox-inline">
												<input type="checkbox" data-bind="checked: emailVisible" <#if user.emailVisible >checked="checked"</#if>> 메일 공개
											</label>
										</div>
									</div>
								</fieldset>									
								<div class="form-group">
									<div class="col-sm-offset-2 col-sm-10">
										<div class="btn-group pull-right">	
											<button type="submit" class="btn btn-default custom-modify">기본정보변경</button>		
											<button type="submit" class="btn btn-primary custom-password-change">비밀번호 변경</button>				
										</div>							
									</div>
								</div>																	
							</form>
						</div>
					</div>
					<div class="blank-top-5" ></div>					
						<!-- Nav tabs -->
						<ul class="nav nav-tabs" id="my-profile-tab">
							<li class="active"><a href="#profile-basic-info" data-toggle="tab">기본정보</a></li>
							<li><a href="#profile-social-network" data-toggle="tab">쇼셜 네트워크</a></li>
							<li><a href="#profile-application-cfg" data-toggle="tab">응용프로그램 설정</a></li>
						</ul>
						<!-- Tab panes -->
						<div class="tab-content">
							<div class="tab-pane active" id="profile-basic-info">
								<div class="blank-top-5" ></div>					
								<table class="table  table-hover no-margin-bottom" >
									<tbody>
										<tr>
											<td><small>외부 계정</small></td>
											<td>${user.external?string("네", "아니오")}</td>
										</tr>									
										<tr>
											<td><small>회사</small></td>
											<td>${user.company.displayName} <small>(${user.company.description})</small></td>
										</tr>
										<tr>
											<td><small>그룹</small></td>
											<td>
												<#list groups as item >								
												<small><span class="label label-info" style="font-size:100%; font-weight:normal;">${item.displayName}</span></small>
												</#list>  										
											</td>
										</tr>																						
										<tr>
											<td><small>권한</small></td>
											<td>
												<#list roles as item >								
													<small><span class="label label-success" style="font-size:100%; font-weight:normal;"><i class="fa fa-key"></i> ${item}</span></small>						
												</#list>  										
											</td>
										</tr>		
										<tr>
											<td><small>마지막 로그인</small></td>
											<td><span class="text-muted data-bind="text: lastLoggedIn"><#if user.lastLoggedIn?? >${user.lastLoggedIn}</#if></span></td>
										</tr>																				
									</tbody>
								</table>								
							</div>
							<div class="tab-pane" id="profile-social-network">
								<div class="blank-top-5" ></div>					
								<div class="container" style="width:100%">
									<div class="row">			
										<div class="col-sm-5 leftless rightless">											
											<!-- start my social network grid -->	
											<table id="my-social-network-grid">
												<colgroup>
													<col/>
													<col/>
												</colgroup>
												<thead>
													<tr>
													<th>
													미디어
													</th>
													<th>
													상태
													</th>													
													</tr>
												</thead>
												<tbody>
													<tr>
														<td colspan="2"></td>
													</tr>
												</tbody>
											</table>	
											<!-- end my social network grid -->
										</div>										
										<div id="my-social-network-account-details" class="col-sm-7 rightless"></div>
									</div>
								</div>	
							</div>
							<div class="tab-pane" id="profile-application-cfg">
								<div class="blank-top-5" ></div>
								준비중입니다.
							</div>	
						</div>					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal">확인</button>
				</div>
			
			</div>
		</div>
		
		<!-- social network -->
		<script type="text/x-kendo-template" id="my-social-network-account-details-template">
		<div class="panel panel-default margin-buttom-5">
			<!--<div class="panel-heading"><i class="fa fa-user"></i></div>-->
			<div class="panel-body" style="padding:10px;">				
				#if ( typeof (twitterProfile)  == "object" ){ #
				<div class="media">
					<a class="pull-left" href="\\#"><img class="media-object" src="#=twitterProfile.profileImageUrl#" alt="프로파일 이미지" class="img-rounded"></a>
					<div class="media-body">
						<h5 class="media-heading">#=twitterProfile.screenName# (#=twitterProfile.name#)</h5>
						#=twitterProfile.description#</br>
						</br>
						트위터 URL : #=twitterProfile.profileUrl#</br>
						표준시간대: #=twitterProfile.timeZone#</br>	
						웹 사이트: #=twitterProfile.url#</br>	
						언어: #=twitterProfile.language#</br>	
						위치: #=twitterProfile.location#</br>	
					</div>			
				</div>
				</br>
				<ul class="list-group">
					<li class="list-group-item">
					<span class="badge">#=twitterProfile.statusesCount#</span>
					트윗
					</li>
					<li class="list-group-item">
					<span class="badge">#=twitterProfile.friendsCount#</span>
					팔로잉
					</li>
					<li class="list-group-item">
					<span class="badge">#=twitterProfile.followersCount#</span>
					팔로워
					</li>		
				</ul>			
				# } else if ( typeof (facebookProfile)  == "object" ) { #
				<div class="media">
					<a class="pull-left" href="\\#"><img class="media-object" src="http://graph.facebook.com/#=facebookProfile.id#/picture" alt="프로파일 이미지" class="img-rounded"></a>
					<div class="media-body">
						<h5 class="media-heading">
						#=facebookProfile.name# (#=facebookProfile.firstName#, #=facebookProfile.lastName#)
						</h5>
						</br>
						URL : #=facebookProfile.link#</br>
						로케일 : #=facebookProfile.locale#</br>
						# if ( typeof (facebookProfile.location)  == "object" ) { #
						위치 : #=facebookProfile.location.name#</br>
						# } #
					</div>		
				</div>						
				# } else if ( typeof (tumblrProfile)  == "object" ) { #
				<ul class="media-list">
					<li class="media">					
						<div class="media-body">
							<h5 class="media-heading">
							#=tumblrProfile.name#
							</h5>
							<p>팔로잉 : #=tumblrProfile.following#, 좋아요 : #=tumblrProfile.likes#</p>
							# for (var i = 0; i < tumblrProfile.blogs.length ; i++) { #												
							# var blog = tumblrProfile.blogs[i] ; #	
							<div class="media">
							<!--
				            <a class="pull-left" href="\\#">
				              <img class="media-object" data-src="holder.js/64x64" alt="Generic placeholder image">
				            </a>
				            -->
				            <div class="media-body">
				              <!--<h4 class="media-heading">Nested media heading</h4>-->
				              #=blog.url #
				            </div>
				          </div>							
						</div>	
						# } #	
					</li>	
					
				</ul>											
				# } else if ( typeof (error)  == "object" ) { #
				
				# } #
			</div>
		</div>				
		</script>
		<script type="text/x-kendo-template" id="social-network-grid-row-template">
			<tr>
				<td><i class="fa fa-#: serviceProviderName#"></i>&nbsp; #: serviceProviderName#</td>
				<td>
				#if ( ! connected  ) { # 
				<button type="button" class="btn btn-info custom-social-network-connect btn-sm" data-url="#:authorizationUrl#" >연결</button>  
				# } else { # 
				<div class="btn-group">
					<button type="button" class="btn btn-danger custom-social-network-disconnect btn-sm">연결 취소</button>  
				</div>
				# }  #  				
				</td>
			</tr>					
		</script>			
	</body> 
</html>
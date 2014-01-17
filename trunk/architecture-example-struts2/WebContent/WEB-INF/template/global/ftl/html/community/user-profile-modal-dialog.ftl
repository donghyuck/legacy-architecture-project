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
					if( !$("#my-social-network-list-view" ).data('kendoListView') ){
						$("#my-social-network-list-view").kendoListView({
							dataSource: new kendo.data.DataSource({
								transport: {
									read: {
										type : 'POST',
										dataType : "json", 
										url : '${request.contextPath}/community/list-socialnetwork.do?output=json'
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
									data : "socialNetworks",
									model : SocialNetwork
								}
							}),
							selectable: "single",
							template: kendo.template($("#social-network-list-view-template").html()),
							change: function(e) { 
								var data = this.dataSource.view() ;
								var selectedCell = data[this.select().index()];					
							},
							dataBound: function(e) {				
								this.select( this.element.children().first() );									
								$("#my-social-network-list-view button").each(function( index ) {
									var list_view_action = $(this);
									if( list_view_action.hasClass("custom-social-network-connect") ){
										list_view_action.click(function (e) {
											e.preventDefault();	
											goSocialPopup(list_view_action.attr("data-url"));
										});
									}else if( list_view_action.hasClass("custom-social-network-disconnect") ){
										list_view_action.click(function (e) {
											e.preventDefault();												
										});
									}else if ( list_view_action.hasClass("custom-social-network-account") ){ 
										var _myMediaId = list_view_action.attr("data-id");
										var _myMedia = list_view_action.attr("data-media");
										
										list_view_action.click(function (e) {
											$.ajax({
												type : 'POST',
												url : "${request.contextPath}/community/get-" + _myMedia + "-profile.do?output=json",
												data: { socialNetworkId: _myMediaId },
												success : function(response){
													if( response.error ){
															// 연결실패.
													} else {	
														var myMediaAccountTemplate = kendo.template($('#my-social-network-account-details-template').html());			
														$("#my-social-network-account-details").html( myMediaAccountTemplate(response) );											
														//alert( kendo.stringify(response) );		
													}
												},
												error:handleKendoAjaxError													
											});	
										});
									
									}
								});	
							}
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
						var imageId = -1;
						var _currentUser = $("#account-panel").data("currentUser" );
						if( _currentUser.properties.imageId ){
							imageId = _currentUser.properties.imageId
						}
						e.data = { userId: _currentUser.userId , imageId:imageId  };									    								    	 		    	 
					},
					success : function(e) {								    
						if( e.response.photo ){
							var _currentUser = $("#account-panel").data("currentUser" );
							_currentUser.properties.imageId = e.response.photo.imageId;
							var photoUrl = '${request.contextPath}/accounts/view-image.do?width=100&height=150&imageId=' + _currentUser.properties.imageId ;
							$('#my-photo-image').attr( 'src', photoUrl );
						}				
					}	
				});
			}

		// Popup window code
		function goSocialPopup(url) {
			popupWindow = window.open(
				url,'popUpWindow','height=500,width=600,left=10,top=10,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no,status=yes')
		}
		
		function handleSocialCallbackResult( success ){
			if( success ){

			}else{
				alert( "연결에 실패하였습니다." );
			}
				if($("#my-photo-upload").data("kendoUpload")){
					$("#my-photo-upload").data("kendoUpload").dataSource.read();
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
		#my-social-network-list-view .k-listview div.k-state-selected{
			background: #ffffff;
			color: #ffffff; 
		}

		#my-social-network-list-view .k-listview:after
		{
			content: ".";
			display: block;
			height: 0;
			clear: both;
			visibility: hidden;
		}
		
		#my-social-network-list-view .k-listview
		{
			padding: 0;
			min-width: 690px;
			min-height: 100px;
		}
				
		
		#my-social-network-list-view {
			padding: 0px;
			border: 0px;		
		}
				
		</style>			
		<div id="my-profile-dialog" class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">내정보</h4>
				</div>
				<div class="modal-body">
					<div class="media">
						<a class="pull-left dropdown-toggle" href="#" data-toggle="dropdown">
							<#if user.properties.imageId??>
							<img id="my-photo-image" class="media-object img-thumbnail" src="${request.contextPath}/accounts/view-image.do?width=100&height=150&imageId=${user.properties.imageId}"," />
							<#else> 
							<img id="my-photo-image" class="media-object img-thumbnail" src="http://placehold.it/100x150&amp;text=[No Photo]" />
							</#if>  
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
										<label class="col-sm-2 control-label">아이디</label>
										<div class="col-sm-10">
											<h5 data-bind="text:username" >${ user.username }</h5>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">이름</label>
										<div class="col-sm-10">
											<input type="email" class="form-control" placeholder="이름" data-bind="value:name" value="${ user.name }"/>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">메일</label>
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
					<div class="alert alert-danger no-margin-bottom block-space-10">								
						<i class="fa fa-info"></i> 마지막으로 <span data-bind="text: lastProfileUpdate">${user.lastProfileUpdate}</span> 에 수정하였습니다. 사진를 클릭하면 새로운 사진을 업로드 하실 수 있습니다. 
					</div>
					<div class="blank-top-5" ></div>					
						<!-- Nav tabs -->
						<ul class="nav nav-tabs" id="my-profile-tab">
							<li class="active"><a href="#profile-basic-info" data-toggle="tab">기본정보</a></li>
							<li><a href="#profile-social-network" data-toggle="tab">쇼셜 네트워크</a></li>
						</ul>
						<!-- Tab panes -->
						<div class="tab-content">
							<div class="tab-pane active" id="profile-basic-info">
								<div class="blank-top-5" ></div>					
								<table class="table  table-hover no-margin-bottom" >
									<tbody>
										<tr>
											<td>회사</td>
											<td>${company.displayName}<small>(${company.description})</small></td>
										</tr>
										<tr>
											<td>외부 계정</td>
											<td>${user.external?string("네", "아니오")}</td>
										</tr>
										<tr>
											<td>그룹</td>
											<td>
												<#list groups as item >								
												<span class="label label-info" style="font-size:100%; font-weight:normal;"><i class="fa fa-folder-o"></i> ${item.displayName}</span>
												</#list>  										
											</td>
										</tr>																						
										<tr>
											<td>권한</td>
											<td>
												<#list roles as item >								
													<span class="label label-success" style="font-size:100%; font-weight:normal;"><i class="fa fa-key"></i> ${item}</span>						
												</#list>  										
											</td>
										</tr>		
										<tr>
											<td>마지막 로그인</td>
											<td><span class="text-muted data-bind="text: lastLoggedIn">${user.lastLoggedIn}</span></td>
										</tr>																				
									</tbody>
								</table>								
							</div>
							<div class="tab-pane" id="profile-social-network">
								<div class="blank-top-5" ></div>	
								<div id="my-social-network-account-details"></div>
								<div id="my-social-network-list-view"></div>						
							</div>
						</div>					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal">확인</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal-dialog -->	

		
		<!-- social network -->
		<script type="text/x-kendo-template" id="my-social-network-account-details-template">
			<div class="alert alert-warning alert-dismissable margin-buttom-5">
				<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>				
				#if ( typeof (twitterProfile)  == "object" ){ #
				<div class="media">
					<a class="pull-left" href="\\#"><img class="media-object" src="#=twitterProfile.profileImageUrl#" alt="프로파일 이미지" class="img-rounded"></a>
					<div class="media-body">
						<h4 class="media-heading">#=twitterProfile.screenName# (#=twitterProfile.name#)</h4>
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
						<h4 class="media-heading">#=facebookProfile.name# (#=facebookProfile.firstName#, #=facebookProfile.lastName#)</h4>
						</br>
						URL : #=facebookProfile.link#</br>
						로케일 : #=facebookProfile.locale#</br>
						위치 : #=facebookProfile.location.name#</br>
					</div>		
				</div>								
				# } else if ( typeof (error)  == "object" ) { #
				
				# } #
			</div>			
		</script>
		<script type="text/x-kendo-template" id="social-network-list-view-template">
			<div class="panel panel-default margin-buttom-5">
				<div class="panel-heading"><i class="fa fa-#: serviceProviderName#"></i>&nbsp; #: serviceProviderName#</div>	
				<div class="panel-body" style="padding:10px;">				
				#if ( ! connected  ) { # 
				<button type="button" class="btn btn-default custom-social-network-connect" data-url="#:authorizationUrl#" >연결</button>  
				# } else { # 
				<div class="btn-group">
				<button type="button" class="btn btn-info custom-social-network-account" data-media="#: serviceProviderName#" data-account="#:socialAccountId#"><i class="fa fa-user"></i></button><button type="button" class="btn btn-danger custom-social-network-disconnect">연결 취소</button>  
				</div>
				# }  #  
				</div>
			</div>
		</script>	
	</body> 
</html>
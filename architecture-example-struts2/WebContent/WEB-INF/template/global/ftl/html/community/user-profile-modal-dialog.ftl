<#ftl encoding="UTF-8"/>
<html decorator="none">
	<body>
		<script type="text/javascript">
		<!--
			$("#my-profile-dialog form  button").each(function( index ) {
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
				e.preventDefault()
				alert($(this).attr('href'));
				
				if( $(this).attr('href') == '#profile-social-network' ){					
					if( !$("#my-social-network-list-view" ) ){
						$("#my-social-network-list-view").kendoListView({
							dataSource: new kendo.data.DataSource({
								transport: {
									read: {
										type : 'POST',
										dataType : "json", 
										url : '${request.contextPath}/community/socialnetwork-list.do?output=json'
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
								//$("#announce-list-view").data( "announcePlaceHolder", selectedCell )										
								//showAnnounce();							
							},
							dataBound: function(e) {
								if( this.dataSource.data().length == 0 ){
								//	$("#announce-view-panel").html( 
								//		$('#alert-message-template').html() 
								//	);
								}							
								this.select( this.element.children().first() );				
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
								<div id="my-social-network-list-view"></div>
							</div>
						</div>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal">확인</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal-dialog -->	


	</body> 
</html>
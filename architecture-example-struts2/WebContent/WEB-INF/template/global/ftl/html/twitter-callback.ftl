<#ftl encoding="UTF-8"/>
<html decorator="secure-metro">
	<head>
		<title>트위터 인증이 성공하였습니다.</title>
		<script type="text/javascript"> 
        yepnope([{
            load: [ 
			'${request.contextPath}/js/jquery/1.10.2/jquery.min.js',
			'${request.contextPath}/js/bootstrap/3.0.0/bootstrap.min.js',
       	    '${request.contextPath}/js/kendo/kendo.web.min.js',
       	    '${request.contextPath}/js/kendo/kendo.ko_KR.js',       	   
       	    '${request.contextPath}/js/kendo/cultures/kendo.culture.ko-KR.min.js', 
       	    '${request.contextPath}/js/common/common.models.js',
       	    '${request.contextPath}/js/common/common.apis.js',
       	    '${request.contextPath}/js/common/common.ui.js'], 
            complete: function() {               
				
				var oauth_token = '${ action.oauth_token }';
				var oauth_verifier = '${ action.oauth_verifier }';				
				var socialAccountId = window.opener.$("#connect-social-id").val();
				var grid = window.opener.$("#social-grid").data("kendoGrid");
				
				if( typeof (grid) == 'object' ){								
					$.ajax({
						type : 'POST',
						url : '${request.contextPath}/secure/update-twitter-social-account.do?output=json',
						data:{ 
							socialAccountId: socialAccountId, 
							item: kendo.stringify( { oauth_verifier: oauth_verifier, oauth_token: oauth_token }) 
						},
						success : function(response){
							$('.progress').hide();
							if( response.error ){							
							
							} else {
								
								var template = kendo.template($('#twitter-profile-template').html());
								$('#twitter-profile').html( template(response) );
							}
							
							$( ":button" ).click( function(e){
								grid.dataSource.read();
								window.close();
							});
								
						},
						error:handleKendoAjaxError,
						dataType : 'json'
					});	
				}				
			}	
		}]);
		</script>		
	</head>
	<body>	
		<div class="container layout">
			<div class="row blank-top-15">			
				<div class="col-12 col-lg-12">						
					<div class="panel panel-default">
						<div class="panel-body">
							<img src="${request.contextPath}/images/common/twitter-bird-light-bgs.png" alt="Twitter Logo" class="img-rounded">	
							<div class="progress progress-striped active">
								<div class="progress-bar"  role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width: 100%">
								</div>
							</div>
							<div id="twitter-profile" ></div>			
						</div>
					</div>							
				</div>
			</div>
		</div>
		
		<script id="twitter-profile-template" type="text/x-kendo-template">				
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
				<button id="close-btn" type="button" class="btn btn-primary btn-block">확인</button>				
				# } #
				
		</script>					
	</body>
</html>
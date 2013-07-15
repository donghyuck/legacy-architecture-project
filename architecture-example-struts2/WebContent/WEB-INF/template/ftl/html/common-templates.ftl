		<script id="account-status-template" type="text/x-kendo-template">
			<div class="panel-header"> 	
				<div class="row">
					<div class="small-12 columns">
						<ul class="button-group">
						  <li><a href="\\#" class="small button">Button 1</a></li>
						  <li><a href="\\#" class="small button">Button 2</a></li>
						  <li><a href="\\#" class="small button">Button 3</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="panel-body"> 	
				<div class="row layout">
					<div class="small-4 columns">
						<div class="big-box">
							<img id="user-details-photo" src="#: photoUrl #" />
						</div>							
					</div>
					<div class="small-8 columns">
						<div class="box">
						<h4>#: name #</h4>
						#if (isSystem ) {#
						<span class="round alert label">시스템</span>
						# } #
						<p style="font-color:000000;">
							#: email #
						</p>					
						</div>							
					</div>								
				</div>			
			</div>
			<div class="panel-tailer"> 		
				<div class="row layout">
					<div class="small-12 columns">		
						<a class="k-button">계정설정&nbsp;<span class="k-icon k-i-custom"></span></a>
						<a class="k-button" href="${request.contextPath}/logout" >로그아웃</a>			
					</div>
				</div>
			</div>
		</script>
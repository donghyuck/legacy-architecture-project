<#ftl encoding="UTF-8"/>
<html decorator="none">
	<body>
		<script type="text/javascript">
		<!--
			$("#my-profile-dialog a").each(function( index ) {
				var dialog_action = $(this);		
				dialog_action.click(function (e) {
					e.preventDefault();		
					alert( $(this).html() );
				});
			});			
		-->
		</script>
			
		<div id="my-profile-dialog" class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">${user.name}</h4>
				</div>
				<div class="modal-body">
					<div class="media">
						<a class="pull-left" href="#">
							<#if user.properties.imageId??>
							<img class="media-object img-thumbnail" src="PHOTO_URL = "/accounts/view-image.do?width=100&height=150&imageId=${user.properties.imageId}"," />
							<#else> 
							<img class="media-object img-thumbnail" src="http://placehold.it/100x150&amp;text=[No Photo]" />
							</#if>  
						</a>
						<div class="media-body">
							<h4 class="media-heading">${ user.username }</h4>
							<table class="table">
								<tbody>
									<tr>
										<th class="col-lg-3 col-sm-4">아이디</th>
										<td><input type="text" class="form-control" placeholder="아이디" disabled data-bind="value:username"/></td>
									</tr> 
								</tbody>
							</table>		
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
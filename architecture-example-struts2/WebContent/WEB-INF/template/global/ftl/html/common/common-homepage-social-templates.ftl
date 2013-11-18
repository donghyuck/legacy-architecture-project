		<script type="text/x-kendo-tmpl" id="facebook-homefeed-template">
		#if (type != "STATUS") {#
		<li class="media">
		    <a class="pull-left" href="\\#">
		    	<img class="media-object" src="http://graph.facebook.com/#=from.id#/picture" alt="프로파일 이미지">
		    </a>
		    <div class="media-body">
		      <h5 class="media-heading">#: from.name # (#: kendo.toString(updatedTime, "D") #)</h5>		     	
		     	<span class="label label-primary">#: type #</span>		     	
		     	#if ( typeof( message ) == 'string'  ) { #
		     	<br>
		     	#: message #
		     	# } #				     	     	
		     	#if ( name !=null ) { #
		     	<br>#: name  #
		     	# } #		     	     	
		     	#if ( type == 'LINK' ) { #
		     	<br>
		     	<span class="glyphicon glyphicon-link"></span>&nbsp;<a href="#: link #">#: link #</a>
		     	# } else if ( type == 'PHOTO' ) { #
		     		<br>
		     		<img src="#: picture.replace("_s.", "_n.")  #" alt="media" class="img-rounded img-responsive">
		     	# } else { #		     	
		     		#if ( picture !=null ) { #
		     		<br><img src="#: picture.replace("_s.", "_n.")  #" alt="media" class="img-rounded img-responsive">
		     		# } #		     		
		     		#if ( source !=null ) { #
		     		<br>source : <span class="glyphicon glyphicon-link"></span>&nbsp;<a href="#: source #">#: source #</a>
		     		# } #
		     	# } #
		     	
		     	#if ( typeof( caption ) == 'string'  ) { #
		     	<br><br>
		     	<blockquote>
		     	<p>#: caption #</p>
				</blockquote>
		     	# } #			     	
		     	
		     	#if ( typeof( description ) == 'string'  ) { #
		     	<blockquote><p class="text-muted"><small>
		     	#: description #
		     	</small></p>
		     	</blockquote>
		     	# } #		
		     							
				# for (var i = 0; i < comments.length ; i++) { #												
				# var comment = comments[i] ; #							
					<div class="media">
						<a class="pull-left" href="\\#">
							<img class="media-object" src="http://graph.facebook.com/#=comment.from.id#/picture" alt="프로파일 이미지" class="img-circle">
						</a>	
						<div class="media-body">
							 <h6 class="media-heading">#: comment.from.name # &nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-thumbs-up"></span>#:comment.likesCount#</h6>
							 <p class="text-muted">#:comment.message#</p>
						</div>				
					</div>				
				# } #
			</div>
		</li>					
		# } #  	
		</script>		
		<script type="text/x-kendo-tmpl" id="twitter-timeline-template">
		<li class="media">
		    <a class="pull-left" href="\\#">
		      <img src="#: user.profileImageUrl #" alt="#: user.name#" class="media-object">
		    </a>
		    <div class="media-body">
		      <h4 class="media-heading">#: user.name # (#: kendo.toString(createdAt, "D") #)</h4>
		     	#: text #      	
							# for (var i = 0; i < entities.urls.length ; i++) { #					
							# var url = entities.urls[i] ; #		
							<br><span class="glyphicon glyphicon-link"></span>&nbsp;<a href="#: url.expandedUrl  #">#: url.displayUrl #</a>
							 # } #	
							<p>
							# for (var i = 0; i < entities.media.length ; i++) { #					
							# var media = entities.media[i] ; #					
							<img src="#: media.mediaUrl #" width="100%" alt="media" class="img-rounded">
							# } #
							</p>
							#if (retweeted) {#					
						<div class="media">
							<a class="pull-left" href="\\#">
								<img src="#: retweetedStatus.user.profileImageUrl #" width="100%" alt="media" class="img-rounded">
							</a>
							<div class="media-body">
								<h4 class="media-heading">#: retweetedStatus.user.name #</h4>
							</div>
						</div>						
							# } #
		    </div>
		  </li>					
		</script>
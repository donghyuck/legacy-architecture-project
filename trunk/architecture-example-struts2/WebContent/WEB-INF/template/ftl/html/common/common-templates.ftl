		<script id="account-template" type="text/x-kendo-template">
			<a id="account-button" data-dropdown="account-dropdown" href="\\#">
				<span class="avatar-container">				
					#if (photoUrl != null && photoUrl != 'null' && photoUrl != '')  { #
					<span class="avatar-wrapper loaded"><img src="#:photoUrl#" /></span>
					# } else { #
					<span class="avatar-wrapper"><img class="loading"></span>
					# } #
				</span>
				<i></i>
			</a>
			<nav id="account-dropdown" data-dropdown-content >											
				<ul>
					<li class="text">이름 : #:name #</li>
					<li class="text">메일 : #:email #</li>
					<li class="video"><a href="\\#/welcome"><span class="icon"></span>Video Tour</a></li>
					<li class="settings"><a href="\\#"><span class="icon"></span>계정설정</a></li>
					<li class="logout"><a href="/logout"><span class="icon"></span>로그아웃</a></li>
					<li class="shutdown"><a href="\\#"><span class="icon"></span>Shutdown…</a></li>
				</ul>
			</nav>		
		</script>
		
		<script id="sidebar-template" type="text/x-kendo-template">	
			<div class="rexy-header">
				<div id="navigation">
					<nav role="navigation">
						<a href="\\#/setup" class="home active" title="홈"><i>Home</i></a>
						<a href="\\#/profile" class="friends" title="사용자관리"><i>Friends</i></a>
						<a href="\\#/settings/profile" class="settings" title="설정"><i>Settings</i></a>
					</nav>
				</div>
			</div>
			<section id="sidebar-menu-section" class="rexy-content"></section>
		</script>

	    <script id="sidebar-menu-template" type="text/x-kendo-template">	    
	    <ul id="sidebar-menu">
	    	# for (var i = 0; i < data.length; i++) { #
	    	# var item =data[i] ; #
	    	<li>#= item.title #
		    	#if ( item.components.length > 0) { #	    			      	
		    	<ul>
		    	# for ( var j = 0 ; j <item.components.length ; j ++ ) { #
		    	# var sub_item =item.components[j] ; #
		    		#if (sub_item.page != 'null' && sub_item.page != '')  { #<li action="#=sub_item.page#" description="#=sub_item.description#" ># } else { #<li># } # 
					#= sub_item.title #
		    		#if ( sub_item.components.length > 0) { #	
		    			<ul>
		      	 		# for ( var k = 0 ; k <sub_item.components.length ; k ++ ) { #	 	
		      	 		# var sub_sub_item =sub_item.components[k] ; #
		      	 			#if (sub_sub_item.page != 'null' && sub_sub_item.page != '')  { #<li action="#=sub_sub_item.page#" description="#=sub_sub_item.description#" ># } else { #<li># } #
		      	 			#= sub_sub_item.title #</li>
		      	 		# } #
		    			</ul>	
		    		# } #
		    		</li>
		    	# } #	
		    	</ul>		
		      	# } #
	        </li>
	        # } #
	    </ul>    
	    </script>		
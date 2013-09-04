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
				<div class="btn-group">
				  <button class="btn">Left</button>
				  <button class="btn">Middle</button>
				  <button class="btn">Right</button>
				</div>
			
			</div>			
		    <div class="row">
      			<div class="span3 bs-docs-sidebar">	
				<ul class="nav nav-list">
					<li class="nav-header">List header</li>
					<li class="active"><a href="\\#">Home</a></li>
					<li><a href="\\#">Library</a></li>
				</ul>
			</div>
			</div>
			
		</script>

	    <script id="top-menu-template" type="text/x-kendo-template">	    
			<div class="panel panel-default">
				<div class="panel-heading">
				회사
				</div>
				<div class="panel-body">
					<input id="companyList" style="width:100%" value="${action.company.companyId}" />
				</div>
			</div>
			
			<ul id="top-menu">
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
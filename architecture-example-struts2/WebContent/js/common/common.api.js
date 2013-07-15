/**
 * User 
 */
;(function($, undefined) {
	var api = window.api = window.api || {};
	api.ui = {};
	api.accounts = {			
		Anonymous : new User({})
	};	
	var AUTH_URL = "/accounts/get-user.do?output=json", SYSTEM_ROLE = "ROLE_SYSTEM" ;	
	api.accounts.hasRole = function( role, user ){					
		if( typeof( user.roles ) != "undefined" && $.inArray( role, user.roles ) >= 0 )
			return true
		else 
			return false;        	
	};	
	api.accounts.load =  function (url, options){	    
		if (typeof url === "object") {
	        options = url;
	        url = undefined;
	    }	    
	    // Force options to be an object
	    options = options || {};		    
	    $.ajax({
			type : 'POST',
			url : options.url || AUTH_URL ,
			success : function(response){
				api.accounts.Token = new User ( $.extend(response.currentUser, { roles: response.roles } ));
				api.accounts.Token.set('isSystem', api.accounts.hasRole(SYSTEM_ROLE, api.accounts.Token) );
				options.success (api.accounts.Token);
			},
			error:options.error || handleKendoAjaxError,
			dataType : "json"
		});	
	};	
	
		
	api.accounts.login =  function (options){	      
	    // Force options to be an object
	    options = options || {};
	    $.ajax({
			type: "POST",
			url: options.url ||  "/login",
			dataType: 'json',
			data: options.data || {},
			success : function( response ) {   
				if( response.error ){ 												
					options.fail(response) ;
				} else {
					options.success(response) ;
				}
			},
			error: function( xhr, ajaxOptions, thrownError){         				        
				options.error(thrownError) ;
			}
		});			    
	}
	
})(jQuery);
/**
 * User 
 */
;(function($, undefined) {
	var common = window.common = window.common || {};
	common.api = {};	
	common.api.getTargetCompany =  function (url, options){
		if (typeof url === "object") {
	        options = url;
	        url = undefined;
	    }	    
	    // Force options to be an object
	    options = options || {};		    
	    $.ajax({
			type : 'POST',
			url : options.url || '/secure/get-company.do?output=json' ,
			data: options.data || {},
			success : function(response){
				if( response.error ){ 												
					options.fail(response) ;
				} else {					
					var company = new Company (response.targetCompany);	
					options.success(company) ;					
				}
			},
			error:options.error || handleKendoAjaxError,
			dataType : "json"
		});	
	};				
	
	common.api.isValidEmail = function (email){
		var expr = /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
        return expr.test(email);
	};
	
	common.api.getUser = function (url, options){
		var user = new User ();			
		if (typeof url === "object") {
	        options = url;
	        url = undefined;
	    }	 
		
		$.ajax({
			type : 'POST',
			url : options.url || '/accounts/get-user.do?output=json' ,
			success : function(response){
				
				if( response.error ){ 												
					//options.fail(response) ;
				} else {					
					user = new User (response.currentUser);						
				}
				return user;
			},
			error:options.error || function(e){ return user },
			dataType : "json"
		});	
		
	}
	
	common.api.signin = function ( options ){		
		if (typeof url === "object") {
			options = url;
			url = undefined;
		}
		options = options || {};
		$.ajax({
			type : 'POST',
			url : options.url ,
			data: { onetime : options.onetime },
			success : function(response){
				if( response.error ){ 												
					options.fail(response) ;
				} else {					
					options.success(response) ;					
				}
			},
			error:options.error || handleKendoAjaxError,
			dataType : "json"
		});	
	};
	
})(jQuery);

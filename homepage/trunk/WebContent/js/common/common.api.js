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
	
	common.api.getUser = function (options){	
		options = options || {};
		$.ajax({
			type : 'POST',
			url : options.url || '/accounts/get-user.do?output=json' ,
			success : function(response){
				var user = new User ();			
				if( response.error ){ 		
					if( typeof options.fail === 'function'  )
						options.fail(response) ;
				} else {				
					user = new User (response.currentUser);	
				}
				if( typeof options.success === 'function'  )
					options.success (user);
			},
			error:options.error || handleKendoAjaxError,
			dataType : "json"
		});	
		
	}
	
	common.api.signin = function ( options ){		
		options = options || {};
		$.ajax({
			type : 'POST',
			url : options.url ,
			data: { onetime : options.onetime },
			success : function(response){
				if( response.error ){ 												
					options.fail(response) ;
				} else {					
					options.success( new User(response.account)) ;					
				}
			},
			error:options.error || handleKendoAjaxError,
			dataType : "json"
		});	
	};

	common.api.signup = function ( options ){		
		options = options || {};
		$.ajax({
			type : 'POST',
			url : options.url || '/accounts/signup-external.do?output=json' ,
			data: { item : options.data },
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

	common.api.guid = function()
	{
		var result, i, j;
		result = '';
		for(j=0; j<32; j++)
		{
		if( j == 8 || j == 12|| j == 16|| j == 20)
		result = result + '-';
		i = Math.floor(Math.random()*16).toString(16).toUpperCase();
		result = result + i;
		}
		return result
	}
		
	common.api.photoStreamsDataSource = new kendo.data.DataSource({		
		serverPaging: true,
		pageSize: 15,
		transport: { 
			read: {
				url : '/community/list-streams-photo.do?output=json',
				type: 'POST',
				dataType: 'json'
			},
            parameterMap: function (options, type){
                return { startIndex: options.skip, pageSize: options.pageSize }
            }
		},
        schema: {
            total: "photoCount",
            data: "photos",
            model: models.Photo
        },
        error:handleKendoAjaxError
	});
	
})(jQuery);

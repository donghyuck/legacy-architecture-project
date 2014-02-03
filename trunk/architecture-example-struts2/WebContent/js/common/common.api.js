/**
 * User 
 */
;(function($, undefined) {
	var common = window.common = window.common || {};
	common.apis = {};
	
	common.apis.getTargetCompany =  function (url, options){
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
})(jQuery);

;(function($, undefined) {
	var common = window.common = window.common || {};	
	common.api = {};
	

	
})(jQuery);
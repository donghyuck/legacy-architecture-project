/**
 * User 
 */
(function($, undefined) {
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
					options.success(response) ;
				}
			},
			error:options.error || handleKendoAjaxError,
			dataType : "json"
		});	
	};	
})(jQuery);
;(function($, undefined) {

	var kendo = window.kendo,	
	stringify = kendo.stringify,
	UNDEFINED = 'undefined',
	LOCALE = 'ko-KR',
	isFunction = kendo.isFunction;
	
	Community = kendo.Class.extend({		
		// The `init` method will be called when a new instance is created
		init : function (options){
		    // Force options to be an object
			options = options || {};
			
			var that = this;
			that.options = options;
			
			if( typeof options.culture === UNDEFINED )
				options.culture = LOCALE;
			if( typeof options.contextPath === UNDEFINED )
				options.contextPath = '';
			if( typeof options.loading === UNDEFINED )
				options.loading = true;			
			
			kendo.culture(that.options.culture);				
			
			if(options.loading){
				 $('.page-loader').fadeOut('slow');
			}
		}		
	});
	
	
})(jQuery);

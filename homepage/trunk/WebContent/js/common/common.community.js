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
		   
			var that = this;	
			options = that.options;
			options = options || {};
			 
			if( typeof options.culture === UNDEFINED )
				options.culture = LOCALE;
			
			that.move();
			alert(options.culture);
			alert(options.contextPath);
		},		
		culture : function (culture){
			kendo.culture(culture);				
		},		
		move : function () {
			
			alert ("fdaf");
		}
	});
	
	
})(jQuery);

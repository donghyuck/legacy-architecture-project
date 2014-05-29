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
			
			if( typeof options.culture === UNDEFINED )
				options.culture = LOCALE;
			that.culture();
			that.move();
			alert(options.culture);
			alert(options.contextPath);
			alert(that.options.culture);
		},		
		options: {
			
		},
		culture : function (culture){
			kendo.culture(culture);				
		},		
		move : function () {
			
			alert ("fdaf");
		}
	});
	
	
})(jQuery);

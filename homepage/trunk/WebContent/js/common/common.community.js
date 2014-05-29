;(function($, undefined) {

	var kendo = window.kendo,	
	stringify = kendo.stringify,
	UNDEFINED = 'undefined',
	isFunction = kendo.isFunction;
	
	Community = kendo.Class.extend({		
		// The `init` method will be called when a new instance is created
		init : function (options){
		    // Force options to be an object
		    options = options || {};
			var that = this;	
			options = that.options;
			
			that.move();
		}
		
		culture : function (culture){
			kendo.culture(culture);				
		} 		
		
		move : function () {
			
			alert ("fdaf");
		}
	});
	
	
})(jQuery);

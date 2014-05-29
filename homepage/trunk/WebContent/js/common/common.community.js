;(function($, undefined) {

	var kendo = window.kendo,	
	stringify = kendo.stringify,
	UNDEFINED = 'undefined',
	isFunction = kendo.isFunction;
	
	Community = kendo.Class.extend({		
		// The `init` method will be called when a new instance is created
		init : function (){
			kendo.culture("ko-KR");	
		}
		
		culture : function (culture){
			kendo.culture(culture);				
		} 		
	});
	
	
})(jQuery);

/**
 * COMMON ADMIN UI DEFINE
 */
/**
 *  extNavbar widget
 *  
 *  top navigation bar
 */
(function($, undefined) {
	var common = window.common = window.common || {};
	common.ui =  common.ui || {};
	
	var kendo = window.kendo,
	Widget = kendo.ui.Widget,
	Class = kendo.Class,
	stringify = kendo.stringify,
	isFunction = kendo.isFunction,
	UNDEFINED = 'undefined',
	POST = 'POST',
	OBJECT_TYPE = 30 ,
	JSON = 'json';
	
	common.ui.admin.Setup = kendo.Class.extend({		
		init : function (options){
			options = options || {};			
			var that = this;
			that.options = options;
			if( typeof that.options.pageSize === UNDEFINED ){
				that.options.pageSize = 3;
			}				
		}
	});		
	
	
	
})(jQuery);


common.ui.admin.setup = function (){
	return new common.ui.admin.Setup();	
}
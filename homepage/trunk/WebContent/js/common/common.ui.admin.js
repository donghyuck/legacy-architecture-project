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
	common.ui =  common.ui || {},
	common.ui.admin = common.ui.admin || {};
	
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
			refresh();
		},
		refresh: function(){			
			$('.menu-content-profile .close').click(function () {
				var $p = $(this).parents('.menu-content');
				$p.addClass('fadeOut');
				setTimeout(function () {
					$p.css({ height: $p.outerHeight(), overflow: 'hidden' }).animate({'padding-top': 0, height: $('#main-navbar').outerHeight()}, 500, function () {
						$p.remove();
					});
				}, 300);
				return false;
			});
		}
	});		
	
	
	
})(jQuery);


common.ui.admin.setup = function (options){
	options = options || {};			
	return new common.ui.admin.Setup(options);	
}
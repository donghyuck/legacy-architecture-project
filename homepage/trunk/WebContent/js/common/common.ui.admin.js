/**
 * COMMON ADMIN UI DEFINE
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
			
			options.localStorageSupported = typeof window.Storage !== "undefined" ? true : false;			
			that._pixelAdmin = window.PixelAdmin;
			that.refresh();
		},		
		_createCompanySelector : function(){
			var item = {
				selector: 'companyDropDownList',
				name: 'companySelector',
				dataTextField: 'displayName',
				dataValueField: 'companyId',
				dataSource: {
					transport: {
						read: {
							dataType: JSON,
							url: '/secure/list-company.do?output=json',
							type: POST
						}
					},
					schema: { 
						data: "companies",
						model : Company
					}
				}	
			};			
			$( item.selector ).extDropDownList(item);			
		},
		refresh: function(){			
			var that = this;
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
			that._createCompanySelector();			
			that._pixelAdmin.start([]);
		
		}
	});	
})(jQuery);

common.ui.admin.setup = function (options){
	options = options || {};			
	return new common.ui.admin.Setup(options);	
}
	
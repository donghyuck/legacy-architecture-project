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
			that._pixelAdmin = window.PixelAdmin;			
			that.refresh();
		},		
		_doAuthenticate : function(){		
			var that = this;
			var renderTo = "account-panel";				
			if ($("#" +renderTo ).length == 0) {
				$('body').append(	'<div id="' + renderTo + '" style="display:none;"></div>');
			}
			$("#" +renderTo ).kendoAccounts({
				visible : false,
				authenticate : function( e ){
					if( isFunction( that.options.authenticate ) )
						authenticate(e);
				}
			});
		},
		_createCompanySelector : function(){	
			var that = this;
			$('#targetCompany').kendoDropDownList({
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
				},
				change : function (e){			
					if( isFunction( that.options.companyChanged ) )
						that.options.companyChanged( this.dataSource.get(this.value) );
				} 
			});		
			
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
			that._doAuthenticate();
			that._pixelAdmin.start([]);	
		}
	});	
})(jQuery);

common.ui.admin.setup = function (options){
	options = options || {};			
	return new common.ui.admin.Setup(options);	
}
	
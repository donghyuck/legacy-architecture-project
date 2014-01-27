/**
 * COMMON UI DEFINE
 */


(function($, undefined) {
	var Widget = kendo.ui.Widget;
	var ui = window.ui = window.ui || {};
	
	/** Utility */
	ui.util = {};	
	ui.util.prettyDate = function(time){
		var date = new Date((time || "").replace(/-/g,"/").replace(/[TZ]/g," ")),
			diff = (((new Date()).getTime() - date.getTime()) / 1000),
			day_diff = Math.floor(diff / 86400);
				
		if ( isNaN(day_diff) || day_diff < 0 || day_diff >= 31 )
			return;
				
		return day_diff == 0 && (
				diff < 60 && "just now" ||
				diff < 120 && "1 minute ago" ||
				diff < 3600 && Math.floor( diff / 60 ) + " minutes ago" ||
				diff < 7200 && "1 hour ago" ||
				diff < 86400 && Math.floor( diff / 3600 ) + " hours ago") ||
			day_diff == 1 && "Yesterday" ||
			day_diff < 7 && day_diff + " days ago" ||
			day_diff < 31 && Math.ceil( day_diff / 7 ) + " weeks ago";
	};
	
	ui.util.feedUrl = function ( site ){
		if( site == "twitter") {
			return "/community/get-twitter-hometimeline.do?output=json";			
		}else if ( site == "facebook"){
			return "/community/get-facebook-homefeed.do?output=json";			
		}
		return null;
	};  

	ui.util.feedReturnSchemaData = function ( site ){
		if( site == "twitter") {
			return "homeTimeline";			
		}else if ( site == "facebook"){
			return "homeFeed";			
		}
		return null;
	};  

	ui.kendoAlert = Widget.extend({		
		init: function(element, options) {			
			var that = this;
			Widget.fn.init.call(that, element, options);
			this.options = that.options;
			windowTemplate = kendo.template(that.options.windowTemplate);
			element.html( windowTemplate(options.data) );	
			close = options.close || that.options.close ;
			
			$(element).find("[data-alert] a.close").click(
				function(e){
					e.preventDefault();
					$(element).find("[data-alert]").fadeOut(300, function(){
						$(element).find("[data-alert]").remove();
						close() ;
					});				
				}										
			);
			 kendo.notify(that);
		},
		options : {
			name: "Alert",
			windowTemplate: '<div data-alert class="alert alert-danger">#=message#<a href="\\#" class="close">&times;</a></div>',
			close : function (){} 
		}		
	}); 	
	
	$.fn.extend( { 
		kendoAlert : function ( options ) {
			return new ui.kendoAlert ( this , options );		
		}
	});
	
})(jQuery);	


/**
 *  extPanel widget
 */
(function($, undefined) {
	var Widget = kendo.ui.Widget, DataSource = kendo.data.DataSource, ui = window.ui = window.ui || {};
	var proxy = $.proxy, CHANGE = "change" ;
	var observable = new kendo.data.ObservableObject({ title : "&nbsp;" } );	
	
	ui.extPanel = Widget.extend({
		init: function(element, options) {			
			var that = this;
			Widget.fn.init.call(that, element, options);			
			options = that.options;
			if( options.title )
				observable.set("title", options.title);
			
			that.render();		
			
			kendo.notify(that);			
		},
		events : {			
			CHANGE
		},
		options : {
			name: "Panel",
			title : null,
			template : null,
			data : null
		},
		hide: function (){
			var that = this ;
			that.element.hide();			
		},
		data : function(){
			var that = this ;
			if(that.options.data )
				return that.options.data;
			return observable ;
		},
		data : function ( Object data ){
			that.options.data = data;
			that.trigger( CHANGE, null ); 
		},
		title: function( title ){
			if( title ){
				observable.set("title" , title);
			}else{
				return observable.get("title");
			}
		},
		refresh: function () {
			var that = this ;
			that.render();
		},
		show: function (){
			var that = this ;
			that.element.show();			
		},
		render: function () {				
			var that = this ;			
        	if( that.options.template ){        		
        		 
        		if( that.options.data ){	
        			that.element.html( that.options.template(that.options.data) ) 
        			kendo.bind(that.element, that.options.data );
        		}else{
        			that.element.html( that.options.template )          		
        			kendo.bind(that.element, observable );
        		}
        	}        	
        	$(that.element).find(".panel-header-actions a.k-link").each(function( index ){
        		 
        		$(this).click(function (e) {
        			e.preventDefault();
        			var header_action = $(this);
        			if( header_action.text() == "Minimize" ){        				
        				var header_action_icon = header_action.find('span');
						if( header_action_icon.hasClass("k-i-minimize") ){
							header_action_icon.removeClass("k-i-minimize");
							header_action_icon.addClass("k-i-maximize");
							$(that.element).find(".panel-body, .panel-footer").addClass("hide");
						}else{
							header_action_icon.removeClass("k-i-maximize");
							header_action_icon.addClass("k-i-minimize");
							$(that.element).find(".panel-body, .panel-footer").removeClass("hide");
						}
						
        			}else if ( header_action.text() == "Close"){
        				that.hide();
        			}else if ( header_action.text() == "Refresh"){	
        				
        			}else if ( header_action.text() == "Custom" ){
        				$(that.element).find(".panel-body:last").toggleClass("hide");
        			}        			
        		});
        		
        	});
        	
        	// custom 
        	$(that.element).find(".panel-body:last button.close").click(function(e){
        		e.preventDefault();	
        		$(that.element).find(".panel-body:last").addClass("hide");       	
        	});	
		}
	});
	
	$.fn.extend( { 
		extPanel : function ( options ) {
			return new ui.extPanel ( this , options );		
		}
	});	
})(jQuery);

/**
 *  extDropDownList widget
 */
(function($, undefined) {
	var Widget = kendo.ui.Widget, DataSource = kendo.data.DataSource, ui = window.ui = window.ui || {};
	ui.extDropDownList = Widget.extend({
		init: function(element, options) {
			var that = this;
			Widget.fn.init.call(that, element, options);
			options = that.options;		
			if( options.renderTo == null ){
				options.renderTo = element;
			}	
			if( options.value != null )
				that.value = options.value;			
			that.render(options);
		},
		events : {			
		},
		value: 0,
		dataSource : null,
		options : {
			name: "dropDownList",	
			dataSource: null,
			template : null,
			enabled : true,
		},
		_change: function (){
			var that = this;
		//	alert( '>>' + that.value );
			if( that.options.change != null )
				that.options.change( that.dataSource.get( that.value) );
		},
		render: function ( options ) {				
			var contentId = this.element.attr('id') + 'list';			
			var content = options.renderTo ;
			var that = this;
			
			that.dataSource = DataSource.create(options.dataSource);		
			that.dataSource.fetch(function(){
				var items = that.dataSource.data();
				var html = '<select id="' + contentId + '"role="navigation" class="form-control"'; 
				if( ! options.enabled )
					html = html + 'disabled >';
				else
					html = html + '>';
				
				$.each( items, function (index, value) {
					html = html + '<option ';
					if( that.value == value[options.dataValueField] )
						html = html + ' selected ';					
					html = html + ' value="'+ value[options.dataValueField]  +'" ' + '>'+ value[options.dataTextField] +'</option>' ;					
				});
				html = html + '</select>' ;
				content.append(html);
				that._change();
				
				$( "#" + contentId  ).change(function() {
					that.value = $( "#" + contentId  ).val() ;
					that._change();
				});
				
				if( options.doAfter != null){
					options.doAfter(that);
				}
 			});
		}
	});

	$.fn.extend( { 
		extDropDownList : function ( options ) {
			return new ui.extDropDownList ( this , options );		
		}
	});
	
})(jQuery);

/**
 * extNavBar widget
 */
(function($, undefined) {
	var Widget = kendo.ui.Widget, DataSource = kendo.data.DataSource, ui = window.ui = window.ui || {};
	ui.extTopBar = Widget.extend({
		init: function(element, options) {			
			var that = this;
			Widget.fn.init.call(that, element, options);			
			options = that.options;					
			if( options.renderTo == null ){
				options.renderTo = element;
			}
			that.items = new Array();
			that.render(options);	
			
		},
		events : {			
		},
		dataSource : null,
		items : null,
		options : {
			name: "topBar",	
			renderTo: null,
			menuName : null,
			dataSource: null,
			template : null,
			select : null,
		},
		render: function ( options ) {			
			var content = options.renderTo ;
			if( options.dataSource == null  && options.menuName != null ){
				this.dataSource = DataSource.create({
					transport: {
						read: {
							url: "/secure/get-company-menu-component.do?output=json",
							dataType: "json",
							data: {
								menuName: options.menuName
							}
						}
					},
					schema: {
						data: "targetCompanyMenuComponent.components"
					}
				});
			}else{
				this.dataSource = DataSource.create(options.dataSource);
			}			
			var that = this;
			that.dataSource.fetch(function(){
			var items = that.dataSource.data();
			content.append( options.template( items ) );	
				
				content.find('.nav a').click(function( e ){
					if( $(e.target).is('[action]') ){
						var selected = $(e.target);
						var item = { title: $.trim(selected.text()), action: selected.attr("action") , description: selected.attr("description") || "" };
						if( options.select != null )
							options.select( item );
						else
							that.select( item );
					}					
				});						
				if( $.isArray( options.items ) ){
					//alert ("array");
				}else if( (typeof options.items == "object") && (options.items !== null) ){
					if( options.items.type == 'dropDownList' ){
						var subItem = options.items;
						var subObject = $('#' + subItem.id ).extDropDownList(subItem);
						that.items.push( subObject );
					}
				}				
				if( options.doAfter != null ){
					options.doAfter(that);    					
				}
			});

		},
		select : function( item ){
			var content = this.options.renderTo ;			
			content.find("form[role='navigation']").attr("action", item.action ).submit();	
		},
		getMenuItem : function( name ){
			var items = this.dataSource.data();
			var menuItem = null;
			$.each( items, function ( i, item ){
				if(item.components.length > 0 )
				{	
					$.each( item.components, function ( j, item2 ){
						if( name == item2.name ){
							menuItem = item2;
							return;
						}
					});
				}else{
					if( name == item.name ){
						menuItem = item;
						return;						
					}
				}
			});
			return menuItem;
		},
		item : function (id){
			return $('#' + id ).data("extDropDownList");			
		}
	});

	$.fn.extend( { 
		extTopBar : function ( options ) {
			return new ui.extTopBar ( this , options );		
		}
	});
	
})(jQuery);


/**
 *  kendoTopBar widget
 */
(function($, undefined) {
	var Widget = kendo.ui.Widget, DataSource = kendo.data.DataSource, ui = window.ui = window.ui || {};
	var sliding = false;	
	var $body = $('body');	
	ui.kendoTopBar = Widget.extend({
		init: function(element, options) {
			var that = this;
			Widget.fn.init.call(that, element, options);
			options = that.options;		
			that.render(options);			
			element.click($.proxy( that._open, this ));				
		},
		events : {
			
		},
		options : {
			name: "topBar",	
			renderTo: null,
			dataSource: null,
			template : null
		},
		render: function ( options ) {			
			var content = options.renderTo ;
			var dataSource = DataSource.create(options.dataSource);	
			var that = this;
			dataSource.fetch(function(){
				var items = dataSource.data();
				content.append( options.template( items ) );	
				content.find('ul').first().kendoMenu({
 					orientation :  "vertical", // "horizontal",
 					select: function(e){	
 						if( $(e.item).is('[action]') ){
 							var selected = $(e.item);
 							options.select( { title: $.trim(selected.text()), action: selected.attr("action") , description: selected.attr("description") || "" } ); 							
 							setTimeout( function(){ that._open(); }, 300);
 						}
					}
 				});	
				if( options.doAfter != null ){
					options.doAfter(content);
				}	
			});
		},
		// Function that controls opening of the pageslide
		_open: function (e){
			var content = this.options.renderTo ;
			 var slide = kendo.fx(content).slideIn("right");
			 if( sliding ){
				 slide.reverse();
				 sliding = false;
			}else{	 
				 slide.play();
				 sliding = true;
			}
			e.preventDefault();
		}
	});
	$.fn.extend( { 
		kendoTopBar : function ( options ) {
			return new ui.kendoTopBar ( this , options );		
		}
	});	
})(jQuery);

/**
 *  Extended Accounts widget
 */
(function($, undefined) {	
	var Widget = kendo.ui.Widget, ui = window.ui = window.ui || {};
	var proxy = $.proxy,
	template = kendo.template,
	AUTHENTICATE_URL = "/accounts/get-user.do?output=json",
	AUTHENTICATE = "authenticate", 
	LOGIN_URL = "/login", 
	PHOTO_URL = "/accounts/view-image.do?width=100&height=150",
	ERROR = "error",
	UPDATE = "update",
	SYSTEM_ROLE = "ROLE_SYSTEM",
    NS = ".kendoAccounts",
	open = false,
	DISABLED = "disabled";

	ui.kendoAccounts = Widget.extend( {		
		init: function(element, options) {	
			var that = this;			
			Widget.fn.init.call(that, element, options);
			options = that.options;
			element = that.element;			
			if (options.doAuthenticate) {
                that.authenticate();
            }            
			kendo.notify(that);
		},	
		options : {	
			name : "Accounts",
			photoUrl : null,
			doAuthenticate: true,			
			visible: true,
			ajax : { url : AUTHENTICATE_URL },
			template : null,
			dropdown : true
		},
		events : [
			AUTHENTICATE,
			ERROR,
			UPDATE
		],
        authenticate: function(){
        	var that = this ;
         	$.ajax({
    			type : 'POST',
    			url : that.options.ajax.url,
    			success : function(response){
    				user = new User ( $.extend(response.currentUser, { roles: response.roles } ));
   					user.set('isSystem', user.hasRole(SYSTEM_ROLE ) );
   					$(that.element).data("currentUser", user );
   					that.token = user ;    				
    				that.trigger( AUTHENTICATE, {token: user}); 
    				if(that.options.visible){
    					that.render();
    				}
    				if( that.options.afterAuthenticate != null ){
    					that.options.afterAuthenticate();    					
    				}
    			},
    			error: that.options.ajax.error || handleKendoAjaxError,
    			dataType : "json"
    		});	 
        },
        login :  function (url, options){	    
    		if (typeof url === "object") {
    	        options = url;
    	        url = undefined;
    	    }	    
    	    // Force options to be an object
    	    options = options || {};		    
    	    $.ajax({
    			type : 'POST',
    			url : options.url || LOGIN_URL ,
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
    	},
        render : function(){
        	var that = this, element, content;
        	if( that.options.photoUrl != null ){
        		that.token.photoUrl = that.options.photoUrl ;
        	}else{
        		that.token.photoUrl = null;
        	}        	        	
        	if( that.token.properties.imageId ){
        		that.token.photoUrl = PHOTO_URL + '&imageId=' + that.token.properties.imageId ;	
        	}
        	if( that.options.template ){
        		that.element.html( that.options.template( that.token ) );        		
        	}        	
        	if( that.options.dropdown ){
	        	$(that.element).on('click.fndtn.dropdown', '[data-dropdown]', function (e) {        		
	                e.preventDefault();
	                e.stopPropagation();
	                that.toggle($(this));
	            });
	        	$('[data-dropdown-content]').on('click.fndtn.dropdown', function (e) {
	                e.stopPropagation();
	            });
        	}
        },
        toggle : function(target) {        	
        	var dropdown = $('#' + target.data('dropdown'));	        	
        	if( target.hasClass("dropped")){
        		target.removeClass("dropped");
        		dropdown.css("display", "none");
        		open = false;
        	}else{
        		target.addClass("dropped");
        		dropdown.css("display", "block");
        		open = true;
        	}        	
        },
        destroy: function() {
        	var that = this;
            Widget.fn.destroy.call(that);
            that.element.off(NS);
            open = false;
        },
        token : new User({})
	});
	
	$.fn.extend( { 
		kendoAccounts : function ( options ) {
			return new ui.kendoAccounts ( this , options );		
		}
	});	
})(jQuery);

function handleKendoAjaxError(xhr) {
	var message = "";
	if (xhr.status == 0) {
		message = "오프라인 상태입니다.";
	} else if (xhr.status == 404) {
		message = "요청하신 페이지를 찾을 수 없습니다.";
	} else if (xhr.status == 500) {
		message = "시스템 내부 오류가 발생하였습니다.";
	} else if (xhr.status == 403 || xhr.errorThrown == "Forbidden") {
		message =  "접근 권한이 없습니다."; // "Access to the specified resource has
									// been forbidden.";
	} else if (xhr.errorThrown == 'timeout') {
		message = "처리 대기 시간을 초가하였습니다. 잠시 후 다시 시도하여 주십시오.";
	} else if (xhr.errorThrown == 'parsererror') {
		message = "데이터 파싱 중에 오류가 발생하였습니다.";
	} else {
		message = "오류가 발생하였습니다." ;
	}

	$.jGrowl(message, {
		sticky : false,
		life : 1000,
		animateOpen : {
			height : "show"
		},
		animateClose : {
			height : "hide"
		}
	});
};

(function($, window, document, undefined) {
	'use strict';
	var settings = {
		callback : $.noop,
		deep_linking : true,
		init : false
	},
	methods = {
		init : function(options) {
			settings = $.extend({}, settings, options);
			return this.each(function() {
				if (!settings.init)
					methods.events();
				if (settings.deep_linking)
					methods.from_hash();
			});
		},
		events : function() {
			$(document).on('click.fndtn', '.tabs a', function(e) {
				methods.set_tab($(this).parent('dd, li'), e);
			});
			settings.init = true;
		},
		set_tab : function($tab, e) {
			var $activeTab = $tab.closest('dl, ul').find('.active'), target = $tab.children('a').attr("href"), hasHash = /^#/.test(target), $content = $(target + 'Tab');
			if (hasHash && $content.length > 0) {
				// Show tab content
				if (e && !settings.deep_linking)
					e.preventDefault();
				$content.closest('.tabs-content').children('li').removeClass('active').hide();
				$content.css('display', 'block').addClass('active');
			}
			// Make active tab
			$activeTab.removeClass('active');
			$tab.addClass('active');
			settings.callback();
		},
		from_hash : function() {
			var hash = window.location.hash, $tab = $('a[href="' + hash + '"]');
			$tab.trigger('click.fndtn');
		}
	}

}(jQuery, this, this.document));

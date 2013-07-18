/**
 * COMMON UI DEFINE
 */
;(function($, undefined) {
	var Widget = kendo.ui.Widget;
	var ui = window.ui = window.ui || {};

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
			windowTemplate: '<div data-alert class="alert-box alert">#=message#<a href="\\#" class="close">&times;</a></div>',
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
 *  kendoTopBar widget
 */
;(function($, undefined) {
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
			dataSource.fetch(function(){
				var items = dataSource.data();
				content.html( options.template( items ) );	
				content.find('ul').first().kendoMenu({
 					orientation :  "vertical", // "horizontal",
 					select: function(e){	
 						if( $(e.item).is('[action]') ){
 							var selected = $(e.item);
 							options.select( { title: $.trim(selected.text()), action: selected.attr("action") , description: selected.attr("description") || "" } );
 						}
					}
 				});				
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
 *  Extended Pageslide widget
 */
;(function($, undefined) {
	var Widget = kendo.ui.Widget, DataSource = kendo.data.DataSource, ui = window.ui = window.ui || {};
	var sliding = false;	
	var $body = $('body'), $pageslide = $('#pageslide');
	
	ui.kendoPageslide = Widget.extend({
		init: function(element, options) {			
			var that = this;
			Widget.fn.init.call(that, element, options);
			options = that.options;	
			//that.options = $.extend({}, settings, options);		
			if( $pageslide.length == 0 ) {
				$pageslide = $('<div />').attr( 'id', 'pageslide' ).css( 'display', 'none' ).appendTo( $('body') );
			}
			if( options.template ){
				$pageslide.append( options.template( options.data ) );	     	
        	}			
			element.click($.proxy( that._open, this ));			
			if(options.visible){
				that._open();
			}
			
			if( $.isArray( options.content) ){
				alert ("array");
			}else if( (typeof options.content == "object") && (options.content !== null) ){
				that.render( options.content );
			}
			
		},
		events : {
		},
		options : {
			name: "Pageslide",	
			enabled: true,
			visible : false,
			speed:      200,      // Accepts standard jQuery effects speeds (i.e. fast, normal or milliseconds)
	        direction:  'right',   // Accepts 'left' or 'right'
	        modal:      false,   // If set to true, you must explicitly close
									  // pageslide using $.pageslide.close();
	        data: {},
	        template : null,
	        content: null
		},
		render: function ( options ) {			
			var content = $( "#" + options.renderTo ) ;
			var dataSource = DataSource.create(options.dataSource);
			dataSource.fetch(function(){
				var items = dataSource.data();
				content.html( options.template( items ) );	
				content.find('ul').first().kendoMenu({
 					orientation : "vertical",
 					select: function(e){	
 						if( $(e.item).is('[action]') ){
 							var selected = $(e.item);
 							options.select( { title: $.trim(selected.text()), action: selected.attr("action") , description: selected.attr("description") || "" } );
 						}
					}
 				});				
			});			
		},
		// Function that controls opening of the pageslide
		_open: function (e){
			 if ( $pageslide.is(':visible')) {
				// If we clicked the same element twice, toggle closed
				 $("#wrapper").addClass("translate");
				
				this._close();
			}else {
				 $("#wrapper").removeClass("translate");
				this._start();
			}
		},
		_start : function( direction, speed ) {
	        var slideWidth = $pageslide.outerWidth( true ),
	            bodyAnimateIn = {},
	            slideAnimateIn = {};	        
	        // If the slide is open or opening, just ignore the call
	        if( $pageslide.is(':visible') || sliding ) return;	        
	        sliding = true;	          
	        switch( direction ) {
	            case 'left':
	                $pageslide.css({ left: 'auto', right: '-' + slideWidth + 'px' });
	                bodyAnimateIn['margin-left'] = '-=' + slideWidth;
	                slideAnimateIn['right'] = '+=' + slideWidth;
	                break;
	            default:
	                $pageslide.css({ left: '-' + slideWidth + 'px', right: 'auto' });
	                bodyAnimateIn['margin-left'] = '+=' + slideWidth;
	                slideAnimateIn['left'] = '+=' + slideWidth;
	                break;
	        }	                    
	        // Animate the slide, and attach this slide's settings to the
			// element
	        $body.animate(bodyAnimateIn, speed);
	        $pageslide.show().animate(slideAnimateIn, speed, function() {
	        	sliding = false;
	        });
		},
		_close : function( callback ) {
	            slideWidth = $pageslide.outerWidth( true ),
	            speed = $pageslide.data( 'speed' ),
	            bodyAnimateIn = {},
	            slideAnimateIn = {}
	            	        
	        // If the slide isn't open, just ignore the call
	        if( $pageslide.is(':hidden') || sliding ) return;	        
	            sliding = true;
	        
	        switch( $pageslide.data( 'direction' ) ) {
	            case 'left':
	                bodyAnimateIn['margin-left'] = '+=' + slideWidth;
	                slideAnimateIn['right'] = '-=' + slideWidth;
	                break;
	            default:
	                bodyAnimateIn['margin-left'] = '-=' + slideWidth;
	                slideAnimateIn['left'] = '-=' + slideWidth;
	                break;
	        }
	        
	        $pageslide.animate(slideAnimateIn, speed);
	        $body.animate(bodyAnimateIn, speed, function() {
	            $pageslide.hide();
	            sliding = false;
	            if( typeof callback != 'undefined' ) callback();
	        });
	    }
	});

	$.fn.extend( { 
		kendoPageslide : function ( options ) {
			return new ui.kendoPageslide ( this , options );		
		}
	});
	
})(jQuery);

/**
 *  Extended Accounts widget
 */
;(function($, undefined) {	
	var Widget = kendo.ui.Widget, ui = window.ui = window.ui || {};
	var proxy = $.proxy,
	template = kendo.template,
	AUTHENTICATE_URL = "/accounts/get-user.do?output=json",
	AUTHENTICATE = "authenticate", 
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
			template : null
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
    				that.token = user ;
    				that.trigger( AUTHENTICATE, {token: user});    				
    				if(that.options.visible){
    					that.render();
    				}
    			},
    			error: that.options.ajax.error || handleKendoAjaxError,
    			dataType : "json"
    		});	 
        },
        render : function(){
        	//that.trigger( UPDATE, {token: user});
        	var that = this, element, content  ;        	

        	if( that.options.photoUrl != null ){
        		that.token.photoUrl = that.options.photoUrl ;
        	}else{
        		that.token.photoUrl = null;
        	}
        	        	
        	if( that.token.properties.imageId ){
        		that.token.photoUrl = PHOTO_URL + '&imageId=' + that.token.properties.imageId ;	
        	}
        	if (!that.element ) {
        		//that.element.html( );
        	} 
        	if( that.options.template ){
        		that.element.html( that.options.template( that.token ) );        		
        	}         	
        	
        	$(that.element).on('click.fndtn.dropdown', '[data-dropdown]', function (e) {        		
                e.preventDefault();
                e.stopPropagation();
                that.toggle($(this));
            });
        	
       	/*
            $('*, html, body').on('click.fndtn.dropdown', function (e) {
                if (!$(e.target).data('dropdown')) {
                	alert("2"); //alert($('[data-dropdown-content]').hasClass("dropped"));
                }else{
                	alert("1");
                }
              });
            */
        	
            //$('*, html, body').on('click', function (e) {
            	//alert(  e.target.html());
        	//});
            
        	$('[data-dropdown-content]').on('click.fndtn.dropdown', function (e) {
        		//alert( e.target.text());
                e.stopPropagation();
              });
            
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
		message = "페이터 파싱 중에 오류가 발생하였습니다.";
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
}

;
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

	$.fn.foundationTabs = function(method) {
		if (methods[method]) {
			return methods[method].apply(this, Array.prototype.slice.call( arguments, 1));
		} else if (typeof method === 'object' || !method) {
			return methods.init.apply(this, arguments);
		} else {
			$.error('Method ' + method + ' does not exist on jQuery.foundationTabs');
		}
	};
	
}(jQuery, this, this.document));

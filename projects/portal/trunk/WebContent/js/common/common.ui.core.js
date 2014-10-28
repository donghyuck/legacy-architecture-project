/**
 * COMMON UI CORE
 * dependency : jquery
 */
;(function($, undefined) {
		var common = window.common = window.common || { },
		extend = $.extend,
		each = $.each,
		isArray = $.isArray,
		proxy = $.proxy,
		noop = $.noop,
		math = Math,
		FUNCTION = "function",
		STRING = "string",
		OBJECT = "object",
		UNDEFINED = "undefined";
				
	function guid () {		
		var result, i, j;
		result = '';
		for(j=0; j<32; j++)
		{
			if( j == 8 || j == 12|| j == 16|| j == 20)
				result = result + '-';
			i = Math.floor(Math.random()*16).toString(16).toUpperCase();
			result = result + i;
		}
		return result		
	}		
	extend(common, {	
		ui: common.ui || {},
		guid : common.guid || guid
	});
		
})(jQuery);


;(function($, undefined) {
	var ui = common.ui,
	isFunction = kendo.isFunction,
	extend = $.extend,
	DataSource = kendo.data.DataSource,
	Widget = kendo.ui.Widget, 
	progress = kendo.ui.progress,
	POST = 'POST',	
	JSON = 'json',
	VISIBLE = ":visible",
	STRING = 'string',
	CLICK = "click",
	CHANGE = "change",	
	OPEN = "open",
	HIDDEN = "hidden",
	CURSOR = "cursor",	
	DEACTIVATE = "deactivate",
	ACTIVATE = "activate",	
	UNDEFINED = "undefined";
	
	function handleAjaxError(xhr) {
		var message = "";		
		
		if( typeof xhr === STRING ){
			message = xhr;			
		} else {		
			if (xhr.status == 0) {
				message = "오프라인 상태입니다.";
			} else if (xhr.status == 404 || xhr.errorThrown == "Not found")  {
				message = "요청하신 페이지를 찾을 수 없습니다.";
			} else if (xhr.status == 500) {
				message = "시스템 내부 오류가 발생하였습니다.";
			} else if (xhr.status == 503) {
				message = "서비스 이용이 지연되고 있습니다. 잠시 후 다시 시도하여 주십시오.";			
			} else if (xhr.status == 403 || xhr.errorThrown == "Forbidden") {
				message =  "접근 권한이 없습니다.";
			} else if (xhr.errorThrown == 'timeout') {
				message = "처리 대기 시간을 초가하였습니다. 잠시 후 다시 시도하여 주십시오.";
			} else if (xhr.errorThrown == 'parsererror') {
				message = "데이터 파싱 중에 오류가 발생하였습니다.";
			} else {
				message = "오류가 발생하였습니다." ;
			}		
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
	
	
	function defined(x) {
		return (typeof x != UNDEFINED);
	};
	
	function status ( selector, status ){
		var element = selector;
		if(defined(status)){
			if( status === 'disable') {
				if( !element.is(":disabled") ){
					element.prop("disabled", true);
					if( element.is("[data-toggle='button']") ){
						element.toggleClass("active");
					}
				}
			}else if (status === 'enable' ){
				if( element.is(":disabled") ){
					element.prop("disabled", false);
					if( element.is("[data-toggle='button']") ){
						element.toggleClass("active");
					}
				}				
			}
		}
	}
	
	function slimScroll( renderTo , options ){
		options = options || {};
		renderTo.slimScroll( options );
	}
	
	function visible( selector ){
		return selector.is(":visible");
	}
	
	var DEFAULT_PAGER_SETTING = {
			refresh : true,		
			buttonCount : 9,
			info: false
	};	
	
	function listview( renderTo, options){		
		if(!renderTo.data("kendoListView")){			
			 renderTo.kendoListView(options);
		}		
		return renderTo.data("kendoListView");
	}
	
	function pager ( renderTo, options ){		
		options = options || {};				
		var settings = extend(true, {}, DEFAULT_PAGER_SETTING , options ); 
		if(!renderTo.data("kendoPager")){			
			 renderTo.kendoPager(settings);
		}		
		return renderTo.data("kendoPager");
	}
	
	/**
	 *  
	 *  
	 */
	var DEFAULT_DATASOURCE_SETTING = {
			transport:{
				read:{
					type :POST,
					dataType : JSON
				} 				
			},
			serverPaging: true,
			error:handleAjaxError,	
			pageSize: 10		
	};
	
	
	function datasource(url, options){		
		options = options || {};		
		var settings = extend(true, {}, DEFAULT_DATASOURCE_SETTING , options ); 
		if( defined(url) ){
			settings.transport.read.url = url;			
		}		
		var dataSource =  DataSource.create(settings);
		return dataSource;
	};

	var DEFAULT_AJAX_SETTING = {
		type : POST,	
		data : {},
		dataType : JSON,
		error:handleAjaxError 		
	};	


	function ajax ( url, options ){
		options = options || {};	
		var settings = extend(true, {}, DEFAULT_AJAX_SETTING , options ); 
		if( defined( url) ){
			settings.url = url;			
		}				
		$.ajax(settings);		
	};	

	var ButtonsGroup = Widget.extend({
		init : function(element, options) {
			var that = this;
			Widget.fn.init.call(that, element, options);
			element = that.wrapper = that.element;
			options = that.options;
			that._radio();
			kendo.notify(that);
		},
		events : [ CLICK, CHANGE ],
		options : {
			name : "ButtonsGroup",
			enable : true
		},
		_value : function() {
			var that = this;
			if (that.radio) {
				return that.element.find(".active input[type='radio']").val();
			}
		},
		_radio : function() {
			var that = this;
			var input = that.element.find("input[type='radio']");
			if (input.length > 0) {
				that.radio = true;
			} else {
				that.radio = false;
			}
			if (that.radio) {
				that.value = that._value();
				input.on(CHANGE, function(e) {
					if (that.value != this.value) {
						that.value = this.value;
						that.trigger(CHANGE, {
							value : that.value
						})
					}
				});
			}
		}
	});
	
	function buttonsGroup ( renderTo, options ){		
		options = options || {};	
		if( defined(renderTo) ){
			if(renderTo.data("kendoButtonsGroup")){
				return	renderTo.data("kendoButtonsGroup");
			}else{
				return new ButtonsGroup(renderTo, options ); 				 
			}
		}
	}	
	
	function scrollTop(selector){
		$('html, body').animate({scrollTop: selector.offset().top}, 1000);
	}
	
	
	extend(ui , {	
		handleAjaxError : common.ui.handleAjaxError || handleAjaxError,
		defined : common.ui.defined || defined,
		visible : common.ui.visible || visible,
		status : common.ui.status || status,
		datasource : common.ui.datasource || datasource,
		ajax : common.ui.ajax || ajax,
		listview : common.ui.listview || listview,
		pager : common.ui.pager || pager,
		slimScroll : common.ui.slimScroll || slimScroll,
		buttonsGroup : common.ui.buttonsGroup || buttonsGroup,
		scrollTop: common.ui.scrollTop || scrollTop
	});
	
})(jQuery);


;(function($, undefined) {
	var ui = common.ui,
	handleAjaxError = ui.handleAjaxError,
	defined = ui.defined,
	isFunction = kendo.isFunction,
	extend = $.extend,
	DataSource = kendo.data.DataSource,
	Widget = kendo.ui.Widget, 
	template = kendo.template,
	progress = kendo.ui.progress,
	POST = 'POST',	
	JSON = 'json',
	VISIBLE = ":visible",
	STRING = 'string',
	CLICK = "click",
	HIDDEN = "hidden",
	CURSOR = "cursor",	
	VISIBLE = ":visible",
	HIDDEN = "hidden",
	CURSOR = "cursor",
	// events
	CHANGE = "change", 
	OPEN = "open",
	DEACTIVATE = "deactivate",
	ACTIVATE = "activate",
	CLOSE = "close",
	REFRESH = "refresh",
	CUSTOM = "custom",
	ERROR = "error",
	DRAGSTART = "dragstart",
    DRAGEND = "dragend",		
	REFRESHICON = ".k-window-titlebar .k-i-refresh",
	MINIMIZE_MAXIMIZE = ".k-window-actions .k-i-minimize,.k-window-actions .k-i-maximize",
	UNDEFINED = "undefined";
	
	var PANEL = ".panel",
	PANEL_HEADING = ".panel-heading",
	PANEL_TITLE = ".panel-title",
	PANEL_BODY = ".panel-body",
	PANEL_HEADING_BUTTONS = ".panel-heading .k-window-action",	
	templates = {
			wrapper: template("<div class='panel panel-default' />"),	
			action: template(
		            "<a role='button' href='\\#' class='k-window-action k-link'>" +
		                "<span role='presentation' class='k-icon k-i-#= name.toLowerCase() #'>#= name #</span>" +
		            "</a>"
		        ),
			heading: template(
				"<div class='panel-heading'>" +
				"<h3 class='panel-title'>#= title #</h3>" +
				"<div class='k-window-actions panel-header-controls'>" +
				"<div class='k-window-actions'>" +
	            "# for (var i = 0; i < actions.length; i++) { #" +
	                "#= action({ name: actions[i] }) #" +
	            "# } #" +
	            "</div>" +			
				"</div>"	 +
				"</div>"	
			) ,
			body: template("<div class='panel-body'><div class='panel-body-loading'></div></div>"),
			footer: template("<div class='panel-footer'></div>")
		};
	
	
	var Panel = Widget.extend({
		init : function(element, options) {
			var that = this, wrapper, content, visibility, display, isVisible = false, 
			suppressActions = options && options.actions && !options.actions.length, id;
			
			Widget.fn.init.call(that, element, options);
			options = that.options;			
			element = that.element;
			content = options.content;			
			
			if (suppressActions) {
				options.actions = [];
			}

			if (!defined(options.visible) || options.visible === null) {
				options.visible = element.is(VISIBLE);
			}
			if (element.is(VISIBLE)) {
				isVisible = true;				
			} else {
				visibility = element.css("visibility");
				display = element.css("display");
				element.css({ visibility: HIDDEN, display: "" });
				element.css({ visibility: visibility, display: display });
			}			
			if (!defined(options.visible) || options.visible === null) {
				options.visible = element.is(VISIBLE);				
			}
			wrapper = that.wrapper = element.closest(PANEL);
			wrapper.append(templates.heading( extend( templates, options )));
			wrapper.append(templates.body( {} ) );
			
			if (content) {
				that.render();			
			}
			
			if( defined(options.template)){
				if (!defined(options.data) ){
					options.data = {};
				}
				options.content = options.template(options.data); 
				that.render();			
			}

			 if( options.autoBind )
				kendo.bind(element, options.data );
			 
			id = element.attr("id");		
			
			wrapper.on("click", "> " + PANEL_HEADING_BUTTONS, proxy(that._panelActionHandler, that));
			 if (options.visible) {
				 that.trigger(OPEN, {target: that});
				 that.trigger(ACTIVATE);
			 }
			kendo.notify(that);
		},
		events:[
			OPEN,
			CLOSE,
			REFRESH,
			DRAGSTART,
			DRAGEND,
			CUSTOM,
			ERROR
		],		
		options : {
			name : "Panel",
			title: "",
			actions: ["Close"],
			content : null,
			visible: null,
			autoBind: false,
			animation : {
				open: {},
				close: {}
			},
			refreshContent : true,
			handlers : {}
		},
		data : function( data ){
			var that = this;
			if( defined(data)){
				that.options.data = data;
			}else{
				return that.options.data;
			}
		},
		_animations: function() {
			var options = this.options;
			if (options.animation === false) {
				 options.animation = { open: { effects: {} }, close: { hide: true, effects: {} } };				
			}
		},
		_closable: function() {
			return $.inArray("close", $.map(this.options.actions, function(x) { return x.toLowerCase(); })) > -1;
		},
		_panelActionHandler: function(e){
			if (this._closing) {
                return;
            }
			 var icon = $(e.target).closest(".k-window-action").find(".k-icon");
			 var action = this._actionForIcon(icon);
			 if (action) {
				 e.preventDefault();
				 this[action]();
				 return false;
			 }			 
		},
		_actionForIcon: function(icon) {
			var iconClass = /\bk-i-\w+\b/.exec(icon[0].className)[0];
			return {
				"k-i-close": "_close",
				"k-i-maximize": "maximize",
				"k-i-minimize": "minimize",
				"k-i-restore": "restore",
				"k-i-refresh": "refresh",
				"k-i-custom": "_custom"
			}[iconClass];
		},				
		
		_custom: function(systemTriggered){
			var that = this;
			that.trigger(CUSTOM, {target: that});
		},
		_close: function(systemTriggered) {
			var that = this,
				wrapper = that.wrapper,
				options = that.options,
				showOptions = options.animation.open,
				hideOptions = options.animation.close;
			
			if (wrapper.is(VISIBLE) && !that.trigger(CLOSE, { userTriggered: !systemTriggered, target: that })) {
				that._closing = true;
				 options.visible = false;
				 wrapper.kendoStop().kendoAnimate({
					effects: hideOptions.effects || showOptions.effects,
					reverse: hideOptions.reverse === true,
					duration: hideOptions.duration,
					complete: proxy(this._deactivate, this)
				 });
			}			
		},
		toggleMaximization: function () {
            if (this._closing) {
                return this;
            }
            return this[this.options.isMaximized ? "restore" : "maximize"]();
        },		
		_deactivate: function() {
			this.wrapper.hide().css("opacity","");
			this.trigger(DEACTIVATE);			
			this.destroy();
        },
		title : function (text){
			var that = this,
				wrapper = that.wrapper,
				options = that.options;
		},
		maximize: sizingAction("maximize", function() {
			var that = this,
            wrapper = that.wrapper;
			if( !wrapper.children(EXT_PANEL_BODY).is(VISIBLE) ){
				wrapper.children(EXT_PANEL_BODY).slideToggle(200);		
			}			
			that.options.isMaximized = true;
			
		}),
		minimize: sizingAction("minimize", function() {
			var that = this,
				wrapper = that.wrapper;	
			that.options.isMinimized = true;
			if( wrapper.children(PANEL_BODY).is(VISIBLE) ){	
				wrapper.children(PANEL_BODY).slideToggle(200);		
			}
		}),
		restore: function () {
			var that = this;
			var options = that.options;
			that.wrapper.find(".panel-heading .k-i-restore").parent().remove().end().end().find(MINIMIZE_MAXIMIZE).parent().show().end().end();
			that.wrapper.children(EXT_PANEL_BODY).slideToggle(200);		
			options.isMaximized = options.isMinimized = false;			
			return that;
		},
		render: function(){
			var that = this,
			wrapper = that.wrapper,
			options = that.options;
			wrapper.children(EXT_PANEL_BODY).html(options.content);		
		},	
		refresh: function(){
			var that = this,
			wrapper = that.wrapper,
			options = that.options;
			if( isFunction(options.handlers.refresh) ){
				options.handlers.refresh();				
			}			
			that.trigger(REFRESH, {target: that});			
		},		
		content:function(html, data){
		 	var content = this.wrapper.children(EXT_PANEL_BODY);
		 	if (!defined(html)) {
		 		return content.html();		 		
		 	}
		 	content.empty().html(html);
		 	kendo.bind(content, data);
		},
		destroy: function () {
			 Widget.fn.destroy.call(this);
			 this.unbind(undefined);
			 kendo.destroy(this.wrapper);
			 this.wrapper.empty().remove();
			 this.wrapper = this.appendTo = this.element = $();
		}
	});
	
	
	extend(ui , {	
		panel : common.ui.panel || panel
	});
	
})(jQuery);

/**
 * COMMON UI DEFINE
 */
 
function handleKendoAjaxError(xhr) {
	var message = "";
	if (xhr.status == 0) {
		message = "오프라인 상태입니다.";
	} else if (xhr.status == 404) {
		message = "요청하신 페이지를 찾을 수 없습니다.";
	} else if (xhr.status == 500) {
		message = "시스템 내부 오류가 발생하였습니다.";
	} else if (xhr.status == 403 || xhr.errorThrown == "Forbidden") {
		message =  "접근 권한이 없습니다."; // "Access to the specified resource has been forbidden.";
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

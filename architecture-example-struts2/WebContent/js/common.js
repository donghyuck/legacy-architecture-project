function token(url, option, handler, dataType) {
	var params = "create=" + option + "&output=" + dataType;
	$.ajax({
		type : 'POST',
		url : url,
		data : params,
		success : handler,
		dataType : dataType
	});
}

;
(function($, window, undefined) {
	'use strict';		
	$.fn.commonSwitchBox = function(options) {		
		// Loops Through Each Toggle Switch On Page		
		$('div.Switch').each(function() {			
			// Search of a checkbox within the parent
			var checkboxId = '#' + $(this).attr("for") ;			
			if( !$( checkboxId ).hasClass("show") ){
				$( checkboxId ).hide();
			}			
			if( $( checkboxId ).is(':checked')){
				$(this).removeClass('On').addClass('Off');
			}else{
				$(this).removeClass('Off').addClass('On');				
			}		
			$(this).unbind("click");
		});
		
		
		// Switch Click
		$('div.Switch').click(function() {			
			// Check If Enabled (Has 'On' Class)			
			var checkboxId = '#' + $(this).attr("for") ;		
			if ($(this).hasClass('On')) {
				// Try To Find Checkbox Within Parent Div, And Check It				
				//$(this).parent().find('input:checkbox').attr('checked', true);				
				$( checkboxId ).attr('checked', true );				
				// Change Button Style - Remove On Class, Add Off Class
				$(this).removeClass('On').addClass('Off');
			} else { // If Button Is Disabled (Has 'Off' Class)
				// Try To Find Checkbox Within Parent Div, And Uncheck It
				$( checkboxId ).attr('checked', false);
				// Change Button Style - Remove Off Class, Add On Class
				$(this).removeClass('Off').addClass('On');
			}			
		});		
	}
})(jQuery, this);
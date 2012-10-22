function token( url, option, handler, dataType ){	
	var params = "create=" + option  + "&output=" + dataType  ;	
	$.ajax( {
		type: 'POST',
		url: url,
		data: params,
		success: handler,
		dataType: dataType	
	    }
	);	
}
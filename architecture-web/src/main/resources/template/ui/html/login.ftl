<!DOCTYPE html>
<html>
<head>
<title>Simple Login</title>
<link rel="stylesheet" href="/includes/css/jquery.jgrowl.css" type="text/css" media="all">
</head>
<body>
<section>
    <form id="login"> 
	<header>
    	<h3>Login</h3>
    </header>
    <article>
    	<p>아이디와 패스워드를 입력하십시오.</p>      
	    <fieldset id="inputs">
	        <input id="username" name="username" type="text" placeholder="아이디" autofocus required>   
	        <input id="password" name="password" type="password" placeholder="비밀번호" required>
	    </fieldset>        
    </article>
    <footer>
    	<input type="submit" id="submit" value="로그인">
    </footer>
    </form>
</section>
<script src="/includes/scripts/yepnope/1.0.2/yepnope.min.js"></script>
<script>
// load jquery script with yepnope.
yepnope([{
  load:  ['preload!/includes/scripts/jquery/1.7/jquery.min.js',
          'preload!/includes/scripts/jgrowl/1.2.5/jquery.jgrowl.min.js'],
  complete: function () {           
    // ajax submit with jquery     
    $('#login').submit(
        function(){
			$.ajax({
			    type: 'POST',
			    dataType: 'xml',
			    url: 'accounts/authenticate',
			    data: { username: $('#username').val(), password: $('#password').val(), dataType:'xml'},
			    success: function( xml ) {			    
			        
			        $(xml).find('error').each( function(){
			            var msg = $(this).text();			            		            	
			            $('#username').val('');
			            $('#password').val('');
			            $.jGrowl(msg);
			        } );			        
                                      
                   $(xml).find("welcome").each( function(){
			            var msg = $(this).text();			            		            	
			            $('#username').val('');
			            $('#password').val('');
			            $.jGrowl(msg);	
			            $(location).attr('href', '/main.do');
                   } );
                   
			    }    
			});	
            return false;
        }
    );
  }
}]);
</script>
</body>
</html>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="utf-8" />
    <title>Sign in with your account</title>
    <script src="/includes/scripts/ext-core/3.1.0/ext-core.js" type="text/javascript" charset="utf-8"></script>
  </head>  
  <body id="login">
    <script type="text/javascript">    
    
      function doLogin(){
        Ext.Ajax.request({
   		    url: '/accounts/authenticate.ajax',
            form: 'loginForm',
            success: function(response, opts) {            
                var text = response.responseText;
                var data = Ext.decode(text);
                
                alert(text);
                
                if(data.response.authentication == "true"){
                    document.pageRedirectForm.submit();
                }else{
                    alert("아이디 또는 비밀번호 오류입니다.");
                    document.loginForm.reset();
                }
            },
            failure: function(response, opts) {
                alert('server-side failure with status code ' + response.status);                
            }
        });
      }    
      
    </script>
    <section> 
	    <h1 id="title">Sign in with your account</h1> 
	    <form method="post" accept-charset="UTF-8" id="loginForm" name="loginForm">
	      <label for="signin_username">Name:</label> 
		  <input type="text" id="signin_username" name="username" tabindex="1" placeholder="Enter your name" required autofocus />
		  <label for="signin_password">Password:</label> 
		  <input type="password" id="signin_password" name="password" placeholder="Enter your password" tabindex="2" required />
		  <button type="button" tabindex="3" onclick="doLogin();">Login in</button>
	    </form>
	    
	    <form method="post" accept-charset="UTF-8" id="pageRedirectForm" name="pageRedirectForm" action="/setup/main.do"></form>
    </section>
</body>
</html>
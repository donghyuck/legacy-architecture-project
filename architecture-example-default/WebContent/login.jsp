<%@ page pageEncoding="UTF-8"%>
<html decorator="banded">
<head>
<title>로그인</title>
<script type="text/javascript">
		 yepnope([{
       	  load: [ '<%= request.getContextPath()  %>/js/jquery/1.8.2/jquery.min.js',      
       	          '<%= request.getContextPath()  %>/js/kendo/kendo.web.min.js',
       	          'css!<%= request.getContextPath()  %>/styles/kendo.common.min.css',
       	          'css!<%= request.getContextPath()  %>/styles/kendo.metro.min.css'
       	         ],
              	  complete: function() {        	
              		  
              		 var validator = $("#login-form").kendoValidator().data("kendoValidator");     
           		     $("#login").click(function() {
           			  if( validator.validate() )
           		      {        				
           				 $.ajax({
           				     type: "POST",
           				     url: "/login",
           				     dataType: 'json',
           				     data: $("form[name=fm1]").serialize(),
           				     success : function( response ) {   
                                    if( response.error ){                                    	 
                                         var status = $(".status");
                                         status.text( "입력한 사용자 이름 또는 비밀번호가 잘못되었습니다." ).addClass("error") ;                                      
                                         $("#login").kendoAnimate("slideIn:up");          
                                         $("#password").val("").focus();
                                    } else {
                                         var status = $(".status");
                                         status.text(  ""  );
                                         $("form[name='fm1']")[0].reset();                                           
                                         $("form[name='fm1']").attr("action", "/main.do").submit();
                                    }                                 
           				     },
           				     error: function( xhr, ajaxOptions, thrownError){         				        
           				    	 $("form[name='fm1']")[0].reset();                    
           				    	 var status = $(".status");
   							     status.text(  "잘못된 접근입니다."  ).addClass("error") ;    
           				         $("#login").kendoAnimate("slideIn:up");
           				     }
           				 });
           			  }else{        			      
           				  $("#login").kendoAnimate("slideIn:up"); 
           			  }
                     });               	  
              	  }
		 }]);  
</script>
</head>
<body>
	<!-- Main Page Content and Sidebar -->
	<div class="row">
		<div class="eight columns">
			<h3>계정</h3>
			<p>설명</p>			
		</div>
		<div class="four columns">
		    <section  id="login-form" class="panel radius round validator-form">
			<h5>로그인</h5>			
				<form name="fm1" method="POST" accept-charset="utf-8">
					<input type="hidden" id="output" name="output" value="json" />
					<div class="row">
					    <div class="twelve mobile-one columns centered">
						    <input type="text" id="username" name="username" class="k-textbox" pattern="[^0-9][A-Za-z]{2,20}" placeholder="아이디" required validationMessage="아이디를 입력하여 주세요." />
						</div>
					</div>
					<div class="row">
					    <div class="twelve mobile-one columns centered">
						    <input type="password" id="password" name="password" class="k-textbox" placeholder="비밀번호" required validationMessage="비밀번호를 입력하여 주세요." />
						</div>
					</div>
				
					<div class="row">
					    <div class="twelve mobile-one columns">
					        <br/>
							<p>
						    <small class="status round"></small>
						    </p>
					    </div>
					</div>
					
					<div class="row">
					    <div class="twelve mobile-one columns centered">
					        <button type="button" id="login" class="k-button small-text">로그인</button>
					    </div>					    
					</div>						
				</form>	
			</section>
		</div>

		<!-- End Sidebar -->
	</div>
	
	<!-- End Main Content and Sidebar -->

</body>
</html>
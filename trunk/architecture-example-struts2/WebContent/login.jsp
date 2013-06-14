<%@ page pageEncoding="UTF-8"%>
<html decorator="main">
<head>
<title>로그인</title>
<script type="text/javascript">
		 yepnope([{
       	  load: [ 
     			'<%= request.getContextPath()  %>/js/jquery/1.9.1/jquery.min.js',
    			'<%= request.getContextPath()  %>/js/jgrowl/jquery.jgrowl.min.js',
    			'<%= request.getContextPath()  %>/js/kendo/kendo.web.min.js',
    			'<%= request.getContextPath()  %>/js/kendo/kendo.ko_KR.js',
    			'<%= request.getContextPath()  %>/js/common/common.ui.min.js',
    			'<%= request.getContextPath()  %>/js/common/common.models.js' ],
              	  complete: function() {        	              		              		  
              		  
                 	 var templateContent = $("#alert-template").html();
                	 var template = kendo.template(templateContent);	               		  
              		 var validator = $("#login-form").kendoValidator().data("kendoValidator");     
              		 
           		     $("#login").click(function() {           		    	
           		      $("#status").html("");
           			  if( validator.validate() )
           		      {        				
           				 $.ajax({
           				     type: "POST",
           				     url: "/login",
           				     dataType: 'json',
           				     data: $("form[name=fm1]").serialize(),
           				     success : function( response ) {   
           				    	    
                                    if( response.error ){ 

                                    	 $("#status").html(  template({ message: "입력한 사용자 이름 또는 비밀번호가 잘못되었습니다." })  );
                                    	 
                                        // var status = $(".status");
                                         //status.text( "입력한 사용자 이름 또는 비밀번호가 잘못되었습니다." ).addClass("error") ;                                      
                                         $("#login").kendoAnimate("slideIn:up");          
                                         $("#password").val("").focus();
                                    } else {
                                         //var status = $(".status");
                                         //status.text(  ""  );
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
           				  //$("#login").kendoAnimate("slideIn:up"); 
           			  }
                     });               	  
              	  }
		 }]);  
</script>
</head>
<body>
	<!-- Main Page Content  -->
  <header>
    <div class="row">
      <div class="twelve columns">
        <h1>로그인</h1>
        <h4>Foundation is designed to quickly prototype, and one thing we've found very helpful is a series of visibility classes that can be applied to turn things on and off for different device characteristics. On this page, you'll see different elements on different screen sizes or orientations.</h4>
      </div>
    </div>
  </header>
  	
	<div class="row">
		<div class="five columns">
		</div>
		<div class="eight columns">				
		    <div class="row">
		        <div class="nine columns"></div>
		   		<div class="three columns"></div>
		    </div>
		</div>		
		<div class="four columns">	
		    <div  id="login-form" class="panel radius round validator-form">
			    <form name="fm1" method="POST" accept-charset="utf-8">
				    <input type="hidden" id="output" name="output" value="json" />		    
				    <div class="row">
				        <div class="twelve columns centered" style="padding:10px;">
				        </div>  
				    </div>		    
				    <div class="row">
				        <div class="twelve columns centered" style="padding:10px;">
				        <input type="text" id="username" name="username"  class="k-textbox" pattern="[^0-9][A-Za-z]{2,20}" placeholder="아이디" required validationMessage="아이디를 입력하여 주세요." />      
				        </div>  
				    </div>
				    <div class="row">
				        <div class="twelve columns centered" style="padding:10px;">
				        <input type="password" id="password" name="password" class="k-textbox"  placeholder="비밀번호" required validationMessage="비밀번호를 입력하여 주세요." />
				        </div>
				    </div>
				    <div class="row">
				        <div class="twelve columns centered" style="padding:10px;">				        
				        <button type="button" id="login" class="k-button">로그인</button>	
				        </div>
				    </div>	
				    <div class="row">
				        <div class="twelve columns centered" style="padding:10px;">
				            <div id="status">				            
				            </div>
				        </div>
				    </div>
				</form>				    
		    </div>
		</div>
	</div>
    <script type="text/x-kendo-template" id="alert-template">
    <div class="alert-box alert">
          #=message#
        <a href="" class="close">&times;</a>
    </div>
    </script>
    
	<STYLE type="text/css">	    

                span.k-tooltip {
                     margin-top: 3px;
                     margin-left: 0px;
                     -moz-border-radius: 15px;
					 border-radius: 15px;
					vertical-align:middle ;
                }
                
				html .k-textbox
				{
				    -moz-box-sizing: border-box;
				    -webkit-box-sizing: border-box;
				    box-sizing: border-box;
				    height: 2.12em;
				    line-height: 2.12em;
				    padding: 2px .3em;
				    text-indent: 0;
				    width: 230px;
				}
	</STYLE>
	<!-- End Main Content and Sidebar -->
	<!-- Start Breadcrumbs -->	    
	<section class="row">
	    <div class="twelve columns">
	        <hr style="margin-top:10px;margin-bottom:10px;" />
			<ul class="breadcrumbs">
			  <li><a href="<%= request.getContextPath()  %>/main.do">홈</a></li>
			  <li class="current"><a href="#">로그인</a></li>
			</ul>
		</div>
	</section>
	<!-- End Breadcrumbs -->	    	
</body>
</html>
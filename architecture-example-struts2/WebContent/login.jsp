<%@ page pageEncoding="UTF-8"%>
<%@ page import="architecture.common.user.*"%>
<html decorator="homepage">
<head>
<title>로그인</title>
<%

User user = SecurityHelper.getUser();
Company company = user.getCompany();

%>
<script type="text/javascript">
		 yepnope([{
       	  load: [ 
    			'<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/js/jquery/1.9.1/jquery.min.js',
    			'<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/js/jgrowl/jquery.jgrowl.min.js',
    			'<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/js/kendo/kendo.web.min.js',
    			'<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/js/kendo/kendo.ko_KR.js',			
    			'<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/js/bootstrap/3.0.0/bootstrap.min.js',
    			'<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/js/bootstrap/3.0.0/tooltip.js',			
    			'<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/js/common/holder.js',
    			'<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/js/common/common.models.js',
    			'<%= architecture.ee.web.util.ServletUtils.getContextPath(request) %>/js/common/common.ui.js'],
              	  complete: function() {        	              		              		  
              		  
                 	 var templateContent = $("#alert-template").html();
                	 var template = kendo.template(templateContent);	               		  
              		 var validator = $("#login-panel").kendoValidator().data("kendoValidator");     
              		 
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
	<!-- 
	<div class="header">
		<div class="container"  style="width: auto;">		
				<nav class="navbar navbar-inverse" role="navigation">
					<div class="navbar-header">
						<a class="navbar-brand" href="/main.do"><%= company.getDisplayName()  %></a>
					</div>														
					<ul class="nav navbar-nav navbar-right">
						<li>
							<a href="#">회원가입</a>
						</li>
					</ul>
				</nav>
		</div>
	</div>
	 -->
	<div class="container layout">	
		<div class="row">
			<div class="col-lg-12">
				<div class="page-header">
				  <h1>로그인  <small>Subtext for header</small></h1>
				</div>			
			</div>
		</div>		
		<div class="row">
			<div class="col-lg-6">
			
				<div class="panel panel-default">		
					<div id="login-panel" class="panel-body">
						<form name="fm1" class="form-horizontal" role="form" method="POST" accept-charset="utf-8">
							<input type="hidden" id="output" name="output" value="json" />		    
						  <div class="form-group">
						    <label for="username" class="col-lg-2 control-label">아이디</label>
						    <div class="col-lg-10">
						      <input type="text" class="form-control"  id="username" name="username"  pattern="[^0-9][A-Za-z]{2,20}" placeholder="아이디" required validationMessage="아이디를 입력하여 주세요.">
						    </div>
						  </div>
						  <div class="form-group">
						    <label for="password" class="col-lg-2 control-label">비밀번호</label>
						    <div class="col-lg-10">
						      <input type="password" class="form-control" id="password" name="password"  placeholder="비밀번호" required validationMessage="비밀번호를 입력하여 주세요." >
						    </div>
						  </div>
						  <div class="form-group">
						    <div class="col-lg-offset-2 col-lg-10">
						      <div class="checkbox">
						        <label>
						          <input type="checkbox">로그인 상태유지  
						        </label>
						      </div>
						    </div>
						  </div>
						  <div class="form-group">
						    <div class="col-lg-offset-2 col-lg-10">
						      <button id="login" type="button" class="btn btn-primary">로그인</button>
						    </div>
						  </div>
						    <div class="col-lg-12">
						       <div id="status"></div>
						    </div>
						</form>
					</div>
					<div class="panel-footer">
						<div class="btn-group ">
							<button type="button" class="btn btn-default" >아이디/비밀번호찾기</button>
							<button type="button" class="btn btn-default">회원가입</button>
						</div>
					</div>						
				</div>
							
			</div>
			<div class="col-lg-6">
			kkk
			</div>
		</div>
	</div>
		
	<script type="text/x-kendo-template" id="alert-template">
	<div class="alert alert-danger">
		<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
          #=message#
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
	<!-- End Breadcrumbs -->	    	
</body>
</html>
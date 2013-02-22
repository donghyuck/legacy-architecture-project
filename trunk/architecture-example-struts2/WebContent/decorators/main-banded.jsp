<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="architecture.common.user.User"%>
<%@ page import="architecture.common.util.StringUtils"%>
<%@ page import="architecture.ee.web.util.ServletUtils"%>
<%@ page import="architecture.user.util.SecurityHelper"%>
<%
    
	String path = ServletUtils.getServletPath(request);
    User user = SecurityHelper.getUser();
    
 %>
<!DOCTYPE html>
<html>
<head>
<title><decorator:title default="" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="-1">
<meta name="viewport" content="width=device-width" />
<script src="<%=request.getContextPath()%>/js/yepnope/1.5.4/yepnope-min.js"></script>

<!-- START Kendo UI Metro Theme CSS  -->
<link  rel="stylesheet" type="text/css"  href="<%=request.getContextPath()%>/styles/kendo/kendo.common.min.css" />
<link  rel="stylesheet" type="text/css"  href="<%=request.getContextPath()%>/styles/kendo/kendo.metro.min.css" />
<!-- END Kendo UI Metro Theme CSS  -->

<link  media="screen"  rel="stylesheet" type="text/css"  href="<%=request.getContextPath()%>/styles/common.ui.css" />
<link  media="screen"  rel="stylesheet" type="text/css"  href="<%=request.getContextPath()%>/styles/common.top-bar.css" />

<script type="text/javascript">
<!--
yepnope([{
    load: [  
        '<%=request.getContextPath() %>/js/jquery/1.8.2/jquery.min.js',        
        '<%= request.getContextPath() %>/js/jgrowl/jquery.jgrowl.min.js'
    ],    
    complete: function(){
    	$(document).ready(function(){    		
    		// 브라우져 호환성 처리
    		if( jQuery.browser.msie ){
    			if( jQuery.browser.version != '9.0' ) {    				
    				$('.block-grid.two-up>li:nth-child(2n+1)').css({clear: 'both'}); 
    				$('.block-grid.three-up>li:nth-child(3n+1)').css({clear: 'both'}); 
    				$('.block-grid.four-up>li:nth-child(4n+1)').css({clear: 'both'}); 
    				$('.block-grid.five-up>li:nth-child(5n+1)').css({clear: 'both'});    				
    			}
    		}
    		   		
    		/* START TOP MENU */    		
    		$(document).ready(function(){    			
    			
			    var dropdown = $(".top-bar a[href=#expand]"), submenus = $(".top-bar").find(".submenu-box");
			
			    //sub menu dropdowns functionality
			    dropdown.click(function (e) {
			        var $this = $(this);
			        submenus.fadeOut(); //close all dropdowns
			        $this.toggleClass("expanded"); //set active state to dropdown link
			        dropdown.not($this).removeClass("expanded"); //for closed dropdown menus - remove active state of their link
			        $this.siblings(".submenu-box").not(":animated").fadeToggle(250); //display needed dropdown menu
			        e.stopPropagation();
			        e.preventDefault();
			    });
			
			    //on click outside of dropdowns, they disappear
			    $(document).click(function () {
			        submenus.fadeOut();
			        dropdown.removeClass("expanded");
			    });
			});
    		/* END TOP MENU */
    		
    		
    		$('#expend-top-btn').click( function (e){        	
    			var currentTarget=$(e.currentTarget);			
            	if( currentTarget.find('span').hasClass('k-icon k-si-plus') ) {
            		currentTarget.find('span').removeClass('k-icon k-si-plus')
            		currentTarget.find('span').addClass('k-icon k-si-minus')
           		    
            		 $('#expend-top-section').slideDown();
            	} else {
            		$(this).find('span').removeClass('k-icon k-si-minus')
            		$(this).find('span').addClass('k-icon k-si-plus')        		
            		
            		 $('#expend-top-section').slideUp();
            	}
        	}); 
    		   		
    		/* START LOGIN BOX */
    		$('.signin-button').live("click", function(e){    			
    			var currentTarget=$(e.currentTarget);			
    			if( currentTarget.find('span').hasClass('k-i-arrowhead-s') ) {
    				currentTarget.find('span').removeClass('k-i-arrowhead-s');		
    				$(".signin_window").toggle();
    				currentTarget.find('span').addClass('k-i-arrowhead-n');    				 
    			}else{    				
    				currentTarget.find('span').removeClass('k-i-arrowhead-n');    	
    				$(".signin_window").hide();
    				currentTarget.find('span').addClass('k-i-arrowhead-s');    				
    			}    			
    		});    		
    		
            $(document).mouseup(function(e) {
                if($(e.target).parent("a.signin").length==0) {                   
                    $(".signin_window").hide();
                    $(".signin").removeClass("menu-open");
                }
            });    
            
            $(".signin_window").mouseup(function() {
                return false
            });
            
            $("#login-btn").click(function() {            	
            	$.ajax({
  				     type: "POST",
  				     url: "/login",
  				     dataType: 'json',
  				     data: $("form[name='signin-fm']").serialize(),
  				     success : function( response ) {     				    	    
                           if( response.error ){ 
                        	   alert( response.error.message );
                           	 $("#status").html(  template({ message: "입력한 사용자 이름 또는 비밀번호가 잘못되었습니다." })  );                           	                                  
                                $("#login-btn").kendoAnimate("slideIn:up");          
                                $("#password").val("").focus();                               
                           } else {
                                $("form[name='signin-fm']")[0].reset();               	                            
                                $("form[name='signin-fm']").attr("action", "/main.do").submit();
                           }                                 
  				     },
  				     error: function( xhr, ajaxOptions, thrownError){         			
  				    	 $("form[name='signin-fm']")[0].reset();                    
  				    	 var status = $(".status");
						     status.text(  "잘못된 접근입니다."  ).addClass("error") ;    
  				         $("#login-btn").kendoAnimate("slideIn:up");
  				     }
  				 });            	
            });
            /* END LOGIN BOX */            
    	});    	
    }
}]);
-->
</script>  
<decorator:head />     
</head>
<body class="off-canvas">
<!-- START Top Site Bar -->
	<div class="top-bar">
		<div class="attached">
			<ul>
				<li><a href="<%= request.getContextPath()  %>/main.do">홈</a></li>
			</ul>			
			<ul class="right">
	            <li><a href="#">진단</a></li>
	            <li><a href="#">학습</a></li>
	            <li><a href="#expand">지원 <span>&#9660;</span></a>
	                <ol class="submenu-box support-menu">
		                <li><a href="http://www.kendoui.com/forums.aspx">Forums</a></li>
		                <li><a href="http://stackoverflow.com/questions/tagged/kendo-ui">StackOverflow Forums</a></li>
	                </ol>
	            </li>
	<%		if ( ! user.isAnonymous()) { %> 	            
	            <li><a href="#expand">시스템 도구<span>&#9660;</span></a>
	                <div class="submenu-box">
		                <h4>TOOLS</h4>
		                <h3><a href="http://www.kendoui.com/dojo.aspx">시스템 도구</a></h3>
		                <hr />
		                <ol id="resources-first">
			                <li><a href="<%= request.getContextPath()  %>/secure/main-user.do">사용자</a></li>
			                <li><a href="<%= request.getContextPath()  %>/secure/main-group.do">그룹</a></li>
			                <li><a href="http://demos.kendoui.com/">권한</a></li>		                
			                <li><a href="<%= request.getContextPath()  %>/secure/main-system-info.do">모니터링</a></li>
		                </ol>
		                <ol>
			                <li><a href="#">로그인</a></li>
			                <li><a href="#">자원</a></li>
		                </ol>
						<ol id="resources-first">
			                <li><a href="http://docs.kendoui.com/">도움말</a></li>
		                </ol>	                
	                </div>
	            </li>
<%			} %>	            
	            <li><a href="http://www.kendoui.com/contact.aspx">Contact Us</a></li>
	<%		if ( ! user.isAnonymous()) { %>            
	        	<li>
	        	    <div style="padding-right: 10px;">
	        		<a class="k-button metro-black" id="expend-top-btn"><%= user.getName() %><span class="k-icon k-si-plus"></span></a>
	        	    </div>
	        	</li>
	        	<li>
	        		<div style="padding-right: 10px;"><a href="<%= request.getContextPath()  %>/logout" class="k-button metro-black" >로그아웃</a></div>       	
	        	</li>	
	<%       	} else {
					boolean isLoginPage = StringUtils.contains(path, "/login.do");
					if (!isLoginPage) { %>
				<li>  			
					<div style="padding-right: 10px;"> 회원이세요?&nbsp;&nbsp; 
					<a href="#" class="k-button signin-button metro-black">로그인&nbsp;<span class="k-icon k-i-arrowhead-s"></span></a>
					</div>				
	  			</li>				
	<%			}else{ %>
				<li>  			
					<div style="padding-right: 10px;">
					<a href="<%= request.getContextPath()  %>/accounts/signup.do"  class="k-button metro-black" >회원가입</a>
					</div>				
	  			</li>
	<%			}
	       		} %>
	        </ul>
	  	</div>
	</div>
	<!-- END Top Site Bar -->
	<div class="k-widget k-window signin_window"  style="display:none;" data-role="draggable">
				<form name="signin-fm" method="POST" accept-charset="utf-8">	
					      <input type="hidden" id="output" name="output" value="json" />	
					      <label for="username">아이디 또는 메일주소</label>
					      <input type="text" id="username" name="username"  class="k-textbox" pattern="[^0-9][A-Za-z]{2,20}" placeholder="아이디" required validationMessage="아이디를 입력하여 주세요."  tabindex="4"/>
				          <label for="password">비밀번호</label>
				          <input type="password" id="password" name="password" class="k-textbox"  placeholder="비밀번호" required validationMessage="비밀번호를 입력하여 주세요." tabindex="5" />
					      <p class="remember">
					      <button type="button" id="login-btn" class="k-button"  tabindex="6" >로그인</button>
					      </p>				      
					      <p class="forgot"> 
						      <a href="#"  id="resend_password_link">비밀번호 찾기</a> 
						      <a id="forgot_username_link" title="비밀번호를 알고 있다면 메일주소를 사용하여 로그인하십시오." href="#">아이디 찾기</a>
					      </p>	
				</form>
	</div>

	<div id="expend-top-section" class="row user-profile-panel" > 
		<div class="twelve columns" >			    
			<div class="row">
	            <div class="six columns"></div>
	            <div class="six columns">	                
					    <table style="padding:5px; margin: 10px;">
						    <tbody>
							    <tr>
								    <td  colspan="2"><a href="#" class="th"><img src="<%= request.getContextPath()  %>/images/anonymous_user_64.png"></a></td>
								</tr>							    
							    <tr>
								    <td><span class="success radius  label"><%= user.getUsername() %></span></td>
									<td><label><%= user.getEmail() %></label></td>
								</tr>	
							    <tr>
								    <td><label>메시지</label></td>
									<td><span class="alert round label">5</span></td>
								</tr>										
							    <tr>
								    <td><label>공지</label></td>
									<td><span class="alert round label">5</span></td>
								</tr>										
							    <tr>
								    <td><label>마지막 회원정보 변경일</label></td>
									<td><%= user.getLastProfileUpdate() %></td>
								</tr>													    
							    <tr>
								    <td><label>마지막 로그인 일자</label></td>
									<td><%= user.getLastLoggedIn() %></td>
								</tr>			
								</tr>													
							</tbody>
						</table>
				</div>
	        </div>			               
			<div class="row">
	            <div class="eight columns"></div>
	            <div class="four columns">
					<a href="<%= request.getContextPath()  %>/accounts/view-profile.do" class="k-button" >회원정보 변경</a>
					<a href="<%= request.getContextPath()  %>/logout" class="k-button" >로그아웃</a>
	            </div>
	        </div>    	      			
		</div>	
	</div>

	<decorator:body />		
	<!-- Footer -->
	<footer class="row">
		<div class="twelve columns">
			<div class="row">
				<div class="six columns">
					<p>카피라이터 추가</p>
				</div>
				<div class="six columns">
					<ul class="link-list right">
						<!-- 
			            <li><a href="#">Link 1</a></li>
			            <li><a href="#">Link 2</a></li>
			            <li><a href="#">Link 3</a></li>
			            <li><a href="#">Link 4</a></li>            
			           -->
					</ul>
				</div>
			</div>
		</div>
	</footer>
</body>
</html>
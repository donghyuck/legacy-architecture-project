<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<%@ page pageEncoding="UTF-8"%>
<%@ page import="architecture.common.user.User"%>
<%@ page import="architecture.common.util.StringUtils"%>
<%@ page import="architecture.ee.web.util.ServletUtils"%>
<%@ page import="architecture.security.util.SecurityHelper"%>
<!DOCTYPE html>
<html>
<head>
<title><decorator:title default="" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="-1">
<meta name="viewport" content="width=device-width" />
<script src="<%=request.getContextPath()%>/js/yepnope.js"></script>
<link href="<%=request.getContextPath()%>/styles/main.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/styles/foundation.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
<!--
yepnope([{
    load: [  
        '<%=request.getContextPath()%>/js/jquery/1.8.2/jquery.min.js',         
        '<%= request.getContextPath()  %>/js/foundation/foundation.min.js' ],
    complete: function(){ 
    	$(document).foundationNavigation();
        $(document).foundationAlerts();
        $(document).foundationButtons();
        $(document).foundationCustomForms();
        
        $('#expend-top-btn').click( function (){        	
        	if( $(this).find('span').hasClass('k-icon k-si-plus') ) {
        		$(this).find('span').removeClass('k-icon k-si-plus')
        		$(this).find('span').addClass('k-icon k-si-minus')
       		    $('#expend-top-section').slideDown();
        	} else {
        		$(this).find('span').removeClass('k-icon k-si-minus')
        		$(this).find('span').addClass('k-icon k-si-plus')        		
        		$('#expend-top-section').slideUp();
        	}
        } );
    }
}]);  
-->
</script>  
<decorator:head />     
</head>
<body class="off-canvas">
	<!-- Header and Nav -->	
<%
    String path = ServletUtils.getServletPath(request);
    User user = SecurityHelper.getUser();
 %>   
	<section class="row" > 	
	     <div class="twelve columns">
			<ul class="nav-bar" style="margin-bottom: 0px; ">
			    <li>
			    <a href="<%= request.getContextPath()  %>/main.do">          홈          </a></li>			
				<li class="has-flyout">
				  <a href="#">시스템</a>
				  <a href="#" class="flyout-toggle"><span></span></a>
				  <ul class="flyout">
				    <li><a href="<%= request.getContextPath()  %>/accounts/admin/main-user.do">사용자 관리</a></li>
				    <li><a href="<%= request.getContextPath()  %>/accounts/admin/main-group.do">그룹 관리</a></li>
				    <li><a href="#">권한 관리</a></li>
				    <li><a href="#">모니터링</a></li>				    
				  </ul>
				</li>	
			</ul>
		</div>	
	</section>		
	<section class="row" >
        <div class="twelve columns" align="right" style="margin-bottom: 5px; padding-bottom: 5px; padding-top: 10px;">
<%
    if (user.isAnonymous()) {
    	boolean isLoginPage = StringUtils.contains(path, "/login.do") ;
		if( isLoginPage ){
%>
		    <a href="<%= request.getContextPath()  %>/accounts/signup.do"  class="k-button" >회원가입</a>
<% 
    	}else{
%>
			<a href="<%= request.getContextPath()  %>/accounts/login.do" class="k-button" >로그인</a>
<%   } 
    }else{
%>    	            
            <a class="k-button" id="expend-top-btn"><%= user.getName() %><span class="k-icon k-si-plus"></span></a>
<% 
    } 
%>	
        </div>
    </section>    	    
    <section id="expend-top-section" class="row"  style="margin-bottom: 0px; padding-bottom: 0px; padding-top: 0px; display:none; "> 
		<div class="six columns" ></div>	
		<div class="six columns" >
		    <div  class="main-section k-widget k-header"  style="margin-top: 0px;">
			<div class="row">
	            <div class="three columns"><a href="#" class="th"><img src="<%= request.getContextPath()  %>/images/anonymous_user_64.png"></a></div>
	            <div class="nine columns">	                
					    <table class="twelve">
						    <tbody>
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
							</tbody>
						</table>
				</div>
	        </div>		
			<div class="row">
                <div class="twelve  columns" align="right">
				    <a href="<%= request.getContextPath()  %>/accounts/view-profile.do" class="k-button" >회원정보 변경</a>
				    <a href="<%= request.getContextPath()  %>/logout" class="k-button" >로그아웃</a>
			    </div>
	        </div>
		</div>		
		</div>	
	</section>    		
	<!-- End Header and Nav -->	
	
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
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
    load: [ '<%= request.getContextPath()  %>/js/jquery/1.8.2/jquery.min.js', '<%= request.getContextPath()  %>/js/foundation/foundation.min.js' ],
    complete: function(){ 
        $(document).foundationNavigation();
        $(document).foundationTabs();
    }
}]);  
-->
</script>  
<decorator:head />     
</head>
<body>
	<!-- Header and Nav -->
	<div class="row">
		<div class="three columns">
			<h1>
				<img src="<%= request.getContextPath()  %>/images/yeti9000.png" />
			</h1>
		</div>
		<div class="nine columns">
			<ul class="nav-bar right">
			    <li class="active"><a href="#">Nav Item 1</a></li>			
				<li class="has-flyout">
				  <a href="#">Nav Item 2</a>
				  <a href="#" class="flyout-toggle"><span> </span></a>
				  <ul class="flyout">
				    <li><a href="#">Sub Nav Item 1</a></li>
				    <li><a href="#">Sub Nav Item 2</a></li>
				    <li><a href="#">Sub Nav 3</a></li>
				    <li><a href="#">Sub Nav 4</a></li>
				    <li><a href="#">Sub Nav Item 5</a></li>
				  </ul>
				</li>			
<%
    String path = ServletUtils.getServletPath(request);
    User user = SecurityHelper.getUser();
    if (user.isAnonymous()) {
    	if( StringUtils.contains(path, "/login.jsp") ){
    		%>
			<li><a href="<%= request.getContextPath()  %>/member/signup.do"  class="small">회원가입</a></li>
			<% 
    	}else{
    		%>
			<li><a href="<%= request.getContextPath()  %>/login.jsp">로그인</a></li>
			<% 
    	}
    }else{    	
		%>
			<li class="has-flyout"><a href="#"><%= user.getName() %></a> 
				<a href="#" class="flyout-toggle"><span> </span></a>
				<div class="flyout large right">
					<h5><%= user.getName() %></h5>
					<div class="row">
						<div class="eight columns">
							<p><%= user.getEmail() %></p>				
						</div>
						<div class="four columns">
							<img src="http://placehold.it/200x250" />
						</div>
					</div>
					<div class="row">
						<div class="eight columns">
							<p><a href="<%= request.getContextPath()  %>/member/view-profile.do" class="k-button" >계정설정</a> <a href="/logout" class="k-button" >로그아웃</a></p>		
						</div>
					</div>
				</div>

			</li>
<%     	
    }
%>
			</ul>
		</div>
	</div>
	<!-- End Header and Nav -->
	<decorator:body />
	<!-- Footer -->
	<footer class="row">
		<div class="twelve columns">
			<hr />
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
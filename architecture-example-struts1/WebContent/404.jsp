<%@ page import="architecture.ee.services.*,architecture.ee.web.util.WebApplicationHelper"%>
<html>
    <head>
        <title>404</title>
        <link href="<%= request.getContextPath()  %>/styles/main.css" rel="stylesheet" />
    </head>
    <body>
     <header>
     <p>존재하지 않는 페이지 입니다.</p>
     </header>      
     <session id="main">
     <img src="<%= request.getContextPath()  %>/images/404.jpg">        
     </session>
    </body>
</html>

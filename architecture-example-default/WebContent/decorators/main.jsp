<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title><decorator:title default="Main Page Templage ..." /></title>    
        
  </head>
  <body>
  <table>
      <tr>
          <td width="200"></td>
          <td>상단 메뉴 위치</td>
      </tr>  
      <tr>
          <td width="200"><td><decorator:head /></td>
      </tr>   
      <tr>
          <td width="200"><td><decorator:body /></td>
      </tr> 
      
       <tr>
          <td width="200"><td>하단 공동 위치 </td>
      </tr>                 
  </table>
  </body>
</html>
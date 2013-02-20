<%@ page pageEncoding="UTF-8"%>
<%@ page import="architecture.ee.services.*,architecture.ee.web.util.WebApplicationHelper"%>
<html>
    <head>
        <title>Query Action 테스트</title>
    </head>
    <body>
        <p>Query Action 테스트</p>
        <form action="/query.do">
        함수: <select name="method">
            <option value="list">list</option>
            <option value="map">map</option>            
            <option value="listWithError">listWithError</option>
        </select>
        <p/>
        쿼리: <input type="text" name= "statement"  value="DEFAULT.SELECT_ALL_V2_ROLES">
        <p/>
        파라메터값:  <input type="text" name= "parameters">
        <p/>
        결과 포맷: <select name="output">
            <option value="html">html</option>
            <option value="xml">xml</option>
            <option value="json">json</option>
        </select>
        <p/>        
         <button type="submit" >실행</button> <button type="reset" >리셋</button>
        </form>
        
    </body>
</html>

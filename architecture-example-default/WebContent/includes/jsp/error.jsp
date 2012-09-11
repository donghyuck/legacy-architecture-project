<%@ page isErrorPage="true"
         import="java.util.Locale,
                 architecture.common.exception.Codeable,
                 architecture.common.util.I18nTextUtils,
                 architecture.ee.util.OutputFormat,
                 architecture.ee.web.util.WebApplicationHelper,
                 architecture.ee.web.util.ParamUtils" %><%
                 
	String formatString = ParamUtils.getParameter(request, "output", "html");
	OutputFormat format = OutputFormat.stingToOutputFormat(formatString);
	
	Throwable ex = exception;
	if( ex == null ){
		// 스트럿츠 오류 처리
		ex = (Throwable)request.getAttribute( org.apache.struts.Globals.EXCEPTION_KEY );
	}
	
	int objectType = 1;
	int objectAttribute = 1 ;
	int errorCode = 0;
	Locale localeToUse = WebApplicationHelper.getLocale();
	
	String exceptionClassName = "";
	String exceptionMessage   = "";
		
	if( ex != null ){		
		if( ex  instanceof Codeable ){
			errorCode = ((Codeable)	ex).getErrorCode();				
		}
		exceptionClassName =  ex.getClass().getName();
		exceptionMessage = ex.getMessage() ;
	}
		
    if(format == OutputFormat.XML ){
    	response.setContentType("text/xml;charset=UTF-8");
%><?xml version="1.0" encoding="UTF-8"?>
<response>
    <error>
        <locale><%= localeToUse %></locale>
        <code><%= I18nTextUtils.generateResourceBundleKey(objectType, errorCode, objectAttribute ) %></code>
        <exception><%= exceptionClassName  %></exception>
        <message><%= exceptionMessage == null ? "" : exceptionMessage %></message>        
    </error>
</response>    	
<%    	
    } else if (format == OutputFormat.JSON ) {
    	response.setContentType("application/json;charset=UTF-8");
%>{"error":{ "locale" : <%= localeToUse %>, "code": <%= I18nTextUtils.generateResourceBundleKey(objectType, errorCode, objectAttribute ) %>, "exception" : <%= exceptionClassName  %>, "message" : <%= exceptionMessage == null ? "" : exceptionMessage %> }}<%	
    } %>
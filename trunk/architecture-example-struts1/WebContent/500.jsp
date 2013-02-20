<%@ page isErrorPage="true"%>
<%@ page import="java.util.*"%>
<%@ page import="org.apache.velocity.exception.MethodInvocationException"%>
<%@ page import="architecture.common.util.StringUtils"%>
<%@ page import="architecture.ee.services.*"%>
<%@ page import="architecture.ee.web.util.WebApplicationHelper"%>
<%@ page import="architecture.ee.web.util.HtmlUtils"%>
<%@ page import="architecture.ee.web.util.PatternLayoutWithStackTrace" %>
<%@ page import="java.lang.reflect.InvocationTargetException"%>

<%
	String context = request.getContextPath();
%>
<html>
<head>
<title>500</title>
<link href="<%=request.getContextPath()%>/styles/main.css" rel="stylesheet" />
</head>
<body>
    <header>ERROR</header>
    <session id="sysErrPanel" class="panel">
		<p>A system error has occurred &mdash; our apologies!</p>

		<ol>
			<li>a description of your problem and what you were doing at the
				time it occurred</li>
			<li>a copy of the error and system information found below</li>
			<li>a copy of the application logs (if possible).</li>
		</ol>

		<p>
			We will respond as promptly as possible.<br> Thank you!
		</p>
		<p>
			<a href="<%=context%>/"><strong>Return to site
					homepage&hellip;</strong></a>
		</p>
	</session>
    <%
        String uri = (String)request.getAttribute("javax.servlet.error.request_uri");    
    %>
    <session id="cause">
	<h3>Cause</h3>
	<%
		String ex = (String) request.getAttribute("javax.servlet.error.message");
		Throwable throwable = exception;
		String causedBy = "";
		
		while (throwable != null) {
			String at = throwable.getStackTrace().length > 0 ? throwable.getStackTrace()[0].toString() : "Unknown location";
	%>
	<p>
		<%=causedBy %><%= HtmlUtils.htmlEncode(String.valueOf(throwable)) %><br>
		&nbsp;&nbsp;&nbsp;&nbsp;at <%= HtmlUtils.htmlEncode(at) %>
	</p>
	<%
		causedBy = "caused by: ";
			if (throwable instanceof InvocationTargetException) {
				throwable = ((InvocationTargetException) throwable)
						.getTargetException();
			} else if (throwable instanceof MethodInvocationException) {
				throwable = ((MethodInvocationException) throwable)
						.getWrappedThrowable();
			} else if (throwable instanceof ServletException) {
				throwable = ((ServletException) throwable).getRootCause();
			} else {
				throwable = throwable.getCause();
			}
		}
	%>
	</session>
	
	<h4>Stack Trace</h4>
	<pre id="stacktrace"><%
            StringBuffer sb = new StringBuffer();
            PatternLayoutWithStackTrace.appendStackTrace(sb, exception);
            out.print(HtmlUtils.htmlEncode(sb.toString()));
        %>
        </pre>	
<h3>Referer URL</h3>
<p><%= request.getHeader("Referer") != null ? HtmlUtils.htmlEncode(request.getHeader("Referer")) : "Unknown" %></p>        
 <h3> Application Information</h3>
        
<h3>Request</h3>
            <%
                try {
            %>
            <h4>Information</h4>
            <dl>
                <dt>URL</dt><dd><%= HtmlUtils.htmlEncode(request.getRequestURL().toString()) %>
                <dt>URI</dt><dd><%= HtmlUtils.htmlEncode(request.getRequestURI()) %>
                <dt>Context Path</dt><dd><%= request.getContextPath() %></dd>
                <dt>Servlet Path</dt><dd><%= request.getServletPath() %></dd>
                <% if (StringUtils.isNotBlank(request.getPathInfo())) { %>
                    <dt>Path Info</dt><dd><%= HtmlUtils.htmlEncode(request.getPathInfo()) %></dd>
                <% } %>
                <% if (StringUtils.isNotBlank(request.getQueryString())) { %>
                <dt>Query String</dt><dd><%= HtmlUtils.htmlEncode(request.getQueryString()) %></dd>
                <% } %>
            </dl>
            <h4>Headers (Limited subset)</h4>
            <dl>
            <%
                String[] headers = new String[]{"host", "x-forwarded-for", "user-agent", "keep-alive", "connection", "cache-control", "if-modified-since", "if-none-match"};
                for (int i = 0; i < headers.length; i++)
                {
                    String name = headers[i];
                    Enumeration headerValues = request.getHeaders(name);
                    if (headerValues == null || !headerValues.hasMoreElements()) continue;
            %>
                    <dt><%= HtmlUtils.htmlEncode(name) %></dt>
            <%
                    while (headerValues.hasMoreElements())
                    {
            %>
                    <dd><%= HtmlUtils.htmlEncode(String.valueOf(headerValues.nextElement()))%></dd>
            <%
                    }
                }
            %>
            </dl>
            <h4>Attributes</h4>
            <dl>
            <%
                for (Enumeration attributeNames = request.getAttributeNames(); attributeNames.hasMoreElements();)
                {
                    String name = String.valueOf(attributeNames.nextElement());
                %>
                    <dt><%= HtmlUtils.htmlEncode(name) %></dt>
                    <dd><%= HtmlUtils.htmlEncode(String.valueOf(request.getAttribute(name)))%></dd>
                <%
                }
            %>
            </dl>
            <h4>Parameters (Limited subset)</h4>
            <dl>
            <%
                for (Enumeration parameterNames = request.getParameterNames(); parameterNames.hasMoreElements();)
                {
                    String name = String.valueOf(parameterNames.nextElement());
                    if (name.contains("pass")) continue;
                %>
                <dt><%= HtmlUtils.htmlEncode(name) %></dt>
                <%
                    String[] parameterValues = request.getParameterValues(name);
                    for (int i = 0; i < parameterValues.length; i++)
                    {
                        %>
                        <dd><%= HtmlUtils.htmlEncode(parameterValues[i]) %></dd>
                        <%
                    }
               }
            %>
            </dl>      
</body>
</html>
<%
    }
    finally
    {
        out.flush();
    }
%>

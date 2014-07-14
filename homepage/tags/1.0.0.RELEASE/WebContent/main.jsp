<html>
	<head>
		<title>Go to System</title>
	</head>
	<body>
		<strong><a href="<%= request.getContextPath() %>/main.do">Click here!</a></strong> to go to System.
		<%= java.net.InetAddress.getLocalHost().getHostAddress() %>
    </body>
</html>
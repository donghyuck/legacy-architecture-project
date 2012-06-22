package architecture.ee.web.util;

import javax.servlet.http.HttpServletRequest;

public class ServletUtils {
	public static String getServletPath(HttpServletRequest request) {
		String thisPath = request.getServletPath();
		if (thisPath == null) {
			String requestURI = request.getRequestURI();
			if (request.getPathInfo() != null)
				thisPath = requestURI.substring(0,
						requestURI.indexOf(request.getPathInfo()));
			else
				thisPath = requestURI;
		} else if (thisPath.equals("") && request.getPathInfo() != null)
			thisPath = request.getPathInfo();
		return thisPath;
	}
}

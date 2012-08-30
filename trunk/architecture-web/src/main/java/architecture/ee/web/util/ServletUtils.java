/*
 * Copyright 2012 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
   
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

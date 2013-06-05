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
package architecture.ee.web.struts2.view.servlet;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.struts2.views.util.DefaultUrlHelper;
import org.apache.struts2.views.util.UrlHelper;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.config.entities.ResultConfig;

public class ServletActionRedirectResult extends ServletRedirectResult {

	private Map<String, Object> requestParameters;
	private String actionName;
	private String namespace;
	private String method;
	protected List<String> prohibitedResultParam;

	public ServletActionRedirectResult() {
		requestParameters = new LinkedHashMap<String, Object>();
		prohibitedResultParam = Arrays.asList(new String[] { 
				"location",
				"namespace", 
				"method", 
				"encode", 
				"parse", 
				"location",
				"prependServletContext", 
				"actionName" });
	}

	public void execute(ActionInvocation invocation) throws Exception {
		actionName = conditionalParse(actionName, invocation);
		if (namespace == null)
			namespace = invocation.getProxy().getNamespace();
		else
			namespace = conditionalParse(namespace, invocation);
		if (method == null)
			method = "";
		else
			method = conditionalParse(method, invocation);
		String resultCode = invocation.getResultCode();
		if (resultCode != null) {
			ResultConfig resultConfig = (ResultConfig) invocation.getProxy().getConfig().getResults().get(resultCode);
			
			Map<String,String> resultConfigParams = resultConfig.getParams();			
			for(java.util.Map.Entry<String,String> e : resultConfigParams.entrySet()){
				if (!prohibitedResultParam.contains(e.getKey()))
					requestParameters.put(e.getKey(), e.getValue() != null ? (conditionalParse(e.getValue(), invocation)) : "");
			}
		}
		StringBuilder tmpLocation = new StringBuilder( actionMapper.getUriFromActionMapping(new ActionMapping( actionName, namespace, method, null)));
		
		UrlHelper urlHelper = new DefaultUrlHelper();
		urlHelper.buildParametersString(requestParameters, tmpLocation, "&");
		
		setLocation(tmpLocation.toString());
		super.execute(invocation);
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
}

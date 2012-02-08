package architecture.ee.web.struts2.view.servlet;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.dispatcher.ServletRedirectResult;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.struts2.views.util.UrlHelper;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.config.entities.ResultConfig;

public class ServletActionRedirectResult extends ServletRedirectResult {

	private Map<String, String> requestParameters;
	private String actionName;
	private String namespace;
	private String method;
	protected List<String> prohibitedResultParam;

	public ServletActionRedirectResult() {
		requestParameters = new LinkedHashMap<String, String>();
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
		StringBuilder tmpLocation = new StringBuilder(
				actionMapper.getUriFromActionMapping(new ActionMapping( actionName, namespace, method, null)));
		
		UrlHelper.buildParametersString(requestParameters, tmpLocation, "&");
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

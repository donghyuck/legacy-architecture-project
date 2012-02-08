package architecture.ee.web.theme.sitemesh;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.util.ApplicatioinConstants;
import architecture.ee.web.util.ParamUtils;

import com.opensymphony.module.sitemesh.Config;
import com.opensymphony.module.sitemesh.Factory;
import com.opensymphony.sitemesh.ContentProcessor;
import com.opensymphony.sitemesh.DecoratorSelector;
import com.opensymphony.sitemesh.compatability.PageParser2ContentProcessor;
import com.opensymphony.sitemesh.webapp.SiteMeshFilter;
import com.opensymphony.sitemesh.webapp.SiteMeshWebAppContext;

public class ThemeFilter extends SiteMeshFilter {

	private static final Log log = LogFactory.getLog(ThemeFilter.class);
	
	private static Factory sitemeshFactory;
		
	private String ignorePrarmeterName ;	
	
	private String[] ignorePrarmeterValues = new String[0];
	
	public ThemeFilter() {
		super();
		
		ignorePrarmeterName = ApplicationHelper.getConfigService().getLocalProperty(ApplicatioinConstants.THTMES_EXCLUDES_PATTERN_PARAMETER_NAME_PROP_NAME, "dataType");
		String ignorePrarmeterValueString =  ApplicationHelper.getConfigService().getLocalProperty(ApplicatioinConstants.THTMES_EXCLUDES_PATTERN_PARAMETER_VALUE_PROP_NAME, "json");
		ignorePrarmeterValues = StringUtils.commaDelimitedListToStringArray(ignorePrarmeterValueString);
		
	}	
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		boolean doIgnore = false;
		
		if( ignorePrarmeterValues.length > 0 ){
			String value = ParamUtils.getParameter((HttpServletRequest)request, ignorePrarmeterName);
			for(String ignore : ignorePrarmeterValues){
				doIgnore = ignore.trim().equals(value) ;
				break;	
			}
		}
		
		if(doIgnore){
			chain.doFilter(request, response);
			return;
		}else{		
			super.doFilter(request, response, chain);
		}	
	}
	
	@Override
	protected ContentProcessor initContentProcessor(
			SiteMeshWebAppContext webAppContext) {
		return new PageParser2ContentProcessor(getSitemeshFactory());
	}

	@Override
	protected DecoratorSelector initDecoratorSelector(
			SiteMeshWebAppContext webAppContext) {
		return new FrameworkDecoratorSelector(getSitemeshFactory().getDecoratorMapper());
	}

	@Override
	public void init(FilterConfig filterConfig) {
		super.init(filterConfig);
		Factory factory = Factory.getInstance(new Config(filterConfig));
		factory.refresh();
		sitemeshFactory = factory ;
	}

	public static void setupSitemeshFactory(ServletConfig servletConfig) {
		sitemeshFactory = Factory.getInstance(new Config(servletConfig));
		sitemeshFactory.refresh();
	}

	public static Factory getSitemeshFactory() {
		return sitemeshFactory;
	}

	public static void ensureFactorySetup(ServletConfig servletConfig) {
		if (getSitemeshFactory() == null)
			setupSitemeshFactory(servletConfig);
	}

}

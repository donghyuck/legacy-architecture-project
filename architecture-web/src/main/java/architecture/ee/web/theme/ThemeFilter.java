package architecture.ee.web.theme;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

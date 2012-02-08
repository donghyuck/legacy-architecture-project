package architecture.ee.web.struts2.view.freemarker;

import java.io.IOException;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import freemarker.cache.WebappTemplateLoader;

public class ExtendedWebappTemplateLoader extends WebappTemplateLoader {

	private Log log = LogFactory.getLog(getClass());
		
	@Override
	public Object findTemplateSource(String name) throws IOException {		
		log.debug("findTemplateSource:"+name);		
		return super.findTemplateSource(name);
		
	}

	@Override
	public long getLastModified(Object templateSource) {
		return super.getLastModified(templateSource);
	}

	public ExtendedWebappTemplateLoader(ServletContext servletContext, String path) {
		super(servletContext, path);
	}

	public ExtendedWebappTemplateLoader(ServletContext servletContext) {
		super(servletContext);
	}

}

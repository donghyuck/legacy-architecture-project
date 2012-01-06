package architecture.ee.web.struts2.view.freemarker;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import freemarker.cache.ClassTemplateLoader;

public class ExtendedClassTemplateLoader extends ClassTemplateLoader {
	
	private Log log = LogFactory.getLog(getClass());
	
	public ExtendedClassTemplateLoader(Class loaderClass, String path) {
		super(loaderClass, path);
	}

	protected URL getURL(String name)
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = null;
        if(loader != null)
            url = loader.getResource(name);
        if(url == null)
            return super.getURL(name);
        else
            return url;
    }

	@Override
	public Object findTemplateSource(String name) throws IOException {
		return super.findTemplateSource(name);
	}
}

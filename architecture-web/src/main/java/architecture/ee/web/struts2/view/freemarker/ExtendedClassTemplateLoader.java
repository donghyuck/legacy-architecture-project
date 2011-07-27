package architecture.ee.web.struts2.view.freemarker;

import java.net.URL;

import freemarker.cache.ClassTemplateLoader;

public class ExtendedClassTemplateLoader extends ClassTemplateLoader {

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
}

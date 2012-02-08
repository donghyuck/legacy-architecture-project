package architecture.ee.web.theme.sitemesh;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.module.sitemesh.DecoratorMapper;
import com.opensymphony.sitemesh.Content;
import com.opensymphony.sitemesh.Decorator;
import com.opensymphony.sitemesh.DecoratorSelector;
import com.opensymphony.sitemesh.SiteMeshContext;
import com.opensymphony.sitemesh.compatability.Content2HTMLPage;
import com.opensymphony.sitemesh.compatability.OldDecorator2NewDecorator;
import com.opensymphony.sitemesh.webapp.SiteMeshWebAppContext;
import com.opensymphony.sitemesh.webapp.decorator.NoDecorator;

public class FrameworkDecoratorSelector implements DecoratorSelector {
    
	private static final Log log = LogFactory.getLog(FrameworkDecoratorSelector.class);
	
	private final DecoratorMapper decoratorMapper;
    
	public FrameworkDecoratorSelector(DecoratorMapper decoratorMapper)
    {
        this.decoratorMapper = decoratorMapper;
    }
    
    public Decorator selectDecorator(Content content, SiteMeshContext context) {
    	
        SiteMeshWebAppContext webAppContext = (SiteMeshWebAppContext) context;
        HttpServletRequest request = webAppContext.getRequest();
        com.opensymphony.module.sitemesh.Decorator decorator = decoratorMapper.getDecorator(webAppContext.getRequest(), new Content2HTMLPage(content, request));

        log.debug("Apply decorator:" + decorator.getPage() );
        
        if (decorator == null || decorator.getPage() == null) {
        	diagnostics(content, context);
            return new NoDecorator();
        } 
        
        if (request.getParameter("sitemeshDispatcher") == null) {
        	return new FrameworkSitemeshDecorator(decorator);
        } else {
            return new OldDecorator2NewDecorator(decorator);            
        }
    }
    	
	private void diagnostics(Content content, SiteMeshContext context){

	}

}

package architecture.ee.web.theme.sitemesh;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.theme.Theme;
import architecture.ee.web.theme.ThemeManager;

import com.opensymphony.module.sitemesh.Config;
import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.DecoratorMapper;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.module.sitemesh.mapper.AbstractDecoratorMapper;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ognl.OgnlValueStack;

public class ThemeDecoratorMapper extends AbstractDecoratorMapper {
    
	private static final Log log = LogFactory.getLog(ThemeDecoratorMapper.class.getName());
	private static final String IGNORE_PROPERTY_KEY = "decorators-ignore";
    
    private static String ignore[] = new String[0];
    
	@Override
	public Decorator getDecorator(HttpServletRequest request, Page page) {

		Decorator decorator = super.getDecorator(request, page);
		if (decorator == null)
			return decorator;
	
		String name = decorator.getName().trim();
		if (!shouldLoad(name))
			return super.getDecorator(request, page);
		
		List<Theme> themes = loadTheme(request);		
		log.debug("Available themes : " + themes.size());		
		if (themes != null && themes.size() > 0)
			return createDecorator(themes, decorator);
		else
			return decorator;
	}

	
	@Override
    public Decorator getNamedDecorator(HttpServletRequest request, String name)
    {
        Decorator decorator = super.getNamedDecorator(request, name);
        if(decorator == null || !shouldLoad(decorator.getName().trim()))
            return decorator;
        List<Theme> themes = loadTheme(request);
        if(themes != null && themes.size() > 0)
            return createDecorator(themes, decorator);
        else
            return decorator;
    }

	@Override
	public void init(Config config, Properties properties, DecoratorMapper parent) throws InstantiationException {
        String ignoreList = properties.getProperty(IGNORE_PROPERTY_KEY);
        if(ignoreList != null)
            ignore = ignoreList.split(",");
        super.init(config, properties, parent);
	}

    private Decorator createDecorator(List<Theme> themes, Decorator decorator)
    {
        return new ThemeDecorator(decorator, decorator.getPage(), themes);
    }
    
	private boolean shouldLoad(String name){
		boolean loadTheme = true;
		for( String anIgnore : ignore ){
			if(anIgnore.trim().equals(name))
            {
                loadTheme = false;
                break;
            }
		}
		return loadTheme;
	}   
	
    private List<Theme> loadTheme(HttpServletRequest request)
    {
        OgnlValueStack stack = (OgnlValueStack)request.getAttribute(ServletActionContext.STRUTS_VALUESTACK_KEY);
        if(stack == null)
            return Collections.EMPTY_LIST;
        else
            return ApplicationHelper.getComponent(ThemeManager.class).determineThemes(new ActionContext(stack.getContext()), request);
        
    }
}

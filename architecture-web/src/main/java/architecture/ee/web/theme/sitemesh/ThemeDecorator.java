package architecture.ee.web.theme.sitemesh;

import java.util.List;

import architecture.ee.web.theme.Theme;

import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.mapper.DefaultDecorator;

public class ThemeDecorator extends DefaultDecorator {
    protected Decorator decorator;
    protected List<Theme> themes;
    
    public ThemeDecorator(Decorator decorator, String page, List<Theme> themes)
    {
        super(decorator.getName(), page, null);
        this.decorator = decorator;
        this.themes = themes;
    }
    
    public String getInitParameter(String paramName)
    {
        return decorator.getInitParameter(paramName);
    }

    public List<Theme> getThemes()
    {
        return themes;
    }
    
}

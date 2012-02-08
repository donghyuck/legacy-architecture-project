package architecture.ee.web.theme;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.opensymphony.xwork2.ActionContext;

public interface ThemeManager {

    public abstract String getThemeHome();
    
    public abstract String getThemeHome(Theme theme);
    
    public abstract List<Theme> determineThemes(ActionContext ac, HttpServletRequest request);
    
    public abstract Theme getGlobalTheme();
    
    public abstract Theme getThemeByURL(String url);
    
    public abstract Map<ThemeMap, String> getThemeMaps();
    
    public abstract Collection<Theme> getThemes();
    
    public abstract List<Theme> getThemesSorted();
    
    public abstract Theme getTheme(String name);

    public abstract Theme getTheme(ThemeMap thememap);
    
    public abstract void reloadThemeMaps();
    
    public abstract String getPathForTheme(Theme theme);

    public abstract File getDirectoryForTheme(Theme theme);
}

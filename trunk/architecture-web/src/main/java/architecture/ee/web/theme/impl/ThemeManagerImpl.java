package architecture.ee.web.theme.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.PatternSyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.ee.admin.AdminHelper;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.struts2.view.freemarker.ExtendedFreemarkerManager;
import architecture.ee.web.theme.Theme;
import architecture.ee.web.theme.ThemeManager;
import architecture.ee.web.theme.ThemeMap;
import architecture.ee.web.theme.ThemeType;
import architecture.ee.web.theme.ThemeXStreamFactory;
import architecture.ee.web.theme.dao.ThemeDao;
import architecture.ee.web.util.ApplicatioinConstants;

import com.opensymphony.xwork2.ActionContext;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;

public class ThemeManagerImpl implements ThemeManager {

	private static final String THEME_KEY = "__theme";

	protected static final String THEME_CONFIG_FILENAME = "theme.xml";
	 
	private Log log = LogFactory.getLog(ThemeManagerImpl.class.getName());
	
	private File themesDir ;
	
	private ExtendedFreemarkerManager extendedFreemarkerManager ;
	
	protected Map<String, Theme> themes;
	
	protected Map<ThemeMap, String> themeMaps;
	
	protected Theme globalTheme;
	
	private ThemeDao themeDao;
	
	public ThemeManagerImpl(){
		themesDir = null;
		extendedFreemarkerManager = null;
	}
		
	public void initialize(){
		themes = loadThemes();
		themeMaps = loadThemeMaps();
	}
	
	public ThemeDao getThemeDao() {
		return themeDao;
	}

	public void setThemeDao(ThemeDao themeDao) {
		this.themeDao = themeDao;
	}

	public ExtendedFreemarkerManager getExtendedFreemarkerManager() {
		return extendedFreemarkerManager;
	}

	public void setExtendedFreemarkerManager(
			ExtendedFreemarkerManager extendedFreemarkerManager) {
		this.extendedFreemarkerManager = extendedFreemarkerManager;
	}

	public String getThemeHome() {
		File themeDir = getThemeDir();
        String path = themeDir == null ? null : themeDir.getAbsolutePath();
		return path;
	}

	public String getThemeHome(Theme theme) {
		String path = (new StringBuilder()).append(getThemeHome()).append(File.separator).append(theme.getName().toLowerCase()).toString();
		return path;
	}

	private synchronized File getThemeDir(){
		
		if(themesDir == null)
        {
            String dir = System.getProperty(ApplicatioinConstants.THEMES_ROOT_ENV_KEY);
            if(dir != null)
            {
                File testDir = new File(dir);
                if(testDir.exists() && testDir.isDirectory() && testDir.canRead())
                    themesDir = testDir;
                else
                    log.error((new StringBuilder()).append("Could not read directory, ").append(dir).append(" specified as themes.directory").toString());
            }
            if(themesDir == null)
            {
                dir = AdminHelper.getConfigService().getLocalProperty("themes.directory");
                if(dir != null)
                {
                    File testDir = new File(dir);
                    if(testDir.exists() && testDir.isDirectory())
                        themesDir = testDir;
                }
            }
            if(themesDir == null)
            {
                themesDir = AdminHelper.getRepository().getFile("themes");
                if(!themesDir.exists())
                    themesDir.mkdir();
                log.debug("theme dir:" + themesDir + ":" + themesDir.exists());
            }
            return themesDir.exists() ? themesDir : null;
        } else
        {
            return themesDir;
        }
	}

	@SuppressWarnings("unchecked")
	public List<Theme> determineThemes(ActionContext ac, HttpServletRequest request) {
		
		List<Theme> list ;
		List<Theme> themes = new ArrayList<Theme>();
		
		if(request == null)
        {
            if(getGlobalTheme() != null)
            {
                themes.add(getGlobalTheme());
                list = themes;
            }else{
            	list = null;
            }
        }
		
		if(request.getAttribute(THEME_KEY) != null)
        {
			list = (List<Theme>)request.getAttribute(THEME_KEY);
        }else{        	
        	
        	for(ThemeType type : ThemeType.getTypes()){
        		Theme theme = null;
        		if(type == ThemeType.URL){
        			String uriHeader = ApplicationHelper.getApplicationProperty(ApplicatioinConstants.THTMES_URI_HEADER_PROP_NAME, null);
        			String uri;
        			if(uriHeader != null){
        				 uri = request.getHeader(uriHeader);
        			}else{
        				uri = request.getRequestURI();
        				String queryString = request.getQueryString();
        				if(queryString != null)
                            uri = (new StringBuilder()).append(uri).append("?").append(queryString).toString();
        			}
        			String url = (new StringBuilder()).append(request.getHeader("Host")).append(uri).toString();
        			theme = getThemeByURL(url);
        		}else{
        			
        		}   
                if(theme != null)
                    themes.add(theme);
        	}        	
    		if(getGlobalTheme() != null)
                themes.add(getGlobalTheme());
            if(themes.size() > 0)
                request.setAttribute(THEME_KEY, themes);
            list = themes;
        }
		
		return list;
	}

	public Theme getThemeByURL(String url){
		if(url == null)
            return null;
        try
        {
        	for( ThemeMap map : getThemeMaps().keySet()){
        		if(map.getType() != null && map.getType().equals(ThemeType.URL))
                {
                    String mapURL = map.getStringValue();
                    if(url.matches(mapURL))
                        return getTheme(map);
                }
        	}
        }
        catch(PatternSyntaxException e)
        {
            log.error("Error loading theme from URL Theme Map: Invalid URL Pattern.", e);
        }
        return null;
	}
	
	public Theme getGlobalTheme(){		
		return globalTheme;
	}
	
	public Map<ThemeMap, String> getThemeMaps(){		
		if(themeMaps == null)
            themeMaps = loadThemeMaps();
		
		return themeMaps;
	}
	
	public Theme getTheme(ThemeMap themeMap){		
		if(themes == null)
            themes = loadThemes();		
        if(themeMaps == null)
            themeMaps = loadThemeMaps();
        		
		return themes.get(themeMaps.get(themeMap));
	}


	public Collection<Theme> getThemes() {
		log.debug( "-----------------------------------------------getThemes" );
        if(this.themes == null)
        	this.themes = loadThemes();        
		return this.themes.values();
	}

	public List<Theme> getThemesSorted() {		
		List<Theme> themes = new ArrayList<Theme>(getThemes());		
        Collections.sort(themes, new Comparator<Theme>() {
			public int compare(Theme o, Theme o1) {
                if(o == null || o.getName() == null)
                    return -1;
                if(o1 == null || o1.getName() == null)
                    return 1;
                else
                    return o.getName().compareToIgnoreCase(o1.getName());
			}
        });
		return themes;
	}

	public Theme getTheme(String name) {
		return themes.get(name);
	}

	public void reloadThemeMaps() {
		themeMaps = loadThemeMaps();		
	}

	public String getPathForTheme(Theme theme) {
		String path ;		
		if(theme == null)
		    path = null;
		else
			path = (new StringBuilder()).append(getThemeHome()).append(File.separator).append(theme.getName()).toString();		
		return path;
	}

	public File getDirectoryForTheme(Theme theme) {
		File file;
        if(theme == null)
            file = null;
        else
            file = new File(getPathForTheme(theme));		
		return file;
	}
	
	
	private Map<String, Theme> loadThemes(){
		
		log.debug( "-----------------------------------------------getThemeDir:" + getThemeDir());
		
		if(getThemeDir() != null)
        {
            Map<String, Theme> themes = new HashMap<String, Theme>();            
            FileFilter dirFilter = new FileFilter() {
                public boolean accept(File file)
                {
                    return file.isDirectory();
                }
            };
            
            
            
            File themeDirs[] = getThemeDir().listFiles(dirFilter);
            
            if(themeDirs != null)
            {
            	for(File themeDir : themeDirs){
            		log.debug("searching theme from " + themeDir.getAbsolutePath() + "************************" );
            		Theme theme = null;
                    try
                    {
                    	log.debug( "loading theme from " + themeDir.getAbsolutePath());
                        theme = loadTheme(themeDir);
                    }
                    catch(Exception e)
                    {
                        log.error("Error loading theme, continuing with loading other possible themes", e);
                    }
                    if(theme != null)
                        themes.put(theme.getName(), theme);
            	}

            }
            if(globalTheme != null)
                globalTheme = (Theme)themes.get(globalTheme.getName());
            return themes;
        } else
        {
            return Collections.emptyMap();
        }
	}
	
	private Map<ThemeMap, String> loadThemeMaps(){
		
		Map<ThemeMap, String> themeMappings = new TreeMap<ThemeMap, String>(new Comparator<ThemeMap>() {
			public int compare(ThemeMap map1, ThemeMap map2)
            {
                if(map1 == null)
                    return -1;
                if(map2 == null)
                    return 1;
                int compare = map1.getType().getPrecedence() - map2.getType().getPrecedence();
                if(compare == 0)
                    compare = map1.hashCode() - map2.hashCode();
                return compare;
            }			
		});
		
		for( ThemeMap themeMap : themeDao.getAllThemeMaps()){
			String theme = themeMap.getThemeName();
			if( themeMap.getType() == ThemeType.GLOBAL ){
			
				globalTheme = themes.get(theme);
			
			}else{
				themeMappings.put(themeMap, theme);
			}			
		}
		return themeMappings;
	}
	
	//getThemeDao
	private Theme loadTheme(File themeDir){
		if(!themeDir.isHidden()){
			XStream xstream = ThemeXStreamFactory.getThemeXStream();
			
			File configs[] = themeDir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name)
                {
                    return name.equals(THEME_CONFIG_FILENAME);
                }				
			});
			
			if( configs.length > 0 ){
				try {
					Theme theme = (Theme)xstream.fromXML(new FileReader(configs[0]));
					if(theme != null){
						if(theme.validate() && theme.getName().toLowerCase().equals(themeDir.getName()))
					    {                        
					        return theme;
					    }
					}
				} catch (FileNotFoundException e) {
					log.error((new StringBuilder()).append("Error reading theme.xml for theme '").append(themeDir.getName()).append("'.").toString(), e);	                
				} catch(ConversionException e) {
					log.error((new StringBuilder()).append("Failed reading theme.xml for theme '").append(themeDir.getName()).append("'.").toString(), e);	                
				}
			}
			log.error((new StringBuilder()).append("Theme '").append(themeDir.getName()).append("' is not configured correctly.").append("Make sure the theme name is specified in the theme.xml file, ").append("and that the name of the theme matches the name of the directory containing the ").append("theme.").toString());
			log.error((new StringBuilder()).append("Error reading theme.xml for theme '").append(themeDir.getName()).append("'.").toString());
			
		}
		log.error((new StringBuilder()).append("theme.xml could not be loaded for theme '").append(themeDir.getName()).append("'.").toString());
        return null;
	}
}
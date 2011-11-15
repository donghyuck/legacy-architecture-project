/*
 * MenuRepository.java
 *
 * Created on January 29, 2001, 9:51 AM
 */
package architecture.ee.web.ui.navigator.menu;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.ee.web.ui.navigator.displayer.MenuDisplayerMapping;
import architecture.ee.web.ui.navigator.displayer.VelocityMenuDisplayer;
import architecture.ee.web.ui.navigator.menu.dao.Menu;
import architecture.ee.web.ui.navigator.menu.dao.MenuDao;

/**
 * Holder of Menus and their items. Can be populated programmatically.
 *
 * @author  ssayles, mraible
 */
public class MenuRepositoryImpl implements Serializable, MenuRepository {
    //~ Static fields/initializers =============================================

    public static final String MENU_REPOSITORY_KEY = "architecture.ee.web.ui.navigator.menu.MENU_REPOSITORY";
    
    private Log log = LogFactory.getLog(getClass());
    private MenuDao menuDao;
    //~ Instance fields ========================================================

    
    protected String config = null;
    protected String name = null;
    protected ServletContext servletContext = null;
    
    protected LinkedMap menus = new LinkedMap();
    protected LinkedMap displayers = new LinkedMap();
    protected LinkedMap templates = new LinkedMap();
    private String breadCrumbDelimiter;

    //~ Methods ================================================================
    public Set getMenuNames() {
        return menus.keySet();
    }

    /**
     * Convenience method for dynamic menus - returns the top-level menus
     * only.
     */
    public List<MenuComponent> getTopMenus() {
        List<MenuComponent> topMenus = new ArrayList<MenuComponent>();
        if (menus == null) {
            log.warn("No menus found in repository!");
            return topMenus;
        }

        
        for (Iterator it = menus.keySet().iterator(); it.hasNext();) {
            String name = (String) it.next();
            MenuComponent menu = getMenu(name);
            if (menu.getParent() == null) {
                topMenus.add(menu);
            }
        }
        return topMenus;
    }

    public MenuComponent getMenu(String menuName) {
        return (MenuComponent) menus.get(menuName);
    }

    public MenuDisplayerMapping getMenuDisplayerMapping(String displayerName) {
        return (MenuDisplayerMapping) displayers.get(displayerName);
    }

    protected Digester initDigester() {
    	
        Digester digester = new Digester();
        digester.setClassLoader(Thread.currentThread().getContextClassLoader());
        digester.push(this);

        //digester.setDebug(getDebug());
        // 1
        digester.addObjectCreate("MenuConfig/Menus/Menu", "architecture.ee.web.ui.navigator.menu.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu");
        digester.addSetNext("MenuConfig/Menus/Menu", "addMenu");

        // 2
        digester.addObjectCreate("MenuConfig/Menus/Menu/Item", "architecture.ee.web.ui.navigator.menu.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu/Item");
        digester.addSetNext("MenuConfig/Menus/Menu/Item", "addMenuComponent", "architecture.ee.web.ui.navigator.menu.MenuComponent");

        // 3        
        digester.addObjectCreate("MenuConfig/Menus/Menu/Item/Item", "architecture.ee.web.ui.navigator.menu.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu/Item/Item");
        digester.addSetNext("MenuConfig/Menus/Menu/Item/Item", "addMenuComponent", "architecture.ee.web.ui.navigator.menu.MenuComponent");

        // 4
        digester.addObjectCreate("MenuConfig/Menus/Menu/Item/Item/Item", "architecture.ee.web.ui.navigator.menu.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu/Item/Item/Item");
        digester.addSetNext("MenuConfig/Menus/Menu/Item/Item/Item", "addMenuComponent", "architecture.ee.web.ui.navigator.menu.MenuComponent");

        // 5
        digester.addObjectCreate("MenuConfig/Menus/Menu/Item/Item/Item/Item", "architecture.ee.web.ui.navigator.menu.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu/Item/Item/Item/Item");
        digester.addSetNext("MenuConfig/Menus/Menu/Item/Item/Item/Item", "addMenuComponent", "architecture.ee.web.ui.navigator.menu.MenuComponent");

        // 6
        digester.addObjectCreate("MenuConfig/Menus/Menu/Item/Item/Item/Item/Item", "architecture.ee.web.ui.navigator.menu.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu/Item/Item/Item/Item/Item");
        digester.addSetNext("MenuConfig/Menus/Menu/Item/Item/Item/Item/Item", "addMenuComponent", "architecture.ee.web.ui.navigator.menu.MenuComponent");

        // 7
        digester.addObjectCreate("MenuConfig/Menus/Menu/Item/Item/Item/Item/Item/Item", "architecture.ee.web.ui.navigator.menu.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu/Item/Item/Item/Item/Item/Item");
        digester.addSetNext("MenuConfig/Menus/Menu/Item/Item/Item/Item/Item/Item", "addMenuComponent", "architecture.ee.web.ui.navigator.menu.MenuComponent");

        // digester.addObjectCreate("MenuConfig/Displayers/Displayer", "net.sf.navigator.displayer.MenuDisplayerMapping", "mapping");
        // digester.addSetProperties("MenuConfig/Displayers/Displayer");
        // digester.addSetNext("MenuConfig/Displayers/Displayer", "addMenuDisplayerMapping", "net.sf.navigator.displayer.MenuDisplayerMapping");
        // digester.addSetProperty("MenuConfig/Displayers/Displayer/SetProperty", "property", "value");

        return digester;
    }

    /**
     * Adds a new menu.  This is called when parsing the menu xml definition.
     * @param menu The menu component to add.
     */
    public void addMenu(MenuComponent menu) {
        if (menus.containsKey(menu.getName())) {            
            if (log.isDebugEnabled()) {
                log.warn("Menu '" + menu.getName() + "' already exists in repository");
            }
            List children = (getMenu(menu.getName())).getComponents();
            if (children != null && menu.getComponents() != null) {
                for (Iterator it = children.iterator(); it.hasNext();) {
                    MenuComponent child = (MenuComponent) it.next();
                    menu.addMenuComponent(child);
                }
            }
        }
        menus.put(menu.getName(), menu);
    }

    /**
     * Allows easy removal of a menu by its name.
     * @param name
     */
    public void removeMenu(String name) {
        menus.remove(name);
    }

    /**
     * Allows easy removal of all menus, suggested use for users wanting to reload menus without having to perform
     * a complete reload of the MenuRepository
     */
    public void removeAllMenus() {
        menus.clear();
    }

    public void addMenuDisplayerMapping(MenuDisplayerMapping displayerMapping) {
        displayers.put(displayerMapping.getName(), displayerMapping);
        if (displayerMapping.getType().equals("net.sf.navigator.displayer.VelocityMenuDisplayer")) {
            if (servletContext == null) {
                log.error("ServletContext not set - can't initialize Velocity");
            } else {
                VelocityMenuDisplayer.initialize(servletContext);
            }
        }
    }

    /**
     * This method is so menu repositories can retrieve displayers from the
     * default repository specified in menu-config.xml
     * @return the displayers specified in this repository
     */
    public LinkedMap getDisplayers() {
        return displayers;
    }

    /**
     * Allow the displayers to be set as a whole.  This should only be used
     * when copying the displayers from the default repository to a newly
     * created repository.
     * @param displayers
     */
    public void setDisplayers(LinkedMap displayers) {
        this.displayers = displayers;
    }

    
    public MenuDao getMenuDao() {
		return menuDao;
	}

	public void setMenuDao(MenuDao menuDao) {
		this.menuDao = menuDao;
	}

	public void load() throws Exception {
		List<Menu> list = menuDao.getMenus();
		for(Menu menu : list){
			try{
	        	StringReader stringreader = new StringReader(menu.getXml());
	            log.debug((new StringBuilder()).append("loading menu data:\n").append(stringreader.toString()).toString());
	            Digester digester = initDigester();
	            digester.parse(stringreader);        	
            }
            catch(Exception exception)
            {
                log.error(exception);
            }
		}
    }
    /*
    {
        if (getServletContext() == null) {
            throw new LoadableResourceException("no reference to servlet context found");
        }

        InputStream input = null;
        Digester digester = initDigester();

        try {
            input = getServletContext().getResourceAsStream(config);
            digester.parse(input);
        } catch (Exception e) {
            e.printStackTrace();
            throw new LoadableResourceException("Error parsing resource file: " + config + " nested exception is: " + e.getMessage());
        } finally {
            try {
                input.close();
            } catch (Exception e) {
            }
        }
    }*/

    public void reload() throws Exception {
        menus.clear();
        displayers.clear();
        load();
    }

    public void setLoadParam(String loadParam) {
        config = loadParam;
    }

    public String getLoadParam() {
        return config;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext context) {
        this.servletContext = context;
    }

    /**
     * Method getMenu.  Get a subMenu beneath a root or parent menu.  Will drill-down as deep as requested
     * @param menuName - e.g grandParent.parent.menu
     * @param delimiter - e.g. '.'
     * @return MenuComponent
     */
    public MenuComponent getMenu(String menuName, String delimiter) {
        MenuComponent parent = null;
        StringTokenizer st = new StringTokenizer(menuName, delimiter);
        boolean firstMenu = true;

        while (st.hasMoreTokens()) {
            if (firstMenu) {
                parent = this.getMenu(st.nextToken());
                firstMenu = false;
            } else {
                MenuComponent child = null;
                String name = st.nextToken();
                for (int a = 0; a < parent.getComponents().size(); a++) {
                    if (name.equals(((MenuComponent) parent.getComponents().get(a)).getName())) {
                        child = (MenuComponent) parent.getComponents().get(a);
                        a = parent.getComponents().size();
                    }
                }
                if (child != null) {
                    parent = child;
                } else {
                    parent = null;
                    break;
                }
            }
        }

        return parent;
    }

    /**
     * Method getMenuDepth.
     * Get the depth of the deepest sub-menu within the requested top menu
     * @param menuName - name of the top menu to check the menu depth 
     * @return int.  If no menuName found return -1
     */
    public int getMenuDepth(String menuName) {

        MenuComponent menu = this.getMenu(menuName);
        if (menu == null)
            return -1;
        if (menu.getMenuComponents() == null)
            return 1;
        return menu.getMenuDepth();
    }

    /**
     * Method getMenuDepth.
     * Get the depth of the deepest sub-menu throughout all menus held in the repository
     * @return int.  If no menus return -1.
     */
    public int getMenuDepth() {
        int currentDepth = 0;

        List topMenus = this.getTopMenus();

        if (topMenus == null)
            return -1;
        for (Iterator menu = topMenus.iterator(); menu.hasNext();) {
            int depth = ((MenuComponent) menu.next()).getMenuDepth();
            if (currentDepth < depth)
                currentDepth = depth;
        }
        return currentDepth;
    }

    /**
     * Method getTopMenusAsArray.  Get menus as array rather than a List
     * @return MenuComponent[]
     */
    public MenuComponent[] getTopMenusAsArray() {
        List menuList = this.getTopMenus();
        MenuComponent[] menus = new MenuComponent[menuList.size()];
        for (int a = 0; a < menuList.size(); a++) {
            menus[a] = (MenuComponent) menuList.get(a);
        }

        return menus;
    }

    /**
     * Get a List of all the top menus' names
     * @return List
     */
    public List getTopMenusNames() {
        List menus = this.getTopMenus();
        ArrayList names = new ArrayList();
        for (Iterator iterator = menus.iterator(); iterator.hasNext();) {
            MenuComponent menu = (MenuComponent) iterator.next();
            names.add(menu.getName());
        }
        return names;
    }

    public void setBreadCrumbDelimiter(String string) {
        breadCrumbDelimiter = string;
    }

    public void buildBreadCrumbs() {
        if (breadCrumbDelimiter == null) {
            throw new NullPointerException("No breadCrumbDelimiter present");
        }
        ArrayList menus = (ArrayList)this.getTopMenus();
        for (Iterator iterator = menus.iterator(); iterator.hasNext();) {
            MenuComponent menu = (MenuComponent)iterator.next();
            menu.setBreadCrumb(breadCrumbDelimiter);
        }
    }

    public void buildBreadCrumbs(String delimiter) {
        this.breadCrumbDelimiter = delimiter;
        buildBreadCrumbs();
    }
}

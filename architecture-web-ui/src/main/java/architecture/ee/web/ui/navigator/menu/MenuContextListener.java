package architecture.ee.web.ui.navigator.menu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.ee.web.ui.navigator.util.LoadableResourceException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 * This class is designed for use in applications that don't use Struts
 * but do want to use Struts Menu.  You simply need to configure this
 * listener in your web.xml file with the following syntax:</p>
 * <pre>
 *   &lt;!--
 *   - Loads the menu-config.xml for struts-menu at startup,
 *   - by default from "/WEB-INF/menu-config.xml".
 *   - To override this, add a context-param named "menuConfigLocation"
 *   - web.xml file.
 *  --&gt;
 * &lt;listener&gt;
 *  &lt;listener-class&gt;net.sf.navigator.menu.MenuContextListener&lt;/listener-class&gt;
 * &lt;/listener&gt;
 * </pre>
 * 
 * @author Matt Raible
 */
public class MenuContextListener implements ServletContextListener {
    private static Log log = LogFactory.getLog(MenuContextListener.class);
    private ServletContext ctx;

    /** Configuration file for menus */
    private String menuConfig = "/WEB-INF/menu-config.xml";

    /**
     * Initialization of the Menu Repository.
     */
    public void contextInitialized(ServletContextEvent sce) {
        ctx = sce.getServletContext();

        if (log.isDebugEnabled()) {
            log.debug("Starting struts-menu initialization");
        }
        
        // check for menuConfigLocation context-param
        String override = 
            sce.getServletContext().getInitParameter("menuConfigLocation");
        if (override != null) {
            if (log.isDebugEnabled()) {
                log.debug("using menuConfigLocation: " + override);
            }
            this.menuConfig = override;
        }
        
       /* MenuRepositoryImpl repository = new MenuRepositoryImpl();
        repository.setLoadParam(menuConfig);
        repository.setServletContext(ctx);

        try {
            repository.load();
            ctx.setAttribute(MenuRepositoryImpl.MENU_REPOSITORY_KEY, repository);

            if (log.isDebugEnabled()) {
                log.debug("struts-menu initialization successfull");
            }
        } catch (LoadableResourceException lre) {
            log.fatal("Failure initializing struts-menu: " + lre.getMessage());
        }*/
    }

    public void contextDestroyed(ServletContextEvent sce) {
        if (log.isDebugEnabled()) {
            log.debug("destroying struts-menu...");
        }

        sce.getServletContext().removeAttribute(MenuRepositoryImpl.MENU_REPOSITORY_KEY);
        menuConfig = null;
        ctx = null;
    }
}

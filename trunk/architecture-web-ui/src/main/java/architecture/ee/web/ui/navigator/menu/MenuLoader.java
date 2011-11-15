package architecture.ee.web.ui.navigator.menu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.context.support.WebApplicationObjectSupport;

import architecture.ee.web.ui.navigator.util.LoadableResourceException;

import javax.servlet.ServletException;

/**
 * This loader is available for those that use the Spring Framework.  To 
 * use it, simply configure it as follows in your applicationContext.xml file.
 * </p>
 * <pre>
 * &lt;bean id="menu" class="net.sf.navigator.menu.MenuLoader"&gt;
 *  &lt;property name="menuConfig"&gt;
 *      &lt;value&gt;/WEB-INF/menu-config.xml&lt;/value&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * <p>The menuConfig property is an optional attribute.  It is set to 
 * /WEB-INF/menu-config.xml by default.</p>
 * 
 * @author Matt Raible
 */
public class MenuLoader extends WebApplicationObjectSupport {
    private static Log log = LogFactory.getLog(MenuLoader.class);

    /** Configuration file for menus */
    private String menuConfig = "/WEB-INF/menu-config.xml";

    /**
     * Set the Menu configuration file
     * @param menuConfig the file containing the Menus/Items
     */
    public void setMenuConfig(String menuConfig) {
        this.menuConfig = menuConfig;
    }

    /**
     * Initialization of the Menu Repository.
     * @throws org.springframework.context.ApplicationContextException if an error occurs
     */
    protected void initApplicationContext() throws ApplicationContextException {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Starting struts-menu initialization");
            }

           /* MenuRepositoryImpl repository = new MenuRepositoryImpl();
            repository.setLoadParam(menuConfig);
            repository.setServletContext(getServletContext());

            try {
                repository.load();
                getServletContext().setAttribute(MenuRepositoryImpl.MENU_REPOSITORY_KEY, repository);

                if (log.isDebugEnabled()) {
                    log.debug("struts-menu initialization successful");
                }
            } catch (LoadableResourceException lre) {
                throw new ServletException("Failure initializing struts-menu: " +
                                           lre.getMessage());
            }*/
        } catch (Exception ex) {
            throw new ApplicationContextException("Failed to initialize Struts Menu repository",
                                                  ex);
        }
    }
}

/*
 * MenuPlugin.java
 *
 * Created on March 6, 2002, 10:04 PM
 */
package architecture.ee.web.ui.navigator.menu;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

import architecture.ee.web.ui.navigator.util.LoadableResourceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


/**
 * Struts plug-in adapter for the menuing module.
 *
 * @author  ssayles
 */
public class MenuPlugIn implements PlugIn {
    //~ Instance fields ========================================================

    /**
     * The <code>Log</code> instance for this class.
     */
    private static Log log = LogFactory.getLog(MenuPlugIn.class);
    private MenuRepositoryImpl repository;
    private String menuConfig = "/WEB-INF/menu-config.xml";
    private HttpServlet servlet;

    //~ Methods ================================================================

    public String getMenuConfig() {
        return menuConfig;
    }

    public void setMenuConfig(String menuConfig) {
        this.menuConfig = menuConfig;
    }

    public void init(ActionServlet servlet, ModuleConfig config)
    throws ServletException {
        if (log.isDebugEnabled()) {
            log.debug("Starting struts-menu initialization");
        }

       /* this.servlet = servlet;
        repository = new MenuRepositoryImpl();
        repository.setLoadParam(menuConfig);
        repository.setServletContext(servlet.getServletContext());

        try {
            repository.load();
            servlet.getServletContext().setAttribute(MenuRepositoryImpl.MENU_REPOSITORY_KEY, repository);

            if (log.isDebugEnabled()) {
                log.debug("struts-menu initialization successful");
            }
        } catch (LoadableResourceException lre) {
            throw new ServletException("Failure initializing struts-menu: " +
                lre.getMessage());
        }*/
    }

    public void destroy() {
        repository = null;
        servlet.getServletContext().removeAttribute(MenuRepositoryImpl.MENU_REPOSITORY_KEY);
        menuConfig = null;
        servlet = null;
    }
}

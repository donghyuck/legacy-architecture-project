/*
 * Copyright 2016 donghyuck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package architecture.web.ui.menu;

import java.io.InputStream;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.ServletContextResourceLoader;

public class DefaultMenuManager implements MenuManager, InitializingBean, ServletContextAware {

    private ServletContextResourceLoader resourceRoader ;
    private String menuConfigLocation = "/WEB-INF/menu-config.xml";;
    private MenuRepository menuRepository = new XmlMenuRepository();
    
    public DefaultMenuManager() {
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
	resourceRoader = new ServletContextResourceLoader( servletContext );
    }


    @Override
    public void afterPropertiesSet() throws Exception {
	
	InputStream is = resourceRoader.getResource(menuConfigLocation).getInputStream();
	menuRepository.reload(is);
	
    }
    @Override
    public MenuRepository getMenuRepository() {
	return null;
    }

    public ServletContextResourceLoader getResourceRoader() {
        return resourceRoader;
    }

    public void setResourceRoader(ServletContextResourceLoader resourceRoader) {
        this.resourceRoader = resourceRoader;
    }

    public String getMenuConfigLocation() {
        return menuConfigLocation;
    }

    public void setMenuConfigLocation(String menuConfigLocation) {
        this.menuConfigLocation = menuConfigLocation;
    }


}

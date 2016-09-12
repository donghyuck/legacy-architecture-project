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

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.ServletContextResourceLoader;

public class DefaultMenuManager implements MenuManager, InitializingBean, ServletContextAware {

    private Logger logger = LoggerFactory.getLogger(DefaultMenuManager.class);
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
	
	Resource resource = resourceRoader.getResource(menuConfigLocation);
	
	logger.debug("loading menu from :" + resource.toString());
	
	menuRepository.reload(resource.getInputStream());
	
    }
    @Override
    public MenuRepository getMenuRepository() {
	return menuRepository;
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

/*
 * Copyright 2012, 2013 Donghyuck, Son
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
package architecture.ee.web.navigator;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public abstract class AbstractMenuRepository implements MenuRepository, EventSource {

	protected Log log = LogFactory.getLog(getClass());
	protected EventPublisher eventPublisher;
	protected Cache menuCache;
	protected Cache menuIdCache ;
	protected boolean caseInsensitiveGroupNameMatch = true ;
	
	protected LinkedMap menus = new LinkedMap();
	
	public Cache getMenuCache() {
		return menuCache;
	}

	public void setMenuCache(Cache menuCache) {
		this.menuCache = menuCache;
	}

	public Cache getMenuIdCache() {
		return menuIdCache;
	}

	public void setMenuIdCache(Cache menuIdCache) {
		this.menuIdCache = menuIdCache;
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	
    public Menu getMenu(String name)
        throws MenuNotFoundException
    {
    	String nameToUse = caseMenuName(name);
    	if( menuIdCache.get(nameToUse)  != null ){
    		log.debug( menuIdCache.get(nameToUse )) ;
    		Long menuId = (Long)menuIdCache.get(nameToUse).getValue();    		
    		return getMenu(menuId);
    	}else{
    		Menu g = getMenuByName(nameToUse);
    		menuIdCache.put( new Element( nameToUse, g.getMenuId() ) );
    		return getMenu(g.getMenuId());
    	}
    }

	public Menu getMenu(long menuId) throws MenuNotFoundException {
		Menu menu = getMenuInCache(menuId);		
		if (menu == null) {
			 menu = getMenuById(menuId);
			 menuCache.put(new Element(menuId, menu));
		}
		return menu;
	}
    
	protected Menu getMenuInCache(long groupId){
		if( menuCache.get(groupId) != null)
			return  (Menu) menuCache.get( groupId ).getValue();
		else 
			return null;
	}
	
    protected String caseMenuName(String name)
    {
        return caseInsensitiveGroupNameMatch ? name.toLowerCase() : name;
    }
    
    protected abstract Menu getMenuByName(String name) throws MenuNotFoundException;
	
    protected abstract Menu getMenuById(long menuId)  throws MenuNotFoundException;
    
    protected boolean nameEquals(Menu m1, Menu m2){
    	return m1.getName() != null && m2.getName() != null && caseMenuName(m1.getName()).equals(caseMenuName(m2.getName()));
    }
    
    protected void menuNameUpdated(String oldMenuName){
        menuIdCache.remove(caseMenuName(oldMenuName));	
    }
    
    protected void clearMenuFromCache(Menu menu){
    	menuCache.remove(menu.getMenuId());
    }

    
}

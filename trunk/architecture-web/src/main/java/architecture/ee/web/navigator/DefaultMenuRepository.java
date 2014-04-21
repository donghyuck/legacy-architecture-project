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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import architecture.ee.web.navigator.dao.MenuDao;

public class DefaultMenuRepository extends AbstractMenuRepository {
	
	private MenuDao menuDao ;
	
	private Map<String, MenuHolder> holders = new HashMap<String, MenuHolder>();
	
	
	public MenuDao getMenuDao() {
		return menuDao;
	}

	public void setMenuDao(MenuDao menuDao) {
		this.menuDao = menuDao;
	}
	
	protected MenuHolder getMenuHolder(Menu menu){
		MenuHolder holder = holders.get(menu.getName());
		if( holder == null ){
			XmlMenuHolder xmlMenuHolder = new XmlMenuHolder(menu.getMenuData());
			xmlMenuHolder.reload();
			holder = xmlMenuHolder;
			holders.put(menu.getName(), holder);
		}
		return holder;
	}
	
	public MenuComponent getMenuComponent(Menu menu, String menuName) throws MenuNotFoundException {
		MenuHolder holder = getMenuHolder(menu);
		return holder.getMenu(menuName);
	}
	
	public MenuComponent getMenuComponent(String name, String menuName) throws MenuNotFoundException {
		Menu menu = getMenu(name);
		MenuHolder holder = getMenuHolder(menu);
		return holder.getMenu(menuName);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void createMenu(String name, String title, String location, String xml) throws MenuAlreadyExistsException {
		Date now = new Date(System.currentTimeMillis());
		DefaultMenu imp = new DefaultMenu();
		imp.setName(name);
		imp.setLoaction(location);
		imp.setTitle(title);
		imp.setMenuData(xml);		
		imp.setCreationDate(now);
		imp.setModifiedDate(now);
		menuDao.createMenu(imp);
	}

	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void updateMenu(Menu menu) {
		Date now = new Date(System.currentTimeMillis());
		if( menu.getMenuId() <1 ){		
			
			menu.setCreationDate(now);
			menu.setModifiedDate(now);
			menuDao.createMenu(menu);
		}else{
			holders.remove(menu.getName());
			menu.setModifiedDate(now);
			menuDao.updateMenu(menu);		
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void removeMenu(Menu menu) {
		holders.remove(menu.getName());
		menuNameUpdated(menu.getName());
		clearMenuFromCache(menu);		
		menuDao.removeMenu(menu);		
	}

	public void refershMenu(Menu menu) {
		holders.remove(menu.getName());
		clearMenuFromCache(menu);
	}

	@Override
	protected Menu getMenuByName(String name) throws MenuNotFoundException {
		Menu menu = menuDao.getMenuByName(name);
		if(menu == null){
		    //    throw CodeableException.newException(MenuNotFoundException.class, 5142, name);//
			throw new MenuNotFoundException((new StringBuilder()).append("No menu found for with name ").append(name).toString());
		}
	    return menu;
	}

	@Override
	protected Menu getMenuById(long menuId) throws MenuNotFoundException {
		if(menuId == -2L)
	        return null ;
		Menu menu = menuDao.getMenuById(menuId);
		if(menu == null)
			throw new MenuNotFoundException((new StringBuilder()).append("No menu found for with id ").append(menuId).toString());
		return menu;
	}

	public int getTotalMenuCount() {
		return menuDao.getTotalMenuCount();
	}

	public List<Menu> getMenus() {
		List<Long> ids = menuDao.getAllMenuIds();
		List<Menu> menus = new ArrayList<Menu>(ids.size());
		for( Long menuId : ids ){
			try {
				Menu menu = getMenu(menuId.longValue());
				menus.add(menu);
			} catch (MenuNotFoundException e) {
			}
		}		
		return menus;
	}

	public List<Menu> getMenus(int startIndex, int numResults) {
		List<Long> ids = menuDao.getMenuIds(startIndex, numResults);
		List<Menu> menus = new ArrayList<Menu>(ids.size());
		for( Long menuId : ids ){
			try {
				Menu menu = getMenu(menuId.longValue());
				menus.add(menu);
			} catch (MenuNotFoundException e) {
			}
		}		
		return menus;
	}

	public void destory(){
		
	}
	
	public void initialize(){
		
	}
}

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

import java.util.List;
import java.util.Set;

public interface MenuRepository {

	public Set<String> getMenuNames(Menu menu)throws MenuNotFoundException;
	
	public MenuComponent getMenuComponent(String name, String menu) throws MenuNotFoundException;
		
	public MenuComponent getMenuComponent(Menu menu, String name) throws MenuNotFoundException;
	
	public abstract int getTotalMenuCount();

	public abstract List<Menu> getMenus();

	public abstract List<Menu> getMenus(int startIndex, int numResults);
	
	public Menu getMenu(long menuId) throws MenuNotFoundException;
	
	public Menu getMenu(String name) throws MenuNotFoundException;
	
	public void createMenu(String name, String title, String location, String xml ) throws MenuAlreadyExistsException;
	
	public void updateMenu(Menu menu);
	
	public void removeMenu(Menu menu);
	
	public void refershMenu(Menu menu);

}

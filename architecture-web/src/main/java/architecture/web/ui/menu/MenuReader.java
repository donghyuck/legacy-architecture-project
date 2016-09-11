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

import java.util.List;
import java.util.Set;

import architecture.web.ui.LoadableResourceException;

public interface MenuReader {
    
    public Set<String> getMenuNames();

    public List<MenuComponent> getTopMenus();

    public MenuComponent getMenu(String menuName);

    public void addMenu(MenuComponent menu);

    public void removeMenu(String name);

    public void removeAllMenus();

    public void reload() throws LoadableResourceException;

    public MenuComponent getMenu(String menuName, String delimiter);

    public int getMenuDepth(String menuName);

    public int getMenuDepth();

    public MenuComponent[] getTopMenusAsArray();

    public List<String> getTopMenusNames();

    public void setBreadCrumbDelimiter(String string);

    public void buildBreadCrumbs();

    public void buildBreadCrumbs(String delimiter);
}

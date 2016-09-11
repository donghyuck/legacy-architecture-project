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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.digester3.Digester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import architecture.web.ui.LoadableResourceException;

public abstract class AbstractMenuReader implements MenuReader {

    public AbstractMenuReader() {
    }

    private String breadCrumbDelimiter;
    protected Logger log = LoggerFactory.getLogger(getClass());
    protected LinkedMap<String , MenuComponent> menus = new LinkedMap<String , MenuComponent>();

    // ~ Methods
    // ================================================================
    public Set<String> getMenuNames() {
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

        for (Iterator<String> it = menus.keySet().iterator(); it.hasNext();) {
            String name = it.next();
            MenuComponent menu = getMenu(name);
            if (menu.getParent() == null) {
                topMenus.add(menu);
            }
        }
        return topMenus;
    }

    public MenuComponent getMenu(String menuName) {
        return menus.get(menuName);
    }

    protected Digester initDigester() {
        Digester digester = new Digester();
        digester.setClassLoader(Thread.currentThread().getContextClassLoader());
        digester.push(this);

        //digester.setDebug(getDebug());
        // 1
        digester.addObjectCreate("MenuConfig/Menus/Menu", "architecture.web.ui.menu.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu");
        digester.addSetNext("MenuConfig/Menus/Menu", "addMenu");

        // 2
        digester.addObjectCreate("MenuConfig/Menus/Menu/Item", "architecture.web.ui.menu.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu/Item");
        digester.addSetNext("MenuConfig/Menus/Menu/Item", "addMenuComponent", "architecture.web.ui.menu.MenuComponent");

        // 3        
        digester.addObjectCreate("MenuConfig/Menus/Menu/Item/Item", "architecture.web.ui.menu.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu/Item/Item");
        digester.addSetNext("MenuConfig/Menus/Menu/Item/Item", "addMenuComponent", "architecture.web.ui.menu.MenuComponent");

        // 4
        digester.addObjectCreate("MenuConfig/Menus/Menu/Item/Item/Item", "architecture.web.ui.menu.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu/Item/Item/Item");
        digester.addSetNext("MenuConfig/Menus/Menu/Item/Item/Item", "addMenuComponent", "architecture.web.ui.menu.MenuComponent");

        // 5
        digester.addObjectCreate("MenuConfig/Menus/Menu/Item/Item/Item/Item", "architecture.web.ui.menu.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu/Item/Item/Item/Item");
        digester.addSetNext("MenuConfig/Menus/Menu/Item/Item/Item/Item", "addMenuComponent", "architecture.web.ui.menu.MenuComponent");

        // 6
        digester.addObjectCreate("MenuConfig/Menus/Menu/Item/Item/Item/Item/Item", "architecture.web.ui.menu.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu/Item/Item/Item/Item/Item");
        digester.addSetNext("MenuConfig/Menus/Menu/Item/Item/Item/Item/Item", "addMenuComponent", "architecture.web.ui.menu.MenuComponent");

        // 7
        digester.addObjectCreate("MenuConfig/Menus/Menu/Item/Item/Item/Item/Item/Item", "architecture.web.ui.menu.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu/Item/Item/Item/Item/Item/Item");
        digester.addSetNext("MenuConfig/Menus/Menu/Item/Item/Item/Item/Item/Item", "addMenuComponent", "architecture.web.ui.menu.MenuComponent");

        digester.addObjectCreate("MenuConfig/Displayers/Displayer", "net.sf.navigator.displayer.MenuDisplayerMapping", "mapping");
        digester.addSetProperties("MenuConfig/Displayers/Displayer");
        digester.addSetNext("MenuConfig/Displayers/Displayer", "addMenuDisplayerMapping", "net.sf.navigator.displayer.MenuDisplayerMapping");
        digester.addSetProperty("MenuConfig/Displayers/Displayer/SetProperty", "property", "value");

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
            List<MenuComponent> children = (getMenu(menu.getName())).getComponents();
            if (children != null && menu.getComponents() != null) {
                for (Iterator<MenuComponent> it = children.iterator(); it.hasNext();) {
                    MenuComponent child = it.next();
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

    public abstract void reload() throws LoadableResourceException;

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

        List<MenuComponent> topMenus = this.getTopMenus();

        if (topMenus == null)
            return -1;
        for (Iterator<MenuComponent> menu = topMenus.iterator(); menu.hasNext();) {
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
        List<MenuComponent> menuList = this.getTopMenus();
        MenuComponent[] menus = new MenuComponent[menuList.size()];
        for (int a = 0; a < menuList.size(); a++) {
            menus[a] = menuList.get(a);
        }

        return menus;
    }

    /**
     * Get a List of all the top menus' names
     * @return List
     */
    public List<String> getTopMenusNames() {
        List<MenuComponent> menus = this.getTopMenus();
        ArrayList<String> names = new ArrayList<String>();
        for (Iterator<MenuComponent> iterator = menus.iterator(); iterator.hasNext();) {
            MenuComponent menu = iterator.next();
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
        List<MenuComponent> menus = this.getTopMenus();
        for (Iterator<MenuComponent> iterator = menus.iterator(); iterator.hasNext();) {
            MenuComponent menu = iterator.next();
            menu.setBreadCrumb(breadCrumbDelimiter);
        }
    }

    public void buildBreadCrumbs(String delimiter) {
        this.breadCrumbDelimiter = delimiter;
        buildBreadCrumbs();
    }

}

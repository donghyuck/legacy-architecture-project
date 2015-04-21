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
package architecture.ee.web.ws;

import architecture.ee.web.navigator.MenuComponent;

public class MenuItem {
	private String menu;
	private String name;
	private String title;
	private String page;
	private String description;
	private String icon;
	private String roles;
	private boolean last = false;
	int depth = 0;
	boolean child = false;
	boolean progenitor = false;
	
	/**
	 * 
	 */
	public MenuItem() {
		this.name = null;
		this.title = null;
		this.page = null;
		this.description = null;
		this.icon = null;
		this.roles = null;
		this.menu = null;
	}

	public MenuItem(MenuComponent mc, String menu,boolean progenitor) {
		this();
		this.depth = mc.getMenuDepth();
		this.page = mc.getPage();
		this.roles = mc.getRoles();
		this.name = mc.getName();
		this.title = mc.getTitle();
		this.description = mc.getDescription();
		this.last = mc.isLast();
		this.menu = menu;
		this.progenitor = progenitor;
		if( mc.getComponents() != null && mc.getComponents().size() > 0 )
			this.child = true;
	}



	/**
	 * @return progenitor
	 */
	public boolean isProgenitor() {
		return progenitor;
	}

	/**
	 * @param progenitor 설정할 progenitor
	 */
	public void setProgenitor(boolean progenitor) {
		this.progenitor = progenitor;
	}

	/**
	 * @return child
	 */
	public boolean isChild() {
		return child;
	}

	/**
	 * @param child 설정할 child
	 */
	public void setChild(boolean child) {
		this.child = child;
	}

	/**
	 * @return menu
	 */
	public String getMenu() {
		return menu;
	}

	/**
	 * @param menu
	 *            설정할 menu
	 */
	public void setMenu(String menu) {
		this.menu = menu;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            설정할 name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            설정할 title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return page
	 */
	public String getPage() {
		return page;
	}

	/**
	 * @param page
	 *            설정할 page
	 */
	public void setPage(String page) {
		this.page = page;
	}

	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            설정할 description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 *            설정할 icon
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @return roles
	 */
	public String getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            설정할 roles
	 */
	public void setRoles(String roles) {
		this.roles = roles;
	}

	/**
	 * @return last
	 */
	public boolean isLast() {
		return last;
	}

	/**
	 * @param last
	 *            설정할 last
	 */
	public void setLast(boolean last) {
		this.last = last;
	}

	/**
	 * @return depth
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * @param depth
	 *            설정할 depth
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}

}

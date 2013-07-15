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

import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;

public class XmlMenuHolder extends AbstractMenuHolder{

	private String content ;
	
	public XmlMenuHolder(String content) {
		this.content = content;
	}

	public void reload() {
        menus.clear();
		StringReader reader = new StringReader(content); 
		Digester digester = initDigester();
		try {
			digester.parse(reader);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}		
	}


    protected Digester initDigester() {
    	
        Digester digester = new Digester();
        digester.setClassLoader(Thread.currentThread().getContextClassLoader());
        digester.push(this);
        
        // 1
        digester.addObjectCreate("MenuConfig/Menus/Menu", "architecture.ee.web.navigator.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu");
        digester.addSetNext("MenuConfig/Menus/Menu", "addMenu");

        // 2
        digester.addObjectCreate("MenuConfig/Menus/Menu/Item", "architecture.ee.web.navigator.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu/Item");
        digester.addSetNext("MenuConfig/Menus/Menu/Item", "addMenuComponent", "architecture.ee.web.navigator.MenuComponent");

        // 3        
        digester.addObjectCreate("MenuConfig/Menus/Menu/Item/Item", "architecture.ee.web.navigator.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu/Item/Item");
        digester.addSetNext("MenuConfig/Menus/Menu/Item/Item", "addMenuComponent", "architecture.ee.web.navigator.MenuComponent");

        // 4
        digester.addObjectCreate("MenuConfig/Menus/Menu/Item/Item/Item", "architecture.ee.web.navigator.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu/Item/Item/Item");
        digester.addSetNext("MenuConfig/Menus/Menu/Item/Item/Item", "addMenuComponent", "architecture.ee.web.navigator.MenuComponent");

        // 5
        digester.addObjectCreate("MenuConfig/Menus/Menu/Item/Item/Item/Item", "architecture.ee.web.navigator.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu/Item/Item/Item/Item");
        digester.addSetNext("MenuConfig/Menus/Menu/Item/Item/Item/Item", "addMenuComponent", "architecture.ee.web.navigator.MenuComponent");

        // 6
        digester.addObjectCreate("MenuConfig/Menus/Menu/Item/Item/Item/Item/Item", "architecture.ee.web.navigator.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu/Item/Item/Item/Item/Item");
        digester.addSetNext("MenuConfig/Menus/Menu/Item/Item/Item/Item/Item", "addMenuComponent", "architecture.ee.web.navigator.MenuComponent");

        // 7
        digester.addObjectCreate("MenuConfig/Menus/Menu/Item/Item/Item/Item/Item/Item", "architecture.ee.web.navigator.MenuComponent", "type");
        digester.addSetProperties("MenuConfig/Menus/Menu/Item/Item/Item/Item/Item/Item");
        digester.addSetNext("MenuConfig/Menus/Menu/Item/Item/Item/Item/Item/Item", "addMenuComponent", "architecture.ee.web.navigator.MenuComponent");

        /**
        digester.addObjectCreate("MenuConfig/Displayers/Displayer", "net.sf.navigator.displayer.MenuDisplayerMapping", "mapping");
        digester.addSetProperties("MenuConfig/Displayers/Displayer");
        digester.addSetNext("MenuConfig/Displayers/Displayer", "addMenuDisplayerMapping", "net.sf.navigator.displayer.MenuDisplayerMapping");
        digester.addSetProperty("MenuConfig/Displayers/Displayer/SetProperty", "property", "value");
        */

        return digester;
    }


}

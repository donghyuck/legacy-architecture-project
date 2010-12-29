/*
 * Copyright 2010 INKIUM, Inc.
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

package architecture.common.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParserFactory;

import javolution.util.FastMap;
import javolution.util.FastList;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * JAR 에 포함된 META-INF/impl-factory.xml 파일에 정의된 정보를 기반으로 객체 인스턴스를 
 * 생성하는 유틸리티. 하나 이상의 파일이 검색된 경우 XML 에 정의된  lank 정보를 가지고 
 * 우선순위를 판단하여 높은 순위의 것을 생성하여 리턴.
 * 
 * <p>
 * (이 클래스는 javolution 을 사용하여 구현됨.)
 * 
 * @author DongHyuck, Son
 * 
 */
public class ImplFactory {
	
	private static class Factory {

		public String interfaceName;

		public String implName;

		public boolean isOverride;

		public String platforms;

		private Factory() {
			isOverride = false;
			platforms = null;
		}

		public String toString() {
			
			StringBuilder builder = new StringBuilder();
			builder.append('[');
			builder.append(interfaceName);
			builder.append('=');
			builder.append(implName);
			builder.append(',');
			builder.append(isOverride);
			builder.append(',');
			builder.append(platforms);
			builder.append(']');
			return builder.toString(); //'[' + interfaceName + '=' + implName + ',' + isOverride + ',' + platforms + ']';
		}
		
	}
	
	private static class FactoryList {

		public int rank;
		public List<Factory> factories;

		private FactoryList() {
			factories = new ArrayList<Factory>();
		}

		public String toString() {
			
			StringBuilder builder = new StringBuilder();
			builder.append("[ rank = ");
			builder.append(rank);
			builder.append(',');
			builder.append(factories);
			builder.append(']');			
			return builder.toString(); //"[ rank = " + rank + ',' + factories + ']';
		}
		
	}
	

	private static class ImplFactoryParsingHandler extends DefaultHandler {

		static {
			ImplFactory.actualPlatformString = "distributed";
		}

		private static boolean validOn(String s) {
			if (s == null)
				return true;
			String as[] = s.split(",");
			int i = 0;
			for (int j = as.length; i < j; i++) {
				String s1 = as[i].trim();
				if (s1.equalsIgnoreCase("all"))
					return true;
				if (s1.equalsIgnoreCase(ImplFactory.actualPlatformString))
					return true;
			}
			return false;
		}

		Locator locator;

		int rank;

		List<Factory> factories;

		Factory factory;

		StringBuffer value;

		public ImplFactoryParsingHandler() {
			rank = 0;
			factories = new ArrayList<Factory>();
			value = new StringBuffer();
		}

		public void characters(char ac[], int i, int j) throws SAXException {
			value.append(ac, i, j);
		}

		public void endDocument() throws SAXException {
		}

		public void endElement(String s, String s1, String s2) throws SAXException {
			if (s2.equals("rank"))
				try {
					rank = Integer.parseInt(value.toString());
				} catch (NumberFormatException e) {
					throw new SAXParseException("Problem converting rank to number: " + value.toString(), locator);
				}
			else if (s2.equals("factory") && factory != null)
				if (validOn(factory.platforms))
					factories.add(factory);
				else
					factory = null;
			value.setLength(0);
		}

		public void error(SAXParseException saxparseexception)
				throws SAXException {
			factory = null;
		}

		public void fatalError(SAXParseException saxparseexception)
				throws SAXException {
			factories.clear();
			factory = null;
			throw saxparseexception;
		}

		public List<Factory> getFactories() {
			return factories;
		}

		public void setDocumentLocator(Locator locator1) {
			locator = locator1;
		}

		public void startDocument() throws SAXException {
			factories.clear();
		}

		public void startElement(String s, String s1, String s2, Attributes attributes) throws SAXException {
			value.setLength(0);
			if (!s2.equals("factories"))
				if (s2.equals("factory")) {
					factory = new Factory();
					factory.isOverride = false;
					int i = attributes.getLength();
					for (int j = 0; j < i; j++) {
						String s3 = attributes.getQName(j);
						String s4 = attributes.getValue(j);
						if (s3.equals("interface"))
							factory.interfaceName = s4;
						else if (s3.equals("impl"))
							factory.implName = s4;
						else if (s3.equals("override"))
							factory.isOverride = s4 != null && s4.equalsIgnoreCase("true");
						else if (s3.equals(PLATFORM))
							factory.platforms = s4;
						else
							throw new SAXParseException("Invalid attribute: " + s3, locator);
					}

				} else if (!s2.equals("rank"))
					throw new SAXParseException("Invalid tag: " + s2, locator);
		}

		public void warning(SAXParseException e)
				throws SAXException {
			factory = null;
		}
	}
	

	private static class RankComparator implements Comparator <FactoryList>{

		private RankComparator() {
			
		}

		public int compare(FactoryList factorylist, FactoryList factorylist1) {
			return factorylist.rank - factorylist1.rank;
		}
	}

	private static final String IF_PLUGIN_PATH = "META-INF/impl-factory.xml";

	private static Map<String, String> _map;

	static final String DISTRIBUTED = "distributed";

	static final String ALL = "all";

	static final String PLATFORM = "platform";

	private static String actualPlatformString = null;

	//private static final Log logger = LogFactory.getLog(ImplFactory.class);

	static {

		try {
			_map = new FastMap<String, String>(); // HashMap<String, String>(); -> using javalotion package!			
			List<FactoryList> list =  new FastList<FactoryList>(); //new ArrayList<FactoryList>();
			
			ImplFactory.parseLegacyXmlFile(list);
			
			Collections.sort(list, new RankComparator());	
			
			for(FactoryList factorylist : list){
			    for( Factory factory : factorylist.factories ){
			    	if (!_map.containsKey(factory.interfaceName)) {
						_map.put( factory.interfaceName, factory.implName);
						continue;
					}
					if (factory.isOverride) {
						_map.put(factory.interfaceName, factory.implName);
						continue;
					}			    	
					if (!equivalent(factory.implName, _map.get(factory.interfaceName)))
					{
					}			    	
			    }	
			}
			
			for(FactoryList factorylist : list){
				factorylist.factories.clear();
			}			
			list.clear();
			
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	private static boolean equivalent(String s, Object obj) {
		if (obj instanceof String)
			return obj.equals(s);
		else
			return false;
	}

	private static Class<?> loadClass(Object classname) throws ClassNotFoundException {
		return loadClass((String) classname);
	}

   private static Class<?> loadClass(String classname) throws ClassNotFoundException {
		try {
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			return classloader.loadClass(classname);
		} catch (ClassNotFoundException e) {
			return ImplFactory.class.getClassLoader().loadClass(classname);
		}
	}

	public static Class<?> loadClassFromKey(Class<?> class1) {
		return loadClassFromKey(class1.getName());
	}

	public static Class<?> loadClassFromKey(String key) {
		
		Object obj = _map.get(key);
		Class<?> class1 = null;
		if (obj == null)
			throw new NoClassDefFoundError("Invalid Implementation Key, " + key);
		try {
			class1 = loadClass(obj);
		} catch (Exception exception) {	
			throw new IllegalStateException(exception.getMessage());
		}
		return class1;
	}

	public static Object loadImplFromClass(Class<?> objectclass) {
		try {
			return objectclass.newInstance();
		} catch (Throwable throwable) {
			throw new IllegalStateException(objectclass.getName() + " is not a valid class.");
		}
	}

	public static Object loadImplFromClass(String classname) {
		try {
			Class<?> class1 = loadClass(classname);
			return loadImplFromClass(class1);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}

	public static Object loadImplFromKey(Class<?> class1) {
		return loadImplFromKey(class1.getName());
	}

	public static Object loadImplFromKey(String key) {
		Class<?> class1 = loadClassFromKey(key);
		return loadImplFromClass(class1);
	}
	
	private static List<FactoryList> parseLegacyXmlFile(List<FactoryList> list) throws Exception {	
		
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		Enumeration<URL> enumeration = classloader.getResources(IF_PLUGIN_PATH);
				
		SAXParserFactory saxparserfactory = SAXParserFactory.newInstance();
		saxparserfactory.setNamespaceAware(true);
		XMLReader xmlreader = saxparserfactory.newSAXParser().getXMLReader();
		ImplFactoryParsingHandler implfactoryparsinghandler = new ImplFactoryParsingHandler();
		xmlreader.setContentHandler(implfactoryparsinghandler);
		xmlreader.setDTDHandler(implfactoryparsinghandler);
		xmlreader.setEntityResolver(implfactoryparsinghandler);		
		xmlreader.setErrorHandler(implfactoryparsinghandler);
		
		System.out.println("Enum:");		
		do {
			if (!enumeration.hasMoreElements())
				break;
			URL url = (URL) enumeration.nextElement();
			System.out.println(" - " + url);
			try {
				xmlreader.parse(new InputSource(url.openStream()));
				FactoryList factorylist = new FactoryList();
				factorylist.rank = implfactoryparsinghandler.rank;
				factorylist.factories.addAll(implfactoryparsinghandler.getFactories());
				list.add(factorylist);
			} catch (Exception exception) {}
		} while (true);
		
		return list;
	}

	public ImplFactory() {
		
	}
}
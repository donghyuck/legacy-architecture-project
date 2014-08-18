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
package architecture.common.model.factory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParserFactory;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileObject;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import architecture.common.util.vfs.VFSUtils;

public class ModelTypeFactory {

	private static final Log log = LogFactory.getLog(ModelTypeFactory.class);
	
	public static class ModelType {
	

		private String code;
		
		private int id;
		
		private String implName;
		
		private String interfaceName;

		private boolean isOverride;

		
		private ModelType() {
			isOverride = false;
		}
		
		private ModelType(int id, String code) {
			this.code = code;
			this.id = id;
			this.isOverride = false;
		}
		
		

		/**
		 * @return code
		 */
		public String getCode() {
			return code;
		}

		/**
		 * @return id
		 */
		public int getId() {
			return id;
		}

		/**
		 * @return implName
		 */
		public String getImplName() {
			return implName;
		}

		/**
		 * @return interfaceName
		 */
		public String getInterfaceName() {
			return interfaceName;
		}

		/**
		 * @return isOverride
		 */
		public boolean isOverride() {
			return isOverride;
		}

		public String toString() {			
			StringBuilder builder = new StringBuilder();
			builder.append('[');
			builder.append(interfaceName);
			builder.append('=');
			builder.append(id);
			builder.append(',');
			builder.append(code);
			builder.append(',');
			builder.append(implName);
			builder.append(',');
			builder.append(isOverride);
			builder.append(']');
			return builder.toString();
		}
		
	}

	private static class ModelList {

		public int rank;
		public List<ModelType> modelTypes;

		private ModelList() {
			modelTypes = new ArrayList<ModelType>();
		}

		public String toString() {
			
			StringBuilder builder = new StringBuilder();
			builder.append("[ rank = ");
			builder.append(rank);
			builder.append(',');
			builder.append(modelTypes);
			builder.append(']');			
			return builder.toString();
		}
		
	}
	
	private static class ImplFactoryParsingHandler extends DefaultHandler {

		private static boolean validOn(String s) {
			if (s == null)
				return true;
			String as[] = s.split(",");
			int i = 0;
			for (int j = as.length; i < j; i++) {
				String s1 = as[i].trim();
				if (s1.equalsIgnoreCase("all"))
					return true;
			}
			return false;
		}

		Locator locator;

		int rank;

		List<ModelType> modelTypes;

		ModelType modelType;

		StringBuffer value;

		public ImplFactoryParsingHandler() {
			rank = 0;
			modelTypes = new ArrayList<ModelType>();
			value = new StringBuffer();
		}

		public void characters(char ac[], int i, int j) throws SAXException {
			value.append(ac, i, j);
		}

		public void endDocument() throws SAXException {
		}

		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (qName.equals("rank"))
				try {
					rank = Integer.parseInt(value.toString());
				} catch (NumberFormatException e) {
					throw new SAXParseException("Problem converting rank to number: " + value.toString(), locator);
				}
			else if (qName.equals("model") && modelType != null)
				modelTypes.add(modelType);
			value.setLength(0);
		}

		public void error(SAXParseException e) throws SAXException {
			modelType = null;
		}

		public void fatalError(SAXParseException e) throws SAXException {
			modelTypes.clear();
			modelType = null;
			throw e;
		}

		/**
		 * @return
		 * @uml.property  name="factories"
		 */
		public List<ModelType> getModels() {
			return modelTypes;
		}

		public void setDocumentLocator(Locator locator) {
			this.locator = locator;
		}

		public void startDocument() throws SAXException {
			modelTypes.clear();
		}

		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			value.setLength(0);
			if (!qName.equals("models"))
				if (qName.equals("model")) {
					modelType = new ModelType();
					modelType.isOverride = false;
					int i = attributes.getLength();
					for (int j = 0; j < i; j++) {
						String name = attributes.getQName(j);
						String value = attributes.getValue(j);
						if (name.equals("id"))
							modelType.id = Integer.parseInt(value);
						else if (name.equals("code"))
							modelType.code = value;
						else if (name.equals("override"))
							modelType.isOverride = value != null && value.equalsIgnoreCase("true");
						else if (name.equals("implName"))
							modelType.implName = value;
						else if (name.equals("interfaceName"))
							modelType.interfaceName = value;						
						else
							throw new SAXParseException("Invalid attribute: " + name, locator);
					}
				} else if (!qName.equals("rank"))
					throw new SAXParseException("Invalid tag: " + qName, locator);
		}

		public void warning(SAXParseException e)
				throws SAXException {
			modelType = null;
		}
	}
	

	private static class RankComparator implements Comparator <ModelList>{
		private RankComparator() {
			
		}
		
		public int compare(ModelList factories1, ModelList factories2) {
			return factories1.rank - factories2.rank;
		}		
	}


	private static final String IF_PLUGIN_PATH = "META-INF/impl-model.xml";

	private static Map<String, ModelType> _map;

	public static ModelType UNKNOWN = new  ModelType(0, "UNKNOWN");
	
	static {

		try {
			_map = new FastMap<String, ModelType>(); 
			List<ModelList> list =  new FastList<ModelList>(); 
			
			ModelTypeFactory.parseLegacyXmlFile(list);
			
			Collections.sort(list, new RankComparator());	
			
			for(ModelList factorylist : list){
			    for( ModelType factory : factorylist.modelTypes ){
			    	if (!_map.containsKey(factory.code)) {
						_map.put( factory.code, factory);
						continue;
					}
					if (factory.isOverride) {
						_map.put(factory.code, factory);
						continue;
					}			    	
					if (!equivalent(factory, _map.get(factory.code)))
					{
					}			    	
			    }	
			}
			
			for(ModelList factorylist : list){
				factorylist.modelTypes.clear();
			}			
			list.clear();
			
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}
	
	private static boolean equivalent(ModelType type, Object obj) {
		if (obj instanceof ModelType)
			return ((ModelType) obj).id == type.id;
		else
			return false;
	}

	public static ModelType getTypeFromCode(String code) {		
		log.debug("code[" + code + "]"  );		
		return _map.get(code);
	}
	
	public static Integer getTypeIdFromCode(String code) {		
		log.debug("code[" + code + "]"  );		
		return _map.get(code).id;
	}
	
/*	public static ModelType getModelTypeFromObject(Object model) {
		
		for( ModelType type : _map.values() ){
			if( model.getClass().getName().equals( type.implName ) )
				return type;
		}
		return _map.get("UNKNOWN");
	}*/
	
	private static List<ModelList> parseLegacyXmlFile(List<ModelList> list) throws Exception {	
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Enumeration<URL> enumeration = cl.getResources(IF_PLUGIN_PATH);				
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XMLReader xmlreader = factory.newSAXParser().getXMLReader();
		ImplFactoryParsingHandler handler = new ImplFactoryParsingHandler();
		xmlreader.setContentHandler(handler);
		xmlreader.setDTDHandler(handler);
		xmlreader.setEntityResolver(handler);		
		xmlreader.setErrorHandler(handler);		
		System.out.println("Model Enum:");	
		
		do {
			if (!enumeration.hasMoreElements())
				break;
			URL url = (URL) enumeration.nextElement();
			System.out.println(" - " + url);
			try {
				xmlreader.parse(new InputSource(url.openStream()));
				ModelList factorylist = new ModelList();
				factorylist.rank = handler.rank;
				factorylist.modelTypes.addAll(handler.getModels());
				list.add(factorylist);
			} catch (Exception exception) {}
		} while (true);
		
		return list;
	}
}


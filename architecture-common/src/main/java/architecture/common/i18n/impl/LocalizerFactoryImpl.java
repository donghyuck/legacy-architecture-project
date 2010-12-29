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
package architecture.common.i18n.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import architecture.common.i18n.Localizer;
import architecture.common.i18n.LocalizerFactory;

/**
 * 
 * @author DongHyuck, Son 
 *
 */
public class LocalizerFactoryImpl implements LocalizerFactory.Implementation {

	/**
    private static class LocalizerResourceBundle extends ListResourceBundle implements Serializable
    {    
    	private Locale locale = null;
        private Object contents[][]; 
                
        protected LocalizerResourceBundle(Map<Object, Object> newKeyValues)
        {
            contents = new Object[newKeyValues.size()][2];
            int index = 0;
            for(Object key:newKeyValues.keySet()){
                contents[index][0] = key;
                contents[index][1] = newKeyValues.get(key);
                index++;
            }    
        }
                
        public Object[][] getContents()
        {
            return contents;
        }
        
		public Locale getLocale() {
			if(locale != null)
				return locale;
			return super.getLocale();
		}        
    }
    **/
    
    private static class LocalizerResourceBundleControl extends ResourceBundle.Control {
        
    	public static final String XML = "xml";
    	    	
        public static final List<String> FORMAT_DEFAULT = Collections.unmodifiableList(Arrays.asList("java.properties", XML));
        
        public List<String> getFormats(String baseName) {
            return FORMAT_DEFAULT;
        }

        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException, IOException 
        {        	
        	
            if ((baseName == null) || (locale == null) || (format == null) || (loader == null)) {
                throw new NullPointerException();
            }
            
            ResourceBundle bundle = null;            
            bundle = super.newBundle(baseName, locale, format, loader, reload);
            
            /**
            if (format.equals(XML)) {
            	String bundleName = toBundleName(baseName, locale);                
                String resourceName = toResourceName(bundleName, format);     
                URL url = loader.getResource(resourceName);        
                
                if (url == null) {
                    return null;
                }                
                                    
                URLConnection connection = url.openConnection();
                if (connection == null) {
                    return null;
                }
                if (reload) {
                    connection.setUseCaches(false);
                }
                
                InputStream stream = connection.getInputStream();
                if (stream == null) {
                    return null;
                }
                
                BufferedInputStream bis = new BufferedInputStream(stream);
                bundle = getLocalizerBundle(bis);
                bis.close();
                                       
            }else{
                bundle = super.newBundle(baseName, locale, format, loader, reload);
            }
            **/
            return bundle;
        }
    }
    
    // private static final Log log = LogFactory.getLog(LocalizerFactoryImpl.class);    

    public Localizer getLocalizer(String baseName){        
        return getLocalizer(baseName, Locale.getDefault(), Thread.currentThread().getContextClassLoader()); 
    }
    
    public Localizer getLocalizer(String baseName, Locale targetLocale){           
        return getLocalizer(baseName, targetLocale, Thread.currentThread().getContextClassLoader());    
    }
    
    public Localizer getLocalizer(String baseName, Locale targetLocale, ClassLoader cl){        
        ResourceBundle bundle = ResourceBundle.getBundle(baseName, targetLocale, cl, new LocalizerResourceBundleControl());       
        return new Localizer( bundle );    
    }
        
    /**
    private static LocalizerResourceBundle getLocalizerBundle(InputStream in) throws IOException {
        Reader reader = new BufferedReader(new InputStreamReader(in));
        try {          	
        	SAXBuilder builder = new SAXBuilder();
            // xmlReader.setEncoding("UTF-8");
            Document document = builder.build(reader);           
            
            if( log.isDebugEnabled() )
            try {
                XMLOutputter outputter = new XMLOutputter();
                outputter.output(document, System.out);
            } catch (java.io.IOException e) {}
            
            return getLocalizerBundle(document.getRootElement());            
        } catch (Exception e) {
            throw new java.io.IOException (e);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
    **/
    
    /**
	private static LocalizerResourceBundle getLocalizerBundle (Element element){
    	
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put(Localizer.VERSION, element.getChildTextTrim(Localizer.VERSION));
        map.put(Localizer.PREFIX, element.getChildTextTrim(Localizer.PREFIX));
        map.put(Localizer.SUBSYSTEM, element.getChildTextTrim(Localizer.SUBSYSTEM));
        
        if(log.isDebugEnabled())
	        log.debug(
	                String.format("Localizer [subsystem=%s, version=%s]",
	                map.get(Localizer.SUBSYSTEM),map.get(Localizer.VERSION))
	        );        
        
        List<Element> list = element.getChildren(Localizer.MESSAGE_BODY);
        for(Element el : list){            
            String key = Localizer.MESSAGE_BODY + el.getAttributeValue(Localizer.ID);
            String value = el.getText();
            map.put(key, value );
        }
        
        List<Element> list2 = element.getChildren(Localizer.MESSAGE_DETAIL);
        for(Element el : list2){
            String key = Localizer.MESSAGE_DETAIL + el.getAttributeValue(Localizer.ID);
            String value = el.getTextTrim();
            map.put(key, value);            
        }                
        return new LocalizerResourceBundle(map);
    }
	*/
}

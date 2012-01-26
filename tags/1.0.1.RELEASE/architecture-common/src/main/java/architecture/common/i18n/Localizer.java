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
package architecture.common.i18n;

import java.text.DecimalFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author    DongHyuck, Son
 */
public class Localizer {
	
	public static final String PREFIX_DELIM = "-";
	/**
	 * @uml.property  name="pREFIX"
	 */
	public static final String PREFIX = "prefix";
	/**
	 * @uml.property  name="vERSION"
	 */
	public static final String VERSION = "version";
	public static final String ID = "id";
	public static final String MESSAGE_BODY = "messagebody";
	public static final String CAUSE = "cause";
	public static final String ACTION = "action";
	
	public  static final DecimalFormat decimalformat = new DecimalFormat("000000");
	
	private ResourceBundle bundle;
	
	public Localizer(ResourceBundle resourcebundle) {
        bundle = resourcebundle;
    }
	
    /**
	 * @return
	 * @throws MissingResourceException
	 * @uml.property  name="vERSION"
	 */
    public String getVersion() throws MissingResourceException {
        return (String) bundle.getObject(VERSION);
    }
	
    /**
	 * @return
	 * @uml.property  name="pREFIX"
	 */
    public String getPrefix(){
        String prefix = null;
        try {
            prefix = (String) bundle.getObject(PREFIX);
            if (prefix != null && prefix.length() == 0)
                prefix = null;
        } catch (MissingResourceException e) {
        }
        return prefix;
	}
	

	public String getBody(String id)throws MissingResourceException{
		return getString(MESSAGE_BODY, id);
	}

	public String getBody(int id)throws MissingResourceException{
		return getString(MESSAGE_BODY, id);
	}

	public String getCause(String id)throws MissingResourceException{
		return getString(CAUSE, id);
	}

	public String getCause(int id)throws MissingResourceException{
		return getString(CAUSE, id);
	}
	

	public String getString(String key, int id)throws MissingResourceException{
		return getObject(key, id).toString();
	}
    
	public String getString(String key, String id) throws MissingResourceException {
       return getObject(key, id).toString();
    }	
	

    public Object getObject(String key, String id) throws MissingResourceException {
        return bundle.getObject((new StringBuilder()).append(key).append( stripPrefix(id)).toString());
    }

	public Object getObject(String key, int id){
		return getObject(key, decimalformat.format(id));
	}

    public String get(String id) {
        return getString("", id);
    }
    
	private String stripPrefix(String str) {
		String prefix = getPrefix();
		String stringToUse = str;
		if (prefix != null && str.indexOf((new StringBuilder()).append(prefix).append(PREFIX_DELIM).toString()) != -1)
			stringToUse = str.substring(prefix.length() + 1);
		return stringToUse;
	}
	
}
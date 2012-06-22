package architecture.ee.util;

import architecture.common.util.StringUtils;


/**
 * @author   donghyuck
 */
public enum OutputFormat {
	/**
	 * @uml.property  name="jSON"
	 * @uml.associationEnd  
	 */
	JSON, /**
	 * @uml.property  name="xML"
	 * @uml.associationEnd  
	 */
	XML, /**
	 * @uml.property  name="hTML"
	 * @uml.associationEnd  
	 */
	HTML;
		
	public static OutputFormat stingToOutputFormat(String dataTypeString){
        if(StringUtils.isEmpty(dataTypeString))
            return HTML;        
        else if(dataTypeString.toLowerCase().equals("json"))
        	return JSON;
       	else if (dataTypeString.toLowerCase().equals("xml"))
       		return XML;
       	else
       		return HTML;
        
	}
}

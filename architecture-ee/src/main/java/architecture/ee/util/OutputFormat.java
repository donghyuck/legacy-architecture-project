package architecture.ee.util;

import architecture.common.util.StringUtils;

/**
 * @author donghyuck
 */
public enum OutputFormat {
    /**
     */
    JSON,
    /**
     */
    XML,
    /**
     */
    HTML;

    public static OutputFormat stingToOutputFormat(String dataTypeString) {

	if (StringUtils.isEmpty(dataTypeString))
	    return HTML;
	else if (dataTypeString.toLowerCase().equals("json"))
	    return JSON;
	else if (dataTypeString.toLowerCase().equals("xml"))
	    return XML;
	else
	    return HTML;

    }

}

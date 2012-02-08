package architecture.ee.web.theme.impl;
import architecture.ee.web.model.impl.ThemeModelImpl;
import architecture.ee.web.theme.ThemeXStreamFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.collections.PropertiesConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ThemeXStreamFactoryImpl implements ThemeXStreamFactory.Implementation {

	private XStream xstream = null;
	
	public XStream getThemeXStream() {
		
		if(xstream == null){
			xstream = new XStream(new DomDriver());
			xstream.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
			xstream.registerConverter(new PropertiesConverter());
			xstream.alias("theme", ThemeModelImpl.class); 
		}
		
		return xstream;
	}

}

package architecture.ext.sync.connector.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import architecture.common.adaptor.Context;
import architecture.common.adaptor.ReadConnector;
import architecture.common.jdbc.ParameterMapping;

public class EsaramXmlReaderConnector implements ReadConnector {
	
	private Log log = LogFactory.getLog(getClass());

	private String location;
	
	private String charset = "EUC-KR";
		
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	
	public Object pull(Context context) {
				
		Properties properties = context.getObject("properties", Properties.class);
		List<ParameterMapping> parameterMappings = context.getObject("parameterMappings", List.class);		
		
		final String filenamePrefix = properties.getProperty("filenamePrefix");
		final String filenameExtension = properties.getProperty("filenameExtension");
		
		Object[] data = context.getObject("data", Object[].class);	
		if( data == null)
		{
			data = new Object[0];
		}
		
		File root = new File( location );
		File[] files = root.listFiles(new FilenameFilter(){
			
			
			public boolean accept(File dir, String name) {
				if( name.startsWith(filenamePrefix) && name.endsWith(filenameExtension) )
					return true;
				return false;
			}});
		
		for(File file : files){
			log.debug(file.getAbsoluteFile());
		}
		return null;
	}
	
	protected List<Map<String, Object>> xmlToList (File file){
		
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		SAXReader saxReader = new SAXReader();		
		
		try {
			Document document = saxReader.read(new FileInputStream(file), charset );			
			Element root = document.getRootElement();			 
			// typ:dataList
			int count = 0 ;
			List<Element> list = root.elements("dataList");
			for(Element e : list){				
				Map<String, Object> data = new HashMap<String, Object>();
				List<Element> row = e.elements();
				for( Element column : row ){
					String name = column.getName();
					String value = column.getTextTrim();
					data.put(name, value);
				}
				count ++ ;
				result.add(data);
			}
			log.debug( file.getName() + ":" + count );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
		
	}
	
}

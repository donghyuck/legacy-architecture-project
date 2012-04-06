package architecture.common.adaptor.connector.ftp;

import java.util.Collections;
import java.util.List;

import architecture.common.adaptor.Context;
import architecture.common.adaptor.ReadConnector;
import architecture.common.jdbc.ParameterMapping;

public class FtpReadConnector extends AbstractFtpConnector implements ReadConnector {

	public Object pull(Context context) {
		
		List<ParameterMapping> parameterMappings = context.getObject("parameterMappings", List.class);		
		Object[] data = context.getObject("data", Object[].class);
		if( parameterMappings == null ){
			parameterMappings = Collections.EMPTY_LIST;
		}		
		if( data == null)
		{
			data = new Object[0];
		}
		
		
		return null;
	}

}

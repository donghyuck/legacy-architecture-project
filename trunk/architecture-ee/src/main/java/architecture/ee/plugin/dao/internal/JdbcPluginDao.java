package architecture.ee.plugin.dao.internal;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import architecture.ee.plugin.dao.PluginBean;
import architecture.ee.plugin.dao.PluginDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;

public class JdbcPluginDao extends ExtendedJdbcDaoSupport implements PluginDao{

	
	
	public PluginBean create(PluginBean pluginbean) {
		
		Date now = new Date();
		
		
		return null;
	}

	public PluginBean create(PluginBean pluginbean, int contentLength, InputStream inputstream) {
		// TODO Auto-generated method stub
		return null;
	}
	public void delete(PluginBean pluginbean) {
		// TODO Auto-generated method stub
		
	}

	public void delete(String pluginName) {
		// TODO Auto-generated method stub
		
	}

	public PluginBean getByName(String pluginName) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<PluginBean> getPluginBeans() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPluginData(PluginBean pluginbean, int contentLength,
			InputStream inputstream) {
		// TODO Auto-generated method stub
		
	}

	public InputStream getPluginData(PluginBean pluginbean) {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean doesPluginTableExist() {
		// TODO Auto-generated method stub
		return false;
	}

}

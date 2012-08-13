package architecture.ee.component.core.lifecycle;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.Resource;

import architecture.common.lifecycle.ConfigRoot;

/**
 * @author   donghyuck
 */
public class ConfigRootImpl implements ConfigRoot {

	private String rootURL;

	private Resource rootFileObject;
	
	public ConfigRootImpl(Resource fileObject) {
		this.rootFileObject = fileObject;
		try {
			this.rootURL = rootFileObject.getURL().toString();
		} catch (IOException e) {
		}
	}

	public ConfigRootImpl(String rootURL) {
		this.rootURL = rootURL;
	}
		
	private Resource getRootResource() {
		return rootFileObject;
	}

	public String getConfigRootPath() {
		return rootURL;
	}

	public File getFile(String name) {
		try {			
			return getRootResource().createRelative(name).getFile();
		} catch (IOException e) {
		}
		return null;
	}
	
	public String getURI(String name) {
		try {
			
			return  getRootResource().createRelative(name).getURI().toString();
		} catch (IOException e) {
		}
		return null;
	}
	
	public String getRootURI(){
		return rootURL;
	}

}
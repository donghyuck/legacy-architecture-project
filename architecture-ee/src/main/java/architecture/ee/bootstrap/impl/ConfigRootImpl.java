package architecture.ee.bootstrap.impl;

import java.io.File;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;

import architecture.common.lifecycle.ConfigRoot;

/**
 * @author  donghyuck
 */
public class ConfigRootImpl implements ConfigRoot {

	private String rootURL;
	/**
	 * @uml.property  name="rootFileObject"
	 */
	private FileObject rootFileObject;
	
	public ConfigRootImpl(FileObject fileObject) {
		this.rootFileObject = fileObject;
		try {
			this.rootURL = fileObject.getURL().toString();
		} catch (FileSystemException e) {
		}
	}

	public ConfigRootImpl(String rootURL) {
		this.rootURL = rootURL;
	}
		
	/**
	 * @return
	 * @uml.property  name="rootFileObject"
	 */
	private FileObject getRootFileObject() {
		return rootFileObject;
	}

	public String getConfigRootPath() {
		return rootURL;
	}

	public File getFile(String name) {
		try {
			FileObject obj = getRootFileObject().resolveFile(name);		   
			return  new File(obj.getURL().getFile());
		} catch (FileSystemException e) {
		}
		return null;
	}
	
	public String getURI(String name) {
		try {
			FileObject obj = getRootFileObject().resolveFile(name);		   
			return  obj.getName().getURI();
		} catch (FileSystemException e) {
		}
		return null;
	}
	
	public String getRootURI(){
		return rootURL;
	}

}
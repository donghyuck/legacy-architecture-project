package architecture.ee.spring.lifecycle.internal;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import architecture.common.scanner.DirectoryListener;
import architecture.common.scanner.URLDirectoryScanner;
import architecture.common.vfs.VFSUtils;
import architecture.ee.component.DirectoryScanner;

public class DirectoryScannerImpl /** extends SpringLifecycleService **/ implements InitializingBean, DisposableBean, DirectoryScanner {

	private URLDirectoryScanner scanner;
	private List<String> resourceLocations;
	private ResourceLoader resourceLoader;
		
	private Log log = LogFactory.getLog(getClass());
	
	public DirectoryScannerImpl() {
		scanner = new URLDirectoryScanner();
		scanner.setRecursiveSearch(true);
		scanner.setScanEnabled(true);
		this.resourceLoader = new DefaultResourceLoader();
		this.resourceLocations = Collections.emptyList();
	}
	
		
	public void setDirectoryListenerList(List<DirectoryListener> directoryListeners) {
		for(DirectoryListener listener : (List<DirectoryListener>)directoryListeners){
			scanner.removeDirectoryListener(listener);			
			scanner.addDirectoryListener(listener);
		}
	}

	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	public List<String> getResourceLocations() {
		return resourceLocations;
	}

	public void setResourceLoader(ResourceLoader loader) {
		resourceLoader = loader;
	}

	public void setResourceLocations(List<String> resourceLocations) {
		this.resourceLocations = resourceLocations;
	}
			
	public void setRecursiveSearch(boolean recurse){
		scanner.setRecursiveSearch(recurse);
	}
	
	public void setPollIntervalMillis(int pollIntervalMillis){
		scanner.setPollIntervalMillis(pollIntervalMillis);
	}

	public void addDirectoryListener(DirectoryListener fileListener) {
		scanner.addDirectoryListener(fileListener);
	}

	public void addScanDir(String path) {	
		scanner.addScanDir(path);
	}

	public DirectoryListener[] getDirectoryListeners() {
		return scanner.getDirectoryListeners();
	}

	public void removeDirectoryListener(DirectoryListener fileListener) {
		scanner.removeDirectoryListener(fileListener);
	}

	public void removeScanURL(URL url) {
		scanner.removeScanURL(url);
	}

	public void addScanURI(URI uri) {
		try {
			scanner.addScanURL(uri.toURL());
		} catch (MalformedURLException e) {
		}
	}

	public void addScanURL(URL url) {
		scanner.addScanURL(url);
	}

	public void removeScanURI(URI uri) {
		try {
			scanner.removeScanURL(uri.toURL());
		} catch (MalformedURLException e) {
		}
	}
	
	public void destroy() throws Exception {
		if(scanner.isStarted())
			scanner.doStop();
		scanner.destroy();		
	}


	public void afterPropertiesSet() throws Exception {
		scanner.setRecursiveSearch(true);
		scanner.setScanEnabled(true);
		
		scanner.create();
		loadResourceLocations();
		scanner.start();		
	}
	
	protected void loadResourceLocations() {
		try {					
			for (String path : resourceLocations) {		
				FileObject fo = VFSUtils.resolveFile(path);
				if(fo.exists()){
					URL url = fo.getURL();
					url.openConnection();
					scanner.addScanURL(url);
				}
			}
		} catch (Exception e) { }
	}		
}

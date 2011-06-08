package architecture.ee.spring.lifecycle.internal;

import java.io.File;
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

import architecture.common.scanner.DirectoryListener;
import architecture.common.scanner.URLDirectoryScanner;
import architecture.common.vfs.VFSUtils;
import architecture.ee.component.DirectoryScanner;
import architecture.ee.spring.jdbc.SqlQueryFactoryBuilder;

public class DirectoryScannerImpl /** extends SpringLifecycleService **/ implements InitializingBean, DisposableBean, DirectoryScanner {

	private URLDirectoryScanner scanner;
	private List<String> resourceLocations;
	private boolean fastDeploy = false;
	
	//private ResourceLoader resourceLoader;
	
	private Log log = LogFactory.getLog(getClass());
	
	public DirectoryScannerImpl() {		
		
		//this.resourceLoader = new DefaultResourceLoader();
		this.resourceLocations = Collections.emptyList();
		
		try {
			this.scanner = createURLDirectoryScanner(true);
		} catch (Exception e) { }
	}
	
	public void setDirectoryListenerList(List<DirectoryListener> directoryListeners) {
		for(DirectoryListener listener : (List<DirectoryListener>)directoryListeners){
			scanner.removeDirectoryListener(listener);			
			scanner.addDirectoryListener(listener);
		}
	}

	private URLDirectoryScanner createURLDirectoryScanner(boolean recursive) throws Exception {
		URLDirectoryScanner scanner = new URLDirectoryScanner();
		scanner.setRecursiveSearch(true);
		scanner.setScanEnabled(true);
		scanner.create();
		return scanner;
	}
		
/*	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}*/

	public void setFastDeploy(boolean fastDeploy) {
		this.fastDeploy = fastDeploy;
	}

	public List<String> getResourceLocations() {
		return resourceLocations;
	}

/*	public void setResourceLoader(ResourceLoader loader) {
		resourceLoader = loader;
	}*/

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
		loadResourceLocations();		
		if(!scanner.isStarted())
		    scanner.start();
	}
	
	protected void loadResourceLocations() {
		try {
			for (String path : resourceLocations) {		
				FileObject fo = VFSUtils.resolveFile(path);
				if(fo.exists()){
		
					URL url = fo.getURL();
					url.openConnection();					
					if(fastDeploy){
						if(log.isDebugEnabled()){
							log.debug("Fast deploy : " + url );							
							SqlQueryFactoryBuilder builder = null;							 
							for(DirectoryListener listener : scanner.getDirectoryListeners()){
								if(listener instanceof SqlQueryFactoryBuilder ){									
									builder = (SqlQueryFactoryBuilder)listener;			
								}
							}							
							File file = new File(url.getFile());
							fastDeploy(file, builder);							
						}
					}
					scanner.addScanURL(url);
				}
			}
		} catch (Exception e) { }
	}		
	
	public void fastDeploy(File file, SqlQueryFactoryBuilder builder){

		if(file.isFile()){
			if(builder.validateFile(file)){
	        	builder.fileCreated(file);
	        }
		}else{
			for( File c : file.listFiles() ){
			    fastDeploy(c, builder);				
			}		
		}	
	}
	
	
}
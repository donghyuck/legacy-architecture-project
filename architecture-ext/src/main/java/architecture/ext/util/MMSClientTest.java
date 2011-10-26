package architecture.ext.util;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.springframework.util.MethodInvoker;

import architecture.ext.mms.client.MMSClient;

public class MMSClientTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String extDir = System.getProperty("ext.dir");		
		File root = new File(extDir);			
		File[] files = root.listFiles(new FilenameFilter(){
			public boolean accept(File dir, String name) {				
				if( name.endsWith(".jar") ){
				
					if(name.startsWith("architecture-ext"))
						return false;
					
					return true;			
				}
				return false;
			}});
		
		int size = files.length;		
		URL[] urls = new URL[size];
		for( int i = 0 ; i < size ; i ++ ){
			try {
				urls[i] = files[i].toURL();
				System.out.println("##" + urls[i]);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}	
		MMSClient client = null;
		try {			
			URLClassLoader cl = URLClassLoader.newInstance(urls);			
			Class c = cl.loadClass("architecture.ext.util.MMSClientLocator");			
			Object targetObject = c.newInstance();			
			MethodInvoker invoker = new MethodInvoker();
		    invoker.setTargetObject(targetObject);
		    invoker.setTargetClass(MMSClientLocator.class);
		    invoker.setTargetMethod("getMMSClient");
		    invoker.prepare();
		    client = (MMSClient)invoker.invoke();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println (">>>" + client);
		
		
	}

}

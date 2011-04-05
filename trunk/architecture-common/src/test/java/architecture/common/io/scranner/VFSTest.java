package architecture.common.io.scranner;

import org.apache.commons.vfs.FileChangeEvent;
import org.apache.commons.vfs.FileListener;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;
import org.junit.Test;

import architecture.common.vfs.ExtendedFileMonitor;


public class VFSTest {
	
	
	public VFSTest() {
		

	}


	public static void main(String[] args){
		
		System.out.println("-----------");		
		System.out.println("-----------");
		try {
			(new VFSTest()).testLocalFileSystem();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testLocalFileSystem() throws Exception{

		ExtendedFileMonitor monitor = new ExtendedFileMonitor(new FileListener(){

			public void fileChanged(FileChangeEvent event) throws Exception {		
				System.out.println(event.getFile().getName() + " changed.");
				System.out.println(event.getFile().getContent() + ".");
			}

			public void fileCreated(FileChangeEvent event) throws Exception {			
				System.out.println(event.getFile().getName() + " created.");
				System.out.println(event.getFile().getContent() + ".");
			}

			public void fileDeleted(FileChangeEvent event) throws Exception {
				System.out.println(event.getFile().getName() + " deleted.");
				System.out.println(event.getFile().getContent() + ".");
			}});
		
	    
		//monitor.setDelay(1000);
		//monitor.setRecursive(true);
		
		monitor.setRecursive(true);
		
		
		String path = "file:///C:/TOOLS/workspace/opensource/PowerJ/installableApps/default/WebContent/WEB-INF/config/sql/" ;
	
		FileSystemManager vmgr = VFS.getManager();
		FileObject fo = vmgr.resolveFile(path);
		
		//System.out.println( fo.getType().hasChildren() );
		
		if(fo.getType().hasChildren()){
		FileObject[] children = fo.getChildren();
		//System.out.println( "Children of " + fo.getName().getURI() );
		
		for ( int i = 0; i < children.length; i++ )
		{
		   // System.out.println( children[ i ].getName().getBaseName() );
		}
		}

		monitor.addFile(fo);
		for( FileObject fobj : monitor.getMonitoredFileObjectList()){
			
			System.out.println( "obj=" + fobj.getName().getPath());
		}
		
		//monitor.run();
		//monitor.start();
		monitor.run();
		
		//ScannerThread t = new ScannerThread(true);
		//t.setDaemon(true);
		//t.run();
	}

	public class ScannerThread extends Thread {
		
		/** True if the scan loop should run. */
		protected boolean enabled;

		/** True if we are shutting down. */
		protected boolean shuttingDown;

		/** Lock/notify object. */
		protected Object lock = new Object();

		public ScannerThread(boolean enabled) {
			super("Directory Scanner Thread");
			this.enabled = enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
			synchronized (lock) {
				lock.notifyAll();
			}
						
		}

		public void shutdown() {
			enabled = false;
			shuttingDown = true;
			synchronized (lock) {
				lock.notifyAll();
			}
		}

		public void run() {
			while (!shuttingDown) {
				// If we are not enabled, then wait
				if (!enabled) {
					try {
						synchronized (lock) {
							lock.wait();
						}
					} catch (InterruptedException ignore) {
					}
				}
				loop();
			}
		}

		public void doScan() {
			// Scan for new/removed/changed/whatever
		}

		protected void loop() {
			while (enabled) {
				doScan();
				// Sleep for scan period
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ignore) {
				}
			}
		}
	}
}

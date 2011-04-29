package architecture.common.scanner;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public abstract class AbstractDirectoryScanner {

    protected Log log = LogFactory.getLog(getClass());
	
	private static final int DEFAULT_POOL_INTERVAL_MILLIS = 5000;
	
	private int pollIntervalMillis = DEFAULT_POOL_INTERVAL_MILLIS;
	
	private boolean scanEnabled = true;
    
	// 이부분을 동기화 되도록 수정한다.
    private final LinkedList<DirectoryListener> listenerList = new LinkedList<DirectoryListener>();
	
	private ScannerThread scannerThread;
	
	// ///////////////////////////////////////////////////////////////////////
	//                                                                      //
	// ///////////////////////////////////////////////////////////////////////
	
	public void create() throws Exception {		
		//setup + start scanner thread
		scannerThread = new ScannerThread(false);
		scannerThread.setDaemon(true);
		scannerThread.start();		
	}
		
    
	public void start() throws Exception {
		synchronized (scannerThread){
			// scan before we enable the thread, so JBoss version shows up
			// afterwards
			scannerThread.doScan();
			// enable scanner thread if we are enabled
			scannerThread.setEnabled(scanEnabled);
		}
	}
	
	
    public void doStop() throws Exception {
		// disable scanner thread
		if (scannerThread != null)
			scannerThread.setEnabled(false);
	}
	
	public void destroy() throws Exception {		
        // drop our ref to deployer, so scan will fail        
        listenerList.clear();        
		// shutdown scanner thread
		if (scannerThread != null) {
			synchronized (scannerThread) {
				scannerThread.shutdown();
			}
		}
		scannerThread = null;
	}
	
	public boolean isStarted(){		
		if(scannerThread == null)
			return false;		
		return scannerThread.enabled.get();		
	}
	
	// ///////////////////////////////////////////////////////////////////////
	// DirectoryScanner                                                     //
	// ///////////////////////////////////////////////////////////////////////    
    /**
     * Adds DirectoryListener
     *
     * @param fileListener The FileListener
     */
    public void addDirectoryListener(DirectoryListener fileListener) {
        listenerList.add(fileListener);
    }
    
    public void removeDirectoryListener( DirectoryListener fileListener ){
    	listenerList.remove(fileListener);
    }
    /**
     * Retrieve an array of DirectoryListeners.
     *
     * @return DirectoryListeners as array of DirectoryListener
     */
    public DirectoryListener[] getDirectoryListeners() {
        DirectoryListener[] listeners = new DirectoryListener[listenerList.size()];
        listenerList.toArray(listeners);
        return listeners;
    }
    
    
	public int getPollIntervalMillis() {
        return pollIntervalMillis;
    }

    public void setPollIntervalMillis(int pollIntervalMillis) {
        this.pollIntervalMillis = pollIntervalMillis;
    }
    
	public void setScanEnabled(final boolean flag) {
		this.scanEnabled = flag;
	}

	public boolean isScanEnabled() {
		return scanEnabled;
	}
    
	/**
	 * This is here to work around a bug in the IBM vm that causes an
	 * AbstractMethodError to be thrown when the ScannerThread calls scan.
	 * 
	 * @throws Exception
	 */
	public abstract void scan() throws Exception;
	
    ////////////////////////////////////////////////////////////////
    // InnerClass
    ///////////////////////////////////////////////////////////////
	/**
	 * Should use Timer/TimerTask instead? This has some issues with interaction
	 * with ScanEnabled attribute. ScanEnabled works only when
	 * starting/stopping.
	 */
	public class ScannerThread extends Thread {
		
		/** We get our own logger. */
		protected Log log = LogFactory.getLog(ScannerThread.class);

		/** True if the scan loop should run. */
		protected AtomicBoolean enabled;

		/** True if we are shutting down. */
		protected AtomicBoolean shuttingDown;

		/** Lock/notify object. */
		protected Object lock = new Object();

		public ScannerThread(boolean enabled) {
			super("Directory Scanner Thread");
			this.enabled = new AtomicBoolean(enabled);
			shuttingDown = new AtomicBoolean(false);
		}

		public void setEnabled(boolean enabled) {
			this.enabled.set(enabled);
			synchronized (lock) {
				lock.notifyAll();
			}
			//if(log.isDebugEnabled()){
				//if(this.enabled.get())
				//	log.debug(MessageFormatter.format("003510"));
				//else
					//log.debug(MessageFormatter.format("003509"));
			//}
		}

		public void shutdown() {
			enabled.set(false);
			shuttingDown = new AtomicBoolean(true);
			synchronized (lock) {
				lock.notifyAll();
			}
			//if(log.isDebugEnabled())
				//log.debug(MessageFormatter.format("003511"));
		}

		public void run() {
			setPriority(Thread.MIN_PRIORITY);
			//log.debug(MessageFormatter.format("003502"));
			while (!shuttingDown.get()) {
				// If we are not enabled, then wait
				if (!enabled.get()) {
					try {
						//if(log.isDebugEnabled())
							//log.debug(MessageFormatter.format("003512"));
						synchronized (lock) {
							lock.wait();
						}
						
					} catch (InterruptedException e) { 
						e.printStackTrace();
					}
				}
				loop();
			}
			//log.debug(MessageFormatter.format("003503"));
		}

		public void doScan() {
			try {
				scan();
			} catch (Exception e) {
				//log.error(MessageFormatter.format("003513"), e);
			}
		}

		protected void loop() {
			while (enabled.get()) {
				doScan();
				// Sleep for scan period
				try {
					Thread.sleep(pollIntervalMillis);
				} catch (InterruptedException ignore) {
				}
			}
		}
	}
	
}

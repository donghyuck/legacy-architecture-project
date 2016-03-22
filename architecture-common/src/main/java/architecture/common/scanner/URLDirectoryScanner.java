package architecture.common.scanner;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import architecture.common.exception.NullArgumentException;
import architecture.common.util.L10NUtils;

public class URLDirectoryScanner extends AbstractDirectoryScanner {

    protected boolean doRecursiveSearch = true;

    protected List<URL> urlList = Collections.synchronizedList(new ArrayList<URL>());

    protected Map<String, FileInfo> files = new HashMap<String, FileInfo>();

    public URLDirectoryScanner() {

    }

    public void addScanDir(final String path) {
	File file = new File(path);
	if (file.exists() && file.isAbsolute()) {
	    try {
		URL url = file.toURI().toURL();
		addScanURL(url);
	    } catch (MalformedURLException e) {
	    }
	}
    }

    public void addScanURL(final URL url) {
	if (url == null)
	    throw new NullArgumentException(L10NUtils.format("002088"));
	try {
	    // check if this is a valid url
	    url.openConnection().connect();
	} catch (IOException e) {
	    // logger a warning in case this is not a transient error
	    // that needs fixing (like a wrong scan url setting)
	    log.warn(L10NUtils.format("002089", e.getClass().getName(), e.getMessage()));
	}
	urlList.add(url);
	if (log.isDebugEnabled())
	    log.debug(L10NUtils.format("002090", url));
    }

    public void removeScanURL(final URL url) {
	if (url == null)
	    throw new NullArgumentException(L10NUtils.format("002088"));
	boolean success = urlList.remove(url);
	if (success) {
	    if (log.isDebugEnabled())
		log.debug(L10NUtils.format("002091", url));
	}
    }

    public boolean hasScanURL(final URL url) {
	if (url == null)
	    throw new NullArgumentException(L10NUtils.format("002088"));

	return urlList.contains(url);
    }

    public void setRecursiveSearch(boolean recurse) {
	doRecursiveSearch = recurse;
    }

    public boolean getRecursiveSearch() {
	return doRecursiveSearch;
    }

    public void scan() throws Exception {

	if (urlList == null)
	    throw new NullArgumentException(L10NUtils.format("002088"));

	HashSet<String> oldList = new HashSet<String>(files.keySet());
	List<FileAction> actions = new LinkedList<FileAction>();

	synchronized (urlList) {

	    for (URL url : urlList) {

		// log.debug("scan:" + url);

		File parent = new File(url.getFile());
		File[] children = parent.listFiles();
		if (!parent.exists() || children == null) {
		    log.error(url + " is not exist.");
		    return;
		}
		for (File child : children) {
		    if (!child.canRead()) {
			continue;
		    }

		    FileInfo now = child.isDirectory() ? getDirectoryInfo(child) : getFileInfo(child);
		    FileInfo then = (FileInfo) files.get(now.getPath());
		    if (then == null) { // Brand new, wait a bit to make sure
					// it's not still changing
			now.setNewFile(true);
			files.put(now.getPath(), now);

			if (log.isDebugEnabled())
			    log.debug(L10NUtils.format("002094", now.getPath()));
		    } else {
			oldList.remove(then.getPath());
			if (now.isSame(then)) { // File is the same as the last
						// time we scanned it
			    if (then.isChanging()) {
				if (log.isDebugEnabled())
				    log.debug(L10NUtils.format("002095", now.getPath()));
				// Used to be changing, now in (hopefully) its
				// final state
				if (then.isNewFile()) {
				    actions.add(new FileAction(FileAction.NEW_FILE, child, then));
				} else {
				    actions.add(new FileAction(FileAction.UPDATED_FILE, child, then));
				}
				then.setChanging(false);
			    } // else it's just totally unchanged and we ignore
			      // it this pass
			} else {
			    // The two records are different -- record the
			    // latest as a file that's changing
			    // and later when it stops changing we'll do the add
			    // or update as appropriate.
			    now.setNewFile(then.isNewFile());
			    files.put(now.getPath(), now);
			    if (log.isDebugEnabled())
				log.debug(L10NUtils.format("002096", now.getPath()));

			}
		    }
		}
	    }
	}

	// Look for any files we used to know about but didn't find in this pass
	// for (Iterator<String> it = oldList.iterator(); it.hasNext();) {
	// String name = (String) it.next();

	for (String name : oldList) {
	    FileInfo info = (FileInfo) files.get(name);
	    if (log.isDebugEnabled())
		log.debug(L10NUtils.format("002097", name));

	    if (info.isNewFile()) { // Was never added, just whack it
		files.remove(name);
	    } else {
		actions.add(new FileAction(FileAction.REMOVED_FILE, new File(name), info));
	    }
	}

	// log.debug( "action:" + actions.size() );

	DirectoryListener[] listeners = getDirectoryListeners();

	// for( int i = 0; i < listeners.length ; i++){
	// DirectoryListener listener = listeners[i];

	for (DirectoryListener listener : listeners) {

	    // First pass: validate all changed files, so any obvious errors
	    // come out first
	    for (Iterator<FileAction> it = actions.iterator(); it.hasNext();) {
		FileAction action = (FileAction) it.next();
		if (!listener.validateFile(action.child)) {
		    resolveFile(action);
		    it.remove();
		}
	    }
	    // Second pass: do what we're meant to do
	    for (Iterator<FileAction> it = actions.iterator(); it.hasNext();) {
		FileAction action = (FileAction) it.next();
		try {
		    if (action.action == FileAction.REMOVED_FILE) {
			if (listener.fileDeleted(action.child)) {
			    files.remove(action.child.getPath());
			}
		    } else if (action.action == FileAction.NEW_FILE) {
			String result = listener.fileCreated(action.child);
			if (result != null) {
			    action.info.setNewFile(false);
			}
		    } else if (action.action == FileAction.UPDATED_FILE) {
			listener.fileChanged(action.child);
		    }
		} catch (Exception e) {
		    // log.error(e);
		    log.error(L10NUtils.format("002098", action.getActionName(), action.child.getAbsolutePath()), e);
		} finally {
		    resolveFile(action);
		}
	    }
	}
    }

    ////////////////////////////////////////////////////////////////
    // Private Methods for Handling File
    ///////////////////////////////////////////////////////////////

    private void resolveFile(FileAction action) {
	if (action.action == FileAction.REMOVED_FILE) {
	    files.remove(action.child.getPath());
	} else {
	    action.info.setChanging(false);
	}
    }

    private FileInfo getDirectoryInfo(File dir) {
	FileInfo info = new FileInfo(dir.getAbsolutePath());
	info.setSize(0);
	info.setModified(getLastModifiedInDir(dir));
	return info;
    }

    private long getLastModifiedInDir(File dir) {
	long value = dir.lastModified();
	File[] children = dir.listFiles();
	long test;
	for (int i = 0; i < children.length; i++) {
	    File child = children[i];
	    if (!child.canRead()) {
		continue;
	    }
	    if (child.isDirectory()) {
		test = getLastModifiedInDir(child);
	    } else {
		test = child.lastModified();
	    }
	    if (test > value) {
		value = test;
	    }
	}
	return value;
    }

    private FileInfo getFileInfo(File child) {
	FileInfo info = new FileInfo(child.getAbsolutePath());
	info.setSize(child.length());
	info.setModified(child.lastModified());
	return info;
    }

    ////////////////////////////////////////////////////////////////
    // InnerClass
    ///////////////////////////////////////////////////////////////
    /**
     * @author donghyuck
     */
    private static class FileAction {

	private static int NEW_FILE = 1;
	private static int UPDATED_FILE = 2;
	private static int REMOVED_FILE = 3;
	private int action;
	private File child;
	/**
	 * @uml.property name="info"
	 * @uml.associationEnd
	 */
	private FileInfo info;

	public FileAction(int action, File child, FileInfo info) {
	    this.action = action;
	    this.child = child;
	    this.info = info;
	}

	public String getActionName() {
	    return action == NEW_FILE ? "new file" : action == UPDATED_FILE ? "updated" : "disappeared";
	}
    }

    /**
     * @author donghyuck
     */
    private static class FileInfo implements Serializable {

	private static final long serialVersionUID = 2396755457574442311L;
	/**
	 * @uml.property name="path"
	 */
	private String path;
	/**
	 * @uml.property name="size"
	 */
	private long size;
	/**
	 * @uml.property name="modified"
	 */
	private long modified;
	/**
	 * @uml.property name="newFile"
	 */
	private boolean newFile;
	/**
	 * @uml.property name="changing"
	 */
	private boolean changing;

	public FileInfo(String path) {
	    this.path = path;
	    newFile = false;
	    changing = true;
	}

	/**
	 * @return
	 * @uml.property name="path"
	 */
	public String getPath() {
	    return path;
	}

	/**
	 * @return
	 * @uml.property name="size"
	 */
	public long getSize() {
	    return size;
	}

	/**
	 * @param size
	 * @uml.property name="size"
	 */
	public void setSize(long size) {
	    this.size = size;
	}

	/**
	 * @return
	 * @uml.property name="modified"
	 */
	public long getModified() {
	    return modified;
	}

	/**
	 * @param modified
	 * @uml.property name="modified"
	 */
	public void setModified(long modified) {
	    this.modified = modified;
	}

	/**
	 * @return
	 * @uml.property name="newFile"
	 */
	public boolean isNewFile() {
	    return newFile;
	}

	/**
	 * @param newFile
	 * @uml.property name="newFile"
	 */
	public void setNewFile(boolean newFile) {
	    this.newFile = newFile;
	}

	/**
	 * @return
	 * @uml.property name="changing"
	 */
	public boolean isChanging() {
	    return changing;
	}

	/**
	 * @param changing
	 * @uml.property name="changing"
	 */
	public void setChanging(boolean changing) {
	    this.changing = changing;
	}

	public boolean isSame(FileInfo info) {
	    if (!path.equals(info.path)) {
		throw new IllegalArgumentException(L10NUtils.format("002099"));
	    }
	    return size == info.size && modified == info.modified;
	}
    }
}

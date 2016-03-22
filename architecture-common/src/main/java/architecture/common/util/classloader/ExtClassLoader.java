package architecture.common.util.classloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.StringTokenizer;

import architecture.common.util.L10NUtils;

/**
 * 동적 클래스 로딩을 위한 확장 클래스 로더 클래스
 * 
 * 
 * @author donghyuck
 */
public class ExtClassLoader extends URLClassLoader {

    public static interface Gateway {
	public abstract URL findResource(String name);

	public abstract URL[] findResources(String name) throws IOException;

	public abstract Class<?> loadClass(String name);

    }

    /**
     * @uml.property name="instance"
     * @uml.associationEnd
     */
    private static ExtClassLoader instance;

    /**
     * @return
     * @uml.property name="instance"
     */
    public static ExtClassLoader getInstance() {
	if (instance != null) {
	    return instance;
	} else {
	    instance = new ExtClassLoader(System.getProperty("runtime.ext.dirs"));
	    return instance;
	}
    }

    /**
     * @uml.property name="gateway"
     * @uml.associationEnd
     */
    private Gateway gateway;

    protected ArrayList<URL> containedPaths;

    protected ArrayList<URL> nativePaths;

    String classpath;

    String nativepath;

    public ExtClassLoader(ClassLoader classloader) {
	this(null, classloader);
    }

    public ExtClassLoader(String path) {
	this(path, ClassLoader.getSystemClassLoader());
    }

    public ExtClassLoader(String path, ClassLoader classloader) {
	super(new URL[0], classloader);
	gateway = null;
	containedPaths = new ArrayList<URL>();
	nativePaths = new ArrayList<URL>();
	classpath = null;
	nativepath = null;
	if (path != null)
	    addPath(path);
	if (instance == null)
	    instance = this;
    }

    /**
     * Returns the search path of URLs for loading classes and resources. This
     * includes the original list of URLs specified to the constructor, along
     * with any URLs subsequently appended by the addURL() method.
     * 
     * @return the search path of URLs for loading classes and resources.
     */
    public URL[] _getURLs() {
	return super.getURLs();
    }

    public void addNativePath(String path) {
	if (path == null) {
	    // if(WSLauncher.debug)
	    System.out.println(L10NUtils.format("002201"));
	} else {
	    File file = null;
	    Exception exception = null;
	    for (StringTokenizer stringtokenizer = new StringTokenizer(path, File.pathSeparator); stringtokenizer
		    .hasMoreTokens();) {
		try {
		    file = (new File(stringtokenizer.nextToken())).getCanonicalFile();
		    nativePaths.add(file.toURL());
		} catch (Exception exception1) {
		    exception = exception1;
		}
		// if(WSLauncher.debug)
		if (exception != null)
		    System.out.println("An error occurred adding native path \""
			    + (file != null ? file.toString() : "null") + "\": " + exception);
		else if (!file.exists())
		    System.out.println("Native path \"" + file.getPath()
			    + "\" does not exist! Specify a directory containing a native library.");
		else if (!file.isDirectory()) {
		    System.out.println("Native path \"" + file.getPath()
			    + "\" is not a directory! Specify a directory containing a native library.");
		} else {
		    String as[] = file.list();
		    if (as == null || as.length == 0) {
			System.out.println("Native path \"" + file.getPath()
				+ "\" is empty! Specify a directory containing a native library.");
		    } else {
			boolean flag = false;
			boolean flag1 = false;
			Object obj = null;
			for (int i = as.length - 1; i >= 0; i--)
			    if (as[i] != null) {
				String s1 = as[i].toLowerCase();
				flag |= s1.endsWith(".dll") || s1.endsWith(".so") || s1.endsWith(".a");
				flag1 |= s1.endsWith(".jar") || s1.endsWith(".zip") || s1.endsWith(".class");
			    }

			if (!flag)
			    System.out.println("Native path \"" + file.getPath()
				    + "\" contains no native libraries! Specify a directory containing a native library.");
			if (flag1)
			    System.out.println("Native path \"" + file.getPath()
				    + "\" contains JAR/Zip/class files. Should path be in classpath also/instead?");
		    }
		}
		file = null;
		exception = null;
	    }
	}
    }

    public void addPath(String path) {
	if (path == null) {

	} else {

	    File file = null;
	    for (StringTokenizer stringtokenizer = new StringTokenizer(path, File.pathSeparator); stringtokenizer
		    .hasMoreTokens();) {
		file = new File(stringtokenizer.nextToken());
		try {
		    file = file.getCanonicalFile();
		} catch (IOException ioexception) {
		}

		try {
		    URL url = file.toURL();
		    addURL(url);
		} catch (Exception exception) {
		}

		if (file.isDirectory()) {
		    // if(WSLauncher.debug)
		    System.out.println("Adding jar and zip files from " + file.getPath());
		    String as1[] = file.list();
		    if (as1 != null) {
			Arrays.sort(as1);
			int i = 0;
			while (i < as1.length) {
			    String s1 = as1[i].toLowerCase();
			    // if(WSLauncher.debug)
			    System.out.println("Checking file " + s1);
			    if (s1.endsWith(".jar") || s1.endsWith(".zip")) {
				File file1 = new File(file, as1[i]);
				try {
				    addURL(file1.toURL());
				} catch (Exception exception1) {
				}
			    }
			    i++;
			}
		    }
		}
		file = null;
	    }
	}

    }

    /**
     * Appends the specified URL to the list of URLs to search for classes and
     * resources.
     *
     * @param url
     *            the URL to be added to the search path of URLs
     */
    public void addURL(URL url) {
	if (!containedPaths.contains(url)) {
	    containedPaths.add(url);
	    super.addURL(url);
	    classpath = null;
	}
    }

    /**
     * Finds and loads the class with the specified name from the URL search
     * path. Any URLs referring to JAR files are loaded and opened as needed
     * until the class is found.
     *
     * @param name
     *            the name of the class
     * @return the resulting class
     * @exception ClassNotFoundException
     *                if the class could not be found
     */
    public Class<?> findClass(String name) throws ClassNotFoundException {
	Class<?> class1 = super.findClass(name);

	/*
	 * if(WSLauncher.debug) WSLauncher.out.println("[Loaded " + s +
	 * " by ExtClassLoader]");
	 */

	return class1;
    }

    protected String findLibrary(String name) {
	/*
	 * if(WSLauncher.debug) WSLauncher.out.println(
	 * "looking for library on nativepath: " + s);
	 */
	String s1 = findLibrary0(name, nativePaths);
	if (s1 == null) {
	    /*
	     * if(WSLauncher.debug) WSLauncher.out.println(
	     * "looking for library on classpath: " + s);
	     */
	    s1 = findLibrary0(name, containedPaths);
	}
	return s1;
    }

    protected String findLibrary0(String name, ArrayList<URL> pathList) {
	final String mappedLib = System.mapLibraryName(name);
	final ArrayList<URL> paths = pathList;

	for (URL url : paths) {

	    File file = new File(url.getFile());
	    if (!file.isDirectory())
		continue;

	    File file1 = new File(file, mappedLib);
	    if (!file1.exists())
		continue;

	    return file1.getPath();
	}
	return null;
    }

    public URL findResource(String name) {
	URL url = super.findResource(name);
	if (url == null && gateway != null)
	    url = gateway.findResource(name);
	return url;
    }

    /**
     * Returns an Enumeration of URLs representing all of the resources on the
     * URL search path having the specified name.
     *
     * @param name
     *            the resource name
     * @exception IOException
     *                if an I/O exception occurs
     * @return an <code>Enumeration</code> of <code>URL</code>s
     */
    public Enumeration findResources(String name) throws IOException {
	final Enumeration extensionResources = super.findResources(name);
	final URL resources[];
	if (gateway != null)
	    resources = gateway.findResources(name);
	else
	    resources = null;
	if (resources == null || resources.length == 0)
	    return extensionResources;
	else
	    return new Enumeration() {

		int current = 0;

		public boolean hasMoreElements() {
		    if (current < resources.length)
			return true;
		    else
			return extensionResources.hasMoreElements();
		}

		public Object nextElement() {
		    if (current < resources.length)
			return resources[current++];
		    else
			return extensionResources.nextElement();
		}
	    };
    }

    public String getClassPath() {
	if (classpath == null) {
	    StringBuffer stringbuffer = new StringBuffer();
	    URL aurl[] = super.getURLs();
	    for (int i = 0; i < aurl.length; i++)
		try {
		    File file = new File(aurl[i].getFile());
		    stringbuffer.append(file.getCanonicalPath());
		    stringbuffer.append(File.pathSeparator);
		} catch (Exception exception) {
		}

	    stringbuffer.append(System.getProperty("java.class.path"));
	    classpath = stringbuffer.toString();
	}
	return classpath;
    }

    public String getNativePath() {
	if (nativepath == null) {
	    StringBuffer stringbuffer = new StringBuffer();
	    Object aobj[] = nativePaths.toArray();
	    for (int i = 0; i < nativePaths.size(); i++)
		try {
		    File file = new File(((URL) aobj[i]).getFile());
		    stringbuffer.append(file.getCanonicalPath());
		    stringbuffer.append(File.pathSeparator);
		} catch (Exception exception) {
		}

	    nativepath = stringbuffer.toString();
	}
	return nativepath;
    }

    public URL[] getURLs() {
	return null;
    }

    public void installGateway(Gateway gateway1) {
	if (gateway != null) {
	    throw new IllegalStateException("Gateway cannot be installed twice");
	} else {
	    gateway = gateway1;
	    return;
	}
    }

    /**
     * Loads the class with the specified <a href="#name">binary name</a>. The
     * default implementation of this method searches for classes in the
     * following order:
     *
     * <p>
     * <ol>
     *
     * <li>
     * <p>
     * Invoke {@link #findLoadedClass(String)} to check if the class has already
     * been loaded.
     * </p>
     * </li>
     *
     * <li>
     * <p>
     * Invoke the {@link #loadClass(String) <tt>loadClass</tt>} method on the
     * parent class loader. If the parent is <tt>null</tt> the class loader
     * built-in to the virtual machine is used, instead.
     * </p>
     * </li>
     *
     * <li>
     * <p>
     * Invoke the {@link #findClass(String)} method to find the class.
     * </p>
     * </li>
     *
     * </ol>
     *
     * <p>
     * If the class was found using the above steps, and the <tt>resolve</tt>
     * flag is true, this method will then invoke the
     * {@link #resolveClass(Class)} method on the resulting <tt>Class</tt>
     * object.
     *
     * <p>
     * Subclasses of <tt>ClassLoader</tt> are encouraged to override
     * {@link #findClass(String)}, rather than this method.
     * </p>
     *
     * @param name
     *            The <a href="#name">binary name</a> of the class
     *
     * @param resolve
     *            If <tt>true</tt> then resolve the class
     *
     * @return The resulting <tt>Class</tt> object
     *
     * @throws ClassNotFoundException
     *             If the class could not be found
     */
    public Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
	return super.loadClass(name, resolve);
    }

}

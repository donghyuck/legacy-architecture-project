package architecture.ee.plugin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.zip.ZipFile;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import architecture.ee.util.ApplicationConstants;

import com.google.common.collect.Lists;



public class PluginUtils {


    private static final Log log = LogFactory.getLog(PluginUtils.class);
    
    public static final String PLUGIN_DIR_SYS_PROP = "pluginDirs";
    public static final String PLUGIN_PROP_REDERER_COMMUNITIES = "renderer.communityIDs";
    
    public static File extractPlugin(File jarFile, File basedir)
        throws IOException
    {
        String pluginName = jarFile.getName().substring(0, jarFile.getName().length() - 4).toLowerCase();
        File dir = new File(basedir, pluginName);
        if(!dir.exists())
        {
            unzipPlugin(pluginName, jarFile, dir);
            try
            {
                Document document = getPluginConfiguration(dir);
                if(document != null)
                {
                    Node node = document.selectSingleNode("/plugin/name");
                    if(node != null)
                    {
                        String pluginName2 = node.getText();
                        if(!pluginName2.equals(pluginName))
                        {
                            pluginName = pluginName2;
                            File todir = new File(basedir, pluginName2);
                            if(!dir.equals(todir))
                            {
                                if(todir.exists())
                                    FileUtils.deleteDirectory(todir);
                                FileUtils.copyDirectory(dir, todir);
                                FileUtils.deleteDirectory(dir);
                                dir = todir;
                            }
                        }
                    }
                }
            }
            catch(DocumentException e)
            {
                log.error(e);
            }
        }
        return dir;
    }

    public static List<String> getDevPluginPaths()
    {
        List<String> paths = Lists.newArrayList();
        String pluginDirs = System.getProperty(PLUGIN_DIR_SYS_PROP);

        if(Boolean.parseBoolean(System.getProperty("framework.devMode", "false")) && pluginDirs != null)
        {
            String split[] = pluginDirs.split(",");
            for(String str: split){
                str = str.trim();
                if(!"".equals(str))
                    paths.add(str);
            }
        }
        return paths;
    }

    public static Document getPluginConfiguration(File pluginDir)
        throws DocumentException
    {
        File pluginConfig = new File(pluginDir, "plugin.xml");
        if(pluginConfig.exists())
        {
            SAXReader saxReader = new SAXReader();            
            saxReader.setEncoding(ApplicationConstants.DEFAULT_CHAR_ENCODING);
            return saxReader.read(pluginConfig);
        } else
        {
            return null;
        }
    }

    public static File outputJarFile(String pluginName, InputStream in, File basedir)
        throws IOException
    {
        File file;
        OutputStream out = null;
        try{
	        file = new File(basedir, (new StringBuilder()).append(pluginName).append(".jar").toString());
	        file.createNewFile();        
	        out = new BufferedOutputStream(new FileOutputStream(file));	        
	        byte buffer[] = new byte[32768];	        
	        int len;	        
	        while((len = in.read(buffer)) != -1) 
	            out.write(buffer, 0, len);        	
        }finally{
            if(out != null)
                out.close();
        }
        return file;
    }

    public static boolean resultsContainsClass(List results, final Class class1)
    {
		return CollectionUtils.exists(results, new Predicate() {

			public boolean evaluate(Object obj) {
				return obj != null && obj.getClass().isAssignableFrom(class1);
			}
		});
    }

    private static void unpackArchives(File libDir)
    {
		File packedFiles[] = libDir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".pack");
			}
		});
		
        if(packedFiles == null)
            return;
        
        for(File packedFile : packedFiles){
        	try
            {
                String jarName = packedFile.getName().substring(0, packedFile.getName().length() - ".pack".length());
                File jarFile = new File(libDir, jarName);
                if(jarFile.exists())
                    jarFile.delete();
                InputStream in = new BufferedInputStream(new FileInputStream(packedFile));
                JarOutputStream out = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(new File(libDir, jarName))));
                java.util.jar.Pack200.Unpacker unpacker = Pack200.newUnpacker();
                unpacker.unpack(in, out);
                in.close();
                out.close();
                packedFile.delete();
            }
            catch(Exception e)
            {
                log.error(e);
            }
        }
        
    }

    public static void unzipPlugin(String pluginName, File jarFile, File dir)
    {
        ZipFile zipFile;
        try
        {
            zipFile = new JarFile(jarFile);
            if(zipFile.getEntry("plugin.xml") == null)
                return;
            
            dir.mkdir();
            dir.setLastModified(jarFile.lastModified());
            log.debug((new StringBuilder()).append("Extracting plugin: ").append(pluginName).toString());
            
            Enumeration e = zipFile.entries();
            do
            {
                if(!e.hasMoreElements())
                    break;
                JarEntry entry = (JarEntry)e.nextElement();
                File entryFile = new File(dir, entry.getName());
                if(!entry.getName().toLowerCase().endsWith("manifest.mf") && !entry.isDirectory())
                {
                    entryFile.getParentFile().mkdirs();
                    FileOutputStream out = new FileOutputStream(entryFile);
                    InputStream zin = zipFile.getInputStream(entry);
                    byte b[] = new byte[512];
                    int len;
                    while((len = zin.read(b)) != -1) 
                        out.write(b, 0, len);
                    out.flush();
                    out.close();
                    zin.close();
                }
            } while(true);
            zipFile.close();
            unpackArchives(new File(dir, "lib"));
        }
        catch(Exception e)
        {
            log.error(e);
        }
        return;
    }

    private PluginUtils()
    {
    }

}

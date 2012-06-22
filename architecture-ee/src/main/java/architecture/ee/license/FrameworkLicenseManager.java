package architecture.ee.license;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicBoolean;

import architecture.common.exception.LicenseException;
import architecture.common.license.License;
import architecture.common.license.LicenseManager;
import architecture.common.license.LicenseProvider;
import architecture.ee.component.admin.AdminHelper;

public class FrameworkLicenseManager extends LicenseManager {
	
	/**
	 * @author   donghyuck
	 */
	public enum FrameworkLicenseProperty {
		
		/**
		 * @uml.property  name="numSeats"
		 * @uml.associationEnd  
		 */
		numSeats, 
		/**
		 * @uml.property  name="numClusterMembers"
		 * @uml.associationEnd  
		 */
		numClusterMembers, 
		/**
		 * @uml.property  name="numCopies"
		 * @uml.associationEnd  
		 */
		numCopies, 
		/**
		 * @uml.property  name="expirationDate"
		 * @uml.associationEnd  
		 */
		expirationDate ;		
	};
		
	protected AtomicBoolean initialized; //initialized = new AtomicBoolean(false);
	protected AtomicBoolean failed;
	
    protected FrameworkLicenseManager()
    {
    	initialized = new AtomicBoolean(false);
        failed = new AtomicBoolean(false);
    }
    
    public void initialize(){
    	
    	if(initialized.get()){
    		log.debug("Already initialized, call destroy()");
    	}else{
    		
    		File file = AdminHelper.getRepository().getLicenseFile();
    		
    		if(!file.canRead() && file.exists() ){
    			String msg = (new StringBuilder()).append("The \"").append(file.getName()).append("\" license file was found").append(", but system does not have permission to read it.").toString();
                log.fatal(msg);
                throw new LicenseException(msg);
    		}    		
    		try
            {
                Reader reader = null;
                if(file.exists())
                    reader = new BufferedReader(new FileReader(file));                
                initialize(reader);
            }
            catch(IOException e)
            {
                log.debug(e.getMessage(), e);
                failed.set(true);
                throw new LicenseException("Reading of license failed.");
            }
            catch(LicenseException e)
            {
                log.debug(e.getMessage(), e);
                failed.set(true);
                throw e;
            }
            initialized.set(true);
    	}
    }
    
	public void initialize(Reader reader) throws IOException {
		if (!initialized.get())
			initialize(((LicenseProvider) (new DefaultLicenseProvider())), reader);
		else
			log.debug("Already initialized, call destroy");
	}
    
	
	public void initialize(LicenseProvider licenseProvider, Reader reader) throws LicenseException, IOException {
		if (!initialized.get()) {
			
			if ("true".equals(AdminHelper.getRepository().getSetupApplicationProperties().get("license.evaluation")) ) {
				log.info("Using license information from local property (startup-config.xml)");
				setProvider(licenseProvider);
				log.warn("Initialization with a license file has failed, initializing in evaluation mode.");
				setLicense(createDefaultLicense());
			} else if (reader != null) {
				log.info("Using license information from license file");
				super.initialize(licenseProvider, reader);
			} else {
				log.info("Initializing with evaluation license.");
				setLicense(createDefaultLicense());
			}
		} else {
			log.debug("Already initialized, call destroy()");
		}
	}
	
	
	
    public void destroy()
    {
        initialized.set(false);
    }
    
    public static License createDefaultLicense()
    {
        DefaultLicenseProvider provider = new DefaultLicenseProvider();
        License defaultLicense = new License();
        defaultLicense.setID(12345L);
        defaultLicense.setClient(new License.Client("Evaluation User", ""));
        defaultLicense.setType(License.Type.EVALUATION);
        defaultLicense.setCreationDate((new GregorianCalendar(2012, 1, 1)).getTime());
        defaultLicense.setVersion(provider.getVersion());
        defaultLicense.setName(provider.getName());
        String edition = null ;// getBuildProperty("edition");
        if(edition == null)
            edition = "internal";
        defaultLicense.setEdition(edition);
        defaultLicense.setModules(new ArrayList<License.Module>( provider.getInstalledModules()));
        defaultLicense.getProperties().put(FrameworkLicenseProperty.numSeats.name(), "5000");
        defaultLicense.getProperties().put(FrameworkLicenseProperty.numClusterMembers.name(), "1");
        defaultLicense.getProperties().put("defaultLicense", "true");
        return defaultLicense;
    }
    
    public boolean isExternal()
    {
        License license = getLicense();
        String dev = (String)license.getProperties().get("developer");
        boolean isDeveloper = dev != null && "true".equalsIgnoreCase(dev);
        if(isDeveloper)
        {
            String s = System.getProperty("framework.license.edition");
            if(s != null)
            {
                if("external".equals(s))
                    return true;
                if("internal".equals(s))
                    return false;
                log.fatal((new StringBuilder()).append("Unknown edition ").append(s).toString());
            }
        }
        return "external".equalsIgnoreCase(license.getEdition());
    }
}

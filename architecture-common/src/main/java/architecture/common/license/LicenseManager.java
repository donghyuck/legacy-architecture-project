package architecture.common.license;

import java.io.IOException;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.exception.LicenseException;
import architecture.common.exception.LicenseInitializationException;
import architecture.common.license.io.LicenseReader;
import architecture.common.license.validator.Validator;

public class LicenseManager {
    
	private static final DateFormat dateFormat = DateFormat.getDateTimeInstance(2, 3);
    
    protected static final Log log = LogFactory.getLog(LicenseManager.class);
    
    private License license;
    private LicenseProvider provider;
    
	public void initialize(LicenseProvider provider, Reader reader) throws LicenseException, IOException {
		this.provider = provider;
		license = null;
		
		LicenseReader licenseReader = new LicenseReader();
		License l = licenseReader.read(reader);
		Collection<LicenseException> exceptions = validate(provider, l);
		if (!exceptions.isEmpty()) {
			throw new LicenseInitializationException("License Validation Failed", exceptions);
		} else {
			l.setVersion(provider.getVersion());
			license = l;
			return;
		}
	}
    
    public static Collection<LicenseException> validate(LicenseProvider provider, License license)
    {
        ArrayList<LicenseException> exceptions = new ArrayList<LicenseException>();
        for(Validator v : provider.getValidators()){
        	try
            {
                v.validate(license);
            }
            catch(LicenseException e)
            {
                log.fatal(e.getMessage(), e);
                exceptions.add(e);
            }
        }
        return exceptions;
    }
    
    public License getLicense()
    {
        return license;
    }

    public boolean isInitialized()
    {
        return license != null;
    }
    
    public boolean isModuleLicensed(String module)
    {
    	for( License.Module m :provider.getInstalledModules()){
            if(module.equals(m.getName()) && license.getModules().contains(m))
                return true;
    		
    	}
        return false;
    }
    
    public Collection<License.Module> getLicensedModules()
    {
        ArrayList<License.Module> list = new ArrayList<License.Module>();
        for( License.Module m : license.getModules()){
        	 if(provider.getInstalledModules().contains(m))
                 list.add(m);
        }       
        return Collections.unmodifiableCollection(list);
    }

    
    public static Date getDateProperty(String property, License license)
    {
        Date d = null;
        
        String dataString = (String)license.getProperties().get(property);
        if(dataString != null)
            try
            {
                d = dateFormat.parse(dataString);
            }
            catch(ParseException e)
            {
                log.fatal( e.getMessage(), e);
            }
        return d;
    }
    
    protected void setLicense(License license)
    {
        this.license = license;
    }

    protected LicenseProvider getProvider()
    {
        return provider;
    }

    protected void setProvider(LicenseProvider provider)
    {
        this.provider = provider;
    }
}

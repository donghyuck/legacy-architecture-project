/*
 * Copyright 2012 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.ee.license;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicBoolean;

import architecture.common.exception.LicenseException;
import architecture.common.license.License;
import architecture.common.license.LicenseManager;
import architecture.common.license.LicenseProvider;
import architecture.common.util.L10NUtils;
import architecture.common.util.StringUtils;
import architecture.ee.component.admin.AdminHelper;

public class FrameworkLicenseManager extends LicenseManager {

/**
 * 
 * 
 * @author  <a href="mailto:donghyuck.son@gmail.com">Donghyuck Son </a>
 */
	public enum FrameworkLicenseProperty {
		
		numSeats, 

		numClusterMembers, 

		numCopies, 

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
    		log.debug(  L10NUtils.getMessage( "003409" ));
    	}else{
    		
    		File file = AdminHelper.getRepository().getLicenseFile(); 
    		
    		if(!file.canRead() && file.exists() ){
                String msg = L10NUtils.format("003410",  file.getName() );
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
                throw new LicenseException( L10NUtils.getMessage( "003403" ) );
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
			initialize( new DefaultLicenseProvider(), reader);
		else
			log.debug(  L10NUtils.getMessage( "003409" ));
	}
    
	
	public void initialize(LicenseProvider licenseProvider, Reader reader) throws LicenseException, IOException {
		if (!initialized.get()) {
			
			Reader readerToUse = reader ;
			
			String licenseKey = AdminHelper.getRepository().getSetupApplicationProperties().get("license.file");
			if( readerToUse == null && !StringUtils.isEmpty( licenseKey )){
						
				readerToUse = new StringReader( licenseKey );				
			}
			
			if ("true".equals(AdminHelper.getRepository().getSetupApplicationProperties().get("license.evaluation")) ) {				
				
				log.info( L10NUtils.getMessage( "003411" ) );				
				setProvider(licenseProvider);				
				log.warn( L10NUtils.getMessage( "003412" )  );				
				setLicense(createDefaultLicense());
				
			} else if (readerToUse != null) {			
				
				log.info( L10NUtils.getMessage( "003413" ) );				
				super.initialize(licenseProvider, readerToUse);		
				
			} else {				
				log.info( L10NUtils.getMessage( "003404" ) );
				setLicense(createDefaultLicense());
			}
		} else {
			log.debug(  L10NUtils.getMessage( "003409" ));
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
        defaultLicense.getProperties().put(FrameworkLicenseProperty.numSeats.name(), "500");
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
            String str = System.getProperty("framework.license.edition");
            if(str != null)
            {
                if("external".equals(str))
                    return true;
                if("internal".equals(str))
                    return false;
                log.fatal((new StringBuilder()).append("Unknown edition ").append(str).toString());
            }
        }
        return "external".equalsIgnoreCase(license.getEdition());
    }
}

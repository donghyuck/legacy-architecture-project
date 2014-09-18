package architecture.ee.license;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import architecture.common.license.License;
import architecture.common.license.LicenseProvider;
import architecture.common.license.validator.CheckSignatureValidator;
import architecture.common.license.validator.NameValidator;
import architecture.common.license.validator.Validator;

public class DefaultLicenseProvider implements LicenseProvider {

    public DefaultLicenseProvider()
    {
    }

    public String getName()
    {
        return "APPLICATION ARCHITECTURE for JAVA";
    }

    public License.Version getVersion()
    {
        return new License.Version(1, 0, 0);
    }

    public Collection<Validator> getValidators()
    {
        ArrayList<Validator> validators = new ArrayList<Validator>();
        validators.add(new CheckSignatureValidator());
        validators.add(new NameValidator(getName()));
        return Collections.unmodifiableCollection(validators);
    }

    public Collection<License.Module> getInstalledModules()
    {
        ArrayList<License.Module> modules = new ArrayList<License.Module>();
        
        modules.add(new License.Module("wiki"));
        modules.add(new License.Module("blogs"));
        modules.add(new License.Module("forums"));
        modules.add(new License.Module("analytics"));
        modules.add(new License.Module("bridges"));
        
        return Collections.unmodifiableCollection(modules);
    }
}

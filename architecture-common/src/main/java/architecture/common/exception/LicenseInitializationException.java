package architecture.common.exception;

import java.util.Collection;

public class LicenseInitializationException extends LicenseException {

    private Collection<LicenseException> exceptions;
    
	public LicenseInitializationException(String msg, Collection<LicenseException> cause) {
		super(msg);
	}
	
    public Collection<LicenseException> getExceptions()
    {
        return exceptions;
    }


}

package architecture.common.exception;

import java.util.Collection;

/**
 * @author  donghyuck
 */
public class LicenseInitializationException extends LicenseException {

    /**
	 * @uml.property  name="exceptions"
	 */
    private Collection<LicenseException> exceptions;
    
	public LicenseInitializationException(String msg, Collection<LicenseException> cause) {
		super(msg);
	}
	
    /**
	 * @return
	 * @uml.property  name="exceptions"
	 */
    public Collection<LicenseException> getExceptions()
    {
        return exceptions;
    }


}

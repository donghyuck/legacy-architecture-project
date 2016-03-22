package architecture.user.spi;

import architecture.ee.exception.ApplicationException;

public class AlreadyExistsException extends ApplicationException {

    /**
    * 
    */
    private static final long serialVersionUID = 5916721737344787601L;

    public AlreadyExistsException() {
	super();
    }

    public AlreadyExistsException(String msg, Throwable cause) {
	super(msg, cause);
    }

    public AlreadyExistsException(String msg) {
	super(msg);
    }

    public AlreadyExistsException(Throwable cause) {
	super(cause);
    }

}

package architecture.user;

import architecture.ee.exception.ApplicationException;

public class GroupNotFoundException extends ApplicationException {

    public GroupNotFoundException() {
	super();
    }

    public GroupNotFoundException(String msg, Throwable cause) {
	super(msg, cause);
    }

    public GroupNotFoundException(String msg) {
	super(msg);
    }

    public GroupNotFoundException(Throwable cause) {
	super(cause);
    }

}

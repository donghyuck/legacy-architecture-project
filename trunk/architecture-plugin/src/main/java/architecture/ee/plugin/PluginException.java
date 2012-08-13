package architecture.ee.plugin;

import org.apache.commons.lang.exception.NestableException;

public class PluginException extends NestableException {

	public PluginException() {
		super();
	}

	public PluginException(String msg) {
		super(msg);
	}

	public PluginException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public PluginException(Throwable cause) {
		super(cause);
	}

}

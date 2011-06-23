package architecture.ee.security;

import java.io.Serializable;

public interface AuthToken extends Serializable {

	public abstract long getUserId();

	public abstract boolean isAnonymous();
}

package architecture.common.user.authentication;

public interface AuthToken {

	public abstract long getUserId();

	public abstract boolean isAnonymous();
	
	public abstract String getUsername();
	
}

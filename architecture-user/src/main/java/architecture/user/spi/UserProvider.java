package architecture.user.spi;

import architecture.common.user.User;

public interface UserProvider {
	
	public abstract User getUser(User user);

	public abstract Iterable<User> getUsers();

	public abstract Iterable<String> getUsernames();

	public abstract boolean supportsUpdate();

	public abstract void update(User user) throws UnsupportedOperationException;

	public abstract User create(User user) throws AlreadyExistsException, UnsupportedOperationException;

	public abstract void delete(User user);

	public abstract boolean supportsPagination();

	public abstract int getCount() throws UnsupportedOperationException;

	public abstract Iterable<User> getUsers(int start, int num);

	public abstract String getName();
}

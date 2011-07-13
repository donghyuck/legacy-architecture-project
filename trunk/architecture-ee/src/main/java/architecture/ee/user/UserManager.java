package architecture.ee.user;

import java.util.Date;
import java.util.List;

public interface UserManager {
	
	/**
	 * 새로운 사용자를 생성한다.
	 * @param user
	 * @return
	 * @throws UserAlreadyExistsException
	 * @throws UnsupportedOperationException
	 * @throws EmailAlreadyExistsException
	 */
	public abstract User createUser(User user) throws UserAlreadyExistsException, UnsupportedOperationException, EmailAlreadyExistsException;

	/**
	 * 새로운 로컬 사용자를 생성한다.
	 * @param user
	 * @return
	 * @throws UserAlreadyExistsException
	 */
	public abstract User createApplicationUser(User user) throws UserAlreadyExistsException;

	public abstract User getUser(User user);

	public abstract User getUser(User user, boolean flag);

	public abstract User getUser(String s) throws UserNotFoundException;

	public abstract User getUser(long l) throws UserNotFoundException;

	public abstract void deleteUser(User user)
			throws UnsupportedOperationException, UserNotFoundException;

	public abstract void deleteUserAndContent(User user)
			throws UnsupportedOperationException, UserNotFoundException;

	public abstract int getApplicationUserCount();

	public abstract List getApplicationUsers();

	public abstract List getApplicationUsers(int i, int j);

	public abstract int getAuthenticatedUserCount();

	public abstract int getTotalUserCount();

	public abstract int getRecentUserCount(Date date);

	public abstract List getUsers();

	public abstract List getUsers(int i, int j);

	public abstract boolean isCreationSupported();

	public abstract User updateUser(User user) throws UserNotFoundException,
			UserAlreadyExistsException;

	public abstract void enableUser(User user);

}

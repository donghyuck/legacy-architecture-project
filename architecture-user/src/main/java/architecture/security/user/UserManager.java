package architecture.security.user;

import java.util.Date;
import java.util.List;

import architecture.common.user.User;
import architecture.common.user.UserAlreadyExistsException;
import architecture.common.user.UserNotFoundException;

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

	public abstract User getUser(String username) throws UserNotFoundException;

	public abstract User getUser(long userID) throws UserNotFoundException;

	
	public abstract void deleteUser(User user) throws UnsupportedOperationException, UserNotFoundException;

	public abstract void deleteUserAndContent(User user) throws UnsupportedOperationException, UserNotFoundException;

	
	public abstract int getApplicationUserCount();

	public abstract List<User> getApplicationUsers();

	public abstract List<User> getApplicationUsers(int i, int j);

	public abstract int getAuthenticatedUserCount();

	public abstract int getTotalUserCount();

	public abstract int getRecentUserCount(Date date);

	public abstract List<User> getUsers();

	public abstract List<User> getUsers(int i, int j);

	public abstract boolean isCreationSupported();

	public abstract User updateUser(User user) throws UserNotFoundException, UserAlreadyExistsException;

	public abstract void enableUser(User user);
	
}

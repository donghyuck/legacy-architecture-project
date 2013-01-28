package architecture.user.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import architecture.common.user.User;

public interface UserDao {

    public abstract List<Integer> getUserIdsWithUserProperty(String propertyName, String propertyValue);

    public abstract User create(User user);

    public abstract User update(User user);

    public abstract User getUserByUsername(String username);

    public abstract User getUserByUsernameNoCase(String username);

    public abstract User getUserByEmail(String email);

    public abstract User getUserById(long userId);

    public abstract void delete(User user);

    public abstract Map<String, String> getUserProperties(long userId);

    public abstract void setUserProperties(long userId, Map<String, String> props);

    public abstract int getApplicationUserCount();

    public abstract List<User> getApplicationUsers();

    public abstract List<User> getApplicationUsers(int startIndex, int numResults);

    public abstract int getTotalUserCount();

    public abstract int getAuthenticatedUserCount();

    public abstract int getRecentUserCount(Date date);

    /**
     * 
     * @param nameOrEmail
     * @return
     */
    public abstract List<User> findUsers(String nameOrEmail);
    
    /**
     * 
     * @param nameOrEmail
     * @param startIndex
     * @param numResults
     * @return
     */
    public abstract List<User> findUsers(String nameOrEmail, int startIndex, int numResults);
    
    public abstract int getFoundUserCount(String nameOrEmail) ;
    
    
    public abstract List<User> getAllUsers();

    public abstract List<User> getAllUsers(int startIndex, int numResults);

    public abstract List<Integer> getUserIdsWithStatuses(int status[]);
    
}
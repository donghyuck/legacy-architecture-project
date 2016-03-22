/*
 * Copyright 2012 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.common.user;

import java.util.Date;
import java.util.List;

public interface UserManager {

    /**
     * 새로운 사용자를 생성한다.
     * 
     * @param user
     * @return
     * @throws UserAlreadyExistsException
     * @throws UnsupportedOperationException
     * @throws EmailAlreadyExistsException
     */
    public abstract User createUser(User user)
	    throws UserAlreadyExistsException, UnsupportedOperationException, EmailAlreadyExistsException;

    /**
     * 새로운 로컬 사용자를 생성한다.
     * 
     * @param user
     * @return
     * @throws UserAlreadyExistsException
     */
    public abstract User createUser(User user, Company company)
	    throws UserAlreadyExistsException, UnsupportedOperationException, EmailAlreadyExistsException;

    /**
     * 새로운 로컬 사용자를 생성한다.
     * 
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

    public abstract List<User> getUsers(int startIndex, int numResults);

    public abstract boolean isCreationSupported();

    public abstract User updateUser(User user) throws UserNotFoundException, UserAlreadyExistsException;

    public abstract void enableUser(User user);

    public abstract List<User> findUsers(String nameOrEmail);

    public abstract List<User> findUsers(String nameOrEmail, int startIndex, int numResults);

    public abstract int getFoundUserCount(String nameOrEmail);

    public abstract int getUserCount(Company company);

    public abstract List<User> getUsers(Company company);

    public abstract List<User> getUsers(Company company, int startIndex, int numResults);

    public abstract List<User> findUsers(Company company, String nameOrEmail);

    public abstract List<User> findUsers(Company company, String nameOrEmail, int startIndex, int numResults);

    public abstract int getFoundUserCount(Company company, String nameOrEmail);

    public abstract List<User> findUsersWithGroupFilter(Company company, Group group, String nameOrEmail);

    public abstract List<User> findUsersWithGroupFilter(Company company, Group group, String nameOrEmail,
	    int startIndex, int numResults);

    public abstract int getFoundUserCountWithGroupFilter(Company company, Group group, String nameOrEmail);

}

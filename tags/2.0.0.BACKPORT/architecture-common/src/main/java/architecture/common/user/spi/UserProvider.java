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
package architecture.common.user.spi;

import java.util.Map;

import architecture.common.user.User;
import architecture.common.user.UserAlreadyExistsException;
import architecture.common.user.UserNotFoundException;

public interface UserProvider {

	public User create(User user )  throws UserAlreadyExistsException ;
	
	/**
	 * username, password, email 값을 가지고 사용자를 생성한다.
	 * 
	 * 기능을 지원하지 않는 경우 UnsupportedOperationException 이 리턴된다.
	 * @param username
	 * @param password
	 * @param email
	 * @return
	 * @throws UserAlreadyExistsException
	 */
	public User createUser(String username, String password, String email) throws UserAlreadyExistsException ;
	
	/**
	 * username, password, name, email 값과 추가 옵션값을 가지고 사용자를 생성한다.
	 * 
	 * 기능을 지원하지 않는 경우 UnsupportedOperationException 이 리턴된다.
	 * @param username
	 * @param password
	 * @param name
	 * @param email
	 * @param nameVisible
	 * @param emailVisible
	 * @param proeprties
	 * @return
	 * @throws UserAlreadyExistsException
	 */
	public User createUser(String username, String password, String name,  String email, boolean  nameVisible, boolean emailVisible, Map proeprties) throws UserAlreadyExistsException ;
	
	/**
	 * userId 에 해당하는 사용자를 리턴한다.
	 * 
	 * 기능을 지원하지 않는 경우 UnsupportedOperationException 이 리턴된다.
	 * @param userId
	 * @return
	 * @throws UserNotFoundException
	 */
	public User getUser(long userId) throws UserNotFoundException ;
	
	public User getUser(User user);
	
	/**
	 * username 에 해당하는 사용자를 리턴한다.
	 * 
	 * 기능을 지원하지 않는 경우 UnsupportedOperationException 이 리턴된다.
	 * @param username
	 * @return
	 * @throws UserNotFoundException
	 */
	public User getUser(String username) throws UserNotFoundException ;
	
	/**
	 * username 에 해당하는 userId 값을 리턴한다.
	 * 
	 * 기능을 지원하지 않는 경우 UnsupportedOperationException 이 리턴된다.
	 * @param username
	 * @return
	 * @throws UserNotFoundException
	 */
	public long getUserId(String username) throws UserNotFoundException ;
	
	/**
	 * 사용자를 삭제한다.
	 * 
	 * 기능을 지원하지 않는 경우 UnsupportedOperationException 이 리턴된다.
	 * @param user
	 */
	public void deleteUser(User user);
	
	public void updateUser(User user);
	
	/**
	 * 전체사용자를 리턴한다.
	 * 
	 * 기능을 지원하지 않는 경우 UnsupportedOperationException 이 리턴된다.
	 * @return
	 */
	public abstract Iterable<User> getUsers();

	/**
	 * startIndex 에서 시작하여 numResults 수 만큼의 사용자를 리턴한다.
	 * 기능을 지원하지 않는 경우 UnsupportedOperationException 이 리턴된다.
	 * @param startIndex
	 * @param numResults
	 * @return
	 */
	public abstract Iterable<User> getUsers(int startIndex, int numResults);
	
	/**
	 * 전체 사용자 수를 리턴한다.
	 * 
	 * 기능을 지원하지 않는 경우 UnsupportedOperationException 이 리턴된다.
	 * @return
	 */
	public int getUserCount();
	
	
	public String getName();
	
	public boolean supportsUpdate();
	
	public boolean supportsPaginate();
	
		
}

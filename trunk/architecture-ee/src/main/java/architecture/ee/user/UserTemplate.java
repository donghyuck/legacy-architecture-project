package architecture.ee.user;

import java.util.Map;

import architecture.ee.model.impl.UserModelImpl;


/**
 * 
 * @author donghyuck
 */
public class UserTemplate extends UserModelImpl implements User {

	public UserTemplate() {
		super();
	}

	public UserTemplate(long userId) {
		super(userId);
	}

	public UserTemplate(String userName, String password, String email, String name, boolean emailVisible, boolean nameVisible, Map<String, String> props) {
		super(userName, password, email, name, emailVisible, nameVisible, props);
	}

	public UserTemplate(String userName, String password, String email, String firstName, String lastName, boolean emailVisible, boolean nameVisible, Map<String, String> props) {
		super(userName, password, email, firstName, lastName, emailVisible, nameVisible, props);
	}

	public UserTemplate(String userName, String password, String email, String name) {
		super(userName, password, email, name);
	}

	public UserTemplate(String userName, String password, String email) {
		super(userName, password, email);
	}

	public UserTemplate(String userName) {
		super(userName);
	}

	public UserTemplate(User user) {
		super(user);
	}

}
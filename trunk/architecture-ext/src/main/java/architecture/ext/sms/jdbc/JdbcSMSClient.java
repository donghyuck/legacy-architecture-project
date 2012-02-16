package architecture.ext.sms.jdbc;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import architecture.ext.sms.client.SMSClient;
import architecture.ext.sms.client.SMSException;

public class JdbcSMSClient extends JdbcDaoSupport implements SMSClient  {

	
	private String queryString;
	
	public void openSession() throws SMSException {
		
	}

	public void send(String senderNumber, String receiverNumber, String contents) throws SMSException {
		
	}

	public void closeSession() throws SMSException {
		
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

}

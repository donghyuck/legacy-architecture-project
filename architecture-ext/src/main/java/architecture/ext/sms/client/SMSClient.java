package architecture.ext.sms.client;

public interface SMSClient {

	public void openSession() throws SMSException;
	
	public void send(String senderNumber, String receiverNumber, String contents) throws SMSException ;
	
	public void closeSession() throws SMSException;
	
}

package architecture.ext.sms.x3;

import org.springframework.beans.factory.FactoryBean;

import architecture.ext.sms.client.SMSClient;

/**
 * @author  donghyuck
 */
public class X3SMSClientFactoryBean implements FactoryBean {

	/**
	 * @uml.property  name="singleton"
	 */
	private boolean singleton = false;
	/**
	 * @uml.property  name="connectionString"
	 */
	private String connectionString = "sme://000.000.000.131:20000";
	/**
	 * @uml.property  name="username"
	 */
	private String username = "";
	/**
	 * @uml.property  name="password"
	 */
	private String password = "";
	/**
	 * @uml.property  name="receiverCreated"
	 */
	private boolean receiverCreated = true;
	/**
	 * @uml.property  name="deliveryReport"
	 */
	private boolean deliveryReport = false;
	
	
	public SMSClient getObject() throws Exception {
		
		X3SMSClient client = new X3SMSClient();
		client.setConnectionString(getConnectionString());
		client.setDeliveryReport(isDeliveryReport());
		client.setReceiverCreated(isReceiverCreated());
		client.setUsername(getUsername());
		client.setPassword(getPassword());
		
		return client;
	}

	public Class getObjectType() {
		return SMSClient.class;
	}

	/**
	 * @return
	 * @uml.property  name="singleton"
	 */
	public boolean isSingleton() {
		return singleton;
	}

	/**
	 * @param singleton
	 * @uml.property  name="singleton"
	 */
	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	/**
	 * @return
	 * @uml.property  name="connectionString"
	 */
	public String getConnectionString() {
		return connectionString;
	}

	public X3SMSClientFactoryBean() {
		super();
	}

	/**
	 * @param connectionString
	 * @uml.property  name="connectionString"
	 */
	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	/**
	 * @return
	 * @uml.property  name="username"
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 * @uml.property  name="username"
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return
	 * @uml.property  name="password"
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 * @uml.property  name="password"
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return
	 * @uml.property  name="receiverCreated"
	 */
	public boolean isReceiverCreated() {
		return receiverCreated;
	}

	/**
	 * @param receiverCreated
	 * @uml.property  name="receiverCreated"
	 */
	public void setReceiverCreated(boolean receiverCreated) {
		this.receiverCreated = receiverCreated;
	}

	/**
	 * @return
	 * @uml.property  name="deliveryReport"
	 */
	public boolean isDeliveryReport() {
		return deliveryReport;
	}

	/**
	 * @param deliveryReport
	 * @uml.property  name="deliveryReport"
	 */
	public void setDeliveryReport(boolean deliveryReport) {
		this.deliveryReport = deliveryReport;
	}
	
}

package architecture.ext.sms.x3;

import org.springframework.beans.factory.FactoryBean;

import architecture.ext.sms.client.SMSClient;

public class X3SMSClientFactoryBean implements FactoryBean {

	private boolean singleton = false;
	private String connectionString = "sme://000.000.000.131:20000";
	private String username = "";
	private String password = "";
	private boolean receiverCreated = true;
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

	public boolean isSingleton() {
		return singleton;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	public String getConnectionString() {
		return connectionString;
	}

	public X3SMSClientFactoryBean() {
		super();
	}

	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isReceiverCreated() {
		return receiverCreated;
	}

	public void setReceiverCreated(boolean receiverCreated) {
		this.receiverCreated = receiverCreated;
	}

	public boolean isDeliveryReport() {
		return deliveryReport;
	}

	public void setDeliveryReport(boolean deliveryReport) {
		this.deliveryReport = deliveryReport;
	}
	
}

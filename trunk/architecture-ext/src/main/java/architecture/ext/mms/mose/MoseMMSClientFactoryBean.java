package architecture.ext.mms.mose;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;

import architecture.ext.mms.client.MMSClient;

import com.dkitec.mose.client.MoseClient;

public class MoseMMSClientFactoryBean implements FactoryBean {

	private Log log = LogFactory.getLog(getClass());
	
	private boolean singleton = false;
	
	//Client 수정불가
	private String soapUrl = "/";                //수정금지

	public MoseMMSClientFactoryBean() {
		super();
	}

	//Client 수정 (아이피 ,아이디,패스워드)
	private int timeOut = 10000 ;                // 10 second  - 연결시도 2초후 연결되지 않으면 타임아웃
	private int port = 13000;                    // TEST Dummy 14000
	private String host = "000.000.000.000";
	private String username = "";			     // ID
	private String password = "";		         // Password
	
	public MMSClient getObject() throws Exception {		
		MoseClient client = MoseClient.createMoseClient(getHost(), getPort(), getSoapUrl(), getUsername(), getPassword());	
		MoseMMSClient obj = new MoseMMSClient(client);
		obj.setTimeOut(getTimeOut());
		return obj;
	}

	public Class getObjectType() {
		return MMSClient.class;
	}

	public boolean isSingleton() {
		return singleton;
	}
	
	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	public String getSoapUrl() {
		return soapUrl;
	}

	public void setSoapUrl(String soapUrl) {
		this.soapUrl = soapUrl;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
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
	
}

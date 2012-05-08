package architecture.ext.mms.mose;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;

import architecture.ext.mms.client.MMSClient;

import com.dkitec.mose.client.MoseClient;

/**
 * @author  donghyuck
 */
public class MoseMMSClientFactoryBean implements FactoryBean {

	private Log log = LogFactory.getLog(getClass());
	
	/**
	 * @uml.property  name="singleton"
	 */
	private boolean singleton = false;
	
	//Client 수정불가
	/**
	 * @uml.property  name="soapUrl"
	 */
	private String soapUrl = "/";                //수정금지

	public MoseMMSClientFactoryBean() {
		super();
	}

	//Client 수정 (아이피 ,아이디,패스워드)
	/**
	 * @uml.property  name="timeOut"
	 */
	private int timeOut = 10000 ;                // 10 second  - 연결시도 2초후 연결되지 않으면 타임아웃
	/**
	 * @uml.property  name="port"
	 */
	private int port = 13000;                    // TEST Dummy 14000
	/**
	 * @uml.property  name="host"
	 */
	private String host = "000.000.000.000";
	/**
	 * @uml.property  name="username"
	 */
	private String username = "";			     // ID
	/**
	 * @uml.property  name="password"
	 */
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
	 * @uml.property  name="soapUrl"
	 */
	public String getSoapUrl() {
		return soapUrl;
	}

	/**
	 * @param soapUrl
	 * @uml.property  name="soapUrl"
	 */
	public void setSoapUrl(String soapUrl) {
		this.soapUrl = soapUrl;
	}

	/**
	 * @return
	 * @uml.property  name="timeOut"
	 */
	public int getTimeOut() {
		return timeOut;
	}

	/**
	 * @param timeOut
	 * @uml.property  name="timeOut"
	 */
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	/**
	 * @return
	 * @uml.property  name="port"
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port
	 * @uml.property  name="port"
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return
	 * @uml.property  name="host"
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 * @uml.property  name="host"
	 */
	public void setHost(String host) {
		this.host = host;
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
	
}

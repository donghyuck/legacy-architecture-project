package architecture.ext.sms.x3;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import x3.client.smeapi.SMEConnection;
import x3.client.smeapi.SMEConnectionFactory;
import x3.client.smeapi.SMEException;
import x3.client.smeapi.SMEListener;
import x3.client.smeapi.SMEReceiver;
import x3.client.smeapi.SMEReport;
import x3.client.smeapi.SMERequest;
import x3.client.smeapi.SMEResponse;
import x3.client.smeapi.SMESender;
import x3.client.smeapi.SMESession;
import x3.client.smeapi.impl.SMEConnectionFactoryImpl;
import architecture.ext.sms.client.SMSClient;
import architecture.ext.sms.client.SMSException;


public class X3SMSClient implements SMSClient, SMEListener {

	private Log log = LogFactory.getLog(getClass());
	
	private String connectionString = "sme://000.000.000.131:20000"; // //sme://서버주소:서버포트";
	private String username ;
	private String password ;
	private boolean receiverCreated = true;
	private int count = 1;
	private boolean deliveryReport = false;
	
	
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
	DecimalFormat numberFormat = new DecimalFormat("0000");
	
	private SMEConnectionFactory factory = null;
	
	
	private SMEConnection connection = null;
	private SMESession session = null;
	private SMESender sender = null;
	private SMEReceiver receiver = null;
	
	public String getConnectionString() {
		return connectionString;
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

	protected SMEConnectionFactory getSMEConnectionFactory() throws SMEException{
		if( factory == null ){
			factory = new SMEConnectionFactoryImpl(connectionString);		
		}
		return factory;
	}
	
	protected SMEConnection getSMEConnection() throws SMEException{
		if(connection == null)
			connection = getSMEConnectionFactory().createConnection(username, password);		
		return connection;
	}
	
	protected SMESession getSMESession() throws SMEException{
	    if( session == null ){
	    	
	    	session = getSMEConnection().createSession();	
	    	session.setReceiverCreated(receiverCreated);
	    
	    }
	    return session;
	}
	
	protected SMESender getSMESender() throws SMEException {		
		if( sender == null )
			sender = getSMESession().createSender();		
		if( sender != null && !sender.isConnected())
			sender = getSMESession().createSender();
		return sender;
	}	

	protected SMERequest newSMERequest() throws SMEException {		
		return getSMESession().createRequest();
	}
	
	public void openSession() throws SMSException {		
		try {
			session = getSMESession();
			sender = session.createSender();
			if(deliveryReport){
				receiver = session.createReceiver();
				receiver.setListener(this);
			}
			getSMEConnection().start();
		} catch (SMEException e) {
			throw new SMSException(e);
		}		
	}
	
	public void closeSession() {
		try {
            if (sender != null)
                sender.close();
            
            if (receiver != null)
            	receiver.close();
        } catch (SMEException e) {
            log.error(e);
        }

        try {
            if (session != null)
            	session.close();
        } catch (SMEException e) {
        	log.error(e);
        }

        try {
            if (connection != null)
            	connection.close();
        } catch (SMEException e) {
        	log.error(e);
        }
	}
	
	/**
	 * 
	 * @param senderNumber
	 * @param receiverName
	 * @param receiverNumber
	 * @param title
	 * @param contents 84 bite below..
	 */
	public void send(String senderNumber, String receiverNumber, String contents) throws SMSException {
		SMERequest request = null;		
		try {
			request = newSMERequest();
			request.setMessageId(getSeqId());
			request.setTo(receiverNumber);  //수신번호  // destination
			request.setFrom(senderNumber);
			request.setText(contents);
			
			SMEResponse res = getSMESender().send(request);
            if( res.getResult() != 0 ){
            	log.error("error = " + res.getResult() + ":" + res.toString());
            	throw new SMEException("ERROR:" + res.getResult() );
            }
		} catch (SMEException e) {
			throw new SMSException(e);
		}
	}
	
	private int getNextNo(){
		count = count + 1 ;
		return count;
	}
	
	public String getSeqId(){
		Date date = new Date(System.currentTimeMillis());		
		StringBuffer sf = new StringBuffer();
		sf.append( dateFormat.format(date) );		
		sf.append( numberFormat.format(getNextNo() )); 		
		return sf.toString();
	}

	public void onMessage(SMEReport report) {
		log.info(report.toString());
	}

	public void setReceiverCreated(boolean receiverCreated) {
		this.receiverCreated = receiverCreated;
	}

	public void setDeliveryReport(boolean deliveryReport) {
		this.deliveryReport = deliveryReport;
	}
	
	
}
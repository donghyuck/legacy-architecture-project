package architecture.ext.mms.mose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dkitec.mose.client.MessageStatus;
import com.dkitec.mose.client.MoseClient;

public class MoseMMSResultReceiver {

	private Log log = LogFactory.getLog(getClass());
	private MoseClient client = null;
	private int timeOut = 10000 ; 
	private int port = 14000;                    // TEST Dummy 14000
	private String host = "10.181.24.133";
	private String username = "";			     // ID
	private String password = "";		         // Password
	private String soapUrl = "/";                //수정금지
	
	
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

	public String getSoapUrl() {
		return soapUrl;
	}

	public void setSoapUrl(String soapUrl) {
		this.soapUrl = soapUrl;
	}

	public MoseMMSResultReceiver() {		
	}
	
	public MoseMMSResultReceiver(String host, int port, String soapUrl,String username,String password ) {
		this.client = MoseClient.createMoseClient(host, port, soapUrl, username, password);	
	}
	
	public MoseMMSResultReceiver(MoseClient client) {
		this.client = client;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}
	
	private List<String> messages = Collections.synchronizedList(new ArrayList<String>());
	
	public void addMessingId(String msgId){
		messages.add(msgId);
	}
	
	public void receive (){		
	    if(client != null){
			for(String msgId : messages){
				MessageStatus[] msgList = client.requestResult(msgId, timeOut);
				for(int i = 0; i < msgList.length; i++) {
						String Mainstatus	= msgList[i].getStatus();		// 메인 상태  :  전송중 또는 성공
						String telco		= msgList[i].getTelco();		// 이동통신사 정보
						String receiveNo	= msgList[i].getPhoneNo();		//수신자 번호
						String receiveName	= msgList[i].getRecipient();	// 수신자 번호
						String deliveryDate = msgList[i].getDeliveryDate(); // 이통사 결과수신시간
						/*******
						* 이동통신사 상태는 Mainstatus 가  성공 실패 등을 좌우 하는 조건임
						* Mainstatus 가 "실패" 이어도  SKTStatus,KTFStatus,LGTStatus 중
						* 하나이상  "성공"이면  메시지 전송에 성공한 상태.
						* 전송결과를  최종으로 업데이트 하기전에는 종료 되지 않은 상태임 '전송중'
						* 실패 처리 =============================================================
						* '전송중' 상태로 만 3일이 지나면 '강제종료' 처리됨
						* '강제종료' = 이통사로부터 dr을 수신하지 못한 메세지
						*
						*/
						String SKTStatus = msgList[i].getSktStatus();		// SKT 이통사 상태
						String KTFStatus = msgList[i].getKtfStatus();		// KTF 이통사 상태
						String LGTStatus = msgList[i].getLgtStatus();		// LGT 이통사 상태						
						log.info("Mainstatus="+Mainstatus+", telco="+telco+",receiveNo= "+receiveNo+",="+receiveName+",deliveryDate=" +deliveryDate+",SKTStatus="+SKTStatus+",KTFStatus="+KTFStatus+",LGTStatus="+LGTStatus);
				}				
				messages.remove(msgId);
			}
	    }		
	}
	
}

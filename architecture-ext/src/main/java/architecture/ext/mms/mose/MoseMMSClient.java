package architecture.ext.mms.mose;

import java.text.ParseException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.ext.mms.client.MMSClient;
import architecture.ext.mms.client.MMSException;
import architecture.ext.sms.client.SMSException;

import com.dkitec.mose.client.MessageStatus;
import com.dkitec.mose.client.MoseClient;
import com.dkitec.mose.client.SoapFault;
import com.dkitec.mose.client.SubmitRequest;
import com.dkitec.mose.client.SubmitResponse;

/**
 * @author  donghyuck
 */
public class MoseMMSClient implements MMSClient {
	
	private Log log = LogFactory.getLog(getClass());
	private MoseClient client = null;
	/**
	 * @uml.property  name="timeOut"
	 */
	private int timeOut = 10000 ; 
	/**
	 * @uml.property  name="port"
	 */
	private int port = 14000;                    // TEST Dummy 14000
	/**
	 * @uml.property  name="host"
	 */
	private String host = "10.181.24.133";
	/**
	 * @uml.property  name="username"
	 */
	private String username = "";			     // ID
	/**
	 * @uml.property  name="password"
	 */
	private String password = "";		         // Password
	/**
	 * @uml.property  name="soapUrl"
	 */
	private String soapUrl = "/";                //수정금지
	/**
	 * @uml.property  name="receiver"
	 * @uml.associationEnd  
	 */
	private MoseMMSResultReceiver receiver = null ;
	
	
	
	
	/**
	 * @param reveiver
	 * @uml.property  name="receiver"
	 */
	public void setReceiver(MoseMMSResultReceiver reveiver) {
		this.receiver = reveiver;
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

	public MoseMMSClient() {		
	}
	
	public MoseMMSClient(String host, int port, String soapUrl,String username,String password ) {
		this.client = MoseClient.createMoseClient(host, port, soapUrl, username, password);	
	}
	
	public MoseMMSClient(MoseClient client) {
		this.client = client;
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

	public void send(String senderNumber,  String[] receiverNames, String[] receiverNumbers, String subject, String contents, String date) throws SMSException {
		
		if(client == null)
			this.client = MoseClient.createMoseClient(host, port, soapUrl, username, password);	
		
		
		System.out.println(client.getHost() + ", " + client.getPort() + ", "+ client.getUrl() + ", "+ client);
		
		try {
			/**
			 * 생성된 Client를 통해 전송 객체(SubmitRequest)를 생성한다.
			 */
			SubmitRequest request = MoseClient.createSubmitRequest();
			
			request.setSenderPhoneNo(senderNumber);  // 발신자 번호
			/**********************
			* 수신번호는 반드시 휴대전화번호만 입력
			* 현재 센터(기업형) MMS G/W 는 일반번호(인터넷전화 포함)에 대한
			* 메시지 전송을 지원안함.
			* 동시 수신 설정 최대 50명 , 권장 25명
			*/
			
			for( int i = 0 ; i < receiverNumbers.length ; i ++ ){
			    String phone = receiverNumbers[i];
			    String name  = 	receiverNames[i];
			    request.setReceiverList(name, phone);
			}
			
			/***************
			* 제목 최대 자리수  정보
			* SKT : 30 Byte (한글+영문조합 15자)
			* KTF : 30 Byte (한글 15자)
			* LGT : 30 Byte (한글 15자)
			*
			*/
			request.setSubject(subject);  // 전송 메시지 제목
			
			/**********************
			* 메세지 내용 언어지원  :  한글 , 영문 ,기타
			* 국내 휴대전화에서 표현되는 문자 지원  한문 지원 안함
			* 표현 자리수  : 1000 Byte 지원
			* 예 ) 알파벳 숫자 조합(각 1 Byte)  :  전체 1000 자
			* 예 ) 한글( 2 Byte)  :  500 자
			*/
			request.setMsgText(contents);  // 전송 메시지내용
			
			if(StringUtils.isEmpty(date)){
			
			}else{
				request.setSendingDate(date);
			}

			System.out.println("Request = " + request );
			
			SubmitResponse response = client.send(request, getTimeOut());
			
			if(response.isSuccess()) { // 전송요청 성공 여부
				String messageID = response.getMessageId();  // 전송요청한 메시지 ID (전송 결과 요청시  필요)
				String ststus = response.getStatus();  //  전송요청 상태 정보(200일 경우 성공)
				log.info( "MMS[ ID=" + messageID + ", status=" + ststus + "] send success.");
				
				if(receiver!=null)
				    receiver.addMessingId(messageID);				
				
			}else{				
				SoapFault soapFault = response.getSoapFault();  //  전송 실패 정보
				String faultCode = soapFault.getFaultCode();  //  전송 실패 코드
				String faultString = soapFault.getFaultString();  // 전송 실패 설명 정보
				String faultActor = soapFault.getFaultActor();  //  전송 실패 액터 정보			
				throw new MMSException("Code:" + faultCode + " - " + faultString );				
			}
		} catch (IllegalArgumentException e) {
			throw new MMSException(e);
		} catch (ParseException e) {
			throw new MMSException("잘못된 시간 형식입니다.");
		}
	}

	public void send(String senderNumber, String receiverName,
			String receiverNumber, String subject, String contents)
			throws SMSException {
		
		send(senderNumber, receiverName, receiverNumber, subject, contents, null);		
	
	}

	public void send(String senderNumber, String receiverName,
			String receiverNumber, String subject, String contents, String date)
			throws SMSException {
		send(senderNumber, new String[]{receiverName}, new String[]{receiverNumber}, subject, contents, date);
	}

	public void send(String senderNumber, List<String> receiverNames,
			List<String> receiverNumbers, String subject, String contents)
			throws SMSException {
		String[] n = new String [receiverNames.size()]; 
		receiverNames.toArray(n);
		String[] p = new String [receiverNumbers.size()]; 
		receiverNumbers.toArray(p);
		
		send(senderNumber, n, p, subject, contents);
		
	}

	public void send(String senderNumber, String[] receiverNames,
			String[] receiverNumbers, String subject, String contents)
			throws SMSException {
		send(senderNumber, receiverNames, receiverNumbers, subject, contents, null);
		
	}

	public void send(String senderNumber, List<String> receiverNames, List<String> receiverNumbers, String subject, String contents, String date) throws SMSException {
		
		String[] n = new String [receiverNames.size()]; 
		receiverNames.toArray(n);
		String[] p = new String [receiverNumbers.size()]; 
		receiverNumbers.toArray(p);
		
		if(MoseMMSServiceHelper.isGetMMSResultEabled())
			send(senderNumber, n, p, subject, contents, date);
	}
	
	public void result(String msgId){
		
		MessageStatus[] msgList = client.requestResult(msgId, timeOut);
		{
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
		}
		
		//MoseMMSServiceHelper.messages.remove(msgId);
		
	}
	
}

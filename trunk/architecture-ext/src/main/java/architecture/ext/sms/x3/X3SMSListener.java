package architecture.ext.sms.x3;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import x3.client.smeapi.SMEConnection;
import x3.client.smeapi.SMEConnectionFactory;
import x3.client.smeapi.SMEException;
import x3.client.smeapi.SMEListener;
import x3.client.smeapi.SMEReceiver;
import x3.client.smeapi.SMEReport;
import x3.client.smeapi.SMESession;
import x3.client.smeapi.impl.SMEConnectionFactoryImpl;

/**
 * @author  donghyuck
 */
public class X3SMSListener implements SMEListener {

	private Log log = LogFactory.getLog(getClass());
	
	private SMEConnectionFactory factReceiver = null;
	private SMEConnection connReceiver = null;
	private SMESession sessReceiver = null;

	private SMEReceiver receiver = null;
	private boolean isConnected = false;
    
	/**
	 * @uml.property  name="connectionString"
	 */
	private String connectionString = "sme://000.000.000.131:20000"; // //sme://서버주소:서버포트";
	/**
	 * @uml.property  name="username"
	 */
	private String username ;
	/**
	 * @uml.property  name="password"
	 */
	private String password ;
	/**
	 * @uml.property  name="pingterm"
	 */
	private int pingterm = 60;  // 세션연결 유지 시간 최대값
		
	public X3SMSListener() {
	}

	/**
	 * @return
	 * @uml.property  name="connectionString"
	 */
	public String getConnectionString() {
		return connectionString;
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
	 * @uml.property  name="pingterm"
	 */
	public int getPingterm() {
		return pingterm;
	}

	/**
	 * @param pingterm
	 * @uml.property  name="pingterm"
	 */
	public void setPingterm(int pingterm) {
		this.pingterm = pingterm;
	}

	public void initialize() throws SMEException {
        this.factReceiver = new SMEConnectionFactoryImpl(connectionString);
        this.connReceiver = factReceiver.createConnection(username, password); // 아이디와 패스워드입니다.
        this.sessReceiver = connReceiver.createSession();
        this.receiver = sessReceiver.createReceiver();
        this.receiver.setPingTerm(pingterm);
        this.receiver.setListener(this);
        this.connReceiver.start();
        isConnected = true;
    }
    
	public void distory() {
        try {
            if (receiver != null)
                receiver.close();
        } catch (SMEException e) {
            log.error(e);
        }

        try {
            if (sessReceiver != null)
                sessReceiver.close();
        } catch (SMEException e) {
        	 log.error(e);
        }

        try {
            if (connReceiver != null)
                connReceiver.close();
        } catch (SMEException e) {
        	 log.error(e);
        }
    }
	
	public void onMessage(SMEReport report) {
		if (report instanceof SMEReport)
        {
            if (report.isConnected())
            {
                SMEReport rpt = (SMEReport) report;
                //================================================================================//
                // 메세지 처리과정 순서 (출발지 : 기관  ,  목적지: 센터SMS G/W )
                //1.기관   : 메세지 전송요청        -> 센터SMS: 번호 및 메세지 형식확인
                //2.센터SMS: 이통사로 전달          -> 이통사 : 번호 및 메세지 형식확인
                //3.이통사 : 폰에 메세지 전송      <-> Phone  :실제수신전화 에서 응답
                //4.이통사 : 발신결과 값 전송       -> 센터SMS: 이통사 처리결과 값 수신 처리
                //5.센터SMS: 기관에 발신결과 전송   -> 기관   : 최종 전송 결과 수신 처리
                //6.메세지 처리 종료
			    //================================================================================//
                // 코드 :  '0' = 성공 , 이외의 코드 오류 코드 참조
				// Rsvs는 기본값을  : '0' 으로설정 할 수 없슴
                String  msgId       = rpt.getMessageId();
                int     Rsv         = rpt.getResult();    //결과코드
                String  doneTime    = rpt.getDeliverTime(); //이동통신사 결과처리시간-단말기에 전달된 시간(이동통신사 생성)
                String  netCode     = rpt.getDestination(); //이동통신사 정보
                //System.out.println(rpt);
                //System.out.println("Receiver Number is :" + ((SMEReportImpl)rpt).receiver.activeCount()); // 주석처리
                //if (Rsv != SMEMessage.RESULT_SUCCESS){실패} else{성공}
                    //System.out.println("SMSMessage (msgId = " + msgId + ") report = " + rpt.getResult());

                    System.out.println("MessageId   : "+ msgId      );
                    System.out.println("Result      : "+ Rsv        );
                    System.out.println("Done Time   : "+ doneTime   );
                    System.out.println("Net Code    : "+ netCode    );
                    System.out.println("===========================");
                    
                    StringBuffer sf = new StringBuffer();
                    sf.append(" SMS Report [MessageId   : "+ msgId      );
                    sf.append(",Result      : "+ Rsv        );
                    sf.append(",Done Time   : "+ doneTime   );
                    sf.append(",Net Code    : "+ netCode    );
                    sf.append(" ] ");
                    log.info(sf.toString());
            }
            else
            {
                //System.out.println("SMEReceiver Disconnected!!"); // 주석처리
            	log.error("SMEReceiver Disconnected!!");
                isConnected = false;
            }
        }		
	}

}

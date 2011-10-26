package tests;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import architecture.ext.mms.client.MMSClient;
import architecture.ext.sms.client.SMSClient;


public class SMSTest {

	
	public static void main(String[] args){
		SMSTest test = new SMSTest();
		test.sendMessage();
	}
	
	private Log log = LogFactory.getLog(getClass());
	private String location = "webApplicaitonContext.xml";
		
	public ApplicationContext getApplicationContext(){		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(location);
		return context;
	}
	
	@Test
	public void day(){}

	
	public void sendMessage(){
		log.debug("=================");
		
		SMSClient client = (SMSClient)getApplicationContext().getBean("smsClient");
				
		log.debug("=================111");
		try{
			client.openSession();
			log.debug("=================1111");
			client.send("01090262795", "01090262795", "문자전송테스트입니다.");
			
		}catch(Exception e){
			log.error(e);
		}finally{
			client.closeSession();
		}	
		
		MMSClient client2 = (MMSClient) getApplicationContext().getBean("mmsClient");
		client2.send("01090262795", "홍길순", "01090262795", "문자 전송 테스트", "문자전송테스트입니다.");
		
		// SENDER_PHONE RECEIVER_NAME RECEIVER_PHONE MSG_SUBJECT, MSG_BODY
		
	}
}

package tests;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import architecture.ext.sms.client.SMSClient;

public class SMSTest {

	public static void main(String[] args){
		SMSTest test = new SMSTest();
		test.sendMessage();
	}
	
	private Log log = LogFactory.getLog(getClass());
	private String location = "smsSubsystemContext.xml";
		
	public ApplicationContext getApplicationContext(){		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(location);
		return context;
	}
	
	@Test
	public void day(){}

	@Test
	public void sendMessage(){
		log.debug("=================");
		
		SMSClient client = (SMSClient)getApplicationContext().getBean("smsClient");
				
		log.debug("=================111");
		try{
			client.openSession();
			log.debug("=================1111");
			client.send("01090262795", "01043134332", "남부발전에서 알려드립니다. 빨리오삼.");
			//client.send("01090262795", "01090262795", "문자전송테스트입니다.");
			log.debug("=================2222");
		}catch(Exception e){
			log.error(e);
			e.printStackTrace();
		}finally{
			client.closeSession();
		}			
		// SENDER_PHONE RECEIVER_NAME RECEIVER_PHONE MSG_SUBJECT, MSG_BODY
		
	}
}

package tests;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import architecture.ext.sms.x3.X3SMSClient;


public class SMSTest {

	private Log log = LogFactory.getLog(getClass());
	
		
	@Test
	public void sendMessage(){
		log.debug("=================");
		X3SMSClient client = new X3SMSClient();
		
		client.setUsername("forest001");
		client.setPassword("forest481");
		log.debug("=================111");
		try{
			client.openSession();
			log.debug("=================1111");
			client.send("01090262795", "01090262795", "문자전송테스트입니다.");
			
		}catch(Exception e){
			log.error(e);
			e.printStackTrace();
		}finally{
			client.closeSession();
		}	
		
	}
}

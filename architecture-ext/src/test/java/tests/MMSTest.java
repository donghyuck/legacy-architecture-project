package tests;

import org.junit.Test;

import architecture.ext.mms.client.MMSClient;

import com.caucho.hessian.client.HessianProxyFactory;


public class MMSTest {
	@Test
	public void day(){}
	public static void main(String[] args){
		
try {			
	
	        HessianProxyFactory factory = new HessianProxyFactory();
	        MMSClient client = (MMSClient)factory.create(MMSClient.class, "http://localhost:13000/remoting/mms_client");
	        client.send("01090262795", "홍길순", "01090262795", "문자 전송 테스트", "문자전송테스트입니다.");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}



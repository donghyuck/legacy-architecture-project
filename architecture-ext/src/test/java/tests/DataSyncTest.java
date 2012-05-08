package tests;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import architecture.ext.sync.connector.ftp.FTPDataSyncClient;

public class DataSyncTest {

	public static void main(String[] args){
		FTPDataSyncClient client = (FTPDataSyncClient)getApplicationContext().getBean("dataSyncClient");
	}

	@Test
	public void day(){
		
		
		
	}
	
	private static Log log = LogFactory.getLog(DataSyncTest.class);
	
	private static String location = "syncSubsystemContext.xml";
		
	private static ClassPathXmlApplicationContext context = null;
	
	public static ApplicationContext getApplicationContext(){		
		if(context == null)
		  context = new ClassPathXmlApplicationContext(location);
		return context;
	}
	
}

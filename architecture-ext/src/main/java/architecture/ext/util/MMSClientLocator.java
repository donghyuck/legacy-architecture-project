package architecture.ext.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dkitec.mose.client.MoseClient;

import architecture.ext.mms.client.MMSClient;
import architecture.ext.mms.mose.MoseMMSClient;

public class MMSClientLocator {
	
	private ClassPathXmlApplicationContext context = null;
	private String contextConfigLocation = "mmsSubsystemContext.xml";
	private String libPath ;
	private String mmsClientName = "mmsClient"; 
	private MoseMMSClient mmsClient = null ;
	
	public void setContextConfigLocation(String contextConfigLocation) {
		this.contextConfigLocation = contextConfigLocation;
	}
	
	public void setLibPath(String libPath) {
		this.libPath = libPath;
	}

	protected ApplicationContext getApplicationContext(){
		
		if( context == null ){
			context = new ClassPathXmlApplicationContext(contextConfigLocation);			
			context.refresh();
		}
		return context;
	}
	
	public MMSClient getMMSClient(){
		
		if(mmsClient == null){
			mmsClient = new architecture.ext.mms.mose.MoseMMSClient(
				MoseClient.createMoseClient(			    
						"10.183.192.12",
					    13000,
					    "/",
					    "forest001",
					    "forest481")
			);
			
			mmsClient.setTimeOut(10000);
		}
		return mmsClient;
		//return (MMSClient) getApplicationContext().getBean(mmsClientName, MMSClient.class);
	}
	
}

package architecture.ext.mms.mose.impl;
import com.dkitec.mose.client.MoseClient;

import architecture.ext.mms.mose.*; 

public class MosMMSClientFactoryImpl implements MosMMSClientFactory.Implementation {
	
	public MoseMMSClient getMosMMSClient(String host, int port, String soapUrl, String username, String password ) {
		
		return new MoseMMSClient(
				MoseClient.createMoseClient( host, port, soapUrl, username, password) 
			);	
	}

	public MoseMMSClient getMosMMSClient(com.dkitec.mose.client.MoseClient moseClient) {		
		return new MoseMMSClient(moseClient);	
	}

}

package architecture.ext.mms.mose;

import architecture.ext.mms.mose.impl.MosMMSClientFactoryImpl;

import com.dkitec.mose.client.MoseClient;


/**
 * @author  donghyuck
 */
public class MosMMSClientFactory {

	public static interface Implementation {
				
		public abstract MoseMMSClient getMosMMSClient( String host, int port, String soapUrl, String username, String password);
		public abstract MoseMMSClient getMosMMSClient( com.dkitec.mose.client.MoseClient moseClient );
		
	}

	/**
	 * @uml.property  name="impl"
	 * @uml.associationEnd  
	 */
	private static Implementation impl = null;

	static {
		impl = (Implementation) new MosMMSClientFactoryImpl();
	}
	
	public static MoseMMSClient getMosMMSClient(MoseClient moseClient){
	    return impl.getMosMMSClient(moseClient);	
	}
	
	public static MoseMMSClient getMosMMSClient( String host, int port, String soapUrl, String username, String password){
		MoseClient moseClient = MoseClient.createMoseClient( host, port, soapUrl, username, password) ;
		return impl.getMosMMSClient(moseClient);
	}
	
}

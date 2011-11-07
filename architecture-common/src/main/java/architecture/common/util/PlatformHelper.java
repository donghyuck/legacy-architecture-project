package architecture.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PlatformHelper {

	public static void main(String [] args ){
		
		showOS();
	}
	
	public static void showOS(){
	    
		Runtime runtime = Runtime.getRuntime();
	    try {
	    	
			Process proc = runtime.exec("systeminfo /FO CSV");		
			
			//System.out.println(System.getProperty("file.encoding"));
			
			InputStream stdOut = proc.getInputStream();
			InputStream stdErr = proc.getErrorStream();
			
			BufferedReader stdReader = new BufferedReader(new InputStreamReader(stdOut, "MS949"));
			BufferedReader errReader = new BufferedReader(new InputStreamReader(stdErr, "UTF-8"));
			
			StringBuffer stdout = new StringBuffer();
			StringBuffer errout = new StringBuffer();
			
			String line = null ;
			
			while( (line = stdReader.readLine()) != null ){
				stdout.append(line).append(PlatformConstants.EOL);				
			}
			line = null ;
			while( (line = errReader.readLine()) != null ){
				errout.append(line).append(PlatformConstants.EOL);				
			}			
			
			stdOut.close();
			stdErr.close();			
			
			System.out.println(stdout.toString());
			System.out.println(errout.toString());
			
		} catch (IOException e) {

		}
		
	}
	
}

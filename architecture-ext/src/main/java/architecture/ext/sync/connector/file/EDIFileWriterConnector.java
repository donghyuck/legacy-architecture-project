package architecture.ext.sync.connector.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.adaptor.Context;
import architecture.common.adaptor.WriteConnector;
import architecture.common.jdbc.ParameterMapping;

public class EDIFileWriterConnector implements WriteConnector {

	private Log log = LogFactory.getLog(getClass());
	
	private String delim = "," ;
	
	private String location;
	
	private long deliveringIntervalMillis = 1000;
	
	private String deliveringFileLocation ;

    private String tarCommand ;

	public String getTarCommand() {
		return tarCommand;
	}

	public void setTarCommand(String tarCommand) {
		this.tarCommand = tarCommand;
	}
	private String deliveringFileCommand ;
	
	private boolean usingCompress = true;
	
	private boolean deliveringFile = true; 
	
	private AtomicInteger nextId = new AtomicInteger(1) ;
	
	private DateFormat df = new SimpleDateFormat("yyyyMMddHHmmSS");
	
	private DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
	
	private String charset = "UTF-8";
		
	public String getDeliveringFileLocation() {
		return deliveringFileLocation;
	}

	public void setDeliveringFileLocation(String deliveringFileLocation) {
		this.deliveringFileLocation = deliveringFileLocation;
	}

	public boolean isDeliveringFile() {
		return deliveringFile;
	}

	public void setDeliveringFile(boolean deliveringFile) {
		this.deliveringFile = deliveringFile;
	}
	
	public void setDeliveringIntervalMillis(long deliveringIntervalMillis) {
		this.deliveringIntervalMillis = deliveringIntervalMillis;
	}
	public String getDeliveringFileCommand() {
		return deliveringFileCommand;
	}

	public void setDeliveringFileCommand(String deliveringFileCommand) {
		this.deliveringFileCommand = deliveringFileCommand;
	}

	public boolean isUsingCompress() {
		return usingCompress;
	}

	public void setUsingCompress(boolean usingCompress) {
		this.usingCompress = usingCompress;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getFileSavePath() {
		return location;
	}
		
	public void setFileSavePath(String location) {
		this.location = location;
	}
	
	protected int getNextId() {
		return nextId.getAndIncrement();
	}

	protected File getFile(Properties properties){
		// file name : 송신기관코드() + 수신기관코드 + EDI 업무구분 코드 (2자리) + 파일구분코드(1자리) + 생성일시(yyyyMMddHH24MISS) + 일련번호 
		File root = new File(location);		
		StringBuffer sf = new StringBuffer();
		sf.append(properties.getProperty("P1", ""));
		sf.append(properties.getProperty("P2", ""));
		sf.append(properties.getProperty("P3", ""));
		sf.append(properties.getProperty("P4", "S"));
		
		String dt = df.format(new Date());
		if( dt.length() > 13)
			dt = dt.substring(0, 14);
		
		sf.append(dt);
		sf.append(getNextId());		
		return new File(root, sf.toString());
	}

	public Object deliver(Context context) {
		
		
		
		// DOC CODE : MOLUQA
		// 파일에 저장할 데이터를 가져온다.
		Properties properties = context.getObject("properties", Properties.class);
		String docCode = properties.getProperty("DOC_CODE", "MOLUQA");
		
		List<Map<String, Object>> data = context.getObject("data", List.class);
		// DOC CODE : MOLUQA

		if( data == null || data.size() == 0)
			return 0;
		
		List<ParameterMapping> parameterMappings = context.getObject("parameterMappings", List.class);		
		
		Collections.sort(parameterMappings, new 
			Comparator<ParameterMapping>(){			
			// a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
				public int compare(ParameterMapping o1, ParameterMapping o2) {				
					if( o1.getIndex() == o2.getIndex() )
						return 0;
					else if ( o1.getIndex() > o2.getIndex() )
						return 1;
					else if ( o1.getIndex() < o2.getIndex() )
						return -1;					
					return 0;
				}});
		
		int columnSize = parameterMappings.size();		
		BufferedWriter out = null ;
		
		String saveFilePath = "";
		try {
			
			File file = getFile(properties);
			saveFilePath = file.getAbsolutePath();
			out = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(file), charset));
					
			for ( Map<String, Object> row : data ){		
				
				StringBuffer sb = new StringBuffer();			
				for( ParameterMapping mapping : parameterMappings ){
					int index = mapping.getIndex();
					Object value = row.get( mapping.getProperty() );
					Object valueToUse = value ;
					if( String.class == mapping.getJavaType()){
												
						if( value == null || "NULL".equals(value)){
							valueToUse = "";
						}
												
						String str = valueToUse.toString() ;
						str = str.replaceAll("[\r\n]", " ");						
						str = str.replaceAll("[,]", " ");
						str = StringUtils.replaceChars(str, ',', ' ');
						
						valueToUse = str;
						
						
					} else if( Long.class == mapping.getJavaType() ) {
						if( value == null || "NULL".equals(value)){
							valueToUse = "0";
						}
					} else if( Integer.class == mapping.getJavaType() ) {
						if( value == null || "NULL".equals(value)){
							valueToUse = "0";
						}
					}					
					
					sb.append(valueToUse);				
					if(index < columnSize )
					sb.append(delim);
				}
				out.write(sb.toString());
				out.newLine();
			}			
		} catch (IOException e) {
			return 0;
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {}
		}
				
		if(usingCompress){
			try {
				
				executeCommand("pwd");
				
				String command = "compress " + saveFilePath ;
		        executeCommand(command);
		        
		        File compressFile = new File(saveFilePath + ".Z");
		        
		        if( compressFile.exists()){

		        	String tarCommandString = MessageFormat.format(tarCommand, new Object[]{ compressFile.getName()});
		        	
		        	executeCommand( tarCommandString );		
		        	
		        	if(deliveringFile){
		        		
		        		if(deliveringIntervalMillis>0){
		        			log.debug("Wait before sending : " + deliveringIntervalMillis );
			        		try {
								Thread.sleep(deliveringIntervalMillis);
							} catch (InterruptedException ignore) {
							}
		        		}
		        		
		        		String deliveringFileCommandToUse = deliveringFileCommand;
		        		StringBuffer sf2 = new StringBuffer();
		        		sf2.append(df2.format(new Date()));
		        		sf2.append(docCode);
		        		sf2.append(nextId.get());
		        		deliveringFileCommandToUse = MessageFormat.format(deliveringFileCommand, new Object[]{docCode, sf2.toString()});
		        	    executeCommand( deliveringFileCommandToUse );
		        	    
		        	}
		        }
		        
			} catch (Exception e) {
				log.error(e);
			}
			
		}
		
		return data.size();
	}
	
	private void executeCommands(String[] commands){
		try {	
			for(String command : commands)
			log.debug( "execute : " + commands );			
			Process p = Runtime.getRuntime().exec(commands);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
	        String s = null;
	        
	     // read the output from the command
            log.debug("Here is the standard output of the command");
            while ((s = stdInput.readLine()) != null) {
            	log.debug(s);
            }
            
            log.debug("Here is the standard error of the command (if any)");
	        while ((s = stdError.readLine()) != null) {
                log.debug(s);
            }		
	        
		} catch (Exception e) {
			log.error(e);
		}
	}
	private void executeCommand(String command){
		try {			
			log.debug( "execute : " + command );
			
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
	        String s = null;
	        
	     // read the output from the command
            log.debug("Here is the standard output of the command");
            while ((s = stdInput.readLine()) != null) {
            	log.debug(s);
            }
            
            log.debug("Here is the standard error of the command (if any)");
	        while ((s = stdError.readLine()) != null) {
                log.debug(s);
            }		
	        
		} catch (Exception e) {
			log.error(e);
		}
	}
	
}
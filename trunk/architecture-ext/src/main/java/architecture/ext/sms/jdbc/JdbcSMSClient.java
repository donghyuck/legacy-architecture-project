/*
 * Copyright 2012, 2013 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.ext.sms.jdbc;

import java.io.UnsupportedEncodingException;
import java.sql.Types;

import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import architecture.common.util.StringUtils;
import architecture.ext.sms.client.SMSClient;
import architecture.ext.sms.client.SMSException;

public class JdbcSMSClient extends JdbcDaoSupport implements SMSClient {

	private String queryString ;

	private String encoding ;
	
	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void openSession() throws SMSException {
		
	}
	/**
	 * 
	 * @param senderNumber 송신자 번호
	 * @param receiverNumber 수신자 번호
	 * @param contents 전송 메시지
	 * 
	 */
	public void send(String senderNumber, String receiverNumber, String contents) throws SMSException {
		
		String contentsToUse = contents ;
		 if(!StringUtils.isEmpty(encoding))
	            try {
	                contentsToUse = new String(contents.getBytes(), encoding);
	            } catch (UnsupportedEncodingException e) {	   
	            }  
		 
		 		getJdbcTemplate().update(queryString, 
				new SqlParameterValue(Types.VARCHAR, receiverNumber) , 
				new SqlParameterValue(Types.VARCHAR, senderNumber) , 
				new SqlParameterValue(Types.VARCHAR, contentsToUse));
	}

	public void closeSession() throws SMSException {
		
	}
	
	
}

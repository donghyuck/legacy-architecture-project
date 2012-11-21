/*
 * Copyright 2012 Donghyuck, Son
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
package architecture.common.adaptor.connector.http;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.adaptor.Connector;

/**
 * 
 * @author donghyuck
 *
 */
public class HttpConnector implements Connector {
	
	protected Log log = LogFactory.getLog(getClass());
	
	public static final String DEFAULT_CHARSET = "UTF-8" ;
	
	public static final String DEFAULT_CONTENT_TYPE = "text/xml; charset=utf-8" ;
	
	/**
	 * 문자 인코딩 형식
	 */
	private String charset = DEFAULT_CHARSET ;
	
	/**
	 * 메시지 형식
	 */
	private String contentType = DEFAULT_CONTENT_TYPE ;
	
	/**
	 * 연결 타임아웃(단위 : 밀리세컨드) 30초
	 */
	private int  connectTimeout = 30 * 1000;
	
	/**
	 * 응답 타임아웃(단위 : 밀리세컨드) 3분
	 */
	private int soTimeout = 3 * 60 * 1000 ;
	
	/**
	 * 재 접속 최대 횟수
	 */
	private int retryMaxCount = 1;
	
	
	/**
	 * 
	 * @return 문자 인코딩 형식 값을 리턴.
	 */
	public String getCharset() {
		return charset;
	}


	/**
	 * 문자인코딩 값을 설정
	 * 
	 * @param charset
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}


	public String getContentType() {
		return contentType;
	}


	public void setContentType(String contentType) {
		this.contentType = contentType;
	}


	public int getConnectTimeout() {
		return connectTimeout;
	}


	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}


	public int getSoTimeout() {
		return soTimeout;
	}


	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}


	public int getRetryMaxCount() {
		return retryMaxCount;
	}


	public void setRetryMaxCount(int retryMaxCount) {
		this.retryMaxCount = retryMaxCount;
	}
	
}

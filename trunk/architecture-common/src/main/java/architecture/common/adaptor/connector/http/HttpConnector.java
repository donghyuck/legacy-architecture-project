package architecture.common.adaptor.connector.http;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.adaptor.Connector;

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
	
	
	public String getCharset() {
		return charset;
	}


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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

public abstract class SimpleClient {

	public static String doService(String serviceUrl, String requestXml) throws Exception {

		// 환경 설정
		String contentType, endoding, transactionUniqueId;
		int connectTimeout, soTimeout;
		int retryMaxCount = 1;
		{
			contentType = "text/xml; charset=utf-8"; // 메시지 형식을 XML로 지정
			endoding = "UTF-8"; // 문자 인코딩 형식

			// TODO timeout 설정
			connectTimeout = 30 * 1000; // 연결 타임아웃(단위 : 밀리세컨드) 30초
			soTimeout = 3 * 60 * 1000;  // 응답 타임아웃(단위 : 밀리세컨드) 3분

		}

		PostMethod method = null; // XML 송신 메소드 선언

		String responseXml = null; // XML 수신
		try {

			HttpClient client; // XML 송신 클라이언트 생성
			{
				HttpConnectionManagerParams params = new HttpConnectionManagerParams();
				params.setConnectionTimeout(connectTimeout);
				params.setSoTimeout(soTimeout);
				params.setTcpNoDelay(true);

				HttpConnectionManager conn = new SimpleHttpConnectionManager();
				conn.setParams(params);
				client = new HttpClient(conn);

				method = new PostMethod(serviceUrl);
				method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
						new DefaultHttpMethodRetryHandler(0, false));
				method.setRequestHeader("Content-Type", contentType);
				method.setRequestHeader("Connection", "close");
				RequestEntity requestEntity = new StringRequestEntity(
						requestXml, contentType, endoding);
				method.setRequestEntity(requestEntity);
			}

//			System.out.println("serviceUrl=" + serviceUrl);
//			System.out.println("requestXml=\n" + requestXml);

			int tryCount = 0;
			while(true){
				tryCount++;
				
				int responseCode = client.executeMethod(method); // XML 송신 및 응답코드 수신
				
				System.out.println("responseCode=" + responseCode);

				{
					InputStream is = method.getResponseBodyAsStream();
					try {
						int readLen;
						byte[] buffer = new byte[1024];
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						while ((readLen = is.read(buffer)) >= 0) {
							baos.write(buffer, 0, readLen);
						}
						byte[] data = baos.toByteArray();
						responseXml = new String(data, endoding);
					} finally {
						is.close();
					}
				}

//					System.out.println("responseXml=\n" + responseXml);

				// 결과 처리
				if (responseCode == HttpStatus.SC_OK) {
					// 정상 응답인 경우
					System.out.println("HTTP CALL SUCCESS. STATUS : " + responseCode);
					break;
					
				} else if (responseXml.indexOf("Fault>") > 0) {
					// 서비스 오류가 발생한 경우
					System.out.println("Response is Fault Message.");
				} else {
					// 통신 오류가 발생한 경우
					System.out.println("HTTP ERROR. STATUS : " + responseCode);
				}
				
				if(tryCount == retryMaxCount){
					System.out.println("Try Count is over! break. RetryMaxCount : " + retryMaxCount + ". TryCount : " + tryCount);
					break;
				}else{
					System.out.println("30sec sleep and retry. TryCount : " + tryCount);
					Thread.sleep(30 * 1000L);
				}

			}// end for 재처리


		} catch (Throwable e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			if (method != null) {
				method.releaseConnection();// 메소스 종료
			}
		}

		return responseXml;
	}
}

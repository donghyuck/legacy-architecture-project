import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public abstract class IndiDutyMatterInfo {

	/**
	 * 		작업 순서
	 * 1. 로그 파일 생성
	 * 2. Header 및 Body 값 셋팅
	 * 3. 요청 XML 생성
	 * 4. GPKI 셋팅
	 * 5. 송신XML 암호화
	 * 6. 송신 및 응답 받기
	 * 7. 응답 데이터 복호화
	 * 8. 응답 데이터 DB 입력
	 */
	public static void main(String[] args) {
		// 행정공유센터 해당 서비스 URL (개발)
		String serviceUrl = "http://10.50.3.172:80/smx/infoservice/esaram/IndiDutyMatterInfoService";
		// 행정공유센터 해당 서비스 URL (운영)
		//String serviceUrl = "http://10.50.3.97:80/smx/infoservice/esaram/IndiDutyMatterInfoService";
		
		// GPKI 암복호화 여부
		boolean isCrypt = true;
		
		// TODO JDBC 연결 정보 셋팅(부처변경)
		String url = "jdbc:oracle:thin:@127.0.0.1:1521:ORADB";
		String user = "scott";
		String password = "tiger";
		
		// 현재시간 구하기
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.KOREA);
		String currenttime = sdf.format(new Date());
		
		// 로그파일 경로 및 파일 명(부처변경)
		String logFilePath = "./log/";
		String logFileName = "IndiDutyMatterInfo_" + currenttime + ".log";
		
		File f = null;
		BufferedWriter br = null;
		
		try{
			/**************************************************
			 * 1. 로그 파일 생성
			 **************************************************/
			try {
				f = new File(logFilePath);
				if(!f.exists()){
					f.mkdirs();
				}
				f = new File(logFilePath + logFileName);
				f.createNewFile();
				br = new BufferedWriter(new FileWriter(f));
				
				br.write("[INFO] [" +sdf.format(new Date())+"] " + "IndiDutyMatterInfo Service Start!!"); br.newLine();
				br.flush();
			}catch(Exception e){
				throw new Exception(e.toString());
			}
			
			
			
			/**************************************************
			 * 2. Header 및 Body 값 셋팅
			 **************************************************/		
			long startTime = System.currentTimeMillis();
			// transactionUniqueId를 조합하기 위한 값 생성
			String rnd1 = Double.toString(java.lang.Math.random()).substring(2, 6);
			String rnd2 = Double.toString(java.lang.Math.random()).substring(2, 6);
			
			// 어제날짜 구하기
			Calendar cal = Calendar.getInstance();
	        cal.add(Calendar.DAY_OF_YEAR, -1);       
	        String yesterday = sdf.format(cal.getTime()).substring(0,8);
			
	        // TODO 헤더 정보 셋팅해야함
			// Header에 들어갈 정보 셋팅
			String serviceName 			= "IndiDutyMatterInfoService";	// 서비스명:인터페이스정의서 참조(연계목록Sheet-행정공유센터 웹서비스명)
			String useSystemCode 		= "xxxxxxxxxx";					// 사용자 시스템코드:행정공유센터에 문의(부처변경)
			String certServerId 		= "SVR0000000000";				// 부처 인증서 ID(부처변경)
			String transactionUniqueId 	= currenttime + rnd1 + rnd2;    // 트랜잭션ID
			String userDeptCode 		= "0000000";					// 연계부처코드:7자리(부처변경)
			String userName 			= "홍길동";						// 사용자 이름:연계담당 공무원 성명(부처변경)
			
			// Body의 요청 파라미터 셋팅
			String reqStartDate 		= "";
			String reqEndDate 			= "";
	    	if(args.length == 0){
				reqStartDate 	= yesterday;
				reqEndDate		= currenttime.substring(0,8);
	    	}else if(args.length == 2){
				reqStartDate 	= args[0];
				reqEndDate		= args[1];
	    	}else{
	    		throw new Exception("Request Parameter is not validate!");
	    	}

			br.write("[INFO] [" +sdf.format(new Date())+"] " + "#######   Header Info   #######"); br.newLine();
			br.write("[INFO] [" +sdf.format(new Date())+"] " + "serviceName         : [" + serviceName + "]"); br.newLine();
			br.write("[INFO] [" +sdf.format(new Date())+"] " + "useSystemCode       : [" + useSystemCode + "]"); br.newLine();
			br.write("[INFO] [" +sdf.format(new Date())+"] " + "certServerId        : [" + certServerId + "]"); br.newLine();
			br.write("[INFO] [" +sdf.format(new Date())+"] " + "transactionUniqueId : [" + transactionUniqueId + "]"); br.newLine();
			br.write("[INFO] [" +sdf.format(new Date())+"] " + "userDeptCode        : [" + userDeptCode + "]"); br.newLine();
			br.write("[INFO] [" +sdf.format(new Date())+"] " + "userName            : [" + userName + "]"); br.newLine();
			br.write("[INFO] [" +sdf.format(new Date())+"] " + "#######   Request Parameter   #######"); br.newLine();
			br.write("[INFO] [" +sdf.format(new Date())+"] " + "reqStartDate        : [" + reqStartDate + "]"); br.newLine();
			br.write("[INFO] [" +sdf.format(new Date())+"] " + "reqEndDate          : [" + reqEndDate + "]"); br.newLine();
			br.flush();
			
			
			
			/**************************************************
			 * 3. 요청 XML 생성
			 **************************************************/
			String requestXml = null;
			// 암호화 전 Request Message:인터페이스정의서 참조(부처변경)
			StringBuffer sb = new StringBuffer();
			sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:typ=\"http://ccais.mopas.go.kr/dh/osc/services/esaram/indidutymatterinfo/types\">	\n");
			sb.append("   <soapenv:Header>																				\n");
			sb.append("      <typ:commonHeader>																			\n");
			sb.append("         <typ:serviceName>" 			+ serviceName 			+ "</typ:serviceName>				\n");
			sb.append("         <typ:useSystemCode>" 		+ useSystemCode 		+ "</typ:useSystemCode>				\n");
			sb.append("         <typ:certServerId>" 		+ certServerId 			+ "</typ:certServerId>				\n");
			sb.append("         <typ:transactionUniqueId>" 	+ transactionUniqueId 	+ "</typ:transactionUniqueId>		\n");
			sb.append("         <typ:userDeptCode>" 		+ userDeptCode 			+ "</typ:userDeptCode>				\n");
			sb.append("         <typ:userName>" 			+ userName 				+ "</typ:userName>					\n");
			sb.append("      </typ:commonHeader>																		\n");
			sb.append("   </soapenv:Header>																				\n");
			sb.append("   <soapenv:Body>																				\n");
			// TODO 서비스 명이 바뀌면 수정해야함. (요청은 "<typ:get" + 서비스명 + ">" 임.
			sb.append("      <typ:getIndiDutyMatterInfoService>																\n");
			sb.append("         <typ:reqStartDate>" 		+ reqStartDate 			+ "</typ:reqStartDate>				\n");
			sb.append("         <typ:reqEndDate>" 			+ reqEndDate 			+ "</typ:reqEndDate>				\n");
			// TODO 서비스 명이 바뀌면 수정해야함. (요청은 "</typ:get" + 서비스명 + ">" 임.
			sb.append("      </typ:getIndiDutyMatterInfoService>																\n");
			sb.append("   </soapenv:Body>																				\n");
			sb.append("</soapenv:Envelope>																				\n");

			requestXml = sb.toString();

        	// 암호화 전 요청 XML
			br.write("[INFO] [" +sdf.format(new Date())+"] " + "Request XML : " + requestXml); br.newLine();
			br.flush();
			
			String encoded = null;
			
			
			GpkiUtil g = null;
			String charset = "UTF-8";
			if(isCrypt){
				try {
					// TODO GPKI 셋팅 해야함.
					/**************************************************
					 * 4. GPKI 셋팅
					 **************************************************/
					
		            // 복호화할 인증서 ID
					String myServerId = certServerId;

		            // 암호화할 인증서 ID:차세대 e-사람 인증서 ID
					// 차세대 e-사람 인증서 ID (개발)
		            String targetServerId = "SVR131000031T";
					// 차세대 e-사람 인증서 ID (운영)
		            //String targetServerId = "SVR1311447001";
		            
					// 부처 인증서 및 라이센스 파일 Path(부처변경) - 같은 폴더에 인증서와 라이센스 파일 있는 것을 기준
		            String certFilePath = "/app/eSaram/gpki/certificate/";
		            
		        	//복호화에 쓰일 부처 인증서키
		        	String envCertFilePathName = certFilePath + myServerId + "_env.cer";
		        	String envPrivateKeyFilePathName = certFilePath + myServerId + "_env.key";
		        	String envPrivateKeyPasswd = "password"; //(부처변경)
		        	
		        	//전자서명에 쓰일 부처 인증서키
		        	String sigCertFilePathName = certFilePath + myServerId + "_sig.cer";
		        	String sigPrivateKeyFilePathName = certFilePath + myServerId + "_sig.key";
		        	String sigPrivateKeyPasswd = "password"; //(부처변경)
	
		        	// GPKI 셋팅 정보 확인
		        	br.write("[INFO] [" +sdf.format(new Date())+"] " + "#######   GPKI Info   #######"); br.newLine();
					br.write("[INFO] [" +sdf.format(new Date())+"] " + "myServerId                : [" + myServerId + "]"); br.newLine();
					br.write("[INFO] [" +sdf.format(new Date())+"] " + "targetServerId            : [" + targetServerId + "]"); br.newLine();
					br.write("[INFO] [" +sdf.format(new Date())+"] " + "certFilePath              : [" + certFilePath + "]"); br.newLine();
					br.write("[INFO] [" +sdf.format(new Date())+"] " + "envCertFilePathName       : [" + envCertFilePathName + "]"); br.newLine();
					br.write("[INFO] [" +sdf.format(new Date())+"] " + "envPrivateKeyFilePathName : [" + envPrivateKeyFilePathName + "]"); br.newLine();
					br.write("[INFO] [" +sdf.format(new Date())+"] " + "envPrivateKeyPasswd       : [" + envPrivateKeyPasswd + "]"); br.newLine();
					br.write("[INFO] [" +sdf.format(new Date())+"] " + "sigCertFilePathName       : [" + sigCertFilePathName + "]"); br.newLine();
					br.write("[INFO] [" +sdf.format(new Date())+"] " + "sigPrivateKeyFilePathName : [" + sigPrivateKeyFilePathName + "]"); br.newLine();
					br.write("[INFO] [" +sdf.format(new Date())+"] " + "sigPrivateKeyPasswd       : [" + sigPrivateKeyPasswd + "]"); br.newLine();
					br.flush();
					
		            g = new GpkiUtil();
		            
		            //ldap 서버 사용 유무
		            g.setIsLDAP(true);
		            
		            //ldap 호출 안할시 인증서 패스
		            g.setGpkiLicPath(certFilePath);		// 라이센스 파일 Path
		            g.setCertFilePath(certFilePath);
		            g.setEnvCertFilePathName(envCertFilePathName);
		            g.setEnvPrivateKeyFilePathName(envPrivateKeyFilePathName);
		            g.setEnvPrivateKeyPasswd(envPrivateKeyPasswd);
		            g.setSigCertFilePathName(sigCertFilePathName);
		            g.setSigPrivateKeyFilePathName(sigPrivateKeyFilePathName);
		            g.setSigPrivateKeyPasswd(sigPrivateKeyPasswd);
		            g.setMyServerId(myServerId);
		            g.setTargetServerIdList(targetServerId);
		            
		            g.init();
	
					/**************************************************
					 * 5. 송신데이터 암호화
					 **************************************************/
					// TODO 서비스 명이 바뀌면 수정해야함. (요청은 "<typ:get" + 서비스명 + ">" 임.
					String original = requestXml
						.split("<typ:getIndiDutyMatterInfoService>")[1]
					    .split("</typ:getIndiDutyMatterInfoService>")[0];
					
					//암호화
					byte[] encrypted 	= g.encrypt(original.getBytes(charset), targetServerId);
					//전자서명
					byte[] signed 		= g.sign(encrypted);
					
					//base64인코딩
					encoded = g.encode(signed);
	
					// 암호화 메시지로 치환
					requestXml = requestXml.replaceAll(original, encoded);
					
		        	// 암호화 후 요청 XML
					br.write("[INFO] [" +sdf.format(new Date())+"] " + "Encrypt Request XML : " + requestXml); br.newLine();
					br.flush();
					
				} catch (Throwable e) {
					throw new Exception(e.toString());
				} // end 4. GPKI 셋팅 & 5. 송신데이터 암호화
			}


			
			/**************************************************
			 * 6. 송신 및 응답 받기
			 **************************************************/
			String responseStr = SimpleClient.doService(serviceUrl, requestXml);
			
        	// 암호화 된 응답 XML
			br.write("[INFO] [" +sdf.format(new Date())+"] " + "Encrypt Response XML : " + responseStr); br.newLine();
			br.flush();
			
			// 응답 실패(에러 메시지로 오거나 "" 메시지가 왔을 경우)시 처리
			if (responseStr.indexOf("Fault>") > 0) {
				throw new Exception("RESPONSE IS FAULT!!!");
			}else if(responseStr.trim().equals("") || responseStr == null){
				throw new Exception("RESPONSE IS EMPTY!!!");
			}
			
			// TODO 서비스 명이 바뀌면 수정해야함. (응답은 "<typ:get" + 서비스명 에서 "Service"가 빠진것 + "Response>" 임.
			String resEncodeStr = responseStr
					.split("<typ:getIndiDutyMatterInfoResponse xmlns:typ=\"http://ccais.mopas.go.kr/dh/osc/services/esaram/indidutymatterinfo/types\">")[1]
					.split("</typ:getIndiDutyMatterInfoResponse>")[0];
			
			String decrypted = "";
			if(isCrypt){
				/**************************************************
				 * 7. 응답 데이터 복호화
				 **************************************************/
				{
					byte[] decoded;
					try {
						 //base64디코딩
						decoded = g.decode(resEncodeStr);
						 //전자서명확인
						byte[] validated = g.validate(decoded);
						 //복호화
						decrypted = new String(g.decrypt(validated), charset);
					} catch (Exception e) {
						throw new Exception(e.toString());
					}
				}
				
	        	// 복호화 된 응답 XML(BODY의 실 데이터 부분)
				br.write("[INFO] [" +sdf.format(new Date())+"] " + "Decrypt Response XML : " + decrypted); br.newLine();
				br.flush();
			}else{
				decrypted = resEncodeStr;
			}
			
//			System.out.println("소요시간 : " + (System.currentTimeMillis() - startTime) + " ms");
			
			/**************************************************
			 * 8. 응답 데이터 DB 입력
			 **************************************************/
			StringBuffer sqlSb = new StringBuffer();
			sqlSb.append("INSERT INTO TN_SLK_MASTER(  ");
			sqlSb.append(" LINK_SEQ                   ");
			sqlSb.append(",LINK_TRT_DTM_VAL           ");
			sqlSb.append(",LINK_CHG_SC_CD             ");
			sqlSb.append(",DPTCD                      ");
			sqlSb.append(",P_ID                       ");
			sqlSb.append(",P_NAME                     ");
			sqlSb.append(",DPRCD                      ");
			sqlSb.append(",DEPT_GNTRM_NM              ");
			sqlSb.append(",DEPT_ABBR_NM               ");
			sqlSb.append(",COP_CD                     ");
			sqlSb.append(",COP_NM                     ");
			sqlSb.append(",JOBPS_CD                   ");
			sqlSb.append(",JOBPS_NM                   ");
			sqlSb.append(",PS_COP_EMPLM_YMD           ");
			sqlSb.append(",PS_DEPT_EMPLM_YMD          ");
			sqlSb.append(",JOBPS_PRMOT_YMD            ");
			sqlSb.append(",HLDOF_STAT_CD              ");
			sqlSb.append(",HLDOF_STAT_NM              ");
			sqlSb.append(",MDFC_DTM                   ");
			sqlSb.append(") VALUES (                  ");
			sqlSb.append(" ?");
			sqlSb.append(",?");
			sqlSb.append(",?");
			sqlSb.append(",?");
			sqlSb.append(",?");
			sqlSb.append(",?");
			sqlSb.append(",?");
			sqlSb.append(",?");
			sqlSb.append(",?");
			sqlSb.append(",?");
			sqlSb.append(",?");
			sqlSb.append(",?");
			sqlSb.append(",?");
			sqlSb.append(",?");
			sqlSb.append(",?");
			sqlSb.append(",?");
			sqlSb.append(",?");
			sqlSb.append(",?");
			sqlSb.append(",?");
			sqlSb.append(")                           ");
			
			Connection con = null;
			PreparedStatement pstmt = null;
			
			try {
				br.write("[INFO] [" +sdf.format(new Date())+"] " + "#######   IF Table Insert Process   #######"); br.newLine();
				br.flush();
				
				String resDataArr[] = null;
				// 응답메시지에 응답데이터가 있다면 DB 입력 작업 실행하기
				if(decrypted.lastIndexOf("</typ:dataList>") > 0){
					// dataList 개수 만큼 dataList안에 있는 데이터를 배열에 담기
					resDataArr = decrypted.substring(0, decrypted.lastIndexOf("</typ:dataList>")).replaceAll("</typ:dataList>", "").split("<typ:dataList>");
					
					br.write("[INFO] [" +sdf.format(new Date())+"] " + "Insert Total Count : " + (resDataArr.length -1)); br.newLine();
					br.flush();
				
					br.write("[INFO] [" +sdf.format(new Date())+"] " + "SQL : " + sqlSb.toString()); br.newLine();
					br.flush();
					
					// JDBC 연결
					Class.forName("oracle.jdbc.driver.OracleDriver");
					con = DriverManager.getConnection(url, user, password);
					con.setAutoCommit(false);

					String link_seq           = "";
					String link_trt_dtm_val   = "";
					String link_chg_sc_cd     = "";
					String dptcd              = "";
					String p_id               = "";
					String p_name             = "";
					String dprcd              = "";
					String dept_gntrm_nm      = "";
					String dept_abbr_nm       = "";
					String cop_cd             = "";
					String cop_nm             = "";
					String jobps_cd           = "";
					String jobps_nm           = "";
					String ps_cop_emplm_ymd   = "";
					String ps_dept_emplm_ymd  = "";
					String jobps_prmot_ymd    = "";
					String hldof_stat_cd      = "";
					String hldof_stat_nm      = "";
					String mdfc_dtm           = "";

					// resDataArr 배열의  0(즉, 첫번째)는 skip. 배열에 담길때는 0(첫번째)는 아무것도 아닌 데이터이기때문.
					for(int i = 1 ; resDataArr.length > i ; i++){
						try{
							pstmt = con.prepareStatement(sqlSb.toString());
							String data = resDataArr[i];
							
							// 값 추출 하기
							link_seq           = data.split("<typ:LINK_SEQ>")[1].split("</typ:LINK_SEQ>")[0];
							link_trt_dtm_val   = data.split("<typ:LINK_TRT_DTM_VAL>")[1].split("</typ:LINK_TRT_DTM_VAL>")[0];
							link_chg_sc_cd     = data.split("<typ:LINK_CHG_SC_CD>")[1].split("</typ:LINK_CHG_SC_CD>")[0];
							dptcd              = data.split("<typ:DPTCD>")[1].split("</typ:DPTCD>")[0];
							p_id               = data.split("<typ:P_ID>")[1].split("</typ:P_ID>")[0];
							p_name             = data.split("<typ:P_NAME>")[1].split("</typ:P_NAME>")[0];
							dprcd              = data.split("<typ:DPRCD>")[1].split("</typ:DPRCD>")[0];
							dept_gntrm_nm      = data.split("<typ:DEPT_GNTRM_NM>")[1].split("</typ:DEPT_GNTRM_NM>")[0];
							dept_abbr_nm       = data.split("<typ:DEPT_ABBR_NM>")[1].split("</typ:DEPT_ABBR_NM>")[0];
							cop_cd             = data.split("<typ:COP_CD>")[1].split("</typ:COP_CD>")[0];
							cop_nm             = data.split("<typ:COP_NM>")[1].split("</typ:COP_NM>")[0];
							jobps_cd           = data.split("<typ:JOBPS_CD>")[1].split("</typ:JOBPS_CD>")[0];
							jobps_nm           = data.split("<typ:JOBPS_NM>")[1].split("</typ:JOBPS_NM>")[0];
							ps_cop_emplm_ymd   = data.split("<typ:PS_COP_EMPLM_YMD>")[1].split("</typ:PS_COP_EMPLM_YMD>")[0];
							ps_dept_emplm_ymd  = data.split("<typ:PS_DEPT_EMPLM_YMD>")[1].split("</typ:PS_DEPT_EMPLM_YMD>")[0];
							jobps_prmot_ymd    = data.split("<typ:JOBPS_PRMOT_YMD>")[1].split("</typ:JOBPS_PRMOT_YMD>")[0];
							hldof_stat_cd      = data.split("<typ:HLDOF_STAT_CD>")[1].split("</typ:HLDOF_STAT_CD>")[0];
							hldof_stat_nm      = data.split("<typ:HLDOF_STAT_NM>")[1].split("</typ:HLDOF_STAT_NM>")[0];
							mdfc_dtm           = data.split("<typ:MDFC_DTM>")[1].split("</typ:MDFC_DTM>")[0];


							//CDATA 삭제하기
							link_seq           = link_seq.substring(link_seq.indexOf("<![CDATA[")+9, link_seq.indexOf("]]>")).trim();
							link_trt_dtm_val   = link_trt_dtm_val.substring(link_trt_dtm_val.indexOf("<![CDATA[")+9, link_trt_dtm_val.indexOf("]]>"));
							link_chg_sc_cd     = link_chg_sc_cd.substring(link_chg_sc_cd.indexOf("<![CDATA[")+9, link_chg_sc_cd.indexOf("]]>"));
							dptcd              = dptcd.substring(dptcd.indexOf("<![CDATA[")+9, dptcd.indexOf("]]>"));
							p_id               = p_id.substring(p_id.indexOf("<![CDATA[")+9, p_id.indexOf("]]>"));
							p_name             = p_name.substring(p_name.indexOf("<![CDATA[")+9, p_name.indexOf("]]>"));
							dprcd              = dprcd.substring(dprcd.indexOf("<![CDATA[")+9, dprcd.indexOf("]]>"));
							dept_gntrm_nm      = dept_gntrm_nm.substring(dept_gntrm_nm.indexOf("<![CDATA[")+9, dept_gntrm_nm.indexOf("]]>"));
							dept_abbr_nm       = dept_abbr_nm.substring(dept_abbr_nm.indexOf("<![CDATA[")+9, dept_abbr_nm.indexOf("]]>"));
							cop_cd             = cop_cd.substring(cop_cd.indexOf("<![CDATA[")+9, cop_cd.indexOf("]]>"));
							cop_nm             = cop_nm.substring(cop_nm.indexOf("<![CDATA[")+9, cop_nm.indexOf("]]>"));
							jobps_cd           = jobps_cd.substring(jobps_cd.indexOf("<![CDATA[")+9, jobps_cd.indexOf("]]>"));
							jobps_nm           = jobps_nm.substring(jobps_nm.indexOf("<![CDATA[")+9, jobps_nm.indexOf("]]>"));
							ps_cop_emplm_ymd   = ps_cop_emplm_ymd.substring(ps_cop_emplm_ymd.indexOf("<![CDATA[")+9, ps_cop_emplm_ymd.indexOf("]]>"));
							ps_dept_emplm_ymd  = ps_dept_emplm_ymd.substring(ps_dept_emplm_ymd.indexOf("<![CDATA[")+9, ps_dept_emplm_ymd.indexOf("]]>"));
							jobps_prmot_ymd    = jobps_prmot_ymd.substring(jobps_prmot_ymd.indexOf("<![CDATA[")+9, jobps_prmot_ymd.indexOf("]]>"));
							hldof_stat_cd      = hldof_stat_cd.substring(hldof_stat_cd.indexOf("<![CDATA[")+9, hldof_stat_cd.indexOf("]]>"));
							hldof_stat_nm      = hldof_stat_nm.substring(hldof_stat_nm.indexOf("<![CDATA[")+9, hldof_stat_nm.indexOf("]]>"));
							mdfc_dtm           = mdfc_dtm.substring(mdfc_dtm.indexOf("<![CDATA[")+9, mdfc_dtm.indexOf("]]>"));

							
							// sql에 set 하기
							pstmt.setInt(1, Integer.parseInt(link_seq));
							pstmt.setString(2,link_trt_dtm_val);
							pstmt.setString(3,link_chg_sc_cd);
							pstmt.setString(4,dptcd);
							pstmt.setString(5,p_id);
							pstmt.setString(6,p_name);
							pstmt.setString(7,dprcd);
							pstmt.setString(8,dept_gntrm_nm);
							pstmt.setString(9,dept_abbr_nm);
							pstmt.setString(10,cop_cd);
							pstmt.setString(11,cop_nm);
							pstmt.setString(12,jobps_cd);
							pstmt.setString(13,jobps_nm);
							pstmt.setString(14,ps_cop_emplm_ymd);
							pstmt.setString(15,ps_dept_emplm_ymd);
							pstmt.setString(16,jobps_prmot_ymd);
							pstmt.setString(17,hldof_stat_cd);
							pstmt.setString(18,hldof_stat_nm);
							pstmt.setString(19,mdfc_dtm);
		
							pstmt.executeUpdate();
							
							con.commit();
							br.write("[INFO] [" +sdf.format(new Date())+"] " + "LINK_SEQ : [" + link_seq + "] is Success"); br.newLine();
						}catch(SQLException e){
							con.rollback();
							if(e.toString().indexOf("ORA-00001") > -1){
								br.write("[INFO] [" +sdf.format(new Date())+"] " + "LINK_SEQ : [" + link_seq + "] is PK Error. Continue!"); br.newLine();	
							}else{
								br.write("[INFO] [" +sdf.format(new Date())+"] " + "LINK_SEQ : [" + link_seq + "] is Error"); br.newLine();
								br.write("[INFO] [" +sdf.format(new Date())+"] " + "IF Table Insert Process Break!"); br.newLine();
								throw new Exception(e.toString());
							}
							
						}catch(Exception e){
							con.rollback();
							throw new Exception(e.toString());
						}finally{
							if(pstmt != null) pstmt.close();
						}
					} // END for
				} else{
					// 입력할 데이터가 없을경우
					br.write("[INFO] [" +sdf.format(new Date())+"] " + "Insert Total Count : 0"); br.newLine();
					br.flush();
				}// END if(decrypted.lastIndexOf("</dataList>") > 0){
				
			} catch (Exception e) {
				throw new Exception(e.toString());
			} finally {
				try {
					if(con != null) con.close();
				} catch (Exception e) {
					try {
						br.write("[ERROR] ["+sdf.format(new Date())+"] " + e.toString()); br.newLine();
						br.flush();
					} catch (IOException e1) {
						throw new Exception(e1.toString());
					}
				}
			}// end 8. 응답 데이터 DB 입력
			
		}catch(Exception e){
			try {
				br.write("[ERROR] ["+sdf.format(new Date())+"] " + e.toString()); br.newLine();
				br.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}finally{
			try {
				br.write("[INFO] [" +sdf.format(new Date())+"] " + "IndiDutyMatterInfo Service End!!"); br.newLine();
				br.flush();
				if(br != null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}// end main
}
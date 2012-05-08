package architecture.ext.sms.jdbc;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import architecture.ext.sms.client.SMSClient;
import architecture.ext.sms.client.SMSException;

public class TechBizJdbcSMSClient extends JdbcDaoSupport implements SMSClient  {

	private AtomicLong isOpened =  new AtomicLong();
	
	public void openSession() throws SMSException {
		isOpened.set(0);
	}

	
	public void send(String senderNumber, String receiverNumber, String contents) throws SMSException {
		
		Date now = new Date();
		long ID = 0 ;
		
		if(isOpened.get() <= 0 ){			
			String sql = getQueryString("sms.select.maxId", "select max(SMS_SEND_SEQ) as maxId from SMS_SEND_INFO");
			long maxId = getJdbcTemplate().queryForLong(sql);
			ID = maxId + 1 ;
			
			String send_dtm = DateFormatUtils.format(now, "yyyyMMddHHmm");
			String reg_dtm = DateFormatUtils.format(now, "yyyyMMddHHmmSS");		
			
			if( reg_dtm.length() > 14)
				reg_dtm = reg_dtm.substring(0, 14);			
			
			String insertSql = getQueryString("sms.insert.sendInfo", "INSERT INTO SMS_SEND_INFO ( sms_send_seq, sms_msg_gb, send_mobile_num, send_dtm, msg, reg_dtm, send_admin_id) values ( ?, 6, ?, ?, ?, ?, 'ehrd' )");
			
			getJdbcTemplate().update(insertSql, new Object[]{ID, senderNumber, send_dtm, contents, reg_dtm });
			
			isOpened.set(ID);
		}else{
			ID = isOpened.get();
		}
				
		if ( ID > 0 ){			
			long maxId = getJdbcTemplate().queryForLong("select max(sms_rcv_seq) + 1 from SMS_RCV_INFO");			
			String insertSql = getQueryString("sms.insert.rcvInfo", " INSERT INTO SMS_RCV_INFO (sms_rcv_seq, sms_send_seq, rcv_mobile_num) values ( ?, ?, ? )");
			getJdbcTemplate().update(insertSql, new Object[]{maxId, ID, receiverNumber });			
		}		
	}

	public void closeSession() throws SMSException {
		isOpened.set(0);
	}

	
	protected String getQueryString(String name, String defautlQuery){
		if(getQueryString(name) == null)
			return defautlQuery;
		else
			return getQueryString(name);
	}
	
	protected String getQueryString(String name){
		return null;
	}
}

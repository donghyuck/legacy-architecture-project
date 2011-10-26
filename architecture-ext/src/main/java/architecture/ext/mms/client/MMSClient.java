package architecture.ext.mms.client;

import java.util.List;

import architecture.ext.sms.client.SMSException;

public interface MMSClient {

	/**
	 * 메시지 전송
	 * 
	 * @param senderNumber 보내는 사람 전화번호
	 * @param receiverNames 받는 사람 이름 
	 * @param receiverNumbers 받는 사람 전화번호 
	 * @param subject 제목 (15자리)
	 * @param contents 메시지 본문
	 * @throws SMSException
	 */
	public void send(String senderNumber, String receiverName, String receiverNumber, String subject, String contents) throws SMSException ;

	/**
	 * date 에 해당하는 시간에 메시지를 전송하는 예약 메시지 전송.
	 * 
	 * @param senderNumber 보내는 사람 전화번호
	 * @param receiverName 받는 사람 이름 목록
	 * @param receiverNumber 받는 사람 전화번호 목록
	 * @param subject 제목
	 * @param contents 메시지 본문
	 * @param date yyyyMMddfHHmmSS 형식의 시간 문자
	 * @throws SMSException
	 */
	public void send(String senderNumber, String receiverName, String receiverNumber, String subject, String contents, String date) throws SMSException ;
	
	/**
	 * 메시지 전송
	 * 
	 * @param senderNumber 보내는 사람 전화번호
	 * @param receiverNames 받는 사람 이름 목록
	 * @param receiverNumbers 받는 사람 전화번호 목록
	 * @param subject 제목 (15자리)
	 * @param contents 메시지 본문
	 * @throws SMSException
	 */
	public void send(String senderNumber, List<String> receiverNames, List<String> receiverNumbers, String subject, String contents) throws SMSException ;
	
	/**
	 * 메시지 전송
	 * 
	 * @param senderNumber 보내는 사람 전화번호
	 * @param receiverNames 받는 사람 이름 목록
	 * @param receiverNumbers 받는 사람 전화번호 목록
	 * @param subject 제목 (15자리)
	 * @param contents 메시지 본문
	 * @throws SMSException
	 */
	public void send(String senderNumber, String[] receiverNames, String[] receiverNumbers, String subject, String contents) throws SMSException ;
	
	/**
	 * date 에 해당하는 시간에 메시지를 전송하는 예약 메시지 전송.
	 * 
	 * @param senderNumber 보내는 사람 전화번호
	 * @param receiverNames 받는 사람 이름 목록
	 * @param receiverNumbers 받는 사람 전화번호 목록
	 * @param subject 제목
	 * @param contents 메시지 본문
	 * @param date yyyyMMddfHHmmSS 형식의 시간 문자
	 * @throws SMSException
	 */
	public void send(String senderNumber,  List<String> receiverNames, List<String> receiverNumbers, String subject, String contents, String date) throws SMSException ;
	
	/**
	 * date 에 해당하는 시간에 메시지를 전송하는 예약 메시지 전송.
	 * 
	 * @param senderNumber 보내는 사람 전화번호
	 * @param receiverNames 받는 사람 이름 목록
	 * @param receiverNumbers 받는 사람 전화번호 목록
	 * @param subject 제목
	 * @param contents 메시지 본문
	 * @param date yyyyMMddfHHmmSS 형식의 시간 문자
	 * @throws SMSException
	 */
	public void send(String senderNumber,  String[] receiverNames, String[] receiverNumbers, String subject, String contents, String date) throws SMSException ;
	
}

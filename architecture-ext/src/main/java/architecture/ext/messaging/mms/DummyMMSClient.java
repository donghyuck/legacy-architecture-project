package architecture.ext.messaging.mms;

import java.util.List;

import architecture.ext.mms.client.MMSClient;
import architecture.ext.sms.client.SMSException;

public class DummyMMSClient implements MMSClient {

	public void send(String senderNumber, String receiverName,
			String receiverNumber, String subject, String contents)
			throws SMSException {

	}

	public void send(String senderNumber, String receiverName,
			String receiverNumber, String subject, String contents, String date)
			throws SMSException {

	}

	public void send(String senderNumber, List<String> receiverNames,
			List<String> receiverNumbers, String subject, String contents)
			throws SMSException {

	}

	public void send(String senderNumber, String[] receiverNames,
			String[] receiverNumbers, String subject, String contents)
			throws SMSException {

	}

	public void send(String senderNumber, List<String> receiverNames,
			List<String> receiverNumbers, String subject, String contents,
			String date) throws SMSException {

	}

	public void send(String senderNumber, String[] receiverNames,
			String[] receiverNumbers, String subject, String contents,
			String date) throws SMSException {

	}

}

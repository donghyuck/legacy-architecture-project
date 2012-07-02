package architecture.ee.audit;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractAuditor implements Auditor {

	protected static final Log log = LogFactory.getLog(AbstractAuditor.class);

	public AbstractAuditor() {
	}

	public MutableAuditMessage auditMethodCall(MethodCall call)
			throws NotAuditableException {
		MutableAuditMessage message = new MutableAuditMessage();
		message.setUser(call.getCaller());
		message.setTimestamp(new Date());
		try {
			message.setNode(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			log.warn("Host name unresolvable. Audit will not contain node information unless the host name can be resolved via DNS.");
			message.setNode(null);
		}
		message.setDetails(buildDetails(call, message));
		message.setDescription(buildDescription(call, message));
		if ((message.getDetails() == null || message.getDescription() == null)
				&& !message.isIgnore())
			return null;
		else
			return message;
	}

	public abstract String buildDescription(MethodCall methodcall,
			MutableAuditMessage mutableauditmessage);

	public abstract String buildDetails(MethodCall methodcall,
			MutableAuditMessage mutableauditmessage);

}

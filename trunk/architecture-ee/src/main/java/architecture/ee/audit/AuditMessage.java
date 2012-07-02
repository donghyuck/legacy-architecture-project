package architecture.ee.audit;

import java.net.InetAddress;
import java.util.Date;

import architecture.common.user.User;


public interface AuditMessage
{

    public abstract User getUser();

    public abstract Date getTimestamp();

    public abstract InetAddress getNode();

    public abstract String getDescription();

    public abstract String getDetails();
}


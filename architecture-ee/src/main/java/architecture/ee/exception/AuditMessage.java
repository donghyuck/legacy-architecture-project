package architecture.ee.exception;

import java.net.InetAddress;
import java.util.Date;

import org.springframework.security.core.userdetails.User;

public interface AuditMessage {

    public abstract User getUser();

    public abstract Date getTimestamp();

    public abstract InetAddress getNode();

    public abstract String getDescription();

    public abstract String getDetails();
    
}

package architecture.ee.audit;

import java.net.InetAddress;
import java.util.Date;

import architecture.common.user.User;

public class MutableAuditMessage implements AuditMessage {

	private String description;

	private String details;

	private long id;

	private boolean ignore;

	private InetAddress node;

	private Date timestamp;

	private User user;

	public MutableAuditMessage() {
		id = -1L;
	}

	public String getDescription() {
		return description;
	}

	public String getDetails() {
		return details;
	}

	public long getId() {
		return id;
	}

	public long getID() {
		return id;
	}

	public InetAddress getNode() {
		return node;
	}

	public int getObjectType() {
		return 2003;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public User getUser() {
		return user;
	}

	public boolean isIgnore() {
		return ignore;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setID(long id) {
		this.id = id;
	}

	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}

	public void setNode(InetAddress node) {
		this.node = node;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public void setUser(User user) {
		this.user = user;
	}
}

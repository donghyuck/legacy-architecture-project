package architecture.user.permission.event;

import java.util.Date;

import architecture.common.lifecycle.event.Event;

public class PermissionEvent extends Event {
    public enum Type {

	ADDED,

	REMOVED
    };

    private Type type;

    private Date date;

    private long userId;

    private long groupId;

    private long permission;

    public PermissionEvent(Object source, Type type) {
	super(source);
	this.type = type;
	date = new Date();
    }

    /**
     * @return date
     */
    public Date getDate() {
	return date;
    }

    /**
     * @return type
     */
    public Type getType() {
	return type;
    }

    public long getUserId() {
	return userId;
    }

    public void setUserId(long userId) {
	this.userId = userId;
    }

    public long getGroupId() {
	return groupId;
    }

    public void setGroupId(long groupId) {
	this.groupId = groupId;
    }

    public long getPermission() {
	return permission;
    }

    public void setPermission(long permission) {
	this.permission = permission;
    }

}

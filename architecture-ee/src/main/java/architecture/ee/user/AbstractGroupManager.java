package architecture.ee.user;

import java.util.Collections;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;

/**
 * @author  donghyuck
 */
public abstract class AbstractGroupManager implements GroupManager, EventSource {
	
	protected Log log = LogFactory.getLog(getClass());
	/**
	 */
	protected EventPublisher eventPublisher;
	protected Cache groupCache;
	protected Cache groupIdCache;
	protected Cache groupMemberCache;
	protected boolean caseInsensitiveGroupNameMatch;
	
		
	public AbstractGroupManager() {
		
        caseInsensitiveGroupNameMatch = true;
        //groupCache = AdminHelper.getCache("groupCache");
        //groupIdCache = AdminHelper.getCache("groupIDCache");
        //groupMemberCache = AdminHelper.getCache("groupMemberCache");
	}
	
	/**
	 * @param groupCache
	 */
	public void setGroupCache(Cache groupCache) {
		this.groupCache = groupCache;
	}



	/**
	 * @param groupIdCache
	 */
	public void setGroupIdCache(Cache groupIdCache) {
		this.groupIdCache = groupIdCache;
	}

	/**
	 * @param groupMemberCache
	 */
	public void setGroupMemberCache(Cache groupMemberCache) {
		this.groupMemberCache = groupMemberCache;
	}


	/**
	 * @param caseInsensitiveGroupNameMatch
	 */
	public void setCaseInsensitiveGroupNameMatch(
			boolean caseInsensitiveGroupNameMatch) {
		this.caseInsensitiveGroupNameMatch = caseInsensitiveGroupNameMatch;
	}


	/**
	 * @param eventPublisher
	 */
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}	
	
	public List<Group> getUserGroups(User user){
		long userId = user.getUserId();
		String key = (new StringBuilder()).append("userGroups-").append(userId).toString();
		
		List<Group> groups;
		
	    log.debug(groupMemberCache);
		
		if(groupMemberCache.get(key) != null)
		{
			groups = (List<Group>)groupMemberCache.get(key).getValue();
		}else{
			groups = lookupGroupsForUser(user);
            groupMemberCache.put(new Element(key, groups));
		}
		
		if(groups == null)
			return Collections.emptyList();
		
		return groups;
	}
	
	protected abstract List<Group> lookupGroupsForUser(User user);
	
    protected abstract Group lookupGroup(String name) throws GroupNotFoundException;
	
    public boolean isGetUserGroupsSupported()
    {
        return false;
    }
    
}

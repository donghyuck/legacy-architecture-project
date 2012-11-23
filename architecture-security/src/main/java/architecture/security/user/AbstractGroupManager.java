package architecture.security.user;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;
import architecture.ee.component.admin.AdminHelper;

/**
 * @author  donghyuck
 */
public abstract class AbstractGroupManager implements GroupManager, EventSource {
	
	protected Log log = LogFactory.getLog(getClass());
	/**
	 */
	protected EventPublisher eventPublisher;
	protected boolean caseInsensitiveGroupNameMatch;
	protected Cache groupCache;
    protected Cache groupIdCache ;
    
	public AbstractGroupManager() {
		
        this.caseInsensitiveGroupNameMatch = true;
        this.groupCache = AdminHelper.getCache("groupCache");
        this.groupIdCache = AdminHelper.getCache("groupIdCache");
	}
	
	public void setGroupCache(Cache groupCache) {
		this.groupCache = groupCache;
	}
	
	
	public void setGroupIdCache(Cache groupIdCache) {
		this.groupIdCache = groupIdCache;
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
	
	
    public Group getGroup(String name)
        throws GroupNotFoundException
    {
    	String nameToUse = caseGroupName(name);
    	if( groupIdCache.isKeyInCache(nameToUse) ){
    		Long groupId = (Long)groupIdCache.get(nameToUse).getValue();    		
    		return getGroup(groupId);
    	}else{
    		Group g = lookupGroup(nameToUse);
    		groupIdCache.put( new Element( nameToUse, g.getGroupId() ) );
    		return getGroup(g.getGroupId());
    	}
    }

	public Group getGroup(long groupId) throws GroupNotFoundException {
		Group group = (Group) groupCache.get( groupId ).getValue();
		if (group == null) {
			 group = lookupGroup(groupId);
			 groupCache.put(new Element(groupId, group));
		}
		return group;
	}
    
    protected String caseGroupName(String name)
    {
        return caseInsensitiveGroupNameMatch ? name.toLowerCase() : name;
    }
    
    protected abstract Group lookupGroup(String name) throws GroupNotFoundException;
	
    protected abstract Group lookupGroup(long groupId)  throws GroupNotFoundException;
    
    
	/*
	public List<Group> getUserGroups(User user){
		
		long userId = user.getUserId();
		
		String key = (new StringBuilder()).append("userGroups-").append(userId).toString();
				
		List<Group> groups;
				
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
	
    protected abstract Group lookupGroup(long groupId)  throws GroupNotFoundException;
        
    public boolean isGetUserGroupsSupported()
    {
        return false;
    }
    
    public int getGroupCount()
    {
        return 0;
    }
    

    public Group getGroup(long groupID, boolean force)
        throws GroupNotFoundException
    {
        if(force)
            return lookupGroup(groupID);
        else
            return getGroup(groupID);
    }

    public Group getGroup(long groupID)
        throws GroupNotFoundException
    {
        Group group = (Group)groupCache.get(Long.valueOf(groupID)).getValue();
        if(group == null)
        {
            group = lookupGroup(groupID);
            groupCache.put( new Element ( Long.valueOf(groupID), group ) );
        }
        return group;
    }

    public Group getGroup(String name)
        throws GroupNotFoundException
    {
        name = caseGroupName(name);
        Long groupIDLong = (Long)groupIdCache.get(name).getValue();
        if(groupIDLong == null)
        {
            Group group = lookupGroup(name);
            groupIDLong = Long.valueOf(group.getGroupId());
            groupIdCache.put( new Element( name, groupIDLong ) );
        }
        return getGroup(groupIDLong.longValue());
    }

    protected void groupNameUpdated(String oldGroupName)
    {
        groupIdCache.remove(caseGroupName(oldGroupName));
    }
    
    protected boolean nameEquals(Group group1, Group group2)
    {
        return group1.getName() != null && group2.getName() != null && caseGroupName(group1.getName()).equals(caseGroupName(group2.getName()));
    }

    protected String caseGroupName(String name)
    {
        return caseInsensitiveGroupNameMatch ? name.toLowerCase() : name;
    }

    public boolean isCreateGroupSupported()
    {
        return false;
    }

    public boolean isDeleteGroupSupported()
    {
        return false;
    }

    public boolean isGroupListSupported()
    {
        return false;
    }

    public List<Group> getGroups()
    {
        return Collections.emptyList();
    }

    public List<Group> getGroups(int startIndex, int numResults)
    {
        return Collections.emptyList();
    }*/
}

package architecture.user;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;
import architecture.common.user.Group;
import architecture.ee.component.admin.AdminHelper;

/**
 * @author  donghyuck
 */
public abstract class AbstractGroupManager implements GroupManager, EventSource {
	
	protected Log log = LogFactory.getLog(getClass());
	protected EventPublisher eventPublisher;
	protected boolean caseInsensitiveGroupNameMatch;
	protected Cache groupCache;
    protected Cache groupIdCache ;
    
    
	public AbstractGroupManager() {
		
        this.caseInsensitiveGroupNameMatch = true;
        this.groupCache = AdminHelper.getCache("groupCache");
        this.groupIdCache = AdminHelper.getCache("groupIdCache");
       // this.groupIdCache = AdminHelper.getCache("groupDomainCache");
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
    	if( groupIdCache.get(nameToUse)  != null ){
    		log.debug( groupIdCache.get(nameToUse )) ;
    		Long groupId = (Long)groupIdCache.get(nameToUse).getValue();    		
    		return getGroup(groupId);
    	}else{
    		Group g = lookupGroup(nameToUse);
    		groupIdCache.put( new Element( nameToUse, g.getGroupId() ) );
    		return getGroup(g.getGroupId());
    	}
    }

	public Group getGroup(long groupId) throws GroupNotFoundException {
		Group group = getGroupInCache(groupId);		
		if (group == null) {
			 group = lookupGroup(groupId);
			 groupCache.put(new Element(groupId, group));
		}
		return group;
	}
    
	protected Group getGroupInCache(long groupId){
		if( groupCache.get(groupId) != null)
			return  (Group) groupCache.get( groupId ).getValue();
		else 
			return null;
	}
	
    protected String caseGroupName(String name)
    {
        return caseInsensitiveGroupNameMatch ? name.toLowerCase() : name;
    }
    
    protected abstract Group lookupGroup(String name) throws GroupNotFoundException;
	
    protected abstract Group lookupGroup(long groupId)  throws GroupNotFoundException;
    
    protected boolean nameEquals(Group g1, Group g2){
    	return g1.getName() != null && g2.getName() != null && caseGroupName(g1.getName()).equals(caseGroupName(g2.getName()));
    }
    
    protected void groupNameUpdated(String oldGroupName){
        groupIdCache.remove(caseGroupName(oldGroupName));	
    }

    
    protected void clearGroupFromCache(Group group){
    	groupCache.remove(group.getGroupId());
    }
        
}

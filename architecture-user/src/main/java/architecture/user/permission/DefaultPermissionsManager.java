package architecture.user.permission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.primitives.ArrayLongList;
import org.apache.commons.collections.primitives.LongList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.primitives.Longs;

import architecture.common.cache.CacheSizes;
import architecture.common.cache.Cacheable;
import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;
import architecture.common.user.Group;
import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.common.user.UserNotFoundException;
import architecture.common.user.authentication.AnonymousUser;
import architecture.common.user.authentication.SystemUser;
import architecture.common.util.LockUtils;
import architecture.user.GroupManager;
import architecture.user.GroupNotFoundException;
import architecture.user.permission.dao.PermissionsDao;
import architecture.user.permission.dao.PermissionsDao.Perm;
import architecture.user.permission.event.PermissionEvent;
import architecture.user.security.spring.userdetails.ExtendedUserDetails;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class DefaultPermissionsManager implements PermissionsManager, EventSource {

	public static class PermissionsCacheEntry implements Cacheable {
		
		private static void sort(long array1[], long array2[])
		{
			int left = 0 ;
			int right = array1.length -1 ;
			for( int i = right ; i > left ; i --){
				int max = 0 ;
				for ( int j = left + 1 ; j <= i ; j++ ){
					if(array1[max] < array1[j])
						max = j;
				}
				long temp = array1[i];
				array1[i] = array1[max];
				array1[max] = temp;
				temp = array2[i];
				array2[i] = array2[max];
				array2[max] = temp;
			}
		}
		private long anonymousAdditivePerms;
		private long anonymousNegativePerms;
		private long registeredAdditivePerms;
		private long registeredNegativePerms;
		private long additiveUserIds[];
		private long negativeUserIds[];
		private long additiveUserPerms[];
		private long negativeUserPerms[];
		private long negativeGroupIds[];
		private long additiveGroupIds[];
		private long additiveGroupPerms[];
		
		private long negativeGroupPerms[];
		
		public PermissionsCacheEntry(){
			
		}
		
		public PermissionsCacheEntry(
				long anonymousAdditivePerms, long anonymousNegativePerms,
				long registeredAdditivePerms, long registeredNegativePerms, 
				long[] additiveUserIds, long[] negativeUserIds, 
				long[] additiveUserPerms, long[] negativeUserPerms, 
				long[] additiveGroupIds, long[] negativeGroupIds,
				long[] additiveGroupPerms, long[] negativeGroupPerms) {			
			if(additiveUserIds.length != additiveUserPerms.length || negativeUserIds.length != negativeUserPerms.length || additiveGroupIds.length != additiveGroupPerms.length || 
					negativeGroupIds.length != negativeGroupPerms.length	){
				throw new IllegalArgumentException("Array length must match.");
			}
			this.anonymousAdditivePerms = anonymousAdditivePerms;
			this.anonymousNegativePerms = anonymousNegativePerms;
			this.registeredAdditivePerms = registeredAdditivePerms;
			this.registeredNegativePerms = registeredNegativePerms;
			this.additiveUserIds = additiveUserIds;
			this.negativeUserIds = negativeUserIds;
			this.additiveUserPerms = additiveUserPerms;
			this.negativeUserPerms = negativeUserPerms;
			this.negativeGroupIds = negativeGroupIds;
			this.additiveGroupIds = additiveGroupIds;
			this.additiveGroupPerms = additiveGroupPerms;
			this.negativeGroupPerms = negativeGroupPerms;
			
			sort(additiveUserIds, additiveUserPerms);
			sort(negativeUserIds, negativeUserPerms);
			sort(additiveGroupIds, additiveGroupPerms);
			sort(negativeGroupIds, negativeGroupPerms);
		}
		
		public long getAnonymousPerms(PermissionType permissionType){
			if(PermissionType.NEGATIVE == permissionType){
				return anonymousNegativePerms;
			}else{
				return anonymousAdditivePerms;
			}
		}
		
		@Override
		public int getCachedSize() {
			int size = 0;
			size += CacheSizes.sizeOfObject() ;
			size += CacheSizes.sizeOfLong() * 4 ;
			size += additiveUserPerms.length * CacheSizes.sizeOfLong() * 2;
			size += negativeUserPerms.length * CacheSizes.sizeOfLong() * 2;
			size += additiveGroupPerms.length * CacheSizes.sizeOfLong() * 2;
			size += negativeGroupPerms.length * CacheSizes.sizeOfLong() * 2;
			return size;
		}

		public long getGroupPerms(long groupId, PermissionType permissionType){
			int index;
			if(PermissionType.NEGATIVE == permissionType ){
				index = Arrays.binarySearch(negativeGroupIds, groupId);
				if( index >= 0 )
					return negativeGroupPerms[index];
				else
					return Permissions.NONE;
			}
			index = Arrays.binarySearch(additiveGroupIds, groupId);
			if(index >= 0)
				return additiveGroupPerms[index];
			else 
				return Permissions.NONE;
		}
		
		public List<Long> getGroupsWithPerm(PermissionType permissionType, long permission){
			List<Long> groups = new ArrayList<Long>();
			if(PermissionType.NEGATIVE == permissionType )
			{
				for(int i = 0 ; i < negativeGroupPerms.length ; i ++ ){
					if( ( negativeGroupPerms[i] & permission ) != 0L)
						groups.add(negativeGroupIds[i]);
				}
				return groups;
			}
			else
			{
				for( int i = 0 ; i< additiveGroupPerms.length ; i ++ ){
					if( (additiveGroupPerms[i] & permission) != 0L)
						groups.add(additiveGroupIds[i]);
				}
				return groups;
			}
		}

		public long getRegisteredPerms(PermissionType permissionType){
			if(PermissionType.NEGATIVE == permissionType){
				return registeredNegativePerms;
			}else{
				return registeredAdditivePerms;
			}
		}
		
		public long getUserPerms(long userId, PermissionType permissionType){
			int index;
			if(PermissionType.NEGATIVE == permissionType ){
				index = Arrays.binarySearch(negativeUserIds, userId);
				if( index >= 0 )
					return negativeUserPerms[index];
				else
					return Permissions.NONE;
			}
			index = Arrays.binarySearch(additiveUserIds, userId);
			if(index >= 0)
				return additiveUserPerms[index];
			else 
				return Permissions.NONE;
		}

		public List<Long> getUsersWithPerm(PermissionType permissionType, long permission){
			List<Long> users = new ArrayList<Long>();
			if(PermissionType.NEGATIVE == permissionType )
			{
				for(int i = 0 ; i < negativeUserPerms.length ; i ++ ){
					if( ( negativeUserPerms[i] & permission ) != 0L && negativeUserIds[i] != 2L)
						users.add(negativeUserIds[i]);
				}
				return users;
			}
			else
			{
				for( int i = 0 ; i< additiveUserPerms.length ; i ++ ){
					if( (additiveUserPerms[i] & permission) != 0L && additiveUserIds[i] != 2L )
						users.add(additiveUserIds[i]);
				}
				return users;
			}
		}
	}
	
	public static long fromDbFormat(int permission){
		return (long)Math.pow(2D, permission);
	}
	private static String getPermsCacheKey(int objectType, long objectId){
		return LockUtils.intern(new StringBuilder().append("perms-cache-").append(objectType).append("-").append(objectId).toString());
	}
	public static int toDbFormat(long permission){
		if(permission < 1L)
			throw new IllegalArgumentException(new StringBuilder().append("Permission value negative : " ).append(permission).toString());
		long value = permission;
		int count ;
		for(count = 0 ; (value & 1L) != 1L ; count ++)
			value >>= 1;
		if( value != 1L)
			throw new IllegalArgumentException(new StringBuilder().append("Permission value negative : " ).append(permission).toString());
		else 
			return count;
	}
	private Log log = LogFactory.getLog(DefaultPermissionsManager.class);
	private EventPublisher eventPublisher;
	private PermissionsDao permissionsDao;
	private GroupManager groupManager;
	private UserManager userManager;
	private Cache userPermsCache;
	private Cache objectsWithPermissionsCache;
	
	private Cache permKeyToAssignedMasksCache;

	private boolean useAltGroupMode = false;

	private boolean enableObjectPermCheck = false;

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void addGroupPermission(int objectType, long objectId, Group group, PermissionType permissionType, long permission) {

		int dbPermission = toDbFormat(permission);
		permissionsDao.addGroupPerms(objectType, objectId, group, permissionType.getId(), dbPermission);
		String bundleKey = getPermsCacheKey(objectType, objectId);
		userPermsCache.remove(bundleKey);
		objectsWithPermissionsCache.remove(objectType);
		//
		Map params = new HashMap();
		params.put("objectType", objectType);
		params.put("objectId", objectId);
		params.put("type", permissionType.getId());
		PermissionEvent event = new PermissionEvent(params, PermissionEvent.Type.ADDED);
		event.setGroupId(group.getGroupId());
		event.setPermission(permission);
		firePermissionEvent(event);		
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void addUserPermission(int objectType, long objectId, User user, PermissionType permissionType, long permission) {
		int permissionToUse = toDbFormat(permission);
		permissionsDao.addUserPerms(objectType, objectId, user.getUserId(), permissionType.getId(), permissionToUse);
		String bundleKey = getPermsCacheKey(objectType, objectId);
		userPermsCache.remove(bundleKey);
		objectsWithPermissionsCache.remove(objectType);
		//
		
		Map params = new HashMap();
		params.put("objectType", objectType);
		params.put("objectId", objectId);
		params.put("type", permissionType.getId());
		PermissionEvent event = new PermissionEvent(params, PermissionEvent.Type.ADDED);
		firePermissionEvent(event);
	}
	
	protected void addUserPermission(int objectType, long objectId, long userId, PermissionType permissionType, long permission) {
		int permissionToUse = toDbFormat(permission);
		permissionsDao.addUserPerms(objectType, objectId, userId, permissionType.getId(), permissionToUse);
		String bundleKey = getPermsCacheKey(objectType, objectId);
		userPermsCache.remove(bundleKey);
		objectsWithPermissionsCache.remove(objectType);
		//
		
		Map params = new HashMap();
		params.put("objectType", objectType);
		params.put("objectId", objectId);
		params.put("type", permissionType.getId());
		PermissionEvent event = new PermissionEvent(params, PermissionEvent.Type.ADDED);
		firePermissionEvent(event);
	}

	public boolean anonymousUserHasPermission(int objectType, long objectId, PermissionType permissionType, long permission) {
		boolean flag = ( getPermissionsCacheEntry(objectType, objectId).getAnonymousPerms(permissionType) & permission ) != 0L;		
		return flag;
	}

	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public long assignPermissionMask(String key) {
		long permissions ;
		if( getPermissionMask(key) != -1L)
		{
			permissions = getPermissionMask(key);
		}else{
			Set<Integer> availableMasks = getAvailableMasks();
			if(availableMasks.isEmpty())
				throw new IllegalStateException( new StringBuilder().append("There are no more free permissions mask for ").append(key).toString() );
			Integer dbFormatMask = availableMasks.iterator().next();
			Long mask = fromDbFormat(dbFormatMask.intValue());
			permKeyToAssignedMasksCache.put(new Element(key, mask));
			permissionsDao.addPermissionMask(key, dbFormatMask.intValue());
			permissions = mask.longValue();
		}
		return permissions;
	}

	private void firePermissionEvent(PermissionEvent event){
		if(this.eventPublisher != null)
			eventPublisher.publish(event);
		else
			log.warn("no event publisher configured. this counld be due to being in setup mode.");
	}

	public Map<String, Long> getAssignedPermissions() {
		Map<String, Long> assigned = new HashMap<String, Long>();
		for(PermissionMask permMask :  permissionsDao.getPermissionsMask()){
			long mask = fromDbFormat(permMask.getMask());
			assigned.put(permMask.getName(), mask);
		}
		return assigned;
	}

	private Set<Integer> getAvailableMasks(){	
		
		Set<Integer> availableMasks = new HashSet<Integer>();
		for( int i = 1 ; i < 63 ; i ++ ){
			availableMasks.add(i);
		}
		Permissions.PermissionAtom perms[] = Permissions.PermissionAtom.values();
		for(Permissions.PermissionAtom perm : perms)
		{
			if( perm.getAtomId() > 0L )
				availableMasks.remove(toDbFormat(perm.getAtomId()));
		}		
		for(PermissionMask mask : permissionsDao.getPermissionsMask() ){
			availableMasks.remove(mask.getMask());
		}
		availableMasks.remove(15);
		availableMasks.remove(22);
		return availableMasks;
	}

	public EventPublisher getEventPublisher() {
		return eventPublisher;
	}

	public Permissions getFinalGroupPerms(int objectType, long objectId, long groupId, PermissionType permissionType) {
		PermissionsCacheEntry bundle = getPermissionsCacheEntry(objectType, objectId);
		long permissions = bundle.getGroupPerms(groupId, permissionType);
		return new Permissions(permissions);
	}


	public Permissions getFinalUserPerms(int objectType, long objectId, long userId, PermissionType permissionType) {
		PermissionsCacheEntry bundle = getPermissionsCacheEntry(objectType, objectId);
		long permissions = bundle.getAnonymousPerms(permissionType);
		boolean isUser = userId > 0;
		if( isUser)
		{
			permissions |= bundle.getRegisteredPerms(permissionType);
			permissions |= bundle.getUserPerms(userId, permissionType);
			Permissions groupPerms = getGroupPerms(bundle, userId, permissionType);
			permissions |= groupPerms.value();
		}
		return new Permissions(permissions);
	}
	
	public GroupManager getGroupManager() {
		return groupManager;
	}
	
	private Permissions getGroupPerms(PermissionsCacheEntry bundle, long userId, PermissionType permissionType){
		
		long permissions = 0L;
		User user;
		try {
			user = userManager.getUser(userId);
		} catch (UserNotFoundException e) {
			return new Permissions(Permissions.NONE);
		}
		if( !useAltGroupMode){
			List<Group> groups = groupManager.getUserGroups(user);
			for( Group group: groups)
				permissions |= bundle.getGroupPerms(group.getGroupId(), permissionType);
		}else if( PermissionType.NEGATIVE == permissionType){
			long groupIds[] = bundle.negativeGroupIds;
			for(long groupId : groupIds){
				Group group;
				try {
					group = groupManager.getGroup(groupId);
					   if( groupManager.hasMembership(group, user) ){
						   permissions |= bundle.getGroupPerms(groupId, permissionType);
					   }
				} catch (GroupNotFoundException e) {
					log.warn("group not found.");
				}
			}
		}
		return new Permissions(permissions);		
	}
	public Cache getObjectsWithPermissionsCache() {
		return objectsWithPermissionsCache;
	}
	
	public int getoupsWithPermissionCount(int objectType, long objectId, PermissionType permissionType, long permission){
		return getPermissionsCacheEntry(objectType, objectId).getGroupsWithPerm(permissionType, permission).size();
	}
	
	public long getPermissionMask(String key) {
		Long permissions = -1L;
		if(permKeyToAssignedMasksCache.get(key) == null){
			int dbFormatMask = permissionsDao.getPermissionMask(key);
			if( dbFormatMask != -1)
			{
				permissions = -1L;
			}else{
				long permissionToUse = fromDbFormat(dbFormatMask);
				permissions = permissionToUse;
				permKeyToAssignedMasksCache.put(new Element(key, permissionToUse));
			}	
		}else{
			permissions = (Long) permKeyToAssignedMasksCache.get(key).getValue();
		}
		return permissions;
	}

	public Permissions getPermissions(Authentication auth, int objectType, long objectId){
		long userId = -1L;
		if(auth!=null){
			Object principal = auth.getPrincipal();
			if( principal != null){
				if( principal instanceof ExtendedUserDetails ){
					ExtendedUserDetails details = (ExtendedUserDetails)principal;
					userId = details.getUserId();
				}
			}
		}
		return getPermissions(userId, objectType, objectId);
	}
	
	public Permissions getPermissions(long userId, int objectType, long objectId){
		Permissions permissionUnion = new Permissions(Permissions.NONE);
		Permissions grantedPermissions = getFinalUserPerms(objectType, objectId, userId, PermissionType.ADDITIVE);
		Permissions revokedPermissions = getFinalUserPerms(objectType, objectId, userId, PermissionType.NEGATIVE);
		permissionUnion.set(grantedPermissions.value(), true);
		permissionUnion.set(revokedPermissions.value(), false);
		return permissionUnion;
	}

	public PermissionsCacheEntry getPermissionsCacheEntry(int objectType, long objectId){
		
		String bundleKey = getPermsCacheKey(objectType, objectId);
		PermissionsCacheEntry bundle = null;
		if( userPermsCache.get(bundleKey) != null )
		{
			bundle = (PermissionsCacheEntry) userPermsCache.get(bundleKey).getValue();
		}
		if( bundle == null ){
			synchronized(bundleKey){
				List<Perm> userPerms = permissionsDao.getUserPerms(objectType, objectId);
				long anonymousAdditivePerms = Permissions.NONE;
				long anonymousNegativePerms = Permissions.NONE;
				long registeredAdditivePerms = Permissions.NONE;
				long registeredNegativePerms = Permissions.NONE;
				LongList additiveUserIds = new ArrayLongList() ;
				LongList negativeUserIds = new ArrayLongList() ;
				LongList additiveUserPerms = new ArrayLongList() ;
				LongList negativeUserPerms = new ArrayLongList() ;
				for(Perm perm : userPerms)
				{
					long permission = fromDbFormat(perm.getPermission());
					if( perm.getObjectId() == -1L && perm.getType() == PermissionType.NEGATIVE.getId()){
						anonymousNegativePerms |= permission;
					}else if(perm.getObjectId() == -1L){
						anonymousAdditivePerms |= permission;
					}else if( perm.getObjectId() == 0L && perm.getType() == PermissionType.NEGATIVE.getId()){
						registeredNegativePerms |= permission;
					}else if (perm.getObjectId() == 0L){
						registeredAdditivePerms |= permission;
					}else if (perm.getType() == PermissionType.NEGATIVE.getId()){
						int index = negativeUserIds.indexOf(perm.getObjectId());
						if( index != -1)
						{
							permission |= negativeUserPerms.get(index);
							negativeUserPerms.removeElementAt(index);
							negativeUserIds.removeElementAt(index);
						}
						negativeUserIds.add(perm.getObjectId());
						negativeUserPerms.add(permission);
					}else{
						int index = additiveUserIds.indexOf(perm.getObjectId());
						if( index != -1){
							permission |= additiveUserPerms.get(index);
							additiveUserPerms.removeElementAt(index);
							additiveUserIds.removeElementAt(index);
						}
						additiveUserIds.add(perm.getObjectId());
						additiveUserPerms.add(permission);
					}
				}
				List<Perm> groupPerms = permissionsDao.getGroupPerms(objectType, objectId);
				LongList additiveGroupIds = new ArrayLongList() ;
				LongList negativeGroupIds = new ArrayLongList() ;
				LongList additiveGroupPerms = new ArrayLongList() ;
				LongList negativeGroupPerms = new ArrayLongList() ;
				for(Perm perm : groupPerms){
					long permission = fromDbFormat(perm.getPermission());
					if(perm.getType() == PermissionType.NEGATIVE.getId())
					{
						int index = negativeGroupIds.indexOf(perm.getObjectId());
						if( index != -1){
							permission |= negativeGroupPerms.get(index);
							negativeGroupPerms.removeElementAt(index);
							negativeGroupIds.removeElementAt(index);
						}
						negativeGroupIds.add(perm.getObjectId());
						negativeGroupPerms.add(permission);
					}else{
						
						int index = additiveGroupIds.indexOf(perm.getObjectId());
						if( index != -1){
							permission |= additiveGroupPerms.get(index);
							additiveGroupPerms.removeElementAt(index);
							additiveGroupIds.removeElementAt(index);
						}
						additiveGroupIds.add(perm.getObjectId());
						additiveGroupPerms.add(permission);
					}
				}
				bundle = new PermissionsCacheEntry(
					anonymousAdditivePerms, anonymousNegativePerms, 
					registeredAdditivePerms, registeredNegativePerms,
					additiveUserIds.toArray(), negativeUserIds.toArray(),
					additiveUserPerms.toArray(), negativeUserPerms.toArray(),
					additiveGroupIds.toArray(), negativeGroupIds.toArray(),
					additiveGroupPerms.toArray(), negativeGroupPerms.toArray()
				);
				userPermsCache.put(new Element(bundleKey, bundle));
			}			
		}
		
		return bundle;
		
	}

	public PermissionsDao getPermissionsDao() {
		return permissionsDao;
	}

	public Cache getPermKeyToAssignedMasksCache() {
		return permKeyToAssignedMasksCache;
	}

	public Permissions getSystemPermissions(Authentication auth){
		Permissions permissions = new Permissions(Permissions.NONE);		
		if( auth != null ){
			
			Object principal = auth.getPrincipal();
			if( principal == null){
				log.warn("Null principal for request. No permissions given");
				permissions = new Permissions(Permissions.NONE);
			}else if(principal instanceof SystemUser){
				permissions = new Permissions(Permissions.SYSTEM_ADMINISTRATION);				
			}else if (principal instanceof AnonymousUser){
				permissions = getPermissions(-1L, 17, -1L);
			}else if (principal instanceof ExtendedUserDetails ){
				ExtendedUserDetails details = (ExtendedUserDetails)principal;
				// do more...
			}else if (principal instanceof User){
				User user = (User)principal;
				permissions = getPermissions(user.getUserId(), 17, -1L);
			}else{
				log.warn("");
				permissions = new Permissions(Permissions.NONE);
			}	
		}
		return permissions;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public Cache getUserPermsCache() {
		return userPermsCache;
	}

	public List<Group> groupsWithPermission(int objectType, long objectId, PermissionType permissionType, long permission){
		
		List<Long> groupIds = getPermissionsCacheEntry(objectType, objectId).getGroupsWithPerm(permissionType, permission);
		List<Group> groups = new ArrayList<Group>(groupIds.size());
		for(Long groupId : groupIds){
			try {
				groups.add(groupManager.getGroup(groupId));
			} catch (GroupNotFoundException e) {
				log.warn("group not found.");
			}
		}
		return groups;
	}

	
	@Override
	public boolean hasPermissionsSet(int objectType, long objectId) {
		
		boolean flag = !enableObjectPermCheck;
		if(enableObjectPermCheck){			
			long objectsWithPermissions [] = null;
			if( objectsWithPermissionsCache.get(objectType)!=null )
			{	
				objectsWithPermissions = (long[]) objectsWithPermissionsCache.get(objectType).getValue();
			}
			if( objectsWithPermissions != null ){
				flag = Arrays.binarySearch(objectsWithPermissions, objectId) >= 0 ;
			}else{
				String key = LockUtils.intern(new StringBuilder().append("check-perms-").append(objectType).toString());
				synchronized(key){
					List<Long> objectIds = new ArrayList<Long>();					
					objectIds.addAll(permissionsDao.getObjectIdsWithUserPerms(objectType));
					objectIds.addAll(permissionsDao.getObjectIdsWithGroupPerms(objectType));
					long objectIdArray[] = Longs.toArray(objectIds);
					Arrays.sort(objectIdArray);
					objectsWithPermissionsCache.put(new Element(objectType, objectIdArray));
				}
			}
			flag = Arrays.binarySearch((long[]) objectsWithPermissionsCache.get(objectType).getValue(), objectId) >= 0;
		}
		return flag;
	}
	
	
	public boolean isEnableObjectPermCheck() {
		return enableObjectPermCheck;
	}
	
	public boolean isUseAltGroupMode() {
		return useAltGroupMode;
	}
	
	
	public boolean registeredUserHasPermission(int objectType, long objectId, PermissionType permissionType, long permission) {
		boolean flag = ( getPermissionsCacheEntry(objectType, objectId).getRegisteredPerms(permissionType) & permission ) != 0L;
		return flag;
	}

		
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void removeAllGroupPermissions(int objectType, long objectId, PermissionType permissionType){
		
		permissionsDao.removeAllGroupPerms(objectType, objectId, permissionType.getId());
		String bundleKey = getPermsCacheKey(objectType, objectId);
		userPermsCache.remove(bundleKey);
		objectsWithPermissionsCache.remove(objectType);
		//		
		Map params = new HashMap();
		params.put("objectType", objectType);
		params.put("objectId", objectId);
		params.put("type", permissionType.getId());
		PermissionEvent event = new PermissionEvent(params, PermissionEvent.Type.REMOVED);
		event.setPermission(Permissions.NONE);
		firePermissionEvent(event);		
	}

	public void removeAllPermissions(int objectType, long objectId) {
		
		String bundleKey = this.getPermsCacheKey(objectType, objectId);
		userPermsCache.remove(bundleKey);
		objectsWithPermissionsCache.remove(objectType);
		//
		
		Map params = new HashMap();
		params.put("objectType", objectType);
		params.put("objectId", objectId);
		PermissionEvent event = new PermissionEvent(params, PermissionEvent.Type.REMOVED);
		event.setPermission(Permissions.NONE);
		firePermissionEvent(event);				
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void removeAllUserPermissions(int objectType, long objectId, PermissionType permissionType){
		permissionsDao.removeAllUserPerms(objectType, objectId, permissionType.getId());
		String bundleKey = this.getPermsCacheKey(objectType, objectId);
		userPermsCache.remove(bundleKey);
		objectsWithPermissionsCache.remove(objectType);
		//
		Map params = new HashMap();
		params.put("objectType", objectType);
		params.put("objectId", objectId);
		params.put("type", permissionType);
		PermissionEvent event = new PermissionEvent(params, PermissionEvent.Type.REMOVED);
		event.setPermission(Permissions.NONE);
		firePermissionEvent(event);				
	}


	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void removeGroupPermission(int objectType, long objectId, Group group, PermissionType permissionType, long permission) {
		int permissionToUse = toDbFormat(permission);
		permissionsDao.removeGroupPerms(objectType, objectId, group, permissionType.getId(), permissionToUse);
		String bundleKey = getPermsCacheKey(objectType, objectId);
		userPermsCache.remove(bundleKey);
		objectsWithPermissionsCache.remove(objectType);
		//
		Map params = new HashMap();
		params.put("objectType", objectType);
		params.put("objectId", objectId);
		params.put("type", permissionType.getId());
		PermissionEvent event = new PermissionEvent(params, PermissionEvent.Type.REMOVED);
		event.setGroupId(group.getGroupId());
		event.setPermission(permission);		
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void removeRegisteredUserPermission(int objectType , long objectId , PermissionType permissionType, long permission){
		removeUserPermission(objectType, objectId, 0L, permissionType, permission);
	}
	
	protected void removeUserPermission(int objectType , long objectId , long userId, PermissionType permissionType, long permission ){
		int permissionToUse = toDbFormat(permission);
		permissionsDao.removeUserPerms(objectType, objectId, userId, permissionType.getId(), permissionToUse);
		String bundleKey = getPermsCacheKey(objectType, objectId);
		userPermsCache.remove(bundleKey);
		objectsWithPermissionsCache.remove(objectType);
		//
		Map params = new HashMap();
		params.put("objectType", objectType);
		params.put("objectId", objectId);
		params.put("type", permissionType.getId());
		PermissionEvent event = new PermissionEvent(params, PermissionEvent.Type.REMOVED);
		event.setUserId(userId);
		event.setPermission(permission);
		firePermissionEvent(event);	
	}
	
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void removeUserPermission(int objectType, long objectId, User user, PermissionType permissionType, long permission) {
		int permissionToUse = toDbFormat(permission);
		permissionsDao.removeUserPerms(objectType, objectId, user.getUserId(), permissionType.getId(), permissionToUse);
		String bundleKey = getPermsCacheKey(objectType, objectId);
		userPermsCache.remove(bundleKey);
		objectsWithPermissionsCache.remove(objectType);
		
		Map params = new HashMap();
		params.put("objectType", objectType);
		params.put("objectId", objectId);
		params.put("type", permissionType.getId());
		PermissionEvent event = new PermissionEvent(params, PermissionEvent.Type.REMOVED);
		event.setUserId(user.getUserId());
		event.setPermission(permission);
		firePermissionEvent(event);		
	}
	
	public void setEnableObjectPermCheck(boolean enableObjectPermCheck) {
		this.enableObjectPermCheck = enableObjectPermCheck;
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}
	
	
	public void setObjectsWithPermissionsCache(Cache objectsWithPermissionsCache) {
		this.objectsWithPermissionsCache = objectsWithPermissionsCache;
	}
	
	public void setPermissionsDao(PermissionsDao permissionsDao) {
		this.permissionsDao = permissionsDao;
	}
	
	public void setPermKeyToAssignedMasksCache(Cache permKeyToAssignedMasksCache) {
		this.permKeyToAssignedMasksCache = permKeyToAssignedMasksCache;
	}
	
	
	public void setUseAltGroupMode(boolean useAltGroupMode) {
		this.useAltGroupMode = useAltGroupMode;
	}
	
	

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setUserPermsCache(Cache userPermsCache) {
		this.userPermsCache = userPermsCache;
	}

	public List<User> usersWithPermission(int objectType, long objectId, PermissionType permissionType, long permission) {
		List<Long> userIds = getPermissionsCacheEntry(objectType, objectId).getUsersWithPerm(permissionType, permission);
		List<User> users = new ArrayList<User>(userIds.size());
		for( Long userId : userIds)
		{
			try {
				users.add( userManager.getUser(userId) );
			} catch (UserNotFoundException e) {
				log.warn("user not found");
			}
		}
		return users;
	}


	public int usersWithPermissionCount(int objectType, long objectId, PermissionType permissionType, long permission) {		
		return getPermissionsCacheEntry(objectType, objectId).getUsersWithPerm(permissionType, permission).size();
	}
	
}
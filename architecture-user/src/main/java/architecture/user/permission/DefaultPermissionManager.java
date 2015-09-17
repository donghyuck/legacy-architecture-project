package architecture.user.permission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import architecture.common.cache.CacheSizes;
import architecture.common.cache.Cacheable;
import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;
import architecture.common.util.LockUtils;
import architecture.user.GroupManager;
import architecture.user.permission.dao.PermissionDao;
import net.sf.ehcache.Cache;

public class DefaultPermissionManager implements PermissionManager, EventSource {

	public static class PermissionsCacheEntry implements Cacheable {
		
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
	}
	
	private EventPublisher eventPublisher;
	private PermissionsDao permissionsDao;
	private GroupManager groupManager;
	private Cache userPermsCache;
	
	public GroupManager getGroupManager() {
		return groupManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}


	public PermissionsDao getPermissionsDao() {
		return permissionsDao;
	}

	public void setPermissionsDao(PermissionsDao permissionsDao) {
		this.permissionsDao = permissionsDao;
	}

	public EventPublisher getEventPublisher() {
		return eventPublisher;
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public Cache getUserPermsCache() {
		return userPermsCache;
	}

	public void setUserPermsCache(Cache userPermsCache) {
		this.userPermsCache = userPermsCache;
	}

	public Permissions getFinalUserPermissions(int objectType, long objectId, long userId,
			PermissionType permissionType) {
		// TODO Auto-generated method stub
		return null;
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
				List permissionsDao.getUserPerms(objectType, objectId);
			}			
		}
		
	}
	
	private static String getPermsCacheKey(int objectType, long objectId){
		return LockUtils.intern(new StringBuilder().append("permsCache-").append(objectType).append("-").append(objectId).toString());
	}
}

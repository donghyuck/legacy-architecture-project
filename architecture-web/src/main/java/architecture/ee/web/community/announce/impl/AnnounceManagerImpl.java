/*
 * Copyright 2012, 2013 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.ee.web.community.announce.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;
import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.common.user.UserNotFoundException;
import architecture.ee.web.community.announce.Announce;
import architecture.ee.web.community.announce.AnnounceManager;
import architecture.ee.web.community.announce.AnnounceNotFoundException;
import architecture.ee.web.community.announce.dao.AnnounceDao;

public class AnnounceManagerImpl implements AnnounceManager, EventSource {
	protected Log log = LogFactory.getLog(getClass());	
	private EventPublisher eventPublisher;
	
	private AnnounceDao announceDao ;
	private Cache announceCache ;
	private UserManager userManager;
	
	
	
	/**
	 * @return userManager
	 */
	public UserManager getUserManager() {
		return userManager;
	}

	/**
	 * @param userManager 설정할 userManager
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 * @return announceCache
	 */
	public Cache getAnnounceCache() {
		return announceCache;
	}

	/**
	 * @param announceCache 설정할 announceCache
	 */
	public void setAnnounceCache(Cache announceCache) {
		this.announceCache = announceCache;
	}

	/**
	 * @return eventPublisher
	 */
	public EventPublisher getEventPublisher() {
		return eventPublisher;
	}

	/**
	 * @param eventPublisher 설정할 eventPublisher
	 */
	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	/**
	 * @return announceDao
	 */
	public AnnounceDao getAnnounceDao() {
		return announceDao;
	}

	/**
	 * @param announceDao 설정할 announceDao
	 */
	public void setAnnounceDao(AnnounceDao announceDao) {
		this.announceDao = announceDao;
	}

	public Announce createAnnounce(User user) {
		// TODO 자동 생성된 메소드 스텁
		AnnounceImpl impl = new AnnounceImpl(-1L, 0, -1L , user);
		return impl;
	}

	public Announce createAnnounce(User user, int objectType, long objectId) {
		AnnounceImpl impl = new AnnounceImpl(-1L, objectType, objectId , user);
		return impl;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void addAnnounce(Announce announce) {
		Long announceId = announce.getAnnounceId();
		if( announceId < 0 )
			announceId = announceDao.nextId();
		announce.setAnnounceId(announceId);
		announceDao.insert(announce);		
		updateCache(announce);
		// fire event;
	}

	private void updateCache( Announce announce ){
		announceCache.put(new Element( announce.getAnnounceId(), announce ));		
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void updateAnnounce(Announce announce) {
		Date now = new Date();
		announce.setModifiedDate(now);
		
		announceDao.update(announce);
		updateCache(announce);
	}

	public Announce getAnnounce(long announceId) throws AnnounceNotFoundException  {
		
		
		Announce announce = null;
		if( announceCache.get(announceId)!=null ){
			announce = (Announce) announceCache.get(announceId).getValue();	
		}
		
		if( announce == null ){
			announce = announceDao.load(announceId);
			
			User user;
			try {
				user = userManager.getUser(announce.getUserId());
				announce.setUser( user  );
			} catch (UserNotFoundException e) {

			}
			updateCache(announce);
		}		
		return announce;
	}

	/**
	 * 공지 종료일이 없는 경우 공지 시작일이 현재일 이전이거나 같은 경우 ..
	 * 
	 */
	public List<Announce> getAnnounces(int objectType, long objectId) {
		
		List<Long> announceIds = announceDao.getAnnounceIds(objectType, objectId);
		
		List<Announce> list = new ArrayList<Announce>();
		Date now = new Date();
		long startDate = now.getTime();
		long endDate = now.getTime();

		for( Long announceId : announceIds){
			try {
				Announce announce = getAnnounce(announceId);
				if( announce.getEndDate() == null){					
					if( announce.getStartDate().getTime() <= startDate ){
						list.add(announce);
					}
				}else if (announce.getEndDate().getTime() >= endDate && announce.getStartDate().getTime() <= startDate ){
					list.add(announce);
				}				
			} catch (AnnounceNotFoundException e) {
				log.warn(e);
			}
		}
		return list;
	}

	public List<Announce> getAnnounces(int objectType, long objectId, Date startDate, Date endDate) {
		List<Long> announceIds = announceDao.getAnnounceIds(objectType, objectId);
		 if( announceIds.size() == 0 )
			 return Collections.EMPTY_LIST;
		 if( startDate == null )
			 startDate = new Date(0x8000000000000000L);
		 if( endDate == null )
			 endDate = new Date(0x7fffffffffffffffL);
		 
		 List<Announce> results = filterAnnounces(startDate, endDate, announceIds);		 
		return results;
	}

	private List<Announce> filterAnnounces (Date startDate, Date endDate, List<Long> announceIds ){
		List<Announce> list = new ArrayList<Announce>();
		for( Long announceId : announceIds ){
			try {
				Announce announce = getAnnounce(announceId);
				if( announce.getEndDate() == null){
					
					if( announce.getStartDate().getTime() <= startDate.getTime() )
						list.add(announce);
					
				}else if (announce.getEndDate().getTime() >= endDate.getTime() && announce.getStartDate().getTime() <= startDate.getTime() ){
					list.add(announce);
				}				
			} catch (AnnounceNotFoundException e) {
				log.warn(e);
			}
		}
		return list;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void deleteAnnounce(long announceId) {
		try {
			Announce announce = getAnnounce(announceId);
			announceDao.delete(announce);
			// fire event;
			announceCache.remove(announce.getAnnounceId());
		} catch (AnnounceNotFoundException e) {
			log.error(e);
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void deleteUserAnnounces(User user) {
		
		List<Long> ids = announceDao.getAnnounceIdsForUser(user.getUserId());
		for(Long id : ids){
			deleteAnnounce(id);
		}		
	}

	public void moveAnnounces(int fromObjectType, int toObjectType) {
		// TODO 자동 생성된 메소드 스텁
		
	}

	public void moveAnnounces(int fromObjectType, long fromObjectId,
			int toObjectType, long toObjectId) {
		// TODO 자동 생성된 메소드 스텁		
	}

}

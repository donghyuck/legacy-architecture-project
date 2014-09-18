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
package architecture.ee.web.community.social.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.ehcache.Cache;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.user.Company;
import architecture.common.user.User;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.community.social.ServiceProviderFactory;
import architecture.ee.web.community.social.SocialNetwork;
import architecture.ee.web.community.social.SocialNetwork.Media;
import architecture.ee.web.community.social.SocialNetworkManager;
import architecture.ee.web.community.social.SocialServiceProvider;
import architecture.ee.web.community.social.dao.SocialNetworkDao;

public class DefaultSocialNetworktManager implements SocialNetworkManager {

	private SocialNetworkDao socialNetworkDao;
	private Cache socialNetworkCache;
	
	public DefaultSocialNetworktManager() {
	}


	/**
	 * @return socialNetworkDao
	 */
	public SocialNetworkDao getSocialNetworkDao() {
		return socialNetworkDao;
	}


	/**
	 * @param socialNetworkDao 설정할 socialNetworkDao
	 */
	public void setSocialNetworkDao(SocialNetworkDao socialNetworkDao) {
		this.socialNetworkDao = socialNetworkDao;
	}


	/**
	 * @return socialNetworkCache
	 */
	public Cache getSocialNetworkCache() {
		return socialNetworkCache;
	}


	/**
	 * @param socialNetworkCache 설정할 socialNetworkCache
	 */
	public void setSocialNetworkCache(Cache socialNetworkCache) {
		this.socialNetworkCache = socialNetworkCache;
	}


	public SocialNetwork createSocialNetwork(Company company, Media media) {
		SocailNetworkImpl impl = new SocailNetworkImpl(); 
		Date now = new Date();		
		impl.setCreationDate(now);
		impl.setModifiedDate(now);
		impl.setObjectType(company.getModelObjectType());
		impl.setObjectId(company.getCompanyId());
		impl.setServiceProviderName(media.name().toLowerCase());
		impl.setSocialAccountId(-1L);		
		impl.setConnected(false);
		impl.setSocialServiceProvider(createSocialServiceProvider(media));	
		return impl;
	}

	public SocialNetwork createSocialNetwork(User user, Media media) {
		SocailNetworkImpl impl = new SocailNetworkImpl(); 
		Date now = new Date();		
		impl.setCreationDate(now);
		impl.setModifiedDate(now);
		impl.setObjectType(user.getModelObjectType());
		impl.setObjectId(user.getUserId());
		impl.setServiceProviderName(media.name().toLowerCase());
		impl.setConnected(false);
		impl.setSocialAccountId(-1L);		
		impl.setSocialServiceProvider(createSocialServiceProvider(media));
		
		return impl;
	}

	private SocialServiceProvider createSocialServiceProvider(SocialNetwork.Media media){		
		SocialServiceProvider provider = ServiceProviderFactory.getServiceProvider(media);
		return provider;
	} 
	
	public SocialNetwork getSocialNetworkById(long socialAccountId)
			throws NotFoundException {
		SocialNetwork account = socialNetworkDao.getSocialAccountById(socialAccountId);
		return account;
	}

	public List<SocialNetwork> getSocialNetworks(Company company) {
		List<Long> ids = socialNetworkDao.getSocialAccountIds(ModelTypeFactory.getTypeIdFromCode("COMPANY"), company.getCompanyId());
		List<SocialNetwork> accounts = new ArrayList<SocialNetwork>(ids.size());
		for(Long id : ids){
			try {
				accounts.add( getSocialNetworkById(id) );
			} catch (NotFoundException e) {
			}
		}
		return accounts;
	}

	public List<SocialNetwork> getSocialNetworks(User user) {
		List<Long> ids = socialNetworkDao.getSocialAccountIds(ModelTypeFactory.getTypeIdFromCode("USER"), user.getUserId());
		List<SocialNetwork> accounts = new ArrayList<SocialNetwork>(ids.size());
		for(Long id : ids){
			try {
				accounts.add( getSocialNetworkById(id) );
			} catch (NotFoundException e) {
			}
		}
		return accounts;
	}

	public List<SocialNetwork> getSocialNetworks(int objectType, long objectId) {
		List<Long> ids = socialNetworkDao.getSocialAccountIds(objectType, objectId);
		List<SocialNetwork> accounts = new ArrayList<SocialNetwork>(ids.size());
		for(Long id : ids){
			try {
				accounts.add( getSocialNetworkById(id) );
			} catch (NotFoundException e) {
			}
		}
		return accounts;
	}
	
	public List<SocialNetwork> getSocialNetworks(int objectType, String username) {
		List<Long> ids = socialNetworkDao.getSocialAccountIds(objectType, username);
		List<SocialNetwork> accounts = new ArrayList<SocialNetwork>(ids.size());
		for(Long id : ids){
			try {
				accounts.add( getSocialNetworkById(id) );
			} catch (NotFoundException e) {
			}
		}
		return accounts;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void saveSocialNetwork(SocialNetwork socialNetwork) {
		
		if(socialNetwork.getSocialAccountId() <= 0 ){
			socialNetworkDao.createSocialAccount(socialNetwork);
		}else{
			socialNetworkDao.updateSocialAccount(socialNetwork);			
		}		
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void removeSocialNetwork(SocialNetwork socialNetwork) {
		socialNetworkDao.deleteSocialAccount(socialNetwork);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void updateSocialNetworkProperties(SocialNetwork socialNetwork) {
		if( socialNetwork.getSocialAccountId() > 0 ){
			Date now = new Date();
			socialNetwork.setModifiedDate(now);
			getSocialNetworkDao().updateSocialAccount(socialNetwork);
		}
	}
	
}

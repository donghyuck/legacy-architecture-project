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
package architecture.ee.web.social.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.ehcache.Cache;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import architecture.common.user.Company;
import architecture.common.user.User;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.social.SocialAccount;
import architecture.ee.web.social.SocialAccount.SocialMedia;
import architecture.ee.web.social.SocialAccountManager;
import architecture.ee.web.social.dao.SocialAccountDao;
import architecture.ee.web.social.twitter.TwitterServiceProvider;

public class DefaultSocialAccountManager implements SocialAccountManager {

	private SocialAccountDao socialAccountDao;
	private Cache socialAccountCache;
	
	public DefaultSocialAccountManager() {
	}

	
	
	/**
	 * @return socialAccountDao
	 */
	public SocialAccountDao getSocialAccountDao() {
		return socialAccountDao;
	}



	/**
	 * @param socialAccountDao 설정할 socialAccountDao
	 */
	public void setSocialAccountDao(SocialAccountDao socialAccountDao) {
		this.socialAccountDao = socialAccountDao;
	}



	/**
	 * @return socialAccountCache
	 */
	public Cache getSocialAccountCache() {
		return socialAccountCache;
	}



	/**
	 * @param socialAccountCache 설정할 socialAccountCache
	 */
	public void setSocialAccountCache(Cache socialAccountCache) {
		this.socialAccountCache = socialAccountCache;
	}

	public SocialAccount createSocialAccount(Company company, SocialMedia media) {
		SocialAccountImpl impl = new SocialAccountImpl(); 
		Date now = new Date();		
		impl.setCreationDate(now);
		impl.setModifiedDate(now);
		impl.setObjectType(company.getModelObjectType().getKey());
		impl.setObjectId(company.getCompanyId());
		impl.setServiceProviderName(media.name().toLowerCase());
		impl.setSocialAccountId(-1L);		
		
		if(media == SocialMedia.TWITTER ){
			impl.setSocialServiceProvider(
				new TwitterServiceProvider(
					"4XebpD1MW3CQ8Koh7naQpg",
					"aFlMLXe7fsyE3EnZtTp1LdAHRqEMROqOFW8ldQNYc"
				)
			);
		}		
		return impl;
	}

	public SocialAccount createSocialAccount(User user, SocialMedia media) {
		SocialAccountImpl impl = new SocialAccountImpl(); 
		Date now = new Date();		
		impl.setCreationDate(now);
		impl.setModifiedDate(now);
		impl.setObjectType(user.getModelObjectType().getKey());
		impl.setObjectId(user.getUserId());
		impl.setServiceProviderName(media.name().toLowerCase());
		impl.setSocialAccountId(-1L);		
		
		if(media == SocialMedia.TWITTER ){
			impl.setSocialServiceProvider(
				new TwitterServiceProvider(
					"4XebpD1MW3CQ8Koh7naQpg",
					"aFlMLXe7fsyE3EnZtTp1LdAHRqEMROqOFW8ldQNYc"
				)
			);
		}		
		return impl;
	}

	public SocialAccount getSocialAccountById(long socialAccountId)
			throws NotFoundException {
		SocialAccount account = socialAccountDao.getSocialAccountById(socialAccountId);
		return account;
	}

	public List<SocialAccount> getSocialAccounts(Company company) {
		List<Long> ids = socialAccountDao.getSocialAccountIds(company.getModelObjectType().getKey(), company.getCompanyId());
		List<SocialAccount> accounts = new ArrayList<SocialAccount>(ids.size());
		for(Long id : ids){
			try {
				accounts.add( getSocialAccountById(id) );
			} catch (NotFoundException e) {
			}
		}
		return accounts;
	}

	public List<SocialAccount> getSocialAccounts(User user) {
		List<Long> ids = socialAccountDao.getSocialAccountIds(user.getModelObjectType().getKey(), user.getUserId());
		List<SocialAccount> accounts = new ArrayList<SocialAccount>(ids.size());
		for(Long id : ids){
			try {
				accounts.add( getSocialAccountById(id) );
			} catch (NotFoundException e) {
			}
		}
		return accounts;
	}

	public List<SocialAccount> getSocialAccounts(int objectType, long objectId) {
		List<Long> ids = socialAccountDao.getSocialAccountIds(objectType, objectId);
		List<SocialAccount> accounts = new ArrayList<SocialAccount>(ids.size());
		for(Long id : ids){
			try {
				accounts.add( getSocialAccountById(id) );
			} catch (NotFoundException e) {
			}
		}
		return accounts;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void saveSocialAccount(SocialAccount socialAccount) {
		
		if(socialAccount.getSocialAccountId() <= 0 ){
			socialAccountDao.createSocialAccount(socialAccount);
		}else{
			socialAccountDao.updateSocialAccount(socialAccount);
		}
		
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void removeSocialAccount(SocialAccount socialAccount) {
		socialAccountDao.deleteSocialAccount(socialAccount);
	}
}

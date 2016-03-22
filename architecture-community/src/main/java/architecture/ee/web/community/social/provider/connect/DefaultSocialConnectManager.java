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
package architecture.ee.web.community.social.provider.connect;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import architecture.common.user.Company;
import architecture.common.user.User;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.community.social.provider.connect.SocialConnect.Media;
import architecture.ee.web.community.social.provider.connect.dao.SocialConnectDao;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class DefaultSocialConnectManager implements SocialConnectManager {

    private SocialConnectDao socialConnectDao;

    private Cache socialConnectCache;

    public DefaultSocialConnectManager() {

    }

    /**
     * @return socialConnectDao
     */
    public SocialConnectDao getSocialConnectDao() {
	return socialConnectDao;
    }

    /**
     * @param socialConnectDao
     *            설정할 socialConnectDao
     */
    public void setSocialConnectDao(SocialConnectDao socialConnectDao) {
	this.socialConnectDao = socialConnectDao;
    }

    /**
     * @return socialConnectCache
     */
    public Cache getSocialConnectCache() {
	return socialConnectCache;
    }

    /**
     * @param socialConnectCache
     *            설정할 socialConnectCache
     */
    public void setSocialConnectCache(Cache socialConnectCache) {
	this.socialConnectCache = socialConnectCache;
    }

    @Override
    public SocialConnect createSocialConnect(Company company, Media media) {
	DefaultSocialConnect impl = new DefaultSocialConnect(company.getModelObjectType(), company.getCompanyId(),
		media);
	return impl;
    }

    @Override
    public SocialConnect createSocialConnect(User user, Media media) {
	DefaultSocialConnect impl = new DefaultSocialConnect(user.getModelObjectType(), user.getUserId(), media);
	return impl;
    }

    @Override
    public List<SocialConnect> findSocialConnects(int objectType, long objectId) {
	List<Long> ids = socialConnectDao.getSocialConnectIds(objectType, objectId);
	List<SocialConnect> connects = new ArrayList<SocialConnect>(ids.size());
	for (Long id : ids) {
	    try {
		connects.add(getSocialConnectById(id));
	    } catch (NotFoundException e) {
	    }
	}
	return connects;
    }

    @Override
    public List<SocialConnect> findSocialConnects(Company company) {
	return findSocialConnects(company.getModelObjectType(), company.getCompanyId());
    }

    @Override
    public List<SocialConnect> findSocialConnects(User user) {
	return findSocialConnects(user.getModelObjectType(), user.getUserId());
    }

    @Override
    public SocialConnect getSocialConnectById(long socialConnectId) throws ConnectNotFoundException {
	SocialConnect conn = null;
	try {

	    if (socialConnectCache.get(socialConnectId) != null) {
		conn = (SocialConnect) socialConnectCache.get(socialConnectId).getValue();
	    }
	    if (conn == null) {
		conn = socialConnectDao.getSocialConnectById(socialConnectId);
		socialConnectCache.put(new Element(conn.getSocialConnectId(), conn));
	    }

	} catch (EmptyResultDataAccessException e) {
	    throw new ConnectNotFoundException(e);
	}
	return conn;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void updateSocialConnect(SocialConnect socialConnect) {
	socialConnectDao.updateSocialConnect(socialConnect);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void removeSocialConnect(SocialConnect socialConnect) {
	socialConnectDao.removeSocialConnect(socialConnect);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void updateSocialConnectProperties(SocialConnect socialConnect) {
	socialConnectDao.updateSocialConnect(socialConnect);
	clearCache(socialConnect);
    }

    private void clearCache(SocialConnect socialConnect) {
	socialConnectCache.remove(socialConnect.getSocialConnectId());
    }

    public SocialConnect getSocialConnect(int objectType, long objectId, String providerId)
	    throws ConnectNotFoundException {
	Long id;
	try {
	    id = socialConnectDao.getSocialConnectId(objectType, objectId, providerId);
	} catch (EmptyResultDataAccessException e) {
	    throw new ConnectNotFoundException(e);
	}
	return getSocialConnectById(id);
    }

    public SocialConnect getSocialConnect(Company company, String providerId) throws ConnectNotFoundException {
	return getSocialConnect(company.getModelObjectType(), company.getCompanyId(), providerId);
    }

    @Override
    public SocialConnect getSocialConnect(User user, String providerId) throws ConnectNotFoundException {
	return getSocialConnect(user.getModelObjectType(), user.getUserId(), providerId);
    }

    @Override
    public SocialConnect createSocialConnect(SocialConnect socialConnect) {
	socialConnectDao.addSocialConnect(socialConnect);
	return socialConnect;
    }

    @Override
    public SocialConnect createSocialConnect(User user, Connection<?> connection) {
	ConnectionData data = connection.createData();
	Date now = new Date();
	DefaultSocialConnect socialConnect = new DefaultSocialConnect(-1L, user.getModelObjectType(), user.getUserId(),
		data.getProviderId(), data.getProviderUserId(), data.getDisplayName(), data.getProfileUrl(),
		data.getImageUrl(), data.getAccessToken(), data.getSecret(), data.getRefreshToken(),
		data.getExpireTime(), now, now);
	socialConnectDao.addSocialConnect(socialConnect);
	return socialConnect;
    }

    @Override
    public List<SocialConnect> findSocialConnects(int objectType, String providerId, String providerUserId) {
	List<Long> ids = socialConnectDao.getSocialConnectIds(objectType, providerId, providerUserId);
	List<SocialConnect> connects = new ArrayList<SocialConnect>(ids.size());
	for (Long id : ids) {
	    try {
		connects.add(getSocialConnectById(id));
	    } catch (NotFoundException e) {
	    }
	}
	return connects;
    }
}
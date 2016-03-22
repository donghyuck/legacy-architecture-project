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

import java.util.List;

import org.springframework.social.connect.Connection;

import architecture.common.user.Company;
import architecture.common.user.User;
import architecture.ee.web.community.social.provider.connect.SocialConnect.Media;

public interface SocialConnectManager {

    public abstract SocialConnect createSocialConnect(Company company, Media media);

    public abstract SocialConnect createSocialConnect(User user, Media media);

    public abstract SocialConnect createSocialConnect(User user, Connection<?> connection);

    public abstract SocialConnect createSocialConnect(SocialConnect socialConnect);

    public abstract List<SocialConnect> findSocialConnects(int objectType, long objectId);

    public abstract List<SocialConnect> findSocialConnects(Company company);

    public abstract List<SocialConnect> findSocialConnects(User user);

    public abstract List<SocialConnect> findSocialConnects(int objectType, String providerId, String providerUserId);

    public abstract SocialConnect getSocialConnect(int objectType, long objectId, String providerId)
	    throws ConnectNotFoundException;

    public abstract SocialConnect getSocialConnect(Company company, String providerId) throws ConnectNotFoundException;

    public abstract SocialConnect getSocialConnect(User user, String providerId) throws ConnectNotFoundException;

    public abstract SocialConnect getSocialConnectById(long socialConnectId) throws ConnectNotFoundException;

    public abstract void updateSocialConnect(SocialConnect socialConnect);

    public abstract void removeSocialConnect(SocialConnect socialConnect);

    public abstract void updateSocialConnectProperties(SocialConnect socialConnect);

}

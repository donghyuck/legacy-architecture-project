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
package architecture.ee.web.community.social.provider.connect.dao;

import java.util.List;

import architecture.ee.web.community.social.provider.connect.SocialConnect;

public interface SocialConnectDao {

    public List<Long> getSocialConnectIds(int objectType, long objectId);

    public List<Long> getSocialConnectIds(int objectType, String providerId, String providerUserId);

    public Long getSocialConnectId(int objectType, long objectId, String providerId);

    public SocialConnect getSocialConnectById(long socialConnectId);

    public void addSocialConnect(SocialConnect socialConnect);

    public void updateSocialConnect(SocialConnect socialConnect);

    public void removeSocialConnect(SocialConnect socialNetwork);

}

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
package architecture.user.dao;

import java.util.Map;

import architecture.common.user.User;

public interface ExternalUserProfileDao {

    public abstract Map<Long, Object> getProfile(User user);

    public abstract Map<String, Object> getProfile(long userId);

    public abstract void setProfile(User user, Map<String, Object> profile);

    public abstract void setProfile(long userId, Map<String, Object> profile);

}

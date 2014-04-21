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
package architecture.ee.web.community.profile.dao;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import architecture.ee.web.community.profile.ProfileImage;
import architecture.ee.web.community.profile.ProfileImageNotFoundException;

public interface ProfileDao {

	public void removeProfileImage(ProfileImage image);
	
	public void addProfileImage(ProfileImage image, File file);
	
	public ProfileImage getProfileImageById(Long profileImageId) throws ProfileImageNotFoundException ;
	
	public Long getPrimaryProfileImageByUser(Long userId) throws ProfileImageNotFoundException;
	
	public List<Long> getProfileImageIds(Long userId);
	
	public Integer getProfileImageCount(Long userId);
	
	public InputStream getInputStream(ProfileImage image);

}

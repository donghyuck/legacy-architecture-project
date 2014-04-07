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
package architecture.ee.web.community.announce;

import java.util.Date;
import java.util.List;

import architecture.common.user.User;

public interface AnnounceManager {
	
	public abstract Announce createAnnounce(User user);
	
	public abstract Announce createAnnounce(User user, int objectType, long objectId);
	
	public abstract void addAnnounce(Announce announce);
	
	public abstract void updateAnnounce(Announce announce);
	
	public abstract Announce getAnnounce(long announceId) throws AnnounceNotFoundException ;
	
	public abstract List<Announce> getAnnounces(int objectType, long objectId);
	
	public abstract List<Announce> getAnnounces(int objectType, long objectId , Date startDate, Date endDate); 
	
	public abstract void deleteAnnounce(long announceId);
	
	public abstract void deleteUserAnnounces( User user);
	
	public abstract void moveAnnounces( int fromObjectType, int toObjectType);
	
	public abstract void moveAnnounces( int fromObjectType, long fromObjectId,  int toObjectType, long toObjectId );
	
	public abstract int countAnnounce(int objectType, long objectId);
	
}

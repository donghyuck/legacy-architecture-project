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

    public abstract Announce getAnnounce(long announceId) throws AnnounceNotFoundException;



    public abstract void deleteAnnounce(long announceId);

    public abstract void deleteUserAnnounces(User user);

    public abstract void moveAnnounces(int fromObjectType, int toObjectType);

    public abstract void moveAnnounces(int fromObjectType, long fromObjectId, int toObjectType, long toObjectId);

    /**
     * 
     * @param objectType
     * @param objectId
     * @return
     */
    public abstract List<Announce> getAnnounces(int objectType, long objectId);

    /**
     * 
     * startDate 일자보다 시작일이 적거나 같고 
     * endDate 일자보다 종료일이 크거나 같은 경우에 
     * 해당하는 이벤트를 리턴한다. 즉 특정기간동안 유효한 이벤트들을 검색하는 목적이다.  
     * 
     * @param objectType
     * @param objectId
     * @param startDate
     * @param endDate
     * @return
     */
    public abstract List<Announce> getAnnounces(int objectType, long objectId, Date startDate, Date endDate);
    

    public abstract int countAnnounce(int objectType, long objectId);

    
    /**
     * 
     * @param objectType
     * @param objectId
     * @param endDate
     * @return
     */
    public abstract int getAnnounceCount(int objectType, long objectId, Date endDate);

    
    
    /**
     * 
     * 
     * @param objectType
     * @param objectId
     * @param startDate
     * @param endDate
     * @return
     */
    public abstract int getAnnounceCount(int objectType, long objectId, Date startDate, Date endDate);

}

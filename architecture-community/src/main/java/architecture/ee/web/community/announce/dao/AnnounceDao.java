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
package architecture.ee.web.community.announce.dao;

import java.util.Date;
import java.util.List;

import architecture.ee.web.community.announce.Announce;
import architecture.ee.web.community.announce.AnnounceNotFoundException;

public interface AnnounceDao {
	
	public abstract Announce load(long announceId) throws AnnounceNotFoundException ;
	
	public abstract Long nextId();
	
	public abstract void update( Announce annoucne );
	
	public abstract void insert( Announce annoucne );
	
	public abstract void delete( Announce annoucne );
	
	public abstract void move( Long fromId, Long toId);
	
	public abstract List<Long> getAnnounceIdsForUser ( long userId );
	
	public abstract List<Long> getAnnounceIds ( int objectType, long objectId );
	
	public abstract List<Long> getAnnounceIds ();
	
	public abstract int getAnnounceCount ( int objectType, long objectId );
	
	public abstract int getAnnounceCount ( int objectType, long objectId, Date endDate );
	
}

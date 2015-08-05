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
package architecture.ee.web.community.poll;

import java.util.Date;
import java.util.List;

import architecture.common.model.ModelObject;
import architecture.common.user.User;
import architecture.ee.web.community.model.ContentObject.Status;

public interface Poll extends ModelObject {

	public static final long ALLOW_USER_VOTE_MODIFICATION = 16L;
	public static final long ALLOW_ANONYMOUS_VOTE_MODIFICATION = 32L;
	public static final long MULTIPLE_SELECTIONS_ALLOWED = 256L;

	public abstract long getPollId();

	public abstract int getCommentStatus();

	public abstract Date getCreationDate();

	public abstract String getDescription();

	public abstract Date getEndDate();

	public abstract Date getExpireDate();

	public abstract Date getModifiedDate();
	
	public abstract void setModifiedDate(Date modifiedDate);

	public abstract String getName();

	public abstract long getObjectId();

	public abstract int getObjectType();

	public abstract List<PollOption> getOptions();

	public abstract Date getStartDate();

	public abstract Status getStatus();

	public abstract User getUser();

	public abstract boolean isModeEnabled(long mode);
	
	public abstract long getMode();
	
	public abstract void setOptions(List<PollOption> options) ;
	
}

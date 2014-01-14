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
package architecture.ee.web.community.dao;

import java.util.Date;
import java.util.List;

import architecture.ee.web.community.Comment;
import architecture.ee.web.community.CommentNotFoundException;

public interface CommentDao {

	public abstract void createComment(Comment commnet);
	
	public abstract void updateComment(Comment commnet);
	
	public abstract void deleteComment(Comment commnet);
	
	public abstract Comment getCommentById(Long id);
	
	public abstract int getCommentCount(int objectType, long objectId );
	
	public abstract List<Long> getCommentIds(int objectType, long objectId );
	
	public abstract List<Long> getCommentIds(int objectType, long objectId , int startIndex, int maxResults );
	
	public abstract List<Comment> getUserComments(Long id) throws CommentNotFoundException ;
	
	public abstract List<Comment> getCommentsUpdatedAfter(Date date);	 
	 
}

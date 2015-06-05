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
package architecture.ee.web.community.comment;

import java.util.List;



public interface CommentTreeWalker extends  java.io.Serializable {

    public abstract int getCommentDepth(Comment comment);

    public abstract int getChildCount(Comment comment);

    public abstract int getRecursiveChildCount(Comment comment);

    public abstract int getIndexOfChild(Comment comment, Comment comment1);

    public abstract boolean isLeaf(Comment comment);
    
    public abstract List<Comment> topLevelComments();
    
	public abstract Comment getParent(Comment comment)
			throws CommentNotFoundException;

	public abstract Comment getChild(Comment comment, int i)
			throws CommentNotFoundException;

	public abstract List<Comment> children(Comment comment);
	
	public abstract List<Comment> recursiveChildren(Comment comment);	
	
}

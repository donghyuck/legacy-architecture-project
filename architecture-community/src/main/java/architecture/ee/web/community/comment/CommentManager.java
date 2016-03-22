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

import architecture.common.user.User;
import architecture.common.user.authentication.UnAuthorizedException;

public interface CommentManager {

    public static final int COMMENTS_NONE = 1;
    public static final int COMMENTS_OPEN = 2;
    public static final int COMMENTS_CLOSED = 3;
    public static final int COMMENTS_MODERATED = 4;
    public static final int COMMENTS_DEFAULT = 2;

    public abstract Comment getRootParent();

    public abstract Comment getComment(long commentId) throws CommentNotFoundException;

    public abstract void update(Comment comment) throws UnAuthorizedException;

    public abstract void setBody(Comment comment, String text);

    public abstract void addComment(Comment comment) throws UnAuthorizedException;

    public abstract Comment createComment(int objectType, long objectId, User user, String text)
	    throws UnAuthorizedException;

    public CommentTreeWalker getCommentTreeWalker(int objectType, long objectId);

}

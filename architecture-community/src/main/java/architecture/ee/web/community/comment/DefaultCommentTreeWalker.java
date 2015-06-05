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

import java.util.ArrayList;
import java.util.List;

import architecture.common.util.LongTree;
import architecture.ee.util.ApplicationHelper;


public class DefaultCommentTreeWalker implements CommentTreeWalker  {
	
	private int objectType = -1;
	private long objectId = -1L;
	private LongTree tree;
	
	private static final Comment TOP_COMMENT = new DefaultComment();
	
	/**
	 * @return objectType
	 */
	public int getObjectType() {
		return objectType;
	}
	/**
	 * @param objectType
	 * @param objectId
	 * @param tree
	 */
	public DefaultCommentTreeWalker(int objectType, long objectId, LongTree tree) {
		this.objectType = objectType;
		this.objectId = objectId;
		this.tree = tree;
	}
	/**
	 * @param objectType 설정할 objectType
	 */
	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}
	/**
	 * @return objectId
	 */
	public long getObjectId() {
		return objectId;
	}
	/**
	 * @param objectId 설정할 objectId
	 */
	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}
	/**
	 * @return tree
	 */
	public LongTree getTree() {
		return tree;
	}
	/**
	 * @param tree 설정할 tree
	 */
	public void setTree(LongTree tree) {
		this.tree = tree;
	}
	

	protected long[] getCommentIds( Comment parent ) {
		return tree.getChildren(parent.getCommentId());
	}
	
	protected long[] getRecursiveCommentIds( Comment parent ) {
		return tree.getRecursiveChildren(parent.getCommentId());
	}	
	
	public int getCommentDepth(Comment comment) {
		int depth = tree.getDepth(comment.getCommentId());
		if (depth == -1)
			throw new IllegalArgumentException((new StringBuilder())
					.append("Comment ").append(comment.getCommentId())
					.append(" does not belong to this document.").toString());
		else
			return depth - 1;
	}
	
	public int getRecursiveChildCount(Comment parent) {
		return getRecursiveChildCount(parent.getCommentId());
	}

	
	public int getIndexOfChild(Comment parent, Comment child) {
		return tree.getIndexOfChild(parent.getCommentId(), child.getCommentId());
	}

	public boolean isLeaf(Comment comment) {
		return tree.isLeaf(comment.getCommentId());
	}

	private int getRecursiveChildCount(long parentId) {
		int numChildren = 0;
		int num = tree.getChildCount(parentId);
		numChildren += num;
		for (int i = 0; i < num; i++) {
			long childID = tree.getChild(parentId, i);
			if (childID != -1L)
				numChildren += getRecursiveChildCount(childID);
		}
		return numChildren;
	}
	@Override
	public int getChildCount(Comment comment) {
		return tree.getChildCount(comment.getCommentId());
	}
	@Override
	public List<Comment> topLevelComments() {
		return children(this.TOP_COMMENT);
	}
	
	@Override
	public Comment getParent(Comment comment) throws CommentNotFoundException {
		long parentId = tree.getParent(comment.getCommentId());
		if (parentId == -1L) {
			return null;
		} else {
			CommentManager mgr = ApplicationHelper.getComponent(CommentManager.class);
			return mgr.getComment(parentId);
		}
	}
	
	@Override
	public Comment getChild(Comment comment, int index)
			throws CommentNotFoundException {
		long childId = tree.getChild(comment.getCommentId(), index);
		if (childId == -1L) {
			return null;
		} else {
			CommentManager mgr = ApplicationHelper.getComponent(CommentManager.class);
			return mgr.getComment(childId);
		}
	}
	@Override
	public List<Comment> children(Comment comment) {
		long children[] = tree.getChildren(comment.getCommentId());
		List<Comment> list = new ArrayList<Comment>();
		CommentManager mgr = ApplicationHelper.getComponent(CommentManager.class);
		for( long childId : children )
			try {
				list.add(mgr.getComment(childId));
			} catch (CommentNotFoundException e) {
			}
		return list;
	}
	
	@Override
	public List<Comment> recursiveChildren(Comment comment) {
		 long comments[] = tree.getRecursiveChildren(comment.getCommentId());
		 List<Comment> list = new ArrayList<Comment>();
		 CommentManager mgr = ApplicationHelper.getComponent(CommentManager.class);
			for( long commentId : comments )
				try {
					list.add(mgr.getComment(commentId));
				} catch (CommentNotFoundException e) {
				}
			return list;
	}
}

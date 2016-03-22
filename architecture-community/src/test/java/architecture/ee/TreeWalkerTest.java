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
package architecture.ee;

import org.junit.Test;

import architecture.common.util.LongTree;
import architecture.ee.web.community.comment.Comment;
import architecture.ee.web.community.comment.DefaultComment;
import architecture.ee.web.community.comment.DefaultCommentTreeWalker;

public class TreeWalkerTest {
    private static final Comment TOP_COMMENT = new DefaultComment();

    public TreeWalkerTest() {
	// TODO 자동 생성된 생성자 스텁
    }

    @Test
    public void createTreeWalker() {

	int total = 2;
	total++;
	final LongTree tree = new LongTree(-1L, total);
	DefaultCommentTreeWalker walker = new DefaultCommentTreeWalker(1, 1, tree);
	tree.addChild(-1L, 51);
	tree.addChild(-1L, 52);

	System.out.println(tree.toString());
	System.out.println(total);
	System.out.println(walker.getRecursiveChildCount(TOP_COMMENT));
    }

}

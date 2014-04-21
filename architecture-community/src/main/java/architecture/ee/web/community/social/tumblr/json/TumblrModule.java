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
package architecture.ee.web.community.social.tumblr.json;
import architecture.ee.web.community.social.tumblr.BlogInfo;
import architecture.ee.web.community.social.tumblr.Post;
import architecture.ee.web.community.social.tumblr.Posts;
import architecture.ee.web.community.social.tumblr.UserInfo;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class TumblrModule extends SimpleModule {
    public TumblrModule() {
        super("TumblrModule", new Version(1, 0, 0, null, "architecture.ee.web.community", "social-tumblr"));
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(TumblrResponse.class, TumblrResponseMixin.class);
        context.setMixInAnnotations(UserInfoResponse.class, UserInfoResponseMixin.class);
        context.setMixInAnnotations(BlogInfo.class, BlogInfoMixin.class);
        context.setMixInAnnotations(UserInfo.class, UserInfoMixin.class);
        context.setMixInAnnotations(Post.class, PostMixin.class);
        context.setMixInAnnotations(Posts.class, PostsMixin.class);
    }
}

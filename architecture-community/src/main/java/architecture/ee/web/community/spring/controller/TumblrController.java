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
package architecture.ee.web.community.spring.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.social.tumblr.api.AvatarSize;
import org.springframework.social.tumblr.api.BlogOperations;
import org.springframework.social.tumblr.api.Following;
import org.springframework.social.tumblr.api.Likes;
import org.springframework.social.tumblr.api.Posts;
import org.springframework.social.tumblr.api.PostsQuery;
import org.springframework.social.tumblr.api.Tumblr;
import org.springframework.social.tumblr.api.UserInfo;
import org.springframework.social.tumblr.api.UserOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import architecture.common.user.SecurityHelper;
import architecture.common.user.User;
import architecture.ee.web.community.social.provider.connect.ConnectNotFoundException;
import architecture.ee.web.community.social.provider.connect.SocialConnect;
import architecture.ee.web.community.social.provider.connect.SocialConnect.Media;
import architecture.ee.web.community.social.provider.connect.SocialConnectManager;

@Controller
@RequestMapping("/connect/tumblr")
public class TumblrController {

    private static final Log log = LogFactory.getLog(TumblrController.class);

    @Autowired
    @Qualifier("socialConnectManager")
    private SocialConnectManager socialConnectManager;

    public TumblrController() {
    }

    @RequestMapping(value = "/user/lookup.json", method = RequestMethod.POST)
    @ResponseBody
    public UserInfo lookupUser() throws Exception {

	SocialConnect account = getSocialConnect(SecurityHelper.getUser(), Media.TUMBLR);
	Tumblr api = (Tumblr) account.getConnection().getApi();
	UserOperations userOperations = api.userOperations();
	return userOperations.info();
    }

    @RequestMapping(value = "/following.json", method = RequestMethod.POST)
    @ResponseBody
    public Following following() throws Exception {
	SocialConnect account = getSocialConnect(SecurityHelper.getUser(), Media.TUMBLR);
	Tumblr api = (Tumblr) account.getConnection().getApi();
	return api.userOperations().following();
    }

    @RequestMapping(value = "/likes.json", method = RequestMethod.POST)
    @ResponseBody
    public Likes likes() throws Exception {
	SocialConnect account = getSocialConnect(SecurityHelper.getUser(), Media.TUMBLR);
	Tumblr api = (Tumblr) account.getConnection().getApi();
	return api.userOperations().likes();
    }

    @RequestMapping(value = "/dashboard.json", method = RequestMethod.POST)
    @ResponseBody
    public Posts dashboard(@RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset)
	    throws Exception {
	SocialConnect account = getSocialConnect(SecurityHelper.getUser(), Media.TUMBLR);
	Tumblr api = (Tumblr) account.getConnection().getApi();
	if (offset > 0) {
	    PostsQuery query = new PostsQuery();
	    query.setOffset(offset);
	    // query.setSinceId(offset);
	    return api.userOperations().dashboard(query);
	}
	return api.userOperations().dashboard();
    }

    @RequestMapping(value = "/{blogHostname}/avatar", method = RequestMethod.GET)
    @ResponseBody
    public RedirectView blog(@PathVariable("blogHostname") String blogHostname,
	    @RequestParam(value = "size", defaultValue = "SMALL", required = false) String size) throws Exception {
	SocialConnect account = getSocialConnect(SecurityHelper.getUser(), Media.TUMBLR);
	Tumblr api = (Tumblr) account.getConnection().getApi();

	BlogOperations bo = api.blogOperations(blogHostname + ".tumblr.com");
	String url = bo.avatar(AvatarSize.SMALL);
	log.debug("info:" + bo.info().getUrl());
	log.debug("blogHostname:" + blogHostname);
	log.debug(url);

	return new RedirectView(url, true);
    }

    protected SocialConnect getSocialConnect(User user, Media media) throws ConnectNotFoundException {
	return socialConnectManager.getSocialConnect(user, media.name().toLowerCase());
    }

}

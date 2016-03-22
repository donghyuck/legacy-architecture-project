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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.UserOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import architecture.common.user.SecurityHelper;
import architecture.common.user.User;
import architecture.ee.web.community.social.provider.connect.ConnectNotFoundException;
import architecture.ee.web.community.social.provider.connect.SocialConnect;
import architecture.ee.web.community.social.provider.connect.SocialConnect.Media;
import architecture.ee.web.community.social.provider.connect.SocialConnectManager;

@Controller
@RequestMapping("/connect/twitter")
public class TwitterController {

    @Autowired
    @Qualifier("socialConnectManager")
    private SocialConnectManager socialConnectManager;

    public TwitterController() {
    }

    @RequestMapping(value = "/user/lookup.json", method = RequestMethod.POST)
    @ResponseBody
    public TwitterProfile lookupUser(
	    @RequestParam(value = "userId", defaultValue = "0", required = false) Integer userId,
	    @RequestParam(value = "screenName", defaultValue = "", required = false) String screenName,
	    NativeWebRequest request, Model model) throws Exception {

	SocialConnect account = getSocialConnect(SecurityHelper.getUser(), Media.TWITTER);
	Twitter api = (Twitter) account.getConnection().getApi();

	UserOperations userOperations = api.userOperations();
	TwitterProfile profile = null;
	if (profile == null && userId > 0) {
	    profile = userOperations.getUserProfile(userId);
	}
	if (profile == null && StringUtils.hasText(screenName)) {
	    profile = userOperations.getUserProfile(screenName);
	}
	if (profile == null) {
	    profile = userOperations.getUserProfile();
	}
	return profile;
    }

    @RequestMapping(value = "/user_timeline.json", method = RequestMethod.POST)
    @ResponseBody
    public List<Tweet> getUserTimeline() throws Exception {
	SocialConnect account = getSocialConnect(SecurityHelper.getUser(), Media.TWITTER);
	Twitter api = (Twitter) account.getConnection().getApi();
	return api.timelineOperations().getUserTimeline();
    }

    @RequestMapping(value = "/home_timeline.json", method = RequestMethod.POST)
    @ResponseBody
    public List<Tweet> getHomeTimeline() throws Exception {
	SocialConnect account = getSocialConnect(SecurityHelper.getUser(), Media.TWITTER);
	Twitter api = (Twitter) account.getConnection().getApi();
	return api.timelineOperations().getHomeTimeline();
    }

    @RequestMapping(value = "/favorites.json", method = RequestMethod.POST)
    @ResponseBody
    public List<Tweet> getFavorites() throws Exception {
	SocialConnect account = getSocialConnect(SecurityHelper.getUser(), Media.TWITTER);
	Twitter api = (Twitter) account.getConnection().getApi();
	return api.timelineOperations().getFavorites();
    }

    protected SocialConnect getSocialConnect(User user, Media media) throws ConnectNotFoundException {
	return socialConnectManager.getSocialConnect(user, media.name().toLowerCase());
    }

}

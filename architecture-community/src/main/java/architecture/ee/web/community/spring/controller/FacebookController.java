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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.NotePost;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Photo;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.api.UserOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/connect/facebook")
public class FacebookController {

    @Autowired
    @Qualifier("socialConnectManager")
    private SocialConnectManager socialConnectManager;

    public FacebookController() {
    }

    @RequestMapping(value = "/user/lookup.json", method = RequestMethod.POST)
    @ResponseBody
    public FacebookProfile lookupUser(
	    @RequestParam(value = "userId", defaultValue = "", required = false) String userId) throws Exception {

	SocialConnect account = getSocialConnect(SecurityHelper.getUser(), Media.FACEBOOK);
	Facebook api = (Facebook) account.getConnection().getApi();
	UserOperations userOperations = api.userOperations();
	if (StringUtils.isNotBlank(userId))
	    return userOperations.getUserProfile(userId);
	else
	    return userOperations.getUserProfile();
    }

    @RequestMapping(value = "/photo/{photoId}", method = RequestMethod.GET)
    @ResponseBody
    public Object getPhoto(@PathVariable("photoId") String photoId) throws Exception {
	SocialConnect account = getSocialConnect(SecurityHelper.getUser(), Media.FACEBOOK);
	Facebook api = (Facebook) account.getConnection().getApi();
	Photo photo = api.mediaOperations().getPhoto(photoId);
	return photo;
    }

    @RequestMapping(value = "/friends.json", method = RequestMethod.POST)
    @ResponseBody
    public List<FacebookProfile> getFriendProfiles() throws Exception {
	SocialConnect account = getSocialConnect(SecurityHelper.getUser(), Media.FACEBOOK);
	Facebook api = (Facebook) account.getConnection().getApi();
	return api.friendOperations().getFriendProfiles();
    }

    @RequestMapping(value = "/feed.json", method = RequestMethod.POST)
    @ResponseBody
    public List<Post> getFeed(@RequestParam(value = "userId", defaultValue = "", required = false) String userId)
	    throws Exception {
	SocialConnect account = getSocialConnect(SecurityHelper.getUser(), Media.FACEBOOK);
	Facebook api = (Facebook) account.getConnection().getApi();
	if (StringUtils.isNotBlank(userId))
	    return api.feedOperations().getFeed(userId);
	else
	    return api.feedOperations().getFeed();
    }

    @RequestMapping(value = "/homefeed.json", method = RequestMethod.POST)
    @ResponseBody
    public PagedList<Post> getHomeFeed(NativeWebRequest request) throws Exception {
	SocialConnectController.setOutputFormat(request);
	SocialConnect account = getSocialConnect(SecurityHelper.getUser(), Media.FACEBOOK);
	Facebook api = (Facebook) account.getConnection().getApi();
	return api.feedOperations().getHomeFeed();
    }

    @RequestMapping(value = "/notes.json", method = RequestMethod.POST)
    @ResponseBody
    public PagedList<NotePost> getNotes() throws Exception {
	SocialConnect account = getSocialConnect(SecurityHelper.getUser(), Media.FACEBOOK);
	Facebook api = (Facebook) account.getConnection().getApi();
	return api.feedOperations().getNotes();
    }

    protected SocialConnect getSocialConnect(User user, Media media) throws ConnectNotFoundException {
	return socialConnectManager.getSocialConnect(user, media.name().toLowerCase());
    }

    private void setOutputFormat(NativeWebRequest request) {
	HttpServletRequest httprequest = request.getNativeRequest(HttpServletRequest.class);
	HttpServletResponse httpresponse = request.getNativeResponse(HttpServletResponse.class);
	httprequest.setAttribute("output", "json");
    }

}

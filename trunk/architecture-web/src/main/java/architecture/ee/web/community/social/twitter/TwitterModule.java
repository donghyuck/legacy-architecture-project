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
package architecture.ee.web.community.social.twitter;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;


class TwitterModule extends SimpleModule {

	public TwitterModule() {
		super("TwitterModule", new Version(1, 0, 0, null));
	}
	
	@Override
	public void setupModule(SetupContext context) {
		context.setMixInAnnotations(TwitterProfile.class, TwitterProfileMixin.class);
	//	context.setMixInAnnotations(SavedSearch.class, SavedSearchMixin.class);
	//	context.setMixInAnnotations(Trend.class, TrendMixin.class);
	//	context.setMixInAnnotations(Trends.class, TrendsMixin.class);
	//	context.setMixInAnnotations(SuggestionCategory.class, SuggestionCategoryMixin.class);
	//	context.setMixInAnnotations(DirectMessage.class, DirectMessageMixin.class);
	//	context.setMixInAnnotations(UserList.class, UserListMixin.class);
		context.setMixInAnnotations(Tweet.class, TweetMixin.class);
	//	context.setMixInAnnotations(SearchResults.class, SearchResultsMixin.class);
	//	context.setMixInAnnotations(Place.class, PlaceMixin.class);
	//	context.setMixInAnnotations(SimilarPlacesResponse.class, SimilarPlacesMixin.class);
		context.setMixInAnnotations(Entities.class, EntitiesMixin.class);
		context.setMixInAnnotations(HashTagEntity.class, HashTagEntityMixin.class);
		context.setMixInAnnotations(MediaEntity.class, MediaEntityMixin.class);
		context.setMixInAnnotations(MentionEntity.class, MentionEntityMixin.class);
		context.setMixInAnnotations(UrlEntity.class, UrlEntityMixin.class);
	}

}

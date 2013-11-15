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
package architecture.ee.web.social.facebook;

import architecture.ee.web.social.facebook.Photo.Image;
import architecture.ee.web.social.facebook.PhotoMixin.ImageMixin;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;


public class FacebookModule extends SimpleModule {

	public FacebookModule() {
		super("FacebookModule", new Version(1, 0, 0, null));
	}
	
	@Override
	public void setupModule(SetupContext context) {
		context.setMixInAnnotations(FacebookProfile.class, FacebookProfileMixin.class);
		context.setMixInAnnotations(WorkEntry.class, WorkEntryMixin.class);
		context.setMixInAnnotations(EducationEntry.class, EducationEntryMixin.class);
		context.setMixInAnnotations(Reference.class, ReferenceMixin.class);
		//context.setMixInAnnotations(GroupMemberReference.class, GroupMemberReferenceMixin.class);
		//context.setMixInAnnotations(Album.class, AlbumMixin.class);
		//context.setMixInAnnotations(Group.class, GroupMixin.class);
		//context.setMixInAnnotations(Event.class, EventMixin.class);
		//context.setMixInAnnotations(Invitation.class, InvitationMixin.class);
		//context.setMixInAnnotations(EventInvitee.class, EventInviteeMixin.class);
		//context.setMixInAnnotations(Checkin.class, CheckinMixin.class);
		context.setMixInAnnotations(Page.class, PageMixin.class);
		context.setMixInAnnotations(Location.class, LocationMixin.class);
		context.setMixInAnnotations(Comment.class, CommentMixin.class);
		context.setMixInAnnotations(Tag.class, TagMixin.class);
		context.setMixInAnnotations(Video.class, VideoMixin.class);
		context.setMixInAnnotations(Photo.class, PhotoMixin.class);
		context.setMixInAnnotations(Image.class, ImageMixin.class);
		context.setMixInAnnotations(Post.class, PostMixin.class);
		context.setMixInAnnotations(CheckinPost.class, CheckinPostMixin.class);
		context.setMixInAnnotations(LinkPost.class, LinkPostMixin.class);
		context.setMixInAnnotations(NotePost.class, NotePostMixin.class);
		context.setMixInAnnotations(PhotoPost.class, PhotoPostMixin.class);
		context.setMixInAnnotations(StatusPost.class, StatusPostMixin.class);
		context.setMixInAnnotations(VideoPost.class, VideoPostMixin.class);
		context.setMixInAnnotations(Account.class, AccountMixin.class);
		context.setMixInAnnotations(SwfPost.class, SwfPostMixin.class);
		context.setMixInAnnotations(MusicPost.class, MusicPostMixin.class);
		//context.setMixInAnnotations(GroupMembership.class, GroupMembershipMixin.class);
		context.setMixInAnnotations(CoverPhoto.class, CoverPhotoMixin.class);
	}
}


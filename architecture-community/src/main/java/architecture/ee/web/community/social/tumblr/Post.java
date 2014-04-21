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
package architecture.ee.web.community.social.tumblr;

import java.io.Serializable;
import java.util.List;

public class Post  implements Serializable  {

	private long id;
	private String blogName;
	private PostType type;
	private String postUrl;
	private long timestamp;
	private String gmtDateString;
	private String format;
	private String reblogKey;
	private List<String> tags;
	private int noteCount;
	private boolean createdViaBookmarklet;
	private boolean createdViaMobile;
	private String sourceUrl;
	private String sourceTitle;
	private boolean liked;
	private PostState state;

	// text type, link type, chat type
	private String title;
	// text type, chat type
	private String body;

	// photo type, audio type, video type
	private String caption;
	// photo type
	private int width;
	// photo type
	private int height;

	// photo type, photoset
	private List<Photo> photos;

	// quote type
	private String text;
	// quote type
	private String source;

	// link type
	private String url;
	// link type
	private String description;

	// chat type
	private List<ChatMessage> dialogue;

	// audio type
	private String audioPlayer;
	// audio type
	private int plays;
	// audio type
	private String albumArt;
	// audio type
	private String artist;
	// audio type
	private String album;
	// audio type
	private String trackName;
	// audio type
	private int trackNumber;
	// audio type
	private int year;

	// video type
	private List<VideoPostPlayer> videoPlayers;

    private String askingName;
    private String askingUrl;
    private String question;
    private String answer;
    
	/**
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            설정할 id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return blogName
	 */
	public String getBlogName() {
		return blogName;
	}

	/**
	 * @param blogName
	 *            설정할 blogName
	 */
	public void setBlogName(String blogName) {
		this.blogName = blogName;
	}

	/**
	 * @return type
	 */
	public PostType getType() {
		return type;
	}

	/**
	 * @param type
	 *            설정할 type
	 */
	public void setType(PostType type) {
		this.type = type;
	}

	/**
	 * @return postUrl
	 */
	public String getPostUrl() {
		return postUrl;
	}

	/**
	 * @param postUrl
	 *            설정할 postUrl
	 */
	public void setPostUrl(String postUrl) {
		this.postUrl = postUrl;
	}

	/**
	 * @return timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            설정할 timestamp
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return gmtDateString
	 */
	public String getGmtDateString() {
		return gmtDateString;
	}

	/**
	 * @param gmtDateString
	 *            설정할 gmtDateString
	 */
	public void setGmtDateString(String gmtDateString) {
		this.gmtDateString = gmtDateString;
	}

	/**
	 * @return format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format
	 *            설정할 format
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @return reblogKey
	 */
	public String getReblogKey() {
		return reblogKey;
	}

	/**
	 * @param reblogKey
	 *            설정할 reblogKey
	 */
	public void setReblogKey(String reblogKey) {
		this.reblogKey = reblogKey;
	}

	/**
	 * @return tags
	 */
	public List<String> getTags() {
		return tags;
	}

	/**
	 * @param tags
	 *            설정할 tags
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	/**
	 * @return noteCount
	 */
	public int getNoteCount() {
		return noteCount;
	}

	/**
	 * @param noteCount
	 *            설정할 noteCount
	 */
	public void setNoteCount(int noteCount) {
		this.noteCount = noteCount;
	}

	/**
	 * @return createdViaBookmarklet
	 */
	public boolean isCreatedViaBookmarklet() {
		return createdViaBookmarklet;
	}

	/**
	 * @param createdViaBookmarklet
	 *            설정할 createdViaBookmarklet
	 */
	public void setCreatedViaBookmarklet(boolean createdViaBookmarklet) {
		this.createdViaBookmarklet = createdViaBookmarklet;
	}

	/**
	 * @return createdViaMobile
	 */
	public boolean isCreatedViaMobile() {
		return createdViaMobile;
	}

	/**
	 * @param createdViaMobile
	 *            설정할 createdViaMobile
	 */
	public void setCreatedViaMobile(boolean createdViaMobile) {
		this.createdViaMobile = createdViaMobile;
	}

	/**
	 * @return sourceUrl
	 */
	public String getSourceUrl() {
		return sourceUrl;
	}

	/**
	 * @param sourceUrl
	 *            설정할 sourceUrl
	 */
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	/**
	 * @return sourceTitle
	 */
	public String getSourceTitle() {
		return sourceTitle;
	}

	/**
	 * @param sourceTitle
	 *            설정할 sourceTitle
	 */
	public void setSourceTitle(String sourceTitle) {
		this.sourceTitle = sourceTitle;
	}

	/**
	 * @return liked
	 */
	public boolean isLiked() {
		return liked;
	}

	/**
	 * @param liked
	 *            설정할 liked
	 */
	public void setLiked(boolean liked) {
		this.liked = liked;
	}

	/**
	 * @return state
	 */
	public PostState getState() {
		return state;
	}

	/**
	 * @param state
	 *            설정할 state
	 */
	public void setState(PostState state) {
		this.state = state;
	}

	/**
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            설정할 title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body
	 *            설정할 body
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * @param caption
	 *            설정할 caption
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * @return width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            설정할 width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            설정할 height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return photos
	 */
	public List<Photo> getPhotos() {
		return photos;
	}

	/**
	 * @param photos
	 *            설정할 photos
	 */
	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}

	/**
	 * @return text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            설정할 text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source
	 *            설정할 source
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            설정할 url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            설정할 description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return dialogue
	 */
	public List<ChatMessage> getDialogue() {
		return dialogue;
	}

	/**
	 * @param dialogue
	 *            설정할 dialogue
	 */
	public void setDialogue(List<ChatMessage> dialogue) {
		this.dialogue = dialogue;
	}

	/**
	 * @return audioPlayer
	 */
	public String getAudioPlayer() {
		return audioPlayer;
	}

	/**
	 * @param audioPlayer
	 *            설정할 audioPlayer
	 */
	public void setAudioPlayer(String audioPlayer) {
		this.audioPlayer = audioPlayer;
	}

	/**
	 * @return plays
	 */
	public int getPlays() {
		return plays;
	}

	/**
	 * @param plays
	 *            설정할 plays
	 */
	public void setPlays(int plays) {
		this.plays = plays;
	}

	/**
	 * @return albumArt
	 */
	public String getAlbumArt() {
		return albumArt;
	}

	/**
	 * @param albumArt
	 *            설정할 albumArt
	 */
	public void setAlbumArt(String albumArt) {
		this.albumArt = albumArt;
	}

	/**
	 * @return artist
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * @param artist
	 *            설정할 artist
	 */
	public void setArtist(String artist) {
		this.artist = artist;
	}

	/**
	 * @return album
	 */
	public String getAlbum() {
		return album;
	}

	/**
	 * @param album
	 *            설정할 album
	 */
	public void setAlbum(String album) {
		this.album = album;
	}

	/**
	 * @return trackName
	 */
	public String getTrackName() {
		return trackName;
	}

	/**
	 * @param trackName
	 *            설정할 trackName
	 */
	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}

	/**
	 * @return trackNumber
	 */
	public int getTrackNumber() {
		return trackNumber;
	}

	/**
	 * @param trackNumber
	 *            설정할 trackNumber
	 */
	public void setTrackNumber(int trackNumber) {
		this.trackNumber = trackNumber;
	}

	/**
	 * @return year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year
	 *            설정할 year
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return videoPlayers
	 */
	public List<VideoPostPlayer> getVideoPlayers() {
		return videoPlayers;
	}

	/**
	 * @param videoPlayers
	 *            설정할 videoPlayers
	 */
	public void setVideoPlayers(List<VideoPostPlayer> videoPlayers) {
		this.videoPlayers = videoPlayers;
	}
	
	

	/**
	 * @return askingName
	 */
	public String getAskingName() {
		return askingName;
	}

	/**
	 * @param askingName 설정할 askingName
	 */
	public void setAskingName(String askingName) {
		this.askingName = askingName;
	}

	/**
	 * @return askingUrl
	 */
	public String getAskingUrl() {
		return askingUrl;
	}

	/**
	 * @param askingUrl 설정할 askingUrl
	 */
	public void setAskingUrl(String askingUrl) {
		this.askingUrl = askingUrl;
	}

	/**
	 * @return question
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * @param question 설정할 question
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * @return answer
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * @param answer 설정할 answer
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	/*
	 * (비Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Post [id=").append(id).append(", ");
		if (blogName != null)
			builder.append("blogName=").append(blogName).append(", ");
		if (postUrl != null)
			builder.append("postUrl=").append(postUrl).append(", ");
		builder.append("timestamp=").append(timestamp).append(", ");
		if (gmtDateString != null)
			builder.append("gmtDateString=").append(gmtDateString).append(", ");
		if (format != null)
			builder.append("format=").append(format).append(", ");
		if (reblogKey != null)
			builder.append("reblogKey=").append(reblogKey).append(", ");
		builder.append("noteCount=").append(noteCount)
				.append(", createdViaBookmarklet=")
				.append(createdViaBookmarklet).append(", createdViaMobile=")
				.append(createdViaMobile).append(", ");
		if (sourceUrl != null)
			builder.append("sourceUrl=").append(sourceUrl).append(", ");
		if (sourceTitle != null)
			builder.append("sourceTitle=").append(sourceTitle).append(", ");
		builder.append("liked=").append(liked).append(", ");
		if (title != null)
			builder.append("title=").append(title).append(", ");
		if (body != null)
			builder.append("body=").append(body).append(", ");
		if (caption != null)
			builder.append("caption=").append(caption).append(", ");
		builder.append("width=").append(width).append(", height=")
				.append(height).append(", ");
		if (text != null)
			builder.append("text=").append(text).append(", ");
		if (source != null)
			builder.append("source=").append(source).append(", ");
		if (url != null)
			builder.append("url=").append(url).append(", ");
		if (description != null)
			builder.append("description=").append(description).append(", ");
		if (audioPlayer != null)
			builder.append("audioPlayer=").append(audioPlayer).append(", ");
		builder.append("plays=").append(plays).append(", ");
		if (albumArt != null)
			builder.append("albumArt=").append(albumArt).append(", ");
		if (artist != null)
			builder.append("artist=").append(artist).append(", ");
		if (album != null)
			builder.append("album=").append(album).append(", ");
		if (trackName != null)
			builder.append("trackName=").append(trackName).append(", ");
		builder.append("trackNumber=").append(trackNumber).append(", year=")
				.append(year).append("]");
		return builder.toString();
	}

}

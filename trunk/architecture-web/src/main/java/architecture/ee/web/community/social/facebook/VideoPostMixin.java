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
package architecture.ee.web.community.social.facebook;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class VideoPostMixin extends PostMixin {

	@JsonCreator
	VideoPostMixin(
			@JsonProperty("id") String id, 
			@JsonProperty("from") Reference from, 
			@JsonProperty("created_time") Date createdTime,
			@JsonProperty("updated_time") Date updatedTime) {
		super(id, from, createdTime, updatedTime);
	}
	
	@JsonProperty("source")
	String source;
	
	@JsonProperty("object_id")
	String videoId;
	
	@JsonProperty("tags")
	@JsonDeserialize(using=TagListDeserializer.class)
	List<Tag> tags;
	
}

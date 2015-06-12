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
package architecture.ee.web.community.tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TagManagerHelper {
		
	private Log log = LogFactory.getLog(getClass());
	
	private final TagManager tagManager;

	public TagManagerHelper(TagManager tagManager) {
		this.tagManager = tagManager;
	}

	public void setTags(String tags, int objectType, long objectId) {		
		if (tags == null || "".equals(tags.trim())) {
			tagManager.removeAllTags(objectType, objectId);
		} else {
			List<String> tagAr = Arrays.asList(tags.trim().toLowerCase().split("(\\s|,|\\\\)"));
			List<ContentTag> removedTags = new ArrayList<ContentTag>();
			for (String tag : tagAr) {
				String safeTag = tag.trim();
				if (!"".equals(safeTag)) {
					ContentTag ct = tagManager.createTag(safeTag);
					boolean hasNewTag = false;
					List<ContentTag> existingTags = tagManager.getTags(objectType, objectId);
					for (ContentTag existingTag : existingTags) {
						if (ct.equals(existingTag))
							hasNewTag = true;
						if (!removedTags.contains(existingTag)
								&& !tagAr.contains(existingTag.getName())) {
							tagManager.removeTag(existingTag, objectType, objectId);
							removedTags.add(existingTag);
						}
					}
					if (!hasNewTag)
						tagManager.addTag(ct, objectType, objectId);
				}
			}
		}
	}	

	public String getTagsAsString(int objectType, long objectId) {
		StringBuilder tagValues = new StringBuilder();
		
		
		List<ContentTag> existingTags = tagManager.getTags(objectType, objectId);
		
		log.debug("existingTags:" + existingTags );
		
		for (ContentTag existingTag : existingTags) {
			tagValues.append(existingTag.getName());
			tagValues.append(" ");
		}
		
		return tagValues.toString().trim();
	}

}

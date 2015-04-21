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
package architecture.ee.web.attachment;

import architecture.common.cache.CacheSizes;
import architecture.common.cache.Cacheable;

public class ImageLink implements Cacheable {

	private Long imageId = -1L;
	
	private String linkId;
	
	private boolean publicShared = false;
	
	/**
	 * @param linkId
	 * @param imageId
	 * @param publicShared
	 */
	public ImageLink(String linkId, Long imageId, boolean publicShared) {
		super();
		this.linkId = linkId;
		this.imageId = imageId;
		this.publicShared = publicShared;
	}

	/**
	 * @return imageId
	 */
	public Long getImageId() {
		return imageId;
	}

	/**
	 * @return linkId
	 */
	public String getLinkId() {
		return linkId;
	}

	/**
	 * @return publicShared
	 */
	public boolean isPublicShared() {
		return publicShared;
	}

	public int getCachedSize() {
		return  CacheSizes.sizeOfBoolean() + CacheSizes.sizeOfLong() + CacheSizes.sizeOfString(linkId) ;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ImageLink [");
		if (imageId != null)
			builder.append("imageId=").append(imageId).append(", ");
		if (linkId != null)
			builder.append("linkId=").append(linkId).append(", ");
		builder.append("publicShared=").append(publicShared).append("]");
		return builder.toString();
	}	
	
}

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

import java.util.List;

public class ImageConfig {

	private boolean enabled;
	private int maxImageSize;
	private int imagePreviewMaxSize ;
	private int imageMaxWidth ;
	private int imageMaxHeight ;
	private int maxImagesPerObject ;
	private boolean allowAllByDefault ;
	private List<String> allowedTypes ;
	private List<String> disallowedTypes ;
	private boolean forceThumbnailsEnabled ;
	
	public ImageConfig() {
        imagePreviewMaxSize = 250;
        imageMaxWidth = 450;
        imageMaxHeight = 600;
	}	
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getMaxImageSize() {
		return maxImageSize;
	}

	public void setMaxImageSize(int maxImageSize) {
		this.maxImageSize = maxImageSize;
	}

	public int getImagePreviewMaxSize() {
		return imagePreviewMaxSize;
	}

	public void setImagePreviewMaxSize(int imagePreviewMaxSize) {
		this.imagePreviewMaxSize = imagePreviewMaxSize;
	}

	public int getImageMaxWidth() {
		return imageMaxWidth;
	}

	public void setImageMaxWidth(int imageMaxWidth) {
		this.imageMaxWidth = imageMaxWidth;
	}

	public int getImageMaxHeight() {
		return imageMaxHeight;
	}

	public void setImageMaxHeight(int imageMaxHeight) {
		this.imageMaxHeight = imageMaxHeight;
	}

	public int getMaxImagesPerObject() {
		return maxImagesPerObject;
	}

	public void setMaxImagesPerObject(int maxImagesPerObject) {
		this.maxImagesPerObject = maxImagesPerObject;
	}



	public boolean isAllowAllByDefault() {
		return allowAllByDefault;
	}



	public void setAllowAllByDefault(boolean allowAllByDefault) {
		this.allowAllByDefault = allowAllByDefault;
	}



	public List<String> getAllowedTypes() {
		return allowedTypes;
	}



	public void setAllowedTypes(List<String> allowedTypes) {
		this.allowedTypes = allowedTypes;
	}



	public List<String> getDisallowedTypes() {
		return disallowedTypes;
	}



	public void setDisallowedTypes(List<String> disallowedTypes) {
		this.disallowedTypes = disallowedTypes;
	}



	public boolean isForceThumbnailsEnabled() {
		return forceThumbnailsEnabled;
	}



	public void setForceThumbnailsEnabled(boolean forceThumbnailsEnabled) {
		this.forceThumbnailsEnabled = forceThumbnailsEnabled;
	}



	public boolean equals(Object o)
    {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        ImageConfig that = (ImageConfig)o;
        
        if(allowAllByDefault != that.allowAllByDefault)
            return false;
        if(forceThumbnailsEnabled != that.forceThumbnailsEnabled)
            return false;
        if(imageMaxHeight != that.imageMaxHeight)
            return false;
        if(imageMaxWidth != that.imageMaxWidth)
            return false;
        if(imagePreviewMaxSize != that.imagePreviewMaxSize)
            return false;
        if(enabled != that.enabled)
            return false;
        if(maxImageSize != that.maxImageSize)
            return false;
        if(maxImagesPerObject != that.maxImagesPerObject)
            return false;
        if(allowedTypes == null ? that.allowedTypes != null : !allowedTypes.equals(that.allowedTypes))
            return false;
        return disallowedTypes == null ? that.disallowedTypes == null : disallowedTypes.equals(that.disallowedTypes);
    }

    public int hashCode()
    {
        int result = enabled ? 1 : 0;
        result = 31 * result + maxImageSize;
        result = 31 * result + imagePreviewMaxSize;
        result = 31 * result + imageMaxWidth;
        result = 31 * result + imageMaxHeight;
        result = 31 * result + maxImagesPerObject;
        result = 31 * result + (allowedTypes == null ? 0 : allowedTypes.hashCode());
        result = 31 * result + (disallowedTypes == null ? 0 : disallowedTypes.hashCode());
        result = 31 * result + (allowAllByDefault ? 1 : 0);
        result = 31 * result + (forceThumbnailsEnabled ? 1 : 0);
        return result;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ImageManagerBean");
        sb.append("{enabled=").append(enabled);
        sb.append(", maxImageSize=").append(maxImageSize);
        sb.append(", imagePreviewMaxSize=").append(imagePreviewMaxSize);
        sb.append(", imageMaxWidth=").append(imageMaxWidth);
        sb.append(", imageMaxHeight=").append(imageMaxHeight);
        sb.append(", maxImagesPerObject=").append(maxImagesPerObject);
        sb.append(", allowedTypes=").append(allowedTypes);
        sb.append(", disallowedTypes=").append(disallowedTypes);
        sb.append(", allowAllByDefault=").append(allowAllByDefault);
        sb.append(", forceThumbnailsEnabled=").append(forceThumbnailsEnabled);
        sb.append('}');
        return sb.toString();
    }    
    
}

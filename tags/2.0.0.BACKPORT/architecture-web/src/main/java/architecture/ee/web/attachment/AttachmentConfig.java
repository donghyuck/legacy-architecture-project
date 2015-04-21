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

public class AttachmentConfig {

    private long typeId;
    private boolean attachmentsEnabled;
    private int maxAttachmentSize;
    private int maxAttachmentsPerDocument;
    private int maxAttachmentsPerMessage;
    private int maxAttachmentsPerBlogPost;
    private List<String> allowedTypes;
    private List<String> disallowedTypes;
    private boolean allowAllByDefault;
    private boolean imagePreviewEnabled;
    private int imagePreviewMaxSize;
    private boolean imagePreviewRatioEnabled;
    
    public long getTypeId() {
		return typeId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public boolean isAttachmentsEnabled()
    {
        return attachmentsEnabled;
    }

    public void setAttachmentsEnabled(boolean attachmentsEnabled)
    {
        this.attachmentsEnabled = attachmentsEnabled;
    }

    public int getMaxAttachmentSize()
    {
        return maxAttachmentSize;
    }

    public void setMaxAttachmentSize(int maxAttachmentSize)
    {
        this.maxAttachmentSize = maxAttachmentSize;
    }

    public int getMaxAttachmentsPerDocument()
    {
        return maxAttachmentsPerDocument;
    }

    public void setMaxAttachmentsPerDocument(int maxAttachmentsPerDocument)
    {
        this.maxAttachmentsPerDocument = maxAttachmentsPerDocument;
    }

    public int getMaxAttachmentsPerBlogPost()
    {
        return maxAttachmentsPerBlogPost;
    }

    public void setMaxAttachmentsPerBlogPost(int maxAttachmentsPerBlogPost)
    {
        this.maxAttachmentsPerBlogPost = maxAttachmentsPerBlogPost;
    }

    public List<String> getAllowedTypes()
    {
        return allowedTypes;
    }

    public void setAllowedTypes(List<String> allowedTypes)
    {
        this.allowedTypes = allowedTypes;
    }

    public List<String> getDisallowedTypes()
    {
        return disallowedTypes;
    }

    public void setDisallowedTypes(List<String> disallowedTypes)
    {
        this.disallowedTypes = disallowedTypes;
    }

    public boolean isAllowAllByDefault()
    {
        return allowAllByDefault;
    }

    public void setAllowAllByDefault(boolean allowAllByDefault)
    {
        this.allowAllByDefault = allowAllByDefault;
    }

    public boolean isImagePreviewEnabled()
    {
        return imagePreviewEnabled;
    }

    public void setImagePreviewEnabled(boolean imagePreviewEnabled)
    {
        this.imagePreviewEnabled = imagePreviewEnabled;
    }

    public int getMaxAttachmentsPerMessage()
    {
        return maxAttachmentsPerMessage;
    }

    public void setMaxAttachmentsPerMessage(int maxAttachmentsPerMessage)
    {
        this.maxAttachmentsPerMessage = maxAttachmentsPerMessage;
    }

    public int getImagePreviewMaxSize()
    {
        return imagePreviewMaxSize;
    }

    public void setImagePreviewMaxSize(int imagePreviewMaxSize)
    {
        this.imagePreviewMaxSize = imagePreviewMaxSize;
    }

    public boolean isImagePreviewRatioEnabled()
    {
        return imagePreviewRatioEnabled;
    }

    public void setImagePreviewRatioEnabled(boolean imagePreviewRatioEnabled)
    {
        this.imagePreviewRatioEnabled = imagePreviewRatioEnabled;
    }

    public boolean equals(Object o)
    {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        AttachmentConfig that = (AttachmentConfig)o;
        if(allowAllByDefault != that.allowAllByDefault)
            return false;
        if(attachmentsEnabled != that.attachmentsEnabled)
            return false;
        if(imagePreviewEnabled != that.imagePreviewEnabled)
            return false;
        if(imagePreviewMaxSize != that.imagePreviewMaxSize)
            return false;
        if(imagePreviewRatioEnabled != that.imagePreviewRatioEnabled)
            return false;
        if(maxAttachmentSize != that.maxAttachmentSize)
            return false;
        if(maxAttachmentsPerDocument != that.maxAttachmentsPerDocument)
            return false;
        if(maxAttachmentsPerMessage != that.maxAttachmentsPerMessage)
            return false;
        if(maxAttachmentsPerBlogPost != that.maxAttachmentsPerBlogPost)
            return false;
        if(typeId != that.typeId)
            return false;
        if(allowedTypes == null ? that.allowedTypes != null : !allowedTypes.equals(that.allowedTypes))
            return false;
        return disallowedTypes == null ? that.disallowedTypes == null : disallowedTypes.equals(that.disallowedTypes);
    }

    public int hashCode()
    {
        int result = (int)(typeId ^ typeId >>> 32);
        result = 31 * result + (attachmentsEnabled ? 1 : 0);
        result = 31 * result + maxAttachmentSize;
        result = 31 * result + maxAttachmentsPerDocument;
        result = 31 * result + maxAttachmentsPerMessage;
        result = 31 * result + maxAttachmentsPerBlogPost;
        result = 31 * result + (allowedTypes == null ? 0 : allowedTypes.hashCode());
        result = 31 * result + (disallowedTypes == null ? 0 : disallowedTypes.hashCode());
        result = 31 * result + (allowAllByDefault ? 1 : 0);
        result = 31 * result + (imagePreviewEnabled ? 1 : 0);
        result = 31 * result + imagePreviewMaxSize;
        result = 31 * result + (imagePreviewRatioEnabled ? 1 : 0);
        return result;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("AttachmentConfig");
        sb.append("{typeId=").append(typeId);
        sb.append(", attachmentsEnabled=").append(attachmentsEnabled);
        sb.append(", maxAttachmentSize=").append(maxAttachmentSize);
        sb.append(", maxAttachmentsPerDocument=").append(maxAttachmentsPerDocument);
        sb.append(", maxAttachmentsPerMessage=").append(maxAttachmentsPerMessage);
        sb.append(", maxAttachmentsPerBlogPost=").append(maxAttachmentsPerBlogPost);
        sb.append(", allowedTypes=").append(allowedTypes);
        sb.append(", disallowedTypes=").append(disallowedTypes);
        sb.append(", allowAllByDefault=").append(allowAllByDefault);
        sb.append(", imagePreviewEnabled=").append(imagePreviewEnabled);
        sb.append(", imagePreviewMaxSize=").append(imagePreviewMaxSize);
        sb.append(", imagePreviewRatioEnabled=").append(imagePreviewRatioEnabled);
        sb.append('}');
        return sb.toString();
    }

}

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

import java.io.File;
import java.io.InputStream;
import java.util.List;

import architecture.ee.exception.NotFoundException;

public interface AttachmentManager {

	public abstract Attachment getAttachment(long attachmentId) throws NotFoundException ;
	
	public abstract List<Attachment> getAttachments(int objectType, long objectId);
	
	public abstract Attachment createAttachment(int objectType, long objectId, String name, String contentType, File file);
	
	public abstract Attachment createAttachment(int objectType, long objectId, String name, String contentType, InputStream inputStream);	
	
	public abstract Attachment createAttachment(int objectType, long objectId, String name, String contentType, InputStream inputStream, int size);
	
	public abstract Attachment saveAttachment( Attachment attachment );	

	public abstract void removeAttachment( Attachment attachment );
	
	public abstract InputStream getAttachmentInputStream(Attachment attachment);
	
	public abstract InputStream getAttachmentImageThumbnailInputStream(Attachment image, int width, int height ) ;	
		
	public abstract int getTotalAttachmentCount(int objectType, long objectId);
}

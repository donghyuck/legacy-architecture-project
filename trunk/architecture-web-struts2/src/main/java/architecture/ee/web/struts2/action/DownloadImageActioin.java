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
package architecture.ee.web.struts2.action;

import java.io.InputStream;

import architecture.ee.exception.NotFoundException;
import architecture.ee.exception.SystemException;
import architecture.ee.web.attachment.Image;
import architecture.ee.web.attachment.ImageManager;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

import com.opensymphony.xwork2.Preparable;

public class DownloadImageActioin extends FrameworkActionSupport  implements Preparable {

	private Long imageId;

	private ImageManager imageManager ;
	
	public void prepare() throws Exception {
		
	}
	
	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public ImageManager getImageManager() {
		return imageManager;
	}

	public void setImageManager(ImageManager imageManager) {
		this.imageManager = imageManager;
	}

	public Image getTargetImage() {
		try {
			return imageManager.getImage(imageId);
		} catch (NotFoundException e) {
			throw new SystemException(e);
		}		
	}
	
	public InputStream getTargetImageInputStream() {
		return imageManager.getImageInputStream(getTargetImage());
	}
	
	public String getTargetImageContentType(){
		return getTargetImage().getContentType();
	}

	public String getTargetImageFileName(){
		return getTargetImage().getName();
	}	
	
	public int getTargetImageContentLength(){
		return getTargetImage().getSize();
	}
	
}

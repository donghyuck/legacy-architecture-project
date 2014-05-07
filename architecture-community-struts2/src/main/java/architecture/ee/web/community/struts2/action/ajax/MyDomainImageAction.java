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
package architecture.ee.web.community.struts2.action.ajax;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.List;

import architecture.ee.web.attachment.Image;
import architecture.ee.web.attachment.impl.ImageImpl;


public class MyDomainImageAction extends MyImageAction  {

	private static final  int DOMAIN_OBJEDT_TYPE = 1 ;

	public int getTotalTargetImageCount(){
		if( getUser().getUserId() < 1 )
			return 0 ;
		return getImageManager().getTotalImageCount(DOMAIN_OBJEDT_TYPE, getUser().getCompany().getCompanyId());
	}
	
	public List<Image> getTargetImages(){    	
    	if( getUser().getUserId() < 1 )
    		return Collections.EMPTY_LIST;    	
        if( getPageSize() > 0 ){
            return getImageManager().getImages(DOMAIN_OBJEDT_TYPE, getUser().getCompany().getCompanyId(), getStartIndex(),getPageSize());            
        }else{            
            return getImageManager().getImages(DOMAIN_OBJEDT_TYPE, getUser().getCompany().getCompanyId());
        }
    }	

	public String updateImage() throws Exception {		
		if(isMultiPart() ){
			Image imageToUse;
			if( getImageId()< 0  ){	
				File fileToUse = getUploadImage();							
				imageToUse = getImageManager().createImage( DOMAIN_OBJEDT_TYPE, getUser().getCompany().getCompanyId(),  getUploadImageFileName(),  getUploadImageContentType(),  fileToUse);					
				extractMetadata( imageToUse, fileToUse);				
				setImageId ( getImageManager().saveImage(imageToUse).getImageId() );					
			}else{
				imageToUse = getTargetImage();
				File fileToUse = getUploadImage();
				extractMetadata( imageToUse, fileToUse);				
				((ImageImpl)imageToUse).setSize( (int)fileToUse.length());
				((ImageImpl)imageToUse).setInputStream( new FileInputStream(fileToUse));
				log.debug("image size:" + imageToUse.getSize());
				log.debug("image stream:" + imageToUse.getInputStream());
				getImageManager().saveImage(imageToUse);			
			}		
		}
		return success();
	}
	
	/* (비Javadoc)
	 * @see architecture.ee.web.community.struts2.action.ajax.MyImageAction#deleteImage()
	 */
	@Override
	public String deleteImage() throws Exception {
		return super.deleteImage();
	}

	/* (비Javadoc)
	 * @see architecture.ee.web.community.struts2.action.ajax.MyImageAction#updateImageProperties()
	 */
	@Override
	public String updateImageProperties() throws Exception {
		return super.updateImageProperties();
	}

	/* (비Javadoc)
	 * @see architecture.ee.web.community.struts2.action.ajax.MyImageAction#deleteImageProperties()
	 */
	@Override
	public String deleteImageProperties() throws Exception {
		return super.deleteImageProperties();
	}
	
}

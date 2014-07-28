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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import architecture.ee.exception.NotFoundException;
import architecture.ee.exception.SystemException;
import architecture.ee.util.ApplicationHelper;
import architecture.ee.web.attachment.dao.AttachmentDao;
import architecture.ee.web.attachment.impl.AttachmentImpl;

public class DefaultAttachmentManager extends AbstractAttachmentManager implements AttachmentManager {

	
	private AttachmentDao attachmentDao;

	private Cache attachmentCache;
	
	private File attachmentDir;	
	


	public AttachmentDao getAttachmentDao() {
		return attachmentDao;
	}

	public void setAttachmentDao(AttachmentDao attachmentDao) {
		this.attachmentDao = attachmentDao;
	}
	

	public Cache getAttachmentCache() {
		return attachmentCache;
	}

	public void setAttachmentCache(Cache attachmentCache) {
		this.attachmentCache = attachmentCache;
	}

	protected synchronized File getAttachmentDir() {
		if(attachmentDir == null)
        {
			attachmentDir = ApplicationHelper.getRepository().getFile("attachments");
			if(!attachmentDir.exists())
            {
                boolean result = attachmentDir.mkdir();
                if(!result)
                    log.error((new StringBuilder()).append("Unable to create attachment directory: '").append(attachmentDir).append("'").toString());
            }
        }
        return attachmentDir;
	}
	

	public Attachment getAttachment(long attachmentId) throws NotFoundException {
		
		Attachment attachment = getAttachmentInCache(attachmentId);
		if( attachment == null){
			try {
				attachment = attachmentDao.getByAttachmentId(attachmentId);
			} catch (DataAccessException e) {
				throw new NotFoundException(e);
			}
			attachmentCache.put(new Element(attachmentId, attachment ));
		}
		return attachment;
	}

	public List<Attachment> getAttachments(int objectType, long objectId) {
		return attachmentDao.getByObjectTypeAndObjectId(objectType, objectId);
	}
	
	protected Attachment getAttachmentInCache(long attachmentId){
		if( attachmentCache.get(attachmentId) != null && attachmentId > 0L )
			return  (Attachment) attachmentCache.get( attachmentId ).getValue();
		else 
			return null;
	}
	
	
	public Attachment createAttachment(int objectType, long objectId, String name, String contentType, File file) {
		
		AttachmentImpl attachment = new AttachmentImpl();
		attachment.setObjectType(objectType);
		attachment.setObjectId(objectId);
		attachment.setName(name);
		attachment.setContentType(contentType);		
		attachment.setSize( (int) FileUtils.sizeOf(file));
		try {
			attachment.setInputStream(FileUtils.openInputStream(file));
		} catch (IOException e) {
			log.debug(e);
		}	
		return attachment;
	}
	
	public Attachment createAttachment(int objectType, long objectId, String name, String contentType, InputStream inputStream) {
		
		AttachmentImpl attachment = new AttachmentImpl();
		attachment.setObjectType(objectType);
		attachment.setObjectId(objectId);
		attachment.setName(name);
		attachment.setContentType(contentType);
		attachment.setInputStream(inputStream);		
		try {
			attachment.setSize( IOUtils.toByteArray(inputStream).length );
		} catch (IOException e) {
			log.debug(e);
		}		
		return attachment;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public Attachment saveAttachment(Attachment attachment) {
		
		Date now = new Date();
		Attachment attachmentToUse = attachment ;
		if( attachmentToUse.getAttachmentId() > 0 ){
			attachmentCache.remove(attachmentToUse.getAttachmentId());			
			attachmentToUse.setModifiedDate(now);
			attachmentDao.updateAttachment(attachmentToUse);			
		}else{			
			attachmentToUse.setCreationDate(now);
			attachmentToUse.setModifiedDate(now);
			attachmentToUse = attachmentDao.createAttachment(attachmentToUse);
		}		
		try {			
			if( attachmentToUse.getInputStream() != null )
			{
				attachmentDao.saveAttachmentData(attachmentToUse, attachmentToUse.getInputStream());				
				Collection<File> files = FileUtils.listFiles(getAttachmentCacheDir(), FileFilterUtils.prefixFileFilter(attachment.getAttachmentId() + ""), null);
				for(File file : files){
					FileUtils.deleteQuietly(file);
				}
			}
			
			return getAttachment(attachment.getAttachmentId());
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	
	public InputStream getAttachmentInputStream(Attachment attachment) {
		try {
			File file = getAttachmentFromCacheIfExist(attachment);
			return FileUtils.openInputStream(file);
		} catch (IOException e) {
			throw new SystemException(e);
		}
	}
	
	protected File getAttachmentFromCacheIfExist(Attachment attachment) throws IOException{		
		File dir = getAttachmentCacheDir();
		
		StringBuilder sb = new StringBuilder();
		sb.append( attachment.getAttachmentId() ).append(".bin");		
		
		File file = new File(dir, sb.toString() );		
		if( file.exists() ){
			long size = FileUtils.sizeOf(file);
			if( size != attachment.getSize() ){
				// size different make cache new one....
				InputStream inputStream = attachmentDao.getAttachmentData(attachment);
				FileUtils.copyInputStreamToFile(inputStream, file);
			}
		}else{
			// doesn't exist, make new one ..
			InputStream inputStream = attachmentDao.getAttachmentData(attachment);
			FileUtils.copyInputStreamToFile(inputStream, file);
		}		
		return file;
	}	
	
	protected File getAttachmentCacheDir(){
		File dir = new File(getAttachmentDir(), "cache" );	
		if( !dir.exists() )
			dir.mkdir();
		
		return dir;
	}
	

	public void  initialize() {		
		log.debug( "initializing attachement manager" );		
		getAttachmentDir();
	}	
	public void destroy(){
		
	}
	
	public InputStream getAttachmentImageThumbnailInputStream(Attachment image, int width, int height ) {		
		try {
			
			File file = getThumbnailFromCacheIfExist(image, width, height);
			return FileUtils.openInputStream(file);
		} catch (IOException e) {
			throw new SystemException(e);
		}
	}

	
	public InputStream getImageThumbnailInputStream(Attachment image, int width, int height ) {		
		try {
			
			File file = getThumbnailFromCacheIfExist(image, width, height);
			return FileUtils.openInputStream(file);
		} catch (IOException e) {
			throw new SystemException(e);
		}
	}


	protected File getThumbnailFromCacheIfExist(Attachment image,  int width, int height ) throws IOException{		
		
		log.debug( "thumbnail generation " + width + "x" + height );
		File dir = getAttachmentCacheDir();
		File file = new File(dir, toThumbnailFilename(image, width, height) );		
		File originalFile = getAttachmentFromCacheIfExist( image );	
		
		log.debug( "source: " + originalFile.getAbsoluteFile() + ", " + originalFile.length() );
		log.debug( "target:" + file.getAbsoluteFile());
		
		if( file.exists() && file.length() > 0 ){
			image.setThumbnailSize((int)file.length());
			return file;
		}
		
		BufferedImage originalImage = ImageIO.read(originalFile);		
		if( originalImage.getHeight() < height || originalImage.getWidth() < width ){
			image.setThumbnailSize(0);
			return originalFile ;
		}
		
		BufferedImage thumbnail = Thumbnails.of(originalImage).size(width, height).asBufferedImage();
		ImageIO.write(thumbnail, "png", file );
		image.setThumbnailSize((int)file.length());
		
		return file;		
	}

	protected String toThumbnailFilename(Attachment image,  int width, int height){
		StringBuilder sb = new StringBuilder();
		sb.append( image.getAttachmentId() ).append("_").append(width).append("_").append(height).append(".bin");	
		return sb.toString();
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void removeAttachment(Attachment attachment) {		
		Attachment attachmentToUse = attachment ;		
		if( attachmentToUse.getAttachmentId() > 0 ){
			attachmentCache.remove(attachmentToUse.getAttachmentId());		
			attachmentDao.deleteAttachment(attachmentToUse);
			attachmentDao.deleteAttachmentData(attachmentToUse);
		}
	}

	public int getTotalAttachmentCount(int objectType, long objectId) {
		return attachmentDao.getAttachmentCount(objectType, objectId);
	}
}

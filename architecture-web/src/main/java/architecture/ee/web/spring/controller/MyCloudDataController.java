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
package architecture.ee.web.spring.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.user.SecurityHelper;
import architecture.common.user.User;
import architecture.common.user.UserManager;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.attachment.Attachment;
import architecture.ee.web.attachment.AttachmentManager;
import architecture.ee.web.attachment.Image;
import architecture.ee.web.attachment.ImageLink;
import architecture.ee.web.attachment.ImageManager;
import architecture.ee.web.attachment.impl.AttachmentImpl;
import architecture.ee.web.attachment.impl.ImageImpl;
import architecture.ee.web.util.WebSiteUtils;
import architecture.ee.web.ws.Property;
import architecture.ee.web.ws.Usage;

@Controller("my-cloud-data-controller")
@RequestMapping("/data")
public class MyCloudDataController {

    private Log log = LogFactory.getLog(getClass());

    @Inject
    @Qualifier("imageManager")
    private ImageManager imageManager;

    @Inject
    @Qualifier("attachmentManager")
    private AttachmentManager attachmentManager;

    @Inject
    @Qualifier("userManager")
    private UserManager userManager;

    public UserManager getUserManager() {
	return userManager;
    }

    public void setUserManager(UserManager userManager) {
	this.userManager = userManager;
    }

    /**
     * @return imageManager
     */
    public ImageManager getImageManager() {
	return imageManager;
    }

    /**
     * @param imageManager
     *            설정할 imageManager
     */
    public void setImageManager(ImageManager imageManager) {
	this.imageManager = imageManager;
    }

    /**
     * @return attachmentManager
     */
    public AttachmentManager getAttachmentManager() {
	return attachmentManager;
    }

    /**
     * @param attachmentManager
     *            설정할 attachmentManager
     */
    public void setAttachmentManager(AttachmentManager attachmentManager) {
	this.attachmentManager = attachmentManager;
    }

    @Secured({ "ROLE_USER" })
    @RequestMapping(value = "/cloud/usage.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public Map<String, Usage> usage(NativeWebRequest request) {
	User user = SecurityHelper.getUser();
	int objectType = user.getModelObjectType();
	long objectId = user.getUserId();
	long maxSize = 1073741824 * 5;

	long imageUsage = imageManager.getUsage(objectType, objectId);
	long fileUsage = attachmentManager.getUsage(objectType, objectId);
	float percentage = (imageUsage + fileUsage) * 100 / maxSize;

	Map<String, Usage> list = new HashMap<String, Usage>();
	list.put("photo", new Usage("photo", imageUsage));
	list.put("file", new Usage("file", fileUsage));
	list.put("limit", new Usage("limit", maxSize));
	return list;
    }

    @Secured({ "ROLE_USER" })
    @RequestMapping(value = "/images/list.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public ImageList getImageList(
	    @RequestParam(value = "objectType", defaultValue = "2", required = false) Integer objectType,
	    @RequestParam(value = "objectId", defaultValue = "0", required = false) Long objectId,
	    @RequestParam(value = "startIndex", defaultValue = "0", required = false) Integer startIndex,
	    @RequestParam(value = "pageSize", defaultValue = "0", required = false) Integer pageSize,
	    NativeWebRequest request) throws NotFoundException {
	User user = SecurityHelper.getUser();

	return getImageList(objectType, objectId, startIndex, pageSize,
		request.getNativeRequest(HttpServletRequest.class));

    }

    private ImageList getImageList(int objectType, long objectId, int startIndex, int pageSize,
	    HttpServletRequest request) throws NotFoundException {
	User user = SecurityHelper.getUser();
	if (objectType == 1) {
	    objectId = user.getCompanyId();
	} else if (objectType == 2) {
	    objectId = user.getUserId();
	} else if (objectType == 30) {
	    objectId = WebSiteUtils.getWebSite(request).getWebSiteId();
	}

	ImageList list = new ImageList();

	list.setTotalCount(imageManager.getTotalImageCount(objectType, objectId));
	if (pageSize > 0) {
	    list.setImages(imageManager.getImages(objectType, objectId, startIndex, pageSize));
	} else {
	    list.setImages(imageManager.getImages(objectType, objectId));
	}
	return list;

    }

    public static class ImageList {

	private List<Image> images;
	private int totalCount;

	/**
	 * @return images
	 */
	public List<Image> getImages() {
	    return images;
	}

	/**
	 * @param images
	 *            설정할 images
	 */
	public void setImages(List<Image> images) {
	    this.images = images;
	}

	/**
	 * @return totalCount
	 */
	public int getTotalCount() {
	    return totalCount;
	}

	/**
	 * @param totalCount
	 *            설정할 totalCount
	 */
	public void setTotalCount(int totalCount) {
	    this.totalCount = totalCount;
	}

    }

    private boolean hasPermissions(Image image, User user) {
	if (user.isAnonymous())
	    return false;
	if (image.getObjectType() == ModelTypeFactory.getTypeIdFromCode("COMPANY")
		&& image.getObjectId() != user.getCompanyId()) {
	    return false;
	} else if (image.getObjectType() == ModelTypeFactory.getTypeIdFromCode("USER")
		&& image.getObjectId() != user.getUserId()) {
	    return false;
	}
	return true;
    }

    
    @RequestMapping(value = "/images/details.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public ImageDetails getImageDetails(
	    @RequestParam(value = "imageId", defaultValue = "0", required = true) Long imageId,
	    NativeWebRequest request) throws NotFoundException {
	
	User user = SecurityHelper.getUser();
	
	Image image = imageManager.getImage(imageId);
	ImageDetails details = new ImageDetails(image);
	try {
	    Metadata metadata = ImageMetadataReader.readMetadata(new BufferedInputStream(imageManager.getImageInputStream(image)), false);
	    if (metadata.containsDirectory(ExifSubIFDDirectory.class)) {
		List<ExifTag> list = new ArrayList<ExifTag>();
		ExifSubIFDDirectory exifDirectory = metadata.getDirectory(ExifSubIFDDirectory.class);
		for (Tag tag : exifDirectory.getTags()) {
		    list.add(new ExifTag(tag.getTagName(), tag.getDescription(), exifDirectory.getString(tag.getTagType())));
		}
		details.setExif(list);
	    }
	} catch (Exception e) {
	}
	return details;
    }

    public static class ImageDetails {
	private Image image;
	private List<ExifTag> exif;

	/**
	 * @param image
	 */
	public ImageDetails(Image image) {
	    this.image = image;
	    this.exif = Collections.EMPTY_LIST;
	}

	/**
	 * @return image
	 */
	public Image getImage() {
	    return image;
	}

	/**
	 * @param image
	 *            설정할 image
	 */
	public void setImage(Image image) {
	    this.image = image;
	}

	/**
	 * @return exif
	 */
	public List<ExifTag> getExif() {
	    return exif;
	}

	/**
	 * @param exif
	 *            설정할 exif
	 */
	public void setExif(List<ExifTag> exif) {
	    this.exif = exif;
	}

    }

    public static class ExifTag {
	String tagName;
	String description;
	String value;

	/**
	 * @param tagName
	 * @param description
	 * @param value
	 */
	public ExifTag(String tagName, String description, String value) {
	    super();
	    this.tagName = tagName;
	    this.description = description;
	    this.value = value;
	}

	/**
	 * @return tagName
	 */
	public String getTagName() {
	    return tagName;
	}

	/**
	 * @param tagName
	 *            설정할 tagName
	 */
	public void setTagName(String tagName) {
	    this.tagName = tagName;
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
	 * @return value
	 */
	public String getValue() {
	    return value;
	}

	/**
	 * @param value
	 *            설정할 value
	 */
	public void setValue(String value) {
	    this.value = value;
	}
    }

    @RequestMapping(value = "/images/get.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public Image getImage(@RequestParam(value = "imageId", defaultValue = "0", required = true) Long imageId,
	    NativeWebRequest request) throws NotFoundException {
	User user = SecurityHelper.getUser();
	return imageManager.getImage(imageId);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @RequestMapping(value = "/images/update_with_media.json", method = RequestMethod.POST)
    @ResponseBody
    public List<Image> uploadImageWithMedia(
	    @RequestParam(value = "objectType", defaultValue = "2", required = false) Integer objectType,
	    @RequestParam(value = "objectId", defaultValue = "0", required = false) Long objectId,
	    @RequestParam(value = "imageId", defaultValue = "0", required = false) Long imageId,
	    MultipartHttpServletRequest request) throws NotFoundException, IOException {
	User user = SecurityHelper.getUser();
	if (objectType == 1) {
	    objectId = user.getCompanyId();
	} else if (objectType == 2) {
	    objectId = user.getUserId();
	} else if (objectType == 30) {
	    objectId = WebSiteUtils.getWebSite(request).getWebSiteId();
	}

	Iterator<String> names = request.getFileNames();
	List<Image> list = new ArrayList<Image>();
	while (names.hasNext()) {
	    String fileName = names.next();
	    log.debug(fileName);
	    MultipartFile mpf = request.getFile(fileName);
	    InputStream is = mpf.getInputStream();
	    log.debug("imageId: " + imageId);
	    log.debug("file name: " + mpf.getOriginalFilename());
	    log.debug("file size: " + mpf.getSize());
	    log.debug("file type: " + mpf.getContentType());
	    log.debug("file class: " + is.getClass().getName());

	    Image image;
	    if (imageId > 0) {
		image = imageManager.getImage(imageId);
		image.setName(mpf.getOriginalFilename());
		((ImageImpl) image).setInputStream(is);
		((ImageImpl) image).setSize((int) mpf.getSize());
	    } else {
		image = imageManager.createImage(objectType, objectId, mpf.getOriginalFilename(), mpf.getContentType(), is, (int) mpf.getSize());
		image.setUser(user);
	    }
	    log.debug(hasPermissions(image, user));
	    imageManager.saveImage(image);
	    list.add(image);
	}
	return list;
    }

    
    
    
    @RequestMapping(value = "/images/insert.json", method = RequestMethod.POST)
    @ResponseBody
    public Image updateImage(@RequestBody ImageImpl newImage, NativeWebRequest request) throws NotFoundException {
	User user = SecurityHelper.getUser();
	// return imageManager.getImage(imageId);
	return null;
    }

    @RequestMapping(value = "/images/upload.json", method = RequestMethod.POST)
    @ResponseBody
    public Image uploadImage(@RequestBody ImageImpl newImage, NativeWebRequest request) throws NotFoundException {
	User user = SecurityHelper.getUser();
	// return imageManager.getImage(imageId);
	return null;
    }

    
    
    
    @RequestMapping(value = "/images/link.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public ImageLink getImageLink(@RequestParam(value = "imageId", defaultValue = "0", required = true) Long imageId,
	    NativeWebRequest request) throws NotFoundException {
	User user = SecurityHelper.getUser();
	Image image = imageManager.getImage(imageId);
	if (hasPermissions(image, user)) {
	    return imageManager.getImageLink(image, true);
	}
	return imageManager.getImageLink(image);
    }

    @RequestMapping(value = "/images/properties/list.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public List<Property> getImagePropertyList(
	    @RequestParam(value = "imageId", defaultValue = "0", required = true) Long imageId,
	    NativeWebRequest request) throws NotFoundException {
	User user = SecurityHelper.getUser();
	Image image = imageManager.getImage(imageId);
	Map<String, String> properties = image.getProperties();
	return toList(properties);
    }

    @RequestMapping(value = "/images/properties/update.json", method = RequestMethod.POST)
    @ResponseBody
    public List<Property> updateImagePropertyList(
	    @RequestParam(value = "imageId", defaultValue = "0", required = true) Long imageId,
	    @RequestBody List<Property> newProperties, NativeWebRequest request) throws NotFoundException {
	User user = SecurityHelper.getUser();
	Image image = imageManager.getImage(imageId);
	Map<String, String> properties = image.getProperties();
	// update or create
	for (Property property : newProperties) {
	    properties.put(property.getName(), property.getValue().toString());
	}
	if (newProperties.size() > 0) {
	    imageManager.updateImageProperties(image);
	}
	return toList(properties);
    }

    @RequestMapping(value = "/images/properties/delete.json", method = { RequestMethod.POST, RequestMethod.DELETE })
    @ResponseBody
    public List<Property> deleteImagePropertyList(
	    @RequestParam(value = "imageId", defaultValue = "0", required = true) Long imageId,
	    @RequestBody List<Property> newProperties, NativeWebRequest request) throws NotFoundException {
	User user = SecurityHelper.getUser();

	Image image = imageManager.getImage(imageId);
	Map<String, String> properties = image.getProperties();
	log.debug(properties);
	log.debug(newProperties);
	for (Property property : newProperties) {
	    properties.remove(property.getName());
	}
	if (newProperties.size() > 0) {
	    log.debug(properties);
	    imageManager.updateImageProperties(image);
	}
	return toList(properties);
    }

    protected List<Property> toList(Map<String, String> properties) {
	List<Property> list = new ArrayList<Property>();
	for (String key : properties.keySet()) {
	    String value = properties.get(key);
	    list.add(new Property(key, value));
	}
	return list;
    }

    protected void updateImageProperties(Image image, Map<String, String> properties) {
	if (properties.size() > 0) {
	    image.setProperties(properties);
	    imageManager.updateImageProperties(image);
	}
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @RequestMapping(value = "/files/upload.json", method = RequestMethod.POST)
    @ResponseBody
    public List<Attachment> uploadFiles(
	    @RequestParam(value = "objectType", defaultValue = "2", required = false) Integer objectType,
	    @RequestParam(value = "fileId", defaultValue = "0", required = false) Long fileId,
	    MultipartHttpServletRequest request) throws NotFoundException, IOException {
	User user = SecurityHelper.getUser();
	Iterator<String> names = request.getFileNames();
	List<Attachment> list = new ArrayList<Attachment>();
	while (names.hasNext()) {
	    String fileName = names.next();
	    log.debug(fileName);
	    MultipartFile mpf = request.getFile(fileName);
	    InputStream is = mpf.getInputStream();
	    log.debug("fileId: " + fileId);
	    log.debug("file name: " + mpf.getOriginalFilename());
	    log.debug("file size: " + mpf.getSize());
	    log.debug("file type: " + mpf.getContentType());
	    log.debug("file class: " + is.getClass().getName());

	    Attachment attachment;
	    if (fileId > 0) {
		attachment = attachmentManager.getAttachment(fileId);
		attachment.setName(mpf.getOriginalFilename());
		((AttachmentImpl) attachment).setInputStream(is);
		((AttachmentImpl) attachment).setSize((int) mpf.getSize());
	    } else {
		attachment = attachmentManager.createAttachment(objectType, user.getUserId(), mpf.getOriginalFilename(),
			mpf.getContentType(), is, (int) mpf.getSize());
	    }

	    attachmentManager.saveAttachment(attachment);
	    list.add(attachment);
	}
	return list;
    }

    @RequestMapping(value = "/files/list.json", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public FileList getFileList(
	    @RequestParam(value = "objectType", defaultValue = "2", required = false) Integer objectType,
	    @RequestParam(value = "startIndex", defaultValue = "0", required = false) Integer startIndex,
	    @RequestParam(value = "pageSize", defaultValue = "0", required = false) Integer pageSize,
	    NativeWebRequest request) throws NotFoundException {
	User user = SecurityHelper.getUser();
	return getFileList(objectType, startIndex, pageSize, request.getNativeRequest(HttpServletRequest.class));
    }

    private FileList getFileList(int objectType, int startIndex, int pageSize, HttpServletRequest request)
	    throws NotFoundException {
	User user = SecurityHelper.getUser();
	long objectId = user.getUserId();
	if (objectType == 1) {
	    objectId = user.getCompanyId();
	} else if (objectType == 30) {
	    objectId = WebSiteUtils.getWebSite(request).getWebSiteId();
	}
	FileList list = new FileList();
	list.setTotalCount(attachmentManager.getTotalAttachmentCount(objectType, objectId));
	list.setFiles(attachmentManager.getAttachments(objectType, objectId));

	return list;
    }

    @RequestMapping(value = "/files/get.json}", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Attachment getFile(@RequestParam(value = "fileId", defaultValue = "0", required = true) Long fileId,
	    NativeWebRequest request) throws NotFoundException {
	User user = SecurityHelper.getUser();
	return attachmentManager.getAttachment(fileId);
    }

    @RequestMapping(value = "/files/properties/list.json", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public List<Property> getFileProperty(
	    @RequestParam(value = "fileId", defaultValue = "0", required = true) Long fileId, NativeWebRequest request)
	    throws NotFoundException {
	User user = SecurityHelper.getUser();

	Attachment attachment = attachmentManager.getAttachment(fileId);
	Map<String, String> properties = attachment.getProperties();
	return toList(properties);
    }

    @RequestMapping(value = "/files/properties/update.json", method = RequestMethod.POST)
    @ResponseBody
    public List<Property> updateFileProperty(
	    @RequestParam(value = "fileId", defaultValue = "0", required = true) Long fileId,
	    @RequestBody List<Property> newProperties, NativeWebRequest request) throws NotFoundException {
	User user = SecurityHelper.getUser();
	Attachment attachment = attachmentManager.getAttachment(fileId);
	Map<String, String> properties = attachment.getProperties();
	// update or create
	for (Property row : newProperties) {
	    properties.put(row.getName(), (String) row.getValue());
	}
	if (newProperties.size() > 0)
	    attachmentManager.saveAttachment(attachment);
	return toList(properties);
    }

    @RequestMapping(value = "/files/properties/delete.json", method = { RequestMethod.DELETE, RequestMethod.POST })
    @ResponseBody
    public List<Property> deleteFileProperty(
	    @RequestParam(value = "fileId", defaultValue = "0", required = true) Long fileId,
	    @RequestBody List<Property> newProperties, NativeWebRequest request) throws NotFoundException {
	User user = SecurityHelper.getUser();
	Attachment attachment = attachmentManager.getAttachment(fileId);
	Map<String, String> properties = attachment.getProperties();
	for (Property row : newProperties) {
	    properties.remove(row.getName());
	}
	if (newProperties.size() > 0)
	    attachmentManager.saveAttachment(attachment);
	return toList(properties);
    }

    public static class FileList {

	private List<Attachment> files;
	private int totalCount;

	/**
	 * @return files
	 */
	public List<Attachment> getFiles() {
	    return files;
	}

	/**
	 * @param files
	 *            설정할 files
	 */
	public void setFiles(List<Attachment> files) {
	    this.files = files;
	}

	/**
	 * @return totalCount
	 */
	public int getTotalCount() {
	    return totalCount;
	}

	/**
	 * @param totalCount
	 *            설정할 totalCount
	 */
	public void setTotalCount(int totalCount) {
	    this.totalCount = totalCount;
	}

    }

    public static class ItemList {

	private List<?> items;
	private int totalCount;

	/**
	 * @param items
	 * @param totalCount
	 */

	public ItemList(List<?> items, int totalCount) {
	    super();
	    this.items = items;
	    this.totalCount = totalCount;
	}

	/**
	 * @return items
	 */
	public List<?> getItems() {
	    return items;
	}

	/**
	 * @param items
	 *            설정할 items
	 */
	public void setItems(List<?> items) {
	    this.items = items;
	}

	/**
	 * @return totalCount
	 */
	public int getTotalCount() {
	    return totalCount;
	}

	/**
	 * @param totalCount
	 *            설정할 totalCount
	 */
	public void setTotalCount(int totalCount) {
	    this.totalCount = totalCount;
	}

    }

}

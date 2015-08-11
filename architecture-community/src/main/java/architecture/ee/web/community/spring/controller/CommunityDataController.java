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
package architecture.ee.web.community.spring.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import architecture.common.model.factory.ModelTypeFactory;
import architecture.common.user.SecurityHelper;
import architecture.common.user.User;
import architecture.common.user.authentication.UnAuthorizedException;
import architecture.ee.exception.NotFoundException;
import architecture.ee.web.attachment.AttachmentManager;
import architecture.ee.web.attachment.Image;
import architecture.ee.web.attachment.ImageManager;
import architecture.ee.web.community.announce.Announce;
import architecture.ee.web.community.announce.AnnounceManager;
import architecture.ee.web.community.announce.AnnounceNotFoundException;
import architecture.ee.web.community.announce.impl.DefaultAnnounce;
import architecture.ee.web.community.comment.Comment;
import architecture.ee.web.community.comment.CommentManager;
import architecture.ee.web.community.comment.CommentTreeWalker;
import architecture.ee.web.community.comment.DefaultComment;
import architecture.ee.web.community.page.DefaultBodyContent;
import architecture.ee.web.community.page.DefaultPage;
import architecture.ee.web.community.page.ImmutablePage;
import architecture.ee.web.community.page.Page;
import architecture.ee.web.community.page.PageManager;
import architecture.ee.web.community.page.PageState;
import architecture.ee.web.community.poll.DefaultPoll;
import architecture.ee.web.community.poll.Poll;
import architecture.ee.web.community.poll.PollManager;
import architecture.ee.web.community.poll.PollOption;
import architecture.ee.web.community.poll.PollStats;
import architecture.ee.web.community.stats.ViewCountManager;
import architecture.ee.web.community.streams.Photo;
import architecture.ee.web.community.streams.PhotoStreamsManager;
import architecture.ee.web.site.WebSiteNotFoundException;
import architecture.ee.web.spring.controller.MyCloudDataController.ItemList;
import architecture.ee.web.util.WebSiteUtils;
import architecture.ee.web.ws.Property;
import architecture.ee.web.ws.Result;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Controller ("community-data-controller")
@RequestMapping("/data")
public class CommunityDataController {
	private static final Log log = LogFactory.getLog(CommunityDataController.class);

	@Inject
	@Qualifier("photoStreamsManager")
	private PhotoStreamsManager photoStreamsManager ;
	
	@Inject
	@Qualifier("imageManager")
	private ImageManager imageManager ;
	
	@Inject
	@Qualifier("attachmentManager")
	private AttachmentManager attachmentManager;

	@Inject
	@Qualifier("announceManager")
	private AnnounceManager announceManager ;
	
	@Inject
	@Qualifier("pageManager")
	private PageManager pageManager;
	
	@Inject
	@Qualifier("viewCountManager")
	private ViewCountManager viewCountManager;	
	
	@Inject
	@Qualifier("commentManager")
	private CommentManager commentManager ;

	
	@Inject
	@Qualifier("pollManager")
	private PollManager pollManager;
	
	
	
	/**
	 * @return announceManager
	 */
	public AnnounceManager getAnnounceManager() {
		return announceManager;
	}

	/**
	 * @param announceManager 설정할 announceManager
	 */
	public void setAnnounceManager(AnnounceManager announceManager) {
		this.announceManager = announceManager;
	}

	/**
	 * @return photoStreamsManager
	 */
	public PhotoStreamsManager getPhotoStreamsManager() {
		return photoStreamsManager;
	}

	/**
	 * @param photoStreamsManager 설정할 photoStreamsManager
	 */
	public void setPhotoStreamsManager(PhotoStreamsManager photoStreamsManager) {
		this.photoStreamsManager = photoStreamsManager;
	}

	/**
	 * @return imageManager
	 */
	public ImageManager getImageManager() {
		return imageManager;
	}

	/**
	 * @param imageManager 설정할 imageManager
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
	 * @param attachmentManager 설정할 attachmentManager
	 */
	public void setAttachmentManager(AttachmentManager attachmentManager) {
		this.attachmentManager = attachmentManager;
	}
	
	@RequestMapping(value={"/pages/comment.json"},method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Result  addComment (
			@RequestParam(value="objectType", defaultValue="0", required=true ) Integer objectType,
			@RequestParam(value="objectId", defaultValue="0", required=true ) Long objectId,
			@RequestParam(value="text", defaultValue="", required=true ) String text,
			@RequestParam(value="name", defaultValue="", required=false ) String name,
			@RequestParam(value="email", defaultValue="", required=false ) String email,
			NativeWebRequest request ) throws NotFoundException {		
		
		User user = SecurityHelper.getUser();			
		String address = request.getNativeRequest(HttpServletRequest.class).getRemoteAddr();		
		
		Comment comment = commentManager.createComment(objectType, objectId, user, text);
		comment.setIPAddress(address);
		
		if(StringUtils.isNotEmpty(name))
			comment.setName(name);
		
		if(StringUtils.isNotEmpty(email))
			comment.setEmail(email);
		
		commentManager.addComment(comment);
		
		CommentTreeWalker walker = commentManager.getCommentTreeWalker(objectType, objectId);				
		
		return Result.newResult(walker.getRecursiveChildCount(commentManager.getRootParent()));
	}
	
	@RequestMapping(value={"/pages/comments/list.json"},method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public CommentList getPageComments (
			@RequestParam(value="pageId", defaultValue="0", required=true ) Long pageId,
			NativeWebRequest request 
			){
		
		int objectType = ModelTypeFactory.getTypeIdFromCode("PAGE") ;
		CommentTreeWalker walker = commentManager.getCommentTreeWalker(objectType, pageId);
		
		Comment PARENT = new DefaultComment();		
		CommentList list = new CommentList();
		list.setTotalCount(walker.getRecursiveChildCount(commentManager.getRootParent()));
		list.setComments( walker.recursiveChildren(commentManager.getRootParent()) );	
		return list;
	}	
	
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SITE_ADMIN' , 'ROLE_USER')")
	@RequestMapping(value="/pages/published/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public PageList  getPublishedPageList(
			@RequestParam(value="objectType", defaultValue="2", required=false ) Integer objectType,
			@RequestParam(value="state", defaultValue="NONE", required=false ) String state,
			@RequestParam(value="startIndex", defaultValue="0", required=false ) Integer startIndex,
			@RequestParam(value="pageSize", defaultValue="15", required=false ) Integer pageSize,
			NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();					
		return getPublishedPageList(objectType, startIndex, pageSize);
	}

	private PageList getPublishedPageList( int objectType, int startIndex, int pageSize){		
		PageList list = new PageList();
		list.setTotalCount(pageManager.getPageCount(objectType, PageState.PUBLISHED));
		
		List<Page> pages = new ArrayList(list.getTotalCount());
		for( Page page :  pageManager.getPages(objectType, PageState.PUBLISHED, startIndex, pageSize) )
		{
			pages.add(new ImmutablePage(page));
		}
		
		list.setPages(pages);
		
		return list;
	}
	
	
	private List<Page> toImmutablePageList(List<Page> list){
		List<Page> pages = new ArrayList(list.size());
		for( Page page :  list )
		{
			pages.add(new ImmutablePage(page));
		}
		return pages;
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SITE_ADMIN' , 'ROLE_USER')")
	@RequestMapping(value="/pages/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public PageList  getPageList(
			@RequestParam(value="objectType", defaultValue="2", required=false ) Integer objectType,
			@RequestParam(value="state", defaultValue="NONE", required=false ) String state,
			@RequestParam(value="startIndex", defaultValue="0", required=false ) Integer startIndex,
			@RequestParam(value="pageSize", defaultValue="15", required=false ) Integer pageSize,
			@RequestParam(value="full", defaultValue="true", required=false ) Boolean isFull,
			NativeWebRequest request ) throws NotFoundException {		
		
		User user = SecurityHelper.getUser();						
		long objectId = user.getUserId();		
		if( objectType == 1 ){
			objectId = user.getCompanyId();			
		}else if ( objectType == 30){
			objectId = WebSiteUtils.getWebSite(request.getNativeRequest(HttpServletRequest.class)).getWebSiteId();
		}	
		
		PageState pageState = PageState.valueOf(state.toUpperCase());	
		return getPageList(objectType, objectId, pageState, startIndex, pageSize, isFull);
	}
	
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value="/pages/get.json", method=RequestMethod.POST)
	@ResponseBody
	public Page getPage(
			@RequestParam(value="pageId", defaultValue="2", required=true ) Long pageId,
			@RequestParam(value="version", defaultValue="0", required=false ) Integer version,
			@RequestParam(value="count", defaultValue="0", required=false ) Integer count,
			NativeWebRequest request) throws NotFoundException{		
		
		User user = SecurityHelper.getUser();
		Page page = pageManager.getPage(pageId);
		
		
		if( page.getPageState() == PageState.PUBLISHED ){
			if( count > 0 )
				viewCountManager.addPageCount(page);
			return page;
		}
		
		if(page.getUser().getUserId() == user.getUserId()){
			return page;
		}
		
		throw new UnAuthorizedException();
	}	
	
			
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value="/pages/update_state.json", method=RequestMethod.POST)
	@ResponseBody
	public Page updatePageState(
			@RequestBody DefaultPage page, 
			NativeWebRequest request) throws NotFoundException{	
		
		User user = SecurityHelper.getUser();
		if(page.getUser() == null && page.getPageId() == 0)
			page.setUser(user);
		
		if( user.isAnonymous() || user.getUserId() != page.getUser().getUserId() )
			throw new UnAuthorizedException();
				
		Page target = pageManager.getPage(page.getPageId());
		target.setPageState(page.getPageState());
		pageManager.updatePage(target);
		return target;
	}
	
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value="/pages/update.json", method=RequestMethod.POST)
	@ResponseBody
	public Page updatePage(
			@RequestBody DefaultPage page, 
			NativeWebRequest request) throws NotFoundException{		
		User user = SecurityHelper.getUser();
		if(page.getUser() == null && page.getPageId() == 0)
			page.setUser(user);
		
		if( user.isAnonymous() || user.getUserId() != page.getUser().getUserId() )
			throw new UnAuthorizedException();
		
		boolean doUpdate = false;		
		Page target ;
		String tagsString = null;
		
		
		log.debug("page:" + page.getProperties());		
		
		
		if( page.getPageId() > 0){
			target = pageManager.getPage(page.getPageId());
			if( !StringUtils.equals(page.getName(), target.getName()) || 
					!StringUtils.equals(page.getTitle(), target.getTitle()) ||
					!StringUtils.equals(page.getSummary(), target.getSummary()) ||
				
					!StringUtils.equals(page.getBodyContent().getBodyText(), target.getBodyContent().getBodyText())){
				//target.setProperties(page.getProperties());

			}
			doUpdate = true;		
			log.debug( "do update ..." );
		}else{
			if( page.getObjectType() == 30 && page.getObjectId() == 0L ){
				page.setObjectId(WebSiteUtils.getWebSite(request.getNativeRequest(HttpServletRequest.class)).getWebSiteId());			
			}else if (page.getObjectType() == 1 && page.getObjectId() == 0L ){
				page.setObjectId(user.getCompanyId());		
			}else if (page.getObjectType() == 2 && page.getObjectId() == 0L ){
				page.setObjectId(user.getUserId());	
				
			}
			target =  new DefaultPage(page.getObjectType(), page.getObjectId());
			target.setUser(page.getUser());			
			target.setBodyContent(new DefaultBodyContent());
			//target.setProperties(page.getProperties());
			doUpdate = true;		
		}		
		
		tagsString = page.getProperty("tagsString", null);	
		if( tagsString != null )
			page.getProperties().remove("tagsString");
		
		if( doUpdate ){
			target.setName(page.getName());
			target.setTitle(page.getTitle());
			target.setSummary(page.getSummary());
			target.setBodyText(page.getBodyContent().getBodyText());
			target.setProperties(page.getProperties());			
			pageManager.updatePage(target);			
		}		
		
		if( tagsString != null && !StringUtils.equals(target.getTagDelegator().getTagsAsString(), tagsString) )
			target.getTagDelegator().setTags(tagsString);
		
		log.debug("input:" + page.getProperties());		
		log.debug("target:"+ target.getProperties());		
		
		return pageManager.getPage(target.getPageId());
	}
	
	@RequestMapping(value="/pages/properties/list.json", method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<Property>  getImageProperty(@RequestParam(value="pageId", defaultValue="0", required=true ) Long pageId, @RequestParam(value="versionId", defaultValue="1" ) Integer versionId, NativeWebRequest request ) throws NotFoundException {		
		
		User user = SecurityHelper.getUser();	
		if(pageId <= 0){
			return Collections.EMPTY_LIST;
		}		
		Page page = pageManager.getPage(pageId, versionId);
		Map<String, String> properties = page.getProperties();
		return toList(properties);
	}
		
	@RequestMapping(value="/pages/properties/update.json", method=RequestMethod.POST)
	@ResponseBody
	public List<Property>  updateImageProperty(
			@RequestParam(value="pageId", defaultValue="0", required=true ) Long pageId, 
			@RequestParam(value="versionId", defaultValue="1" ) Integer versionId, 
			@RequestBody List<Property> newProperties, NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();		
		Page page = pageManager.getPage(pageId, versionId);
		Map<String, String> properties = page.getProperties();		
		// update or create
		for (Property property : newProperties) {
			properties.put(property.getName(), property.getValue().toString());
		}		
		if( newProperties.size() > 0){
			pageManager.updatePage(page);
		}	
		return toList(properties);
	}

	@RequestMapping(value="/pages/properties/delete.json", method={RequestMethod.POST, RequestMethod.DELETE})
	@ResponseBody
	public List<Property>  deleteImageProperty(@RequestParam(value="pageId", defaultValue="0", required=true ) Long pageId, 
			@RequestParam(value="versionId", defaultValue="1" ) Integer versionId, @RequestBody List<Property> newProperties,  
			NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();		
		Page page = pageManager.getPage(pageId, versionId);
		Map<String, String> properties = page.getProperties();	
		log.debug(properties);		
		log.debug(newProperties);		
		for (Property property : newProperties) {
			properties.remove(property.getName());
		}
		if( newProperties.size() > 0){
			log.debug(properties);
			pageManager.updatePage(page);
		}		
		return toList(properties);
	}
	
	protected List<Property> toList (Map<String, String> properties){
		List<Property> list = new ArrayList<Property>();
		for (String key : properties.keySet()) {
			String value = properties.get(key);
			list.add(new Property(key, value));
		}
		return list;
	}
	
	private boolean isPageStateFilterEnabled(PageState pageState){
		if( pageState != PageState.NONE){
			return true;
		}else{
			return false;
		}
	}
	private PageList getPageList( int objectType, long objectId, PageState pageState, int startIndex, int pageSize, Boolean isFull){		
		
		PageList list = new PageList();	
		boolean byState = isPageStateFilterEnabled(pageState);
		
		List<Page> data ;
		int totalCount = 0 ;
		if(byState){
			data = pageManager.getPages(objectType, objectId, pageState, startIndex, pageSize);
			totalCount = pageManager.getPageCount(objectType, objectId, pageState);
		}else{
			data = pageManager.getPages(objectType, objectId, startIndex, pageSize);
			totalCount = pageManager.getPageCount(objectType, objectId);
		}
		
		if(isFull){
			list.setPages( data );	
		}else{
			
			list.setPages( toImmutablePageList( data ));		
		}
		list.setTotalCount(totalCount);		
		
		return list;
	}
	
	public static class PageList {		
		private List<Page> pages ;
		private int totalCount ;
		
		public PageList() {
		}
		/**
		 * @return pages
		 */
		public List<Page> getPages() {
			return pages;
		}
		/**
		 * @param pages 설정할 pages
		 */
		public void setPages(List<Page> pages) {
			this.pages = pages;
		}
		/**
		 * @return totalCount
		 */
		public int getTotalCount() {
			return totalCount;
		}
		/**
		 * @param totalCount 설정할 totalCount
		 */
		public void setTotalCount(int totalCount) {
			this.totalCount = totalCount;
		}
	}

	public static class CommentList {
		
		private List<Comment> comments ;
		private int totalCount ;		/**
		 * 
		 */
		public CommentList() {
		}
		/**
		 * @return pages
		 */
		public List<Comment> getComments() {
			return comments;
		}
		/**
		 * @param pages 설정할 pages
		 */
		public void setComments(List<Comment> comments) {
			this.comments = comments;
		}
		/**
		 * @return totalCount
		 */
		public int getTotalCount() {
			return totalCount;
		}
		/**
		 * @param totalCount 설정할 totalCount
		 */
		public void setTotalCount(int totalCount) {
			this.totalCount = totalCount;
		}
	}
	
	/**
	 * get streams photo by imageId
	 * 
	 * @param imageId
	 * @param request
	 * @return
	 * @throws NotFoundException
	 */
	@RequestMapping(value="/streams/photos/get.json", method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<Photo>  getStreamPhoto(@RequestParam(value="imageId", defaultValue="0", required=true ) Long imageId, NativeWebRequest request ) throws NotFoundException {		
		Image image = imageManager.getImage(imageId);
		return photoStreamsManager.getPhotosByImage(image);
	}

	/**
	 * get stream photo by linkId
	 * @param linkId
	 * @param request
	 * @return
	 * @throws NotFoundException
	 */
			
	@RequestMapping(value="/streams/photos/getByLink.json", method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public Photo getStreamPhotoByLink(@RequestParam(value="linkId", required=true ) String linkId, NativeWebRequest request ) throws NotFoundException {		
		return photoStreamsManager.getPhotoById(linkId);
	}
	
	
	@RequestMapping(value="/streams/photos/insert.json",method={RequestMethod.POST} )
	@ResponseBody
	public Result insertPhotoStream(@RequestParam(value="imageId", defaultValue="0", required=true ) Long imageId, NativeWebRequest request) throws NotFoundException {
		User user = SecurityHelper.getUser();		
		if(user.isAnonymous())
			throw new UnAuthorizedException();		
		
		Image image = imageManager.getImage(imageId);
		photoStreamsManager.addImage(image, user);
		return Result.newResult();
		
	}
	
	@RequestMapping(value="/streams/photos/delete.json",method={RequestMethod.POST} )
	@ResponseBody
	public void deletePhotosStreams(@RequestParam(value="imageId", defaultValue="0", required=true ) Long imageId, NativeWebRequest request) throws NotFoundException{		
		User user = SecurityHelper.getUser();		
		if(user.isAnonymous())
			throw new UnAuthorizedException();			
		Image image = imageManager.getImage(imageId);
		
		photoStreamsManager.deletePhotos(image, user);
	}
	
	
	@PreAuthorize("permitAll")
	@RequestMapping(value="/streams/photos/list_with_random.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public PhotoList  getStreamPhotoListByRandom(
			@RequestParam(value="objectType", defaultValue="2", required=false ) Integer objectType,
			@RequestParam(value="startIndex", defaultValue="0", required=false ) Integer startIndex,
			@RequestParam(value="pageSize", defaultValue="15", required=false ) Integer pageSize,
			NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();		
		return getStreamPhotoList(objectType, startIndex, pageSize, true, request.getNativeRequest(HttpServletRequest.class));
	}
	
	@PreAuthorize("permitAll")
	@RequestMapping(value="/streams/photos/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public PhotoList  getStreamPhotoList(
			@RequestParam(value="objectType", defaultValue="2", required=false ) Integer objectType,
			@RequestParam(value="startIndex", defaultValue="0", required=false ) Integer startIndex,
			@RequestParam(value="pageSize", defaultValue="15", required=false ) Integer pageSize,
			NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();		
		return getStreamPhotoList(objectType, startIndex, pageSize, false, request.getNativeRequest(HttpServletRequest.class));
	}
	
	private PhotoList getStreamPhotoList(int objectType, int startIndex, int pageSize, boolean random, HttpServletRequest request) throws NotFoundException{			
		User user = SecurityHelper.getUser();
		long objectId = user.getUserId();		
		if( objectType == 1 ){
			objectId = user.getCompanyId();			
		}else if ( objectType == 30){
			objectId = WebSiteUtils.getWebSite(request).getWebSiteId();
		}				
		PhotoList list = new PhotoList();
		
		if(objectType > 0 && objectId == 0)
			list.setTotalCount( photoStreamsManager.getPhotoCount(objectType ));
		else if (objectType > 0 && objectId > 0)
			list.setTotalCount( photoStreamsManager.getPhotoCount(objectType, objectId));		
		else
			list.setTotalCount(photoStreamsManager.getTotalPhotoCount());

		if(objectType > 0 && objectId == 0){
			if( random )
				list.setPhotos(photoStreamsManager.getPhotosByRandom(objectType, startIndex, pageSize));
			else
				list.setPhotos(photoStreamsManager.getPhotos(objectType, startIndex, pageSize));
		}else if (objectType > 0 && objectId > 0){
			if( random )
				list.setPhotos(photoStreamsManager.getPhotosByRandom(objectType, objectId, startIndex, pageSize));
			else
				list.setPhotos(photoStreamsManager.getPhotos(objectType, objectId, startIndex, pageSize));
		}else{
			if( random )
				list.setPhotos(photoStreamsManager.getPhotosByRandom(startIndex, pageSize));
			else
				list.setPhotos(photoStreamsManager.getPhotos(startIndex, pageSize));
		}
		return list;
	}
	
	
	@RequestMapping(value="/images/upload_by_url.json", method=RequestMethod.POST)
	@ResponseBody
	public Image  uploadImageByUrl(@RequestBody UrlImageUploader uploader, NativeWebRequest request ) throws NotFoundException, IOException {		
		
		User user = SecurityHelper.getUser();
		int objectType = uploader.getObjectType();
		long objectId = uploader.getObjectId();
		if( objectType == 2){
			objectId = user.getUserId();	
		}
		else if( objectType == 1 ){
			objectId = user.getCompanyId();			
		}else if ( objectType == 30){
			objectId = WebSiteUtils.getWebSite(request.getNativeRequest(HttpServletRequest.class)).getWebSiteId();
		}	
		Image imageToUse = imageManager.createImage(objectType, objectId,  uploader.getFileName(),  uploader.getContentType(),  uploader.readFileFromUrl());
		
		if( uploader.getSourceUrl() == null){
			uploader.setSourceUrl(uploader.getImageUrl());
		}
		imageToUse.getProperties().put("source", uploader.getSourceUrl().toString());
		imageToUse.getProperties().put("url", uploader.getImageUrl().toString());		
		log.debug(imageToUse);
		
		return  imageManager.saveImage(imageToUse);
	}
	
	public static class UrlImageUploader {
		
		 private int objectType = 2;
		 
		 private URL sourceUrl ;		 
		 
		 private URL imageUrl ;
		 
		 private long objectId = 0 ;
		 
		 @JsonIgnore
		 private String contentType;
		 
		 public void setObjectId(long objectId){
			 this.objectId = objectId;
		 }
		 
		 public long getObjectId(){
			return this.objectId;
		 }
		 
		/**
		 * @return sourceUrl
		 */
		 
		 
		public URL getSourceUrl() {
			return sourceUrl;
		}

		/**
		 * @return objectType
		 */
		public int getObjectType() {
			return objectType;
		}

		/**
		 * @param objectType 설정할 objectType
		 */
		public void setObjectType(int objectType) {
			this.objectType = objectType;
		}

		/**
		 * @param sourceUrl 설정할 sourceUrl
		 */
		public void setSourceUrl(URL sourceUrl) {
			this.sourceUrl = sourceUrl;
		}

		/**
		 * @return imageUrl
		 */
		public URL getImageUrl() {
			return imageUrl;
		}

		/**
		 * @param imageUrl 설정할 imageUrl
		 */
		public void setImageUrl(URL imageUrl) {
			this.imageUrl = imageUrl;
		}
				
		public String getContentType(){			
			if(contentType == null){
				Tika tika = new Tika();
				try {
					contentType = tika.detect(imageUrl);
				} catch (IOException e) {
					contentType = null;
				}
			}
			return contentType;
		}		
		
		public String getFileName(){
			return FilenameUtils.getName(imageUrl.getFile());
		}
		
		public File readFileFromUrl() throws IOException{		
			File temp = File.createTempFile(UUID.randomUUID().toString(), ".tmp");
			temp.deleteOnExit();
			FileUtils.copyURLToFile(imageUrl, temp);			
			return temp;
		}
	}	
	
	public static class PhotoList {
		
		private List<Photo> photos ;
		private int totalCount ;


		/**
		 * @return photos
		 */
		public List<Photo> getPhotos() {
			return photos;
		}
		/**
		 * @param photos 설정할 photos
		 */
		public void setPhotos(List<Photo> photos) {
			this.photos = photos;
		}
		/**
		 * @return totalCount
		 */
		public int getTotalCount() {
			return totalCount;
		}
		/**
		 * @param totalCount 설정할 totalCount
		 */
		public void setTotalCount(int totalCount) {
			this.totalCount = totalCount;
		}	
		
	}

	
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value="/announce/update.json", method=RequestMethod.POST)
	@ResponseBody
	public Announce saveAnnounce(
			@RequestBody DefaultAnnounce announce, 
			NativeWebRequest request) throws AnnounceNotFoundException, WebSiteNotFoundException{		
		User user = SecurityHelper.getUser();
		if(announce.getUser() == null && announce.getAnnounceId() == 0)
			announce.setUser(user);
		
		if( user.isAnonymous() || user.getUserId() != announce.getUser().getUserId() )
			throw new UnAuthorizedException();
				
		Announce target ;
		if( announce.getAnnounceId() > 0){
			target = announceManager.getAnnounce(announce.getAnnounceId());
		}else{
			if( announce.getObjectType() == 30 && announce.getObjectId() == 0L ){
				announce.setObjectId(WebSiteUtils.getWebSite(request.getNativeRequest(HttpServletRequest.class)).getWebSiteId());			
			}else if (announce.getObjectType() == 1 && announce.getObjectId() == 0L ){
				announce.setObjectId(user.getCompanyId());		
			}
			target = announceManager.createAnnounce(user, announce.getObjectType() , announce.getObjectId());
		}
		
		target.setSubject(announce.getSubject());
		target.setBody(announce.getBody());	
		target.setStartDate( announce.getStartDate());
		target.setEndDate(announce.getEndDate());
		if(target.getAnnounceId() > 0 ){
			announceManager.updateAnnounce(target);		
		}else{
			announceManager.addAnnounce(target);
		}		
		return target;
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value="/announce/delete.json", method=RequestMethod.POST)
	@ResponseBody
	public Boolean destoryAnnounce(@RequestParam(value="announceId", defaultValue="0", required=true ) Long announceId, NativeWebRequest request){
		User user = SecurityHelper.getUser();
			
		
		return true;
	}
	
	
	
	@PreAuthorize("permitAll")
	@RequestMapping(value="/announce/list.json",method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public AnnounceList  getAnnounceList (
		@RequestParam(value="objectType", defaultValue="30", required=false ) Integer objectType, 
		@RequestParam(value="objectId", defaultValue="0", required=false  ) Long objectId, 
		@RequestParam(value="startIndex", defaultValue="0", required=false ) Integer startIndex,
		@RequestParam(value="pageSize", defaultValue="0", required=false ) Integer pageSize,
		@RequestParam(value="startDate", required=false ) @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") Date startDate,
		@RequestParam(value="endDate", required=false ) @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") Date endDate,		
		NativeWebRequest request ) throws NotFoundException {		
		User user = SecurityHelper.getUser();		
		if(!user.isAnonymous()){
			if( objectType == 30 && objectId == 0L ){
				objectId = WebSiteUtils.getWebSite(request.getNativeRequest(HttpServletRequest.class)).getWebSiteId();			
			}else if (objectType == 1 && objectId == 0L ){
				objectId = user.getCompanyId();		
			}
		}else{
			objectType = 30 ;
			objectId = WebSiteUtils.getWebSite(request.getNativeRequest(HttpServletRequest.class)).getWebSiteId();		
		}		
		
		if( startDate == null )
			startDate = Calendar.getInstance().getTime();
		if (endDate == null)
			endDate = Calendar.getInstance().getTime();
		
		return new AnnounceList(announceManager.getAnnounces(objectType, objectId, startDate, endDate), getTotalAnnounceCount(objectType, objectId, startDate, endDate));
	}
		
	private int getTotalAnnounceCount(int objectType, long objectId , Date startDate, Date endDate){		
		if(startDate != null ){
			return announceManager.getAnnounceCount(objectType, objectId, startDate, endDate == null ? Calendar.getInstance().getTime() : endDate);
		}
		return announceManager.getAnnounceCount(objectType, objectId, endDate == null ? Calendar.getInstance().getTime() : endDate);
	}	
	
	
	public static class AnnounceList {
		private List<Announce> announces ;
		private int totalCount ;		
		/**
		 * @param announces
		 * @param totalCount
		 */
		public AnnounceList(List<Announce> announces, int totalCount) {
			super();
			this.announces = announces;
			this.totalCount = totalCount;
		}
		/**
		 * @return announces
		 */
		public List<Announce> getAnnounces() {
			return announces;
		}
		/**
		 * @param announces 설정할 announces
		 */
		public void setAnnounces(List<Announce> announces) {
			this.announces = announces;
		}
		/**
		 * @return totalCount
		 */
		public int getTotalCount() {
			return totalCount;
		}
		/**
		 * @param totalCount 설정할 totalCount
		 */
		public void setTotalCount(int totalCount) {
			this.totalCount = totalCount;
		}
	} 

	/** ======================================== **/
	/**  POLL					                                                    * */
	/** ======================================== **/
	@RequestMapping(value={"/polls/list.json"},method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public ItemList getPolls (			
			@RequestParam(value="objectType", defaultValue="0", required=false ) Integer objectType,
			@RequestParam(value="objectId", defaultValue="0", required=false ) Long objectId,
			@RequestParam(value="active", defaultValue="false", required=false ) Boolean active,
			@RequestParam(value="live", defaultValue="false", required=false ) Boolean live,
			NativeWebRequest request 
			){		
		User user = SecurityHelper.getUser();
		ItemList list ;
		if( objectType > 0 && objectId > 0 ){			
			 list = new ItemList(pollManager.getPolls(objectType, objectId), pollManager.getPollCount(objectType, objectId) );		
		}else{
			 list = new ItemList(pollManager.getPolls(user), pollManager.getPollCount(user) );
		}
		return list;		
	}	
	
	@RequestMapping(value={"/polls/stats/list.json"},method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public ItemList getStatsPolls (			
			@RequestParam(value="objectType", defaultValue="0", required=false ) Integer objectType,
			@RequestParam(value="objectId", defaultValue="0", required=false ) Long objectId,
			@RequestParam(value="active", defaultValue="false", required=false ) Boolean active,
			@RequestParam(value="live", defaultValue="false", required=false ) Boolean live,
			NativeWebRequest request 
			){		

		User user = SecurityHelper.getUser();
		
		List<PollStats> items = new ArrayList<PollStats>();
		int pollCount = 0;

		if( objectType > 0 && objectId > 0 ){		
			pollCount = pollManager.getPollCount(objectType, objectId);
			for( Poll p : pollManager.getPolls(objectType, objectId)){
				items.add(pollManager.getPollStats(p, user));
			}
		}else{
			pollCount = pollManager.getPollCount(user);
			for( Poll p : pollManager.getPolls(user)){
				items.add(pollManager.getPollStats(p, user));
			}		
		}
				
		return new ItemList(items, pollCount);		
	}	
	
	@RequestMapping(value={"/polls/stats/get.json"},method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public PollStats getStatsPoll (			
			@RequestParam(value="pollId", defaultValue="0", required=true ) Long pollId,
			NativeWebRequest request 
			) throws UnAuthorizedException, NotFoundException{		
		User user = SecurityHelper.getUser();
		Poll poll = pollManager.getPoll(pollId);
		return pollManager.getPollStats(poll, user);
	}
	 

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value="/polls/update.json", method=RequestMethod.POST)
	@ResponseBody
	public Poll updatePoll(@RequestBody DefaultPoll poll, NativeWebRequest request) throws NotFoundException{		
		
		User user = SecurityHelper.getUser();
		if(poll.getUser() == null && poll.getPollId() < 1)
			poll.setUser(user);
		
		if( user.isAnonymous() || user.getUserId() != poll.getUser().getUserId() )
			throw new UnAuthorizedException();
		
		boolean doUpdate = false;
		
		if( poll.getPollId() > 0){
			DefaultPoll orgPoll = (DefaultPoll) pollManager.getPoll(poll.getPollId());
			orgPoll.setName(poll.getName());
			orgPoll.setDescription(poll.getDescription());
			orgPoll.setStartDate(poll.getStartDate());
			orgPoll.setEndDate(poll.getEndDate());
			orgPoll.setExpireDate(poll.getExpireDate());
			orgPoll.setMode(poll.getMode());
			orgPoll.setStatus(poll.getStatus());
			
			pollManager.updatePoll(orgPoll);
			return orgPoll;
		}else{
			return pollManager.createPoll(poll.getObjectType(), poll.getObjectId(), user, poll.getName());
		}
	}
	
	
	@RequestMapping(value={"/polls/options/list.json"},method={RequestMethod.POST, RequestMethod.GET} )
	@ResponseBody
	public List<PollOption> getPollOptions(
			@RequestParam(value="pollId", defaultValue="0", required=true ) Long pollId, 
			NativeWebRequest request) throws UnAuthorizedException, NotFoundException{	
		Poll poll = pollManager.getPoll(pollId);
		return pollManager.getPollOptions(poll);		
	}	
	
	@RequestMapping(value="/polls/options/update.json", method=RequestMethod.POST)
	@ResponseBody
	public Result updatePollOptions(
			@RequestParam(value="pollId", defaultValue="0", required=true ) Long pollId, 
			@RequestBody List<PollOption> options) throws UnAuthorizedException, NotFoundException{
		
		Poll poll = pollManager.getPoll(pollId);
		pollManager.setPollOptions(poll, options);
		return Result.newResult();
		
	}
	
	@RequestMapping(value="/polls/options/delete.json", method=RequestMethod.POST)
	@ResponseBody
	public Result deletePollOptions(
			@RequestParam(value="pollId", defaultValue="0", required=true ) Long pollId, 
			@RequestBody List<PollOption> options) throws UnAuthorizedException, NotFoundException{
		
		Poll poll = pollManager.getPoll(pollId);
		pollManager.deletePollOptions(poll, options);
		return Result.newResult();
		
	}
	
}
package architecture.ee.web.contact.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.user.User;
import architecture.ee.web.contact.Contact;
import architecture.ee.web.contact.ContactGroup;
import architecture.ee.web.contact.ContactGroupNotFoundException;
import architecture.ee.web.contact.ContactManager;
import architecture.ee.web.contact.ContactNotFoundException;
import architecture.ee.web.contact.ContactUtil;
import architecture.ee.web.contact.Node;
import architecture.ee.web.contact.Tag;
import architecture.ee.web.contact.dao.ContactDao;
import architecture.ee.web.contact.dao.ContactGroupDao;
import architecture.ee.web.contact.dao.TagDao;

public class ContactManagerImpl implements ContactManager{
	
	private ContactDao contactDao;
	private ContactGroupDao contactGroupDao;
	private TagDao tagDao;
	private Cache contactCache;
	private Cache contactGroupCache;
	private Cache contactGroupMapCache;
	
	protected Log log = LogFactory.getLog(getClass());
	
	
	/*******************************************************************************************/
	/************************************* Private Memebers ************************************/
	/*******************************************************************************************/
	
	private void updateCache( Contact contact ){
		contactCache.put(new Element( contact.getContactId(), contact));		
	}
	
	private void updateCache( ContactGroup contactGroup){
		contactGroupCache.put(new Element( contactGroup.getGroupId(), contactGroup));		
	}
	
	private void updateCache( Map contactGroupMap){
		contactGroupMapCache.put(new Element( contactGroupMap.get("CONTACT_ID"), contactGroupMap));		
	}
	
	
	
	/*******************************************************************************************/
	/*************************************** Getter & Setter ***************************************/
	/*******************************************************************************************/
	public Cache getContactGroupCache() {
		return contactGroupCache;
	}


	public void setContactGroupCache(Cache contactGroupCache) {
		this.contactGroupCache = contactGroupCache;
	}

	public ContactGroupDao getContactGroupDao() {
		return contactGroupDao;
	}



	public void setContactGroupDao(ContactGroupDao contactGroupDao) {
		this.contactGroupDao = contactGroupDao;
	}
	public TagDao getTagDao() {
		return tagDao;
	}



	public void setTagDao(TagDao tagDao) {
		this.tagDao = tagDao;
	}



	public ContactDao getContactDao() {
		return contactDao;
	}



	public void setContactDao(ContactDao contactDao) {
		this.contactDao = contactDao;
	}



	public Cache getContactCache() {
		return contactCache;
	}



	public void setContactCache(Cache contactCache) {
		this.contactCache = contactCache;
	}



	public Cache getContactGroupMapCache() {
		return contactGroupMapCache;
	}



	public void setContactGroupMapCache(Cache contactGroupMapCache) {
		this.contactGroupMapCache = contactGroupMapCache;
	}

	
	
	/*******************************************************************************************/
	/***************************************** Contact *****************************************/
	/*******************************************************************************************/
	public Contact getContact(long contactId) throws ContactNotFoundException {
		Contact contact = null; 
		if(contactCache.get(contactId) != null){
			contact = (Contact)contactCache.get(contactId).getValue();
		}
		if(contact == null){
			contact = contactDao.load(contactId);
			updateCache(contact);
		}
				
		return contact;
	}
	
	public int getContactsCount(Contact contact) {
		return contactDao.getContactsCount(contact);
	}
	
	public List<Contact> getContacts(List<Long> contactIds) {
		List <Contact> list = new ArrayList<Contact>();
		if(contactIds != null){
			for(Long contactId: contactIds){
				try{
					Contact item = getContact(contactId);
					list.add(item);
				} catch(ContactNotFoundException e){
					log.equals(e);
				}
			}
		}
		return list;
	}
	
	public List<Contact> getContacts(Contact contact) {
		List <Long> contactIds = contactDao.getContactIds(contact);
		return getContacts(contactIds);
	}
	
	
	
	public List<Contact> getContacts( Contact contact, int startIndex, int pageSize) {
		List <Long> contactIds = contactDao.getContactIds(contact, startIndex, pageSize);
		return getContacts(contactIds);
	}
	
	
	
	public void addContact(Contact contact) {
		log.debug("Contact Manager addContact excuting.........");
		/* STEP 1. 연락처 추가 */
		contactDao.insert(contact);
		
		/* STEP 2. 연락처-그룹 매핑 추가 */
		String[] groupIds = contact.getGroupIds().split(",");
		if(groupIds.length > 0 && !groupIds[0].equals("")){
			for(int i=0; i < groupIds.length; i++){
				ContactGroup contactGroup = new ContactGroupImpl();
				contactGroup.setGroupId(Long.parseLong(groupIds[i]));
				contactDao.insertContactGroupMap(contact, contactGroup);
			}
		}
		
		/* STEP 3. 태그 / 연락처-태그 매핑 추가 */
		String[] tagNames = contact.getTag().split(","); // xxx, yyy, zzz
		log.debug("tag length : " + tagNames.length);
		if(tagNames.length > 0 && !tagNames[0].equals("")){
			for(int i=0; i < tagNames.length; i++){
				Tag tag = new TagImpl();
				tag.setTagName(tagNames[i]);
				tag.setCompanyId(contact.getCompanyId());
				tag.setUserId(contact.getUserId());
				// 3-1. 태그 테이블에 삽입한다. 
				log.debug("==================  3-1");
				Long tagId = tagDao.getTagIdByTagName(tag); // 현재 태그가 기존에 등록이 된 태그인지 확인한다.
				if (tagId < 0){
					// if tagId 가 0보다 작다면 등록되지 않은 태그이므로  태그테이블에 삽입한다.
					tag.setTagId(tagId);
					tagDao.insert(tag);
				}else{ //등록된 경우라면 등록된 tagId를 tag 객체에 세팅한다. 
					tag.setTagId(tagId);
				}
				
				// 3-2. 태그 매핑 테이블에 삽입한다. 
				contactDao.insertContactTagMap(contact, tag);
			}
		}
	}
	
	
	public void updateContact(Contact contact) {
		// STEP1
		contactDao.update(contact);
		
		// STEP2
		String[] groupIds = contact.getGroupIds().split(",");
		if(groupIds.length > 0 && !groupIds[0].equals("")){
			contactDao.deleteContactGroupMapByContactId(contact);
			for(int i=0; i < groupIds.length; i++){
				ContactGroup contactGroup = new ContactGroupImpl();
				contactGroup.setGroupId(groupIds[i].equals("")? 0 : Long.parseLong(groupIds[i]));
				contactDao.insertContactGroupMap(contact, contactGroup);
			}
		}
		
		// STEP3
		String[] tagNames = contact.getTag().split(",");  		
		if(tagNames.length > 0 && !tagNames[0].equals("")){
			contactDao.deleteContactTagMapByContactId(contact);
			for(int i=0; i < tagNames.length; i++){
				Tag tag = new TagImpl();
				tag.setTagName(tagNames[i]);
				tag.setCompanyId(contact.getCompanyId());
				tag.setUserId(contact.getUserId());
				Long tagId = tagDao.getTagIdByTagName(tag);
				if (tagId < 0){
					tag.setTagId(tagId);
					tagDao.insert(tag);
				}else{ 
					tag.setTagId(tagId);
				}
				contactDao.insertContactTagMap(contact, tag);
			}
		}
		
	}
	
	// STEP 1. 태그테이블에서 태그이름들에 해당되는 태그 ID 목록을 가져온다. (tagIds) 
	// STEP 2. 연락처-태그 매핑 테이블에서 연락처 ID 목록을 가져온다. (1의결과를 통해. contactIds)
	// STEP 3. 연락처 ID 목록을 통해 연락처 리스트를 가져온다. (2의결과를 통해. contacts)
	public List<Contact> getContactsByTagNames(Contact contact){
		List<Contact> result = new ArrayList<Contact>();
		
		log.debug("getContactsByTagNames ==========  STEP 1");
		List<Long> tagIds = tagDao.getTagIdsByTagNames(contact.getTag());
		
		List<Long> contactIds = new ArrayList<Long>();
		if(ContactUtil.notEmpty(tagIds)){
			log.debug("getContactsByTagNames ==========  STEP 2");
			contactIds = contactDao.getContactIdsByTagIds(tagIds);
		}
		
		if(ContactUtil.notEmpty(contactIds)){
			log.debug("getContactsByTagNames ==========  STEP 3");
			result = getContacts(contactIds);
		}
		return result;
	}
	
	// STEP 1. 태그이름을 통해 연락처 목록을 얻는다. (By  getContactsByTagNames method)
	// STEP 2.(PIVOT) 연락처-그룹 매핑 테이블에서 매핑된 연락처 ID / 그룹ID 목록을 가져온다. (1의 결과를 통해)
	// STEP 3. 그룹 ID 목록을 통해 그룹리스트를 가져온다. (2의 결과를 통해)
	// STEP 4. 결과 리스트를 생성한다. (2의 결과리스트를 피벗으로, 1과 3의 데이터 리스트에서 데이터를 세팅한다)  
	public List<Contact> getContactsWithGroupsByTagNames(Contact contact){
		
		List<Contact> result = new ArrayList<Contact>(); 
		
		log.debug("getContactsWithGroupsByTagNames ==============  STEP 1");
		List<Contact> contacts = getContactsByTagNames(contact); 
		
		if(ContactUtil.notEmpty(contacts)){
			log.debug("getContactsWithGroupsByTagNames ==============  STEP 2");
			Map <Long, Contact> contactsMap = ContactUtil.getContactMapFromList(contacts);
			List<Long> contactIds = new ArrayList<Long>(contactsMap.keySet());
			List<Map<String,Long>> pivot = contactDao.getContactGroupIdsByContactIds(contactIds);
			
			if(ContactUtil.notEmpty(pivot)){
				log.debug("getContactsWithGroupsByTagNames ==============  STEP 3");
				Set<Long> groupIds = new HashSet<Long>();
				for(Map<String,Long> m : pivot){
					Long groupId = m.get("GROUP_ID");
					groupIds.add(groupId);
				}
				List<ContactGroup> groups = getContactGroups(new ArrayList<Long>(groupIds));
				Map<Long, ContactGroup> groupsMap = ContactUtil.getContactGroupMapFromList(groups);
				
				log.debug("getContactsWithGroupsByTagNames ==============  STEP 4");
				for(Map<String,Long> m : pivot){
					Contact src1 = contactsMap.get(m.get("CONTACT_ID"));
					ContactGroup src2 = groupsMap.get(m.get("GROUP_ID"));
					
					Contact c = new ContactImpl();
					c.setCompanyId(src1.getCompanyId());
					c.setContactDesc(src1.getContactDesc());
					c.setCellPhone(src1.getCellPhone());
					c.setPhone(src1.getPhone());
					c.setContactId(src1.getContactId());
					c.setEmail(src1.getEmail());
					c.setName(src1.getName());
					c.setTag(src1.getTag());
					c.setTypeCode(src1.getTypeCode());
					c.setTypeName(src1.getTypeName());
					c.setContactGroup(src2);
					
					result.add(c);
				}
			}
		}

		return result;
	}
	

	public void deleteContact(Contact contact) {
		
	}
	
	/*
	public List<ContactGroup> getTargetContactGroups(ContactGroup group) {
		
		return contactGroupDao.getTargetContactGroups(group);
	}
	*/
	
	
	
		/**
		[
		{groupId: 1, parentGroupId : null, groupName : "HRD센터"},
		{groupId: 2, parentGroupId : 1, groupName : "영업 1팀"},
		{groupId: 3, parentGroupId : 1, groupName : "영업 2팀"},
		{groupId: 4, parentGroupId : null, groupName : "교육아웃소싱"},
		{groupId: 5, parentGroupId : 4, groupName : "위탁교육"},
		{groupId: 6, parentGroupId : 4, groupName : "EM 서비스"},
		{groupId: 7, parentGroupId : 4, groupName : "강의장 임대"}
		]
		식의 스키마를 가지고 있는 데이터 리스트를 
	
		{
		 groupId : 1, groupName : "HRD 센터", items: [
		 			{ groupId: 3, groupName : "영업1팀"},
		 			{ groupId: 4, groupName : "영업2팀"}
		 		]
		 },
		 {
		  groupId : 4, groupName : "교육아웃소싱", items: [
		  		{ groupId: 5, groupName : "위탁교육"},
		  		{ groupId: 6, groupName : "EM 서비스"},
		  		{ groupId: 7, groupName : "강의장 임대"}
		  	]
		 }
	
		형식으로 변환한다. 
	*/
	public List<Node> getContactGroupNodes(ContactGroup group){
		List<Node> result = new ArrayList<Node>();
		
		log.debug("getContactGroupNodes ==========  STEP 1. 기초 데이터 조회 ");
		List<ContactGroup> list = getContactGroups(group);
		
		log.debug("getContactGroupNodes ==========  STEP 2. 자식 그룹 번호 세팅");
		ContactUtil.setChildGroupIds(list);
		
		log.debug("getContactGroupNodes ==========  STEP 3. Node List 생성");
		List<Node> nodeList= ContactUtil.getNodeListFromGroupList(list, group.getGroupIds());
		Map<Long, Node> nodeMap = ContactUtil.getMapFromNodeList(nodeList);
 
		for(Node n : nodeList){ 
			// 3-1. 자식이 있다면 자식의 레퍼런스를 세팅
			List<Long> childIds = n.getChildIds();
			if(childIds.size() > 0){
				log.debug("getContactGroupNodes ==========  STEP 3-1. " + n.getText() + "의 자식 노드 레퍼런스 생성");
				n.setHasChildren(true);
				List<Node> items = n.getItems();
				for(Long childId : childIds){
					items.add(nodeMap.get(childId));
				}
				n.setItems(items);	
			}
			if(n.getLev() == 1){
				log.debug("getContactGroupNodes ==========  STEP 3-2. 최상위 노드" + n.getText() + "를 최종 결과 리스트에 삽입");
				result.add(n);
			}
		}
		
		return result;
		
		
		/*for(ContactGroup g : list){
		List<Long> childIdList = g.getChildGroupIds(); 
		if(childIdList.size() > 0){ //자식이 있으면
			Node parentNode = new Node();
			parentNode.setId(g.getGroupId());
			parentNode.setText(g.getGroupName());
			parentNode.setHasChildren(true);
			List<Node> childNodeList = parentNode.getItems(); 
			log.debug("부모노드 " + parentNode.getText() + "에 자식노드를 삽입합니다.");
			for(Long childId: childIdList){
				ContactGroup child = groupMap.get(childId); 
				Node childNode = new Node();
				childNode.setId(child.getGroupId());
				childNode.setText(child.getGroupName());
				childNodeList.add(childNode);
				log.debug("자식노드 : " + childNode.getText());
			}
			result.add(parentNode);
		}
	}*/
	}
	
	
	
	
	
	public Contact createContact(User user) { 
		return new ContactImpl(user);  
	}
	
	
	/*******************************************************************************************/
	/*************************************** ContactGroup ***************************************/
	/*******************************************************************************************/
	public ContactGroup getContactGroup(long groupId) throws ContactGroupNotFoundException {
		ContactGroup group = null; 
		if(contactGroupCache.get(groupId) != null){
			group = (ContactGroup)contactGroupCache.get(groupId).getValue();
		}
		if(group == null){
			group = contactGroupDao.load(groupId);
			updateCache(group);
		}
				
		return group;
	}

	public List<ContactGroup> getContactGroups(ContactGroup group) {
		List <Long> groupIds = contactGroupDao.getContactGroupIds(group);
		
		return getContactGroups(groupIds);
	}
	
	public List<ContactGroup> getContactGroups(List<Long> groupIds) {
		List <ContactGroup> list = new ArrayList<ContactGroup>();
		if(groupIds != null){
			for(Long groupId: groupIds){
				try{
					ContactGroup item = getContactGroup(groupId);
					list.add(item);
				} catch(ContactGroupNotFoundException e){
					log.equals(e);
				}
			}
		}
		return list;
	}
	
}

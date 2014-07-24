package architecture.ee.web.contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import architecture.common.user.User;
import architecture.ee.web.community.struts2.action.PageAction;
import architecture.ee.web.contact.impl.ContactGroupImpl;
import architecture.ee.web.contact.impl.ContactImpl;
import architecture.ee.web.util.ParamUtils;

public class ContactAction extends PageAction {

	//private Contact contact;
	//private ContactGroup group;
	private ContactManager contactManager;
	private int pageSize = 0 ;
	private int startIndex = 0 ;  
	private int typeCode = -1;
	private Long companyId = -1L;
	private Long contactId = -1L;
	private String groupIds = "";
	
	
	
	
	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}

	public Long getContactId() {
		return contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}

	public int getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(int typeCode) {
		this.typeCode = typeCode;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public ContactManager getContactManager() {
		return contactManager;
	}

	public void setContactManager(ContactManager contactManager) {
		this.contactManager = contactManager;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public List<Contact> getTargetContacts(){
		ParamUtils.printParameter(request, log);
		log.debug( "startIndex= " + startIndex + ", pageSize=" + pageSize  );
		
		Contact contact = new ContactImpl();
		//ContactGroup group = new ContactGroupImpl();
		
		if( typeCode < 0 || companyId < 0 ){    
			return Collections.EMPTY_LIST;
		}else{
			contact.setTypeCode(typeCode);
			contact.setCompanyId(companyId);
			
			if (pageSize > 0){
				return contactManager.getContacts(contact, startIndex, pageSize); 
			}else{
				/* STEP 1. 연락처 목록 조회 */
				
				List<Contact> contactList = contactManager.getContacts(contact);
				 
				/* STEP 2. 연락처 그룹 목록 조회*/
				/*group.setTypeCode(typeCode);
				group.setCompanyId(companyId);
				log.debug("group.companyId = " + group.getCompanyId());
				log.debug("group.typeCode = " + group.getTypeCode());
				List<ContactGroup> contactGroupList = contactGroupManager.getTargetContactGroups(group);
				*/
				/*for(Contact c : contactList){
					long groupId = c.getGroupId();
					List<String> groupNames = new ArrayList<String>();
					for(ContactGroup g : contactGroupList){
						if(groupId == g.getGroupId() || groupId == g.getParentGroupId()){
							groupNames.add(g.getGroupName());
						}
					}
					c.setGroupNames(groupNames);
				}
				
				//데이터 확인
				for(Contact c : contactList){
					c.showGroupNames(log);
				}
				*/
				return contactList;
			}
		}
	}
	
	
	public Contact getTargetContact() throws ContactNotFoundException{
		if( contactId > 0){
			return contactManager.getContact(contactId);
		}else{
			return contactManager.createContact(getUser());
		}
	}
	
	
	
	 // 연락처 신규 등록 & 수정
	/**
	 * 1) 연락처 그룹매핑
	 * 2) 연락처
	 * 3) 태그  
	 * 4) 연락처 태그매핑
	 * 데이터를 삽입 / 갱신한다. 
	 * */
	public String saveContact() throws Exception {
		ParamUtils.printParameter(request, log);
		Map map = ParamUtils.getJsonParameter(request, "item", Map.class);
		
		User sessionUser = getUser();
		Integer selectedContactId = (Integer)map.get("contactId");
		if(selectedContactId != null){
			contactId = selectedContactId.longValue();
		}
		
		Contact targetContact = getTargetContact();
		String name = (String)map.get("name");
		String phone = (String)map.get("phone");
		String cellPhone = (String)map.get("cellPhone");
		String email = (String)map.get("email");
		String contactDesc = (String)map.get("contactDesc");
		String tag = (String)map.get("tag");
		Integer typeCode= (Integer)map.get("typeCode");
		Long companyId = sessionUser.getCompanyId();
		String groupIds = (String)map.get("groupIds"); 
		
		
		targetContact.setName(name);
		targetContact.setPhone(phone);
		targetContact.setCellPhone(cellPhone);
		targetContact.setEmail(email);
		targetContact.setContactDesc(contactDesc);
		targetContact.setTag(tag);
		targetContact.setCompanyId(companyId);
		targetContact.setTypeCode(typeCode);
		targetContact.setGroupIds(groupIds);
		
		
		if(targetContact.getContactId() > 0){ 
			log.debug("==============  update Contact : " + contactId);
			contactManager.updateContact(targetContact);
		}else{
			log.debug("==============  news Contact ");
			contactManager.addContact(targetContact);
		}
		
		return success();
	}
	
	public int getTargetContactsCount(){
		ParamUtils.printParameter(request, log);
		Contact contact = new ContactImpl();
		contact.setCompanyId(companyId);
		return contactManager.getContactsCount(contact);
	}
	
	@Override
	public String execute() throws Exception {
		log.debug("EXCUTE ============================");
		return success();
	}
	
	
	public List<Node> getTargetContactGroups() {
		ParamUtils.printParameter(request, log);
		ContactGroup contactGroup = new ContactGroupImpl();
		contactGroup.setTypeCode(typeCode);
		contactGroup.setGroupIds(groupIds);
		contactGroup.setCompanyId(getUser().getCompanyId());
		//List<ContactGroup> list = contactGroupManager.getTargetContactGroups(contactGroup); 
		List<Node> list = contactManager.getContactGroupNodes(contactGroup); 
		return list;
	}

	public List<Contact> getContactsByTagNames(){
		log.debug("================ getContactsByTagNames ==================");
		ParamUtils.printParameter(request, log);
		Contact contact = new ContactImpl();
		Map map = ParamUtils.getJsonParameter(request, "item", Map.class);
		
		String tag = (String)map.get("tag");
		contact.setTag(tag);
		
		String srchType = (String)map.get("srchType");
		
		User user = getUser();
		contact.setCompanyId(user.getCompanyId() > 0 ? user.getCompanyId() : 1);
		
		if(srchType.equals("1")){
			return contactManager.getContactsByTagNames(contact);
		}else{
			return contactManager.getContactsWithGroupsByTagNames(contact); 
		}
		
	}
	
	public String getHtmlCodeContactsByTagNames(){
		log.debug("================ getHtmlCodeContactsByTagNames ==================");
		ParamUtils.printParameter(request, log);
		Contact contact = new ContactImpl();
		Map map = ParamUtils.getJsonParameter(request, "item", Map.class);
		
		String tag = (String)map.get("tag");
		contact.setTag(tag);
		
		String srchType = (String)map.get("srchType");
		
		User user = getUser();
		contact.setCompanyId(user.getCompanyId() > 0 ? user.getCompanyId() : 1);
		
		List<Contact> list = contactManager.getContactsWithGroupsByTagNames(contact); 
		List<List<TableCell>> trans = ContactUtil.getTableCellListFromContactList(list);
		trans = ContactUtil.getSpanedList(trans);
		String htmlCode = ContactUtil.getHtmlTableFromTableCellList(trans);
		log.debug(htmlCode);
		return htmlCode;
	}
	
}

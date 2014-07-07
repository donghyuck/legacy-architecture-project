package architecture.ee.web.contact.dao;

import java.util.List;
import java.util.Map;

import architecture.ee.web.contact.Contact;
import architecture.ee.web.contact.ContactGroup;
import architecture.ee.web.contact.ContactNotFoundException;
import architecture.ee.web.contact.Tag;


public interface ContactDao {
	public abstract Long nextId() ;
	
	public abstract List<Long> getContactIds (Contact contact);
	
	public abstract List<Long> getContactIds (Contact contact, int startIndex, int pageSize);
	
	public abstract int getContactsCount(Contact contact);
	
	public abstract Contact load (Long contactId) throws ContactNotFoundException;
	
	public abstract void update( Contact contact );
	
	public abstract void insert( Contact contact);
	
	public abstract void delete( Contact contact );
	
	public abstract void deleteContactTagMapByContactId( Contact contact );
	
	public abstract void deleteContactGroupMapByContactId( Contact contact );
	
	public abstract void insertContactTagMap ( Contact contact, Tag tag);
	
	public abstract void insertContactGroupMap ( Contact contact, ContactGroup contactGroup);
	
	public abstract List<Long> getContactIdsByTagIds(List<Long> tagIds);  
	
	public abstract List<Long> getGroupIdsByContactIds(List<Long> contactIds);
	
	public abstract List<Map<String,Long>> getContactGroupIdsByContactIds(List<Long> contactIds);  
	
}

package architecture.ee.web.contact;

import java.util.List;
import java.util.Map;

import architecture.common.user.User;


public interface ContactManager {

		
		/** CONTACT */
		public void addContact(Contact contact);
		
		public abstract void updateContact(Contact contact);
		
		public abstract void deleteContact(Contact contact);
		
		public Contact createContact(User user);
		
		public Contact getContact(long contactId) throws ContactNotFoundException;
		
		public List<Contact> getContacts(Contact contact);
		
		public List<Contact> getContacts(List<Long> contactIds) ;
		
		public List<Contact> getContacts( Contact contact, int startIndex, int pageSize);
		
		public int getContactsCount(Contact contact);
		
		public List<Contact> getContactsByTagNames(Contact contact);
		
		public List<Contact> getContactsWithGroupsByTagNames(Contact contact);
		
		/** CONTACT GROUP */
		//public List<ContactGroup> getTargetContactGroups(ContactGroup group);
		
		public List<Node> getContactGroupNodes(ContactGroup group);
		
		
}

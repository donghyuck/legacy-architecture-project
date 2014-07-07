package architecture.ee.web.contact.dao;

import java.util.List;

import architecture.ee.web.contact.ContactGroup;
import architecture.ee.web.contact.ContactGroupNotFoundException;

public interface ContactGroupDao {

	// public List<ContactGroup> getTargetContactGroups(ContactGroup group);

	public abstract ContactGroup load (Long groupId) throws ContactGroupNotFoundException;
	
	public abstract List<Long> getContactGroupIds (ContactGroup group);
}

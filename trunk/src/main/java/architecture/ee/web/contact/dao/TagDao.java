package architecture.ee.web.contact.dao;

import java.util.List;

import architecture.ee.web.contact.Contact;
import architecture.ee.web.contact.Tag;

public interface TagDao {
	public abstract void insert(Tag tag);
	
	public abstract Long getTagIdByTagName(Tag tag);
	
	public List<Long> getTagIdsByTagNames(String tagStrings);
}

package architecture.ee.web.contact.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;

import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.contact.Contact;
import architecture.ee.web.contact.ContactGroup;
import architecture.ee.web.contact.ContactNotFoundException;
import architecture.ee.web.contact.Tag;
import architecture.ee.web.contact.dao.ContactDao;
import architecture.ee.web.contact.impl.ContactGroupImpl;
import architecture.ee.web.contact.impl.ContactImpl;

public class JdbcContactDao extends ExtendedJdbcDaoSupport implements ContactDao {

	private ExtendedPropertyDao extendedPropertyDao;
	private String sequencerName = "CONTACT";
	private String propertyTableName = "V2_CONTACT_PROPERTY";
	private String propertyPrimaryColumnName = "CONTACT_ID";
	
	
	
	
	public String getSequencerName() {
		return sequencerName;
	}

	public void setSequencerName(String sequencerName) {
		this.sequencerName = sequencerName;
	}

	public ExtendedPropertyDao getExtendedPropertyDao() {
		return extendedPropertyDao;
	}

	public void setExtendedPropertyDao(ExtendedPropertyDao extendedPropertyDao) {
		this.extendedPropertyDao = extendedPropertyDao;
	}

	

	public String getPropertyTableName() {
		return propertyTableName;
	}

	public void setPropertyTableName(String propertyTableName) {
		this.propertyTableName = propertyTableName;
	}

	public String getPropertyPrimaryColumnName() {
		return propertyPrimaryColumnName;
	}

	public void setPropertyPrimaryColumnName(String propertyPrimaryColumnName) {
		this.propertyPrimaryColumnName = propertyPrimaryColumnName;
	}

	public Long nextId() {
		
		return getNextId(sequencerName);
	}
	
	private final RowMapper<Contact> contactMapper = new RowMapper<Contact>(){
		public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {
			ContactImpl row = new ContactImpl();

			row.setContactId(rs.getLong("CONTACT_ID"));
			row.setName(rs.getString("NAME"));
			row.setEmail(rs.getString("EMAIL"));
			row.setPhone(rs.getString("PHONE"));
			row.setCellPhone(rs.getString("CELL_PHONE"));
			row.setTag(rs.getString("TAG"));
			row.setContactDesc(rs.getString("CONTACT_DESC"));
			row.setCreationDate(rs.getDate("CREATION_DATE"));
			row.setUserId(rs.getLong("CREATE_ID"));
			row.setModifiedDate(rs.getDate("MODIFIED_DATE"));
			row.setModifyId(rs.getLong("MODIFY_ID"));
			row.setDelYn(rs.getInt("DEL_YN") == 0 ? true:false);
			row.setCompanyId(rs.getLong("COMPANY_ID"));
			row.setTypeCode(rs.getInt("TYPE_CODE"));
			row.setTypeName(rs.getString("TYPE_NAME"));
			return row;
		}
	};
	
	private final RowMapper<Long> idMapper =  new RowMapper<Long>(){
		public Long mapRow(ResultSet rs, int rowNum) throws SQLException{
			return rs.getLong(1);
		} 
	};
	
	/*
	private final RowMapper<ContactGroup> contactGroupMapper = new RowMapper<ContactGroup>(){
		public ContactGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
			ContactGroupImpl row = new ContactGroupImpl();
			row.setGroupId(rs.getLong("GROUP_ID"));
			row.setParentGroupId(rs.getLong("PARENT_GROUP_ID"));
			row.setGroupName(rs.getString("GROUP_NAME"));
			row.setCompanyId(rs.getLong("COMPANY_ID"));
			row.setTypeCode(rs.getInt("TYPE_CODE"));
			row.setTypeName(rs.getString("TYPE_NAME"));
			row.setCreationDate(rs.getDate("CREATION_DATE"));
			row.setUserId(rs.getLong("CREATE_ID"));
			row.setModifiedDate(rs.getDate("MODIFIED_DATE"));
			row.setModifyId(rs.getLong("MODIFY_ID"));
			row.setDelYn(rs.getInt("DEL_YN") == 0 ? true:false);
			return row;
		}
	};
	 */
	

	public void insert(Contact contact) {
		log.debug("Contact Dao INSERT excuting.........");
		long contactIdToUse = contact.getContactId();
		if( contactIdToUse < 0){
			contactIdToUse = getNextId(sequencerName); // next sequence
		}
		contact.setContactId(contactIdToUse);
		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_CONTACT.INSERT_CONTACT").getSql(),
				new SqlParameterValue(Types.NUMERIC, contact.getContactId()), 
				new SqlParameterValue(Types.VARCHAR, contact.getName()),
				new SqlParameterValue(Types.VARCHAR, contact.getEmail()),
				new SqlParameterValue(Types.VARCHAR, contact.getPhone()),
				new SqlParameterValue(Types.VARCHAR, contact.getCellPhone()),
				new SqlParameterValue(Types.VARCHAR, contact.getTag()),
				new SqlParameterValue(Types.VARCHAR, contact.getContactDesc()),
				new SqlParameterValue(Types.NUMERIC, contact.getUserId()),
				new SqlParameterValue(Types.NUMERIC, contact.getCompanyId()),
				new SqlParameterValue(Types.NUMERIC, contact.getTypeCode())
				);
		
		//setTopicProperties(topic.getTopicId(), topic.getProperties()); 
	}
	public void update(Contact contact) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_CONTACT.UPDATE_CONTACT").getSql(), 	
				new SqlParameterValue(Types.VARCHAR, contact.getName()),
				new SqlParameterValue(Types.VARCHAR, contact.getEmail()),
				new SqlParameterValue(Types.VARCHAR, contact.getPhone()),
				new SqlParameterValue(Types.VARCHAR, contact.getCellPhone()),
				new SqlParameterValue(Types.VARCHAR, contact.getTag()),
				new SqlParameterValue(Types.VARCHAR, contact.getContactDesc()),
				new SqlParameterValue(Types.NUMERIC, contact.getUserId()),
				new SqlParameterValue(Types.NUMERIC, contact.getTypeCode()),
				new SqlParameterValue(Types.NUMERIC, contact.getContactId())
		);		
		
		//setTopicProperties(topic.getTopicId(), topic.getProperties());	
	}
	
	public Contact load(Long contactId) throws ContactNotFoundException {
		try{
			Contact contact =  getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_CONTACT.GET_CONTACT").getSql(),
					contactMapper,
					new SqlParameterValue(Types.NUMERIC,contactId));
			
			List<Long> groupIds = getExtendedJdbcTemplate().query(getBoundSql("ARCHITECTURE_CONTACT.SELECT_MAPPING_GROUP_IDS_BY_CONTACT_ID").getSql(),
					idMapper,
					new SqlParameterValue(Types.NUMERIC, contact.getContactId()));
			
			contact.setGroupIds(StringUtils.join(groupIds, ","));
			return contact;
			
		}catch(DataAccessException e){
			e.printStackTrace();
			throw new ContactNotFoundException(e);
		}
	}

	public int getContactsCount(Contact contact) {
		return getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_CONTACT.GET_CONTACTS_COUNT_BY_COMPANY_ID").getSql(),
				new Object[] {contact.getCompanyId()},
				Integer.class);
	}
	
	public List<Long> getContactIds(Contact contact) {
		return getExtendedJdbcTemplate().query(
				getBoundSql("ARCHITECTURE_CONTACT.SELECT_CONTACT_IDS_BY_COMPANY_ID").getSql(),
				idMapper,
				new SqlParameterValue(Types.NUMERIC,contact.getCompanyId())
				//,new SqlParameterValue(Types.NUMERIC,contact.getTypeCode())
				);
	}
	
	public List<Long> getContactIds(Contact contact, int startIndex, int pageSize) {
		return getExtendedJdbcTemplate().queryScrollable(
				getBoundSql("ARCHITECTURE_CONTACT.SELECT_CONTACT_IDS_BY_COMPANY_ID").getSql(),
			 	startIndex, 
			 	pageSize,
			 	//new Object[] {contact.getCompanyId(), contact.getTypeCode()},
			 	new Object[] {contact.getCompanyId()},
				new int[] {Types.NUMERIC},
				Long.class
				);
	}
	
	public void delete(Contact contact) {
		getJdbcTemplate().update(
				getBoundSql("ARCHITECTURE_CONTACT.DELETE_CONTACT" ).getSql(),
				new SqlParameterValue (Types.NUMERIC, contact.getUserId()),
				new SqlParameterValue (Types.NUMERIC, contact.getContactId())
		);
		//deleteTopicProperties(topic.getTopicId());	
		
	}
	
	public void insertContactTagMap(Contact contact, Tag tag) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_CONTACT.INSERT_CONTACT_TAG_MAP").getSql(), 	
				new SqlParameterValue(Types.NUMERIC, contact.getContactId()),
				new SqlParameterValue(Types.NUMERIC, tag.getTagId()),
				new SqlParameterValue(Types.NUMERIC, contact.getUserId())
		);		
	}
	
	public void insertContactGroupMap(Contact contact, ContactGroup contactGroup) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_CONTACT.INSERT_CONTACT_GROUP_MAP").getSql(),
				new SqlParameterValue(Types.NUMERIC, contactGroup.getGroupId()),
				new SqlParameterValue(Types.NUMERIC, contact.getContactId()),
				new SqlParameterValue(Types.NUMERIC, contact.getUserId())
		);	
	}
	
	public void deleteContactTagMapByContactId(Contact contact) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_CONTACT.DELETE_CONTACT_TAG_MAP_BY_CONTACT_ID").getSql(), 
				new SqlParameterValue(Types.NUMERIC, contact.getContactId())
		);
	}
	
	public void deleteContactGroupMapByContactId(Contact contact) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_CONTACT.DELETE_CONTACT_GROUP_MAP_BY_CONTACT_ID").getSql(), 
				new SqlParameterValue(Types.NUMERIC, contact.getContactId())
		);
	}

	public List<Long> getContactIdsByTagIds(List<Long> tagIds) {
		Map options = new HashMap();
		options.put("tagIds", tagIds);

		List<Long> result =  getExtendedJdbcTemplate().query(getBoundSqlWithAdditionalParameter("ARCHITECTURE_CONTACT.GET_CONTACT_IDS_BY_TAG_IDS", options).getSql(),
				new RowMapper<Long>(){
						public Long mapRow(ResultSet rs, int rowNum) throws SQLException{
							return rs.getLong(1);
						} 
					}
				);
		return result;
	}
	
	public List<Long> getGroupIdsByContactIds(List<Long> contactIds) {
		Map options = new HashMap();
		options.put("contactIds", contactIds);
		
		List<Long> result =  getExtendedJdbcTemplate().query(getBoundSqlWithAdditionalParameter("ARCHITECTURE_CONTACT.GET_GROUP_IDS_BY_CONTACT_IDS", options).getSql(),
				idMapper
				);
		
		return result;
	} 
	
	public List<Map<String,Long>> getContactGroupIdsByContactIds(List<Long> contactIds) {
		Map options = new HashMap(); 
		options.put("contactIds", contactIds);
		
		List<Map<String,Long>> result =  getExtendedJdbcTemplate().query(
				getBoundSqlWithAdditionalParameter("ARCHITECTURE_CONTACT.GET_GROUP_IDS_BY_CONTACT_IDS", options).getSql(),
				new RowMapper<Map<String,Long>>(){
							public Map<String,Long> mapRow(ResultSet rs, int rowNum) throws SQLException{
								Map<String,Long> map = new HashMap<String,Long>();
								map.put("CONTACT_ID", rs.getLong("CONTACT_ID"));
								map.put("GROUP_ID", rs.getLong("GROUP_ID"));
								return map;
							}
				}
		);
		return result;
	}
}

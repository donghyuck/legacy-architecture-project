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
import architecture.ee.web.contact.ContactGroupNotFoundException;
import architecture.ee.web.contact.ContactNotFoundException;
import architecture.ee.web.contact.dao.ContactGroupDao;
import architecture.ee.web.contact.impl.ContactGroupImpl;
import architecture.ee.web.contact.impl.ContactImpl;

public class JdbcContactGroupDao extends ExtendedJdbcDaoSupport implements ContactGroupDao{

	private ExtendedPropertyDao extendedPropertyDao;
	private String sequencerName = "CONTACT_GROUP";
	private String propertyTableName = "V2_CONTACT_GROUP_PROPERTY";
	private String propertyPrimaryColumnName = "GROUP_ID";
	
	
	
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
	
	public List<ContactGroup> getTargetContactGroups() {
		return null;
	}

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
			//row.setParentGroupName(rs.getString("PARENT_GROUP_NAME"));
			//row.setLev(rs.getInt("LEV"));
			return row;
		}
	};
	
	private final RowMapper<Long> idMapper =  new RowMapper<Long>(){
		public Long mapRow(ResultSet rs, int rowNum) throws SQLException{
			return rs.getLong(1);
		} 
	};
	
	
	/*
	public List<ContactGroup> getTargetContactGroups(ContactGroup group) {
		List<ContactGroup> list = null;
		
		Map options = new HashMap();
		if(group.getTypeCode() > 0){
			options.put("typeCode", group.getTypeCode());
		}else{
			options.put("typeCode", -1);
		}
		list =  getExtendedJdbcTemplate().query(
				getBoundSqlWithAdditionalParameter("ARCHITECTURE_CONTACT.SELECT_CONTACT_GROUP_LIST_BY_COMPANY_ID_AND_TYPE_CODE", options).getSql(),
				contactGroupMapper,
				new SqlParameterValue(Types.NUMERIC,group.getCompanyId())
				);
		return list;
	}
	*/
	
	
	public ContactGroup load(Long groupId) throws ContactGroupNotFoundException {
		try{
			ContactGroup group =  getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_CONTACT.GET_CONTACT_GROUP").getSql(),
					contactGroupMapper,
					new SqlParameterValue(Types.NUMERIC,groupId));
			
			return group;
			
		}catch(DataAccessException e){
			e.printStackTrace();
			throw new ContactGroupNotFoundException(e);
		}
	}
	
	public List<Long> getContactGroupIds(ContactGroup group) {
		List<Long> list = null;
		Map options = new HashMap();
		if(group.getTypeCode() > 0){
			options.put("typeCode", group.getTypeCode());
		}else{
			options.put("typeCode", -1);
		}
		list =  getExtendedJdbcTemplate().query(
				getBoundSqlWithAdditionalParameter("ARCHITECTURE_CONTACT.GET_CONTACT_GROUP_IDS_BY_COMPANY_ID_AND_TYPE_CODE", options).getSql(),
				idMapper,
				new SqlParameterValue(Types.NUMERIC,group.getCompanyId())
				);
		
		return list;
	}
	
	
}

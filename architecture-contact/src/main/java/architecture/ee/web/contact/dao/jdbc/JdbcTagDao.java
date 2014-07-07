package architecture.ee.web.contact.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.util.StringUtils;

import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.contact.Tag;
import architecture.ee.web.contact.dao.TagDao;

public class JdbcTagDao extends ExtendedJdbcDaoSupport implements TagDao{
	private ExtendedPropertyDao extendedPropertyDao;
	private String sequencerName = "TAG";
	private String propertyTableName = "V2_TAG_PROPERTY";
	private String propertyPrimaryColumnName = "TAG_ID";
	
	
	
	
	public ExtendedPropertyDao getExtendedPropertyDao() {
		return extendedPropertyDao;
	}




	public void setExtendedPropertyDao(ExtendedPropertyDao extendedPropertyDao) {
		this.extendedPropertyDao = extendedPropertyDao;
	}




	public String getSequencerName() {
		return sequencerName;
	}




	public void setSequencerName(String sequencerName) {
		this.sequencerName = sequencerName;
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




	public void insert(Tag tag) {
		long tagIdToUse = tag.getTagId();
		if( tagIdToUse < 0){
			tagIdToUse = getNextId(sequencerName); // next sequence
		}
		tag.setTagId(tagIdToUse);
		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_CONTACT.INSERT_TAG").getSql(),
				new SqlParameterValue(Types.NUMERIC, tag.getTagId()), 
				new SqlParameterValue(Types.VARCHAR, tag.getTagName()),
				new SqlParameterValue(Types.NUMERIC, tag.getUserId()),
				new SqlParameterValue(Types.NUMERIC, tag.getCompanyId())
				);
	}
	
	public Long getTagIdByTagName(Tag tag) {
		Object tagCnt = getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_CONTACT.GET_TAG_CNT_BY_TAG_NAME").getSql(),
				new Object[] {tag.getTagName()},
				Integer.class);
		if((Integer)tagCnt > 0){
			Object tagId = getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_CONTACT.GET_TAG_ID_BY_TAG_NAME").getSql(),
					new Object[] {tag.getTagName()},
					Long.class);
			return (Long)tagId;
		}else{
			return -1L;
		}
	}
	
	
	public List<Long> getTagIdsByTagNames(String tagStrings) {
		
		//String[] tags = StringUtils.commaDelimitedListToStringArray(tagStrings); //tagStrings.split(",");
		String[] tags = StringUtils.trimAllWhitespace(tagStrings).split(",");
		/* => FreeMarker do this processes
		String[] tagsToUse = new String[tags.length];
		for(int i=0; i < tags.length; i++ ){
			tagsToUse[i] = "'" + tags[i] + "'";
		}
		
		String queryString = "";
		for(int i=0; i < tags.length; i++ ){
			queryString += "\\\"" + tags[i] + "\\\"";
			if(i != tags.length -1){
				queryString += ",";
			}
			// How about using StringBuilder instance? 
		}*/
		//log.debug("Query String ============ " + queryString);
		Map options = new HashMap();
		options.put("tags", tags);

		return getExtendedJdbcTemplate().query(getBoundSqlWithAdditionalParameter("ARCHITECTURE_CONTACT.GET_TAG_IDS_BY_TAG_NAMES", options).getSql(), 
				new RowMapper<Long>(){
						public Long mapRow(ResultSet rs, int rowNum) throws SQLException{
							return rs.getLong(1);
						} 
		});
		
	}
}

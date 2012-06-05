package architecture.ee.user.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;

import architecture.ee.model.impl.ProfileFieldValueModelImpl;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.user.User;
import architecture.ee.user.dao.UserProfileDao;
import architecture.ee.user.profile.ProfileField;
import architecture.ee.user.profile.ProfileFieldValue;
import architecture.ee.user.profile.ProfileFieldValueCount;
import architecture.ee.user.profile.impl.ProfileFieldValueImpl;

public class JdbcUserProfileDao extends ExtendedJdbcDaoSupport implements UserProfileDao {

	public Map<Long, ProfileFieldValue> getProfile(User user) {		
		Map<Long, ProfileFieldValue> profileMap = new HashMap<Long, ProfileFieldValue>();		
		List<ProfileFieldValue> valueList = getExtendedJdbcTemplate().query(
			getBoundSql("FRAMEWORK_V2.SELECT_USER_PROFILE_VALUES").getSql(), 
			new Object[]{user.getUserId()}, 
			new RowMapper<ProfileFieldValue>(){
					public ProfileFieldValue mapRow(ResultSet rs, int rowNum) throws SQLException {						
						ProfileFieldValueImpl model = new ProfileFieldValueImpl(rs.getLong(1), rs.getInt(2));
						model.setValue(rs.getString(3));
						model.setPrimaryIndex(rs.getInt(4));
						model.setList(rs.getInt(5) ==1 ? true : false );
						return (ProfileFieldValue)model;
			}} );		
		buildProfileFieldValueMap(valueList, profileMap);
		return profileMap;
	}
	
	private void buildProfileFieldValueMap(List<ProfileFieldValue> valuesList, Map<Long, ProfileFieldValue> profileMap){		
		for(ProfileFieldValue field : valuesList){			
			ProfileField.Type profileFieldType = ProfileField.Type.valueOf(field.getFieldTypeId());
			if(profileFieldType == ProfileField.Type.MULTILIST || field.isList())
			{					
				if(profileMap.containsKey(field.getFieldId())){
					ProfileFieldValue pfv = profileMap.get(field.getFieldId());
					if(field.isPrimaryValue()){
						List<String> values = pfv.getValues();
	                    values.add(field.getValue());
	                    pfv.setValues(values, values.size() - 1);
					}else{
						pfv.getValues().add(field.getValue());
					}
				} else
                {
					ProfileFieldValueImpl pfv = new ProfileFieldValueImpl(field.getFieldId(), field.getFieldTypeId());
                    List<String> values = new ArrayList<String>();
                    values.add(field.getValue());
                    if(field.isPrimaryValue()){
                        pfv.setValues(values, 0);
                    }else{
                        pfv.setValues(values);
                    }
                    profileMap.put(field.getFieldId(), (ProfileFieldValue)pfv);
                }
			}else{
				ProfileFieldValueImpl pfv = new ProfileFieldValueImpl(field.getFieldId(), field.getFieldTypeId());
                pfv.setValue(field.getValue());
                profileMap.put(field.getFieldId(), (ProfileFieldValue)pfv);	
			}
		}		
	}	

	public void setProfile(User user, Collection<ProfileFieldValue> profile) {		
		try {			
			getExtendedJdbcTemplate().update(
				getBoundSql("FRAMEWORK_V2.DELETE_USER_PROFILE_VALUES_BY_USER").getSql(), 
				new Object[]{user.getUserId()}, 
				new int[]{Types.INTEGER});					
			for(ProfileFieldValue up : profile ){
				if(up.getValues() != null){
					//int idx = 0 ;
					for(String value : (List<String>) up.getValues()){
						//USER_ID, FIELD_ID, PRIMARY_VALUE, VALUE
						getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.INSERT_USER_PROFILE_VALUE").getSql(), 
							new Object[]{user.getUserId(), up.getFieldId(), up.isPrimaryValue() ? 1 : 0, value }, 
							new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER , Types.VARCHAR });
					}
				}else{
					getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.INSERT_USER_PROFILE_VALUE").getSql(), 
							new Object[]{user.getUserId(), up.getFieldId(), 0, up.getValue()}, 
							new int[]{Types.INTEGER, Types.INTEGER, Types.INTEGER , Types.VARCHAR });
				}
			}
		} catch (DataAccessException e) {
            String msg = (new StringBuilder()).append("Could not set profile values for user ").append(user.getUserId()).toString();
            log.error(msg, e);
            throw e;
		}
	}
	
	public void deleteProfileByUserID(long userId) {		
		try {
			getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.DELETE_USER_PROFILE_VALUES_BY_USER").getSql(), new Object[]{userId}, new int[]{Types.INTEGER});
		} catch (DataAccessException e) {
            String msg = (new StringBuilder()).append("Could not delete user profile for user ").append(userId).toString();
            log.error(msg, e);
		}
	}
	
	public void deleteProfileById(long fieldId) {
		// DELETE_USER_PROFILE_VALUES_BY_PROFILE
		try {
			getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.DELETE_USER_PROFILE_VALUES_BY_USER").getSql(), new Object[]{fieldId}, new int[]{Types.INTEGER});
		} catch (DataAccessException e) {
            String msg = (new StringBuilder()).append("Could not delete user profile for profile ").append(fieldId).toString();
            log.error(msg, e);
            throw e;
		}
	}	

	public Collection<ProfileFieldValueCount> getProfileFieldTermsByFieldId(long fieldId, boolean enabledOnly) {
		try {
			return getExtendedJdbcTemplate().query(getBoundSqlWithAdditionalParameter("FRAMEWORK_V2.SELECT_PROFILE_FIELD_DISTINCT_VALUES_PER_FIELD", enabledOnly ).getSql(), new Object[]{fieldId},
			    new RowMapper<ProfileFieldValueCount>(){

					public ProfileFieldValueCount mapRow(ResultSet rs, int rowNum) throws SQLException {
						ProfileFieldValue model = (ProfileFieldValue) new ProfileFieldValueModelImpl(rs.getLong(1), rs.getInt(2));
						model.setValue(rs.getString(3));					
						return new ProfileFieldValueCount(model, rs.getInt(4));
					}}		
			);
		} catch (DataAccessException e) {
	        String msg = (new StringBuilder()).append("Could get profile terms for profile field ").append(fieldId).toString();
	        log.error(msg, e);
	        throw e;
		}
	}	
}

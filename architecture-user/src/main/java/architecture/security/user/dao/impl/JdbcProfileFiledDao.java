package architecture.security.user.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;

import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.security.user.dao.ProfileFieldDao;
import architecture.security.user.profile.ProfileField;
import architecture.security.user.profile.ProfileFieldOption;
import architecture.security.user.profile.impl.ProfileFieldImpl;
import architecture.security.user.profile.impl.ProfileFieldOptionImpl;

public class JdbcProfileFiledDao  extends ExtendedJdbcDaoSupport implements ProfileFieldDao {

	private final RowMapper<ProfileFieldOption> profileFieldOptionRowMapper = new RowMapper<ProfileFieldOption>(){

		/**
		 * SELECT FIELD_ID, FIELD_VALUE, IDX, DEFAULT_OPTION FROM V2_PROFILE_FIELD_OPT

		 */
		public ProfileFieldOption mapRow(ResultSet rs, int rowNum) throws SQLException {			
			ProfileFieldOptionImpl ut = new ProfileFieldOptionImpl();			 
			ut.setFieldId(rs.getLong(1)); 
			ut.setValue(rs.getString(2)); 
			ut.setIndex(rs.getInt(3)); 
			ut.setDefaultOption(rs.getInt(4)==1); 
			return (ProfileFieldOption)ut;
		}
		
	};
	
	private final RowMapper<ProfileField> getProfileFieldsRowMapper = new RowMapper<ProfileField>(){

		public ProfileField mapRow(ResultSet rs, int rowNum) throws SQLException {	
			ProfileFieldImpl model = new ProfileFieldImpl();		 
			model.setFieldId(rs.getLong(1)); 
			model.setIndex(rs.getInt(2)); 
			return (ProfileField)model;
		}
		
	};
	
	private final RowMapper<ProfileField> getProfileFieldRowMapper = new RowMapper<ProfileField>(){

		public ProfileField mapRow(ResultSet rs, int rowNum) throws SQLException {	
			ProfileFieldImpl field = new ProfileFieldImpl();
            field.setFieldId(rs.getLong(1));
            field.setFieldTypeId(rs.getInt(2));            
            field.setName(rs.getString(3));
            field.setIndex(rs.getInt(4));
            field.setRegistrationIndex(rs.getInt(5));
            field.setDefaultField(rs.getInt(6)==1?true:false);
            field.setEditable(rs.getShort(7) == 1 ? true : false);
            field.setFilterable(rs.getShort(8) == 1 ? true : false);
            field.setListValues(rs.getShort(9) == 1 ? true : false);
            field.setRequired(rs.getShort(10) == 1 ? true : false);
            field.setSearchable(rs.getShort(11) == 1 ? true : false);
            field.setVisible(rs.getShort(12) == 1 ? true : false);
            field.setExternallyManaged(rs.getShort(13) == 1 ? true : false);
            field.setExternalMappingString(rs.getString(14));
            return (ProfileField)field;
		}
	};
	
	
	private String sequencerName = "ProfileField";
	
	public ProfileField createProfileField(ProfileField profilefield) {
		long fieldId = getNextId(sequencerName);
		profilefield.setFieldId(fieldId);
		profilefield.setIndex(getProfileFields().size());
		
		// FIELD_ID, FIELD_TYPE, NAME, IDX, REG_IDX, IS_DEFAULT, IS_EDITABLE, IS_FILTERABLE, IS_LIST, IS_REQUIRED, IS_SEARCHABLE, IS_VISIBLE, EXTERNAL_MANAGED, EXTERNAL_MAPPING

		try {
			getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.CREATE_PROFILE_FIELD").getSql(), 
					new Object[]{ 
				        profilefield.getFieldId(), 
				        profilefield.getFieldTypeId(), 
				        profilefield.getName(), 
				        profilefield.getIndex(), 
				        profilefield.getRegistrationIndex(),
				        profilefield.isDefaultField() ? 1 : 0, 
				        profilefield.isEditable() ? 1 : 0, 
				        profilefield.isFilterable() ? 1 : 0, 
				        profilefield.isListValues() ? 1:0, 
				        profilefield.isRequired() ? 1 : 0, 
				        profilefield.isSearchable() ? 1 : 0, 
				        profilefield.isVisible() ? 1 : 0, 
				        profilefield.isExternallyManaged() ? 1 : 0, 
				        profilefield.getExternalMappingString()
			        });
		} catch (DataAccessException e) {
			String msg = "Could not create profile field";
			log.error(msg, e);
			throw e;
		}		
		return profilefield;
	}

	public ProfileField getProfileField(long fieldId) {
		
		if(fieldId > 0L)
		try {
			return getExtendedJdbcTemplate().queryForObject(getBoundSql("FRAMEWORK_V2.SELECT_PROFILE_FIELD_BY_FIELD_ID").getSql(), new Object[] {fieldId}, new int[] {Types.INTEGER}, getProfileFieldRowMapper );
		} catch (DataAccessException e) {
			String msg = (new StringBuilder()).append("Could not get profile field with ID ").append(fieldId).toString();
            log.error(msg, e);
            throw e;
		}
		return null;
	}

	public void editProfileField(ProfileField profilefield) {
		try {
			getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.UPDATE_PROFILE_FIELD").getSql(), new Object[]{
				profilefield.getFieldTypeId(),
				profilefield.getName(),
				profilefield.getIndex(),
				profilefield.getRegistrationIndex(),
				profilefield.isDefaultField()?1:0,
				profilefield.isEditable()?1:0,
				profilefield.isFilterable()?1:0,
				profilefield.isListValues()?1:0,
				profilefield.isRequired()?1:0,
				profilefield.isSearchable()?1:0,
				profilefield.isVisible()?1:0,
				profilefield.isExternallyManaged()?1:0,
				profilefield.getExternalMappingString(),
				profilefield.getFieldId()
			}); 
		} catch (DataAccessException e) {
            String msg = "Could not update profile field";
            log.error(msg, e);
            throw e;
		}
	}

	public void editProfileFieldOptions(ProfileField profilefield) {
		
		try {
			getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.DELETE_PROFILE_FIELD_OPTIONS").getSql(), new Object[]{profilefield.getFieldId()});
			if(profilefield.getOptions() != null){
				for(ProfileFieldOption option : profilefield.getOptions())
				{
                    if(option.getValue() == null)
                        option.setValue("");
                    
                    if(option.getValue() != null && !"".equals(option.getValue())){
                    	//DEFAULT_OPTION, FIELD_ID, FIELD_VALUE, IDX
                    	getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.CREATE_PROFILE_FIELD_OPTIONS").getSql(), 
                    	    new Object[]{option.isDefaultOption()?1:0, option.getFieldId(), option.getValue(), option.getIndex()}
                    	);  
                    }
				}
			}
		} catch (DataAccessException e) {
            String msg = "Could not update profile field options";
            log.error(msg, e);
            throw e;
		}		
	}

	public List<ProfileFieldOption> getProfileFieldOptions(long fieldId) {
		try {
			return getExtendedJdbcTemplate().query(getBoundSql("FRAMEWORK_V2.SELECT_PROFILE_FIELD_OPTIONS").getSql(),
				new Object[]{ fieldId }, new int[]{ Types.INTEGER }, profileFieldOptionRowMapper);
		} catch (DataAccessException e) {
			 String msg = (new StringBuilder()).append("Could not get profile field options for fieldID: ").append(fieldId).toString();
		     log.error(msg, e);
		     throw e;
		}
	}

	public void deleteProfileField(long fieldId) {
		
		ProfileField field = getProfileField(fieldId);
		try {
			getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.DELETE_PROFILE_FIELD_OPTIONS").getSql(), new Object[]{ fieldId });
			getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.UPDATE_PROFILE_FIELD_INDEX_1").getSql(), new Object[]{ field.getIndex() });
			getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.DELETE_PROFILE_FIELD").getSql(), new Object[]{ fieldId });
		} catch (DataAccessException e) {
            String msg = "Could not delete profile field";
            log.error(msg, e);
            throw e;
		}
	}

	public List<ProfileField> getProfileFields() {
		return getExtendedJdbcTemplate().query(getBoundSql("FRAMEWORK_V2.SELECT_ALL_PROFILE_FIELD").getSql(), getProfileFieldsRowMapper);
	}

	public void setIndex(ProfileField profilefield, int newIndex) {
        if(newIndex < profilefield.getIndex())
        	getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.UPDATE_PROFILE_FIELD_INDEX_2").getSql(), new Object[] {
        			newIndex, profilefield.getIndex()
            });
        else
        	getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.UPDATE_PROFILE_FIELD_INDEX_3").getSql(), new Object[] {
        			newIndex, profilefield.getIndex()
            });
        setRawIndex(profilefield, newIndex);		
	}
	
	public void setRawIndex(ProfileField profilefield, int newIndex){
		getExtendedJdbcTemplate().update(getBoundSql("FRAMEWORK_V2.UPDATE_PROFILE_FIELD_INDEX_4").getSql(), new Object[] {
			newIndex, profilefield.getFieldId()
		});		 
	}
	
}
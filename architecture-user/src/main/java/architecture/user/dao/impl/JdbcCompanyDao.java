/*
 * Copyright 2012, 2013 Donghyuck, Son
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecture.user.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;

import architecture.common.user.Company;
import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.user.DomainMatcher;
import architecture.user.dao.CompanyDao;
import architecture.user.impl.CompanyImpl;

public class JdbcCompanyDao  extends ExtendedJdbcDaoSupport implements CompanyDao {

	
	private final RowMapper<Company> companyMapper = new RowMapper<Company>(){		
		public Company mapRow(ResultSet rs, int rowNum) throws SQLException {			
			Company g = new CompanyImpl();
			g.setCompanyId(rs.getLong("COMPANY_ID"));
			g.setDisplayName(rs.getString("DISPLAY_NAME"));
			g.setName(rs.getString("NAME"));
			g.setDomainName(rs.getString("DOMAIN_NAME"));
			g.setDescription(rs.getString("DESCRIPTION"));
			g.setCreationDate( rs.getDate("CREATION_DATE") ); 
			g.setModifiedDate( rs.getDate("MODIFIED_DATE") ); 		
			return g;
		}		
	};
	
	private ExtendedPropertyDao extendedPropertyDao;	
	private String sequencerName = "COMPANY";
	private String companyPropertyTableName = "V2_COMPANY_PROPERTY";
	private String companyPropertyPrimaryColumnName = "COMPANY_ID";
	
	/**
	 * @param companyPropertyTableName
	 */
	public void setCompanyPropertyTableName(String companyPropertyTableName) {
		this.companyPropertyTableName = companyPropertyTableName;
	}

	/**
	 * @param userPropertyPrimaryColumnName
	 */
	public void setCompanyPropertyPrimaryColumnName(String companyPropertyPrimaryColumnName) {
		this.companyPropertyPrimaryColumnName = companyPropertyPrimaryColumnName;
	}
	
	/**
	 * @param extendedPropertyDao
	 */
	public void setExtendedPropertyDao(ExtendedPropertyDao extendedPropertyDao) {
		this.extendedPropertyDao = extendedPropertyDao;
	}
	
	public void createCompany(Company company) {
		
		long companyId = getNextId(sequencerName);
		if("".equals(company.getDescription()))
			company.setDescription(null);
		
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_SECURITY.CREATE_COMPANY").getSql(), 	
				new SqlParameterValue (Types.NUMERIC, companyId), 
				new SqlParameterValue(Types.VARCHAR, company.getName()),
				new SqlParameterValue(Types.VARCHAR, company.getDisplayName()),
				new SqlParameterValue(Types.VARCHAR, company.getDomainName()),
				new SqlParameterValue(Types.VARCHAR, company.getDescription()),
				new SqlParameterValue(Types.DATE, company.getCreationDate()),
				new SqlParameterValue(Types.DATE, company.getModifiedDate()));		
		company.setCompanyId(companyId);		
		setCompanyProperties(company.getCompanyId(), company.getProperties());		
	}	


	public Company getCompanyByDomainName(String domainName) {
		if( StringUtils.isEmpty(domainName)){
			return null;
		}		
		Company company = null ;
		try {
			company = getExtendedJdbcTemplate().queryForObject(
					getBoundSql("ARCHITECTURE_SECURITY.SELECT_COMPANY_BY_DOMAIN_NAME").getSql(), companyMapper, 
					new SqlParameterValue(Types.VARCHAR,  domainName ) );
			
			company.setProperties(getCompanyProperties( company.getCompanyId() ));
		} catch (IncorrectResultSizeDataAccessException e) {
			if(e.getActualSize() > 1)
	        {
	            log.warn((new StringBuilder()).append("Multiple occurrances of the same company domainName found: ").append(domainName).toString());
	            throw e;
	        }
		} catch (DataAccessException e) {
			 String message = (new StringBuilder()).append("Failure attempting to load company by domainName : ").append(domainName).append(".").toString();
			 log.fatal(message, e);
		}		
		return company;
	}


	public Company getCompanyByName(String name, boolean caseInsensitive) {
		if( StringUtils.isEmpty(name)){
			return null;
		}		
		Company company = null ;
		try {
			company = getExtendedJdbcTemplate().queryForObject(
					getBoundSqlWithAdditionalParameter("ARCHITECTURE_SECURITY.SELECT_COMPANY_BY_NAME", new Boolean( caseInsensitive ) ).getSql(), companyMapper, 
					new SqlParameterValue(Types.VARCHAR, caseInsensitive ? name.toLowerCase() :  name  ) );
			
			company.setProperties(getCompanyProperties( company.getCompanyId() ));
		} catch (IncorrectResultSizeDataAccessException e) {
			if(e.getActualSize() > 1)
	        {
	            log.warn((new StringBuilder()).append("Multiple occurrances of the same company name found: ").append(name).toString());
	            throw e;
	        }
		} catch (DataAccessException e) {
			 String message = (new StringBuilder()).append("Failure attempting to load company by name : ").append(name).append(".").toString();
			 log.fatal(message, e);
		}		
		return company;
	}

	public Company getCompanyById(long companyId) {
		Company company = null ;
		try {
			company = getExtendedJdbcTemplate().queryForObject(getBoundSql("ARCHITECTURE_SECURITY.SELECT_COMPANY_BY_ID").getSql(), companyMapper, new SqlParameterValue(Types.NUMERIC, companyId ) );
			company.setProperties(getCompanyProperties( company.getCompanyId() ));
		} catch (IncorrectResultSizeDataAccessException e) {
			if(e.getActualSize() > 1)
	        {
	            log.warn((new StringBuilder()).append("Multiple occurrances of the same company ID found: ").append(companyId).toString());
	            throw e;
	        }
		} catch (DataAccessException e) {
			 String message = (new StringBuilder()).append("Failure attempting to load company by ID : ").append(companyId).append(".").toString();
			 log.fatal(message, e);
		}		
		return company;
	}

	public void updateCompany(Company company) {
		getExtendedJdbcTemplate().update(getBoundSql("ARCHITECTURE_SECURITY.UPDATE_COMPANY").getSql(), 	
				new SqlParameterValue(Types.VARCHAR, company.getName()),
				new SqlParameterValue(Types.VARCHAR, company.getDisplayName()),
				new SqlParameterValue(Types.VARCHAR, company.getDomainName()),
				new SqlParameterValue(Types.VARCHAR, company.getDescription()),
				new SqlParameterValue(Types.DATE, company.getModifiedDate()),
				new SqlParameterValue(Types.NUMERIC, company.getCompanyId()));
		setCompanyProperties(company.getCompanyId(), company.getProperties());
	}



	public List<Company> getCompanies() {
		throw new UnsupportedOperationException("Group creation not supported.");
	}

	public List<Company> getCompanies(int start, int maxResults) {
		throw new UnsupportedOperationException("Group creation not supported.");
	}


	public List<Long> getCompanyIds(int startIndex, int numResults) {
		return getExtendedJdbcTemplate().queryScrollable(
				getBoundSql("ARCHITECTURE_SECURITY.SELECT_ALL_COMPANY_IDS").getSql(), 
				startIndex, 
				numResults, 
				new Object[0], 
				new int[0], 
				Long.class);
	}	
	
	public Map<String, String> getCompanyProperties(long companyId) {
		return extendedPropertyDao.getProperties(companyPropertyTableName, companyPropertyPrimaryColumnName, companyId);
	}

	public void setCompanyProperties(long companyId, Map<String, String> props) {
		extendedPropertyDao.updateProperties(companyPropertyTableName, companyPropertyPrimaryColumnName, companyId, props);
	}
	public int getCompanyCount() {
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("ARCHITECTURE_SECURITY.COUNT_ALL_COMPANY").getSql());
	}
	public int getCompanyGroupCount( long companyId) {
		return getExtendedJdbcTemplate().queryForInt(getBoundSql("ARCHITECTURE_SECURITY.COUNT_COMPANY_GROUPS").getSql(), new SqlParameterValue(Types.NUMERIC, companyId ));
	}

	public List<Long> getCompanyGroupIds(long companyId) {
		return getExtendedJdbcTemplate().queryForList(getBoundSql("ARCHITECTURE_SECURITY.SELECT_COMPANY_GROUP_IDS").getSql(), Long.class, new SqlParameterValue(Types.NUMERIC, companyId ));
	}
	

	public List<Long> getAllCompanyIds() {
		return getExtendedJdbcTemplate().queryForList(
				getBoundSql("ARCHITECTURE_SECURITY.SELECT_ALL_COMPANY_IDS").getSql(), 
				Long.class);
	}

	public List<Long> getCompanyGroupIds(long companyId, int startIndex, int numResults) {
		return getExtendedJdbcTemplate().queryScrollable(
				getBoundSql("ARCHITECTURE_SECURITY.SELECT_COMPANY_GROUP_IDS").getSql(), 
				startIndex, 
				numResults, 
				new Object[]{companyId}, 
				new int[]{Types.INTEGER}, 
				Long.class);
	}

	public List<DomainMatcher> getCompanyDomainMatchers() {
		return getExtendedJdbcTemplate().query(
			getBoundSql("ARCHITECTURE_SECURITY.SELECT_All_COMPANY_DOMAIN_AND_ID").getSql(),
			new RowMapper<DomainMatcher>(){
				public DomainMatcher mapRow(ResultSet rs, int rowNum)
					throws SQLException {
					DomainMatcher matcher = new DomainMatcher(1, rs.getLong("COMPANY_ID"), rs.getString("DOMAIN_NAME") );
					return matcher;
				}	
		});
	}
}

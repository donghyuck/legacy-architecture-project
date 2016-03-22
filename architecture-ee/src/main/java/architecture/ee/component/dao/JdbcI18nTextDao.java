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
package architecture.ee.component.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Locale;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import architecture.common.i18n.I18nText;
import architecture.common.i18n.dao.I18nTextDao;
import architecture.common.i18n.impl.I18nTextImpl;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;

public class JdbcI18nTextDao extends ExtendedJdbcDaoSupport implements I18nTextDao {

    class I18nTextRowMapper implements RowMapper<I18nText> {
	public I18nText mapRow(ResultSet rs, int rowNum) throws SQLException {
	    I18nTextImpl impl = new I18nTextImpl();
	    impl.setTextId(rs.getLong(1));
	    impl.setName(rs.getString(2));
	    impl.setText(rs.getString(3));
	    impl.setCategoryName(rs.getString(4));
	    impl.setLocaleCode(rs.getString(5));
	    impl.setCreationDate(rs.getDate(6));
	    impl.setModifiedDate(rs.getDate(7));
	    return impl;
	}
    };

    public void createTexts(List<I18nText> list) {
	final List<I18nText> textsToUse = list;
	getExtendedJdbcTemplate().batchUpdate(getBoundSql("ARCHITECTURE_FRAMEWORK.INSERT_I18N_TEXT").getSql(),
		new BatchPreparedStatementSetter() {
		    public void setValues(PreparedStatement ps, int i) throws SQLException {
			for (I18nText text : textsToUse) {
			    long time = System.currentTimeMillis();
			    ps.setLong(1, getNextId("I18nText"));
			    ps.setString(2, text.getName());
			    ps.setString(3, text.getText());
			    ps.setString(4, text.getCategoryName());
			    ps.setString(5, text.getLocaleCode());
			    ps.setDate(6, new java.sql.Date(time));
			    ps.setDate(7, new java.sql.Date(time));
			}
		    }

		    public int getBatchSize() {
			return textsToUse.size();
		    }
		});
    }

    public void updateTexts(List<I18nText> list) {
	final List<I18nText> textsToUse = list;
	getExtendedJdbcTemplate().batchUpdate(getBoundSql("ARCHITECTURE_FRAMEWORK.UPDATE_I18N_TEXT").getSql(),
		new BatchPreparedStatementSetter() {
		    public void setValues(PreparedStatement ps, int i) throws SQLException {
			for (I18nText text : textsToUse) {

			    long time = System.currentTimeMillis();
			    ps.setString(1, text.getText());
			    ps.setDate(2, new java.sql.Date(time));
			    ps.setLong(3, text.getTextId());
			}
		    }

		    public int getBatchSize() {
			return textsToUse.size();
		    }
		});
    }

    public void deleteTexts(List<I18nText> list) {
	final List<I18nText> textsToUse = list;
	getExtendedJdbcTemplate().batchUpdate(getBoundSql("ARCHITECTURE_FRAMEWORK.DELETE_I18N_TEXT").getSql(),
		new BatchPreparedStatementSetter() {
		    public void setValues(PreparedStatement ps, int i) throws SQLException {
			for (I18nText text : textsToUse) {
			    ps.setLong(1, text.getTextId());
			}
		    }

		    public int getBatchSize() {
			return textsToUse.size();
		    }
		});
    }

    public I18nText getText(long textId) {
	return getExtendedJdbcTemplate().queryForObject(
		getBoundSql("ARCHITECTURE_FRAMEWORK.SELECT_I18N_TEXT_BY_ID").getSql(), new Object[] { textId },
		new int[] { Types.INTEGER }, new I18nTextRowMapper());
    }

    public List<I18nText> getTexts() {
	return getExtendedJdbcTemplate().query(getBoundSql("ARCHITECTURE_FRAMEWORK.SELECT_ALL_I18N_TEXT").getSql(),
		new I18nTextRowMapper());
    }

    public List<I18nText> getTexts(Locale locale) {
	return getExtendedJdbcTemplate().query(
		getBoundSql("ARCHITECTURE_FRAMEWORK.SELECT_I18N_TEXT_BY_LOCALE").getSql(),
		new Object[] { locale.toString() }, new int[] { Types.VARCHAR }, new I18nTextRowMapper());
    }

    public List<I18nText> getTexts(String categoryName) {
	return getExtendedJdbcTemplate().query(
		getBoundSql("ARCHITECTURE_FRAMEWORK.SELECT_I18N_TEXT_BY_CATEGORY").getSql(),
		new Object[] { categoryName }, new int[] { Types.VARCHAR }, new I18nTextRowMapper());
    }

    public List<I18nText> getTexts(String categoryName, String localeCode) {
	return getExtendedJdbcTemplate().query(
		getBoundSql("ARCHITECTURE_FRAMEWORK.SELECT_I18N_TEXT_BY_CATEGORY_AND_LOCALE").getSql(),
		new Object[] { categoryName, localeCode }, new int[] { Types.VARCHAR, Types.VARCHAR },
		new I18nTextRowMapper());
    }
}

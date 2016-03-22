/*
 * Copyright 2012 Donghyuck, Son
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

package architecture.ee.i18n.dao.impl;

import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;

public class JdbcI18nTextDao
	extends ExtendedJdbcDaoSupport /* implements I18nTextDao */ {
    /*
     * class I18nTextRowMapper implements RowMapper<I18nText> { public I18nText
     * mapRow(ResultSet rs, int rowNum) throws SQLException { I18nTextImpl impl
     * = new I18nTextImpl(); impl.setTextId(rs.getLong(1));
     * impl.setObjectType(rs.getInt(2)); impl.setObjectId(rs.getLong(3));
     * impl.setObjectAttribute(rs.getInt(4));
     * impl.setLocaleCode(rs.getString(5)); impl.setText(rs.getString(6));
     * impl.setCreationDate(rs.getDate(7)); impl.setModifiedDate(rs.getDate(8));
     * return impl; }};
     * 
     * 
     * public void createTexts(List<I18nText> list) { final List<I18nText>
     * textsToUse = list ; getExtendedJdbcTemplate().batchUpdate(getBoundSql(
     * "ARCHITECTURE_FRAMEWORK.INSERT_I18N_OBJECT_TEXT").getSql(), new
     * BatchPreparedStatementSetter(){ public void setValues(PreparedStatement
     * ps, int i) throws SQLException { for( I18nText text : textsToUse ){ long
     * time = System.currentTimeMillis(); ps.setLong(1, getNextId("I18nText"));
     * ps.setInt(2, text.getObjectType()); ps.setLong(3, text.getObjectId());
     * ps.setInt(4, text.getObjectAttribute()); ps.setString(5,
     * text.getLocaleCode()); ps.setString(6, text.getText()); ps.setDate(7, new
     * java.sql.Date(time)); ps.setDate(8, new java.sql.Date(time)); } } public
     * int getBatchSize() { return textsToUse.size(); }}); }
     * 
     * public void updateTexts(List<I18nText> list) { final List<I18nText>
     * textsToUse = list ; getExtendedJdbcTemplate().batchUpdate(getBoundSql(
     * "ARCHITECTURE_FRAMEWORK.UPDATE_I18N_OBJECT_TEXT").getSql(), new
     * BatchPreparedStatementSetter(){ public void setValues(PreparedStatement
     * ps, int i) throws SQLException { for( I18nText text : textsToUse ){ long
     * time = System.currentTimeMillis(); ps.setString(1, text.getText());
     * ps.setDate(2, new java.sql.Date(time)); ps.setLong(3, text.getTextId());
     * } } public int getBatchSize() { return textsToUse.size(); }}); }
     * 
     * public void deleteTexts(List<I18nText> list) { final List<I18nText>
     * textsToUse = list ; getExtendedJdbcTemplate().batchUpdate(
     * getBoundSql("ARCHITECTURE_FRAMEWORK.DELETE_I18N_OBJECT_TEXT").getSql(),
     * new BatchPreparedStatementSetter(){ public void
     * setValues(PreparedStatement ps, int i) throws SQLException { for(
     * I18nText text : textsToUse ){ ps.setLong(1, text.getTextId()); } } public
     * int getBatchSize() { return textsToUse.size(); }}); }
     * 
     * public I18nText getText(long textId) { return
     * getExtendedJdbcTemplate().queryForObject(
     * getBoundSql("ARCHITECTURE_FRAMEWORK.SELECT_I18N_OBJECT_TEXT_BY_ID").
     * getSql(), new Object[]{textId}, new int[]{Types.INTEGER}, new
     * I18nTextRowMapper()); }
     * 
     * public List<I18nText> getTexts() { return
     * getExtendedJdbcTemplate().query(
     * getBoundSql("ARCHITECTURE_FRAMEWORK.SELECT_ALL_I18N_OBJECT_TEXT").getSql(
     * ), new I18nTextRowMapper()); }
     * 
     * public List<I18nText> getTexts(Locale locale) { return
     * getExtendedJdbcTemplate().query(
     * getBoundSql("ARCHITECTURE_FRAMEWORK.SELECT_I18N_OBJECT_TEXT_BY_LOCALE").
     * getSql(), new Object[]{locale.toString()}, new int[]{ Types.VARCHAR },
     * new I18nTextRowMapper()); }
     * 
     * public List<I18nText> getTexts(int objectType) { return
     * getExtendedJdbcTemplate().query( getBoundSql(
     * "ARCHITECTURE_FRAMEWORK.SELECT_I18N_OBJECT_TEXT_BY_OBJECT_TYPE").getSql()
     * , new Object[]{objectType}, new int[]{Types.INTEGER}, new
     * I18nTextRowMapper()); }
     * 
     * public List<I18nText> getTexts(int objectType, long objectId) { return
     * getExtendedJdbcTemplate().query( getBoundSql(
     * "ARCHITECTURE_FRAMEWORK.SELECT_I18N_OBJECT_TEXT_BY_OBJECT_TYPE_AND_OBJECT_ID"
     * ).getSql(), new Object[]{objectType, objectId}, new int[]{Types.INTEGER,
     * Types.INTEGER}, new I18nTextRowMapper()); }
     * 
     * public List<I18nText> getTexts(int objectType, String locale) { return
     * getExtendedJdbcTemplate().query( getBoundSql(
     * "ARCHITECTURE_FRAMEWORK.SELECT_I18N_OBJECT_TEXT_BY_OBJECT_TYPE_AND_LOCALE"
     * ).getSql(), new Object[]{objectType, locale}, new int[]{Types.INTEGER,
     * Types.VARCHAR}, new I18nTextRowMapper()); }
     */
}
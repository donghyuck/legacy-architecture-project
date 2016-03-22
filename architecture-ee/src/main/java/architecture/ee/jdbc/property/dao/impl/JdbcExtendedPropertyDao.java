package architecture.ee.jdbc.property.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import architecture.common.util.StringUtils;
import architecture.ee.jdbc.property.dao.ExtendedPropertyDao;
import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;

public class JdbcExtendedPropertyDao extends ExtendedJdbcDaoSupport implements ExtendedPropertyDao {

    private String PROPERTY_NAME_COLUMN_NAME = "PROPERTY_NAME";

    private String PROPERTY_VALUE_COLUMN_NAME = "PROPERTY_VALUE";

    public void setPropertyNameColumnName(String columnName) {
	PROPERTY_NAME_COLUMN_NAME = columnName;
    }

    public void setPropertyValueColumnName(String columnName) {
	PROPERTY_VALUE_COLUMN_NAME = columnName;
    }

    public Map<String, String> getProperties(String table, String typeField, long objectID) {

	StringBuilder builder = new StringBuilder("SELECT ").append(PROPERTY_NAME_COLUMN_NAME).append(", ")
		.append(PROPERTY_VALUE_COLUMN_NAME).append(" FROM ").append(table);
	builder.append(" WHERE ");
	builder.append(typeField).append(" =?");

	return getExtendedJdbcTemplate().query(builder.toString(), new Object[] { objectID },
		new ResultSetExtractor<Map<String, String>>() {
		    public Map<String, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
			Map<String, String> rows = new HashMap<String, String>();
			while (rs.next()) {
			    String key = rs.getString(1);
			    String value = rs.getString(2);
			    rows.put(key, value);
			}
			return rows;
		    }
		});
    }

    public void deleteProperties(String table, String typeField, long objectID) {
	StringBuilder builder = new StringBuilder("DELETE FROM ").append(table).append(" WHERE ").append(typeField)
		.append(" = ?");
	getExtendedJdbcTemplate().update(builder.toString(), objectID);
    }

    public void updateProperties(String table, String keyField, long objectId, Map<String, String> properties) {
	if (StringUtils.isEmpty(table))
	    throw new IllegalArgumentException("Table to update properties for cannot be null or empty.");
	if (StringUtils.isEmpty(keyField))
	    throw new IllegalArgumentException("Column specifying key cannot be null or empty.");
	if (properties == null) {
	    deleteProperties(table, keyField, objectId);
	} else {
	    String sql = (new StringBuilder("INSERT INTO ")).append(table).append(" ( ").append(keyField)
		    .append(", " + PROPERTY_NAME_COLUMN_NAME + ", " + PROPERTY_VALUE_COLUMN_NAME + ") VALUES (?, ?, ?)")
		    .toString();

	    final List<Object[]> copy = new ArrayList<Object[]>(properties.size());
	    Set<Map.Entry<String, String>> set = properties.entrySet();
	    SqlParameterSource[] batchArgs = new SqlParameterSource[set.size()];
	    for (Map.Entry<String, String> entry : set) {
		String key = entry.getKey();
		String value = entry.getValue();
		if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)) {
		    copy.add(new Object[] { objectId, key, value });
		}
	    }

	    deleteProperties(table, keyField, objectId);

	    if (!copy.isEmpty())
		getExtendedJdbcTemplate().batchUpdate(sql, copy,
			new int[] { Types.INTEGER, Types.VARCHAR, Types.VARCHAR });
	}

    }

}

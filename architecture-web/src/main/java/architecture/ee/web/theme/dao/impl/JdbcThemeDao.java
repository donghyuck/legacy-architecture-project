package architecture.ee.web.theme.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import architecture.ee.spring.jdbc.support.ExtendedJdbcDaoSupport;
import architecture.ee.web.model.impl.ThemeMapModelImpl;
import architecture.ee.web.theme.ThemeMap;
import architecture.ee.web.theme.ThemeType;
import architecture.ee.web.theme.dao.ThemeDao;

public class JdbcThemeDao extends ExtendedJdbcDaoSupport implements ThemeDao {
	private final RowMapper<ThemeMap> themeMapRowMapper = new RowMapper<ThemeMap>(){

		public ThemeMap mapRow(ResultSet rs, int rowNum) throws SQLException {		
			ThemeType type = ThemeType.fromString(rs.getString(2));
			ThemeMapModelImpl impl = new ThemeMapModelImpl(type);			 
			impl.setThemeName(rs.getString(1));
			impl.setNumValue(rs.getLong(3));
			impl.setStringValue(rs.getString(4));
			impl.setCreationDate(rs.getDate(5));
            impl.setModifiedDate(rs.getDate(6)); 
			return impl;
		}
		
	};
	
	public List<ThemeMap> getAllThemeMaps() {
		return getExtendedJdbcTemplate().query(getBoundSql("FRAMEWORK_WEB_V2.SELECT_ALL_THEME_MAPS").getSql(), themeMapRowMapper);
	}

}

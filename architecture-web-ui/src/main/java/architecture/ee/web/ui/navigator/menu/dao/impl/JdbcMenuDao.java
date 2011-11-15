package architecture.ee.web.ui.navigator.menu.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.jdbc.support.lob.OracleLobHandler;

import architecture.ee.web.ui.navigator.menu.dao.Menu;
import architecture.ee.web.ui.navigator.menu.dao.MenuDao;

public class JdbcMenuDao extends JdbcDaoSupport implements MenuDao {

	private LobHandler lobHandler = new OracleLobHandler ();
		
	private String queryString = "";
			
	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public List<Menu> getMenus() {
		
		final LobHandler lobHandlerToUse = lobHandler;
		
		return getJdbcTemplate().query(getQueryString(), new RowMapper<Menu>(){			
			public Menu mapRow(ResultSet rs, int rowNum) throws SQLException {  			
				Menu model = new Menu();
				model.setId( rs.getLong(1) );
				model.setName(rs.getString(2));
				model.setDescription(rs.getString(3));
				model.setXml(lobHandlerToUse.getClobAsString(rs, 4));
				model.setCreationDate(rs.getDate(5));
				model.setModifiedDate(rs.getDate(6));			
				return (Menu)model;
			}			
			
		} );
	}

	public LobHandler getLobHandler() {
		return lobHandler;
	}

	public void setLobHandler(LobHandler lobHandler) {
		this.lobHandler = lobHandler;
	}

}

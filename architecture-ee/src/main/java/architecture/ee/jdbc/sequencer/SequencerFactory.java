package architecture.ee.jdbc.sequencer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import architecture.common.jdbc.datasource.DataSourceFactory;
import architecture.common.jdbc.sequencer.Sequencer;
import architecture.ee.jdbc.sqlquery.factory.Configuration;
import architecture.ee.jdbc.sqlquery.mapping.MappedStatement;

/**
 * @author   donghyuck
 */
public class SequencerFactory {

	private Log log = LogFactory.getLog(getClass());

	/**
	 * @uml.property  name="configuration"
	 * @uml.associationEnd  
	 */
	private Configuration configuration;

	private DataSource dataSource;

	public SequencerFactory(Configuration configuration) {
		this.configuration = configuration;
	    this.dataSource = DataSourceFactory.getDataSource();
	}

	public SequencerFactory(Configuration configuration, DataSource dataSource) {
		this.configuration = configuration;
		this.dataSource = dataSource;
	}

	/**
	 * @return
	 * @uml.property  name="configuration"
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * @param  configuration
	 * @uml.property  name="configuration"
	 */
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public boolean isSetDataSource() {
		if (dataSource == null)
			return false;
		else
			return true;
	}

	/**
	 * @param  dataSource
	 * @uml.property  name="dataSource"
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public JdbcSequencer createJdbcSequencer(int sequenceID, String sequencerName, int blockSize) {
		JdbcSequencer impl = new JdbcSequencer(sequenceID);
		impl.setConfiguration(configuration);
		impl.setDataSource(dataSource);
		impl.setName(sequencerName);
		impl.afterPropertiesSet();
		return impl;
	}

	public Map<Integer, Sequencer> getAllSequencer() {		
		Map<Integer, Sequencer> sequencers = new HashMap<Integer, Sequencer>();		
		JdbcTemplate template = new JdbcTemplate(dataSource);		
		MappedStatement stmt = configuration.getMappedStatement("ARCHITECTURE_FRAMEWORK.SELECT_ALL_SEQUENCER");		
		List<JdbcSequencer> list = template.query(
				stmt.getBoundSql(null).getSql(), 
				new ResultSetExtractor<List<JdbcSequencer>>() {
					public List<JdbcSequencer> extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<JdbcSequencer> l = new ArrayList<JdbcSequencer>();
					while (rs.next()) {
						int sequencerID = rs.getInt(1);
						String name = rs.getString(2);
						long value = rs.getLong(3);
						JdbcSequencer sequencer = new JdbcSequencer(sequencerID);
						sequencer.setName(name);
						l.add(sequencer);
					}
					return l;
				}
		});

		for (JdbcSequencer sequencer : list) {
			sequencer.setConfiguration(configuration);
			sequencer.setDataSource(dataSource);
			sequencers.put(sequencer.getSequencerId(), sequencer);
		}
		return sequencers;
	}
	
}

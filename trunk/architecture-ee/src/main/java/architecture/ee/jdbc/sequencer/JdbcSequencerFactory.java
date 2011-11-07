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
import org.springframework.jdbc.core.ResultSetExtractor;

import architecture.ee.jdbc.datasource.DataSourceFactory;
import architecture.ee.jdbc.query.factory.Configuration;
import architecture.ee.jdbc.query.mapping.MappedStatement;
import architecture.ee.jdbc.sequencer.impl.JdbcSequencer;
import architecture.ee.spring.jdbc.ExtendedJdbcTemplate;

public class JdbcSequencerFactory {

	private Log log = LogFactory.getLog(getClass());

	private Configuration configuration;

	private DataSource dataSource;

	public JdbcSequencerFactory(Configuration configuration) {
		this.configuration = configuration;
	    this.dataSource = DataSourceFactory.getDataSource();
	}

	public JdbcSequencerFactory(Configuration configuration, DataSource dataSource) {
		this.configuration = configuration;
		this.dataSource = dataSource;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public boolean isSetDataSource() {
		if (dataSource == null)
			return false;
		else
			return true;
	}

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
		ExtendedJdbcTemplate template = new ExtendedJdbcTemplate(dataSource);
		MappedStatement stmt = configuration.getMappedStatement("FRAMEWORK_V2.SELECT_ALL_SEQUENCER");
		List<JdbcSequencer> list = template.query(
				stmt.getBoundSql(null).getSql(), 
				new ResultSetExtractor<List<JdbcSequencer>>() {
					public List<JdbcSequencer> extractData(ResultSet rs)
					throws SQLException, DataAccessException {

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

package architecture.ee.jdbc.recordset;

import java.sql.ResultSet;

import architecture.ee.jdbc.recordset.Recordset;
public class RecordsetFactoryImpl implements RecordsetFactory.Implementation {

	public RecordsetFactoryImpl() {
	}

	public Recordset createRecordset(ResultSet rs) throws Throwable {
		return new Recordset(rs);
	}

	public Recordset createRecordset() {
		return new Recordset();
	}

}

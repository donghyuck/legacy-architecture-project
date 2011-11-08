/*
 * Copyright 2010, 2011 INKIUM, Inc.
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
package architecture.ee.jdbc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface JdbcHelper {

	/**
	 * @author                 donghyuck
	 */
	enum DatabaseType {

		/**
		 * @uml.property  name="oracle"
		 * @uml.associationEnd  
		 */
		oracle,

		/**
		 * @uml.property  name="postgresql"
		 * @uml.associationEnd  
		 */
		postgresql,

		/**
		 * @uml.property  name="mysql"
		 * @uml.associationEnd  
		 */
		mysql,

		/**
		 * @uml.property  name="hsqldb"
		 * @uml.associationEnd  
		 */
		hsqldb,

		/**
		 * @uml.property  name="db2"
		 * @uml.associationEnd  
		 */
		db2,

		/**
		 * @uml.property  name="sqlserver"
		 * @uml.associationEnd  
		 */
		sqlserver,

		/**
		 * @uml.property  name="interbase"
		 * @uml.associationEnd  
		 */
		interbase,

		/**
		 * @uml.property  name="derby"
		 * @uml.associationEnd  
		 */
		derby,

		/**
		 * @uml.property  name="unknown"
		 * @uml.associationEnd  
		 */
		unknown;
	}

	public boolean isBatchUpdatesSupported();

	public String getDatabaseProductName();

	public String getDatabaseProductVersion();

	public DatabaseType getDatabaseType();

	public boolean isDeleteSubqueriesSupported();

	public boolean isFetchSizeSupported();

	public String getJdbcDriverName();

	public String getJdbcDriverVersion();

	public boolean isMaxRowsSupported();

	public boolean isScrollResultsSupported();

	public boolean isStreamBlobRequired();

	public boolean isStreamTextRequired();

	public boolean isSubqueriesSupported();

	public int getTransactionIsolation();

	public boolean isTransactionsSupported();

	public PreparedStatement createScrollablePreparedStatement(Connection con,
			String sql) throws SQLException;

	public void setFetchSize(ResultSet rs, int fetchSize);

	public void scrollResultSet(ResultSet rs, int rowNumber)
			throws SQLException;

}

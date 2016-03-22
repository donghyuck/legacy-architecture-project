
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
package architecture.ee.jdbc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

/**
 * @author donghyuck
 */
public interface JdbcHelper {

    /**
     * @author donghyuck
     */
    enum DatabaseType {
	oracle, postgresql, mysql, hsqldb, db2, sqlserver, interbase, derby, unknown;
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

    public PreparedStatement createScrollablePreparedStatement(Connection con, String sql) throws SQLException;

    public void setFetchSize(ResultSet rs, int fetchSize);

    public void scrollResultSet(ResultSet rs, int rowNumber) throws SQLException;

    public NativeJdbcExtractor getNativeJdbcExtractor();

}

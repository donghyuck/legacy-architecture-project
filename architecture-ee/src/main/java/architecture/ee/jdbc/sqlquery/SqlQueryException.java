/*
 * Copyright 2010, 2011 donghyuck,son.
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
package architecture.ee.jdbc.sqlquery;

import architecture.common.exception.CodeableRuntimeException;
import architecture.common.util.L10NUtils;

public class SqlQueryException extends CodeableRuntimeException {

    public SqlQueryException() {
	super();
    }

    public SqlQueryException(String msg, Throwable cause) {
	super(msg, cause);
    }

    public SqlQueryException(String msg) {
	super(msg);
    }

    public SqlQueryException(Throwable cause) {
	super(cause);
    }

    public static SqlQueryException createSqlQueryException(Throwable cause, int code, Object... args) {
	if (code < 60000) {
	    String codeString = L10NUtils.codeToString(code);
	    String msg = L10NUtils.format(codeString, args);
	    SqlQueryException e = new SqlQueryException(msg, cause);
	    e.setErrorCode(code);
	    return e;
	} else {
	    SqlQueryException e = new SqlQueryException(cause);
	    e.setErrorCode(code);
	    return e;
	}
    }

    public static SqlQueryException createSqlQueryException(Throwable cause, int code) {
	if (code < 60000) {
	    String codeString = L10NUtils.codeToString(code);
	    String msg = L10NUtils.getMessage(codeString);
	    SqlQueryException e = new SqlQueryException(msg, cause);
	    e.setErrorCode(code);
	    return e;
	} else {
	    SqlQueryException e = new SqlQueryException(cause);
	    e.setErrorCode(code);
	    return e;
	}
    }
}

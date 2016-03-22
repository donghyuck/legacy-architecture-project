/*
 * Copyright 2012, 2013 Donghyuck, Son
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
package architecture.ee.test;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import architecture.ee.jdbc.sqlquery.SqlQueryHelper;
import architecture.ee.spring.jdbc.support.SqlQueryDaoSupport;

public class SqlQueryService extends SqlQueryDaoSupport {

    private final TransactionTemplate transactionTemplate;

    public SqlQueryService(PlatformTransactionManager transactionManager) {
	this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public Object someServiceMethod() {
	return transactionTemplate.execute(new TransactionCallback<List<Map<String, Object>>>() {
	    public List<Map<String, Object>> doInTransaction(TransactionStatus status) {
		return resultOfSelectOperation();
	    }
	});
    }

    public List<Map<String, Object>> resultOfSelectOperation() {
	SqlQueryHelper helper = new SqlQueryHelper();
	helper.additionalParameter("TABLE_NAME", "V2_I18N_LOCALE");
	List<Map<String, Object>> list = helper.list(getSqlQuery(), "COMMON.SELECT_TABLE_ROWS");
	return list;
    }
}

package unitofwork

import groovy.io.FileType
import architecture.ee.jdbc.sqlquery.SqlQuery
import architecture.ee.services.support.UnitOfWorkForSqlQuery

class SqlQueryTest extends  UnitOfWorkForSqlQuery {
	
	def getTableNames(String tableName){		
		println( "----------------------------------" )
		return sqlQuery.list("DEFAULT.SELECT_ALL_TABLE_NAMES_BY_NAME", tableName)
	}

}

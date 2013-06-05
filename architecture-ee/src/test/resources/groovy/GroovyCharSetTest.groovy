package groovy
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import groovy.io.FileType
import groovy.util.CharsetToolkit
import architecture.ee.jdbc.sqlquery.SqlQuery;
import architecture.ee.services.SqlQueryCallback;

class GroovyCharSetTest implements SqlQueryCallback {

	def getCharSet() {

		def folder = "C:\\TOOLS\\conf\\g"

		def charSet = [:]

		println("8888888888888888888888888888888888888888888888888888888888888888")

		new File(folder).traverse(type:FileType.FILES) {

			charSet[it.name] = new CharsetToolkit(it).getCharset().toString()

			println(it.absolutePath + "," + new CharsetToolkit(it).getCharset().toString() )
		}

		return charSet
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = [ Exception.class ] )
	def doInSqlQuery(SqlQuery sqlQuery){
		return "hello" + sqlQuery
	}
}

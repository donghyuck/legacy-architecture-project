package groovy
import groovy.io.FileType
import groovy.util.CharsetToolkit
class GroovyCharSetTest {
	
	def getCharSet() {
		
		def folder = "C:\\TOOLS"

		def charSet = [:]

		println("print")
		
		new File(folder).traverse(type:FileType.FILES) {
			   
				charSet[it.name] = new CharsetToolkit(it).getCharset().toString()
				
				println(it.absolutePath + "," + new CharsetToolkit(it).getCharset().toString() )
				
		}

		return charSet
 }
}

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
package architecture.ee.jdbc.sqlquery.parser;

import java.util.Properties;

public class PropertyParser {

    public static String parse(String string, Properties variables) {
	VariableTokenHandler handler = new VariableTokenHandler(variables);
	GenericTokenParser parser = new GenericTokenParser("${", "}", handler);
	return parser.parse(string);
    }

    private static class VariableTokenHandler implements TokenHandler {
	private Properties variables;

	public VariableTokenHandler(Properties variables) {
	    this.variables = variables;
	}

	public String handleToken(String content) {
	    if (variables != null && variables.containsKey(content)) {
		return variables == null ? content : variables.getProperty(content);
	    } else {
		return "${" + content + "}";
	    }
	}
    }
}

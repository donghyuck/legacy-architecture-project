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
package architecture.ee.jdbc.query.builder.xml;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.ee.jdbc.query.builder.AbstractBuilder;
import architecture.ee.jdbc.query.builder.SqlBuilderAssistant;
import architecture.ee.jdbc.query.factory.Configuration;
import architecture.ee.jdbc.query.parser.XNode;
import architecture.ee.jdbc.query.parser.XPathParser;

/**
 * @author  donghyuck
 */
public class XmlSqlBuilder extends AbstractBuilder {

	/**
	 * @uml.property  name="builderAssistant"
	 * @uml.associationEnd  
	 */
	private SqlBuilderAssistant builderAssistant;

	/**
	 * @uml.property  name="parser"
	 * @uml.associationEnd  
	 */
	private XPathParser parser;

	private Log log = LogFactory.getLog(XmlSqlBuilder.class);

	public XmlSqlBuilder(InputStream is, Configuration configuration, String resource) {
		super(configuration);
		this.builderAssistant = new SqlBuilderAssistant(configuration, resource);
		this.parser = new XPathParser(new InputStreamReader(is), false,
				configuration.getVariables(), null);
	}

	public XmlSqlBuilder(InputStream is, Configuration configuration) {
		this(is, configuration, null);
	}

	public XmlSqlBuilder(Reader reader, Configuration configuration,
			String resource) {
		super(configuration);
		this.builderAssistant = new SqlBuilderAssistant(configuration, resource);
		this.parser = new XPathParser(reader, false, configuration.getVariables(), null);
	}

	public XmlSqlBuilder(Reader reader, Configuration configuration) {
		this(reader, configuration, null);
	}

	public void build() {
		try {
			XNode context = parser.evalNode("/sql-queryset");
			String namespace = context.getStringAttribute("namespace");
			if (StringUtils.isEmpty(namespace))
				namespace = context.getStringAttribute("name");
			String description = context.getStringAttribute("description");
			log.debug(namespace + ":" + description);
			builderAssistant.setCurrentNamespace(namespace);
			sqlElement(context.evalNodes("/sql-queryset/sql-query"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error parsing Mapper XML. Cause: " + e, e);
		}
	}

	private void sqlElement(List<XNode> list) throws Exception {
		String currentNamespace = builderAssistant.getCurrentNamespace();
		configuration.addStatementNodes(currentNamespace, list);

		log.debug("" + list.size() + " query defined.");
	}

}

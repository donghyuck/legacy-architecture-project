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
package architecture.ee.jdbc.sqlquery.builder.xml;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.util.L10NUtils;
import architecture.ee.jdbc.sqlquery.builder.AbstractBuilder;
import architecture.ee.jdbc.sqlquery.builder.SqlBuilderAssistant;
import architecture.ee.jdbc.sqlquery.factory.Configuration;
import architecture.ee.jdbc.sqlquery.parser.XNode;
import architecture.ee.jdbc.sqlquery.parser.XPathParser;

/**
 * @author   donghyuck
 */
public class XmlSqlBuilder extends AbstractBuilder {

	private Log log = LogFactory.getLog(XmlSqlBuilder.class);
	
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

	public XmlSqlBuilder(InputStream is, Configuration configuration, String resource) {
		super(configuration);
		this.builderAssistant = new SqlBuilderAssistant(configuration, resource);
		this.parser = new XPathParser(new InputStreamReader(is), false, configuration.getVariables(), null);
	}

	public XmlSqlBuilder(InputStream is, Configuration configuration) {
		this(is, configuration, null);
	}

	public XmlSqlBuilder(Reader reader, Configuration configuration, String resource) {
		super(configuration);
		this.builderAssistant = new SqlBuilderAssistant(configuration, resource);
		this.parser = new XPathParser(reader, false, configuration.getVariables(), null);
	}

	public XmlSqlBuilder(Reader reader, Configuration configuration) {
		this(reader, configuration, null);
	}

	public void build() {
		try {
			// OLD VERSION : V1
			XNode context = parser.evalNode("/sql-queryset");
			
			String namespace ;
			String description ;
			String version ;
			
			boolean isNew = false;
			
			if( context != null ){
				namespace = context.getStringAttribute("namespace");
				description = context.getStringAttribute("description");
				version = "1.0";
			}else{
				// NEW VERSION : V2
				isNew = true;
				context = parser.evalNode("/sqlset");					
				namespace = context.evalString("name");
				description = context.evalString("description");
				version = "2.0";
			}

			log.debug( 
				L10NUtils.format("003221", namespace, description, version )					
			);			
			builderAssistant.setCurrentNamespace(namespace);		
			
			
			if(isNew)
				sqlElement(context.evalNodes("/sqlset/sql-query"));
			else
				sqlElement(context.evalNodes("/sql-queryset/sql-query"));
			
			
		} catch (Exception e) {
			throw new RuntimeException( L10NUtils.format("003222", e.getMessage()), e);
		}
	}

	private void sqlElement(List<XNode> list) throws Exception {		
		String currentNamespace = builderAssistant.getCurrentNamespace();
		configuration.addStatementNodes(currentNamespace, list);		
		log.debug(L10NUtils.format("003223", list.size()));
	}

}
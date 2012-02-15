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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import architecture.ee.jdbc.sqlquery.builder.AbstractBuilder;
import architecture.ee.jdbc.sqlquery.builder.SqlBuilderAssistant;
import architecture.ee.jdbc.sqlquery.builder.xml.dynamic.DynamicSqlNode;
import architecture.ee.jdbc.sqlquery.builder.xml.dynamic.DynamicSqlSource;
import architecture.ee.jdbc.sqlquery.builder.xml.dynamic.MixedSqlNode;
import architecture.ee.jdbc.sqlquery.builder.xml.dynamic.SqlNode;
import architecture.ee.jdbc.sqlquery.builder.xml.dynamic.TextSqlNode;
import architecture.ee.jdbc.sqlquery.factory.Configuration;
import architecture.ee.jdbc.sqlquery.mapping.StatementType;
import architecture.ee.jdbc.sqlquery.parser.XNode;
import architecture.ee.jdbc.sqlquery.sql.SqlSource;

/**
 * @author  DongHyuck, Son
 */
public class XmlStatementBuilder extends AbstractBuilder {

	public static final String XML_NODE_DESCRIPTION_TAG = "description";

	public static final String XML_ATTR_STATEMENT_TYPE_TAG = "statementType";
	public static final String XML_ATTR_FETCH_SIZE_TAG = "fetchSize";
	public static final String XML_ATTR_TIMEOUT_TAG = "timeout";
	public static final String XML_ATTR_DYNAMIC_TAG = "dynamic";
	public static final String XML_ATTR_ID_TAG = "id";
	public static final String XML_ATTR_NAME_TAG = "name";
	public static final String XML_ATTR_CALLABLE_TAG = "callable";
	public static final String XML_ATTR_FUNCTION_TAG = "function";
	public static final String XML_ATTR_SCRIPT_TAG = "script";
	public static final String XML_ATTR_COMMENT_TAG = "comment";

	private Log log = LogFactory.getLog(XmlStatementBuilder.class);

	/**
	 */
	private SqlBuilderAssistant builderAssistant;

	public XmlStatementBuilder(Configuration configuration,
			SqlBuilderAssistant builderAssistant) {
		super(configuration);
		this.builderAssistant = builderAssistant;
	}

	public void parseStatementNode(XNode context) {

		String id = context.getStringAttribute(XML_ATTR_ID_TAG);
		String name = context.getStringAttribute(XML_ATTR_NAME_TAG);
		if (StringUtils.isEmpty(id))
			id = name;

		Integer fetchSize = context.getIntAttribute(XML_ATTR_FETCH_SIZE_TAG,
				null);
		Integer timeout = context.getIntAttribute(XML_ATTR_TIMEOUT_TAG, null);

		// bug!!
		StatementType statementType = StatementType.valueOf(context
				.getStringAttribute(XML_ATTR_STATEMENT_TYPE_TAG,
						StatementType.PREPARED.toString()));

		List<SqlNode> contents = parseDynamicTags(context);
		MixedSqlNode rootSqlNode = new MixedSqlNode(contents);

		SqlSource sqlSource = new DynamicSqlSource(configuration, rootSqlNode);
		String nodeName = context.getNode().getNodeName();
		builderAssistant.addMappedStatement(id, sqlSource, statementType,
				fetchSize, timeout);

	}

	private List<SqlNode> parseDynamicTags(XNode node) {
		List<SqlNode> contents = new ArrayList<SqlNode>();
		NodeList children = node.getNode().getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			XNode child = node.newXNode(children.item(i));
			String nodeName = child.getNode().getNodeName();
			if (child.getNode().getNodeType() == Node.CDATA_SECTION_NODE
					|| child.getNode().getNodeType() == Node.TEXT_NODE) {
				String data = child.getStringBody("");
				contents.add(new TextSqlNode(data));
			} else {
				if ("dynamic".equals(nodeName)) {
					String data = child.getStringBody("");
					contents.add(new DynamicSqlNode(data));
				}
			}
		}
		return contents;
	}
}

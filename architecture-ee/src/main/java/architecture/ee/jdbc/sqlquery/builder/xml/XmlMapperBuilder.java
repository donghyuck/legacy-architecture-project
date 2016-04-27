/*
 * Copyright 2016 donghyuck
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.jdbc.ParameterMapping;
import architecture.common.user.Company;
import architecture.common.util.ClassUtils;
import architecture.common.util.StringUtils;
import architecture.ee.jdbc.sqlquery.builder.AbstractBuilder;
import architecture.ee.jdbc.sqlquery.builder.SqlBuilderAssistant;
import architecture.ee.jdbc.sqlquery.factory.Configuration;
import architecture.ee.jdbc.sqlquery.mapping.MapperSource;
import architecture.ee.jdbc.sqlquery.parser.XNode;

public class XmlMapperBuilder extends AbstractBuilder  {
    
    public static final String XML_ATTR_CLASS_TAG = "class";
    public static final String XML_ATTR_IMPL_CLASS_TAG = "implement-class";
    public static final String XML_ATTR_ID_TAG = "id";
    public static final String XML_ATTR_NAME_TAG = "name";
    private Log log = LogFactory.getLog(XmlMapperBuilder.class);

    /**
     * @uml.property name="builderAssistant"
     * @uml.associationEnd
     */
    private SqlBuilderAssistant builderAssistant;

    /**
     * @uml.property name="context"
     * @uml.associationEnd
     */
    private XNode context;
    
    public XmlMapperBuilder(Configuration configuration, SqlBuilderAssistant builderAssistant, XNode context) {
	super(configuration);
	this.builderAssistant = builderAssistant;
	this.context = context;
    }


    public void parseRowMapperFactoryNode() {	
	log.debug("parse mapper");	
	
	String idToUse = context.getStringAttribute(XML_ATTR_ID_TAG);
	String nameToUse = context.getStringAttribute(XML_ATTR_NAME_TAG);
	if (StringUtils.isEmpty(idToUse))
	    idToUse = nameToUse;
	
	String classNameToUse = context.getStringAttribute(XML_ATTR_CLASS_TAG);
	try {
	    
	    Class<?> mappedClass = ClassUtils.getClass(classNameToUse);	
	    List<ParameterMapping> parameterMappings = parseParameterMappings(context);	 
	    MapperSource factory = new MapperSource.Builder(mappedClass, parameterMappings).name(nameToUse).build();	  
	    builderAssistant.addMapperSource(factory.getName(), factory);	    
	} catch (ClassNotFoundException e) {
	    log.error(e);
	}
    }
    
    private List<ParameterMapping> parseParameterMappings(XNode node) {
	
	List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
	List<XNode> children = node.evalNodes("parameterMapping");
	for (XNode child : children) {
	    ParameterMapping.Builder builder = new ParameterMapping.Builder(child.getStringAttribute(XML_ATTR_NAME_TAG));
	    builder.index(child.getIntAttribute("index", 0));
	    builder.mode(child.getStringAttribute("mode", "NONE"));
	    builder.primary(child.getBooleanAttribute("primary", false));
	    builder.encoding(child.getStringAttribute("encoding", null));
	    builder.pattern(child.getStringAttribute("pattern", null));
	    builder.cipher(child.getStringAttribute("cipher", null));
	    builder.cipherKey(child.getStringAttribute("cipherKey", null));
	    builder.cipherKeyAlg(child.getStringAttribute("cipherKeyAlg", null));
	    builder.digest(child.getStringAttribute("digest", null));
	    builder.size(child.getStringAttribute("size", "0"));
	    String javaTypeName = child.getStringAttribute("javaType", null);
	    String jdbcTypeName = child.getStringAttribute("jdbcType", null);	    
	    if (StringUtils.isNotEmpty(jdbcTypeName))
		builder.jdbcTypeName(jdbcTypeName);
	    if (StringUtils.isNotEmpty(javaTypeName))
		builder.javaType(getTypeAliasRegistry().resolveAlias(javaTypeName));
	    parameterMappings.add(builder.build());
	}
	return parameterMappings;
    }
   
}

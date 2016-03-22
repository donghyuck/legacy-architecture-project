/*
 * Copyright 2012 Donghyuck, Son
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
package architecture.common.license.io;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import architecture.common.exception.LicenseException;
import architecture.common.license.License;
import architecture.common.license.LicenseSigner;
import architecture.common.lifecycle.ApplicationConstants;
import architecture.common.util.L10NUtils;

/**
 * 
 * @author <a href="mailto:donghyuck.son@gmail.com">Donghyuck Son </a>
 *
 */
public class LicenseWriter {

    private final License license;

    private Document document;

    private static final Log log = LogFactory.getLog(LicenseWriter.class);

    public LicenseWriter(License license, LicenseSigner signer) throws LicenseException {
	this.license = license;
	try {
	    document = DocumentHelper.parseText(license.toXML());
	    Element root = document.getRootElement();

	    signer.sign(license);

	    Element sig = root.addElement("signature");
	    sig.setText(license.getSignature());
	} catch (Exception e) {
	    log.fatal(e.getMessage(), e);
	    throw new LicenseException(L10NUtils.format("002101", e.getMessage()), e);
	}
    }

    public License getLicense() {
	return license;
    }

    public Document getDocument() {
	return document;
    }

    public static String encode(License license, int columns, LicenseSigner signer) throws IOException {
	LicenseWriter lw = new LicenseWriter(license, signer);
	StringWriter writer = new StringWriter();
	lw.write(writer, columns);
	return writer.toString();
    }

    public void write(Writer writer) throws IOException {
	write(writer, 80);
    }

    public void write(Writer writer, int columns) throws IOException {
	String xml = document.asXML();
	String base64 = new String(Base64.encodeBase64(xml.getBytes(ApplicationConstants.DEFAULT_CHAR_ENCODING)));
	if (columns > 0)
	    base64 = addLineBreaks(base64, columns);
	StringReader reader = new StringReader(base64);
	char buffer[] = new char[32768];
	int len;
	while ((len = reader.read(buffer)) != -1)
	    writer.write(buffer, 0, len);

	writer.flush();
    }

    private String addLineBreaks(String s, int cols) {
	StringBuilder b = new StringBuilder();
	for (int i = 0; i < s.length(); i += cols) {
	    b.append(s.substring(i, i + cols < s.length() ? i + cols : s.length()));
	    b.append("\n");
	}
	return b.toString();
    }
}

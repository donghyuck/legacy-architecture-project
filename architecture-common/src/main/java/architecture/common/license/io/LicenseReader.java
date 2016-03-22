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
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import architecture.common.exception.LicenseException;
import architecture.common.license.License;
import architecture.common.license.validator.CheckSignatureValidator;
import architecture.common.license.validator.Validator;
import architecture.common.lifecycle.ApplicationConstants;

public class LicenseReader {

    private static final Log log = LogFactory.getLog(LicenseReader.class);

    static final boolean assertionsDisabled = false;

    public LicenseReader() {
    }

    public License read(Reader reader) throws LicenseException, IOException {
	String xml = decodeToXml(reader);
	return License.fromXML(xml);
    }

    public License read(String encryptedLicenseKey) throws LicenseException, IOException {
	StringReader reader = new StringReader(encryptedLicenseKey);
	return read(((Reader) (reader)));
    }

    public String formatLicense(String encryptedLicenseKey, boolean checkSignature)
	    throws LicenseException, IOException {
	StringReader reader = new StringReader(encryptedLicenseKey);
	String xml = decodeToXml(reader);
	if (checkSignature) {
	    License license = License.fromXML(xml);
	    Validator validator = new CheckSignatureValidator();
	    validator.validate(license);
	}
	return prettyPrintLicense(xml);
    }

    public String formatLicense(String encryptedLicenseKey) throws LicenseException, IOException {
	return formatLicense(encryptedLicenseKey, false);
    }

    public String prettyPrintLicense(String decryptedLicenseKey) throws LicenseException {
	try {
	    StringReader reader = new StringReader(decryptedLicenseKey);
	    org.dom4j.Document doc = (new SAXReader()).read(reader);
	    reader.close();
	    OutputFormat outputFormat = OutputFormat.createPrettyPrint();
	    outputFormat.setNewlines(true);
	    outputFormat.setTrimText(false);
	    StringWriter writer = new StringWriter();
	    XMLWriter xmlWriter = new XMLWriter(writer, outputFormat);
	    xmlWriter.write(doc);
	    writer.close();
	    return writer.toString();
	} catch (Exception e) {
	    throw new LicenseException(e);
	}
    }

    private String decodeToXml(Reader in) throws IOException {
	StringBuffer text = new StringBuffer();
	char buf[] = new char[1024];
	int len;
	while ((len = in.read(buf)) >= 0) {
	    int j = 0;
	    while (j < len) {
		char ch = buf[j];
		if (Character.isLetter(ch) || Character.isDigit(ch) || ch == '+' || ch == '/' || ch == '=')
		    text.append(ch);
		j++;
	    }
	}
	in.close();
	String xml = new String(
		Base64.decodeBase64(text.toString().getBytes(ApplicationConstants.DEFAULT_CHAR_ENCODING)));
	if (!assertionsDisabled && !text.toString().matches("^[^\\s]*$")) {
	    throw new AssertionError();
	} else {
	    log.debug(xml);
	    return xml;
	}
    }

}

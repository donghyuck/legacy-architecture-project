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

package architecture.common.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

    private static Pattern basicAddressPattern;

    private static Pattern validUserPattern;

    private static Pattern domainPattern;

    private static Pattern ipDomainPattern;

    private static Pattern tldPattern;

    static {
	String basicAddress = "^([\\w\\.!#$%&*\\+/?\\^`{}\\|~_'=-]+)@([\\w\\.-]+)$";
	String specialChars = "\\(\\)><@,;:\\\\\\\"\\.\\[\\]";
	String validChars = (new StringBuilder()).append("[^ \f\n\r\t").append(specialChars).append("]").toString();
	String atom = (new StringBuilder()).append(validChars).append("+").toString();
	String quotedUser = "(\"[^\"]+\")";

	String word = (new StringBuilder()).append("(").append(atom).append("|").append(quotedUser).append(")")
		.toString();
	String validUser = (new StringBuilder()).append("^").append(word).append("(\\.").append(word).append(")*$")
		.toString();
	String domain = (new StringBuilder()).append("^").append(atom).append("(\\.").append(atom).append(")+$")
		.toString();
	String ipDomain = "^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$";
	String knownTLDs = "^\\.(com|net|org|edu|int|mil|gov|arpa|biz|aero|name|coop|info|pro|museum|mobi)$";
	basicAddressPattern = Pattern.compile(basicAddress, 2);
	validUserPattern = Pattern.compile(validUser, 2);
	domainPattern = Pattern.compile(domain, 2);
	ipDomainPattern = Pattern.compile(ipDomain, 2);
	tldPattern = Pattern.compile(knownTLDs, 2);
    }

    public static boolean isValidEmailAddress(String addr) {
	if (addr == null)
	    return false;
	addr = addr.trim();
	if (addr.length() == 0)
	    return false;
	Matcher matcher = basicAddressPattern.matcher(addr);
	if (!matcher.matches())
	    return false;
	String userPart = matcher.group(1);
	String domainPart = matcher.group(2);
	matcher = validUserPattern.matcher(userPart);
	if (!matcher.matches())
	    return false;
	matcher = ipDomainPattern.matcher(domainPart);
	if (matcher.matches()) {
	    for (int i = 1; i < 5; i++) {
		String num = matcher.group(i);
		if (num == null)
		    return false;
		if (Integer.parseInt(num) > 254)
		    return false;
	    }

	    return true;
	}
	matcher = domainPattern.matcher(domainPart);
	if (matcher.matches()) {
	    String tld = matcher.group(matcher.groupCount());
	    matcher = tldPattern.matcher(tld);
	    return tld.length() == 3 || matcher.matches();
	} else {
	    return "localhost".equals(domainPart);
	}
    }

    /**
     * Verify That the given String is in valid URL format.
     * 
     * @param url
     *            The url string to verify.
     * @return a boolean indicating whether the URL seems to be incorrect.
     */
    public final static boolean verifyUrl(String url) {
	if (url == null) {
	    return false;
	}

	if (url.startsWith("https://")) {
	    // URL doesn't understand the https protocol, hack it
	    url = "http://" + url.substring(8);
	}

	try {
	    new URL(url);

	    return true;
	} catch (MalformedURLException e) {
	    return false;
	}
    }

    public final static boolean stringToBoolean(String str) {
	String booleanString = defaultString(str, "false");
	return Boolean.parseBoolean(booleanString);
    }

}

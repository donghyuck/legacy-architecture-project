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
package architecture.ee.web.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import architecture.common.util.StringUtils;
import architecture.common.util.TextUtils;
/**
 * 
 * 
 * @author  <a href="mailto:donghyuck.son@gmail.com">Donghyuck Son </a>
 *
 */
public class PlainTextToHtmlConverter {


    private static final Pattern INLINE_SPACES_TO_REPLACE = Pattern.compile("\\s{2,}");

    public PlainTextToHtmlConverter()
    {
    }
    public static String toHtml(String plainText)
    {
        if(StringUtils.isEmpty(plainText))
        {
            return "";
        } else
        {
            String html = encodeHtmlEntities(plainText);
            html = TextUtils.leadingSpaces(html);
            html = html.replaceAll("\\n", "<br>\n");
            html = TextUtils.hyperlink(html);
            return matchAndReplaceSpaces(html);
        }
    }

    public static String encodeHtmlEntities(String text)
    {
        if(StringUtils.isEmpty(text))
            return "";
        StringBuilder str = new StringBuilder(expectedEncodedLength(text.length()));
        for(int j = 0; j < text.length(); j++)
        {
            char c = text.charAt(j);
            if(c >= '\200')
            {
                str.append(c);
                continue;
            }
            switch(c)
            {
            case 39: // '\''
                str.append("&#39;");
                break;

            case 34: // '"'
                str.append("&quot;");
                break;

            case 38: // '&'
                str.append("&amp;");
                break;

            case 60: // '<'
                str.append("&lt;");
                break;

            case 62: // '>'
                str.append("&gt;");
                break;

            default:
                str.append(c);
                break;
            }
        }

        return str.toString();
    }

    private static int expectedEncodedLength(int originalLength)
    {
        return originalLength + (originalLength >> 3);
    }

    public static String matchAndReplaceSpaces(String html)
    {
        Matcher matcher = INLINE_SPACES_TO_REPLACE.matcher(html);
        StringBuffer result = new StringBuffer(html.length() + 100);
        while(matcher.find()) 
        {
            matcher.appendReplacement(result, " ");
            int numberOfSpaces = matcher.group().length() - 1;
            int i = 0;
            while(i < numberOfSpaces) 
            {
                result.append("&nbsp;");
                i++;
            }
        }
        matcher.appendTail(result);
        return result.toString();
    }
    
}

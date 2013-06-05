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

import java.io.PrintWriter;
import java.io.StringWriter;

public class HtmlUtils {

    public static String getStackTrace(Throwable t)
    {
        if(t == null)
        {
            return "";
        } else
        {
            StringWriter sw = new StringWriter();
            t.printStackTrace(new PrintWriter(sw));
            return sw.toString();
        }
    }
    
    public static String htmlEncode(String text)
    {
        return PlainTextToHtmlConverter.encodeHtmlEntities(text);
    }
}

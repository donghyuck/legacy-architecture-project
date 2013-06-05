/*
 * Copyright 2012, 2013 Donghyuck, Son
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
package architecture.ee.web.attachment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import architecture.common.event.api.EventPublisher;
import architecture.common.event.api.EventSource;

public abstract class AbstractAttachmentManager implements EventSource {
	
	protected Log log = LogFactory.getLog(getClass());
	
	private EventPublisher eventPublisher;
	
	
	
	public EventPublisher getEventPublisher() {
		return eventPublisher;
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	
	
	protected static String listToString(List<String> list)
    {
        StringBuilder sb = new StringBuilder();
        for( String element :  list ){
        	 sb.append(element).append(",");
        }
        return sb.toString();
    }

	protected static List<String> stringToList(String string)
    {
        List<String> list = new ArrayList<String>();
        if(string != null)
        {
            for(StringTokenizer tokens = new StringTokenizer(string, ","); tokens.hasMoreTokens(); list.add(tokens.nextToken()));
        }
        return list;
    }

}

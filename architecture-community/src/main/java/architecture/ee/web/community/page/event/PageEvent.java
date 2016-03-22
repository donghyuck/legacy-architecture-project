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
package architecture.ee.web.community.page.event;

import java.util.Date;

import architecture.common.lifecycle.event.Event;
import architecture.ee.web.community.page.Page;

public class PageEvent extends Event {

    public enum Type {

	CREATED,

	UPDATED,

	DELETED,

	MOVED,

	EXPIRED,

	VIEWED
    };

    private Type type;
    private Date date;

    public PageEvent(Page source, Type type) {
	super(source);
	this.type = type;
	date = new Date();
    }

    /**
     * @return date
     */
    public Date getDate() {
	return date;
    }

    /**
     * @return type
     */
    public Type getType() {
	return type;
    }

}

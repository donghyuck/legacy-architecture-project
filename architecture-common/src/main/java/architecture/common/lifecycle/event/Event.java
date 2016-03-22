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
package architecture.common.lifecycle.event;

import java.util.Date;
import java.util.UUID;

/**
 * @author donghyuck
 */
public abstract class Event extends java.util.EventObject {

    /**
     * @uml.property name="uuid"
     */
    private UUID uuid;

    /**
     * @uml.property name="actor"
     */
    private Object actor;

    private Date createdDate;

    public Event(Object source) {
	super(source);
	this.uuid = UUID.randomUUID();
	this.createdDate = new Date();
    }

    /**
     * @return
     * @uml.property name="actor"
     */
    public Object getActor() {
	return actor;
    }

    public Date getDate() {
	return createdDate;
    }

    /**
     * @return
     * @uml.property name="uuid"
     */
    public UUID getUuid() {
	return uuid;
    }

    /**
     * @param actor
     * @uml.property name="actor"
     */
    public void setActor(Object actor) {
	this.actor = actor;
    }

    public void setDate(Date createdDate) {
	this.createdDate = createdDate;
    }

    public String toString() {
	return (new StringBuilder()).append("Event{uuid=").append(uuid.toString()).append(", eventType=")
		.append(getClass().getName()).append(", createdDate=").append(createdDate).append(", actor=")
		.append(actor).append('}').toString();
    }

}
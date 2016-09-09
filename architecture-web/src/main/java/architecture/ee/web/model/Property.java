/*
 * Copyright 2016
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
package architecture.ee.web.model;

/**
 * AJAX 를 위한 프로퍼티 객체
 * 
 * @author <a href="mailto:donghyuck.son@gmail.com">Donghyuck Son </a>
 *
 */
public class Property implements java.io.Serializable {

    private static final long serialVersionUID = -2672216687853105936L;

    private String name;

    private Object value;

    /**
     * 
     */
    public Property() {
    }

    public Property(String name, Object value) {
	this.name = name;
	this.value = value;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Object getValue() {
	return value;
    }

    public void setValue(Object value) {
	this.value = value;
    }

}

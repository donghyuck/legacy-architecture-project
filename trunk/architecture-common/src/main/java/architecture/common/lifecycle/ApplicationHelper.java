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
package architecture.common.lifecycle;

import architecture.common.exception.ComponentNotFoundException;

public interface ApplicationHelper {

	/**
	 * Injection 방식으로 인자로 넘겨진 객체의 프로퍼티를 세닝하여 리턴한다.
	 * 이 동작은 함수 이름이 보여주듯이 자동으로 처리된다.
	 * 이를 위하여 클래스는 자바빈즈 스타일을 준수하여야 한다.
	 * 
	 * @param obj
	 */
	public void autowireComponent(Object obj);
	
	/**
	 * 인자로 넘겨진 클래스를 생성하고 자동으로 클래스의 프로퍼티를 Injection 방식으로 세팅한다.
	 * 이를 위하여 클래스는 자바빈즈 스타일을 준수하여야 한다.
	 * @param clazz
	 * @return
	 */
	public Object createComponent(Class clazz);
	
	/**
	 * 인자에 해당하는 객체를 검색하여 리턴한다. 인자는 클래스 또는 객체를 구분가능하게 하는 이름이 될 수 있다.
	 * @param obj
	 * @return
	 * @throws ComponentNotFoundException
	 */
	public Object getComponent(Object obj) throws ComponentNotFoundException;
	
	public Object getInstance(Object obj);
	
	public void refresh();
	
}

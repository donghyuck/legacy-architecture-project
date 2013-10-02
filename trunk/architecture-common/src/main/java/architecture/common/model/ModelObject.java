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
package architecture.common.model;

import java.io.Serializable;
import java.util.Date;

import architecture.common.cache.Cacheable;

/**
 * 모델을 표현하기 위한 기본 인터페이스로 
 * 모델 타입, 프라이머리 키, 생성일 , 수정일 정보를 기본으로 같는다.
 * 
 * @author donghyuck
 *
 */
public interface ModelObject extends Cacheable {
	
	
	/**
	 * @return 프라이머리 키 값을 리턴한다.
	 */
	public Serializable getPrimaryKeyObject();	
	
	
	/**
	 * 
	 * @return 객체 유형을 리턴한다.
	 */
	public ModelObjectType getModelObjectType();
	
	/**
	 * @return 생성일을 리턴한다.
	 */
	public Date getCreationDate() ;
	
	/**
	 * 
	 * @param  creationDate 생성일
	 */
	public void setCreationDate(Date creationDate);
	
	/**
	 * @return 수정일을 리턴한다.
	 */
	public Date getModifiedDate();
	
	/**
	 * 
	 * @param  modifiedDate 수정일
	 */
	public void setModifiedDate(Date modifiedDate);
	
}

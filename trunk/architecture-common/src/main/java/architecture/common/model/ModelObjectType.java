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
package architecture.common.model;

public enum ModelObjectType {
	
	UNKNOWN(0, "UNKNOWN"), 
	COMPANY(1, "COMPANY"), 
	USER(2, "USER"), 
	GROUP(3, "GROUP"), 
	ROLE(4, "ROLE"),
	I18N_TEXT (12, "I18N_TEXT"),
	
	ANONYMOUS (13, "ANONYMOUS"),
	SYSTEM (14, "SYSTEM"),
	
	IMAGE ( 16, "IMAGE"),
	ATTACHMENT ( 17, "ATTACHMENT"),
	MENU ( 18, "MENU"),	
	SOCAIL_ACCOUNT ( 19, "SOCAIL_ACCOUNT"),
	CONTENT ( 20, "CONTENT"),
	TEMPLATE ( 21, "TEMPLATE"),
	ANNOUNCE ( 22, "ANNOUNCE");
	
	
	
public static ModelObjectType ALL_TYPES[] = values();
	
	public static ModelObjectType INDEXABLE_TYPES[] = new ModelObjectType[]{USER, GROUP};
	
	public static ModelObjectType valueOf(int typeId){
		
		for( ModelObjectType t : ALL_TYPES ){
			if(t.getTypeId() == typeId)
				return t;
		}
		
		return UNKNOWN;		
	}
	
	public static ModelObjectType resolve(String typeName){
		if(typeName == null)
			return null;
		for( ModelObjectType t : ALL_TYPES ){
			if( t.toString().toLowerCase().equals(typeName.toLowerCase()) )
				return t;
		}
		return UNKNOWN;
	}
		
    private int typeId;
    
    private String code;
   
	private ModelObjectType(int typeId, String code) {
		this.typeId = typeId;
		this.code = code;
	}
	
    public int getTypeId()
    {
        return typeId;
    }

    public int getKey()
    {
        return typeId;
    }

    public String getCode()
    {
        return code;
    }
}

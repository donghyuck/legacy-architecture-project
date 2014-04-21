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
package architecture.ee.web.community.social.tumblr.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class BooleanYNDeserializer extends JsonDeserializer<Boolean> {

    private static final String YES = "Y";
    private static final String NO = "N";

	@SuppressWarnings("unchecked")
	@Override
    public Boolean deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        try {
        	
        	
            return ctxt.getParser().getBooleanValue();
            
        } catch (JsonParseException jpe) {
            if (NO.equalsIgnoreCase(jp.getText())) {
                return false;
            } else if (YES.equalsIgnoreCase(jp.getText())) {
                return true;
            }
            throw jpe;
        }
    }

}
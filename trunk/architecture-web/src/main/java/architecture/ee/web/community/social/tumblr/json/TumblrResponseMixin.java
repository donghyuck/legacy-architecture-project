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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = TumblrResponseMixin.TumblrResponseDeserializer.class)
public class TumblrResponseMixin {

    static class TumblrResponseDeserializer extends JsonDeserializer<TumblrResponse> {

        @Override
        public TumblrResponse deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode root = jp.readValueAsTree();

            TumblrResponse response = new TumblrResponse();

            JsonNode meta = root.get("meta");

            response.setStatus(meta.get("status").intValue());
            response.setMessage(meta.get("msg").textValue());

            response.setResponseJson(root.get("response").toString());

            return response;
        }
        
    }

}
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
package architecture.ee.web.community.social.facebook;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommentListDeserializer extends JsonDeserializer<List<Comment>> {
	@SuppressWarnings("unchecked")
	@Override
	public List<Comment> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		
/*		ObjectMapper mapper = new ObjectMapper();
		mapper.addMixInAnnotations(Comment.class, CommentMixin.class);
		mapper.addMixInAnnotations(Reference.class, ReferenceMixin.class);*/
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new FacebookModule());
		
		jp.setCodec(mapper);

		if(jp.hasCurrentToken()) {
			TreeNode tNode = jp.readValueAsTree();
			TreeNode dataNode = tNode.get("data");
			if(dataNode != null) {
				
				return (List<Comment>) mapper.readValue(dataNode.traverse(), new TypeReference<List<Comment>>() {});
			}
		}
		return Collections.emptyList();
	}
}

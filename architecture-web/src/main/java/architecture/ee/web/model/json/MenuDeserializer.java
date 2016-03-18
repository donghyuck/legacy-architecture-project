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
package architecture.ee.web.model.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import architecture.ee.web.navigator.DefaultMenu;
import architecture.ee.web.navigator.Menu;

public class MenuDeserializer extends JsonDeserializer<Menu> {

	public MenuDeserializer() {
	}

	@Override
	public Menu deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
		
		ObjectCodec oc = jsonParser.getCodec();
		JsonNode node = oc.readTree(jsonParser);
		
		DefaultMenu menu = new DefaultMenu(node.get("menuId").longValue());
		menu.setName(node.get("name").textValue());
		menu.setTitle(node.get("title").textValue());
		menu.setMenuData(node.get("menuData").textValue());
		menu.setEnabled(node.get("enabled").asBoolean());
		
		return menu;
	}

}

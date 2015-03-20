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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class LocationMixin {
	
	@JsonCreator
	LocationMixin(
			@JsonProperty("latitude") double latitude, 
			@JsonProperty("longitude") double longitude) {}
	
	@JsonProperty("street")
	String street;

	@JsonProperty("city")
	String city;

	@JsonProperty("state")
	String state;

	@JsonProperty("country")
	String country;

	@JsonProperty("zip")
	String zip;

}
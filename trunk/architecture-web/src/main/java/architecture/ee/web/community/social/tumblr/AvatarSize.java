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
package architecture.ee.web.community.social.tumblr;

public enum AvatarSize {

	/**
	 * 16px x 16px
	 */
	PICO(16),

	/**
	 * 24px x 24px
	 */
	NANO(24),

	/**
	 * 30px x 30px
	 */
	MICRO(30),

	/**
	 * 40px x 40px
	 */
	TINY(40),

	/**
	 * 48px x 48px
	 */
	SMALL(48),

	/**
	 * 64px x 64px
	 */
	NORMAL(64),

	/**
	 * 96px x 96px
	 */
	LARGE(96),

	/**
	 * 128px x 128px
	 */
	MEGA(128),

	/**
	 * 512 x 512
	 */
	GIGA(512);

	private int dimension;

	AvatarSize(int dimension) {
		this.dimension = dimension;
	}

	public int getDimension() {
		return this.dimension;
	}
}

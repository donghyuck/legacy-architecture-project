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
package architecture.ee.web.ws;

import java.text.DecimalFormat;

public class Usage {
	
	private String name ;
	
	private long size;
	
	private String readableSize ;
	
	
	/**
	 * @param name
	 * @param size
	 */
	public Usage(String name, long size) {
		super();
		this.name = name;
		this.size = size;
		this.readableSize = readableFileSize(size);
	}

	public Usage(String name, long size, boolean percentage) {
		super();
		this.name = name;
		this.size = size;
		this.readableSize = readableFileSize(size);
	}
	
	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return readableSize
	 */
	public String getReadableSize() {
		return readableSize;
	}

	/**
	 * @return size
	 */
	public long getSize() {
		return size;
	}

		
	public static final String readableFileSize(long fileSize) {
		if (fileSize <= 0)
			return "0";
		final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(fileSize) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(fileSize/ Math.pow(1024, digitGroups)) +  " " + units[digitGroups];
	}
}

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
package architecture.common.lifecycle;

import java.io.File;

public class FileSystemInfo {

	public static class DiskUsage {
		
		String absolutePath;
		long totalSpace;
		long freeSpace;
		long usableSpace;		
		
		public DiskUsage() {
			super();
		}

		private DiskUsage(File root) {
			super();
			this.absolutePath = root.getAbsolutePath();
			this.totalSpace = root.getTotalSpace();
			this.freeSpace = root.getFreeSpace();
			this.usableSpace = root.getUsableSpace();
		}
		
		/**
		 * @return absolutePath
		 */
		public String getAbsolutePath() {
			return absolutePath;
		}

		/**
		 * @param absolutePath 설정할 absolutePath
		 */
		public void setAbsolutePath(String absolutePath) {
			this.absolutePath = absolutePath;
		}

		/**
		 * @return totalSpace
		 */
		public long getTotalSpace() {
			return totalSpace;
		}

		/**
		 * @param totalSpace 설정할 totalSpace
		 */
		public void setTotalSpace(long totalSpace) {
			this.totalSpace = totalSpace;
		}

		/**
		 * @return freeSpace
		 */
		public long getFreeSpace() {
			return freeSpace;
		}

		/**
		 * @param freeSpace 설정할 freeSpace
		 */
		public void setFreeSpace(long freeSpace) {
			this.freeSpace = freeSpace;
		}

		/**
		 * @return usableSpace
		 */
		public long getUsableSpace() {
			return usableSpace;
		}

		/**
		 * @param usableSpace 설정할 usableSpace
		 */
		public void setUsableSpace(long usableSpace) {
			this.usableSpace = usableSpace;
		}
	}
	

	private 

	public FileSystemInfo() {
	}

	/**
	 * @param absolutePath
	 * @param totalSpace
	 * @param freeSpace
	 * @param usableSpace
	 */
	public FileSystemInfo(File root) {
		super();
		this.absolutePath = root.getAbsolutePath();
		this.totalSpace = root.getTotalSpace();
		this.freeSpace = root.getFreeSpace();
		this.usableSpace = root.getUsableSpace();
	}


	
	
}

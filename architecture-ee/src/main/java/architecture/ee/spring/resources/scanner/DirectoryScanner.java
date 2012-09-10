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

package architecture.ee.spring.resources.scanner;

import java.net.URI;
import java.net.URL;

import architecture.common.scanner.DirectoryListener;

public interface DirectoryScanner {

	public abstract void addScanURI(final URI uri);
	
	public abstract void addScanURL(final URL url);
	
	public abstract void addScanDir(final String path);
	
	public abstract void removeScanURL(final URL url);
	
	public abstract void removeScanURI(final URI uri);
	
	public abstract void addDirectoryListener( DirectoryListener fileListener );
	
	public abstract void removeDirectoryListener( DirectoryListener fileListener );
	
	public abstract DirectoryListener[] getDirectoryListeners();

}
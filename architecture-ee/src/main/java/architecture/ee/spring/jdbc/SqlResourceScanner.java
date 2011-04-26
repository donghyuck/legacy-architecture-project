/*
 * Copyright 2010, 2011 INKIUM, Inc.
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
package architecture.ee.spring.jdbc;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.vfs.FileListener;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;

import architecture.common.lifecycle.ComponentImpl;
import architecture.common.vfs.ExtendedFileMonitor;
import architecture.common.vfs.VFSUtils;
import architecture.ee.jdbc.query.builder.xml.XmlSqlBuilder;
import architecture.ee.jdbc.query.factory.Configuration;

public class SqlResourceScanner extends ComponentImpl {

	private ExtendedFileMonitor fileMonitor;

	public SqlResourceScanner(FileListener listener) {
		this.fileMonitor = new ExtendedFileMonitor(listener);
		this.setRecursive(true);
	}

	public void addUri(String uri) {
		try {
			FileObject fo = VFSUtils.resolveFile(uri);
			fileMonitor.addFile(fo);

		} catch (FileSystemException e) {
			e.printStackTrace();
		}
	}

	public void buildSqlFromInputStream(InputStream inputStream,
			Configuration configuration, String resource) {
		XmlSqlBuilder builder = new XmlSqlBuilder(inputStream, configuration,
				resource);
		builder.build();
	}

	public List<FileObject> getMonitoredFileObjectList() {
		return fileMonitor.getMonitoredFileObjectList();
	}

	public void removeUri(String uri) {
		try {
			FileObject fo = VFSUtils.resolveFile(uri);
			fileMonitor.removeFile(fo);
		} catch (FileSystemException e) {
			e.printStackTrace();
		}
	}

	public void setChecksPerRun(int checksPerRun) {
		fileMonitor.setChecksPerRun(checksPerRun);
	}

	public void setDelay(long delay) {
		fileMonitor.setDelay(delay);
	}

	public void setRecursive(boolean newRecursive) {
		fileMonitor.setRecursive(newRecursive);
	}

	@Override
	protected void startInternal() {
		fileMonitor.start();
	}

	@Override
	protected void stopInternal() {
		fileMonitor.stop();
	}
}

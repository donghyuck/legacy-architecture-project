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
package architecture.common.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SharedFileOutputStream extends SharedOutputStream {
    private static final Log log = LogFactory.getLog(SharedFileOutputStream.class);

    private File file;
    private final OutputStream out;
    private int size;

    public SharedFileOutputStream(File file) throws IOException {
	size = 0;
	this.file = file;
	out = new FileOutputStream(file);
    }

    public void write(int b) throws IOException {
	size++;
	out.write(b);
    }

    public void write(byte b[]) throws IOException {
	size += b.length;
	out.write(b);
    }

    public void write(byte b[], int off, int len) throws IOException {
	size += len;
	out.write(b, off, len);
    }

    public void flush() throws IOException {
	out.flush();
    }

    public void close() throws IOException {
	out.close();
	try {
	    if (file.exists() && file.canWrite()) {
		boolean success = file.delete();
		if (!success)
		    log.error((new StringBuilder()).append("Could not Delete File on Close: ").append(file.getName())
			    .toString());
	    }
	} catch (Exception e) {
	}
    }

    public int getSize() {
	return size;
    }

    public InputStream getInputStream() {
	try {
	    return new FileInputStream(file) {

		public void close() throws IOException {
		    super.close();
		    try {
			if (file.exists() && file.canWrite())
			    file.delete();
		    } catch (Exception e) {
			log.error(e);
		    }
		}
	    };
	} catch (FileNotFoundException e) {
	    return null;
	}
    }

    protected void finalize() throws Throwable {
	super.finalize();
	file.delete();
    }

}
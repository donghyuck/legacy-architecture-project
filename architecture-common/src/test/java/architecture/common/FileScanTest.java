/*
 * Copyright 2016 donghyuck
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

package architecture.common;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FileScanTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {

	String FOLDER = "/Users/donghyuck/Documents/workspace/maven.1440568037035/architecture-ee/src/main/resources/sql";
	System.out.println("monitoring started");
	// The monitor will perform polling on the folder every 5 seconds
	final long pollingInterval = 5 * 1000;
	File folder = new File(FOLDER);

	for(File file : FileUtils.listFiles(folder, new String[]{"xml"}, true)){
	    try {
		System.out.println("deploy: " + file.getCanonicalPath());
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	
	if (!folder.exists()) {
	    // Test to see if monitored folder exists
	    throw new RuntimeException("Directory not found: " + FOLDER);
	}
	
	

	FileAlterationObserver observer = new FileAlterationObserver(folder, new SuffixFileFilter(".xml"));
	FileAlterationMonitor monitor = new FileAlterationMonitor(pollingInterval);
	FileAlterationListener listener = new FileAlterationListenerAdaptor() {
	    
	    @Override
	    public void onStart(final FileAlterationObserver observer) {
		System.out.println("The WindowsFileListener has started on " + observer.getDirectory().getAbsolutePath());
	    }
	    
	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public void onDirectoryCreate(final File directory) {
	        System.out.println(directory.getAbsolutePath() + " was created.");
	    }
	 
	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public void onDirectoryChange(final File directory) {
	        System.out.println(directory.getAbsolutePath() + " wa modified");
	    }
	 
	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public void onDirectoryDelete(final File directory) {
	        System.out.println(directory.getAbsolutePath() + " was deleted.");
	    }
	 
	    
	    @Override
	    public void onFileChange(File file) {
		try {
		    System.out.println("File Changed: " + file.getCanonicalPath());
		} catch (IOException e) {
		    e.printStackTrace();
		}

	    }

	    // Is triggered when a file is created in the monitored folder
	    @Override
	    public void onFileCreate(File file) {
		// "file" is the reference to the newly created file
		try {
		    System.out.println("File created: " + file.getCanonicalPath());
		} catch (IOException e) {
		    e.printStackTrace();
		}

	    }

	    // Is triggered when a file is deleted from the monitored folder
	    @Override
	    public void onFileDelete(File file) {
		try {
		    // "file" is the reference to the removed file
		    System.out.println("File removed: " + file.getCanonicalPath());
		    // "file" does not exists anymore in the location
		    System.out.println("File still exists in location: " + file.exists());
		} catch (IOException e) {
		    e.printStackTrace(System.err);
		}
	    }
	};
	
	observer.addListener(listener);
	monitor.addObserver(observer);
	
	try {
	    monitor.start();
	    
	    System.out.println("-----------------------------1");
	} catch (Exception e) {
	    e.printStackTrace();
	}
	
	try {
	    System.out.println("-----------------------------2");
	    Thread.currentThread().sleep(15000L);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

}

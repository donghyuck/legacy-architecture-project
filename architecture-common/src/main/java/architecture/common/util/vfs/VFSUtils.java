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
package architecture.common.util.vfs;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.VFS;

public class VFSUtils {
	
	private static final Log log = LogFactory.getLog(VFSUtils.class);
	
	public static FileObject resolveFile(File file, String filename) {
		try {
			FileObject fo = VFS.getManager().resolveFile(file, filename);		
			return fo; 
		} catch (FileSystemException e) {
		}
		return null;		
	}

	public static FileObject resolveFile(FileObject file, String filename) {
		try {
			FileObject fo = VFS.getManager().resolveFile(file, filename);		
			return fo; 
		} catch (FileSystemException e) {
		}
		return null;		
	}

	
	public static FileObject resolveFile(String uri) {
		try {
			return  VFS.getManager().resolveFile(uri);
		} catch (FileSystemException e) {
			log.warn(e);
		}
		return null;		
	}
	
	/**
	 * uri 값을 분석하여 FileName 값을 리턴한다.
	 * @param uri
	 * @return
	 */
	public static FileName resolveUri(String uri){
		try {
			return VFS.getManager().resolveURI(uri);
		} catch (FileSystemException e) {
		}
		return null;
	}
	
	public static FileObject toFileObject(File file){
		try {
			return VFS.getManager().toFileObject(file);
		} catch (FileSystemException e) {
		}
		return null;
	}
	
	public static boolean isFile(FileObject fo){
		try {
			if( fo.getType() == FileType.FILE )
				return true;
		} catch (FileSystemException e) {
			log.warn(e);
		}
		return false;
	}
	
	public static boolean isFolder(FileObject fo){
		try {
			if( fo.getType() == FileType.FOLDER )
				return true;
		} catch (FileSystemException e) {
			log.warn(e);
		}
		return false;
	}
}

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
package architecture.common.adaptor.connector.ftp;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import architecture.common.adaptor.Connector;

/**
 * FTP 작업을 위한 추상 클래스
 * 
 * @author donghyuck
 *
 */
public class AbstractFtpConnector implements Connector {

    protected Log log = LogFactory.getLog(getClass());

    private FTPClient ftp = new FTPClient();

    private String hostname;

    private int port = 0;

    private String username;

    private String password;

    public String getHostname() {
	return hostname;
    }

    public void setHostname(String hostname) {
	this.hostname = hostname;
    }

    public int getPort() {
	return port;
    }

    public void setPort(int port) {
	this.port = port;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    protected Object pull() {

	try {
	    int reply;
	    if (port > 0)
		ftp.connect(hostname, port);
	    else
		ftp.connect(hostname);

	    log.debug("Connected to " + hostname + " on " + (port > 0 ? port : ftp.getDefaultPort()));
	    // After connection attempt, you should check the reply code to
	    // verify
	    // success.
	    reply = ftp.getReplyCode();

	    if (!FTPReply.isPositiveCompletion(reply)) {
		ftp.disconnect();
		log.error("FTP server refused connection.");
	    }

	    if (!ftp.login(username, password)) {
		ftp.logout();
	    }

	    log.debug("Remote system is " + ftp.getSystemType());

	} catch (Exception e) {
	    if (ftp.isConnected()) {
		try {
		    ftp.disconnect();
		} catch (IOException f) {
		    // do nothing
		}
		log.error("FTP server refused connection.");
	    }
	}
	return null;
    }

}

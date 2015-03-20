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
package architecture.ee.jdbc.util;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import architecture.ee.exception.SystemException;

/**
 * @author   donghyuck
 */
public class ScriptRunner {

	private static final String LINE_SEPARATOR = System.getProperty(
			"line.separator", "\n");

	private static final String DEFAULT_DELIMITER = ";";

	private Connection connection;

	private boolean stopOnError;
	private boolean autoCommit;
	private boolean sendFullScript;

	private PrintWriter logWriter = new PrintWriter(System.out);
	private PrintWriter errorLogWriter = new PrintWriter(System.err);

	private String delimiter = DEFAULT_DELIMITER;
	private boolean fullLineDelimiter = false;
	private String characterSetName;

	public ScriptRunner(Connection connection) {
		this.connection = connection;
	}

	/**
	 * @param  characterSetName
	 * @uml.property  name="characterSetName"
	 */
	public void setCharacterSetName(String characterSetName) {
		this.characterSetName = characterSetName;
	}

	/**
	 * @param  stopOnError
	 * @uml.property  name="stopOnError"
	 */
	public void setStopOnError(boolean stopOnError) {
		this.stopOnError = stopOnError;
	}

	/**
	 * @param  autoCommit
	 * @uml.property  name="autoCommit"
	 */
	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	/**
	 * @param  sendFullScript
	 * @uml.property  name="sendFullScript"
	 */
	public void setSendFullScript(boolean sendFullScript) {
		this.sendFullScript = sendFullScript;
	}

	/**
	 * @param  logWriter
	 * @uml.property  name="logWriter"
	 */
	public void setLogWriter(PrintWriter logWriter) {
		this.logWriter = logWriter;
	}

	/**
	 * @param  errorLogWriter
	 * @uml.property  name="errorLogWriter"
	 */
	public void setErrorLogWriter(PrintWriter errorLogWriter) {
		this.errorLogWriter = errorLogWriter;
	}

	/**
	 * @param  delimiter
	 * @uml.property  name="delimiter"
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	/**
	 * @param  fullLineDelimiter
	 * @uml.property  name="fullLineDelimiter"
	 */
	public void setFullLineDelimiter(boolean fullLineDelimiter) {
		this.fullLineDelimiter = fullLineDelimiter;
	}

	public void runScript(Reader reader) {
		setAutoCommit();

		try {
			if (sendFullScript) {
				executeFullScript(reader);
			} else {
				executeLineByLine(reader);
			}
		} finally {
			rollbackConnection();
		}
	}

	private void executeFullScript(Reader reader) {
		StringBuffer script = new StringBuffer();
		try {
			BufferedReader lineReader = new BufferedReader(reader);
			String line;
			while ((line = lineReader.readLine()) != null) {
				script.append(line);
				script.append(LINE_SEPARATOR);
			}
			executeStatement(script.toString());
			commitConnection();
		} catch (Exception e) {
			String message = "Error executing: " + script + ".  Cause: " + e;
			printlnError(message);
			throw new SystemException(message, e);
		}
	}

	private void executeLineByLine(Reader reader) {
		StringBuffer command = new StringBuffer();
		try {
			BufferedReader lineReader = new BufferedReader(reader);
			String line;
			while ((line = lineReader.readLine()) != null) {
				command = handleLine(command, line);
			}
			commitConnection();
			checkForMissingLineTerminator(command);
		} catch (Exception e) {
			String message = "Error executing: " + command + ".  Cause: " + e;
			printlnError(message);
			throw new SystemException(message, e);
		}
	}

	public void closeConnection() {
		try {
			connection.close();
		} catch (Exception e) {
			// ignore
		}
	}

	private void setAutoCommit() {
		try {
			if (autoCommit != connection.getAutoCommit()) {
				connection.setAutoCommit(autoCommit);
			}
		} catch (Throwable t) {
			throw new SystemException("Could not set AutoCommit to "
					+ autoCommit + ". Cause: " + t, t);
		}
	}

	private void commitConnection() {
		try {
			if (!connection.getAutoCommit()) {
				connection.commit();
			}
		} catch (Throwable t) {
			throw new SystemException( "Could not commit transaction. Cause: " + t, t);
		}
	}

	private void rollbackConnection() {
		try {
			if (!connection.getAutoCommit()) {
				connection.rollback();
			}
		} catch (Throwable t) {
			// ignore
		}
	}

	private void checkForMissingLineTerminator(StringBuffer command) {
		if (command != null && command.toString().trim().length() > 0) {
			throw new SystemException( "Line missing end-of-line terminator (" + delimiter + ") => " + command);
		}
	}

	private StringBuffer handleLine(StringBuffer command, String line)
			throws SQLException, UnsupportedEncodingException {
		String trimmedLine = line.trim();
		if (lineIsComment(trimmedLine)) {
			println(trimmedLine);
		} else if (commandReadyToExecute(trimmedLine)) {
			command.append(line.substring(0, line.lastIndexOf(delimiter)));
			command.append(LINE_SEPARATOR);
			println(command);
			executeStatement(command.toString());
			command.setLength(0);
		} else if (trimmedLine.length() > 0) {
			command.append(line);
			command.append(LINE_SEPARATOR);
		}
		return command;
	}

	private boolean lineIsComment(String trimmedLine) {
		return trimmedLine.startsWith("//") || trimmedLine.startsWith("--");
	}

	private boolean commandReadyToExecute(String trimmedLine) {
		return !fullLineDelimiter && trimmedLine.endsWith(delimiter)
				|| fullLineDelimiter && trimmedLine.equals(delimiter);
	}

	private void executeStatement(String command) throws SQLException,
			UnsupportedEncodingException {
		if (characterSetName != null) {
			command = new String(command.getBytes(), characterSetName);
		}
		boolean hasResults = false;
		Statement statement = connection.createStatement();
		if (stopOnError) {
			hasResults = statement.execute(command);
		} else {
			try {
				hasResults = statement.execute(command);
			} catch (SQLException e) {
				String message = "Error executing: " + command + ".  Cause: "
						+ e;
				printlnError(message);
			}
		}
		printResults(statement, hasResults);
		try {
			statement.close();
		} catch (Exception e) {
			// Ignore to workaround a bug in some connection pools
		}
	}

	private void printResults(Statement statement, boolean hasResults) {
		try {
			if (hasResults) {
				ResultSet rs = statement.getResultSet();
				if (rs != null) {
					ResultSetMetaData md = rs.getMetaData();
					int cols = md.getColumnCount();
					for (int i = 0; i < cols; i++) {
						String name = md.getColumnLabel(i + 1);
						print(name + "\t");
					}
					println("");
					while (rs.next()) {
						for (int i = 0; i < cols; i++) {
							String value = rs.getString(i + 1);
							print(value + "\t");
						}
						println("");
					}
				}
			}
		} catch (SQLException e) {
			printlnError("Error printing results: " + e.getMessage());
		}
	}

	private void print(Object o) {
		if (logWriter != null) {
			logWriter.print(o);
			logWriter.flush();
		}
	}

	private void println(Object o) {
		if (logWriter != null) {
			logWriter.println(o);
			logWriter.flush();
		}
	}

	private void printlnError(Object o) {
		if (errorLogWriter != null) {
			errorLogWriter.println(o);
			errorLogWriter.flush();
		}
	}
}
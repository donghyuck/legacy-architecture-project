package architecture.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import architecture.common.exception.RuntimeError;

public class PlatformHelper {

    public enum Platform {

	WINDOWS(';'), UNIX(':');

	public final char pathSeparator;

	private Platform(char pathSeparator) {
	    this.pathSeparator = pathSeparator;
	}

	public static Platform current() {
	    if (File.pathSeparatorChar == ':')
		return UNIX;
	    return WINDOWS;
	}

    }

    public static Platform currentPlatform() {
	return Platform.current();
    }

    public static void main(String[] args) {

	showOS();
    }

    public static String getName() {
	return System.getProperty("os.name").toUpperCase(Locale.ENGLISH);
    }

    public static VersionNumber getVersionNumber() {
	return new VersionNumber(System.getProperty("os.version"));
    }

    public static boolean isDarwin() {

	// according to http://developer.apple.com/technotes/tn2002/tn2110.html
	return System.getProperty("os.name").toLowerCase(Locale.ENGLISH).startsWith("mac");
    }

    public static boolean isSnowLeopardOrLater() {
	try {
	    return isDarwin()
		    && new VersionNumber(System.getProperty("os.version")).compareTo(new VersionNumber("10.6")) >= 0;
	} catch (IllegalArgumentException e) {
	    // failed to parse the version
	    return false;
	}
    }

    public static void showOS() {

	Runtime runtime = Runtime.getRuntime();
	try {

	    Process proc = runtime.exec("systeminfo /FO CSV");

	    // System.out.println(System.getProperty("file.encoding"));

	    InputStream stdOut = proc.getInputStream();
	    InputStream stdErr = proc.getErrorStream();
	    BufferedReader stdReader = new BufferedReader(new InputStreamReader(stdOut, "MS949"));
	    BufferedReader errReader = new BufferedReader(new InputStreamReader(stdErr, "UTF-8"));
	    StringBuffer stdout = new StringBuffer();
	    StringBuffer errout = new StringBuffer();
	    String line = null;

	    while ((line = stdReader.readLine()) != null) {
		stdout.append(line).append(PlatformConstants.EOL);
	    }
	    line = null;
	    while ((line = errReader.readLine()) != null) {
		errout.append(line).append(PlatformConstants.EOL);
	    }

	    stdOut.close();
	    stdErr.close();

	    System.out.println(stdout.toString());
	    System.out.println(errout.toString());
	} catch (IOException e) {
	}
    }

    public static float getJvmVersion() {
	String props = System.getProperty("java.specification.version");
	try {
	    return Float.valueOf(props).floatValue();
	} catch (Exception e) {
	    throw new RuntimeError(e);
	}
    }

    public static boolean isJvmVersion(float versionNumber) {
	return getJvmVersion() >= versionNumber;
    }

}

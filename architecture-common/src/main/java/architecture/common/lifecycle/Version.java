package architecture.common.lifecycle;

/**
 * Holds version information for Openfire.
 * @author    Iain Shigeoka
 */
public class Version {

	/**
	 * The major number (ie 1.x.x).
	 * @uml.property  name="major"
	 */
	private int major;

	/**
	 * The minor version number (ie x.1.x).
	 * @uml.property  name="minor"
	 */
	private int minor;

	/**
	 * The micro version number (ie x.x.1).
	 * @uml.property  name="micro"
	 */
	private int micro;

	/**
	 * A status release number or -1 to indicate none.
	 * @uml.property  name="statusVersion"
	 */
	private int statusVersion;

	/**
	 * The release state of the product (Release, Release Candidate).
	 * @uml.property  name="status"
	 * @uml.associationEnd  
	 */
	private ReleaseStatus status;

	/**
	 * Cached version string information
	 * @uml.property  name="versionString"
	 */
	private String versionString;

	/**
	 * Create a new version information object.
	 * 
	 * @param major
	 *            the major release number.
	 * @param minor
	 *            the minor release number.
	 * @param micro
	 *            the micro release number.
	 * @param status
	 *            the status of the release.
	 */
	public Version(int major, int minor, int micro, ReleaseStatus status, int statusVersion) {
		this.major = major;
		this.minor = minor;
		this.micro = micro;
		this.status = status;
		this.statusVersion = statusVersion;
		if (status != null) {
			if (status == ReleaseStatus.Release) {
				versionString = major + "." + minor + "." + micro;
			} else {
				if (statusVersion >= 0) {
					versionString = major + "." + minor + "." + micro + " " + status.toString() + " " + statusVersion;
				} else {
					versionString = major + "." + minor + "." + micro + " " + status.toString();
				}
			}
		} else {
			versionString = major + "." + minor + "." + micro;
		}
	}

	/**
	 * Returns the version number of this instance of Openfire as a String (ie major.minor.revision).
	 * @return    The version as a string
	 * @uml.property  name="versionString"
	 */
	public String getVersionString() {
		return versionString;
	}

	/**
	 * Returns the release status of this product.
	 * @return    the release status of this product.
	 * @uml.property  name="status"
	 */
	public ReleaseStatus getStatus() {
		return status;
	}

	/**
	 * Obtain the major release number for this product.
	 * @return    The major release number 1.x.x
	 * @uml.property  name="major"
	 */
	public int getMajor() {
		return major;
	}

	/**
	 * Obtain the minor release number for this product.
	 * @return    The minor release number x.1.x
	 * @uml.property  name="minor"
	 */
	public int getMinor() {
		return minor;
	}

	/**
	 * Obtain the micro release number for this product.
	 * @return    The micro release number x.x.1
	 * @uml.property  name="micro"
	 */
	public int getMicro() {
		return micro;
	}

	/**
	 * Obtain the status relase number for this product. For example, if the release status is <strong>alpha</strong> the release may be <strong>5</strong> resulting in a release status of <strong>Alpha 5</strong>.
	 * @return    The status version or -1 if none is set.
	 * @uml.property  name="statusVersion"
	 */
	public int getStatusVersion() {
		return statusVersion;
	}

	/**
	 * A class to represent the release status of the server. Product releases are indicated by type safe enum constants.
	 */
	public enum ReleaseStatus {
		/**
		 * @uml.property  name="release"
		 * @uml.associationEnd  
		 */
		Release(""), /**
		 * @uml.property  name="release_Candidate"
		 * @uml.associationEnd  
		 */
		Release_Candidate("RC"), /**
		 * @uml.property  name="beta"
		 * @uml.associationEnd  
		 */
		Beta("Beta"), /**
		 * @uml.property  name="alpha"
		 * @uml.associationEnd  
		 */
		Alpha("Alpha");

		private String status;

		private ReleaseStatus(String status) {
			this.status = status;
		}

		@Override
		public String toString() {
			return status;
		}
	}
}

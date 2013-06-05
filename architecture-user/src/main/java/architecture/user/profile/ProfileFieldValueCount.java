package architecture.user.profile;

import java.util.Comparator;

/**
 * @author  donghyuck
 */
public class ProfileFieldValueCount {
	
	public static class CountComparator implements Comparator<ProfileFieldValueCount> {

		public CountComparator() {
		
		}
		
		public int compare(ProfileFieldValueCount o1, ProfileFieldValueCount o2) {
			return o1.getCount() - o2.getCount();
		}

	}

	public static class NameComparator implements Comparator<ProfileFieldValueCount> {

		public NameComparator() {
		}
		
		public int compare(ProfileFieldValueCount o1, ProfileFieldValueCount o2) {
			String val1 = o1.getValue() != null ? o1.getValue().getValue() != null ? o1
					.getValue().getValue() : ""
					: "";
			String val2 = o2.getValue() != null ? o2.getValue().getValue() != null ? o2
					.getValue().getValue() : ""
					: "";
			return val1.compareTo(val2);
		}

	}

	/**
	 */
	private ProfileFieldValue value;
	/**
	 */
	private int count;

	public ProfileFieldValueCount(ProfileFieldValue value, int count) {
		this.value = value;
		this.count = count;
	}

	/**
	 * @return
	 */
	public ProfileFieldValue getValue() {
		return value;
	}

	/**
	 * @return
	 */
	public int getCount() {
		return count;
	}

}

package architecture.common;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

public class DateTest {

	@Test
	public void test() throws Exception{
		
		Date start = DateUtils.parseDateStrictly("2015-12-14", "yyyy-MM-dd");
		Date end = DateUtils.addDays(start, 45);
		System.out.println(end);
	}

	
	
	public class Sundays {

		public int getSundays(int month, int year) {
			int count = 0;
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month, 1);
			int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

			for (int i = 1; i <= days; i++) {

				calendar.set(year, month, i);
				int day = calendar.get(Calendar.DAY_OF_WEEK);
				if (day == 1)
					count++;

			}
			return count;
		}
	}
	
	
}

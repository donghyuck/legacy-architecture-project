package architecture.common;

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

}

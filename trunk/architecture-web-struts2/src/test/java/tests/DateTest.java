package tests;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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

public class DateTest {

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		String source = "2013-11-29T00:00:00.000Z";
		TimeZone tz = TimeZone.getTimeZone("UTC");
		SimpleDateFormat  dateTransformer = new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss.SSS");
		dateTransformer.setTimeZone(tz);
		Date dt2 = dateTransformer.parse(source);
		System.out.println( dt2 );
/*		DateTime dt = new DateTime();
		DateTimeFormatter fmt = ISODateTimeFormat.dateTimeNoMillis();
		String str = fmt.print(dt);
		System.out.println( str );
		String source = "2013-11-29T00:00:00.000Z";
		DateTime dt2 = fmt.parseDateTime(source);
		
		System.out.println( dt2.toDate() );*/
		
		// TODO 자동 생성된 메소드 스텁
		/*String DATE_FORMAT = "yyyy-MM-dd'T'HH:mmZ";
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat(DATE_FORMAT);
		df.setTimeZone(tz);
		
		
		
		//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ");
		//System.out.println(df.format(new Date()));
		
		System.out.println( DateFormatUtils.ISO_DATE_TIME_ZONE_FORMAT.format(new Date()) );
		
		String source = "2013-11-29T00:00:00.000Z";
		
		//System.out.println( DateFormatUtils.ISO_DATE_TIME_ZONE_FORMAT.parseObject(source) );
		System.out.println( df.parse(source));
		//System.out.println( formatter.parse(source));
*/	}

}

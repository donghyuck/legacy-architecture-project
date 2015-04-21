import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
		// TODO 자동 생성된 메소드 스텁
		String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:sss";
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		System.out.println(formatter.format(new Date()));
		
		String source = "2013-11-26T14:00:00.000Z";
		System.out.println( formatter.parse(source));
		
	}

}

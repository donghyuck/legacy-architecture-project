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
package architecture.ee;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class CrawlerTest {

	@Test
	public void visit() {

		try {
			URL url = new URL("http://www.clien.net/cs2/bbs/board.php?bo_table=jirum");			
			Document doc = Jsoup.parse(url, 1000);
			System.out.println(doc.title());
			Elements list = doc.select(".board_main table>tbody tr");
			for( Element ele : list ){
				Elements ele2 = ele.children();
				if( ele2.size() > 1 && !ele.hasClass("post_notice") ){
					for( Element ele3 : ele2){
						System.out.println( "#" + ele3.html() );
					}
				}
			}
		} catch (MalformedURLException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO 자동 생성된 catch 블록
			e1.printStackTrace();
		}

	}

}

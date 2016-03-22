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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

public class HtmlTest {

    public HtmlTest() {
	// TODO 자동 생성된 생성자 스텁
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
	// TODO 자동 생성된 메소드 스텁

    }

    @Test
    public void testExtrtactImg() {
	String imgRegex = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
	String html = "자신이 업로드한 이미지들에 대한 갤러리 기능을 제공합니다.&nbsp;</p><p>UI 는 구글과 비슷하게 구현하였습니다.</p><p><img class='img-responsive' src='/download/image/1102/스크린샷 2014-07-28 오후 11.34.09.png'></p></div>";
	Document doc = Jsoup.parse(html);

	Element links = doc.select("img").first();
	System.out.println(links.attr("src"));

    }

    @Test
    public void testExtrtactImg2() {
	String imgRegex = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
	String html = "자신이 업로드한 이미지들에 대한 갤러리 기능을 제공합니다.&nbsp;</p><p>UI 는 구글과 비슷하게 구현하였습니다.</p><p></p></div>";
	Document doc = Jsoup.parse(html);
	Element links = doc.select("img").first();
	if (links != null)
	    System.out.println(links.attr("src"));

    }

}

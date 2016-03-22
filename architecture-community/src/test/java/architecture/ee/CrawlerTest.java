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

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class CrawlerTest {

    public void crawlling() throws Exception {
	CrawlConfig config = new CrawlConfig();
	config.setCrawlStorageFolder("/Users/donghyuck/Documents/temp");
	config.setPolitenessDelay(1000);

	/*
	 * You can set the maximum crawl depth here. The default value is -1 for
	 * unlimited depth
	 */
	config.setMaxDepthOfCrawling(2);

	/*
	 * You can set the maximum number of pages to crawl. The default value
	 * is -1 for unlimited number of pages
	 */
	config.setMaxPagesToFetch(1000);

	/**
	 * Do you want crawler4j to crawl also binary data ? example: the
	 * contents of pdf, or the metadata of images etc
	 */
	config.setIncludeBinaryContentInCrawling(false);

	/*
	 * Do you need to set a proxy? If so, you can use:
	 * config.setProxyHost("proxyserver.example.com");
	 * config.setProxyPort(8080);
	 *
	 * If your proxy also needs authentication:
	 * config.setProxyUsername(username); config.getProxyPassword(password);
	 */

	/*
	 * This config parameter can be used to set your crawl to be resumable
	 * (meaning that you can resume the crawl from a previously
	 * interrupted/crashed crawl). Note: if you enable resuming feature and
	 * want to start a fresh crawl, you need to delete the contents of
	 * rootFolder manually.
	 */
	config.setResumableCrawling(false);

	PageFetcher pageFetcher = new PageFetcher(config);

	RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
	RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

	CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
	controller.addSeed("http://www.naver.com/");
	controller.start(MyCrawler.class, 1);

    }

    @Test
    public void visit() {

	try {
	    URL url = new URL("http://www.clien.net/cs2/bbs/board.php?bo_table=jirum");
	    Document doc = Jsoup.parse(url, 1000);
	    System.out.println(doc.title());
	    Elements list = doc.select(".board_main table>tbody tr");
	    for (Element ele : list) {
		Elements ele2 = ele.children();
		if (ele2.size() > 1 && !ele.hasClass("post_notice")) {
		    for (Element ele3 : ele2) {
			System.out.println("#" + ele3.html());
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

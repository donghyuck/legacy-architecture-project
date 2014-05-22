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
package architecture.ee.web.community.struts2.action.ajax;

import java.util.List;

import architecture.common.user.Company;
import architecture.ee.web.community.timeline.Timeline;
import architecture.ee.web.community.timeline.TimelineManager;
import architecture.ee.web.struts2.action.support.FrameworkActionSupport;

public class CompanyTimelineAction extends FrameworkActionSupport {
		
	private TimelineManager timelineManager ;

	public CompanyTimelineAction() {
		
	}

	/**
	 * @return timelineManager
	 */
	public TimelineManager getTimelineManager() {
		return timelineManager;
	}

	/**
	 * @param timelineManager 설정할 timelineManager
	 */
	public void setTimelineManager(TimelineManager timelineManager) {
		this.timelineManager = timelineManager;
	}

	public List<Timeline> getTimelines(){
		Company companyToUse = getWebSite().getCompany();
		return timelineManager.getTimelines(companyToUse);
	}
	
	public String execute() throws Exception {
		return success();
	}
	
}

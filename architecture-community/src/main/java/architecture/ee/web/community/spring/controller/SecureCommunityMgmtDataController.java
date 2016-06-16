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
package architecture.ee.web.community.spring.controller;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import architecture.common.user.CompanyManager;
import architecture.ee.web.attachment.AttachmentManager;
import architecture.ee.web.attachment.ImageManager;
import architecture.ee.web.community.announce.AnnounceManager;
import architecture.ee.web.community.page.PageManager;
import architecture.ee.web.community.streams.PhotoStreamsManager;
import architecture.ee.web.logo.LogoManager;
import architecture.ee.web.navigator.MenuRepository;
import architecture.ee.web.site.WebSiteManager;

@Controller("secure-community-mgmt-data-controller")
@RequestMapping("/secure/data")
public class SecureCommunityMgmtDataController {

    private static final Log log = LogFactory.getLog(SecureCommunityMgmtDataController.class);

    @Inject
    @Qualifier("photoStreamsManager")
    private PhotoStreamsManager photoStreamsManager;

    @Inject
    @Qualifier("imageManager")
    private ImageManager imageManager;

    @Inject
    @Qualifier("attachmentManager")
    private AttachmentManager attachmentManager;

    @Inject
    @Qualifier("announceManager")
    private AnnounceManager announceManager;

    @Inject
    @Qualifier("pageManager")
    private PageManager pageManager;

    @Inject
    @Qualifier("menuRepository")
    private MenuRepository menuRepository;

    @Inject
    @Qualifier("webSiteManager")
    private WebSiteManager webSiteManager;

    @Inject
    @Qualifier("companyManager")
    private CompanyManager companyManager;

    @Inject
    @Qualifier("logoManager")
    private LogoManager logoManager;

    public SecureCommunityMgmtDataController() {
    }

}

package com.hewei.spider.common.site;

import com.hewei.spider.common.constants.SpiderConstants;
import us.codecraft.webmagic.Site;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/9  15:15
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class SpiderSite {

    public static Site getSite() {
        Site site = Site.me();
        //        site.setDomain("blog.sina.com.cn");
        site.setSleepTime(SpiderConstants.SPIDER_SLEEP_TIME);
        site.setUserAgent(SpiderConstants.SPIDER_USERAGENT);
        return site;
    }
}

package com.hewei.spider.common.processor;

import com.hewei.spider.common.constants.SpiderConstants;
import com.hewei.spider.common.site.SpiderSite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/9  15:14
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class SpiderProcessor implements PageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SpiderProcessor.class);

    @Override
    public void process(Page page) {

        String url = page.getUrl().get();

        logger.info("SpiderProcessor url:{}", url);

        page.putField(SpiderConstants.SPIDER_URL, url);

        page.putField(SpiderConstants.SPIDER_HBASE_COLUMN_HTML, page.getHtml().get());

        List<String> news = page.getHtml().links().all();

        page.addTargetRequests(news);

        logger.info("SpiderProcessor new:{}", Arrays.toString(news.toArray(new String[news.size()])));

    }

    @Override
    public Site getSite() {
        return SpiderSite.getSite();
    }
}

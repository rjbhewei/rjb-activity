package com.hewei.spider.common.inits;

import com.hewei.utils.JedisUtils;
import com.hewei.spider.common.constants.SpiderConstants;
import com.hewei.spider.common.pipeline.SpiderPipeline;
import com.hewei.spider.common.processor.SpiderProcessor;
import com.hewei.utils.JedisScheduler;
import us.codecraft.webmagic.Spider;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/9  15:32
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class SpiderRunner {

    public static void start() {
        Spider spider = Spider.create(new SpiderProcessor());
        spider.addUrl(originalUrl());
        spider.setScheduler(new JedisScheduler(JedisUtils.newPool()));
        spider.setExitWhenComplete(false);
        spider.thread(1);
        spider.addPipeline(new SpiderPipeline());
        spider.run();
    }

    private static String[] originalUrl() {
        return new String[]{SpiderConstants.SPIDER_ORIGINAL_URL};
    }
}

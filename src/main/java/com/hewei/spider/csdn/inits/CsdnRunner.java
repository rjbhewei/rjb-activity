package com.hewei.spider.csdn.inits;

import com.hewei.common.utils.EsUtils;
import com.hewei.common.utils.JedisUtils;
import com.hewei.spider.csdn.constants.CsdnConstants;
import com.hewei.spider.csdn.pipeline.CsdnPipeline;
import com.hewei.spider.csdn.processor.CsdnProcessor;
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
public class CsdnRunner {

    public static void start() {
        EsUtils.createIndex(CsdnConstants.ES_INDEX_NAME);
        Spider spider = Spider.create(new CsdnProcessor());
        spider.addUrl(originalUrl());
        spider.setScheduler(new JedisScheduler(JedisUtils.newPool()));
        spider.setExitWhenComplete(false);
        spider.thread(1);
        spider.addPipeline(new CsdnPipeline());
        spider.run();
    }

    private static String[] originalUrl() {
        return new String[]{CsdnConstants.CSDN_ORIGINAL_URL};
    }
}

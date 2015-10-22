package com.hewei.spider.common.constants;

import com.hewei.common.inits.CommonInit;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/9  15:57
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class SpiderConstants {

    public static final String SPIDER_URL = "url";

    public static final String SPIDER_HTML = "html";

    public static final String SPIDER_USERAGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31";

    /** ********************************************************************* */

    public static final String SPIDER_ORIGINAL_URL = CommonInit.getString("SPIDER_ORIGINAL_URL");

    public static final int SPIDER_SLEEP_TIME = CommonInit.getInt("SPIDER_SLEEP_TIME");

    public static final int SPIDER_THREAD = CommonInit.getInt("SPIDER_THREAD");

}

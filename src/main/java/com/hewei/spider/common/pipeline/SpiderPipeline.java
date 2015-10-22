package com.hewei.spider.common.pipeline;

import com.hewei.spider.common.constants.SpiderConstants;
import com.hewei.utils.HbaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Map;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/9  16:08
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class SpiderPipeline implements Pipeline {

    private static final Logger logger = LoggerFactory.getLogger(SpiderPipeline.class);

    @Override
    public void process(ResultItems resultItems, Task task) {
        Map<String, Object> map = resultItems.getAll();
        String url = (String) map.get(SpiderConstants.SPIDER_URL);
        String html = (String) map.get(SpiderConstants.SPIDER_HTML);
        try {
            HbaseUtils.createTable(SpiderConstants.SPIDER_TABLE, SpiderConstants.SPIDER_COLUMN_FAMILY);
            HbaseUtils.addRow(SpiderConstants.SPIDER_TABLE, url, SpiderConstants.SPIDER_COLUMN_FAMILY, SpiderConstants.SPIDER_HTML, html);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("-----");
    }
}

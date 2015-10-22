package com.hewei.spider.common.pipeline;

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

        logger.info("-----");
    }
}

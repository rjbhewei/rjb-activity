package com.hewei.spider.csdn.pipeline;

import com.hewei.common.utils.EsUtils;
import com.hewei.spider.csdn.constants.CsdnConstants;
import com.hewei.spider.csdn.pojos.CsdnActivity;
import com.hewei.spider.csdn.pojos.CsdnActivityDetails;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

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
public class CsdnPipeline implements Pipeline {

    private static final Logger logger = LoggerFactory.getLogger(CsdnPipeline.class);

    private AtomicLong total=new AtomicLong();

    @Override
    public void process(ResultItems resultItems, Task task) {

        Map<String, Object> map = resultItems.getAll();

        List<CsdnActivity> activityList = (List<CsdnActivity>) map.get(CsdnConstants.ACTIVITY);

        if (CollectionUtils.isNotEmpty(activityList)) {
            for (CsdnActivity activity : activityList) {
                EsUtils.add(CsdnConstants.ES_INDEX_NAME, CsdnConstants.ACTIVITY, activity);
            }
            logger.info("total:{}", total.addAndGet(activityList.size()));
            return;
        }

        CsdnActivityDetails activityDetails = (CsdnActivityDetails) map.get(CsdnConstants.ACTIVITY_DETAILS);
        if (activityDetails != null) {
            EsUtils.add(CsdnConstants.ES_INDEX_NAME, CsdnConstants.ACTIVITY_DETAILS, activityDetails);
            logger.info("total:{}", total.incrementAndGet());
            return;
        }

        logger.info("-----");
    }
}

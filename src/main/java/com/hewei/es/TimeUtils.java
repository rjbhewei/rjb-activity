package com.hewei.es;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.hewei.constants.ESConstants;
import com.hewei.enums.SearchErr;
import com.hewei.exception.LogException;
import com.hewei.pojos.TimeLimiter;
import com.hewei.pojos.TimeRanger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/16  16:30
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class TimeUtils {

	private static final Logger logger = LoggerFactory.getLogger(TimeUtils.class);

    private static final Cache<String, TimeLimiter> cache = CacheBuilder.newBuilder().maximumSize(2).expireAfterWrite(1, TimeUnit.DAYS).build();

    static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");

    public static TimeRanger timeRanger(long start, long end, int specificDay) {

        //        long start = DateTime.now().plusDays(1).getMillis();
        //
        //        long end = DateTime.now().getMillis();

        if (start > 0 || end > 0) {
            specificDay = -1;
        }

        if (specificDay > 0) {
            if (specificDay > ESConstants.MAX_SEARCH_DAY) {
                return new TimeRanger(SearchErr.SPECIFIC_DAY_THAN_MAX_DAY, indicesMaxDay());
            } else {
                return new TimeRanger(indices(specificDay));
            }
        }

        TimeLimiter timeLimiter = cacheTimeLimiter();

        if (start <= 0) {
            start = timeLimiter.getDownTimeMillis();
        }

        if (end <= 0) {
            end = timeLimiter.getUpTimeMillis();
        }

        if (start >= timeLimiter.getDownTime().getMillis() && end <= timeLimiter.getUpTime().getMillis() && start <= end) {
            return new TimeRanger(indices(new DateTime(start), new DateTime(end)), start, end);
        }

        return new TimeRanger(SearchErr.TIME_OVERRUN, indicesMaxDay());

    }

    public static String[] indicesMaxDay() {
        return indices(ESConstants.MAX_SEARCH_DAY);
    }

    public static String[] indices(int num) {
        num = num < 1 ? 1 : num;
        String[] indices = new String[num];
        for (int i = 0; i < indices.length; i++) {
            String indexSuffix = DateTime.now().minusDays(i).toString(ESConstants.ES_INDEX_TIME_FORMAT);
            String indexName = ESConstants.ES_INDEX_NAME + ESConstants.UNDERLINE + indexSuffix;
            indices[i] = indexName;
        }
        return indices;
    }

    public static String[] indices(DateTime downTime, DateTime upTime) {

        if (downTime.isEqual(upTime)) {
            return new String[]{downTime.toString(ESConstants.ES_INDEX_TIME_FORMAT)};
        }

        String down = downTime.toString(ESConstants.ES_INDEX_TIME_FORMAT);

        List<String> list = Lists.newArrayList(ESConstants.ES_INDEX_NAME + ESConstants.UNDERLINE + down);

        for (int i = 1; i < ESConstants.MAX_SEARCH_DAY; i++) {

            DateTime tmp = downTime.plusDays(i);

            if (tmp.isAfter(upTime)) {
                break;
            }
            String indexSuffix = tmp.toString(ESConstants.ES_INDEX_TIME_FORMAT);
            String indexName = ESConstants.ES_INDEX_NAME + ESConstants.UNDERLINE + indexSuffix;
            list.add(indexName);
        }

        return list.toArray(new String[list.size()]);
    }


    public static TimeLimiter cacheTimeLimiter() {

        final DateTime now = DateTime.now();

        final String time = now.toString(ESConstants.ES_INDEX_TIME_FORMAT);

        try {
            return cache.get(time, new Callable<TimeLimiter>() {

                @Override
                public TimeLimiter call() throws Exception {
                    DateTime dayDateTime=formatter.parseDateTime(time);
                    return new TimeLimiter(dayDateTime.minusDays(ESConstants.MAX_SEARCH_DAY_WITHOUT_CURRENT), new DateTime(dayDateTime.plusDays(1).minusMillis(1)));
                }
            });
        } catch (ExecutionException e) {
			logger.error(e.getMessage(), e);
			throw new LogException(ESConstants.SERVER_ERR);
        }

    }

}

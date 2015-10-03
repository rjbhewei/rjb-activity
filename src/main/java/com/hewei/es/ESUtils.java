package com.hewei.es;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.hewei.constants.ESConstants;
import com.hewei.enums.SearchErr;
import com.hewei.exception.LogException;
import com.hewei.pojos.SearchPage;
import com.hewei.pojos.TimeRanger;
import com.hewei.pojos.request.SearchPojo;
import com.hewei.pojos.response.SearchResultImpl;
import com.hewei.pojos.response.store.SearchMessage;
import com.hewei.utils.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/8  16:09
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class ESUtils extends ES {

    private static final Logger logger = LoggerFactory.getLogger(ESUtils.class);

    private static final Cache<String, Boolean> cache = CacheBuilder.newBuilder().maximumSize(31).expireAfterWrite(1, TimeUnit.DAYS).build();

	public static boolean cacheAndCheckIndex(final String indexName) {
		try {
			return cache.get(indexName, new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return existIndex(indexName);
				}
			});
		} catch (ExecutionException e) {
			logger.error(e.getMessage(), e);
			throw new LogException(ESConstants.SERVER_ERR);
		}
	}

    public static SearchResultImpl search(SearchPojo pojo) {

        TimeRanger timeRanger = indices(pojo);

        SearchPage searchPage = SearchPage.warpSearchPage(pojo);

        String query = query(timeRanger.wrap(pojo)).buildAsBytes().toUtf8();

        SearchRequestBuilder builder = client().prepareSearch(timeRanger.getIndices()).setTypes(Strings.EMPTY_ARRAY).setFrom(searchPage.getStart()).setSize(searchPage.getSize()).setQuery(query).addFields(responseFields());

        if (StringUtils.isNotEmpty(pojo.getSearch())) {
            builder.addHighlightedField(ESConstants.MESSAGE_STR).setHighlighterPreTags(ESConstants.HIGH_LIGHTER_PRE).setHighlighterPostTags(ESConstants.HIGH_LIGHTER_POST).setHighlighterNumOfFragments(0);
        } else {
            builder.addField(ESConstants.MESSAGE_STR).addSort(ESConstants.STORE_LOG_TIME_STR, SortOrder.DESC);
        }

        logger.info(builder.toString());

        SearchResponse response = builder.get();

        List<SearchMessage> list = new ArrayList<>();

        SearchHits searchHits = response.getHits();

        for (SearchHit hit : searchHits) {

            JSONObject jsonObject = new JSONObject();

            for (SearchHitField searchHitField : hit.getFields().values()) {
                jsonObject.put(searchHitField.getName(), searchHitField.getValue());
            }

            for (HighlightField highlightField : hit.getHighlightFields().values()) {
                StringBuilder sb = new StringBuilder();
                for (Text text : highlightField.fragments()) {
                    sb.append(text.string());
                }
                jsonObject.put(highlightField.getName(), sb.toString());
            }

            list.add(JsonUtils.parse(jsonObject.toJSONString(), SearchMessage.class));

        }

        SearchErr searchErr = timeRanger.getSearchErr() != SearchErr.NO ? timeRanger.getSearchErr() : searchPage.getSearchErr();

        return new SearchResultImpl(searchHits.getTotalHits(), response.getTook().secondsFrac(), searchErr, searchPage.getPage(), searchPage.getSize(), list);
    }

    static BoolQueryBuilder query(SearchPojo pojo){

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (StringUtils.isNotEmpty(pojo.getAppName())) {
            boolQueryBuilder.must(QueryBuilders.termQuery(ESConstants.APP_NAME_STR, pojo.getAppName()));
        }

        if (StringUtils.isNotEmpty(pojo.getAppVersion())) {
            boolQueryBuilder.must(QueryBuilders.termQuery(ESConstants.APP_VERSION_STR, pojo.getAppVersion()));
        }

        if (StringUtils.isNotEmpty(pojo.getPhase())) {
            boolQueryBuilder.must(QueryBuilders.termQuery(ESConstants.PHASE_STR, pojo.getPhase()));
        }

        if (StringUtils.isNotEmpty(pojo.getEnv())) {
            boolQueryBuilder.must(QueryBuilders.termQuery(ESConstants.ENV_STR, pojo.getEnv()));
        }

        if (StringUtils.isNotEmpty(pojo.getClassName())) {
            boolQueryBuilder.must(QueryBuilders.termQuery(ESConstants.CLASS_NAME_STR, pojo.getClassName()));
        }

        if (StringUtils.isNotEmpty(pojo.getMethodName())) {
            boolQueryBuilder.must(QueryBuilders.termQuery(ESConstants.METHOD_NAME_STR, pojo.getMethodName()));
        }

        if (pojo.getLineNum() > 0) {
            boolQueryBuilder.must(QueryBuilders.termQuery(ESConstants.LINE_NUM_STR, pojo.getLineNum()));
        }

        if (StringUtils.isNotEmpty(pojo.getThreadName())) {
            boolQueryBuilder.must(QueryBuilders.termQuery(ESConstants.THREAD_NAME_STR, pojo.getThreadName()));
        }

        if (StringUtils.isNotEmpty(pojo.getContainerID())) {
            boolQueryBuilder.must(QueryBuilders.termQuery(ESConstants.CONTAINER_ID_STR, pojo.getContainerID()));
        }

        if (StringUtils.isNotEmpty(pojo.getSearch())) {
            boolQueryBuilder.must(QueryBuilders.queryStringQuery(pojo.getSearch()).field(ESConstants.MESSAGE_STR));
        }

        if (pojo.getStartTime() > 0 && pojo.getEndTime() > 0) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery(ESConstants.STORE_LOG_TIME_STR).gte(new DateTime(pojo.getStartTime()).toString(ESConstants.ES_DATE_FORMAT)).lte(new DateTime(pojo.getEndTime()).toString(ESConstants.ES_DATE_FORMAT)));
        }

        return boolQueryBuilder;
    }

    static TimeRanger indices(SearchPojo pojo) {

        List<String> indexList = new ArrayList<>();

        TimeRanger timeRanger = TimeUtils.timeRanger(pojo.getStartTime(), pojo.getEndTime(), pojo.getSpecificDay());

        logger.info("index check start:{}", Arrays.toString(timeRanger.getIndices()));

        for (String indexName : timeRanger.getIndices()) {
            if (cacheAndCheckIndex(indexName)) {
                indexList.add(indexName);
            }
        }

        String[] indices = indexList.toArray(new String[indexList.size()]);

        logger.info("index check end:{}", Arrays.toString(indices));

        timeRanger.setIndices(indices);

        return timeRanger;
    }

    static String[] responseFields() {
        return new String[]{ESConstants.APP_NAME_STR, ESConstants.APP_VERSION_STR,
                ESConstants.PHASE_STR, ESConstants.ENV_STR, ESConstants.CONTAINER_ID_STR,
                ESConstants.LOG_PATH_STR, ESConstants.CLASS_NAME_STR, ESConstants.METHOD_NAME_STR,
                ESConstants.LINE_NUM_STR, ESConstants.THREAD_NAME_STR, ESConstants.STORE_LOG_TIME_STR};
    }

}

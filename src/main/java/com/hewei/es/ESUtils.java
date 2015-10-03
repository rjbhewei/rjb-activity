package com.hewei.es;

import com.alibaba.fastjson.JSONObject;
import com.hewei.constants.ESConstants;
import com.hewei.pojos.SearchPage;
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
import java.util.List;

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

    public static SearchResultImpl search(SearchPojo pojo) {

        SearchPage searchPage = SearchPage.warpSearchPage(pojo);

        String query = query(pojo).buildAsBytes().toUtf8();

        SearchRequestBuilder builder = client().prepareSearch(ESConstants.ES_INDEX_NAME).setTypes(Strings.EMPTY_ARRAY).setFrom(searchPage.getStart()).setSize(searchPage.getSize()).setQuery(query).addFields(responseFields());

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

        return new SearchResultImpl(searchHits.getTotalHits(), response.getTook().secondsFrac(), searchPage.getPage(), searchPage.getSize(), list);
    }

    static BoolQueryBuilder query(SearchPojo pojo){

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (StringUtils.isNotEmpty(pojo.getSearch())) {
            boolQueryBuilder.must(QueryBuilders.queryStringQuery(pojo.getSearch()).field(ESConstants.MESSAGE_STR));
        }

        if (pojo.getStartTime() > 0 && pojo.getEndTime() > 0) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery(ESConstants.STORE_LOG_TIME_STR).gte(new DateTime(pojo.getStartTime()).toString(ESConstants.ES_DATE_FORMAT)).lte(new DateTime(pojo.getEndTime()).toString(ESConstants.ES_DATE_FORMAT)));
        }

        return boolQueryBuilder;
    }


    static String[] responseFields() {
        return new String[]{ESConstants.APP_NAME_STR, ESConstants.APP_VERSION_STR,
                ESConstants.PHASE_STR, ESConstants.ENV_STR, ESConstants.CONTAINER_ID_STR,
                ESConstants.LOG_PATH_STR, ESConstants.CLASS_NAME_STR, ESConstants.METHOD_NAME_STR,
                ESConstants.LINE_NUM_STR, ESConstants.THREAD_NAME_STR, ESConstants.STORE_LOG_TIME_STR};
    }

}

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

		String[] fields = responseFields(pojo);

		SearchRequestBuilder builder = client().prepareSearch(ESConstants.ES_INDEX_NAME).setTypes(pojo.getUrlType()).setFrom(searchPage.getStart()).setSize(searchPage.getSize()).setQuery(query).addFields(fields);

		extensionBuilder(pojo, builder);

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
            boolQueryBuilder.must(QueryBuilders.queryStringQuery(pojo.getSearch()).field(ESConstants.DETAIL_DESC));
        }

        if (pojo.getStartTime() > 0 && pojo.getEndTime() > 0) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery(ESConstants.DETAIL_TIME).gte(new DateTime(pojo.getStartTime()).toString(ESConstants.ES_DATE_FORMAT)).lte(new DateTime(pojo.getEndTime()).toString(ESConstants.ES_DATE_FORMAT)));
        }

        return boolQueryBuilder;
    }


	static String[] responseFields(SearchPojo pojo) {
		if (ESConstants.ACTIVITY.equals(pojo.getUrlType())) {
			return new String[]{
					ESConstants.URL,
					ESConstants.PICTURE,
					ESConstants.TITLE,
					ESConstants.DESC,
					ESConstants.TIME,
					ESConstants.LOCATION
					};
		}

		if (ESConstants.ACTIVITY_DETAILS.equals(pojo.getUrlType())) {
			return new String[]{
					ESConstants.DETAIL_TITLE,
					ESConstants.DETAIL_TIME,
					ESConstants.DETAIL_LOCATION,
					ESConstants.DETAIL_EXPENSE,
					ESConstants.DETAIL_TYPE,
					ESConstants.DETAIL_INITIATOR,
					ESConstants.DETAIL_INITIATOR_URL
//					ESConstants.DETAIL_DESC
			};
		}

		return new String[0];
	}

	static void extensionBuilder(SearchPojo pojo, SearchRequestBuilder builder) {
		if (ESConstants.ACTIVITY.equals(pojo.getUrlType())) {
			return;
		}

		if (StringUtils.isNotEmpty(pojo.getSearch())) {
			builder.addHighlightedField(ESConstants.DETAIL_DESC).setHighlighterPreTags(ESConstants.HIGH_LIGHTER_PRE).setHighlighterPostTags(ESConstants.HIGH_LIGHTER_POST).setHighlighterNumOfFragments(0);
		} else {
			builder.addField(ESConstants.DETAIL_DESC).addSort(ESConstants.DETAIL_TIME, SortOrder.DESC);
		}
	}

}

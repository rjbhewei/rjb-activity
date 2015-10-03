package com.hewei.es;

import com.hewei.constants.ESConstants;
import com.hewei.pojos.request.SearchPojo;
import org.elasticsearch.action.suggest.SuggestRequestBuilder;
import org.elasticsearch.action.suggest.SuggestResponse;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.joda.time.DateTime;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/17  19:59
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class Test {
    public static void main(String[] args) {
//        testPrefixSearch();
        suggest();
    }

    public static void testPrefixSearch() {
        SearchPojo pojo = new SearchPojo();
        pojo.setAppName("campaign");
        pojo.setPhase("qa");
        pojo.setEnv("d04");
        pojo.setClassName("AbstractConnector");
        pojo.setMethodName("doStart");
        pojo.setLineNum(266);

//        pojo.setPrefixType(ESConstants.APP_NAME_STR);
//        pojo.setAppName("c");

//        pojo.setPrefixType(ESConstants.PHASE_STR);
//        pojo.setPhase("q");

//        pojo.setPrefixType(ESConstants.ENV_STR);
//        pojo.setEnv("d");

//        pojo.setPrefixType(ESConstants.CLASS_NAME_STR);
//        pojo.setClassName("A");

        pojo.setPrefixType(ESConstants.METHOD_NAME_STR);
        pojo.setMethodName("d");


        ESUtils.prefixSearch(pojo);
    }

    public static void testSearch() {
        //        test(ESConstants.ES_INDEX);
        //        queryBaseByDays("", "", "", "", 0);
        //        testSearch();
        //        System.out.println("index:"+ Arrays.toString(indices(0)));


        SearchPojo pojo = new SearchPojo();
        pojo.setAppName("campaign");
        pojo.setPhase("qa");
        pojo.setEnv("d04");
        pojo.setClassName("AbstractConnector");
        pojo.setMethodName("doStart");
        pojo.setLineNum(266);
        pojo.setSpecificDay(4);
        //        pojo.setSearch("CampaignApplication");
        //        pojo.setAppVersion("AbstractConnector");
        //        pojo.setThreadName("日志打印线程名");
        pojo.setStartTime(DateTime.now().minusDays(5).getMillis());
        pojo.setEndTime(DateTime.now().minusHours(1).getMillis());

        ESUtils.search(pojo);
    }

    public static void suggest(){
//        TermSuggestionBuilder suggestionBuilder = SuggestBuilders.termSuggestion("suggestName").text("doStart").suggestMode("always").field(ESConstants.METHOD_NAME_STR).size(10);
//        SearchRequestBuilder builder = ES.client().prepareSearch("log_index_20150912").setTypes(Strings.EMPTY_ARRAY).setFrom(0).setSize(0).addSuggestion(suggestionBuilder);
//        System.out.println(builder);
//        SearchResponse response =builder.get();
//        System.out.println(response.getTook());
//        System.out.println(response.getHits().getTotalHits());
//        System.out.println(response);
////        response.getSuggest()

        CompletionSuggestionBuilder suggestionsBuilder = new CompletionSuggestionBuilder(
                "complete");
        suggestionsBuilder.text("台");
        suggestionsBuilder.field("suggest");
        suggestionsBuilder.size(10);

        SuggestRequestBuilder builder=ES.client().prepareSuggest("test3")
                .addSuggestion(suggestionsBuilder);
        System.out.println(builder);
        SuggestResponse resp = builder.execute().actionGet();
        System.out.println(resp);


    }

}

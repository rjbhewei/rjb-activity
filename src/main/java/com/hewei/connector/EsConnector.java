package com.hewei.connector;

import com.hewei.constants.ESConstants;
import com.hewei.es.ESUtils;
import com.hewei.exception.LogException;
import com.hewei.pojos.request.SearchPojo;
import com.hewei.pojos.response.SearchResult;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/17  0:31
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class EsConnector {

    public static SearchResult urlSearch(SearchPojo pojo) {
        if ("search".equals(pojo.getUrlType())) {
            return ESUtils.search(pojo);
        }
        if ("prefixSearch".equals(pojo.getUrlType())) {
            return ESUtils.prefixSearch(pojo);
        }
        throw new LogException(ESConstants.URL_TYPE_ERROR);
    }
}

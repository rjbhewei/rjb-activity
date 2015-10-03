package com.hewei.pojos.response;

import com.hewei.constants.ESConstants;
import com.hewei.pojos.response.store.SearchMessage;

import java.util.List;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/15  19:19
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class SearchResultImpl extends SearchResult {

    private List<SearchMessage> messages;

    private int page = 1;

    private int pageSize = ESConstants.DEFAULT_PAGE_SIZE;

    public SearchResultImpl() {
    }

    public SearchResultImpl(long totalHits, double tookInMillis,int page, int pageSize, List<SearchMessage> messages) {
        super(totalHits, tookInMillis);
        this.messages = messages;
        this.page = page;
        this.pageSize = pageSize;
    }

    public List<SearchMessage> getMessages() {
        return messages;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }
}

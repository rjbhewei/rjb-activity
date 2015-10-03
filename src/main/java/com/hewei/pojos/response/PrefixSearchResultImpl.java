package com.hewei.pojos.response;

import com.hewei.pojos.response.store.PrefixSearchMessage;

import java.util.List;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/17  19:43
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class PrefixSearchResultImpl extends SearchResult {

    private List<PrefixSearchMessage> messages;

    public PrefixSearchResultImpl() {}

    public PrefixSearchResultImpl(long totalHits, double tookInMillis, List<PrefixSearchMessage> messages) {
        super(totalHits, tookInMillis);
        this.messages = messages;
    }

    public List<PrefixSearchMessage> getMessages() {
        return messages;
    }
}

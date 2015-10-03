package com.hewei.pojos.response.store;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/8  21:30
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class PrefixSearchMessage {

    private String prefixType;

    private long docCount;

    public PrefixSearchMessage() {
    }

    public PrefixSearchMessage(String prefixType, long docCount) {
        this.prefixType = prefixType;
        this.docCount = docCount;
    }

    public String getPrefixType() {
        return prefixType;
    }

    public long getDocCount() {
        return docCount;
    }
}

package com.hewei.pojos.response;
/**
 * 
 * @author hewei
 * 
 * @date 2015/9/17  19:41
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class SearchResult {

    private long totalHits;

    private double tookInMillis;

    public SearchResult() {
    }

    public SearchResult(long totalHits, double tookInMillis) {
        this.totalHits = totalHits;
        this.tookInMillis = tookInMillis;
    }

    public double getTookInMillis() {
        return tookInMillis;
    }

    public void setTookInMillis(double tookInMillis) {
        this.tookInMillis = tookInMillis;
    }

    public long getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(long totalHits) {
        this.totalHits = totalHits;
    }
}

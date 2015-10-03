package com.hewei.enums;
/**
 * 
 * @author hewei
 * 
 * @date 2015/9/16  16:54
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public enum SearchErr {
    NO("success"),
    TIME_OVERRUN("start time or end time overrun,search limit 7 days (include today),your need vip authority or call maqian/hewei,now search for lately 7 days data"),
    SPECIFIC_DAY_THAN_MAX_DAY("specific day large then max days limit (7 days include today),your need vip authority or call maqian/hewei,now search for lately 7 days data"),
    SEARCH_DATA_LARGE("search data too large,your need vip authority or call maqian/hewei,now search for first page");

    private String errorMessage;

    private SearchErr(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getMessage(){
        return errorMessage;
    }

}

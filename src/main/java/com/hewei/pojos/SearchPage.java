package com.hewei.pojos;

import com.hewei.constants.ESConstants;
import com.hewei.enums.PageSize;
import com.hewei.enums.SearchErr;
import com.hewei.pojos.request.SearchPojo;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/16  19:27
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class SearchPage {

    private int start;

    private int size;

    private SearchErr searchErr = SearchErr.NO;

    public SearchPage(int start, int size,SearchErr searchErr) {
        this.start = start;
        this.size = size;
        this.searchErr=searchErr;
    }

    public SearchPage(int start, int size) {
        this.start = start;
        this.size = size;
    }

    public int getStart() {
        return start;
    }

    public int getSize() {
        return size;
    }

    public SearchErr getSearchErr() {
        return searchErr;
    }

    public static SearchPage warpSearchPage(SearchPojo pojo) {
        int pageSize = pojo.getPageSize() < 1 ? ESConstants.DEFAULT_PAGE_SIZE : PageSize.getSize(pojo.getPageSize());
        int start = pojo.getPage() <= 1 ? ESConstants.DEFAULT_PAGE_START : (pojo.getPage() - 1) * pojo.getPageSize();
        return pageSize + start <= ESConstants.DEFAULT_SEARCH_SIZE ? new SearchPage(start, pageSize) : new SearchPage(ESConstants.DEFAULT_PAGE_START, ESConstants.DEFAULT_PAGE_SIZE, SearchErr.SEARCH_DATA_LARGE);
    }

    public int getPage() {
        int page = 1;
        for (; page < getStart(); page++) {
            if (page * getSize() > getStart()) {
                break;
            }
        }
        return page;
    }

    public static void main(String[] args) {
        System.out.println(new SearchPage(40, ESConstants.DEFAULT_PAGE_SIZE, SearchErr.SEARCH_DATA_LARGE).getPage());
    }
}

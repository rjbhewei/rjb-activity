package com.hewei.enums;
/**
 * 
 * @author hewei
 * 
 * @date 2015/9/16  19:28
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public enum PageSize {

    TEN(10), TWENTY(20), THIRTY(30), FORTY(40), FIFTY(50);

    private int size;

    private PageSize(int size) {
        this.size = size;
    }

    public static int getSize(int initSize) {
        for (PageSize pageSize : PageSize.class.getEnumConstants()) {
            if (initSize <= pageSize.size) {
                return pageSize.size;
            }
        }
        return FIFTY.size;
    }

}

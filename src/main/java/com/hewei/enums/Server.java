package com.hewei.enums;

import com.hewei.constants.ESConstants;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/25  13:54
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public enum Server {

    produce, develop;

    private static final String[] produceServer = new String[]{ESConstants.PRODECE_SERVER};

    public static Server checkServer(String hostName) {
        for (String start : produceServer) {
            if (hostName.startsWith(start)) {
                return produce;
            }
        }
        return develop;
    }


}

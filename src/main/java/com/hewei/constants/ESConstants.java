package com.hewei.constants;

import com.hewei.connector.LogInit;

/**
 * 
 * @author hewei
 * 
 * @date 2015/8/18  15:53
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class ESConstants {

    public static final String PRODECE_SERVER = "nebula-logcenter";

    /** ************************************* 配置 ********************************************** */

    public static final String ES_CLUSTER_NAME = LogInit.getString("ES_CLUSTER_NAME");

    public static final int ES_PORT = LogInit.getInt("ES_PORT");

    public static final String ES_IP = LogInit.getString("ES_IP");


    public static final String REDIS_TAILF_IP = LogInit.getString("REDIS_TAILF_IP");

    public static final int REDIS_TAILF_PORT = LogInit.getInt("REDIS_TAILF_PORT");

    public static final int REDIS_MAX_TOTAL = LogInit.getInt("REDIS_MAX_TOTAL");

    public static final int REDIS_MAX_IDLE = LogInit.getInt("REDIS_MAX_IDLE");

    public static final int REDIS_MAX_WAIT_MILLIS = LogInit.getInt("REDIS_MAX_WAIT_MILLIS");

    public static final int REDIS_TIMEOUT = LogInit.getInt("REDIS_TIMEOUT");

    public static final String ES_INDEX_TIME_FORMAT = "yyyyMMdd";

    public static final String ES_INDEX_NAME = "log_index";

    public static final String UNDERLINE = "_";

    public static final int MAX_SEARCH_DAY = 7;//包括当天

    public static final int MAX_SEARCH_DAY_WITHOUT_CURRENT = MAX_SEARCH_DAY - 1;//除了当天最大的搜索时间

    public static final int DEFAULT_PAGE_START = 0;

    public static final int DEFAULT_PAGE_SIZE = 20;

    public static final int DEFAULT_SEARCH_SIZE = 500;

    public static final int HTTP_PORT = LogInit.getInt("HTTP_PORT");

    public static final int WEB_SOCKET_HTTP_PORT = LogInit.getInt("WEB_SOCKET_HTTP_PORT");

    /**************************es字段需要跟mapping一样***********************/

    public static final String APP_NAME_STR = "appName";

    public static final String APP_VERSION_STR = "appVersion";

    public static final String PHASE_STR = "phase";

    public static final String ENV_STR = "env";

    public static final String CLASS_NAME_STR = "className";

    public static final String CONTAINER_ID_STR = "containerID";

    public static final String METHOD_NAME_STR = "methodName";

    public static final String LINE_NUM_STR = "lineNum";

    public static final String THREAD_NAME_STR = "threadName";

    public static final String MESSAGE_STR = "message";

    public static final String LOG_PATH_STR = "logPath";

    public static final String STORE_LOG_TIME_STR = "storeLogTime";

    public static final String HIGH_LIGHTER_PRE="<a class=\"highlighterKeyWord\">";

    public static final String HIGH_LIGHTER_POST="</a>";

    public static final String ES_DATE_FORMAT="yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    /**************************tip***********************/

    public static final String SERVER_ERR = "server with some error";

    public static final String OPST_ONLY = "server only accept post request";

    public static final String URL_EMPTY = "request url is empty";

    public static final String URL_ERROR = "request url error";

    public static final String URL_TYPE_ERROR = "url type error";



}

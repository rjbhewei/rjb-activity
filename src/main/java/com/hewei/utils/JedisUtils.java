package com.hewei.utils;

import com.hewei.constants.ESConstants;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/21  16:56
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class JedisUtils {



    public static JedisPool newTailfPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(ESConstants.REDIS_MAX_TOTAL);
        config.setMaxIdle(ESConstants.REDIS_MAX_IDLE);
        config.setMaxWaitMillis(ESConstants.REDIS_MAX_WAIT_MILLIS);
        config.setTestOnBorrow(true);
        return new JedisPool(config, ESConstants.REDIS_TAILF_IP, ESConstants.REDIS_TAILF_PORT, ESConstants.REDIS_TIMEOUT);
    }


}

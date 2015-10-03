package com.hewei.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/21  19:01
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public abstract class RedisPoolSender<T> {

    public abstract T execute(Jedis jedis) throws Exception;

    public T run(JedisPool pool) {
        boolean broken = false;
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return execute(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            broken = true;
            return null;
        } finally {
            if (jedis != null) {
                if (broken) {
                    pool.returnBrokenResource(jedis);
                } else {
                    pool.returnResource(jedis);
                }
            }
        }
    }

}
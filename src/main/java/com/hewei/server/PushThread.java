package com.hewei.server;

import com.hewei.utils.RedisPoolSender;
import redis.clients.jedis.Jedis;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/24  16:56
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class PushThread extends Thread {

	private String key;

	public PushThread(String key) {
		this.key = key;
	}

	@Override
	public void run() {
		new RedisPoolSender<Long>() {

			@Override
			public Long execute(Jedis jedis) throws Exception {
				LogPubSub logPubSub = new LogPubSub(key);
				PushServer.SCAN_SET.add(logPubSub);
				jedis.subscribe(logPubSub, key);
				return null;
			}
		}.run(PushServer.POOL);
	}

}

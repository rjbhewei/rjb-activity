package com.hewei.server;

import com.google.common.collect.Sets;
import com.hewei.utils.JedisUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.commons.collections.CollectionUtils;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/24  16:50
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class PushServer {

	public static final Map<String, Set<ChannelId>> GROUP_MAP = new ConcurrentHashMap<>();

	public static ChannelGroup GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	public static JedisPool POOL = JedisUtils.newTailfPool();

	public static final Set<LogPubSub> SCAN_SET = Sets.newConcurrentHashSet();

	static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10);

	static final ExecutorService SINGLE_EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

    static {
        SINGLE_EXECUTOR_SERVICE.execute(new Thread() {//无推送的情况

            @Override
            public void run() {
                while (true) {

                    if (CollectionUtils.isEmpty(SCAN_SET)) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    for (LogPubSub logPubSub : SCAN_SET) {
                        Set<ChannelId> set = PushServer.GROUP_MAP.get(logPubSub.getKey());
                        if (CollectionUtils.isEmpty(set)) {
                            if (logPubSub.isSubscribed()) {
                                logPubSub.unsubscribe();
                            }
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

	public static void addChannel(String key, Channel channel) {
		Set<ChannelId> set = GROUP_MAP.get(key);
		if (set == null) {
			GROUP_MAP.put(key, set = Sets.newConcurrentHashSet());
		}
		set.add(channel.id());
		GROUP.add(channel);
	}

	public static void removeChannel(Channel channel) {
		GROUP.remove(channel);
		for (Map.Entry<String, Set<ChannelId>> entry : GROUP_MAP.entrySet()) {//map数据不多,没性能问题
			Set<ChannelId> set = entry.getValue();
			if (set == null) {
				continue;
			}
			boolean isRemove = set.remove(channel.id());
			if (set.isEmpty()) {
				GROUP_MAP.remove(entry.getKey());
			}
			if (isRemove) {
				break;
			}
		}
	}

    public static void execute(String key){
		if (GROUP_MAP.keySet().contains(key)) {
			return;
		}
		EXECUTOR_SERVICE.execute(new PushThread(key));
    }

}

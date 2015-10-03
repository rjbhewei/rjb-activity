package com.hewei.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

import java.util.Set;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/25  3:00
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class LogPubSub extends JedisPubSub {

	private static final Logger logger = LoggerFactory.getLogger(LogPubSub.class);

	private String key;

	public LogPubSub(String key) {
		this.key = key;
	}

	public void onMessage(String channelName, String message) {
		logger.info("channelName:{}", channelName);
//		logger.info("message:{}", message);
		logger.info("GROUP size:{}", PushServer.GROUP.size());

		Set<ChannelId> set = PushServer.GROUP_MAP.get(key);

		if (CollectionUtils.isEmpty(set) || PushServer.GROUP.size() == 0) {
			unsubscribe();
			return;
		}

		for (ChannelId channelId : set) {
			Channel channel = PushServer.GROUP.find(channelId);
			if (channel == null) {
				continue;
			}
			if (!channel.isOpen()) {
				PushServer.removeChannel(channel);
			}
			channel.writeAndFlush(new TextWebSocketFrame(message));
		}
	}

	public void onSubscribe(String channelName, int subscribedChannels) {
		logger.info("subscribe channelName is :{}", channelName);
		logger.info("subscribedChannels is 1:" + subscribedChannels);
	}

	public void onUnsubscribe(String channel, int subscribedChannels) {
		logger.info("channel is foo:{}", channel);
		logger.info("subscribedChannels is 0:{}", subscribedChannels);
	}

	public String getKey() {
		return key;
	}
}

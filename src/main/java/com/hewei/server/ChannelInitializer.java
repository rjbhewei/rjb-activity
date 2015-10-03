package com.hewei.server;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsHandler;

/**
 * 
 * @author hewei
 * 
 * @date 2015/8/18  15:42
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class ChannelInitializer extends io.netty.channel.ChannelInitializer<SocketChannel> {

    //    GlobalTrafficShapingHandler globalTrafficShapingHandler = new GlobalTrafficShapingHandler(new DefaultEventLoopGroup(8), 1024, 1024);

    private static final CorsConfig config = CorsConfig.withAnyOrigin().maxAge(24 * 60 * 60).allowedRequestHeaders(allowedRequestHeaders()).allowedRequestMethods(HttpMethod.POST).allowCredentials().build();

    private static final DefaultEventLoopGroup loopGroup = new DefaultEventLoopGroup(100);

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

//        ch.config().setWriteBufferLowWaterMark(100);
//
//        ch.config().setWriteBufferHighWaterMark(100);

		ChannelPipeline pipeline = ch.pipeline();

		pipeline.addLast("serverCodec", new HttpServerCodec());

		pipeline.addLast("aggregator", new HttpObjectAggregator(Integer.MAX_VALUE));

		pipeline.addLast("corsHandler", new CorsHandler(config));

        pipeline.addLast("httpRequestFilter", new HttpRequestFilterHandler());

        pipeline.addLast(loopGroup, "handler", new HttpServerHandler());

    }

	private static String[] allowedRequestHeaders() {
		return new String[]{"accept", "content-type"};
	}
}

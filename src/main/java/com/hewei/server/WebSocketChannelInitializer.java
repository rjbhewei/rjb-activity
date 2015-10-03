package com.hewei.server;

import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/21  13:45
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class WebSocketChannelInitializer  extends io.netty.channel.ChannelInitializer<SocketChannel> {

    private static final DefaultEventLoopGroup loopGroup = new DefaultEventLoopGroup(10);

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new WebSocketServerCompressionHandler());
        pipeline.addLast(loopGroup,"webSocket",new WebSocketChannelHandler());
    }
}

package com.hewei.main;

import com.hewei.constants.ESConstants;
import com.hewei.server.ChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/21  19:07
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class HttpLauncher {

    private static final Logger logger = LoggerFactory.getLogger(HttpLauncher.class);

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    public int http_port;

    public HttpLauncher() {
        http_port = ESConstants.HTTP_PORT;
    }

    public void start() throws InterruptedException {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        bootstrap.option(ChannelOption.SO_REUSEADDR, true);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.handler(new LoggingHandler(LogLevel.INFO));
        bootstrap.childHandler(new ChannelInitializer());
        Channel ch = bootstrap.bind(http_port).sync().channel();
        logger.info("service can recieve http request and the port on " + http_port);
        ch.closeFuture().sync();

    }

    public static void asynStart() {
        new Thread() {

            @Override
            public void run() {
                try {
                    new HttpLauncher().start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws InterruptedException {
        new HttpLauncher().start();
    }
}

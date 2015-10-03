package com.hewei.server;

import com.hewei.connector.EsConnector;
import com.hewei.constants.ESConstants;
import com.hewei.exception.LogException;
import com.hewei.pojos.request.SearchPojo;
import com.hewei.pojos.response.SearchResult;
import com.hewei.pojos.response.SearchResultImpl;
import com.hewei.utils.JsonUtils;
import com.hewei.utils.ResponseUtils;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hewei
 * @version 5.0
 * @date 2015/8/18  15:46
 * @desc
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpContent> {

	private static final Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

	private boolean isKeepAlive;

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, HttpContent chunk) throws Exception {

		isKeepAlive = HttpHeaderUtil.isKeepAlive((HttpMessage) chunk);

		String request = chunk.content().toString(CharsetUtil.UTF_8);

		logger.info("client request :{}", request);

		SearchPojo pojo = JsonUtils.parse(request, SearchPojo.class);

        if (StringUtils.isEmpty(pojo.getUrlType())) {
            throw new LogException(ESConstants.URL_TYPE_ERROR);
        }

		SearchResult result = EsConnector.urlSearch(pojo);

		String responseStr = JsonUtils.toJson(result);

		logger.info("server response:{}", responseStr);

		FullHttpResponse response = ResponseUtils.toFullHttpResponse(responseStr.getBytes(CharsetUtil.UTF_8));

		ChannelFuture future = ctx.writeAndFlush(response);

		if (!isKeepAlive) {
			future.addListener(ChannelFutureListener.CLOSE);
		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

		logger.error(cause.getMessage(), cause);

		if (cause instanceof LogException) {

			LogException exception = (LogException) cause;

			SearchResultImpl result = new SearchResultImpl();

			String responseStr = JsonUtils.toJson(result);

			logger.info("server error response:{}", responseStr);

			FullHttpResponse response = ResponseUtils.toFullHttpResponse(responseStr.getBytes(CharsetUtil.UTF_8));

			ctx.writeAndFlush(response);

		}

		if (!isKeepAlive) {
			ctx.channel().close();
		}
	}
}

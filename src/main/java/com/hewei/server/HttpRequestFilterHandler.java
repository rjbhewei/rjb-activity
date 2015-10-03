package com.hewei.server;

import com.hewei.constants.ESConstants;
import com.hewei.exception.LogException;
import com.hewei.pojos.response.SearchResultImpl;
import com.hewei.utils.JsonUtils;
import com.hewei.utils.ResponseUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/17  1:56
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class HttpRequestFilterHandler extends SimpleChannelInboundHandler<HttpRequest> {

	private static final Logger logger = LoggerFactory.getLogger(HttpRequestFilterHandler.class);

    public HttpRequestFilterHandler() {
        super(false);
    }

	private boolean isKeepAlive;

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, HttpRequest request) throws Exception {

		isKeepAlive = HttpHeaderUtil.isKeepAlive(request);

		if (StringUtils.isEmpty(request.uri())) {
			throw new LogException(ESConstants.URL_EMPTY);
		}

		if (!"/ItActivity".equals(request.uri())) {
			throw new LogException(ESConstants.URL_ERROR);
		}

		if (HttpHeaderUtil.is100ContinueExpected(request)) {
			ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
		}

		if (!request.method().equals(HttpMethod.POST)) {
			throw new LogException(ESConstants.OPST_ONLY);
		}

		ctx.fireChannelRead(request);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error(cause.getMessage(), cause);

		if (cause instanceof LogException) {

//			LogException exception = (LogException) cause;

			SearchResultImpl result = new SearchResultImpl();

//			result.setErr(exception.getErrDesc());

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

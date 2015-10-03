package com.hewei.utils;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.*;

import static io.netty.buffer.Unpooled.copiedBuffer;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/17  1:38
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class ResponseUtils {

	public static FullHttpResponse toFullHttpResponse(byte[] responses) {
		return toFullHttpResponse(copiedBuffer(responses));
	}

	public static FullHttpResponse toFullHttpResponse(ByteBuf buf) {

		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);

		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");

		response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());

		response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

		return response;
	}

}

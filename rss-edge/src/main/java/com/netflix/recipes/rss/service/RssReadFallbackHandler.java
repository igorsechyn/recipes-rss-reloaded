package com.netflix.recipes.rss.service;

import java.nio.charset.Charset;
import java.util.Map;

import rx.Observable;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;

import com.netflix.hystrix.HystrixExecutableInfo;
import com.netflix.ribbon.hystrix.FallbackHandler;

public class RssReadFallbackHandler implements FallbackHandler<ByteBuf> {

	@Override
	public Observable<ByteBuf> getFallback(HystrixExecutableInfo<?> paramHystrixExecutableInfo, Map<String, Object> paramMap) {
        byte[] bytes = "{}".getBytes(Charset.defaultCharset());
        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.buffer(bytes.length);
        byteBuf.writeBytes(bytes);
		return Observable.just(byteBuf);
	}
}

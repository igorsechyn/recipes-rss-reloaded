package com.netflix.recipes.rss.service;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

import rx.Notification;
import rx.Observable;
import rx.functions.Func1;

import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.util.HystrixTimer;
import com.netflix.ribbon.Ribbon;
import com.netflix.ribbon.proxy.ProxyLifeCycle;

public class AppRunner {

	public static void main(String[] args) {
        ConfigurationManager.getConfigInstance().setProperty("RssService.ribbon." + CommonClientConfigKey.MaxAutoRetriesNextServer, "3");
        ConfigurationManager.getConfigInstance().setProperty("RssService.ribbon." + CommonClientConfigKey.ListOfServers, "localhost:" + 8888);
        final RssService rssService = Ribbon.from(RssService.class);

        rssService.subscribe("igor", "http://rss.cnn.com/rss/edition.rss").toObservable().materialize().flatMap(new Func1<Notification<ByteBuf>, Observable<ByteBuf>>() {
			@Override
			public Observable<ByteBuf> call(Notification<ByteBuf> paramT1) {
				return rssService.rssByUserId("igor").toObservable();
			}
		}).materialize().flatMap(new Func1<Notification<ByteBuf>, Observable<Void>>() {
			@Override
			public Observable<Void> call(Notification<ByteBuf> subscriptions) {
				System.out.println(subscriptions.getValue().toString(Charset.defaultCharset()));
				return null;
			}
		}).materialize().toBlocking().last();
		HystrixTimer.reset();
        ((ProxyLifeCycle) rssService).shutdown();
	}
}

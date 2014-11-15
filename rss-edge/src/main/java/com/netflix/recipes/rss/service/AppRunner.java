package com.netflix.recipes.rss.service;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.RxNetty;

import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.util.HystrixTimer;
import com.netflix.ribbon.Ribbon;
import com.netflix.ribbon.proxy.ProxyLifeCycle;

public class AppRunner {

	static Logger log = LoggerFactory.getLogger(AppRunner.class);
	
	public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
    ConfigurationManager.getConfigInstance().setProperty("RssService.ribbon." + CommonClientConfigKey.MaxAutoRetriesNextServer, "3");
    ConfigurationManager.getConfigInstance().setProperty("RssService.ribbon." + CommonClientConfigKey.ListOfServers, "localhost:" + 8888);
    log.info("lskdjfsdf");
    final RssService rssService = Ribbon.from(RssService.class);
    triggerSubscriptions(rssService);

    rssService.rssByUserId("igor").toObservable().flatMap(subscriptionURLs -> {
			Gson gson = new Gson();
			log.info("dsfsf");
			JsonArray subscritions = gson.fromJson(subscriptionURLs.toString(Charset.defaultCharset()), JsonArray.class);
			return Observable.from(subscritions);
		}).flatMap(urlElement -> {
			String url = urlElement.getAsString();
			Observable<ByteBuf> contentObservable = RxNetty.createHttpGet(url).flatMap(response -> {
				return response.getContent();
			});
			return contentObservable;
		}).onErrorReturn(t1 -> {
			log.error("", t1);
			return null;
		}).subscribe(content -> {
			log.info("next");
			if (content != null) {
				System.out.println(content.toString(Charset.defaultCharset()));				
			}
		},
		e -> {
			log.info("No connection");
			log.error("Error", e);
		},
		() -> {
			log.info("complete");
			HystrixTimer.reset();
		});
    ((ProxyLifeCycle) rssService).shutdown();
	}

	private static void triggerSubscriptions(RssService rssService) {
		rssService.subscribe("igor", "http://rss.cnn.com/rss/edition.rss").toObservable().materialize().toBlocking().last();
		rssService.subscribe("igor", "http://feeds.washingtonpost.com/rss/politics").toObservable().materialize().toBlocking().last();
	}
	
}



package com.netflix.recipes.rss.server;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.Observable.OnSubscribe;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.netflix.karyon.transport.http.SimpleUriRouter;
import com.netflix.recipes.rss.manager.RSSManager;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpMethod;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import io.reactivex.netty.protocol.http.server.RequestHandler;

public class RssRouter implements RequestHandler<ByteBuf, ByteBuf> {

	private final SimpleUriRouter<ByteBuf, ByteBuf> delegate;

	private static final Logger log = LoggerFactory.getLogger(RssRouter.class);
	
	@Inject
	public RssRouter(RSSManager rssManager) {
		delegate = new SimpleUriRouter<ByteBuf, ByteBuf>();
		
		/*
		 * Deleagate for reading subscriptions from store
		 */
		delegate.addUriRegex(".middletier.rss.user.*$", (request, response) -> {
			log.info("Request Started: " + Thread.currentThread().getName());
			if (request.getHttpMethod() == HttpMethod.GET) {
				String user = StringUtils.substringAfterLast(request.getPath(), "/");
					/*
					 * this is assumed to be a long lasting I/O. If it is not subscribed on the Schedulers.io(), it will block
					 * the only thread of the server the time of the IO operation.  
					 */
					Observable<List<String>> subscriptions = rssManager.getSubscriptions(user); 
					log.info("Request Done: " + Thread.currentThread().getName());

					return Observable.<Void>create((OnSubscribe<Void>) subscriber -> {
						subscriptions.subscribe(urls -> {
							log.info("Request Writing response: " + Thread.currentThread().getName());
							Gson gson = new Gson();
							response.writeStringAndFlush(gson.toJson(urls));
						}, Throwable::printStackTrace, () -> {
							response.close(true);
							subscriber.onCompleted();
						});						
					});
					
			}
			return Observable.empty();
		});
		
		/*
		 * A very fast delegate to test if the server can server other requests, while busy with heavy IO request 
		 */
		delegate.addUriRegex(".middletier.rss.hello.*$", (request, response) -> {
			return Observable.<Void>create((OnSubscribe<Void>) subscriber -> {
				response.writeStringAndFlush("Hello World");
				subscriber.onCompleted();
			});

		});
	}

	@Override
	public Observable<Void> handle(HttpServerRequest<ByteBuf> request,
	    HttpServerResponse<ByteBuf> response) {
		return delegate.handle(request, response);
	}

}

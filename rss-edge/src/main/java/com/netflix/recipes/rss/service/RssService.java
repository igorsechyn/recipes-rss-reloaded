package com.netflix.recipes.rss.service;

import io.netty.buffer.ByteBuf;

import com.netflix.ribbon.RibbonRequest;
import com.netflix.ribbon.proxy.annotation.ClientProperties;
import com.netflix.ribbon.proxy.annotation.Http;
import com.netflix.ribbon.proxy.annotation.Http.HttpMethod;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import com.netflix.ribbon.proxy.annotation.TemplateName;
import com.netflix.ribbon.proxy.annotation.Var;

@ClientProperties(properties = {
		@ClientProperties.Property(name="ReadTimeout", value="2000"),
		@ClientProperties.Property(name="ConnectTimeout", value="1000"),
		@ClientProperties.Property(name="MaxAutoRetriesNextServer", value="2")
}, exportToArchaius = true)
public interface RssService {

    @TemplateName("rssByUserId")
    @Http(
            method = HttpMethod.GET,
            uri = "/middletier/rss/user/{user}")
    @Hystrix(fallbackHandler = RssReadFallbackHandler.class)
    RibbonRequest<ByteBuf> rssByUserId(@Var("user") String userId);

    @TemplateName("subscribe")
    @Http(
            method = HttpMethod.POST,
            uri = "/middletier/rss/user/{user}?url={url}")
    RibbonRequest<ByteBuf> subscribe(@Var("user") String userId, @Var("url") String url);

	
}

package com.netflix.recipes.rss.server;

import io.netty.buffer.ByteBuf;
import com.netflix.karyon.transport.http.KaryonHttpModule;

public class SimpleRouterModuleImpl extends KaryonHttpModule<ByteBuf, ByteBuf> {

	public SimpleRouterModuleImpl() {
		super("httpServerA", ByteBuf.class, ByteBuf.class);
	}

	@Override
	protected void configureServer() {
		bindRouter().to(RssRouter.class);
		server().port(8888).threadPoolSize(1);
	}
}
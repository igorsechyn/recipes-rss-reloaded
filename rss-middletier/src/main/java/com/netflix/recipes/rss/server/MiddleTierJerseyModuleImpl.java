package com.netflix.recipes.rss.server;

import com.netflix.karyon.jersey.blocking.KaryonJerseyModule;
import com.netflix.recipes.rss.manager.RSSManager;

public class MiddleTierJerseyModuleImpl extends KaryonJerseyModule {
    @Override
    protected void configureServer() {
        bind(RSSManager.class).asEagerSingleton();
        server().port(8888).threadPoolSize(100);
    }
}
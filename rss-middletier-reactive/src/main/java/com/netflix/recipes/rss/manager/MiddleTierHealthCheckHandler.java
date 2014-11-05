package com.netflix.recipes.rss.manager;

import com.google.inject.Inject;
import com.netflix.karyon.health.HealthCheckHandler;


public class MiddleTierHealthCheckHandler implements HealthCheckHandler {
	private RSSManager rssManager;
	
	@Inject
	public MiddleTierHealthCheckHandler(RSSManager rssManager) {
		this.rssManager = rssManager;
	}
	
    public int getStatus() {
        return rssManager.getStatus();
    }

}

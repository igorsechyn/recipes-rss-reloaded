package com.netflix.recipes.rss.manager;

import com.netflix.karyon.health.HealthCheckHandler;


public class MiddleTierHealthCheckHandler implements HealthCheckHandler {

    public int getStatus() {
        return 200;
    }

}

package com.netflix.recipes.rss.server;

import com.netflix.adminresources.resources.KaryonWebAdminModule;
import com.netflix.governator.annotations.Modules;
import com.netflix.karyon.KaryonBootstrap;
import com.netflix.karyon.archaius.ArchaiusBootstrap;
import com.netflix.recipes.rss.manager.MiddleTierHealthCheckHandler;

@ArchaiusBootstrap
@KaryonBootstrap(name = "middletier", healthcheck = MiddleTierHealthCheckHandler.class)
@Modules(include = {
        //ShutdownModule.class, TODO: configure a different port than the standard one
        KaryonWebAdminModule.class,
        //KaryonServoModule.class,
        // KaryonEurekaModule.class, // Uncomment this to enable Eureka client.
        SimpleRouterModuleImpl.class
})
public interface MiddletierSimpleServer {

}



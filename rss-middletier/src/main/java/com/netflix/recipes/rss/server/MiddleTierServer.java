/*
 * Copyright 2012 Netflix, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.netflix.recipes.rss.server;

import com.netflix.adminresources.resources.KaryonWebAdminModule;
import com.netflix.governator.annotations.Modules;
import com.netflix.karyon.KaryonBootstrap;
import com.netflix.karyon.ShutdownModule;
import com.netflix.karyon.archaius.ArchaiusBootstrap;
import com.netflix.recipes.rss.manager.MiddleTierHealthCheckHandler;

/**
 * @author Chris Fregly (chris@fregly.com)
 */
@ArchaiusBootstrap
@KaryonBootstrap(name = "middletier", healthcheck = MiddleTierHealthCheckHandler.class)
@Modules(include = {
        ShutdownModule.class,
        KaryonWebAdminModule.class,
        //KaryonServoModule.class,
        // KaryonEurekaModule.class, // Uncomment this to enable Eureka client.
        MiddleTierJerseyModuleImpl.class
})
public interface MiddleTierServer {
    	
}



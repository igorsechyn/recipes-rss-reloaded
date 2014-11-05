package com.netflix.recipes.rss.server;

import com.netflix.governator.guice.LifecycleInjectorBuilderSuite;
import com.netflix.karyon.Karyon;
/**
 * Starts a rx netty server with the jersey router on port 8888
 * @author isechyn
 *
 */
public class JerseyServerRunner {

	public static void main(String[] args) {
		Karyon.forApplication(MiddleTierServer.class, (LifecycleInjectorBuilderSuite[]) null).startAndWaitTillShutdown();;
	}
}

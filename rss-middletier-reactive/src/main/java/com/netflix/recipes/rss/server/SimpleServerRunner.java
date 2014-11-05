package com.netflix.recipes.rss.server;

import com.netflix.governator.guice.LifecycleInjectorBuilderSuite;
import com.netflix.karyon.Karyon;

public class SimpleServerRunner {

	public static void main(String[] args) {
		Karyon.forApplication(MiddletierSimpleServer.class, (LifecycleInjectorBuilderSuite[]) null).startAndWaitTillShutdown();;
	}

}

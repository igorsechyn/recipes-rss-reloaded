package com.netflix.recipes.rss.server;

import com.netflix.governator.guice.LifecycleInjectorBuilderSuite;
import com.netflix.karyon.Karyon;
public class AppRunner {

	public static void main(String[] args) {
		Karyon.forApplication(MiddleTierServer.class, (LifecycleInjectorBuilderSuite[]) null).startAndWaitTillShutdown();;
	}
}

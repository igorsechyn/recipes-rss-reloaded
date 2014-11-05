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
package com.netflix.recipes.rss.manager;

import java.util.List;

import com.netflix.config.DynamicPropertyFactory;
import com.netflix.governator.annotations.AutoBindSingleton;
import com.netflix.governator.annotations.Configuration;
import com.netflix.karyon.health.HealthCheckHandler;
import com.netflix.recipes.rss.RSSConstants;
import com.netflix.recipes.rss.RSSStore;
import com.netflix.recipes.rss.SubscriptionURLs;
import com.netflix.recipes.rss.impl.CassandraStoreImpl;
import com.netflix.recipes.rss.impl.InMemoryStoreImpl;
import com.netflix.recipes.rss.impl.SubscriptionURLsImpl;

/**
 * RSS Manager that
 *  1) Fetches content from RSS feeds using Ribbon
 *  2) Parses RSS feeds
 *  3) Persists feed urls into
 *      a) Cassandra using Astyanax (or)
 *      b) InMemoryStore
 */
@AutoBindSingleton
public class RSSManager implements HealthCheckHandler {
	@Configuration(value="rss.store")
    private String storeType = RSSConstants.RSS_STORE_CASSANDRA;

    private RSSStore store;
    
    public RSSManager() {
        if (RSSConstants.RSS_STORE_CASSANDRA.equals(
                DynamicPropertyFactory.getInstance().getStringProperty(RSSConstants.RSS_STORE, RSSConstants.RSS_STORE_CASSANDRA).get())) {
            store = new CassandraStoreImpl();
        } else {
            store = new InMemoryStoreImpl();
        }
    }

    /**
     * Fetches the User subscriptions urls
     */
    public SubscriptionURLs getSubscriptions(String userId) throws Exception {
        List<String> feedUrls = store.getSubscribedUrls(userId);
        return new SubscriptionURLsImpl(userId, feedUrls);
    }

    /**
     * Add subscription
     */
    public void addSubscription(String user, String decodedUrl) throws Exception {
        if (decodedUrl == null) throw new IllegalArgumentException("url cannot be null");
        store.subscribeUrl(user, decodedUrl);
    }

    /**
     * Delete subscription
     */
    public void deleteSubscription(String user, String decodedUrl) throws Exception {
        if (decodedUrl == null) throw new IllegalArgumentException("url cannot be null");
        store.unsubscribeUrl(user, decodedUrl);
    }

    public int getStatus() {
        return store == null ? 500 : 200;
    }
}
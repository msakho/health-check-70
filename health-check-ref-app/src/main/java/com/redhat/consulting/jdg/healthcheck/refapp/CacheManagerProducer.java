/**
 * @author Meissa
 */
package com.redhat.consulting.jdg.healthcheck.refapp;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

/**
 * This class can be used to create an EmbeddedCacheManger via a @Producer Method.
 * 
 * @author Meissa
 */
public class CacheManagerProducer {
	
	private static final Logger LOGGER = Logger.getLogger(CacheManagerProducer.class.getName());
	private static final long ENTRY_LIFESPAN = 60 * 5 * 1000; // 5 minutes
	
	private EmbeddedCacheManager cacheManager;
	
	/**
	 * The @ApplicationScoped annotation instructs that only one instance of The produced CacheManger will be used.
	 * @return EmbeddedCacheManager
	 */
	@Produces
	@ApplicationScoped
	public EmbeddedCacheManager getCacheManager() {
		LOGGER.info("**************CACHE MANAGER INITIALIZATION ***********************\n\n");
		if (cacheManager == null) {
			LOGGER.info("\n\n DefaultCacheManager does not exist - constructing a new one\n\n");

			GlobalConfiguration glob = new GlobalConfigurationBuilder().clusteredDefault()
					.transport().addProperty("configurationFile", "jgroups-udp.xml")
					.globalJmxStatistics().allowDuplicateDomains(true).enable() 
					.build(); // Builds the GlobalConfiguration object
			Configuration loc = new ConfigurationBuilder().jmxStatistics().enable()
					.clustering().cacheMode(CacheMode.DIST_SYNC) 
					.hash().numOwners(2).groups()/* .addGrouper(new CustomGrouper()) */.enabled() // Keeps two copies of  each key/value  pair
					.expiration().lifespan(ENTRY_LIFESPAN) 
					.build();
			cacheManager = new DefaultCacheManager(glob, loc, true);
			cacheManager.defineConfiguration("my-distributed", loc);
			cacheManager.defineConfiguration("two-my-distributed", loc);
			cacheManager.defineConfiguration("three-my-distributed", loc);
			
			Configuration simple = new ConfigurationBuilder()
		            .eviction().strategy(EvictionStrategy.LRU).maxEntries(4)
		            .build();
			cacheManager.defineConfiguration("simple", simple);
			
		}
		return cacheManager;
	}
	
	
}

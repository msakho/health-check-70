package com.redhat.consulting.jdg.healthcheck.test;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

@ApplicationScoped
public class CacheManagerTest {

	   private static final long ENTRY_LIFESPAN = 60 * 5 * 1000; // 5 minutes
	   
	   private EmbeddedCacheManager manager;

	   public EmbeddedCacheManager getManager() {
		return manager;
	}

	public EmbeddedCacheManager getCacheManager() {
		   if (manager == null) {

	            GlobalConfiguration glob = new GlobalConfigurationBuilder().clusteredDefault() 
	                    .transport().addProperty("configurationFile", "jgroups-udp.xml")
	                    .globalJmxStatistics().allowDuplicateDomains(true).enable() 
	                    .build(); 
	            
	            Configuration loc = new ConfigurationBuilder().jmxStatistics().enable() 
	                    .clustering().cacheMode(CacheMode.DIST_SYNC) // Set Cache mode to DISTRIBUTED with SYNCHRONOUS replication
	                    .hash().numOwners(2).groups()/*.addGrouper(new CustomGrouper())*/.enabled() // Keeps two copies of each key/value pair
	                    .expiration().lifespan(ENTRY_LIFESPAN) 
	                    .build();
	            manager = new DefaultCacheManager(glob, loc, true);
	            manager.defineConfiguration("my-distributed", loc);
	            manager.defineConfiguration("two-my-distributed", loc);
	            manager.defineConfiguration("three-my-distributed", loc);
	        }
	        return manager;
	   }

	   @PreDestroy
	   public void cleanUp() {
	      manager.stop();
	      manager = null;
	   }
}

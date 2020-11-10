package com.redhat.consulting.jdg.healthcheck.refapp;

import java.util.concurrent.TimeUnit;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

import com.redhat.consulting.jdg.healthcheck.configuration.HealthCheckConfiguration;
import com.redhat.consulting.jdg.healthcheck.task.HealthCheckTask;

/**
 * @author Meissa
 */
@Singleton
//@Startup
public class HealthCheckStartup {
	private static final Logger LOGGER = Logger.getLogger(HealthCheckStartup.class.getName());
	private static final long ENTRY_LIFESPAN = 60 * 5 * 1000; // 5 minutes
	
	@Resource
    private ManagedScheduledExecutorService scheduledExecutorService;
    @Inject
	private EmbeddedCacheManager cacheManager;
	
	//@PostConstruct
	public void start() {
		LOGGER.info("**************HEALTH CHECK INITIALIZATION ***********************\n\n");
		//cacheManager = getCacheManager();
		long initialDelay = HealthCheckConfiguration.TIME_INTERVAL;
        try {
        	if(HealthCheckConfiguration.IS_ACTIVE) {
	        	for(String cacheName : HealthCheckConfiguration.CACHES_TO_CHECK) {
	        		
	        		if(cacheManager.getCacheNames().contains(cacheName)) {
	        			
	        			LOGGER.log(Level.INFO, String.format("%s cache found, scheduling %s seconds. Initial delay %s", cacheName, HealthCheckConfiguration.TIME_INTERVAL, initialDelay));
	        			
		        		//TODO: a String is used for the key type
		        		scheduledExecutorService.scheduleWithFixedDelay(new HealthCheckTask<String>(cacheManager, cacheName, HealthCheckConfiguration.CACHE_KEY_NAME), initialDelay, HealthCheckConfiguration.TIME_INTERVAL, TimeUnit.SECONDS);
		        		initialDelay += HealthCheckConfiguration.TIME_DELAY;
	        		} else {
	        			LOGGER.log(Level.WARNING, String.format("%s cache NOT found. Nothing to schedule.", cacheName));
	        		}
	        	}
        	} else {
        		LOGGER.log(Level.WARNING, "JDG Health Check not active on this node. Nothing to schedule.");
        	}
        	
        } catch (Exception e) {
          LOGGER.log(Level.SEVERE, "Unexpected JDG Health Check scheduling error!", e);
        }   
	}
	
	@PreDestroy
	public void cleanup() {
		LOGGER.log(Level.INFO, "JDG Health Check shutdown invoked.");
		scheduledExecutorService.shutdownNow();
		cacheManager.stop();
		
		
	}
	
	
	
	
	

}

/**
 * @author Meissa
 */
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
import javax.inject.Inject;

import org.infinispan.manager.EmbeddedCacheManager;

import com.redhat.consulting.jdg.healthcheck.configuration.HealthCheckConfiguration;
import com.redhat.consulting.jdg.healthcheck.task.HealthCheckTask;

/**
 * @author Meissa
 */
@Singleton
@Startup
public class CustomHealthCheckStartup {
	private static final Logger LOGGER = Logger.getLogger(CustomHealthCheckStartup.class.getName());
	private static final long ENTRY_LIFESPAN = 60 * 5 * 1000; // 5 minutes
	
	@Resource
    private ManagedScheduledExecutorService scheduledExecutorService;
    @Inject
	private EmbeddedCacheManager cacheManager;
    
    @PostConstruct
    public void start() {
		LOGGER.info("**************HEALTH CHECK INITIALIZATION ***********************\n\n");
		
		long initialDelay = HealthCheckConfiguration.TIME_INTERVAL;
		CustomKey key=new CustomKey(HealthCheckConfiguration.CACHE_KEY_NAME);
        try {
        	if(HealthCheckConfiguration.IS_ACTIVE) {
	        	for(String cacheName : HealthCheckConfiguration.CACHES_TO_CHECK) {
	        		
	        		if(cacheManager.getCacheNames().contains(cacheName)) {
	        			
	        			LOGGER.log(Level.INFO, String.format("%s cache found, scheduling %s seconds. Initial delay %s", cacheName, HealthCheckConfiguration.TIME_INTERVAL, initialDelay));
	        			
		        		//TODO: a CustomKey is used for the key type
	        			HealthCheckTask<CustomKey> healthCheckTask = new HealthCheckTask<CustomKey>(cacheManager,cacheName,key);
	        			
	        			scheduledExecutorService.scheduleWithFixedDelay(healthCheckTask, initialDelay, HealthCheckConfiguration.TIME_INTERVAL, TimeUnit.SECONDS);
		        		
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

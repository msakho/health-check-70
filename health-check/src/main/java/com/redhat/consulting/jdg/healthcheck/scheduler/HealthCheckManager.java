package com.redhat.consulting.jdg.healthcheck.scheduler;

import java.util.concurrent.TimeUnit;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.infinispan.manager.EmbeddedCacheManager;

import com.redhat.consulting.jdg.healthcheck.configuration.HealthCheckConfiguration;
import com.redhat.consulting.jdg.healthcheck.task.HealthCheckTask;
@Deprecated
@ApplicationScoped
public class HealthCheckManager {
	
	private static Logger LOGGER = Logger.getLogger(HealthCheckManager.class.getName());

    @Resource
    private ManagedScheduledExecutorService scheduledExecutorService;
    
    @Inject
    private EmbeddedCacheManager cacheManager;

//    @PostConstruct
    public void start() {
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
	public void shutdown() {
		LOGGER.log(Level.INFO, "JDG Health Check shutdown invoked.");
		scheduledExecutorService.shutdownNow();
	}
	
}

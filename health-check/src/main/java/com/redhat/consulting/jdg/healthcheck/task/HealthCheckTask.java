package com.redhat.consulting.jdg.healthcheck.task;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.infinispan.manager.EmbeddedCacheManager;

import com.redhat.consulting.jdg.healthcheck.configuration.HealthCheckConfiguration;
import com.redhat.consulting.jdg.healthcheck.exception.HealthCheckFailure;

public class HealthCheckTask<K> implements Runnable {

	private static Logger LOGGER = Logger.getLogger(HealthCheckTask.class.getName());

	private EmbeddedCacheManager cacheManager;
	private String cachename;
	private K key;
//	private V value;

	private HeatlhCheck<K, String> hc = new HeatlhCheck<>();

	private int failureCount;

	@Override
	public void run() {

		try {
		    
			//TODO: a string is used for the value type
			String value = String.valueOf( (System.currentTimeMillis() % 10000) );

			cacheManager.getCache(cachename).put(key, value);
			hc.healthCheck(cacheManager, cachename, key, value);
			
			
			resetFailureCount();
		} catch (HealthCheckFailure e) {
			failureCount++;
			LOGGER.log(Level.WARNING, String.format("%s Health check failed! Current failures for this cache: %s", cachename, failureCount), e);
		}

		if (failureCount >= HealthCheckConfiguration.MAX_FAILURE_NUMBER) {
			LOGGER.log(Level.SEVERE, String.format("%s FAILED HEALTH CHECK REACHED THE MAXIMUM NUMBER FAILURE AND FAILED MULTIPLE TIME: %s", cachename, failureCount));
		}

	}

	public HealthCheckTask(EmbeddedCacheManager cacheManager, String cachename, K key) {
		super();
		this.cacheManager = cacheManager;
		this.cachename = cachename;
		this.key = key;
//		this.value = value;
	}

	public void resetFailureCount() {
		this.failureCount = 0;
	}

	public int getFailureCount() {
		return this.failureCount;
	}

}

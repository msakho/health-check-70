package com.redhat.consulting.jdg.healthcheck.configuration;

public class HealthCheckConfiguration {

	/**
	 * Comma separated list of the caches to check
	 */
	public static final String[] CACHES_TO_CHECK = System.getProperty("jdg.healthcheck.caches.to.check", "").split(",");
	
	/**
	 * time interval between a check and another
	 */
	public static final long TIME_INTERVAL = Long.parseLong( System.getProperty("jdg.healthcheck.time.interval", "25") );
	
	/**
	 * time added to initial delay for each cache.
	 */
	public static final long TIME_DELAY = Long.parseLong( System.getProperty("jdg.healthcheck.time.delay", "5") );
	
	/**
	 * when reached a SEVERE message log will be written
	 */
	public static final int MAX_FAILURE_NUMBER = Integer.parseInt( System.getProperty("jdg.healthcheck.max.failures", "3") );
	
	
	public static final String CACHE_KEY_NAME = System.getProperty("jdg.healthcheck.cache.key","jdg.healthcheck.cache.key.default");

	/**
	 * Whether to start health check on this node
	 */
	public static final Boolean IS_ACTIVE = Boolean.parseBoolean( System.getProperty("jdg.healthcheck.active","true" ));
	
	/**
	 * Debug purpose only Do not enable in production
	 */
	public static final Boolean REMOVE_KEY_ON_CHECK = Boolean.parseBoolean( System.getProperty("jdg.healthcheck.remove.key.on.check","false" ));
	public static final Boolean FAILURE_ENABLED = Boolean.parseBoolean( System.getProperty("jdg.healthcheck.failure.enabled","false" ));
	public static final String UNKNOWN_KEY = System.getProperty("jdg.healthcheck.unknow.key","UNKNOWN");
}

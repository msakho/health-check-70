/**
 * @author Meissa
 */
package com.redhat.consulting.jdg.healthcheck.refapp;



import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.VersioningScheme;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.server.hotrod.HotRodServer;
import org.infinispan.server.hotrod.configuration.HotRodServerConfiguration;
import org.infinispan.server.hotrod.configuration.HotRodServerConfigurationBuilder;



/**
 * 
 * This Startup class will configure a cache Manager and expose a HotRod Server so that the defined cahces can be migrated .
 */
@Singleton
@Startup
public class HotRodConfigStartup {
	public static final Logger LOGGER = LogManager.getLogger(HotRodConfigStartup.class.getName());
	
	
	private EmbeddedCacheManager manager;
	
	private
	HotRodServer server = null;
	
	@PostConstruct
	/**
	 * This method is called at application startup. It purpose is:
	 * 1) Initilize the Cache Manager,
	 * 2) Enable the compatibility mode,
	 * 3) Expose the HotRod server.
	 */
	public void init()
	{
		
		System.setProperty("infinispan.deserialization.whitelist.regexps", ".*");
		manager = new DefaultCacheManager();
		Configuration builder = new ConfigurationBuilder()
				.compatibility().enable()
				//.versioning().enable().scheme(VersioningScheme.SIMPLE)	
	           .build();
		manager.defineConfiguration("firstToMigrate", builder);
		manager.defineConfiguration("secondToMigrate", builder);
		manager.defineConfiguration("thirdToMigrate", builder);
		
		LOGGER.info("INITIALIZE CACHES TO MIGRATE");
		Cache<String,String> firstToMigrate = manager.getCache("firstToMigrate");
		for (int i = 0; i < 10; i++) {
			firstToMigrate.put("k" + i, "v" + i);
         }
		
		Cache<String,String> secondToMigrate = manager.getCache("secondToMigrate");
		for (int i = 0; i < 20; i++) {
			secondToMigrate.put("k" + i, "v" + i);
         }
		
		Cache<String,String> thirdToMigrate = manager.getCache("thirdToMigrate");
		for (int i = 0; i < 40; i++) {
			thirdToMigrate.put("k" + i, "v" + i);
         }
			
				
		LOGGER.info("********************** START EXPOSING HOT ROD***********************");
		HotRodServerConfiguration hotRodServerConfiguration = new HotRodServerConfigurationBuilder()
	            .host(MigrationConfig.HOTROD_BIND_ADRESSS)
	            .port(MigrationConfig.HOTROD_LISTEN_PORT)
	            .build();
	      server = new HotRodServer();
	      server.start(hotRodServerConfiguration, manager);
	      LOGGER.info("Listening on hotrod://{}:{}", hotRodServerConfiguration.host(), hotRodServerConfiguration.port());
	      
	     
	}
	
	
	
	@PreDestroy
	public void cleanup()
	{
		server.stop();
        manager.stop();
		
	}

}

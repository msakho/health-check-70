package com.redhat.consulting.jdg.healthcheck.refapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.infinispan.manager.EmbeddedCacheManager;


@Named
@ApplicationScoped
public class DistributedCacheController {

	private static final Logger LOGGER = Logger.getLogger(DistributedCacheController.class.getName());

	@Inject
	private EmbeddedCacheManager manager;

	


	public String[] getCachedValues() {

		List<String> result = new ArrayList<>();

		for (String cacheName : manager.getCacheNames() ) {
			
			for (Entry<Object, Object> entry :manager.getCache(cacheName).entrySet()) {
				result.add("cache " + cacheName + " key:" + entry.getKey().toString() + " value: "
						+ entry.getValue().toString());
			}
		}
		return result.toArray(new String[result.size()]);
	}
	
	public String[] getCacheNames() {
		return manager.getCacheNames().toArray(new String[manager.getCacheNames().size()]);
	}
	
	public int getNumberOfEntries() {
		int result = 0;
		for (String cacheName : manager.getCacheNames() ) {
			result += manager.getCache(cacheName).size();
		}
							
        return result;
    }
	
	public void clearCache() {
		for (String cacheName : manager.getCacheNames() ) {
			manager.getCache(cacheName).clear();
		}
    }

	@PostConstruct
	public void init() {
		
	}
}

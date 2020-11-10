package com.redhat.consulting.jdg.healthcheck.task;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.infinispan.context.Flag;
//import org.infinispan.distribution.DistributionInfo;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.remoting.transport.Address;

import com.redhat.consulting.jdg.healthcheck.configuration.HealthCheckConfiguration;
import com.redhat.consulting.jdg.healthcheck.exception.HealthCheckFailure;

public class HeatlhCheck<K, V> {

	private static Logger LOGGER = Logger.getLogger(HeatlhCheck.class.getName());

	/**
	 * Verify that 
	 * - given key is present on each owner
	 * - the value is the same on each owner
	 * - the value is the given one
	 * 
	 * @param <K>
	 * @param <V>
	 * @param cacheManager
	 * @param cachename
	 * @param key
	 * @throws HealthCheckFailure
	 */
	public void healthCheck(EmbeddedCacheManager cacheManager, String cachename, K key, V expectedValue)
			throws HealthCheckFailure {

		LOGGER.log(Level.FINER, String.format("%s cache and %s cacheManager check started", cachename, cacheManager));
		LOGGER.log(Level.FINER, String.format("%s CACHE KEY USED", key));

		AtomicReference<Throwable> throwable = new AtomicReference<>();
		
		if( cacheManager.getCache(cachename) == null) {
			LOGGER.log(Level.WARNING, String.format("%s cache does not exists, skipping", cachename));
			return;
		}

		else if (cacheManager.getCache(cachename).getAdvancedCache().getDistributionManager() == null) {
			LOGGER.log(Level.WARNING, String.format("%s cache is not distributed, skipping", cachename));
			return;
		}

		List<Address> owningNodes = cacheManager.getCache(cachename).getAdvancedCache().getDistributionManager().locate(key);
//			List<Address> owningNodes = cacheManager.getCache(cachename).getAdvancedCache().getComponentRegistry().getStateTransferManager().getCacheTopology().getWriteConsistentHash().locateOwners(key);

		ConcurrentMap<Address, V> results = new ConcurrentHashMap<>(owningNodes.size());
		
		@SuppressWarnings("unchecked")
		CompletableFuture<Void> exec = cacheManager.executor().filterTargets(owningNodes).submitConsumer(
				m -> m.getCache(cachename).getAdvancedCache().withFlags(Flag.CACHE_MODE_LOCAL).get(key),
				(address, result, t) -> {
					if (t == null) {
						results.put(address, (V) result);
					} else {
						throwable.set(t);
					}
				});
		try {
			exec.get(1, TimeUnit.MINUTES);
		} catch (Exception e) {
			throw new HealthCheckFailure(e);
		}finally {
			try {
				if (HealthCheckConfiguration.REMOVE_KEY_ON_CHECK) {
					cacheManager.getCache(cachename).getAdvancedCache().withFlags(Flag.IGNORE_RETURN_VALUES).removeAsync(key);
				}
			}catch(Exception e ) {e.printStackTrace();}
		}

		LOGGER.log(Level.FINER, String.format("%s cache check. Result size: %s owningNodes.size: %s", cachename, results.size(), owningNodes.size()));
		if (results.size() > 0) {
			for (Entry<Address, V> result : results.entrySet()) {
				LOGGER.log(Level.FINER, String.format("%s results: address %s value %s , expected %s", cachename, result.getKey(), result.getValue(), expectedValue.toString()));
				
			}
		}

		if (throwable.get() != null) {
			// FAIL HEALTHCHECK: Error happened during execution
			throw new HealthCheckFailure("FAIL HEALTHCHECK: Generic error happened during execution");
		} else if (results.size() != owningNodes.size()) {
			// FAIL HEALTHCHECK: Unexpected result count
			throw new HealthCheckFailure("FAIL HEALTHCHECK: Unexpected result count");
		} else {
			Iterator<V> iterator = results.values().iterator();
			V first = iterator.next();
			while (iterator.hasNext()) {
				V nextValue = iterator.next();
				if (!first.equals(nextValue)) {
					// FAIL HEALTHCHECK: Value mismatch
					throw new HealthCheckFailure("FAIL HEALTHCHECK: Value mismatch: consistency across cluster");
				} else if (!nextValue.equals(nextValue)) {
					// FAIL HEALTHCHECK: Value mismatch, not 
					throw new HealthCheckFailure("FAIL HEALTHCHECK: Value mismatch: not the expected value");
				}
			}
		}
		
		LOGGER.log(Level.FINE, String.format("%s cache check done", cachename));
	}
}

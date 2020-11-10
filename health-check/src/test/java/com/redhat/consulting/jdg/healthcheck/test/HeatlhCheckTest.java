package com.redhat.consulting.jdg.healthcheck.test;

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
//import org.jboss.arquillian.container.test.api.Deployment;
//import org.jboss.arquillian.junit.Arquillian;
//import org.jboss.shrinkwrap.api.spec.WebArchive;
//import org.junit.Test;
//import org.junit.runner.RunWith;

import com.redhat.consulting.jdg.healthcheck.scheduler.HealthCheckManager;

//@RunWith(Arquillian.class)
public class HeatlhCheckTest {

//	@Deployment
//    public static WebArchive deployment() {
//        return Deployments.baseDeployment()
//        		.addClass(HeatlhCheckTest.class);
////                .addClass(com.redhat.consulting.jdg.healthcheck.HealthCheck<String,String>.class);
//    }


    
    
    @Inject
    private HealthCheckManager healthCheckManager;

}

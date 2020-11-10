package com.redhat.consulting.jdg.healthcheck.test;

import java.io.File;

//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.spec.WebArchive;

import com.redhat.consulting.jdg.healthcheck.configuration.HealthCheckConfiguration;
import com.redhat.consulting.jdg.healthcheck.exception.HealthCheckFailure;
import com.redhat.consulting.jdg.healthcheck.scheduler.HealthCheckManager;
import com.redhat.consulting.jdg.healthcheck.task.HealthCheckTask;

public class Deployments {

	// Disable instantiation
    private Deployments() {
    }

//    public static WebArchive baseDeployment() {
//        return ShrinkWrap.create(WebArchive.class)
//        		.addClass(CacheManagerTest.class)
//        		.addPackage("com.redhat.consulting.jdg.healthcheck")
//            .addPackage(HealthCheckTask.class.getPackage())
//            .addPackage(HealthCheckManager.class.getPackage())
//            .addPackage(HealthCheckConfiguration.class.getPackage())
//            .addPackage(HealthCheckFailure.class.getPackage())
////            .addAsWebInfResource(new File("src/main/webapp/WEB-INF/beans.xml"), "beans.xml")
//            .addAsLibraries(new File("target/test-libs/infinispan-embedded.jar"),
//            				new File("target/test-libs/infinispan-core.jar"),
//                            new File("target/test-libs/cache-api.jar"),
////                            new File("target/test-libs/jboss-marshalling.jar"),
////                            new File("target/test-libs/jboss-marshalling-river.jar"),
//                            new File("target/test-libs/cdi-api.jar")
//            );
//    }
}

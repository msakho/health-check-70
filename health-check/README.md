JBoss JDG: health check 
=====================================
Target Product: JDG
Product Versions: JDG 7.0.0, EAP 7.x


What is it?
-----------

Implementation of a reliable health check for cache status, including application level checks.

A thread is scheduled for each configured cache to check via EAP 7.x ManagedScheduledExecutorService

That verify
  - the given key is present on each owner
  - the value is the same on each owner
  - the value is the given one
	 

Configuration via System Properties
-------------------
	
 * jdg.healthcheck.caches.to.check 
   Comma separated list of the caches to check
	
 * jdg.healthcheck.time.interval
   time iterval between a check and another; default to 25 seconds 
	
 * jdg.healthcheck.time.delay
   time added to initial delay for each cache; default to 5 seconds
	
 * jdg.healthcheck.max.failures
   when reached a SEVERE message log will be written
   
 * jdg.healthcheck.chache.key
   default jdg.healthcheck.chache.key.default
   
 * jdg.healthcheck.active
   Wheter to start healt check on this node, default false
 
 * jdg.healthcheck.remove.key.on.check
   Debug purpose


System requirements
-------------------

All you need to build this project is Java 8.0 (Java SDK 1.8) or better, Maven 3.0 or better.

The articat this project produces is designed to be bundled on an application that run on Red Hat JBoss Enterprise Application Platform 7.0 or later.


Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.


Build and Deploy the Application in Library Mode
------------------------------------------------

The reference application for testing purpose is health-check-ref-app


Debug
------------------------------------------------
for complete debug tracing configure the
com.redhat.consulting.jdg.healthcheck
category to level TRACE
CLI command:
/subsystem=logging/logger=com.redhat.consulting.jdg.healthcheck:add(level=TRACE)


Test the Application using Arquillian and remote EAP instance
-----------------------------------------------------------------

TODO: TEST

If you would like to test the application, there are a couple of unit tests designed to run on a remote EAP/Wildfly instance.

In order to run those test, please do the following steps:

1. Start EAP/Wildfly
2. Build the quickstart using:

        mvn clean test -Peap-remote

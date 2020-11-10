# health-check-70

JBoss JDG: health check 
=====================================
Target Product: JDG
Product Versions: JDG 7.0.0, EAP 7.x


What is it?
-----------

Implementation of a reliable health check for application level checks.

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
The health-check is built as a library thatis is embedded in the health-check-ref-app as a library




Debug
------------------------------------------------
for complete debug tracing configure the
com.redhat.consulting.jdg.healthcheck
category to level TRACE
CLI command:
/subsystem=logging/logger=com.redhat.consulting.jdg.healthcheck:add(level=TRACE)


Test the Application on a EAP instance
-----------------------------------------------------------------

To test the application, the following instruction should be executed
1. Build the artifact with the following command:
mvn clean install
This command will build the health-check-ref-app.war with the healthcheck.library in it's in the WEB-INF/lib folder.
2. Start EAP/Wildfly with the following command
./standalone.sh -P /usr/local/dev/eap/jboss-eap-7.3/standalone/configuration/check-config.properties
This will load the check-config.properties file with the properrties defined as system properties. It's also possible to add them as command lines.
3. Deploy the healthcheck-ref-app
cd health-check-ref-app
mvn wildfly:deploy 
You can also just copy the healthcheck-ref-app in the EAP deployment folder


        

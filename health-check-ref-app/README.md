JBoss JDG: health check - application reference
=====================================
Target Product: JDG
Product Versions: JDG 7.0.0, EAP 7.x

What is it?
-----------

This reference application demontrates usage of JDG-health check artifact and RollingUpgrade Migration

System requirements
-------------------

All you need to build this project is Java 8.0 (Java SDK 1.8) or better, Maven 3.0 or better.

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7.0 or later.

Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md#configure-maven-to-build-and-deploy-the-quickstarts) before testing the quickstarts.

Start EAP
---------

1. Open a command line and navigate to the root of the EAP server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   $JBOSS_HOME/bin/standalone.sh
        For Windows: %JBOSS_HOME%\bin\standalone.bat

Build and Deploy the Application in Library Mode
------------------------------------------------

1. Make sure you have started EAP as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy

4. This will deploy `target/health-check-ref-app.war` to the running instance of the server.

Expose the HotRod Server for future Migration
---------------------



Undeploy the Archive
--------------------

1. Make sure you have started EAP as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy

Test the Application using Arquillian and remote EAP instance
-----------------------------------------------------------------

TODO: TEST

If you would like to test the application, there are a couple of unit tests designed to run on a remote EAP/Wildfly instance.

In order to run those test, please do the following steps:

1. Start EAP/Wildfly
2. Build the quickstart using:

        mvn clean test -Peap-remote

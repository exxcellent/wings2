This directory contains several simple demos on how to use wingS:

 - wingset:  A demo similar to SwingSet demonstrating the wingS
             components and capibilities.
 - desktop:  Another eye-candy demo which simulates a desktop
             with inner windows that can be opened/closed/minimized
             maximized.
 - jsp:      Conceptual demo to demonstrate integration of wingS
             into JSP technology.
 - dwr:      Conceptual demo to show the frame of DWR integration.
             Note that this demo has been already integrated into the
             wingset demo, too.
 - frameset: An experimental frameset support demo.

To build & deploy these demos you'll need a properly configured
JDK and Apache ant environment.

All demos provide a "war" build task and a "deploy" build task
which can be triggered either via
   ant war
or
   ant deploy
on the command prompt, if you included ant into your PATH.

FOR ONE-TIME TRIES:
The "war" target creates a war file in the build directory.
This <demoname>.war file can be copied to i.e. the webapps dir of a
tomcat installation. After restart you should be able to tryout
the demo via pointing your browser to http://localhost:8080/demoname/

IF YOU WANT TO MODIFY / PLAY WITH THE DEMO SOURCES:
Alternativly you can copy and edit etc/custom-build.properties to
point to your tomcat installation. Then the "deploy" task will
directly deploy the according demo to the servlet container
without the need to create and copy a WAR file manually.

NOTE:
Some Demos are also available online via http://wingsframework.org,
although these demos may not be always up-to-date.

Have phun!


These two applications show how EJB timer invocations get lost on Wildfly if 
a maximum request limit is configured and timers are called while the server
is performing under maximum load.

# Test case

1. Start a clean Wildfly and configure the request controller to allow at 
   max two requests. Additionally, debug logging for `org.jboss.as.ejb3.timer` 
   should be enabled. The `configure-wildfly.cli` Wildfly CLI script can be 
   used to make this configuration automatically on a locally installed 
   Wildfly.
   
1. Deploy the request-maker application on the new wildfly. It is a simple 
   web application with single endpoint `/make-request` that takes thirty 
   seconds to complete a HTTP GET request. It enables us to clog up the 
   request controller.  

1. Make two requests to http://localhost:8080/make-request  in parallel. 
   These requests will run for 30 seconds and take up all available requests 
   on the Wildfly instance.

1. While the two requests are running deploy the timer application on the 
   same wildfly. This application defines an EJB timer which fires every 
   five seconds. Its callback take one second to complete.

1. The Wildfly console output shows that the timer application is deployed 
   and that it registered an ejb timer. However, the callback method of the 
   timer is never called. Not even after the two web requests have finished.

1. Once the two web requests finished, log into the Wildfly admin interface 
   and suspend and resume the server. Now the timer invocation will be 
   processed and the timer starts working as expected.

1. Now, try to make two more parallel requests to 
   http://localhost:8080/make-request while the timer is running. If the timer 
   is being activated just when the second request arrives, this request will be 
   rejected (as expected) with a "503 - Service Unavailable". If the second 
   request goes through, though, the timer stalls again until the requests 
   finished and the server is suspended and resumed.

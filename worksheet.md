
# Spring Boot MicroBadge Instructions

##What is Spring Boot?
Spring Boot is a framework designed to simplify the bootstrapping of a web application.
The framework can free the developer from defining boilerplate configuration. 
Spring boot favors convention over configuration and makes it easy to quickly create stand-alone, production-grade, Spring based applications that "just run".

Spring Boot has excellent online documentation and guides at [https://projects.spring.io/spring-boot/]()

## What You'll Need!
* Time: 3 or more hours
* Knowledge:
  * Fluency with Java language, including annotations
  * Familiarity with GitHub
  * Familiarity with Maven
  * Familiarity with the IntelliJ IDE
  * Familiarity with terminal window/command line file system navigation and tools.
    * This microbadge has been tested using MacOS and Linux.
* Software:
  * [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/index.html) or later
  * [Maven 3.0+](https://maven.apache.org/download.cgi) [connected to the FamilySearch Nexus Repo](https://almtools.ldschurch.org/fhconfluence/display/EPT/Connecting+to+the+Internal+Nexus+Repository)
  * [IntelliJ IDE](https://www.jetbrains.com/idea/)

## Check Your Tools!
To begin, we will jump-start by creating an initial maven project with a Spring Boot web site tool.  This step will validate you have all the necessary tools to run a Spring Boot application on your machine.
* Using a browser, go to: http://start.spring.io/
* Add "Web" and "Actuator" to the dependencies list
  * Do this by searching for "Web" and add it to the selected dependencies.
  * Then search for "Actuator" and add it to the selected dependencies.
* Leave the other fields in their default state (or modify them to fit your personal perferences)
* Select "Generate Project" - it will download a zip file for you.
* Extract the zip file contents into a location where you want the project to reside, and open the project (pom.xml) using IntelliJ
* Run the Application
  * INTELLIJ: Run the application from IntelliJ (find "DemoApplication.java", right click and run)
    * You have successfully run the DemoApplication if the last line of text within the console window shows: 
      * "Started DemoApplication in n.nnn seconds (JVM running for x.xxx)"
    * To stop the application: click the red square on the Run window menu panel.
  * COMMAND LiNE: Run the application from command line by opening a terminal window, changing directories to the folder of the application and typing "mvn spring-boot:run"
    * To verify the DemoApplication has successfully launched, open another terminal window and type: "curl localhost:8080"
      * The curl command should return text indicating a 404 "Not Found" error: similar to _{"timestamp":1516374295974,"status":404,"error":"Not Found","message":"No message available","path":"/"}_
      * If the DemoApplication is not loaded, the curl command will return something similar to: _curl: (7) Failed to connect to localhost port 8080: Connection refused_ 
    * To stop the application press CTRL-C in the terminal window running the DemoApplication.
* Congrats! You have successfully created and run your first spring-boot application using tools from the Spring-Boot web site. :)

##1. Hello World!
* Now that you have shown you can launch and stop a Spring Boot app, let's move on to our micro-badge project.
* Following the instructions in the [README.md](README.md), you should have forked the [fs-eng/spring-boot-microbadge](https://github.com/fs-eng/spring-boot-microbadge) project.
  * If you have not done so, do it now so you have a local copy of the microbadge project.
* Use IntelliJ to open your copy of the spring-boot-microbadge pom.xml and import the spring-boot-microbadge project. 
* Using IntelliJ, find the HelloWorldControllerTest, and run it... it should fail (expecting a 200, but actually receiving a 404).
* Typically rest controllers have the following annotations: @RestController and @RequestMapping. @RestController tells springboot 
    where to find your endpoints, and the @RequestMapping maps to the endpoint name. From the [Spring Boot guide](https://spring.io/guides/gs/spring-boot/) we read:
        _The class is flagged as a @RestController, meaning it's ready for use by Spring MVC to handle web requests. @RequestMapping maps / to the index() method. When invoked from a browser or using curl on the command line, the method returns pure text. That's because @RestController combines @Controller and @ResponseBody, two annotations that results in web requests returning data rather than a view_
* Find the HelloWorldController code and insert the @RestController annotation above the @RequestMapping (already included) and run the HelloWorldControllerTest again.
* Notice the new failure. Modify the HelloWorldController to make the test pass.
    
##2. Is It Healthy?
* Run _HealthTest_ - it should fail, not finding the /healthcheck/heartbeat endpoint.
* Notice the following dependency in the pom.xml:

        >     <dependency>
        >        <groupId>org.springframework.boot</groupId>
        >        <artifactId>spring-boot-starter-actuator</artifactId>
        >      </dependency>    
* With the actuator dependency in place, the /health endpoint is enabled by default. Start the app and use a browser to load [localhost:9000/health](localhost:9000/health) - 
    you should get a successful response indicating the status of the app is "UP".
* It may be useful to redirect the url to something more familiar. 
  Find the application.yml file and modify the _endpoints:_ section by inserting a line designating an endpoint path for the healthcheck. 
  After your modifications, the _endpoints:_ section should look similar to the following:
  
        >     endpoints:
        >       enabled: false
        >       health:
        >         enabled: true
        >         path: /healthcheck/heartbeat
* Run _HealthTest_ again - it should now pass
  * Hint: If the app is running in a command window, you may need to stop it prior to running the test from IntelliJ.
* We will come back to adding in your own HealthIndicator in step 6.

##3. Lets Talk!
* In this section we leverage RestTemplate to add capabilities to our simple app.
* Please review [The Guide to RestTemplate](http://www.baeldung.com/rest-template) (http://www.baeldung.com/rest-template) to better understand it's capbilities.
* Run _TfPersonClientTest_ - it should fail, because the client is not yet calling to Tree Foundation (TF).
* In the _TfPersonClient_ class, add a _RestTemplate_ field but do NOT add it to the constructor params.  For various reasons, you should not add the restTemplate 
    directly to the constructor. Instead, add a _RestTemplateBuilder_ to the constructor parameters, and construct a restTemplate within the constructor:
    >    restTemplate = restTemplateBuilder.rootUri(rootUri).build();
* NOTE: the restTemplate is using the passed in "rootUri", which comes from the url.base system property - configured in the application.yml file. 
    By constructing the restTemplate in this way you do not need to worry about setting the host uri when making a get / post / delete request.
* In the "getPerson" method, replace empty return with the following:
    >     final String sessionId = sessionHelper.getSessionId();
    >     try {
    >       return restTemplate.getForObject("/tf/person/{personId}?sessionId={sessionId}",
    >                                        TfPerson.class, personId, sessionId);
    >     }
    >     catch (HttpClientErrorException e) {
    >       if (HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {
    >         return null;
    >       }
    >       throw e;
    >     }
* Re-run _TfPersonClientTest_ and verify the test passes

##4. Wrappers Add Value!
* Run the tests in _PersonSummaryControllerTest_ class - _getPersonSummaryTest_ should fail.
* Add in the necessary @RestController and @RequestMapping annotations so that you create a "summary" endpoint in _PersonSummaryController_
* Add the following annotation onto the "getPersonSummary":
    >   @GetMapping(value = "/{personId}", produces = MediaType.APPLICATION_JSON_VALUE)
* The @GetMapping provides detailed instructions to Spring allowing for sub-paths to the original path declared by the _RequestMapping_ annotation.
    This annotation also indicates what type of media details this particular endpoint will return. The {personId} will be used as a path parameter in the getPersonSummary method.
* Add the @Component annotation on PersonSummaryMapper class. This is an indication to Spring that this class can be considered as a bean.
* Add the following fields into PersonSummaryService, and add them as constructor parameters:
  * TfPersonClient
  * PersonSummaryMapper
* Add the following code into the getOrCalculate method in "PersonSummaryService":
    >     TfPerson person = tfPersonClient.getPerson(personId);
    >     if (person == null) {
    >       return null;
    >     }
    >     return personSummaryMapper.map(person);
* Re-run the tests in _PersonSummaryControllerTest_ class. All tests should now be passing.     

##5. Why Make Multiple Calls?
* Run the tests in _PersonSummaryControllerWithCachingTest_ class. All tests should fail.     
* Add the following annotation to the _PersonSummaryService_ class's _getOrCalculate_ method:
    >     @Cacheable(value="personSummaryCache", key="#personId", unless="#result == null")
* This indicates to Spring that we will be using the _personSummaryCache_ bean to cache the results of any call to the _getOrCalculate_ method
    and that the key into that cache will be the personId
* Run _PersonSummaryControllerWithCachingTest_ tests again. The _getPersonCached_ test should still fail... 
    This is because our spring boot app does not know we want caching enabled.
* Add the following annotation to the _Application.java_ class:
    >     @EnableCaching
* Run _PersonSummaryControllerWithCachingTest_ tests again. The getPersonCached test should now pass, but _getPersonAfterDeletion_ test still fails.
* Open _PersonSummaryService_ and add a new field called _personSummaryCache_, and add it as a constructor parameter
* In the _deletePersonSummary_ instead of just returning, perform the following:
    >     personSummaryCache.evict(personId);    
* Run _PersonSummaryControllerWithCachingTest_ tests again, and verify that all the tests are now passing.

##6. Application Healthcheck Expanded!
* Run tests for _DynamoDbHealthTest_ - tests should fail.
* Startup the app, then use a browser to load: [http://localhost:9000/healthcheck/heartbeat](http://localhost:9000/healthcheck/heartbeat) and notice the status and diskSpace information
* Open _DynamoHealthIndicator_ and make it a bean by adding the @Component annotation
* Make _DynamoHealthIndicator_ implement "HealthIndicator" and add the missing override method
* Add the following into the new "health" method:
    >     try {
    >       TableDescription table = amazonDynamoDB.describeTable(dynamoDbTableName).getTable();
    >       Long itemCount = table.getItemCount();
    >       Long tableSize = table.getTableSizeBytes();
    > 
    >       return Health.up()
    >         .withDetail("itemCount", itemCount)
    >         .withDetail("tableSizeInBytes", tableSize)
    >         .build();
    >     }
    >     catch (Exception e) {
    >       return Health.down()
    >         .withException(e)
    >         .build();
    >     }
* Spring will automatically find all beans that implement the _HealthIndicator_, adding them to its list of _health_ beans. When the health endpoint is called 
    spring will call the _health_ method on each of these beans, adding their details to the overall healthcheck response.
* Restart the app, and again use a browser to load: [http://localhost:9000/healthcheck/heartbeat](http://localhost:9000/healthcheck/heartbeat) and notice the new dynamodb information
* Run the tests for _DynamoDbHealthTest_ - tests should now succeed.

##7. Let's Count!
* Spring actuators have built in metrics. These can be extended, but let's just take a look at what comes out of the box
* Enable the metrics endpoint by adding the following under the _endpoint:_ in the application.yml file:
    >       metrics:
    >         enabled: true
* Start the app, and use a browser to load the following URL:  [http://localhost:9000/metrics](http://localhost:9000/metrics)
* Notice that there is no metrics yet for the "hello" endpoint.
* Load [http://localhost:8080/hello](http://localhost:8080/hello), and then go back and refresh the metrics url. You should see some new "hello" metrics (ie. "counter.status.200.hello")
* Run _MetricsTest_ - tests should all be passing.
    
##8. What Kind Of App Is This?
* Springboot actuators also have a built in info section that can easily be extended.
* Enable the info endpoint by adding following to application.yml under the _endpoint:_ configuration:
    >       info:
    >         enabled: true
* Start the app, and load [http://localhost:9000/info](http://localhost:9000/info) and notice that it isn't very useful yet.
* Add the following to application.yml, which will add information to the info endpoint 
    >     info:
    >        app:
    >          name: @project.artifactId@
    >          description: @project.description@
    >          version: @project.version@
* Re-start the app, and re-load the info url ([http://localhost:9000/info](http://localhost:9000/info))
* Notice that the info url now has something useful, taken straight from the project's pom.xml.
* Run _InfoTest_ - tests should all be passing.

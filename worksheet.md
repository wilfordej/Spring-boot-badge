
## SpringBoot MicroBadge Instructions

1. Learn to use the tools!
    * Go to: http://start.spring.io/
    * Add "Web" and "Actuator" to the dependencies list
    * Select "Generate Project" - it will download a zip file for you
    * Extract the zip file contents, and open it up in IntelliJ
    * Run the application from IntelliJ (find "DemoApplication.java", right click and run)
    * Run the application from command line by running "mvn spring-boot:run"
    * Congrats! This is your first spring-boot application :)

2. Hello World!
    * Run the HelloWorldControllerTest... it should fail
    * Typically rest controllers have the following annotations: @RestController and @RequestMapping. @RestController tells springboot 
    where to find your endpoints, and the @RequestMapping maps to the endpoint name. From: https://spring.io/guides/gs/spring-boot/
        > The class is flagged as a @RestController, meaning it's ready for use by Spring MVC to handle web requests. @RequestMapping maps / to the index() method. When invoked from a browser or using curl on the command line, the method returns pure text. That's because @RestController combines @Controller and @ResponseBody, two annotations that results in web requests returning data rather than a view.
    * Add in the @RestController annotation above the @RequestMapping (already included) and run again.
    * Notice the new failure. Modify the HelloWorldController to make the test pass.
    
3. Is it healthy?
    * Run "HealthTest" - it should fail, not finding the /healthcheck/heartbeat endpoint.
    * Notice the following dependency in the pom.xml:
        >     <dependency>
        >        <groupId>org.springframework.boot</groupId>
        >        <artifactId>spring-boot-starter-actuator</artifactId>
        >      </dependency>    
    * With the actuator dependency in place, the /health endpoint is enabled by default. Start the app and hit localhost:9000/health - 
    you should get a successful response.
    * It may be useful to redirect the url to something more familiar. Replace "endpoints:" with the following:
        >     endpoints:
        >       enabled: false
        >       health:
        >         enabled: true
        >         path: /healthcheck/heartbeat
    * Run "HealthTest" - it should now pass
    * We will come back to adding in your own HealthIndicator in step 7.

4. Lets learn to talk..
    * Review the following restTemplate guide: http://www.baeldung.com/rest-template
    * Run "TfPersonClientTest" - it should fail, because the client is not yet calling to TF.
    * In "TfPersonClient" class, add a "RestTemplate" field but do NOT add it to the constructor params - For various reasons, you should not add the restTemplate 
    directly to the constructor. Instead, add a "RestTemplateBuilder" to the constructor parameters, and construct a restTemplate within the constructor:
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
    * Re-run TfPersonClientTest and verify that the tests pass

5. Wrap it all up
    * Run "PersonSummaryControllerTest" - it should fail.
    * Add in the necessary @RestController and @RequestMapping annotations so that you create a "summary" endpoint in "PersonSummaryController"
    * Add the following annotation onto the "getPersonSummary":
    >   @GetMapping(value = "/{personId}", produces = MediaType.APPLICATION_JSON_VALUE)
    * The @GetMapping provides more detailed instructions to spring as to how to map sub-paths for what was originally described in the "RequestMapping" annotation
    here you are also indicating what type of media this particular endpoint will return. The {personId} will be used as a path parameter in the getPersonSummary method.
    * Add the @Component annotation on PersonSummaryMapper. This is an indication to Spring that this class can be considered as a bean.
    * Add the following fields into PersonSummaryService, and add them as constructor parameters:
      * TfPersonClient
      * PersonSummaryMapper
    * Add the following code into the getOrCalculate method in "PersonSummaryService":
    >     TfPerson person = tfPersonClient.getPerson(personId);
    >     if (person == null) {
    >       return null;
    >     }
    >     return personSummaryMapper.map(person);
    * Re-run PersonSummaryControllerTest. All tests should now be passing.     

6. Why make multiple calls?
    * Run PersonSummaryControllerWithCachingTest. All tests should fail.     
    * Add the following line on the "getOrCalculate" method:
    >     @Cacheable(value="personSummaryCache", key="#personId", unless="#result == null")
    * This indicates to Spring that we will be using the "personSummaryCache" bean to cache the results of any call to the "getOrCalculate" method
    and that the key into that cache will be the personId
    * Run PersonSummaryControllerWithCachingTest. The getPersonCached test should still fail.. 
    This is because our spring boot app does not know we want caching enabled.
    * Add the following line on the "Application.java" class:
    >     @EnableCaching
    * Run PersonSummaryControllerWithCachingTest again. The getPersonCached test should now pass
    * Open PersonSummaryService and add a new field called personSummaryCache, and add it as a constructor parameter
    * In the "deletePersonSummary" instead of just returning, perform the following:
    >     personSummaryCache.evict(personId);    
    * Run PersonSummaryControllerWithCachingTest, and verify that all the tests are now passing.

7. Expanding the application healthcheck.
    * Run the DynamoDbHealthTest - tests should fail.
    * Startup the app, and hit: http://localhost:9000/healthcheck/heartbeat and notice the status and diskSpace information
    * Open "DynamoHealthIndicator" and make it a bean by adding the @Component annotation
    * Make "DynamoHealthIndicator" implement "HealthIndicator" and add the missing override method
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
    * Spring will automatically find all beans that implement the "HealthIndicator", adding them to its list of "health" beans. When the health endpoint is called 
    spring will call the "health" method on each of these beans, adding their details to the overall healthcheck response.
    * Restart the app, and again hit: http://localhost:8080/healthcheck/heartbeat and notice the new dynamodb information
    * Run the DynamoDbHealthTest - tests should now succeed.

8. Let's count
    * Spring actuators have built in metrics. These can be extended, but let's just take a look at what comes out of the box
    * Enable the metrics endpoint by adding the following under the "endpoint:" in the application.yml file:
    >       metrics:
    >         enabled: true
    * Start the app, and load the following url:  http://localhost:9000/metrics
    * Notice that there is no metrics yet for the "hello" endpoint.
    * Load http://localhost:8080/hello, and then go back and refresh the metrics url. You should see some new "hello" metrics (ie. "counter.status.200.hello")
    * Run MetricsTest - tests should all be passing.
    
9. What kind of app is this?
    * Springboot actuators also have a built in info section that can easily be extended.
    * Enable the info endpoint by adding following to application.yml under the "endpoint:" configuration:
    >       info:
    >         enabled: true
    * Load http://localhost:9000/info and notice that it isn't very useful yet.
    * Add the following to application.yml, which will add information to the info endpoint 
    >     info:
    >        app:
    >          name: @project.artifactId@
    >          description: @project.description@
    >          version: @project.version@
    * Re-start the app, and re-load the info url (http://localhost:9000/info)
    * Notice that the info url now has something useful, taken straight from the pom.
    * Run InfoTest - tests should all be passing.

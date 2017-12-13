#!/bin/bash

#The following command line runs qualification locally against the integration reference (local dynamodb instance)
mvn com.jcabi:jcabi-dynamodb-maven-plugin:start spring-boot:run $@

#!/bin/bash
cd ./target/dynamodb-dist/
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb -port 7777 -inMemory


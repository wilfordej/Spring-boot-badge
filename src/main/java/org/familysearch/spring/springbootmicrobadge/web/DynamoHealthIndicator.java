package org.familysearch.spring.springbootmicrobadge.web;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import org.springframework.beans.factory.annotation.Value;

public class DynamoHealthIndicator {

  private final AmazonDynamoDB amazonDynamoDB;
  private final String dynamoDbTableName;

  public DynamoHealthIndicator(AmazonDynamoDB amazonDynamoDB,
                               @Value("${DYNAMODB_TABLE_NAME}") String dynamoDbTableName) {
    this.amazonDynamoDB = amazonDynamoDB;
    this.dynamoDbTableName = dynamoDbTableName;
  }
}

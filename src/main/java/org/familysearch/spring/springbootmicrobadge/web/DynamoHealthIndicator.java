package org.familysearch.spring.springbootmicrobadge.web;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class DynamoHealthIndicator implements HealthIndicator {

  private final AmazonDynamoDB amazonDynamoDB;
  private final String dynamoDbTableName;

  public DynamoHealthIndicator(AmazonDynamoDB amazonDynamoDB,
                               @Value("${DYNAMODB_TABLE_NAME}") String dynamoDbTableName) {
    this.amazonDynamoDB = amazonDynamoDB;
    this.dynamoDbTableName = dynamoDbTableName;
  }

  @Override
  public Health health() {

    try {
      TableDescription table = amazonDynamoDB.describeTable(dynamoDbTableName).getTable();
      Long itemCount = table.getItemCount();
      Long tableSize = table.getTableSizeBytes();

      return Health.up()
        .withDetail("itemCount", itemCount)
        .withDetail("tableSizeInBytes", tableSize)
        .build();
    }
    catch (Exception e) {
      return Health.down()
        .withException(e)
        .build();
    }
  }
}

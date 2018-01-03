package org.familysearch.spring.springbootmicrobadge.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDynamoDBRepositories(
  dynamoDBMapperConfigRef = "dynamoDBMapperConfig",
  basePackages = "org.familysearch.spring")
public class DynamoDBConfig {

  @Bean(destroyMethod = "shutdown")
  public AmazonDynamoDB amazonDynamoDB() {
    return AmazonDynamoDBClient.builder()
      .withRegion(Regions.US_EAST_1)
      .withCredentials(new DefaultAWSCredentialsProviderChain())
      .build();
  }

  @Bean
  public DynamoDB dynamoDB(AmazonDynamoDB amazonDynamoDB) {
    return new DynamoDB(amazonDynamoDB);
  }

  @Bean
  public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB, DynamoDBMapperConfig dynamoDBMapperConfig) {
    return new DynamoDBMapper(amazonDynamoDB, dynamoDBMapperConfig);
  }

  @Bean
  public DynamoDBMapperConfig dynamoDBMapperConfig(@Value("${DYNAMODB_TABLE_NAME}") String dynamoDbTableName) {
    return DynamoDBMapperConfig.builder()
      .withTableNameResolver((clazz, config) -> dynamoDbTableName)
      .build();
  }

}

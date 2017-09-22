package org.familysearch.spring.springbootmicrobadge.config;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
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
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * @author tlindhardt
 */
@Configuration
@EnableDynamoDBRepositories(
    dynamoDBMapperConfigRef = "dynamoDBMapperConfig",
    basePackages = "org.familysearch.spring")
public class DynamoDBConfig {

  @Value("${DYNAMODB_TABLE_NAME}")
  String dynamoDbTableName;

  @Profile("!localhostDynamo")
  @Bean
  public AmazonDynamoDB amazonDynamoDB(AWSCredentialsProviderChain provider) {
    AmazonDynamoDBClient client = new AmazonDynamoDBClient(provider);
    client.setRegion(Region.getRegion(Regions.fromName("us-east-1")));
    return client;
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
  public DynamoDBMapperConfig dynamoDBMapperConfig() {
    return new DynamoDBMapperConfig((DynamoDBMapperConfig.TableNameResolver) (clazz, config) -> dynamoDbTableName);
  }

  @Bean
  public AWSCredentialsProviderChain awsCredentialsProviderChain() {
    return new DefaultAWSCredentialsProviderChain();
  }
}

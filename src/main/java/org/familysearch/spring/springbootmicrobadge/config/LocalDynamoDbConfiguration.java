package org.familysearch.spring.springbootmicrobadge.config;

import java.util.Iterator;
import javax.annotation.PostConstruct;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import org.familysearch.spring.springbootmicrobadge.domain.PersonSummaryEntity;

/**
 * @author Jared Daniels (danielsj@familysearch.org)
 */
@Profile("localhostDynamo")
@Configuration
public class LocalDynamoDbConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(LocalDynamoDbConfiguration.class);

  @Autowired
  DynamoDBMapper dynamoDBMapper;

  @Autowired
  DynamoDB dynamoDB;

  @Value("${DYNAMODB_TABLE_NAME}")
  String dynamoDbTableName;

  @Bean
  public AmazonDynamoDB amazonDynamoDB() {
    AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder.standard();
    builder.setEndpointConfiguration(getEndpointConfiguration());
    builder.withCredentials(new AWSStaticCredentialsProvider(getAwsCredentials()));

    return builder.build();
  }

  @PostConstruct
  public void initializeTables() throws InterruptedException {
    try {
      if (!doesTableExist(dynamoDbTableName)) {
        System.out.println("Create the qualification database Table");
        CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(PersonSummaryEntity.class);
        tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
        Table table = dynamoDB.createTable(tableRequest);
        table.waitForActive();
      }
    }
    catch (SdkClientException e) {
      throw new IllegalStateException("DynamoDB is likely not running locally. " +
                                       "An easy way to start it would be to run the '.startDynamoDbLocal.sh' script, " +
                                       "and then restart the application", e);

    }
    catch (UnsupportedOperationException e) {
      LOGGER.info("Likely an issue with running DynamoDbLocal. We can ignore this error, and simply continue");
    }
  }

  private AwsClientBuilder.EndpointConfiguration getEndpointConfiguration() {
    return new AwsClientBuilder.EndpointConfiguration("http://localhost:7777", "us-east-1");
  }

  private AWSCredentials getAwsCredentials() {
    return new AWSCredentials() {
      @Override
      public String getAWSAccessKeyId() {
        return "";
      }

      @Override
      public String getAWSSecretKey() {
        return "";
      }
    };
  }

  public boolean doesTableExist(String tableName) {
    TableCollection<ListTablesResult> tables = dynamoDB.listTables();
    Iterator<Table> iterator = tables.iterator();

    while (iterator.hasNext()) {
      Table table = iterator.next();
      if(tableName.equals(table.getTableName())) {
        System.out.println("Table exists with name - " + tableName);
        return true;
      }
    }
    return false;
  }
}

package org.familysearch.spring.springbootmicrobadge.config;

import java.io.File;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.familysearch.spring.springbootmicrobadge.domain.PersonSummaryEntity;

@ConditionalOnProperty(prefix="dynamo", name="db", havingValue = "local")
@Configuration
public class LocalDynamoDbConfiguration {

  @Bean(destroyMethod = "shutdown")
  public AmazonDynamoDB amazonDynamoDB() {
    String value = new File("native-libs").getAbsolutePath();
    System.setProperty("sqlite4java.library.path", value);
    return DynamoDBEmbedded.create().amazonDynamoDB();
  }

  @Bean
  public ApplicationRunner initializeTablesRunner(@Value("${DYNAMODB_TABLE_NAME}") String dynamoDbTableName,
                                                  DynamoDBMapper dynamoDBMapper, DynamoDB dynamoDB) {
    return new LocalInitializeTablesRunner(dynamoDbTableName, dynamoDBMapper, dynamoDB);
  }

  private static class LocalInitializeTablesRunner implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalInitializeTablesRunner.class);

    private final String dynamoDbTableName;
    private final DynamoDBMapper dynamoDBMapper;
    private final DynamoDB dynamoDB;

    LocalInitializeTablesRunner(String dynamoDbTableName,
                                DynamoDBMapper dynamoDBMapper, DynamoDB dynamoDB) {
      this.dynamoDbTableName = dynamoDbTableName;
      this.dynamoDBMapper = dynamoDBMapper;
      this.dynamoDB = dynamoDB;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
      try {
        if (!doesTableExist(dynamoDbTableName)) {
          LOGGER.info("Creating the qualification database table");
          CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(PersonSummaryEntity.class);
          tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
          Table table = dynamoDB.createTable(tableRequest);
          table.waitForActive();
          LOGGER.info("Created the qualification database table");
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

    private boolean doesTableExist(String tableName) {
      TableCollection<ListTablesResult> tables = dynamoDB.listTables();

      for (Table table : tables) {
        if (tableName.equals(table.getTableName())) {
          LOGGER.info("Table exists with name '{}'", tableName);
          return true;
        }
      }
      return false;
    }

  }
}

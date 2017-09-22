package org.familysearch.spring.springbootmicrobadge;

import java.io.File;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.familysearch.spring.springbootmicrobadge.config.ApplicationConfig;

@Configuration
@ComponentScan(basePackages = {"org.familysearch.spring"})
@EnableAutoConfiguration
public class ApplicationTestConfig extends ApplicationConfig {

  @Bean
  public AmazonDynamoDB amazonDynamoDB() {
    String value = new File("native-libs").getAbsolutePath();
    System.setProperty("sqlite4java.library.path", value);
    return DynamoDBEmbedded.create().amazonDynamoDB();
  }

}

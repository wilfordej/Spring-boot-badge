package org.familysearch.spring.springbootmicrobadge.web;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.familysearch.spring.springbootmicrobadge.schema.Health;

import static org.assertj.core.api.Assertions.assertThat;

public class DynamoDbHealthTest extends BaseComponent {

  @Test
  public void getDynamoDbHealth() {
    ResponseEntity<Health> responseEntity = testRestTemplate.getForEntity(getHealthcheckUrl(), Health.class);
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody().getDynamo())
     .as("dynamodb information should be present in the healthcheck")
     .isNotNull();
    assertThat(responseEntity.getBody().getDynamo().getStatus().getCode()).isEqualTo("UP");
    assertThat(responseEntity.getBody().getDynamo().getItemCount()).isGreaterThanOrEqualTo(0);
    assertThat(responseEntity.getBody().getDynamo().getTableSizeInBytes()).isGreaterThanOrEqualTo(0);
  }

  private String getHealthcheckUrl() {
    return urlFactoryForTesting.getMgmtUrl() + "/healthcheck/heartbeat";
  }

}

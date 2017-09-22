package org.familysearch.spring.springbootmicrobadge.web;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.familysearch.spring.springbootmicrobadge.schema.Health;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HealthTest extends BaseComponent {

  @Test
  public void getHealthTest() {
    ResponseEntity<Health> entity = getRestTemplate().getForEntity(getHealthcheckUrl(), Health.class);
    assertEquals(HttpStatus.OK, entity.getStatusCode());
    assertEquals("UP", entity.getBody().getStatus());
  }

  @Test
  public void getDiskSpaceTest() {
    ResponseEntity<Health> entity = getRestTemplate().getForEntity(getHealthcheckUrl(), Health.class);
    assertEquals(HttpStatus.OK, entity.getStatusCode());
    assertEquals("UP", entity.getBody().getDiskSpace().getStatus());
    assertTrue(entity.getBody().getDiskSpace().getFree() > 100);
    assertTrue(entity.getBody().getDiskSpace().getTotal() > 100);
  }

  @Test
  public void getDynamoDbHealth() {
    ResponseEntity<Health> entity = getRestTemplate().getForEntity(getHealthcheckUrl(), Health.class);
    assertEquals(HttpStatus.OK, entity.getStatusCode());
    assertEquals("UP", entity.getBody().getDynamo().getStatus().getCode());
    assertTrue(entity.getBody().getDynamo().getItemCount() >= 0);
    assertTrue(entity.getBody().getDynamo().getTableSizeInBytes() >= 0);
  }

  private String getHealthcheckUrl() {
    return urlFactoryForTesting.getMgmtUrl() + "/healthcheck/heartbeat";
  }
}
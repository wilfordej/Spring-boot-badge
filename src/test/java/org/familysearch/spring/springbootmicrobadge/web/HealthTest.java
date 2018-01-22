package org.familysearch.spring.springbootmicrobadge.web;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.familysearch.spring.springbootmicrobadge.schema.Health;

import static org.assertj.core.api.Assertions.assertThat;

public class HealthTest extends BaseComponent {

  @Test
  public void getHealthTest() {
    ResponseEntity<Health> responseEntity = restTemplate.getForEntity(getHealthcheckUrl(), Health.class);
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody().getStatus()).isEqualTo("UP");
  }

  @Test
  public void getDiskSpaceTest() {
    ResponseEntity<Health> responseEntity = restTemplate.getForEntity(getHealthcheckUrl(), Health.class);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody().getDiskSpace().getStatus()).isEqualTo("UP");
    assertThat(responseEntity.getBody().getDiskSpace().getFree()).isGreaterThan(100);
    assertThat(responseEntity.getBody().getDiskSpace().getTotal()).isGreaterThan(100);
  }

  private String getHealthcheckUrl() {
    return urlFactoryForTesting.getMgmtUrl() + "/healthcheck/heartbeat";
  }
}

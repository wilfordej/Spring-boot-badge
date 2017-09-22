package org.familysearch.spring.springbootmicrobadge.web;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.familysearch.spring.springbootmicrobadge.schema.Info;

import static org.junit.Assert.assertEquals;

public class InfoTest extends BaseComponent {

  @Test
  public void getInfoTest() {
    ResponseEntity<Info> appInfo = getRestTemplate().getForEntity(getInfoUrl(), Info.class);
    assertEquals(HttpStatus.OK, appInfo.getStatusCode());
    assertEquals("spring-boot-microbadge", appInfo.getBody().getInfoDetails().getName());
    assertEquals("Demo project for Spring Boot", appInfo.getBody().getInfoDetails().getDescription());
    assertEquals("0.0.1-SNAPSHOT", appInfo.getBody().getInfoDetails().getVersion());
  }

  private String getInfoUrl() {
    return urlFactoryForTesting.getMgmtUrl() + "info";
  }
}
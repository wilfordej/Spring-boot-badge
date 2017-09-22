package org.familysearch.spring.springbootmicrobadge.web;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HelloWorldControllerTest extends BaseComponent {

  @Test
  public void getHelloWorld() {
    ResponseEntity<String> entity = getRestTemplate().getForEntity(urlFactoryForTesting.getTestUrl() + "/hello", String.class);

    assertEquals(HttpStatus.OK, entity.getStatusCode());
    assertTrue(entity.getBody().contains("Hello!"));
  }
}
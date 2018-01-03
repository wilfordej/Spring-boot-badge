package org.familysearch.spring.springbootmicrobadge.web;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class HelloWorldControllerTest extends BaseComponent {

  @Test
  public void getHelloWorld() {
    ResponseEntity<String> responseEntity = restTemplate.getForEntity("/hello", String.class);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody()).contains("Hello!");
  }
}

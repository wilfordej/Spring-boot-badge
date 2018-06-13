package org.familysearch.spring.springbootmicrobadge.web;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.familysearch.spring.springbootmicrobadge.domain.PersonSummaryCache;
import org.familysearch.spring.springbootmicrobadge.service.PersonSummary;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonSummaryControllerTest extends BaseComponent {

  @Autowired
  PersonSummaryCache personSummaryCache;

  @Test
  public void getPersonSummaryTest() {
    String personId = createDefaultPersonForTesting();

    ResponseEntity<PersonSummary> responseEntity = testRestTemplate
      .getForEntity("/summary/{personId}", PersonSummary.class, personId);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody())
      .hasFieldOrPropertyWithValue("firstName", "Bob")
      .hasFieldOrPropertyWithValue("lastName", "Dole");
  }

  @Test
  public void getPerson_NotFoundTest() {
    final ResponseEntity<PersonSummary> responseEntity =
      testRestTemplate.getForEntity("/summary/{personId}", PersonSummary.class, "XXXX-XXX");

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

}

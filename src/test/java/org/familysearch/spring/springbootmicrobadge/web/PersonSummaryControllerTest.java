package org.familysearch.spring.springbootmicrobadge.web;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.familysearch.spring.springbootmicrobadge.service.PersonSummary;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonSummaryControllerTest extends BaseComponent {

  //Test person summary endpoint
  @Test
  public void getPersonSummaryTest() {
    String personId = createDefaultPersonForTesting();

    ResponseEntity<PersonSummary> responseEntity = restTemplate
      .getForEntity("/summary/{personId}", PersonSummary.class, personId);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody())
      .hasFieldOrPropertyWithValue("firstName", "Bob")
      .hasFieldOrPropertyWithValue("lastName", "Dole");
  }

  @Test
  public void getPerson_NotFoundTest() {
    final ResponseEntity<PersonSummary> responseEntity =
      restTemplate.getForEntity("/summary/{personId}", PersonSummary.class, "XXXX-XXX");

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void getPersonCached() {
    //Test dynamo put/get (200)
    String personId = createDefaultPersonForTesting();
    restTemplate.getForEntity("/summary/{personId}", PersonSummary.class, personId);

    ResponseEntity<PersonSummary> responseEntity = restTemplate
      .getForEntity("/summary/{personId}", PersonSummary.class, personId);

    // Assert a cache get, not from TF
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody())
      .hasFieldOrPropertyWithValue("firstName", "Bob")
      .hasFieldOrPropertyWithValue("lastName", "Dole");
  }

  @Test
  public void getPersonAfterDeletion() {
    //Test dynamo put/delete/get (404)
    String personId = createDefaultPersonForTesting();
    restTemplate.getForEntity("/summary/{personId}", PersonSummary.class, personId);

    deleteTreePerson(personId);
    restTemplate.delete("/summary/{personId}", personId);

    final ResponseEntity<PersonSummary> getAfterDeleteResponseEntity =
      restTemplate.getForEntity("/summary/{personId}", PersonSummary.class, personId);

    assertThat(getAfterDeleteResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

}

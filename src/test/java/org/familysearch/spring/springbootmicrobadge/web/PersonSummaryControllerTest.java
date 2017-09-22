package org.familysearch.spring.springbootmicrobadge.web;

import java.net.URI;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import org.familysearch.spring.springbootmicrobadge.service.PersonSummary;

import static org.junit.Assert.assertEquals;

public class PersonSummaryControllerTest extends BaseComponent {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  //Test person summary endpoint
  @Test
  public void getPersonSummaryTest() {
    String personId = createDefaultPersonForTesting();

    ResponseEntity<PersonSummary> entity = getRestTemplate().getForEntity(urlFactoryForTesting.getTestUrl() + "/summary/" + personId, PersonSummary.class);

    assertEquals(HttpStatus.OK, entity.getStatusCode());
    assertEquals("Bob", entity.getBody().getFirstName());
    assertEquals("Dole", entity.getBody().getLastName());
  }

  @Test
  public void getPerson_NotFoundTest() {
    thrown.expect(HttpClientErrorException.class);
    ResponseEntity<PersonSummary> entity = getRestTemplate().getForEntity(urlFactoryForTesting.getTestUrl() + "/summary/XXXX-XXX", PersonSummary.class);

    thrown.expectMessage(String.valueOf(HttpStatus.NOT_FOUND.value()));
  }

  @Test
  public void getPersonCached() {
    //Test dynamo put/get (200)

    String personId = createDefaultPersonForTesting();
    getRestTemplate().getForEntity(urlFactoryForTesting.getTestUrl() + "/summary/" + personId, PersonSummary.class);

    ResponseEntity<PersonSummary> entity = getRestTemplate().getForEntity(urlFactoryForTesting.getTestUrl() + "/summary/" + personId, PersonSummary.class);
    // Assert a cache get, not from TF

    assertEquals(HttpStatus.OK, entity.getStatusCode());
    assertEquals("Bob", entity.getBody().getFirstName());
    assertEquals("Dole", entity.getBody().getLastName());
  }

  @Test
  public void getPersonAfterDeletion() {
    //Test dynamo put/delete/get (404)
    String personId = createDefaultPersonForTesting();
    getRestTemplate().getForEntity(urlFactoryForTesting.getTestUrl() + "/summary/" + personId, PersonSummary.class);

    deletePerson(personId);
    getRestTemplate().delete(URI.create(urlFactoryForTesting.getTestUrl() + "/summary/" + personId));

    thrown.expect(HttpClientErrorException.class);
    ResponseEntity<PersonSummary> entity = getRestTemplate().getForEntity(urlFactoryForTesting.getTestUrl() + "/summary/" + personId, PersonSummary.class);
    thrown.expectMessage(String.valueOf(HttpStatus.NOT_FOUND.value()));
  }

}
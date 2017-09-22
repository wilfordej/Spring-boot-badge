package org.familysearch.spring.springbootmicrobadge.web;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import org.familysearch.spring.springbootmicrobadge.ApplicationTestConfig;
import org.familysearch.spring.springbootmicrobadge.UrlFactoryForTesting;
import org.familysearch.tf.jsonbind.TfNamePartType;
import org.familysearch.tf.jsonbind.dto.TfNameForm;
import org.familysearch.tf.jsonbind.dto.TfNamePart;
import org.familysearch.tf.jsonbind.dto.TfPerson;
import org.familysearch.tf.jsonbind.dto.TfPersonSummary;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = {ApplicationTestConfig.class})
@Ignore
public class BaseComponent {

  @Autowired
  RestTemplateBuilder restTemplateBuilder;

  @Autowired
  UrlFactoryForTesting urlFactoryForTesting;

  private RestTemplate restTemplate;

  @Before
  public void setup() {
    restTemplate = restTemplateBuilder.build();
  }

  String createDefaultPersonForTesting() {
    ResponseEntity<String> response = restTemplate.postForEntity(urlFactoryForTesting.getBaseUrl() + "/tf/person", createTfPerson("Bob", "Dole"), String.class);
    return response.getHeaders().get("X-Entity-ID").get(0);
  }

  void deletePerson(String personId) {
    restTemplate.delete(URI.create(urlFactoryForTesting.getBaseUrl() + "/tf/person/" + personId));
  }

  private TfPerson createTfPerson(String firstName, String lastName) {
    TfPerson tfPerson = new TfPerson();
    TfPersonSummary summary = createSummary(firstName, lastName);
    summary.setNameForms(Collections.singletonList(createTfNameForm(firstName, lastName)));
    tfPerson.setSummary(summary);
    return tfPerson;
  }

  private TfNameForm createTfNameForm(String firstName, String lastName) {
    TfNameForm nameForm = new TfNameForm();
    nameForm.setFullText(getFullName(firstName, lastName));
    nameForm.setParts(Arrays.asList(
        getTfNamePart(TfNamePartType.GIVEN.getTypeString(), firstName),
        getTfNamePart(TfNamePartType.SURNAME.getTypeString(), lastName)));
    return nameForm;
  }

  private TfPersonSummary createSummary(String first, String lastName) {
    TfPersonSummary tfPersonSummary = new TfPersonSummary();
    tfPersonSummary.setName(getFullName(first, lastName));
    return tfPersonSummary;
  }

  private String getFullName(String first, String lastName) {
    return first + " " + lastName;
  }

  private TfNamePart getTfNamePart(String typeString, String bob) {
    TfNamePart givenPart = new TfNamePart();
    givenPart.setType(typeString);
    givenPart.setValue(bob);
    return givenPart;
  }

  public RestTemplate getRestTemplate() {
    return restTemplate;
  }
}


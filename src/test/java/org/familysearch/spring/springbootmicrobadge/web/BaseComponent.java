package org.familysearch.spring.springbootmicrobadge.web;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import org.familysearch.spring.springbootmicrobadge.UrlFactoryForTesting;
import org.familysearch.tf.jsonbind.TfNamePartType;
import org.familysearch.tf.jsonbind.dto.TfNameForm;
import org.familysearch.tf.jsonbind.dto.TfNamePart;
import org.familysearch.tf.jsonbind.dto.TfPerson;
import org.familysearch.tf.jsonbind.dto.TfPersonSummary;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = {
  "url.base=http://localhost:8080",
  "url.test=http://localhost:8080",
  "url.mgmt=http://localhost:9000"
})
@Ignore
public class BaseComponent {

  @Autowired
  TestRestTemplate restTemplate;

  @Autowired
  UrlFactoryForTesting urlFactoryForTesting;

  public String createDefaultPersonForTesting() {
    ResponseEntity<String> response = restTemplate.postForEntity("/tf/person", createTfPerson("Bob", "Dole"), String.class);
    return response.getHeaders().get("X-Entity-ID").get(0);
  }

  void deleteTreePerson(String personId) {
    restTemplate.delete("/tf/person/{personId}", personId);
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

}


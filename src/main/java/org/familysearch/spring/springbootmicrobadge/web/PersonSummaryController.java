package org.familysearch.spring.springbootmicrobadge.web;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.familysearch.spring.springbootmicrobadge.service.PersonSummary;
import org.familysearch.spring.springbootmicrobadge.service.PersonSummaryService;

public class PersonSummaryController {

  private final PersonSummaryService personSummaryService;

  public PersonSummaryController(PersonSummaryService personSummaryService) {
    this.personSummaryService = personSummaryService;
  }

  public ResponseEntity<PersonSummary> getPersonSummary(@PathVariable("personId") String personId) {
    PersonSummary personSummary = personSummaryService.getOrCalculate(personId);
    if (personSummary == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(personSummary);
  }

  @DeleteMapping(value = "/{personId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PersonSummary> deletePersonSummary(@PathVariable("personId") String personId) {
    personSummaryService.deletePersonSummary(personId);
    return ResponseEntity.ok().build();
  }

}

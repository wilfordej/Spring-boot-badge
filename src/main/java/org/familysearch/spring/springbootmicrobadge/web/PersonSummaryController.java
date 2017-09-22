package org.familysearch.spring.springbootmicrobadge.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.familysearch.spring.springbootmicrobadge.service.PersonSummary;
import org.familysearch.spring.springbootmicrobadge.service.PersonSummaryService;

@RestController
@RequestMapping("/summary")
public class PersonSummaryController {

  @Autowired
  PersonSummaryService personSummaryService;

  @RequestMapping(value = "/{personId:.*}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<PersonSummary> getPersonSummary(@PathVariable(value = "personId") String personId) {
    PersonSummary personSummary = personSummaryService.getOrCalculate(personId);
    if (personSummary == null) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity(personSummary, HttpStatus.OK);
  }

  @RequestMapping(value = "/{personId:.*}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<PersonSummary> deletePersonSummary(@PathVariable(value = "personId") String personId) {
    personSummaryService.deletePersonSummary(personId);
    return new ResponseEntity(HttpStatus.OK);
  }

}

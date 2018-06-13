package org.familysearch.spring.springbootmicrobadge.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import org.familysearch.spring.springbootmicrobadge.client.TfPersonClient;
import org.familysearch.spring.springbootmicrobadge.domain.PersonSummaryCache;
import org.familysearch.tf.jsonbind.dto.TfPerson;

@Component
public class PersonSummaryService {
  private TfPersonClient tfPersonClient;
  private PersonSummaryMapper personSummaryMapper;
  private PersonSummaryCache personSummaryCache;

  public PersonSummaryService(TfPersonClient tfPersonClient, PersonSummaryMapper personSummaryMapper, PersonSummaryCache personSummaryCache) {
    this.tfPersonClient = tfPersonClient;
    this.personSummaryMapper = personSummaryMapper;
    this.personSummaryCache = personSummaryCache;
  }

  @Cacheable(value="personSummaryCache", key="#personId", unless="#result == null")
  public PersonSummary getOrCalculate(String personId) {

    TfPerson person = tfPersonClient.getPerson(personId);
    if (person == null) {
      return null;
    }
    return personSummaryMapper.map(person);

  }

  public void deletePersonSummary(String personId) {
    personSummaryCache.evict(personId);
  }
}

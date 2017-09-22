package org.familysearch.spring.springbootmicrobadge.service;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.stereotype.Component;

import org.familysearch.spring.springbootmicrobadge.client.TfPersonClient;
import org.familysearch.spring.springbootmicrobadge.domain.PersonSummaryEntity;
import org.familysearch.spring.springbootmicrobadge.domain.PersonSummaryRepository;
import org.familysearch.tf.jsonbind.dto.TfPerson;

@Component
public class PersonSummaryService {

  private AtomicLong totalNumCalls = new AtomicLong();
  private AtomicLong totalCacheHits = new AtomicLong();

  @Autowired
  TfPersonClient tfPersonClient;

  @Autowired
  PersonSummaryMapper personSummaryMapper;

  @Autowired
  PersonSummaryRepository repository;

  @Autowired
  @Qualifier("gaugeService")
  GaugeService gaugeService;

  public PersonSummary getOrCalculate(String personId) {
    totalNumCalls.incrementAndGet();

    PersonSummary personSummary;
    PersonSummaryEntity personSummaryEntity = repository.findOne(personId);
    if (personSummaryEntity != null) {
      totalCacheHits.incrementAndGet();
      personSummary = new PersonSummary(personSummaryEntity.getFirstName(), personSummaryEntity.getLastName());
    }
    else {
      TfPerson person = tfPersonClient.getPerson(personId);
      if (person == null) {
        return null;
      }
      personSummary = personSummaryMapper.map(person);
      PersonSummaryEntity entity = new PersonSummaryEntity(personId, personSummary.getFirstName(), personSummary.getLastName());
      repository.save(entity);
    }
    gaugeService.submit("personSummaryCacheHit", (double) totalCacheHits.get() / (double) totalNumCalls.get());
    return personSummary;
  }

  public void deletePersonSummary(String personId) {
    PersonSummaryEntity personSummaryEntity = repository.findOne(personId);
    if (personSummaryEntity != null) {
      repository.delete(personSummaryEntity);
    }
  }
}

package org.familysearch.spring.springbootmicrobadge.domain;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.stereotype.Component;

import org.familysearch.spring.springbootmicrobadge.service.PersonSummary;

@Component
public class PersonSummaryCache implements Cache {

  private static final String PERSON_SUMMARY_CACHE = "personSummaryCache";

  private final AtomicLong hits = new AtomicLong();

  private final AtomicLong misses = new AtomicLong();

  private final PersonSummaryRepository repository;
  private final AmazonDynamoDB amazonDynamoDB;
  private final String dynamoDbTableName;

  public PersonSummaryCache(PersonSummaryRepository repository, AmazonDynamoDB amazonDynamoDB, @Value("${DYNAMODB_TABLE_NAME}") String dynamoDbTableName) {
    this.repository = repository;
    this.amazonDynamoDB = amazonDynamoDB;
    this.dynamoDbTableName = dynamoDbTableName;
  }

  @Override
  public String getName() {
    return PERSON_SUMMARY_CACHE;
  }

  @Override
  public Object getNativeCache() {
    return PERSON_SUMMARY_CACHE;
  }

  @Override
  public ValueWrapper get(Object key) {
    PersonSummaryEntity personSummary = repository.findOne((String) key);

    if (personSummary == null) {
      addCacheMiss();
      return null;
    }
    addCacheHit();
    return new SimpleValueWrapper(new PersonSummary(personSummary.getFirstName(), personSummary.getLastName()));
  }

  @Override
  public <T> T get(Object key, Class<T> aClass) {
    throw new UnsupportedOperationException("Method not implemented");
  }

  @Override
  public <T> T get(Object key, Callable<T> callable) {
    throw new UnsupportedOperationException("Method not implemented");
  }

  @Override
  public void put(Object key, Object value) {
    PersonSummary personSummary = (PersonSummary) value;
    PersonSummaryEntity personSummaryEntity = new PersonSummaryEntity((String) key, personSummary.getFirstName(), personSummary.getLastName());
    repository.save(personSummaryEntity);
  }

  @Override
  public ValueWrapper putIfAbsent(Object key, Object value) {
    put(key ,value);
    PersonSummary personSummary = (PersonSummary) value;
    return new SimpleValueWrapper(new PersonSummary(personSummary.getFirstName(), personSummary.getLastName()));
  }

  @Override
  public void evict(Object key) {
    repository.delete((String) key);
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException("Method not implemented");
  }

  public long getCacheSize() {
    return amazonDynamoDB.describeTable(dynamoDbTableName).getTable().getItemCount();
  }

  public long getCacheHits() {
    return hits.get();
  }

  public long getCacheMisses() {
    return misses.get();
  }

  private void addCacheHit() {
    hits.incrementAndGet();
  }

  private void addCacheMiss() {
    misses.incrementAndGet();
  }

}

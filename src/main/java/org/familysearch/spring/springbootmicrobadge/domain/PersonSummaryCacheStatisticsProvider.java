package org.familysearch.spring.springbootmicrobadge.domain;

import org.springframework.boot.actuate.cache.CacheStatistics;
import org.springframework.boot.actuate.cache.CacheStatisticsProvider;
import org.springframework.boot.actuate.cache.DefaultCacheStatistics;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class PersonSummaryCacheStatisticsProvider implements CacheStatisticsProvider<PersonSummaryCache> {

  @Override
  public CacheStatistics getCacheStatistics(CacheManager cacheManager, PersonSummaryCache personSummaryCache) {
    DefaultCacheStatistics defaultCacheStatistics = new DefaultCacheStatistics();
    defaultCacheStatistics.setSize(personSummaryCache.getCacheSize());
    defaultCacheStatistics.setGetCacheCounts(personSummaryCache.getCacheHits(), personSummaryCache.getCacheMisses());
    return defaultCacheStatistics;
  }
}

package org.familysearch.spring.springbootmicrobadge;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.familysearch.spring.springbootmicrobadge.client.SessionHelper;
import org.familysearch.spring.springbootmicrobadge.client.SessionHelperForTesting;

@Configuration
public class ApplicationTestConfig {

  @Bean
  public SessionHelper sessionHelper() {
    return new SessionHelperForTesting(null);
  }
}

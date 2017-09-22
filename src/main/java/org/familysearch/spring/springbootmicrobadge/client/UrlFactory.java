package org.familysearch.spring.springbootmicrobadge.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlFactory {

  @Value("${url.base}")
  private String baseUrl;

  public String getBaseUrl() {
    return baseUrl;
  }
}

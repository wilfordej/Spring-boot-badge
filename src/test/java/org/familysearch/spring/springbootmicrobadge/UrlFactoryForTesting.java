package org.familysearch.spring.springbootmicrobadge;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.familysearch.spring.springbootmicrobadge.client.UrlFactory;

@Component
public class UrlFactoryForTesting extends UrlFactory {

  @Value("${url.test}")
  private String testUrl;

  @Value("${url.mgmt}")
  private String mgmtUrl;

  public String getTestUrl() {
    return testUrl;
  }

  public String getMgmtUrl() {
    return mgmtUrl;
  }
}

package org.familysearch.spring.springbootmicrobadge;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlFactoryForTesting {

  private final String testUrl;
  private final String mgmtUrl;

  public UrlFactoryForTesting(@Value("${url.test}") String testUrl,
                              @Value("${url.mgmt}") String mgmtUrl) {
    this.testUrl = testUrl;
    this.mgmtUrl = mgmtUrl;
  }

  public String getTestUrl() {
    return testUrl;
  }

  public String getMgmtUrl() {
    return mgmtUrl;
  }
}
